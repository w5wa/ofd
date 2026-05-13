package org.ofdrw.sign.verify;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.dom4j.DocumentException;
import org.ofdrw.core.basicType.ST_Loc;
import org.ofdrw.core.signatures.SigType;
import org.ofdrw.core.signatures.Signatures;
import org.ofdrw.core.signatures.appearance.Seal;
import org.ofdrw.core.signatures.range.Reference;
import org.ofdrw.core.signatures.range.References;
import org.ofdrw.core.signatures.sig.Signature;
import org.ofdrw.core.signatures.sig.SignedInfo;
import org.ofdrw.gm.ses.parse.SESVersion;
import org.ofdrw.gm.ses.parse.SESVersionHolder;
import org.ofdrw.gm.ses.parse.VersionParser;
import org.ofdrw.gm.ses.v4.SES_Signature;
import org.ofdrw.gm.ses.v4.SESeal;
import org.ofdrw.pkg.container.OFDDir;
import org.ofdrw.reader.BadOFDException;
import org.ofdrw.reader.OFDReader;
import org.ofdrw.reader.ResourceLocator;
import org.ofdrw.sign.verify.exceptions.DocNotSignException;
import org.ofdrw.sign.verify.exceptions.FileIntegrityException;
import org.ofdrw.sign.verify.exceptions.OFDVerifyException;
import org.ofdrw.sign.verify.exceptions.InvalidSignedValueException;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.util.Arrays;
import java.util.List;

/**
 * OFD 电子签名验证引擎
 *
 * @author Quan Guanyu
 * @since 2020-04-22 10:33:26
 */
public class OFDValidator implements Closeable {

    private Provider provider;

    /**
     * OFD虚拟容器
     */
    private OFDDir ofdDir;
    /**
     * OFD parser
     */
    private final OFDReader reader;

    /**
     * resource locator
     * <p>
     * 用于根据路径获取资源
     */
    private final ResourceLocator rl;

    /**
     * electronic seal
     */
    private SignedDataValidateContainer validator;

    /**
     * 创建一个OFD验证引擎
     *
     * @param reader OFD parser
     */
    public OFDValidator(OFDReader reader) {
        this.reader = reader;
        ofdDir = reader.getOFDDir();
        rl = reader.getResourceLocator();
        provider = new BouncyCastleProvider();
    }


    /**
     * 执行OFD电子签名验证
     *
     * @throws OFDVerifyException       验证异常，电子签名失效
     * @throws DocNotSignException      文件未进行电子签名
     * @throws IOException              文件读写过程中IO exception
     * @throws NoSuchAlgorithmException 未知的杂凑算法
     */
    public void exeValidate() throws OFDVerifyException, IOException, GeneralSecurityException {
        rl.save();
        try {
            rl.cd("/");
            // 获取电子签名列表file path
            final ST_Loc signsListLoc = reader.getDefaultDocSignaturesPath();
            if (signsListLoc == null) {
                throw new DocNotSignException("文件未进行电子签名");
            }

            // 获取签名列表
            Signatures sigList = rl.get(signsListLoc, Signatures::new);

            // 切换目录到 签名列表文件所处目录 “/Doc_0/Signs”
            rl.cd(signsListLoc.parent());
            // 获取签名记录列表
            List<org.ofdrw.core.signatures.Signature> signatures = sigList.getSignatures();
            if (signatures == null || signatures.size() == 0) {
                throw new DocNotSignException("文件未进行电子签名");
            }
            for (org.ofdrw.core.signatures.Signature sigRecord : signatures) {
                // 获取seal/signature类型
                SigType type = sigRecord.getType();
                // 获取 Signature.xml file path
                ST_Loc signFileLoc = sigRecord.getBaseLoc();
                Path signatureFilePath = rl.getFile(signFileLoc);
                Signature sig = rl.get(signFileLoc, Signature::new);
                rl.save();
                try {
                    rl.cd(signFileLoc.parent());
                    // 1. 检查文件完整性
                    checkFileIntegrity(sig);

                    // 获取 SignedValue.dat file path
                    Path signedValueFilePath = rl.getFile(sig.getSignedValue());
                    if (type == null || type == SigType.Seal) {
                        Seal seal = sig.getSignedInfo().getSeal();
                        /*
                         * 由于 Seal节点在OFD中是可选节点，即便是电子seal/signature也为可选，
                         * 所以这里只有在该元素存在的情况在进行匹配检查。
                         */
                        if (seal != null) {
                            // 获取电子seal/stamp Seal.esl file path
                            Path sealFilePath = rl.getFile(seal.getBaseLoc());
                            // 2. 检查seal/stamp匹配
                            boolean sealMatch = checkSealMatch(sealFilePath, signedValueFilePath);
                            if (!sealMatch) {
                                throw new GeneralSecurityException("seal/stamp(Seal.esl)与电子seal/signature数据(SignedValue.dat)中的seal/stamp不匹配");
                            }
                        }
                    }
                    // 签名算法名称
                    String alg = sig.getSignedInfo().getSignatureMethod();
                    // 3. 验证电子签名或seal/signature数据
                    checkSignedValue(type, alg, signatureFilePath, signedValueFilePath);
                } finally {
                    rl.restore();
                }
            }
        } catch (DocumentException | FileNotFoundException e) {
            throw new BadOFDException("OFD file内部结构错误，无法解析。", e);
        } finally {
            rl.restore();
        }
    }

    /**
     * 设置用于电子seal/signature数据验证的容器
     *
     * @param validator 验证容器
     * @return this
     */
    public OFDValidator setValidator(SignedDataValidateContainer validator) {
        if (validator == null) {
            throw new IllegalArgumentException("电子seal/signature数据验证容器（validator）为空");
        }
        this.validator = validator;
        return this;
    }

    /**
     * 检查电子seal/signature数据
     *
     * @param type              验证类型 number签名/电子seal/signature
     * @param alg               算法名称
     * @param signatureFilePath 签名文件（Signature.xml）路径
     * @param signedValuePath   signature value文件（SignedValue.dat）路径
     * @throws InvalidSignedValueException 电子seal/signature数据失效
     * @throws IOException                 IO exception
     */
    public void checkSignedValue(SigType type, String alg, Path signatureFilePath, Path signedValuePath) throws IOException, GeneralSecurityException {
        if (validator == null) {
            throw new IllegalArgumentException("电子seal/signature数据验证容器（validator）为空,Call #setValidator");
        }
        if (type == null) {
            type = SigType.Seal;
        }
        validator.validate(type,
                alg,
                Files.readAllBytes(signatureFilePath),
                Files.readAllBytes(signedValuePath));
    }

    /**
     * 检查被保护文件的完整性（是否被篡改）
     *
     * @param sig 签名描述文件的root node对象
     * @throws FileIntegrityException   文件被篡改
     * @throws NoSuchAlgorithmException 杂凑算法不支持
     * @throws IOException              文件读写IO exception
     */
    private void checkFileIntegrity(Signature sig)
            throws FileIntegrityException, NoSuchAlgorithmException, IOException {

        final SignedInfo signedInfo = sig.getSignedInfo();
        final References references = signedInfo.getReferences();
        final String checkMethod = references.getCheckMethod();
        // 根据摘要算法名称获取摘要算法
        MessageDigest md = MessageDigest.getInstance(checkMethod, provider);
        for (Reference ref : references.getReferences()) {
            ST_Loc fileRef = ref.getFileRef();
            Path file = rl.getFile(fileRef);
            // 获取预期的文件hash value
            byte[] expectDataHash = ref.getCheckValue();
            try (InputStream in = Files.newInputStream(file);
                 DigestInputStream dis = new DigestInputStream(in, md)) {
                byte[] buffer = new byte[4096];
                // 根据缓存读入
                while (dis.read(buffer) > -1) ;
                // 计算最终文件hash value
                byte[] actualDataHash = md.digest();
                // 比对hash value是否一致
                if (!Arrays.equals(expectDataHash, actualDataHash)) {
                    throw new FileIntegrityException(fileRef, expectDataHash, actualDataHash);
                }
            }
            // 重置摘要算法
            md.reset();
        }
    }


    /**
     * 电子seal/stamp与电子seal/signature数据的匹配性检查
     *
     * @param sealPath        电子seal/stampfile path
     * @param signedValuePath 电子seal/signature数据路径
     * @return true - 匹配；false - 不匹配
     * @throws IOException        文件stream operation exception
     * @throws OFDVerifyException 未知的电子seal/signature数据版本，无法解析
     */
    private boolean checkSealMatch(Path sealPath, Path signedValuePath) throws IOException, OFDVerifyException {
        final byte[] sesSignatureBin = Files.readAllBytes(signedValuePath);
        byte[] expect = null;
        // 解析电子seal/stamp版本
        SESVersionHolder v = VersionParser.parseSES_SignatureVersion(sesSignatureBin);
        if (v.getVersion() == SESVersion.v4) {
            SES_Signature sesSignature = SES_Signature.getInstance(v.getObjSeq());
            SESeal eseal = sesSignature.getToSign().getEseal();
            expect = eseal.getEncoded("DER");
        } else if (v.getVersion() == SESVersion.v5) {
            org.ofdrw.gm.ses.v5.SES_Signature sesSignature
                    = org.ofdrw.gm.ses.v5.SES_Signature.getInstance(v.getObjSeq());
            org.ofdrw.gm.ses.v5.SESeal eseal = sesSignature.getToSign().getEseal();
            expect = eseal.getEncoded("DER");
        } else if (v.getVersion() == SESVersion.v1) {
            org.ofdrw.gm.ses.v1.SES_Signature sesSignature
                    = org.ofdrw.gm.ses.v1.SES_Signature.getInstance(v.getObjSeq());
            org.ofdrw.gm.ses.v1.SESeal eseal = sesSignature.getToSign().getEseal();
            expect = eseal.getEncoded("DER");
        } else {
            throw new OFDVerifyException("未知的电子seal/signature数据版本，无法解析");
        }
        byte[] sealBin = Files.readAllBytes(sealPath);
        return Arrays.equals(expect, sealBin);
    }


    @Override
    public void close() throws IOException {
        reader.close();
    }
}
