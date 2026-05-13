package org.ofdrw.sign;

/**
 * @author Quan Guanyu
 * @since 2020-04-18 12:04:06
 */
public enum SignMode {
    /**
     * 保护整个文件
     * <p>
     * 该模式下不允许继续签名/章
     */
    WholeProtected,

    /**
     * 继续seal/signature
     * <p>
     * 该模式下回ignored对Signatures.xml的保护
     */
    ContinueSign;
}
