package com.eastrobot.security.aescbc.exception;

/**
 * Created by carxiong on 21/11/2018.
 */
public class AESException extends Exception {
    public AESException(String msg) {
        super(msg);
    }

    public AESException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
