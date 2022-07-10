package com.xlf.constants;

import com.xlf.exception.ErrorCodeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class ApiResult<T> implements Serializable {
    public static final int SUCCESS_CODE = 0;

    private int code;
    private String msg;
    private boolean isSuccess;
    private T data;

    public static <T> ApiResult<T> success() {
        return success(null);
    }

    public static <T> ApiResult<T> success(T data) {
        return ApiResult.newInstant().setCode(SUCCESS_CODE).setMsg("success").setSuccess(true).setData(data);
    }

    public static <T> ApiResult<T> fail() {
        return fail(ErrorCodeEnum.FAIL.getCode());
    }

    public static <T> ApiResult<T> fail(int code) {
        return fail(code, null);
    }

    public static <T> ApiResult<T> fail(T data) {
        return fail(ErrorCodeEnum.FAIL.getCode(), data);
    }

    public static <T> ApiResult<T> fail(int code, T data) {
        return ApiResult.newInstant().setCode(code).setMsg("fail").setSuccess(false).setData(data);
    }

    public static <T> ApiResult<T> success(int code, String message, T data) {
        return ApiResult.newInstant().setCode(code).setMsg(message).setSuccess(true).setData(data);
    }

    public static <T> ApiResult<T> fail(int code, String message, T data) {
        return ApiResult.newInstant().setCode(code).setMsg(message).setSuccess(false).setData(data);
    }

    private static ApiResult newInstant() {
        return new ApiResult<>();
    }
}
