package org.ofdrw.crypto.integrity;

import java.io.IOException;
import java.nio.file.Path;
import java.security.GeneralSecurityException;

/**
 * 用于完整性保护协议的签名实现
 *
 * @author Quan Guanyu
 * @since 2021-08-17 19:47:04
 */
@FunctionalInterface
public interface ProtectSigner {
    /**
     * 根据 GM/T 0099  OFD完整性保护协议 执行完整性保护 7.4.3 中的 c) d) 步骤
     * <p>
     * c) based on the signature scheme, calculate the hash value of the integrity protection file;
     * d) based on the signature scheme, use the layout file composer's signing private key to digitally sign the hash value;
     *
     * @param tbs 待签名file path
     * @return signature value应符合“GB/T 35275”标准
     * @throws GeneralSecurityException security computation exception
     * @throws IOException IO read/write exception
     */
    byte[] digestThenSign(Path tbs) throws GeneralSecurityException, IOException;
}
