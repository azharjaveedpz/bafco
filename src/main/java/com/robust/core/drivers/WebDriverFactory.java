package com.robust.core.drivers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

public class WebDriverFactory {

    public static WebDriver createInstance(String browser) {

        WebDriver driver;

        switch (browser.toLowerCase()) {
            case "safari":
                driver = new SafariDriver(); // macOS only
                break;

            case "chrome":
                driver = new ChromeDriver();
                break;

            case "firefox":
                driver = new FirefoxDriver();
                break;

            case "edge":
                driver = new EdgeDriver();
                break;

            default:
                throw new RuntimeException("Unsupported browser: " + browser);
        }

        driver.manage().window().maximize();
        return driver;
    }
}
