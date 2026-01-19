package com.robust.listeners;

import com.aventstack.extentreports.ExtentTest;
import com.robust.ai.AIFailureType;
import com.robust.ai.AIMetrics;
import com.robust.ai.AIFailureClassifier;
import com.robust.ai.AIInsightGenerator;

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

import java.io.File;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.apache.logging.log4j.Logger;

public class TestListener implements ITestListener {

    private static final Logger suiteLogger = LoggerUtil.getLogger(TestListener.class);

    // ================== SUITE START ==================
    @Override
    public void onStart(ITestContext context) {
        ExtentManager.getExtent();
        AIMetrics.reset();
        suiteLogger.info("Test suite started: " + context.getName());
    }

    // ================== TEST START ==================
    @Override
    public void onTestStart(ITestResult result) {

        String className = result.getTestClass().getRealClass().getSimpleName();
        String methodName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();

        // Per-test logger
        Logger testLogger = LoggerUtil.getLogger(className + "_" + methodName);
        result.setAttribute("logger", testLogger);

        // Read @TestInfo
        TestInfo info = result.getMethod()
                .getConstructorOrMethod()
                .getMethod()
                .getAnnotation(TestInfo.class);

        String priority = info != null ? info.priority().name() : "NotDefined";

        // HEADER: Priority + Description (NO severity here)
        String headerHtml = buildHeaderHtml(priority, description, null);

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

        AIMetrics.recordPass(result);

        log.info("Test Passed: " + result.getMethod().getMethodName());
    }

    // ================== TEST FAIL ==================
    @Override
    public void onTestFailure(ITestResult result) {

        ExtentTest test = ExtentManager.getTest();
        Logger log = (Logger) result.getAttribute("logger");

        Throwable error = result.getThrowable();
        String screenshotPath = null;

        try {
            if (DriverManager.getDriver() != null) {
                screenshotPath = ScreenshotUtils.captureScreenshot(
                        result.getMethod().getMethodName()
                );
            }
        } catch (Exception e) {
            log.error("Screenshot capture failed", e);
        }

        //  UPDATE HEADER WITH SEVERITY (ONLY ON FAILURE)
        TestInfo info = result.getMethod()
                .getConstructorOrMethod()
                .getMethod()
                .getAnnotation(TestInfo.class);

        if (info != null) {
            String updatedHeader = buildHeaderHtml(
                    info.priority().name(),
                    result.getMethod().getDescription(),
                    info.severity().name()
            );

            // This updates the HEADER (not INFO)
            test.getModel().setDescription(updatedHeader);
        }

        // 🤖 AI classification
        AIFailureType failureType = AIFailureClassifier.classify(error);
        AIMetrics.recordFailure(failureType, result);

        test.fail("Test Failed (" + failureType + "): " + error.getMessage());

        if (screenshotPath != null) {
            test.addScreenCaptureFromPath(screenshotPath);
        }

        attachLogFile(test, result);

        log.error("Test Failed: " + result.getMethod().getMethodName(), error);
    }

    // ================== SUITE FINISH ==================
    @Override
    public void onFinish(ITestContext context) {

        ExtentTest aiSummary = ExtentManager.getExtent()
                .createTest("AI Execution Summary");

        aiSummary.getModel().setStatus(com.aventstack.extentreports.Status.INFO);

        aiSummary.info("UI Failures: " + AIMetrics.getUiFailures());
        aiSummary.info("API Failures: " + AIMetrics.getApiFailures());
        aiSummary.info("Automation Failures: " + AIMetrics.getAutomationFailures());
        aiSummary.info("Timeout Failures: " + AIMetrics.getTimeoutFailures());
        aiSummary.info("Flaky Tests Detected: " + AIMetrics.getFlakyCount());

        aiSummary.info("AI Insight:");
        aiSummary.info(AIInsightGenerator.generateInsight());

        ExtentManager.flushReports();

        suiteLogger.info("Test suite finished: " + context.getName());
    }

    // ================== HEADER BUILDER ==================
    private String buildHeaderHtml(String priority, String description, String severity) {

        String severityRow = "";

        if (severity != null) {
            severityRow =
                    "<tr>"
                  + "<td style='width:120px; font-weight:bold; color:#b00020;'>Severity:</td>"
                  + "<td style='color:#b00020; font-weight:bold;'>" + severity + "</td>"
                  + "</tr>";
        }

        return "<table style='width:100%; border-collapse:collapse;'>"
             + "<tr>"
             + "<td style='width:120px; font-weight:bold;'>Priority:</td>"
             + "<td>" + priority + "</td>"
             + "</tr>"
             + "<tr>"
             + "<td colspan='2'>" + (description != null ? description : "") + "</td>"
             + "</tr>"
             + severityRow
             + "</table>";
    }

    // ================== LOG ATTACHMENT ==================
    private void attachLogFile(ExtentTest test, ITestResult result) {

        String className = result.getTestClass().getRealClass().getSimpleName();
        String methodName = result.getMethod().getMethodName();
        String logPath = LoggerUtil.getLogFilePath(className + "_" + methodName);

        File logFile = new File(logPath);
        if (logFile.exists()) {
            test.info("Execution Log: <a href='file:///" 
                    + logFile.getAbsolutePath() 
                    + "' target='_blank'>" 
                    + logFile.getName() 
                    + "</a>");
        } else {
            test.info("Execution Log not found");
        }
    }
}
