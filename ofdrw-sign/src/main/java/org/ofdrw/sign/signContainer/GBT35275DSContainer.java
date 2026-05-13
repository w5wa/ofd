package org.ofdrw.sign.signContainer;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.gm.GMObjectIdentifiers;
import org.bouncycastle.jcajce.provider.digest.SM3;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.jetbrains.annotations.NotNull;
import org.ofdrw.core.signatures.SigType;
import org.ofdrw.gm.sm2strut.ContentInfo;
import org.ofdrw.gm.sm2strut.OIDs;
import org.ofdrw.gm.sm2strut.SignedData;
import org.ofdrw.gm.sm2strut.builder.SignedDataBuilder;
import org.ofdrw.sign.ExtendSignatureContainer;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.Certificate;

/**
 * according to GM/T 0099-2020 Section 7.2.2 data format requirements
 * <p>
 * b) 签名类型为number签名且签名算法使用SM2时，signature value数据应遵循 GB/T 35275
 *
 * @author Quan Guanyu
 * @since 2021-8-9 16:15:16
 */
public class GBT35275DSContainer implements ExtendSignatureContainer {

    /**
     * signing private key
     */
    private final PrivateKey prvKey;

    /**
     * private key对应public key的certificate
     */
    private final Certificate cert;

    /**
     * 是否需要将文件Hash值进行Base64编码
     * <p>
     * 该参数用于兼容非规范的签名原文被Base64编码的待签名原文
     */
    private boolean enableFileHashBase64;

    /**
     * 创建number签名容器
     * <p>
     * signature value数据应遵循 GB/T 35275
     *
     * @param cert   SM2签名certificate，应符合GB/T 20518
     * @param prvKey private key
     */
    public GBT35275DSContainer(@NotNull Certificate cert, @NotNull PrivateKey prvKey) {
        if (cert == null) {
            throw new IllegalArgumentException("签名使用certificate（cert）不能为空");
        }
        if (prvKey == null) {
            throw new IllegalArgumentException("签名使用private key（prvKey）不能为空");
        }
        this.cert = cert;
        this.prvKey = prvKey;
        this.enableFileHashBase64 = false;
    }

    /**
     * SM3 digest algorithm function
     *
     * @return SM3 digest algorithm function
     */
    @Override
    public MessageDigest getDigestFnc() {
        return new SM3.Digest();
    }

    /**
     * SM2WithSM3
     *
     * @return signature algorithm OID
     */
    @Override
    public ASN1ObjectIdentifier getSignAlgOID() {
        return GMObjectIdentifiers.sm2sign_with_sm3;
    }

    /**
     * sign the data to be signed
     *
     * @param inData       data stream to be signed
     * @param propertyInfo ignored
     * @return signature result value
     * @throws IOException       IO stream read exception
     * @throws SecurityException signature computation exception
     */
    @Override
    public byte[] sign(InputStream inData, String propertyInfo) throws GeneralSecurityException, IOException {
        // calculate original document digest
        MessageDigest md = new SM3.Digest();
        // d) 调用hash algorithm computation签名文件的hash value
        byte[] plaintext = md.digest(IOUtils.toByteArray(inData));
        if (this.enableFileHashBase64) {
            plaintext = Base64.encode(plaintext);
        }

        // e) 根据签名方案，使用操作人签名的private key对hash value进行number签名
        Signature signatureFnc = Signature.getInstance(
                GMObjectIdentifiers.sm2sign_with_sm3.toString(),
                new BouncyCastleProvider());
        signatureFnc.initSign(prvKey);
        signatureFnc.update(plaintext);
        // 执行签名产生signature value
        final byte[] signature = signatureFnc.sign();
        final SignedData signedData = SignedDataBuilder.signedData(plaintext, signature, this.cert);
        ContentInfo contentInfo = new ContentInfo(OIDs.signedData, signedData);
        return contentInfo.getEncoded();
    }

    /**
     * electronic signature does not provide seal
     *
     * @return null
     * @throws IOException IO exception while retrieving seal
     */
    @Override
    public byte[] getSeal() throws IOException {
        return null;
    }

    /**
     * get signature node type
     *
     * @return signature node type
     */
    @Override
    public SigType getSignType() {
        return SigType.Sign;
    }

    /**
     * 是否对文件摘要值进行Base64编码
     * <p>
     * Base64编码后的内容将会为待签名原文被签名
     * <p>
     * 该开关用于兼容部分阅读器只支持签名原文的文件Hash的Base64的情况。
     *
     * @param state 开关，true - 开启、false - 关闭
     */
    public void setEnableFileHashBase64(boolean state) {
        this.enableFileHashBase64 = state;
    }
}
