package org.ofdrw.crypto.integrity;

import org.jetbrains.annotations.NotNull;
import org.ofdrw.gm.sm2strut.GBT35275Validate;
import org.ofdrw.gm.sm2strut.VerifyInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;

/**
 * 基于国密算法SM2 和 SM3算法的完整性保护signature value验证
 * <p>
 * 待signature value应符合 《GB/T 35275》标准
 *
 * @author Quan Guanyu
 * @since 2021-08-24 20:12:27
 */
public class GMProtectVerifier implements ProtectVerifier {
    /**
     * 创建 国密算法SM2 和 SM3算法的完整性保护signature value验证者
     */
    public GMProtectVerifier() {
    }

    /**
     * 根据 GM/T 0099  OFD完整性保护协议 执行完整性保护 7.4.6
     * <p>
     * b) based on the signature scheme, call the hash algorithm to compute the hash of the integrity protection file
     * c) read signature value file and perform signature verification
     *
     * @param ofdEntriesXmlPath 防夹带file path
     * @param signedValue       待验证的signature value，数据结构参照 《GM/T 0099 2020》 7.4.3 密码算法要求
     * @return signature value验证结果
     * @throws GeneralSecurityException security computation exception
     * @throws IOException              IO read/write exception
     */
    @Override
    public boolean digestThenVerify(Path ofdEntriesXmlPath, byte[] signedValue) throws GeneralSecurityException, IOException {
        final byte[] tbs = Files.readAllBytes(ofdEntriesXmlPath);
        // 验证签名
        final VerifyInfo verifyInfo = GBT35275Validate.validate("SM3withSm2", tbs, signedValue);
        return verifyInfo.result;
    }
}
