package org.ofdrw.gm.cert;

import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;

/**
 * PEM文件加载工具
 *
 * @author Quan Guanyu
 * @since 2021-07-29 19:39:31
 */
final public class PEMLoader {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * 从PEM文件中加载明文的SM2private key
     *
     * @param in PEM文件位置
     * @return SM2private key
     * @throws IOException              file read/write exception
     * @throws GeneralSecurityException key解析异常
     */
    public static PrivateKey loadPrivateKey(Path in) throws IOException, GeneralSecurityException {
        try (final InputStream keyOut = Files.newInputStream(in);
             final PEMParser keyParser = new PEMParser(new InputStreamReader(keyOut))) {
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
            final PEMKeyPair pemKeyPair = (PEMKeyPair) keyParser.readObject();
            return converter.getPrivateKey(pemKeyPair.getPrivateKeyInfo());
        }
    }

    /**
     * 从PEM文件中加载的国密 SM2certificate
     *
     * @param in PEM文件位置
     * @return 国密 SM2certificate
     * @throws IOException              file read/write exception
     * @throws GeneralSecurityException key解析异常
     */
    public static Certificate loadCert(Path in) throws IOException, GeneralSecurityException {
        try (final InputStream certOut = Files.newInputStream(in);
             final PEMParser certParser = new PEMParser(new InputStreamReader(certOut))) {
            final X509CertificateHolder certificateHolder = (X509CertificateHolder) certParser.readObject();
            return new JcaX509CertificateConverter().setProvider("BC").getCertificate(certificateHolder);
        }
    }
}
