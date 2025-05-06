package com.project.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
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

	@Pointcut("execution(* springMVC.myApp.controller..*(..)) || execution(* springMVC.myApp.service..*(..))")
	public void loggableMethods() {
	}

	@Before("execution(* springMVC.myApp.controller..*(..)) || execution(* springMVC.myApp.service..*(..))")
	public void logBefore(JoinPoint joinPoint) {
		logger.info("Entering method: {} with arguments: {}", joinPoint.getSignature().toShortString(),
				joinPoint.getArgs());
	}

	@AfterReturning(pointcut = "execution(* springMVC.myApp.controller..*(..)) || execution(* springMVC.myApp.service..*(..))", returning = "result")
	public void logAfter(JoinPoint joinPoint, Object result) {
		logger.info("Exiting method: {} with result: {}", joinPoint.getSignature().toShortString(), result);
	}

	@AfterThrowing(pointcut = "execution(* springMVC.myApp.controller..*(..)) || execution(* springMVC.myApp.service..*(..))", throwing = "error")
	public void logError(JoinPoint joinPoint, Throwable error) {
		logger.error("Exception in method: {} with message: {}", joinPoint.getSignature().toShortString(),
				error.getMessage());
	}
}
