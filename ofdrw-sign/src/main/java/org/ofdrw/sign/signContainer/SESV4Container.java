package org.ofdrw.sign.signContainer;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.asn1.ASN1BitString;
import org.bouncycastle.asn1.ASN1GeneralizedTime;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.gm.GMObjectIdentifiers;
import org.bouncycastle.jcajce.provider.digest.SM3;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.ofdrw.core.signatures.SigType;
import org.ofdrw.gm.ses.v1.SES_Header;
import org.ofdrw.gm.ses.v4.SES_Signature;
import org.ofdrw.gm.ses.v4.SESeal;
import org.ofdrw.gm.ses.v4.TBS_Sign;
import org.ofdrw.sign.ExtendSignatureContainer;
import org.ofdrw.sign.timestamp.TimeStampHook;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.util.Date;
import java.util.Locale;

/**
 * 《GB/T 38540-2020 信息安全技术 安全电子seal/signature密码技术规范》 电子seal/signature数据生成扩展容器
 * <p>
 * Note: this container is for testing only. For electronic signatures, use devices compliant with national standards and having national type certificates.
 *
 * @author Quan Guanyu
 * @since 2020-04-21 01:22:47
 */
public class SESV4Container implements ExtendSignatureContainer {
    /**
     * 签名使用的private key
     */
    private final PrivateKey privateKey;

    /**
     * electronic seal
     */
    private final SESeal seal;

    /**
     * seal/signature使用的certificate
     */
    private final Certificate certificate;

    /**
     * 时间戳hook对象
     */
    private TimeStampHook timeStampHook;

    /**
     * V1 electronic seal/signature container constructor
     *
     * @param privateKey 签名使用的private key
     * @param seal       电子seal/stamp
     * @param signCert   seal/signatureuser certificate
     */
    public SESV4Container(PrivateKey privateKey, SESeal seal, Certificate signCert) {
        this.privateKey = privateKey;
        this.seal = seal;
        this.certificate = signCert;
    }

    /**
     * V1 electronic seal/signature container constructor
     *
     * @param privateKey    签名使用的private key
     * @param seal          电子seal/stamp
     * @param signCert      seal/signatureuser certificate
     * @param timeStampHook 时间戳Hook
     */
    public SESV4Container(PrivateKey privateKey, SESeal seal, Certificate signCert, TimeStampHook timeStampHook) {
        this.privateKey = privateKey;
        this.seal = seal;
        this.certificate = signCert;
        this.timeStampHook = timeStampHook;
    }

    /**
     * provide file digest algorithm function
     *
     * @return digest algorithm function
     */
    @Override
    public MessageDigest getDigestFnc() {
        return new SM3.Digest();
    }

    /**
     * signature methodOID
     *
     * @return signature algorithm OID
     */
    @Override
    public ASN1ObjectIdentifier getSignAlgOID() {
        return GMObjectIdentifiers.sm2sign_with_sm3;
    }

    /**
     * 设置TimeStampHook
     *
     * @param timeStampHook hook对象
     */
    public void setTimeStampHook(TimeStampHook timeStampHook) {
        this.timeStampHook = timeStampHook;
    }

    /**
     * 对待签名数据进行电子seal/signature
     * <p>
     * 注意：该方法不符合《GM/T 0031-2014 安全电子seal/signature密码技术规范》 流程规范，生成的电子seal/signature
     * 不具有效力，请使用符合国家标准具有型号certificate的设备产生电子seal/signature数据。
     * <p>
     * 该方法只用于测试调试。
     *
     * @param inData       data stream to be signed
     * @param propertyInfo seal/signature attribute information
     * @return signature or seal result value
     * @throws IOException              stream operation exception
     * @throws GeneralSecurityException signature computation exception
     */
    @Override
    public byte[] sign(InputStream inData, String propertyInfo) throws IOException, GeneralSecurityException {

        MessageDigest md = getDigestFnc();
        // 签名原文hash value，也就是Signature.xml 文件的hash value
        byte[] dataHash = md.digest(IOUtils.toByteArray(inData));

        TBS_Sign toSign = new TBS_Sign()
                .setVersion(SES_Header.V4)
                .setEseal(seal)
                .setTimeInfo(new ASN1GeneralizedTime(new Date(), Locale.CHINA))
                .setDataHash(dataHash)
                .setPropertyInfo(propertyInfo);

        Signature sg = Signature.getInstance("SM3WithSM2", new BouncyCastleProvider());
        sg.initSign(privateKey);
        sg.update(toSign.getEncoded("DER"));
        final byte[] sigVal = sg.sign();
        SES_Signature signature = new SES_Signature()
                .setToSign(toSign)
                .setCert(certificate)
                .setSignatureAlgID(GMObjectIdentifiers.sm2sign_with_sm3)
                .setSignature(sigVal);

        if (timeStampHook != null) {
            byte[] timeStamp = timeStampHook.apply(sigVal);
            if (timeStamp != null) {
                signature.setTimeStamp(new DERBitString(timeStamp));
            }
        }

        return signature.getEncoded("DER");
    }

    /**
     * 设置时间戳hook
     *
     * @param timeStampHook 传入得时间戳hook
     */
    public void setTimestampHook(TimeStampHook timeStampHook) {
        this.timeStampHook = timeStampHook;
    }

    /**
     * get electronic seal binary encoding
     * <p>
     * if {@link #getSignType()} returns type {@link SigType#Sign}, return null
     *
     * @return electronic seal binary encoding
     * @throws IOException IO exception while retrieving seal
     */
    @Override
    public byte[] getSeal() throws IOException {
        return seal.getEncoded("DER");
    }

    /**
     * get signature node type
     *
     * @return signature node type
     */
    @Override
    public SigType getSignType() {
        return SigType.Seal;
    }
}
