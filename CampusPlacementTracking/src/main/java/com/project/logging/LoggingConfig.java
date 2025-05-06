package com.project.logging;

import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.util.StatusPrinter;

@Configuration
public class LoggingConfig {

	static {
		System.out.println("log config is running");
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		context.reset(); // clear default config

		// Pattern encoder
		PatternLayoutEncoder encoder = new PatternLayoutEncoder();
		encoder.setContext(context);
		encoder.setPattern("%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n");
		encoder.start();

		StatusPrinter.print((LoggerContext) LoggerFactory.getILoggerFactory());
		// Console appender
		ch.qos.logback.core.FileAppender<ILoggingEvent> fileAppender = new ch.qos.logback.core.FileAppender<>();
		fileAppender.setContext(context);
		fileAppender.setName("FILE");
		String projectPath = System.getProperty("user.dir");
		System.out.println(projectPath);
		fileAppender.setFile("C:/Users/manoj/Eclipse_Workspace2/springMVC/logs/app.log");
//		fileAppender.setFile(System.getProperty("user.home") + "/app.log");
//		fileAppender.setFile("logs/app.log"); // relative to working dir
		fileAppender.setAppend(true);
		fileAppender.setEncoder(encoder);
		fileAppender.start();

		ch.qos.logback.core.ConsoleAppender<ILoggingEvent> appender = new ch.qos.logback.core.ConsoleAppender<>();
		appender.setContext(context);
		appender.setEncoder(encoder);
		appender.start();

		// Root logger
		Logger rootLogger = context.getLogger(Logger.ROOT_LOGGER_NAME);
		rootLogger.setLevel(Level.DEBUG);
		rootLogger.addAppender(appender);
		rootLogger.addAppender(fileAppender);

		// Custom logger
		Logger myLogger = context.getLogger("springMVC.myApp");
		myLogger.setLevel(Level.DEBUG);
		myLogger.setAdditive(false);
		myLogger.addAppender(appender);
		myLogger.addAppender(fileAppender);
	}
}