package com.xlf.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * controller log aspect
 */
@Slf4j
@Aspect
@Component
public class ReqRspAspect {


    @Pointcut("execution(public * com..*.web..*.*(..))")
    public void pointCut() {
    }


    @Around("pointCut()")
    public Object aroundAdvice(ProceedingJoinPoint pjp) throws Throwable {
        long startTimestamp = System.currentTimeMillis();
        MethodSignature ms = (MethodSignature) pjp.getSignature();
        Method method = ms.getMethod();
        if (AnnotationUtils.findAnnotation(method, RequestMapping.class) == null) {
            return pjp.proceed();
        }
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        Object[] arguments = pjp.getArgs();
        LogOff logOff = AnnotationUtils.findAnnotation(method, LogOff.class);

        if (Objects.isNull(logOff) || logOff.requestLog()) {
            log.info("ReqRspAspect.aroundAdvice request, method:{},arguments:{}", request.getMethod(), getArguments(arguments));
        }

        Object result = null;
        try {
            result = pjp.proceed();
        } finally {
            long endTimestamp = System.currentTimeMillis();
            long takeTime = endTimestamp - startTimestamp;
            if (Objects.isNull(logOff) || logOff.responseLog()) {
                log.info("ReqRspAspect.aroundAdvice response method:{},result:{} ,cost:{} ms", request.getMethod(), result, takeTime);
            }
        }
        return result;
    }

    private List<Object> getArguments(Object[] arguments) {
        if (Objects.isNull(arguments) || arguments.length <= 0) {
            return new ArrayList<>();
        }
        List<Object> objects = new ArrayList<>(arguments.length);
        for (Object arg : arguments) {
            if (HttpServletRequest.class.isAssignableFrom(arg.getClass())) {
                continue;
            }
            if (HttpServletResponse.class.isAssignableFrom(arg.getClass())) {
                continue;
            }
            if (MultipartFile.class.isAssignableFrom(arg.getClass())) {
                continue;
            }
            objects.add(arg);
        }
        return objects;
    }

}
