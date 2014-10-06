package edu.uci.ics.sdcl.firefly.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.access.PatternLayoutEncoder;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.util.StatusPrinter;
import edu.uci.ics.sdcl.firefly.util.PropertyManager;


public class ApplicationLogger {

	private static Logger logger=null;


	public static Logger initializeSingleton(){
		if(logger == null){
			//logger = LoggerFactory.getLogger(ApplicationLogger.class);

			LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

			FileAppender fileAppender = new FileAppender();
			fileAppender.setContext(loggerContext);
			fileAppender.setName("firefly");
			
			// set the file name
			PropertyManager manager = PropertyManager.initializeSingleton();
			
			fileAppender.setFile(manager.loggerPath+ "/firefly-microtask.log");

			PatternLayoutEncoder encoder = new PatternLayoutEncoder();
			encoder.setContext(loggerContext);
			encoder.setPattern("%r %thread %level - %msg%n");
			encoder.start();

			fileAppender.setEncoder(encoder);
			fileAppender.start();

			// attach the rolling file appender to the logger of your choice
			Logger logbackLogger = loggerContext.getLogger("Main");
			((ch.qos.logback.classic.Logger) logbackLogger).addAppender(fileAppender);

			// OPTIONAL: print logback internal status messages
			StatusPrinter.print(loggerContext);

			// log something
			logbackLogger.debug("hello");
			logger = logbackLogger;
		}
		return logger;

	}

	public void info(String message){
		logger.info(message);
	}

	public void debug(String message){
		logger.debug(message);
	}

	public  void error (String message){
		logger.error(message);
	}

}
