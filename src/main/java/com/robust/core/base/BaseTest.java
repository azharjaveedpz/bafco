package com.robust.core.base;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;


import com.robust.core.drivers.DriverManager;
import com.robust.listeners.TestListener;
import com.robust.utils.ConfigReader;
import com.robust.utils.LoggerUtil;



@Listeners(TestListener.class)
public class BaseTest {

    private static final Logger log = LoggerUtil.getLogger(BaseTest.class);

    @BeforeClass
    public void setUp() {
        log.info("===== Starting Test Class: " + this.getClass().getSimpleName() + " =====");

        // Create browser
        DriverManager.createDriver();

        // Launch URL from config.properties
        String url = ConfigReader.getBaseUrl();
        getDriver().get(url);

        log.info("Navigated to URL: " + url);
    }

    @AfterClass
    public void tearDown() {
        log.info("===== Finished Test Class: " + this.getClass().getSimpleName() + " =====");
        DriverManager.quitDriver();
    }

    protected WebDriver getDriver() {
        return DriverManager.getDriver();
    }

    protected void navigateToURL(String url) {
        getDriver().get(url);
    }

    protected String getPageTitle() {
        return getDriver().getTitle();
    }
}
