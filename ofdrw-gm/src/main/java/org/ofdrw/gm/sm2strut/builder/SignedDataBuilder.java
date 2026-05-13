package org.ofdrw.gm.sm2strut.builder;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.ofdrw.gm.cert.CertTools;
import org.ofdrw.gm.sm2strut.*;

import java.io.IOException;
import java.security.cert.Certificate;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;


/**
 * signature data type构造器
 * <p>
 * 用于将 BC JCE产生的SM2签名转换为 符合 GBT35275 8 signature data type signedData
 *
 * @author Quan Guanyu
 * @since 2021-08-05 18:17:14
 */
public final class SignedDataBuilder {

    /**
     * assemble signature data type
     *
     * @param plaintext   待签名的原文
     * @param signature   signature value
     * @param certificate 签名使用的certificate
     * @return SignedData
     * @throws GeneralSecurityException certificate parsing exception
     * @throws IOException              IO operation exception
     */
    public static SignedData signedData(@NotNull byte[] plaintext,
                                        @NotNull byte[] signature,
                                        @NotNull Certificate certificate) throws GeneralSecurityException, IOException {
        if (plaintext == null || plaintext.length == 0) {
            throw new IllegalArgumentException("签名原文(plaintext)为空");
        }
        if (signature == null || signature.length == 0) {
            throw new IllegalArgumentException("signature value(signature)为空");
        }
        if (certificate == null) {
            throw new IllegalArgumentException("certificate(certificate)为空");
        }
        ArrayList<CertSigHolder> certSigArr = new ArrayList<>(1);
        certSigArr.add(new CertSigHolder(signature, certificate));
        return signedData(plaintext, certSigArr, null);
    }

    /**
     * assemble signature data type
     *
     * @param plaintext  待签名的原文
     * @param certSigArr certificate和signature value
     * @param extCertArr 额外certificate，可以放置CAcertificate等，可选参数。
     * @return SignedData
     * @throws GeneralSecurityException certificate parsing exception
     * @throws IOException              IO operation exception
     */
    public static SignedData signedData(@NotNull byte[] plaintext,
                                        @NotNull List<CertSigHolder> certSigArr,
                                        List<Certificate> extCertArr)
            throws GeneralSecurityException, IOException {
        if (plaintext == null || plaintext.length == 0) {
            throw new IllegalArgumentException("签名原文(plaintext)为空");
        }
        if (certSigArr == null || certSigArr.isEmpty()) {
            throw new IllegalArgumentException("certificate、signature value列表(signature)为空");
        }
        // 消息摘要算法标识符的集合,固定值 SM3算法
        ASN1Set digestAlgorithms = new DERSet(new AlgorithmIdentifier(OIDs.sm3));
        // 待签名的 数据内容
        ContentInfo contentInfo = new ContentInfo(OIDs.data, new DEROctetString(plaintext));

        int len = certSigArr.size();
        int i = 0;
        if (extCertArr != null) {
            len += extCertArr.size();
        }

        ASN1Encodable[] certArr = new ASN1Encodable[len];
        for (CertSigHolder holder : certSigArr) {
            certArr[i] = holder.getAsn1Cert();
            i++;
        }
        // 如果附加的certificate不为空，那么追加到certificate列表
        if (extCertArr != null && !extCertArr.isEmpty()) {
            for (Certificate c : extCertArr) {
                certArr[i] = CertTools.asn1(c);
                i++;
            }
        }
        ASN1Set certificates = new DERSet(certArr);
        ASN1Encodable[] signers = new ASN1Encodable[certSigArr.size()];
        for (int j = 0; j < certSigArr.size(); j++) {
            CertSigHolder item = certSigArr.get(j);
            // construct signer information
            signers[j] = sm2Signer(item.signature, item.getAsn1Cert());
        }
        ASN1Set signerInfos = new DERSet(signers);
        // 组装对象
        return new SignedData(digestAlgorithms, contentInfo, certificates, signerInfos);
    }

    /**
     * construct signer information
     *
     * @param signature   signature value，值是 SM2Signature的DER，其定义见 GBT 35276-2017 7.3 签名数据格式
     * @param certificate certificate
     * @return signer information
     */
    public static SignerInfo sm2Signer(byte[] signature, org.bouncycastle.asn1.x509.Certificate certificate) {
        IssuerAndSerialNumber issuerAndSerialNumber =
                new IssuerAndSerialNumber(certificate.getIssuer(), certificate.getSerialNumber());
        return new SignerInfo(
                issuerAndSerialNumber,
                new AlgorithmIdentifier(OIDs.sm3), // 固定SM3算法
                new AlgorithmIdentifier(OIDs.sm2Sign), // 固定 SM2椭圆曲线数字签名算法
                new DEROctetString(signature)
        );
    }
}
