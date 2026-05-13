package org.ofdrw.crypto.enryptor;

import org.bouncycastle.crypto.CryptoException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.ofdrw.core.crypto.ProtectionCaseID;
import org.ofdrw.core.crypto.encryt.UserInfo;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * 用户 文件加密key 加密器 User File Encrypt Key Encryptor
 * <p>
 * 用于加密 文件加密key 生成 {@link org.ofdrw.core.crypto.encryt.UserInfo}
 *
 * @author Quan Guanyu
 * @since 2021-07-13 19:07:11
 */
public interface UserFEKEncryptor {

    /**
     * encrypt file encryption key and encapsulate as
     *
     * @param fek file encryption key (File Encrypt Key)
     * @param iv  encryption initialization vector IV
     * @return user information (including encrypted file encryption key)
     * @throws CryptoException          加密过程运行异常
     * @throws IOException              IO operation exception
     * @throws GeneralSecurityException 加密异常
     */
    UserInfo encrypt(@NotNull byte[] fek, @NotNull byte[] iv) throws CryptoException, IOException, GeneralSecurityException;

    /**
     * certificate used for user encryption; only required in certificate-based encryptors
     *
     * @return certificate file byte content (DER encoded); may return null for password-based encryption
     * @throws GeneralSecurityException certificate编码错误
     */
    byte[] userCert() throws GeneralSecurityException;

    /**
     * encryption protection scheme identifier, see Appendix A.1 {@link ProtectionCaseID}
     *
     * @return encryption protection scheme identifier
     */
    @NotNull
    String encryptCaseId();
}
