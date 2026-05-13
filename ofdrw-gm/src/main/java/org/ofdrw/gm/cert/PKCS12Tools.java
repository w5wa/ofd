package org.ofdrw.gm.cert;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;

/**
 * PKCS12 解析工具
 *
 * @author Quan Guanyu
 * @since 2020-04-21 02:04:24
 */
public class PKCS12Tools {

    /**
     * get private key from P12
     *
     * @param userP12 PKCS12 file path
     * @param pwd     decryption key
     * @return private key
     * @throws GeneralSecurityException encryption/decryption exception
     * @throws IOException              file read exception
     */
    public static PrivateKey ReadPrvKey(Path userP12, String pwd)
            throws GeneralSecurityException, IOException {
        try (InputStream rootKsIn = Files.newInputStream(userP12)) {
            return ReadPrvKey(rootKsIn, pwd);
        }
    }

    /**
     * get private key from P12
     *
     * @param rootKsIn PKCS12 is
     * @param pwd     decryption key
     * @return private key
     * @throws GeneralSecurityException encryption/decryption exception
     * @throws IOException              file read exception
     */
    public static PrivateKey ReadPrvKey(InputStream rootKsIn, String pwd)
            throws GeneralSecurityException, IOException {
        KeyStore ks = KeyStore.getInstance("PKCS12", new BouncyCastleProvider());
        ks.load(rootKsIn, pwd.toCharArray());
        String alias = ks.aliases().nextElement();
        return (PrivateKey) ks.getKey(alias, pwd.toCharArray());

    }

    /**
     * get private key from P12
     *
     * @param userP12 PKCS12 file path
     * @param alias   key store alias
     * @param pwd     decryption key
     * @return private key
     * @throws GeneralSecurityException encryption/decryption exception
     * @throws IOException              file read exception
     */
    public static PrivateKey ReadPrvKey(Path userP12, String alias, String pwd)
            throws GeneralSecurityException, IOException {
        try (InputStream rootKsIn = Files.newInputStream(userP12)) {
            return ReadPrvKey(rootKsIn, alias, pwd);
        }
    }

    /**
     * get private key from P12
     *
     * @param rootKsIn PKCS12 file input stream
     * @param alias   key store alias
     * @param pwd     decryption key
     * @return private key
     * @throws GeneralSecurityException encryption/decryption exception
     * @throws IOException              file read exception
     */
    public static PrivateKey ReadPrvKey(InputStream rootKsIn, String alias, String pwd)
            throws GeneralSecurityException, IOException {
        KeyStore ks = KeyStore.getInstance("PKCS12", new BouncyCastleProvider());
        ks.load(rootKsIn, pwd.toCharArray());
        return (PrivateKey) ks.getKey(alias, pwd.toCharArray());
    }

    /**
     * 从P12中获取certificate链
     *
     * @param rootKsIn PKCS12 file input stream
     * @param pwd     decryption key
     * @return certificate链，第一张certificate为user certificate
     * @throws GeneralSecurityException encryption/decryption exception
     * @throws IOException              file read exception
     */
    public static Certificate[] ReadCertChain(InputStream rootKsIn, String pwd)
            throws GeneralSecurityException, IOException {
        KeyStore ks = KeyStore.getInstance("PKCS12", new BouncyCastleProvider());
        ks.load(rootKsIn, pwd.toCharArray());
        String alias = ks.aliases().nextElement();
        return ks.getCertificateChain(alias);

    }

    /**
     * get user certificate from P12
     *
     * @param userP12 PKCS12 file path
     * @param pwd     decryption key
     * @return user certificate
     * @throws GeneralSecurityException encryption/decryption exception
     * @throws IOException              file read exception
     */
    public static Certificate ReadUserCert(Path userP12, String pwd)
            throws GeneralSecurityException, IOException {
        try (InputStream rootKsIn = Files.newInputStream(userP12)) {
            return ReadUserCert(rootKsIn, pwd);
        }

    }

    /**
     * get user certificate from P12
     *
     * @param rootKsIn PKCS12 file input stream
     * @param pwd     decryption key
     * @return user certificate
     * @throws GeneralSecurityException encryption/decryption exception
     * @throws IOException              file read exception
     */
    public static Certificate ReadUserCert(InputStream rootKsIn, String pwd)
            throws GeneralSecurityException, IOException {
        return ReadCertChain(rootKsIn, pwd)[0];
    }


    /**
     * 从P12中获取certificate链
     *
     * @param rootKsIn PKCS12的is
     * @param alias    key store alias
     * @param pwd      decryption key
     * @return certificate链，第一张certificate为user certificate
     * @throws GeneralSecurityException encryption/decryption exception
     * @throws IOException              file read exception
     */
    public static Certificate[] ReadCertChain(InputStream rootKsIn, String alias, String pwd)
            throws GeneralSecurityException, IOException {
        KeyStore ks = KeyStore.getInstance("PKCS12", new BouncyCastleProvider());
        ks.load(rootKsIn, pwd.toCharArray());
        return ks.getCertificateChain(alias);
    }

    /**
     * get user certificate from P12
     *
     * @param userP12 PKCS12 file path
     * @param alias   key store alias
     * @param pwd     decryption key
     * @return user certificate
     * @throws GeneralSecurityException encryption/decryption exception
     * @throws IOException              file read exception
     */
    public static Certificate ReadUserCert(Path userP12, String alias, String pwd)
            throws GeneralSecurityException, IOException {
        try (InputStream rootKsIn = Files.newInputStream(userP12)) {
            return ReadUserCert(rootKsIn, alias, pwd);
        }

    }

    /**
     * get user certificate from P12
     *
     * @param rootKsIn PKCS12 file input stream
     * @param alias   key store alias
     * @param pwd     decryption key
     * @return user certificate
     * @throws GeneralSecurityException encryption/decryption exception
     * @throws IOException              file read exception
     */
    public static Certificate ReadUserCert(InputStream rootKsIn, String alias, String pwd)
            throws GeneralSecurityException, IOException {
        return ReadCertChain(rootKsIn, alias, pwd)[0];
    }

}
