package com.project.logging;

import java.util.logging.Logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
	private static final Logger logger = Logger.getLogger(LoggingAspect.class.getName());

	@AfterReturning(pointcut = "execution(* com.project.service.*.*(..))", returning = "result")
	public void logAfterReturning(JoinPoint joinPoint, Object result) {
		logger.info("Method " + joinPoint.getSignature().getName() + " executed successfully with result: " + result);
	}

	@AfterThrowing(pointcut = "execution(* com.project.service.*.*(..))", throwing = "error")
	public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
		logger.severe("Method " + joinPoint.getSignature().getName() + " threw exception: " + error.getMessage());
	}
}