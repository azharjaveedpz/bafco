package com.robust.core.drivers;


import org.openqa.selenium.WebDriver;
import com.robust.utils.ConfigReader;
import com.robust.utils.LoggerUtil;



public class DriverManager {

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static final org.apache.logging.log4j.Logger log = LoggerUtil.getLogger(DriverManager.class);

    public static void createDriver() {

        // Read browser from ConfigReader
        String browser = ConfigReader.getBrowser().toLowerCase();

        log.info("Launching browser: " + browser);

        WebDriver webDriver = WebDriverFactory.createInstance(browser);
        driver.set(webDriver);

        log.info(browser + " driver launched successfully.");
    }

    public static WebDriver getDriver() {
        if (driver.get() == null) {
            log.error("WebDriver is NULL! Did you forget to call createDriver()?");
            throw new RuntimeException("Driver not initialized. Call createDriver() first.");
        }
        return driver.get();
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            log.info("Closing browser...");
            driver.get().quit();
            driver.remove();
            log.info("Browser closed successfully.");
        }
    }
}
