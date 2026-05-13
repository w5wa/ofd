package org.ofdrw.crypto.integrity;

import java.io.IOException;
import java.nio.file.Path;
import java.security.GeneralSecurityException;

/**
 * 实现该接口 用于实现 GM/T 0099 7.4.6 校验流程
 * <p>
 * 中签名方案验证内容
 *
 * @author Quan Guanyu
 * @since 2021-08-24 19:34:22
 */
@FunctionalInterface
public interface ProtectVerifier {

    /**
     * 根据 GM/T 0099  OFD完整性保护协议 执行完整性保护 7.4.6
     * <p>
     * b) based on the signature scheme, call the hash algorithm to compute the hash of the integrity protection file
     * c) read signature value file and perform signature verification
     *
     * @param ofdEntriesXmlPath 防夹带file path
     * @param signedValue 待验证的signature value，数据结构参照 《GM/T 0099 2020》 7.4.3 密码算法要求
     * @return signature value验证结果
     * @throws GeneralSecurityException security computation exception
     * @throws IOException              IO read/write exception
     */
    boolean digestThenVerify(Path ofdEntriesXmlPath,byte[] signedValue) throws GeneralSecurityException, IOException;
}
