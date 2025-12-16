package com.robust.utils;

import java.io.FileInputStream;
import java.util.Properties;


public final class ConfigReader {

    private static final Properties prop = new Properties();

    private ConfigReader() {
       
    }

    static {
        try (FileInputStream fis = new FileInputStream(
                System.getProperty("user.dir") + "/src/test/resources/config.properties")) {
            prop.load(fis);
        } catch (Exception e) {
            throw new RuntimeException("Unable to load config.properties file!", e);
        }
    }

    /* ------------------- Generic Getters ------------------- */

    private static String getProperty(String key) {
        String value = prop.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            throw new RuntimeException("Property not found or empty: " + key);
        }
        return value.trim();
    }

    private static String getProperty(String key, String defaultValue) {
        String value = prop.getProperty(key);
        return (value == null || value.trim().isEmpty()) ? defaultValue : value.trim();
    }

    private static int getIntProperty(String key, int defaultValue) {
        try {
            return Integer.parseInt(getProperty(key));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /* ------------------- Environment ------------------- */

    public static String getEnv() {
        return getProperty("env", "uat").toUpperCase();
    }

    public static String getBaseUrl() {
        switch (getEnv().toLowerCase()) {
            case "dev":
                return getProperty("dev_url");
            case "staging":
                return getProperty("staging_url");
            case "prod":
                return getProperty("prod_url");
            case "uat":
            default:
                return getProperty("uat_url");
        }
    }

    /* ------------------- Browser / Execution ------------------- */

    public static String getBrowser() {
        return getProperty("browser", "chrome");
    }

    public static String getBrowserVersion() {
        return getProperty("browserVersion", "latest");
    }

    public static boolean isMobileExecution() {
        return Boolean.parseBoolean(getProperty("mobileBrowser", "false"));
    }

    public static String getExecutionType() {
        return getProperty("executionType", "web");
    }

    /* ------------------- Device / OS ------------------- */

    public static String getDeviceName() {
        return getProperty("deviceName", "Desktop");
    }

    public static String getOSName() {
        return getProperty("osName", System.getProperty("os.name"));
    }

    public static String getOSVersion() {
        return getProperty("osVersion", System.getProperty("os.version"));
    }

    /* ------------------- Timeouts ------------------- */

    public static int getImplicitWait() {
        return getIntProperty("implicitWait", 10);
    }

    public static int getExplicitWait() {
        return getIntProperty("explicitWait", 20);
    }

    public static int getPageLoadTimeout() {
        return getIntProperty("pageLoadTimeout", 30);
    }

    /* ------------------- Retry ------------------- */

    public static int getRetryCount() {
        return getIntProperty("retryCount", 1);
    }
}
