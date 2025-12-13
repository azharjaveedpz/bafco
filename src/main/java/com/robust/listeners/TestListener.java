package com.robust.listeners;

import com.aventstack.extentreports.ExtentTest;
import com.robust.reports.ExtentManager;
import com.robust.utils.LoggerUtil;
import com.robust.utils.ScreenshotUtils;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    private static final Logger log = LoggerUtil.getLogger(TestListener.class);

    @Override
    public void onStart(ITestContext context) {
        ExtentManager.getExtent();
        log.info("Test suite started: " + context.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {

        String className =
                result.getTestClass().getRealClass().getSimpleName();

        String methodName =
                result.getMethod().getMethodName();

        String description =
                result.getMethod().getDescription();

        //  Test name → Class.Method
        ExtentTest test =
                ExtentManager.getExtent()
                        .createTest(className + "." + methodName);

        // Category → Class
        test.assignCategory(className);

        //  Description shown clearly in report
        if (description != null && !description.isEmpty()) {
            test.info("📝 Description: " + description);
        }

        ExtentManager.setTest(test);

        log.info("▶ TEST STARTED: " + className + "." + methodName
                + (description != null ? " | " + description : ""));
    }






    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentManager.getTest().pass("Test Passed");
        log.info("Test Passed: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {

        Throwable error = result.getThrowable();

        //  Screenshot
        String screenshotPath =
                ScreenshotUtils.captureScreenshot(result.getMethod().getMethodName());

        //  EXTENT → full message
        ExtentManager.getTest()
                .fail("Test Failed: " + error.getMessage())
                .addScreenCaptureFromPath(screenshotPath);

        //  FILE LOG → message only (NO stack trace)
        log.error("Test Failed: " + result.getMethod().getMethodName()
                + " | Reason: " + error.getMessage());

        //  CONSOLE → FULL STACK TRACE
        error.printStackTrace();
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentManager.flushReports();
        log.info("Test suite finished: " + context.getName());
    }
}
