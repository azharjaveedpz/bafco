package com.robust.utils;

import java.io.FileInputStream;
import java.util.Properties;



public class ConfigReader {

    private static Properties prop = new Properties();

    static {
        try {
            FileInputStream fis = new FileInputStream(
                    System.getProperty("user.dir") + "/src/test/resources/config.properties");
            prop.load(fis);
        } catch (Exception e) {
            throw new RuntimeException("Unable to load config.properties file!");
        }
    }

    public static String get(String key) {
        return prop.getProperty(key).trim();
    }

    // Get URL based on env
    public static String getURL() {
        String env = get("env").toLowerCase();
        switch (env) {
            case "dev":
                return get("dev_url");
            case "uat":
                return get("uat_url");
            case "staging":
                return get("staging_url");
            case "prod":
                return get("prod_url");
            default:
                return get("uat_url"); 
        }
    }

    // Optional: get browser
    public static String getBrowser() {
        return get("browser");
    }
}
