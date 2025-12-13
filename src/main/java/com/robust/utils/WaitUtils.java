package com.robust.utils;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitUtils {

    /**
     * Wait until element is visible
     */
    public static WebElement waitForVisible(WebDriver driver, WebElement element, int timeout) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Wait until element is clickable
     */
    public static WebElement waitForClickable(WebDriver driver, WebElement element, int timeout) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Wait until element is present in DOM (for dynamic locators)
     */
    public static WebElement waitForPresence(WebDriver driver, By locator, int timeout) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Wait until URL contains text
     */
    public static boolean waitForUrlContains(WebDriver driver, String partialUrl, int timeout) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.urlContains(partialUrl));
    }

    /**
     * Wait until page title contains text
     */
    public static boolean waitForTitleContains(WebDriver driver, String titlePart, int timeout) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.titleContains(titlePart));
    }
}
