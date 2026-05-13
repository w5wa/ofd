package org.ofdrw.reader;

/**
 * 错误路径异常
 *
 * @author Quan Guanyu
 * @since 2020-04-09 20:05:29
 */
public class ErrorPathException extends RuntimeException {
    public ErrorPathException() {
    }

    public ErrorPathException(String message) {
        super(message);
    }

    public ErrorPathException(String message, Throwable cause) {
        super(message, cause);
    }

    public ErrorPathException(Throwable cause) {
        super(cause);
    }
}
