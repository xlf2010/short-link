package com.xlf.exception;

public enum ErrorCodeEnum {

    FAIL(-1, "FAIL"),

    SUCCESS(0, "success"),

    SERVICE_BUSY_ERROR(1000, "service business,please try again later"),

    REQUEST_PARAMS_FAIL(1001, "params error"),

    USER_NOT_LOGIN(1002, "not login"),

    REQUEST_PARAMS_FORMAT_ERROR(1102, "params format error"),

    TOKEN_EXPIRED(2301, "token expire"),

    SHORT_LINK_NOT_EXIST(3301, "short link doesn't exist");;

    private final int code;
    private final String msg;

    ErrorCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public BusinessException newException() {
        return new BusinessException(code, msg);
    }
}