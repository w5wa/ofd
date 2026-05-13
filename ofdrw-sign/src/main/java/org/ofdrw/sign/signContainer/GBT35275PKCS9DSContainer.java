package org.ofdrw.sign.signContainer;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.gm.GMObjectIdentifiers;
import org.bouncycastle.jcajce.provider.digest.SM3;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jetbrains.annotations.NotNull;
import org.ofdrw.core.signatures.SigType;
import org.ofdrw.gm.sm2strut.ContentInfo;
import org.ofdrw.gm.sm2strut.OIDs;
import org.ofdrw.gm.sm2strut.SignedData;
import org.ofdrw.gm.sm2strut.builder.PKCS9SignedDataBuilder;
import org.ofdrw.sign.ExtendSignatureContainer;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.Certificate;

/**
 * 根据 GM/T 0099-2020 及 PKCS#7 CMS 签名
 * <p>
 * 注该方法与{@link GBT35275DSContainer} 不同，签名对象为SignInfo中的 authenticatedAttributes字segment，
 * authenticatedAttributes为一个CMS PKCS#9表示含签名时间、原文hash value的键值对结构。
 * <p>
 * 该格式为数科验证兼容格式。
 *
 * @author Quan Guanyu
 * @since 2022-6-24 21:42:59
 */
public class GBT35275PKCS9DSContainer implements ExtendSignatureContainer {

    /**
     * signing private key
     */
    private final PrivateKey prvKey;

    /**
     * private key对应public key的certificate
     */
    private final Certificate cert;

    /**
     * 创建number签名容器
     * <p>
     * signature value数据应遵循 GB/T 35275
     *
     * @param cert   SM2签名certificate，应符合GB/T 20518
     * @param prvKey private key
     */
    public GBT35275PKCS9DSContainer(@NotNull Certificate cert, @NotNull PrivateKey prvKey) {
        if (cert == null) {
            throw new IllegalArgumentException("签名使用certificate（cert）不能为空");
        }
        if (prvKey == null) {
            throw new IllegalArgumentException("签名使用private key（prvKey）不能为空");
        }
        this.cert = cert;
        this.prvKey = prvKey;
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
        // e) 根据签名方案，使用操作人签名的private key对hash value进行number签名
        Signature signatureFnc = Signature.getInstance(
                GMObjectIdentifiers.sm2sign_with_sm3.toString(),
                new BouncyCastleProvider());
        signatureFnc.initSign(prvKey);
        byte[] plaintext = IOUtils.toByteArray(inData);
        // 执行签名产生signature value
        final SignedData signedData = PKCS9SignedDataBuilder.signedData(plaintext, signatureFnc, this.cert, null);
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
}
