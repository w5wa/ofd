package org.ofdrw.crypto.enryptor;

import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.engines.SM4Engine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PKCS7Padding;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.jcajce.provider.digest.SM3;
import org.jetbrains.annotations.NotNull;
import org.ofdrw.core.crypto.ProtectionCaseID;
import org.ofdrw.core.crypto.encryt.UserInfo;
import org.ofdrw.gm.support.KDF;

import java.nio.charset.StandardCharsets;

/**
 * OFD user password encryptor
 * <p>
 * key派生规则依据：GB/T 32918.3-2016 信息安全技术 SM2椭圆曲线public key密码算法 第3部分：key交换协议
 * 5.4.3 key派生函数
 *
 * @author Quan Guanyu
 * @since 2021-07-28 18:56:12
 */
public class UserPasswordEncryptor implements UserFEKEncryptor {
    /**
     * 用户名称
     */
    private String username;
    /**
     * 用户角色类型
     * <p>
     * 当时文档管理员时取值为Owner  {@link UserInfo#UserTypeOwner}
     * 普通用户取值为User  {@link UserInfo#UserTypeUser}，默认为User。
     */
    private String userType;

    /**
     * 有key派生函数 KDF派生的加密key
     * <p>
     * 该key用于对 文件加密对称key进行加密
     * <p>
     * KDF函数遵循 《GB/T 32918.3-2016》
     */
    private byte[] fKek;

    /**
     * OFD user password encryptor
     *
     * @param username 用户名称
     * @param userType 用户角色类型 {@link UserInfo#UserTypeOwner}  {@link UserInfo#UserTypeUser}
     * @param password 加密口令
     */
    public UserPasswordEncryptor(@NotNull String username, String userType, @NotNull String password) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("用户名称(username)为空");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("加密口令(password)为空");
        }
        this.username = username;
        this.userType = userType;
        // 扩展口令为加密key
        this.fKek = this.extendKey(password);
    }

    /**
     * OFD user password encryptor
     * <p>
     * 用户类型：User(用户)  {@link UserInfo#UserTypeUser}
     *
     * @param username 用户名称
     * @param password 加密口令
     */
    public UserPasswordEncryptor(@NotNull String username, @NotNull String password) {
        this(username, null, password);
    }

    /**
     * 将口令通过key派生函数生成加密文件加密对称key的key，key派生函数遵循 GB/T 32918
     *
     * @param password 用户输入的口令
     * @return 加密文件加密对称key的key
     */
    private byte[] extendKey(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("加密口令不能为空");
        }
        SM3.Digest h = new SM3.Digest();
        return KDF.extend(h, password.getBytes(StandardCharsets.UTF_8), 16);
    }

    /**
     * encrypt file encryption key and encapsulate as
     *
     * @param fek file encryption key (File Encrypt Key)
     * @param iv  encryption initialization vector IV
     * @return user information (including encrypted file encryption key)
     * @throws CryptoException 加密过程运行异常
     */
    @Override
    public UserInfo encrypt(byte[] fek, byte[] iv) throws CryptoException {
        // 加密 文件加密key
        final PaddedBufferedBlockCipher blockCipher =
                new PaddedBufferedBlockCipher(new CBCBlockCipher(new SM4Engine()), new PKCS7Padding());
        blockCipher.init(true, new ParametersWithIV(new KeyParameter(fKek), iv));
        byte[] buffOut = new byte[64];
        int n = blockCipher.processBytes(fek, 0, fKek.length, buffOut, 0);
        n += blockCipher.doFinal(buffOut, n);

        // 创建 可解密该次操作的用户
        UserInfo userInfo = new UserInfo().setUserName(this.username);
        if (this.userType != null) {
            userInfo.setUserType(this.userType);
        }
        byte[] wk = new byte[n];
        System.arraycopy(buffOut, 0, wk, 0, n);
        userInfo.setIVValue(iv);
        userInfo.setEncryptedWK(wk);
        return userInfo;
    }

    /**
     * certificate used for user encryption; only required in certificate-based encryptors
     *
     * @return certificate file byte content (DER encoded); may return null for password-based encryption
     */
    @Override
    public byte[] userCert() {
        return null;
    }

    /**
     * encryption protection scheme identifier, see Appendix A.1 {@link ProtectionCaseID}
     *
     * @return encryption protection scheme identifier
     */
    @Override
    public @NotNull String encryptCaseId() {
        return ProtectionCaseID.EncryptGMPassword.getId();
    }
}
