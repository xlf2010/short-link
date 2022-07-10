package com.xlf.exception;

import com.xlf.constants.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class ExceptionHandle {

    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ApiResult<Object> handleBindException(BindException e) {
        log.error("handleBindException,", e);
        return ApiResult.fail(e.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public ApiResult<Object> handleBusinessException(BusinessException e) {
        log.error("handleBusinessException,", e);
        return ApiResult.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ApiResult<Object> handleException(Exception e) {
        log.error("handleException,", e);
        return ApiResult.fail(ErrorCodeEnum.FAIL.getCode(), ErrorCodeEnum.FAIL.getMsg());
    }
}
