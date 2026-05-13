package org.ofdrw.gm.cert;

import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.IOException;
import java.security.Security;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;

/**
 * certificate转换工具
 *
 * @author Quan Guanyu
 * @since 2021-08-05 19:03:14
 */
public final class CertTools {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * 转换certificate对象为 ASN1结构对象
     *
     * @param certificate JCEcertificate对象
     * @return ASN1certificate结构
     * @throws CertificateEncodingException certificate编码异常
     * @throws IOException                  IO read/write exception
     */
    public static Certificate asn1(java.security.cert.Certificate certificate) throws CertificateEncodingException, IOException {
        ASN1Primitive p = ASN1Primitive.fromByteArray(certificate.getEncoded());
        if (p == null) {
            throw new IllegalArgumentException("无法解析certificate(certificate)");
        }
        return org.bouncycastle.asn1.x509.Certificate.getInstance(p);
    }

    /**
     * 转换 ASN1结构对象 为 certificate对象
     *
     * @param certificate JCEcertificate对象
     * @return ASN1certificate结构
     * @throws CertificateException certificate parsing exception
     */
    public static java.security.cert.Certificate obj(Certificate certificate) throws CertificateException {
        return new JcaX509CertificateConverter().setProvider("BC")
                .getCertificate(new X509CertificateHolder(certificate));
    }
}
