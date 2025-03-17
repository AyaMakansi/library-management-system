package com.example.demo.service;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    // Pointcut for all methods inside the service layer
    @Pointcut("execution(* com.example.library.service.*.*(..))")
    public void serviceMethods() {}

    // Log method calls before execution
    @Before("serviceMethods()")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("Method called: {}", joinPoint.getSignature().toShortString());
    }

    // Log successful method execution
    @AfterReturning("serviceMethods()")
    public void logAfterSuccess(JoinPoint joinPoint) {
        logger.info("Method executed successfully: {}", joinPoint.getSignature().toShortString());
    }

    // Log exceptions if any
    @AfterThrowing(pointcut = "serviceMethods()", throwing = "ex")
    public void logException(JoinPoint joinPoint, Exception ex) {
        logger.error("Exception in method {}: {}", joinPoint.getSignature().toShortString(), ex.getMessage());
    }

    // Log execution time
    @Around("serviceMethods()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long elapsedTime = System.currentTimeMillis() - start;
        logger.info("Execution time of {}: {} ms", joinPoint.getSignature().toShortString(), elapsedTime);
        return result;
    }
}

