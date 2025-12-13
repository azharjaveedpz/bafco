package com.robust.utils;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.appender.FileAppender;

public class LoggerUtil {

    private static final ConcurrentHashMap<Class<?>, Logger> loggers = new ConcurrentHashMap<>();

    public static Logger getLogger(Class<?> cls) {

        return loggers.computeIfAbsent(cls, LoggerUtil::createLogger);
    }

    private static Logger createLogger(Class<?> cls) {

        try {
            String basePath = System.getProperty("user.dir") + "/Logs";

            String datePath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
            String logDir = basePath + "/" + datePath;
            new File(logDir).mkdirs();

            String timeStamp = new SimpleDateFormat("HH-mm-ss").format(new Date());
            String logFile = logDir + "/" + cls.getSimpleName() + "_" + timeStamp + ".log";

            LoggerContext context = (LoggerContext) LogManager.getContext(false);
            Configuration config = context.getConfiguration();

            PatternLayout layout = PatternLayout.newBuilder()
                    .withPattern("%d{HH:mm:ss} %-5level %c{1} - %msg%n")
                    .build();

            // FILE appender
            FileAppender fileAppender = FileAppender.newBuilder()
                    .setName(cls.getSimpleName() + "_File")
                    .withFileName(logFile)
                    .withAppend(true)
                    .setLayout(layout)
                    .build();
            fileAppender.start();

            // CONSOLE appender
            ConsoleAppender consoleAppender = ConsoleAppender.newBuilder()
                    .setName(cls.getSimpleName() + "_Console")
                    .setLayout(layout)
                    .build();
            consoleAppender.start();

            config.addAppender(fileAppender);
            config.addAppender(consoleAppender);

            AppenderRef[] refs = new AppenderRef[] {
                    AppenderRef.createAppenderRef(fileAppender.getName(), Level.INFO, null),
                    AppenderRef.createAppenderRef(consoleAppender.getName(), Level.INFO, null)
            };

            LoggerConfig loggerConfig = LoggerConfig.createLogger(
                    false,
                    Level.INFO,
                    cls.getName(),
                    "true",
                    refs,
                    null,
                    config,
                    null
            );

            loggerConfig.addAppender(fileAppender, Level.INFO, null);
            loggerConfig.addAppender(consoleAppender, Level.INFO, null);

            config.addLogger(cls.getName(), loggerConfig);
            context.updateLoggers();

            return LogManager.getLogger(cls);

        } catch (Exception e) {
            e.printStackTrace();
            return LogManager.getLogger(cls);
        }
    }
}
