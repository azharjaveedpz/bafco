package com.robust.core.drivers;

import java.util.logging.Logger;

import org.openqa.selenium.WebDriver;

import com.robust.utils.ConfigReader;
import com.robust.utils.LoggerUtil;

public class DriverManager {

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static final org.apache.logging.log4j.Logger log = LoggerUtil.getLogger(DriverManager.class);

    public static void createDriver() {

        // Read browser from config.properties
        String browser = ConfigReader.get("browser").toLowerCase();

        // Check if Safari is requested
        if (ConfigReader.get("safari_browser").equalsIgnoreCase("true")) {
            browser = "safari";
        }

        log.info("Launching browser: " + browser);

        WebDriver webDriver = WebDriverFactory.createInstance(browser);
        driver.set(webDriver);

        log.info(browser + " driver launched successfully.");
    }

    public static WebDriver getDriver() {
        if (driver.get() == null) {
            log.error("Driver is NULL! Call createDriver() first.");
            throw new RuntimeException("Driver is not initialized.");
        }
        return driver.get();
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            log.info("Quitting driver...");
            driver.get().quit();
            driver.remove();
            log.info("Driver quit successfully.");
        }
    }
}
