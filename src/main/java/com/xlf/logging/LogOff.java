package com.xlf.logging;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogOff {
    boolean requestLog() default false; // 请求日志关闭
    boolean responseLog() default false; // 响应日志关闭
}
