package org.ofdrw.sign;

/**
 * signature terminated exception
 * <p>
 * 表示该文档不允许在进行签名
 *
 * @author Quan Guanyu
 * @since 2020-04-17 03:10:39
 */
public class SignatureTerminateException extends SignatureException {


    public SignatureTerminateException() {
    }

    public SignatureTerminateException(String s) {
        super(s);
    }

    public SignatureTerminateException(String message, Throwable cause) {
        super(message, cause);
    }
}
