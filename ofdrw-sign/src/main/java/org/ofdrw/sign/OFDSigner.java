package org.ofdrw.sign;


import org.apache.commons.io.FilenameUtils;
import org.dom4j.DocumentException;
import org.ofdrw.core.basicType.ST_Loc;
import org.ofdrw.core.signatures.SigType;
import org.ofdrw.core.signatures.Signatures;
import org.ofdrw.core.signatures.appearance.Seal;
import org.ofdrw.core.signatures.range.Reference;
import org.ofdrw.core.signatures.range.References;
import org.ofdrw.core.signatures.sig.Parameters;
import org.ofdrw.core.signatures.sig.Provider;
import org.ofdrw.core.signatures.sig.Signature;
import org.ofdrw.core.signatures.sig.SignedInfo;
import org.ofdrw.gv.GlobalVar;
import org.ofdrw.pkg.container.OFDDir;
import org.ofdrw.pkg.container.SignDir;
import org.ofdrw.pkg.container.SignsDir;
import org.ofdrw.reader.BadOFDException;
import org.ofdrw.reader.OFDReader;
import org.ofdrw.reader.ResourceLocator;
import org.ofdrw.sign.stamppos.StampAppearance;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.DigestInputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

/**
 * OFD documentnumberseal/signature引擎
 * <p>
 * seal/signature和验证操作均针对于OFD document中的第一个文档
 *
 * @author Quan Guanyu
 * @since 2020-04-17 02:11:56
 */
public class OFDSigner implements Closeable {


    /**
     * 时间日期格式
     */
    public static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * @return OFDRW 签名提供者
     */
    public static Provider OFDRW_Provider() {
        return new Provider()
                .setProviderName("ofdrw-sign")
                .setCompany("ofdrw")
                .setVersion(GlobalVar.Version);
    }

    /**
     * OFD虚拟容器
     */
    private OFDDir ofdDir;
    /**
     * OFD parser
     */
    private OFDReader reader;

    /**
     * 最大signature ID提供者
     */
    private SignIDProvider MaxSignID;

    /**
     * number签名模式
     * <p>
     * 默认：保护整个文档的number签名模式
     */
    private SignMode signMode;

    /**
     * 签名扩展属性
     */
    private Parameters parameters;

    /**
     * 签名列表文件absolute path
     * <p>
     * 为空 - 表示需要更新主入口文件OFD.xml；
     */
    private ST_Loc signaturesLoc;

    /**
     * seal/signature外观列表
     */
    private List<StampAppearance> apList;


    /**
     * 待保护的文件的过滤器
     * <p>
     * 该过滤器会在seal/signature之前调用，它会遍历OFD内的
     * 每一个文件，由过滤器的结果来决定是否需要加入到seal/signature保护范围中去。
     */
    private ProtectFileFilter protectFileFilter;


    /**
     * 签名实现容器
     */
    private ExtendSignatureContainer signContainer;

    /**
     * 电子签名后文件保存位置
     */
    private Path out;

    /**
     * 电子签名后文件输出流
     */
    private OutputStream outStream;

    /**
     * 是否已经执行exeSign
     */
    private boolean hasSign;

    /**
     * 【2.0 版增加】
     * 此签名基于的签名标识符，一旦签名标注的该属性，则验证时应同时验证“基”签名
     */
    private String relativeID = null;


    /**
     * 不允许调用无参数构造器
     */
    private OFDSigner() {
    }

    /**
     * create OFD signature object
     * <p>
     * 默认使用number类型的ID构造器提供电子seal/signature的ID
     *
     * @param reader    OFD parser
     * @param outStream 电子签名后文件保存位置
     * @throws SignatureTerminateException signature terminated exception
     * @since 2022-6-24 23:21:18
     */
    public OFDSigner(OFDReader reader, OutputStream outStream) throws SignatureTerminateException {
        if (reader == null) {
            throw new IllegalArgumentException("OFD parser（reader）为空");
        }
        if (outStream == null) {
            throw new IllegalArgumentException("电子签名后文件输出流（outStream）为空");
        }


        this.outStream = outStream;
        setProperty(reader, new NumberFormatAtomicSignID(false));
    }


    /**
     * create OFD signature object
     *
     * @param reader     OFD parser
     * @param outStream  电子签名后文件保存位置
     * @param idProvider 签名文件ID提供器
     * @throws SignatureTerminateException signature terminated exception
     * @since 2020-08-24 20:35:45
     */
    public OFDSigner(OFDReader reader, OutputStream outStream, SignIDProvider idProvider) throws SignatureTerminateException {
        if (reader == null) {
            throw new IllegalArgumentException("OFD parser（reader）为空");
        }
        if (outStream == null) {
            throw new IllegalArgumentException("电子签名后文件输出流（outStream）为空");
        }
        if (idProvider == null) {
            throw new IllegalArgumentException("签名文件ID提供器（idProvider）为空");
        }

        this.outStream = outStream;
        setProperty(reader, idProvider);
    }

    /**
     * create OFD signature object
     *
     * @param reader     OFD parser
     * @param out        电子签名后文件保存位置
     * @param idProvider 签名文件ID提供器
     * @throws SignatureTerminateException signature terminated exception
     * @since 2020-08-24 20:35:45
     */
    public OFDSigner(OFDReader reader, Path out, SignIDProvider idProvider) throws SignatureTerminateException {
        if (reader == null) {
            throw new IllegalArgumentException("OFD parser（reader）为空");
        }
        if (out == null) {
            throw new IllegalArgumentException("电子签名后文件保存位置（out）为空");
        }
        if (idProvider == null) {
            throw new IllegalArgumentException("签名文件ID提供器（idProvider）为空");
        }

        this.out = out;
        setProperty(reader, idProvider);
    }

    /**
     * 数据初始化
     */
    private void setProperty(OFDReader reader, SignIDProvider idProvider) throws SignatureTerminateException {
        this.reader = reader;
        this.ofdDir = reader.getOFDDir();
        this.hasSign = false;
        // 初始化从0起的最大signature ID，如果源文档中已经存在签名文件的情况
        // 会在preChecker 设置为当前文件最大ID
        this.MaxSignID = idProvider;
        apList = new LinkedList<>();
        // 默认采用 保护整个文档的number签名模式
        signMode = SignMode.WholeProtected;
        signaturesLoc = null;
        // 执行签名预检查
        preChecker();
    }

    /**
     * create OFD signature object
     * <p>
     * 默认使用： s'NNN'格式解析和生成signature ID
     *
     * @param reader OFD parser
     * @param out    电子签名后文件保存位置
     * @throws SignatureTerminateException signature terminated exception
     */
    public OFDSigner(OFDReader reader, Path out) throws SignatureTerminateException {
        this(reader, out, new StandFormatAtomicSignID());
    }

    /**
     * 获取seal/signature模式
     *
     * @return seal/signature模式
     */
    public SignMode getSignMode() {
        return signMode;
    }

    /**
     * 设置seal/signature模式
     *
     * @param signMode seal/signature模式
     * @return this
     */
    public OFDSigner setSignMode(SignMode signMode) {
        if (signMode == null) {
            signMode = SignMode.WholeProtected;
        }
        this.signMode = signMode;
        return this;
    }

    /**
     * 设置电子签名实现容器
     *
     * @param signContainer 实现容器
     * @return this
     */
    public OFDSigner setSignContainer(ExtendSignatureContainer signContainer) {
        if (signContainer == null) {
            throw new IllegalArgumentException("签名实现容器（signContainer）为空");
        }
        this.signContainer = signContainer;
        return this;
    }

    /**
     * 增加seal/signature外观位置
     *
     * @param sa seal/signature外观位置
     * @return this
     */
    public OFDSigner addApPos(StampAppearance sa) {
        if (sa == null) {
            return this;
        }
        this.apList.add(sa);
        return this;
    }

    /**
     * OFD document预检查
     * <p>
     * 1. 是否需要根性OFD.xml。
     * <p>
     * 2. 是否可以继续number签名，如果Signatures.xml被contains到SignInfo中，那么则不能再继续签名。
     *
     * @throws SignatureTerminateException 不允许继续签名
     */
    private void preChecker() throws SignatureTerminateException {
        ResourceLocator rl = reader.getResourceLocator();
        try {
            rl.save();
            rl.cd("/");
            // 获取Doc_0 的签名列表文件位置
            signaturesLoc = reader.getDefaultDocSignaturesPath();
            // 如果OFD.xml 不含有签名列表file path，那么设置需要更新
            // 如果  Signature.xml file not found时候，也需要重新创建
            if (signaturesLoc == null || (!rl.exist(signaturesLoc.toString()))) {
                // 最大signature ID从0起的
                return;
            }
            // 获取签名列表对象
            Signatures signatures = rl.get(signaturesLoc, Signatures::new);

            // 载入文档中已有的最大signature ID
            String maxSignId = signatures.getMaxSignId();
            // 重新设置当前最大signature ID
            this.MaxSignID.setCurrentMaxSignId(maxSignId);

            // 获取签名文件所在路径
            String parent = signaturesLoc.parent();
            // 切换工作路径到签名容器中
            rl.cd(parent);
            List<org.ofdrw.core.signatures.Signature> signatureList = signatures.getSignatures();
            // 遍历所有签名容器，判断保护文件中是否containsSignatures.xml
            for (org.ofdrw.core.signatures.Signature sig : signatureList) {
                ST_Loc baseLoc = sig.getBaseLoc();
                Signature sigObj = rl.get(baseLoc, Signature::new);
                References refList = sigObj.getSignedInfo().getReferences();
                if (refList.hasFile(signaturesLoc.getLoc())) {
                    throw new SignatureTerminateException("签名列表文件（Signatures.xml）已经被保护，文档不允许继续追加签名");
                }
            }
        } catch (FileNotFoundException | DocumentException e) {
            throw new BadOFDException("错误OFD结构和文件格式", e);
        } finally {
            rl.restore();
        }
    }


    /**
     * 获取文档中待杂凑文件流
     *
     * @return 文件信息流
     */
    private List<ToDigestFileInfo> toBeDigestFileList() throws IOException {
        List<ToDigestFileInfo> res = new LinkedList<>();

        // 获取OFD容器在文件系统中的路径
        Path containerPath = ofdDir.getContainerPath();
        // 文件系统中的容器Unix类型absolute path，如："/home/root/tmp"
        String sysRoot = FilenameUtils.separatorsToUnix(containerPath.toAbsolutePath().toString());
        // 遍历OFD file目录中的所有文件
        Files.walkFileTree(containerPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                // convert path to Unix-style absolute path
                String abxFilePath = FilenameUtils.separatorsToUnix(file.toAbsolutePath().toString());
                // replace file system root path, making it an absolute path in the container system
                abxFilePath = abxFilePath.replace(sysRoot, "");
                final ToDigestFileInfo fileInfo = new ToDigestFileInfo(abxFilePath, file);
                // 执行文件的过滤行为通过过滤器选择出需要保护的文档。
                try {
                    if (protectFileFilter != null && !protectFileFilter.filter(fileInfo.getAbsPath())) {
                        return FileVisitResult.CONTINUE;
                    }
                } catch (Exception ignore) {
                }
                // 如果采用继续seal/signature模式，那么跳过对 Signatures.xml 的文件
                if (signMode == SignMode.ContinueSign
                        && abxFilePath.equals(signaturesLoc.getLoc())) {
                    return FileVisitResult.CONTINUE;
                }
                // 构造加入文件信息列表
                res.add(fileInfo);
                return FileVisitResult.CONTINUE;
            }
        });
        return res;
    }


    /**
     * 签名或seal/signature执行器
     * <p>
     * 1. 构造签名列表。
     * <p>
     * 2. 计算保护文件hash value，设置seal/signature显示位置、seal/stamp，构造签名文件。
     * <p>
     * 3. 计算signature value。
     *
     * @return Signatures 列表对象
     * @throws BadOFDException          文件解析失败，或file not found
     * @throws IOException              签名和文件读写过程中的IO exception
     * @throws GeneralSecurityException 签名异常
     */
    public Signatures exeSign() throws IOException, GeneralSecurityException {
        if (signContainer == null) {
            throw new IllegalArgumentException("签名实现容器（signContainer）为空，请提供签名实现容器");
        }
        hasSign = true;
        // 获取number签名存储目录
        SignsDir signsDir = ofdDir.obtainDocDefault().obtainSigns();
        // 创建签名容器
        SignDir signDir = signsDir.newSignDir();

        /*
         * 1. 获取签名列表文件对象
         *
         * 先尝试获取已经存在的 签名列表文件 Signatures.xml
         * 根据需要可能需要更新OFD.xml
         */
        Signatures signListObj = reader.getDefaultSignatures();
        if (signaturesLoc == null || signListObj == null) {
            signListObj = new Signatures();
            signsDir.setSignatures(signListObj);

            // 构造签名列表file path
            signaturesLoc = signsDir.getAbsLoc()
                    .cat(SignsDir.SignaturesFileName);
            // 设置OFD.xml 的签名列表文件入口
            try {
                ofdDir.getOfd().getDocBody().setSignatures(signaturesLoc);
                // 将更新了的OFD.xml更新到文件系统中
                ofdDir.flushFileByName(OFDDir.OFDFileName);
            } catch (DocumentException e) {
                throw new BadOFDException("OFD.xml 文件解析失败");
            }
        }

        /*
         * 2. 向签名列表文件中加入number签名记录
         *
         * 如果签名列表file not found那么创建，如果已经存在那么更新到文件系统
         */
        // 签名文件
        ST_Loc signatureLoc = signDir.getAbsLoc().cat(SignDir.SignatureFileName);
        // 构造列表文件中的签名记录
        final org.ofdrw.core.signatures.Signature signatureRecord = new org.ofdrw.core.signatures.Signature()
                // 设置ID
                .setID(MaxSignID.incrementAndGet())
                // 设置number签名类型
                .setType(signContainer.getSignType())
                // 设置签名文件位置
                .setBaseLoc(signatureLoc);
        if (this.relativeID != null && this.relativeID.trim().length() > 0) {
            signatureRecord.setRelative(this.relativeID);
        }
        // 放入签名列表中
        signListObj.addSignature(signatureRecord);
        /*
         * 3. 构建签名文件对象
         *
         * - 设置算法
         * - 设置提供者
         * - 计算保护文件摘要值
         * - 签名文件构造
         */
        Path signatureFilePath = buildSignature(signsDir, signDir, signListObj);
        /*
         * 4. 计算number签名获取signature value
         */
        // 设置seal/signature原文的保护信息为：签名文件容器中absolute path。
        String propertyInfo = signDir.getAbsLoc().cat(SignDir.SignatureFileName).toString();
        // 调用容器提供方法计算seal/signature值。
        byte[] signedValue;
        try (InputStream inData = Files.newInputStream(signatureFilePath)) {
            signedValue = signContainer.sign(inData, propertyInfo);
        }
        Path signedValuePath = Paths.get(signDir.getSysAbsPath(), SignDir.SignedValueFileName);
        // 将signature value写入到 SignedValue.dat中
        Files.write(signedValuePath, signedValue);
        return signListObj;
    }

    /**
     * 构造一个签名文件
     * <p>
     * 并写入到签名容器中
     *
     * @param signsDir    签名容器
     * @param signDir     签名资源容器
     * @param signListObj 签名列表描述对象
     * @return 签名文件文件系统路径
     * @throws SignatureException 签名异常
     * @throws IOException        文件读写IO operation exception
     */
    private Path buildSignature(SignsDir signsDir,
                                SignDir signDir,
                                Signatures signListObj) throws IOException, SignatureException {
        // 构造签名信息
        SignedInfo signedInfo = new SignedInfo()
                // 设置签名模块提供者信息
                .setProvider(OFDRW_Provider())
                // 设置signature method
                .setSignatureMethod(signContainer.getSignAlgOID())
                // 设置签名扩展属性
                .setParameters(parameters)
                // 设置签名时间
                .setSignatureDateTime(DF.format(LocalDateTime.now()));

        // 如果是电子seal/signature，那么设置电子seal/stamp
        final ST_Loc signDirAbsLoc = signDir.getAbsLoc();
        if (signContainer.getSignType() == SigType.Seal) {
            // 获取电子seal/stamp二进制字节
            byte[] sealBin = signContainer.getSeal();
            // 由于电子seal/stamp参数为可选参数，这里移除非空检查
            if (sealBin != null && sealBin.length != 0) {
                Path sealPath = Paths.get(signDir.getSysAbsPath(), SignDir.SealFileName);
                // 将电子seal/stamp写入文件
                Files.write(sealPath, sealBin);
                // 构造seal/stamp信息
                Seal seal = new Seal().setBaseLoc(signDirAbsLoc.cat(SignDir.SealFileName));
                signedInfo.setSeal(seal);
            }
        }

        // 加入签名关联的外观
        if (!apList.isEmpty()) {
            for (StampAppearance sa : apList) {
                // 解析除外观注解然后加入签名信息中
                sa.getAppearance(reader, MaxSignID).forEach(signedInfo::addStampAnnot);
            }
        }

        /*
         * 结束了所有需要分配的signature ID
         *
         * 写入了除了signature value文件之外的所有文件
         *
         * - 设置签名列表描述对象的最大ID
         * - 将签名列表文件更新到文件系统
         */
        signListObj.setMaxSignId(MaxSignID.get());
        signsDir.flushFileByName(SignsDir.SignaturesFileName);

        /*
         * 计算并设置所保护的所有文件的摘要
         */
        MessageDigest md = signContainer.getDigestFnc();
        References references = new References()
                // 设置摘要方法
                .setCheckMethod(md.getAlgorithm());
        // 获取要被保护的文件信息序列
        List<ToDigestFileInfo> toDigestFileInfos = toBeDigestFileList();
        for (ToDigestFileInfo fileInfo : toDigestFileInfos) {
            // 计算文件hash value
            byte[] digest = calculateFileDigest(md, fileInfo.getSysPath());
            // 重置杂凑函数
            md.reset();
            Reference ref = new Reference()
                    .setFileRef(fileInfo.getAbsPath())
                    .setCheckValue(digest);
            references.addReference(ref);
        }
        // 设置摘要列表，完成"签名要保护的原文及本次签名相关的信息"的构造
        signedInfo.setReferences(references);

        /*
         * 完成 签名描述文件的root node 构造
         *
         * 序列化为文件写入到文件系统
         */
        Signature signature = new Signature()
                // 设置签名数据文件位置
                .setSignedValue(signDirAbsLoc.cat(SignDir.SignedValueFileName))
                .setSignedInfo(signedInfo);
        signDir.setSignature(signature);
        // 将签名描述file root node写入到文件系统中
        signDir.flushFileByName(SignDir.SignatureFileName);
        // 获取写入文件的操作系统路径
        return Paths.get(signDir.getSysAbsPath(), SignDir.SignatureFileName);
    }

    /**
     * 使用多次读取计算文件hash value
     * <p>
     * 减少内存使用
     *
     * @param md   杂凑计算函数
     * @param path file path
     * @return hash value
     * @throws IOException IO read/write exception
     */
    private byte[] calculateFileDigest(MessageDigest md, Path path) throws IOException {
        try (InputStream in = Files.newInputStream(path);
             DigestInputStream dis = new DigestInputStream(in, md)) {
            byte[] buffer = new byte[4096];
            // 根据缓存读入
            while (dis.read(buffer) > -1) ;
            // 计算最终文件hash value
            return md.digest();
        }
    }

    /**
     * 进行签名/章
     * <p>
     * 然后关闭文档
     *
     * @throws IOException 打包文件过程中IO exception
     */
    @Override
    public void close() throws IOException {
        if (!hasSign) {
            throw new IllegalStateException("请先执行 exeSign在关闭引擎完成number签名。");
        }
        // 打包电子签名后的OFD file
        if (out != null) {
            ofdDir.jar(out);
        } else if (outStream != null) {
            ofdDir.jar(outStream);
        } else {
            throw new IllegalArgumentException("OFD documentoutput directory错误或没有设置输出流");
        }
        // 关闭OFD parser
        reader.close();
    }

    /**
     * 设置 文件过滤器
     * <p>
     * 通过过滤器来实现选择需要保护的文件
     *
     * @param filter 过滤器
     * @return this
     */
    public OFDSigner setProtectFileFilter(ProtectFileFilter filter) {
        this.protectFileFilter = filter;
        return this;
    }

    /**
     * [optional, OFD 2.0]
     * 设置 此签名基于的签名标识符
     * <p>
     * 一旦签名标注的该属性，则验证时应同时验证“基”签名
     *
     * @param id “基”signature ID
     * @return this
     */
    public OFDSigner setRelative(String id) {
        this.relativeID = id;
        return this;
    }

    /**
     * 设置签名扩展属性
     *
     * @param parameters 扩展属性
     * @return this
     */
    public OFDSigner setParameters(Parameters parameters) {
        this.parameters = parameters;
        return this;
    }
}
