package org.ofdrw.layout.exception;

import java.io.IOException;

/**
 * OFDfile parsing exception
 *
 * @author Quan Guanyu
 * @since 2020-04-11 17:03:52
 */
public class DocReadException extends IOException {
    public DocReadException() {
    }

    public DocReadException(String message) {
        super(message);
    }

    public DocReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
