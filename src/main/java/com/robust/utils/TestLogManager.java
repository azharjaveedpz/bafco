package com.robust.utils;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestLogManager {

    private static final ThreadLocal<Logger> testLogger = new ThreadLocal<>();

    public static void startTestLogger(String testName) {
        Logger logger = LogManager.getLogger(testName);
        testLogger.set(logger);
    }

    public static Logger getLogger() {
        return testLogger.get();
    }

    public static void endTestLogger() {
        testLogger.remove();
    }
}
