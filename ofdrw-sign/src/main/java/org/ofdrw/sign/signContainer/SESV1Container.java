package org.ofdrw.sign.signContainer;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.asn1.*;
import org.bouncycastle.asn1.gm.GMObjectIdentifiers;
import org.bouncycastle.jcajce.provider.digest.SM3;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.ofdrw.core.signatures.SigType;
import org.ofdrw.gm.ses.v1.SES_Signature;
import org.ofdrw.gm.ses.v1.SESeal;
import org.ofdrw.gm.ses.v1.TBS_Sign;
import org.ofdrw.sign.ExtendSignatureContainer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.Certificate;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

/**
 * 《GM/T 0031-2014 安全电子seal/signature密码技术规范》 电子seal/signature数据生成扩展容器
 * <p>
 * Note: this container is for testing only. For electronic signatures, use devices compliant with national standards and having national type certificates.
 *
 * @author Quan Guanyu
 * @since 2020-04-21 01:22:47
 */
public class SESV1Container implements ExtendSignatureContainer {
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
     * V1 electronic seal/signature container constructor
     *
     * @param privateKey 签名使用的private key
     * @param seal       电子seal/stamp
     * @param signCert   seal/signatureuser certificate
     */
    public SESV1Container(PrivateKey privateKey, SESeal seal, Certificate signCert) {
        this.privateKey = privateKey;
        this.seal = seal;
        this.certificate = signCert;
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
        byte[] digest = md.digest(IOUtils.toByteArray(inData));

        // 签名时间
        byte[] signUTCTime = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                .getBytes(StandardCharsets.UTF_8);
//        ASN1UTCTime signUTCTime = new ASN1UTCTime(new Date(), Locale.CHINA);
        TBS_Sign tbsSign = new TBS_Sign()
                .setVersion(new ASN1Integer(1))
                .setEseal(seal)
                .setTimeInfo(new DERBitString(signUTCTime))
                .setDataHash(new DERBitString(digest))
                .setPropertyInfo(new DERIA5String(propertyInfo))
                .setCert(new DEROctetString(certificate.getEncoded()))
                .setSignatureAlgorithm(GMObjectIdentifiers.sm2sign_with_sm3);
        Signature signature = Signature.getInstance("SM3withSm2", new BouncyCastleProvider());
        signature.initSign(privateKey);
        signature.update(tbsSign.getEncoded("DER"));
        byte[] sign = signature.sign();
        SES_Signature sesSignature = new SES_Signature(tbsSign, new DERBitString(sign));
        return sesSignature.getEncoded("DER");
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
