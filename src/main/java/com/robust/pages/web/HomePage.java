package com.robust.pages.web;


import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.aventstack.extentreports.ExtentTest;
import com.robust.core.base.BaseTest;
import com.robust.reports.ExtentManager;
import com.robust.utils.LoggerUtil;
import com.robust.utils.StepLogger;
import com.robust.utils.WaitUtils;





public class HomePage {
    private static final Logger log = LoggerUtil.getLogger(BaseTest.class);

    private WebDriver driver;

    // ===========================
    // Constructor
    // ===========================
    public HomePage(WebDriver driver) { 
        this.driver = driver; 
        PageFactory.initElements(driver, this);
    }


    // ===========================
    // Web Elements
    // ===========================
    @FindBy(id = "search-input")
    private WebElement searchBox;

    @FindBy(css = "button.search-btn")
    private WebElement searchButton;

    @FindBy(xpath = "//a[normalize-space()='Contact the Experts']")
    private WebElement contactbutton;

    // ===========================
    // Page Actions
    // ===========================
    
    
    ExtentTest test = ExtentManager.getTest();

    public HomePage enterContactExpertsButton() {

     
        test.info("STEP: Click on 'Contact the Experts' button");
        test.info("EXPECTED: User should navigate to Contact Experts page");

        contactbutton.click();

        test.info("ACTUAL: Page title after click: " + driver.getTitle());

        return this;
    }




    // ===========================
    // Validations
    // ===========================
    public boolean isHomePageLoaded() {
        try {
            return WaitUtils.waitForVisible(driver, contactbutton, 10).isDisplayed();
        } catch (Exception e) {
            throw new AssertionError("Home Page NOT loaded: Contact button not visible", e);
        }
    }

}
