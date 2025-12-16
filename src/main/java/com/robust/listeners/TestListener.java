package com.robust.listeners;

import com.aventstack.extentreports.ExtentTest;
import com.robust.annotations.TestInfo;
import com.robust.core.drivers.DriverManager;
import com.robust.reports.ExtentManager;
import com.robust.utils.LoggerUtil;
import com.robust.utils.ScreenshotUtils;

import java.io.File;

import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    private static final Logger log = LoggerUtil.getLogger(TestListener.class);

    // ================== SUITE START ==================
    @Override
    public void onStart(ITestContext context) {
        ExtentManager.getExtent();
        log.info("Test suite started: " + context.getName());
    }

    // ================== TEST START ==================
    @Override
    public void onTestStart(ITestResult result) {

        String className = result.getTestClass().getRealClass().getSimpleName();
        String methodName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();

        // Create per-test logger
        String logFileName = className + "_" + methodName;
        Logger testLogger = LoggerUtil.getLogger(logFileName);
        result.setAttribute("logger", testLogger); // Save logger for later

        TestInfo info = result.getMethod()
                .getConstructorOrMethod()
                .getMethod()
                .getAnnotation(TestInfo.class);

        String priority = info != null ? info.priority().name() : "NotDefined";

        // Header WITHOUT severity (only on fail)
        String headerHtml =
                "<table style='width:100%; border-collapse:collapse;'>"
              + "<tr>"
              + "<td style='width:120px; font-weight:bold;'>Priority: " + priority + "</td>"
              + "<td>" + (description != null ? description : "") + "</td>"
              + "</tr>"
              + "</table>";

        ExtentTest test = ExtentManager.getExtent()
                .createTest(className + "." + methodName, headerHtml);

        test.assignCategory(className);
        ExtentManager.setTest(test);

        testLogger.info("Test Started: " + className + "." + methodName);
    }

    // ================== TEST PASS ==================
    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTest test = ExtentManager.getTest();
        Logger log = (Logger) result.getAttribute("logger");

        test.pass("Test Passed");
        attachLogFile(test, result);

        log.info("Test Passed: " + result.getMethod().getMethodName());
    }

    // ================== TEST FAIL ==================
    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = ExtentManager.getTest();
        Logger log = (Logger) result.getAttribute("logger");

        Throwable error = result.getThrowable();
        String screenshotPath = ScreenshotUtils.captureScreenshot(result.getMethod().getMethodName());

        TestInfo info = result.getMethod()
                .getConstructorOrMethod()
                .getMethod()
                .getAnnotation(TestInfo.class);

        String priority = info != null ? info.priority().name() : "NotDefined";
        String severity = info != null ? info.severity().name() : "NotDefined";

        // Update header for failed test
        String updatedHeader =
                "<table style='width:100%; border-collapse:collapse;'>"
              + "<tr>"
              + "<td style='width:120px; font-weight:bold;'>Priority: " + priority + "</td>"
              + "<td style='width:160px; font-weight:bold; color:red;'>Severity: " + severity + "</td>"
              + "<td>" + result.getMethod().getDescription() + "</td>"
              + "</tr>"
              + "</table>";

        test.getModel().setDescription(updatedHeader);

        test.fail("Test Failed: " + error.getMessage())
            .addScreenCaptureFromPath(screenshotPath);

        attachLogFile(test, result);

        log.error("Test Failed: " + result.getMethod().getMethodName()
                + " | Priority: " + priority
                + " | Severity: " + severity
                + " | Reason: " + error.getMessage(), error);

        DriverManager.quitDriver();
    }

    // ================== TEST SKIPPED ==================
    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest test = ExtentManager.getTest();
        Logger log = (Logger) result.getAttribute("logger");

        test.skip("Test Skipped");
        attachLogFile(test, result);

        log.warn("Test Skipped: " + result.getMethod().getMethodName());
    }

    // ================== SUITE FINISH ==================
    @Override
    public void onFinish(ITestContext context) {
        ExtentManager.flushReports();
        log.info("Test suite finished: " + context.getName());
    }

    // ================== LOG ATTACHMENT ==================
    private void attachLogFile(ExtentTest test, ITestResult result) {
        String className = result.getTestClass().getRealClass().getSimpleName();
        String methodName = result.getMethod().getMethodName();
        String logPath = LoggerUtil.getLogFilePath(className + "_" + methodName);

        File logFile = new File(logPath);
        if (logFile.exists()) {
            // clickable link in report
            test.info("📄 Execution Log: <a href='file:///" + logFile.getAbsolutePath() +
                      "' target='_blank'>" + logFile.getName() + "</a>");
        } else {
            test.info("📄 Execution Log not found");
        }
    }
}
