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
        AIMetrics.reset(); // Reset AI metrics for this execution
        suiteLogger.info("Test suite started: " + context.getName());
    }

    // ================== TEST START ==================
    @Override
    public void onTestStart(ITestResult result) {
        String className = result.getTestClass().getRealClass().getSimpleName();
        String methodName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();

        // Create per-test logger
        Logger testLogger = LoggerUtil.getLogger(className + "_" + methodName);
        result.setAttribute("logger", testLogger);

        TestInfo info = result.getMethod()
                .getConstructorOrMethod()
                .getMethod()
                .getAnnotation(TestInfo.class);

        String priority = info != null ? info.priority().name() : "NotDefined";

        String headerHtml = "<table style='width:100%; border-collapse:collapse;'>"
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

        // 🤖 AI tracking
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
            } else {
                log.warn("Driver is null. Screenshot skipped.");
            }
        } catch (Exception e) {
            log.error("Screenshot capture failed", e);
        }

        TestInfo info = result.getMethod()
                .getConstructorOrMethod()
                .getMethod()
                .getAnnotation(TestInfo.class);

        String priority = info != null ? info.priority().name() : "NotDefined";
        String severity = info != null ? info.severity().name() : "NotDefined";

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

        // ===== AI SUMMARY (NON-TEST) =====
        ExtentTest aiSummary = ExtentManager.getExtent()
                .createTest("AI Execution Summary");

        // Mark this as INFO so it won't affect pass/fail stats
        aiSummary.getModel().setStatus(com.aventstack.extentreports.Status.INFO);
       // aiSummary.assignCategory("SUMMARY");

        aiSummary.info("UI Failures: " + AIMetrics.getUiFailures());
        aiSummary.info("API Failures: " + AIMetrics.getApiFailures());
        aiSummary.info("Automation Failures: " + AIMetrics.getAutomationFailures());
        aiSummary.info("Timeout Failures: " + AIMetrics.getTimeoutFailures());
        aiSummary.info("Flaky Tests Detected: " + AIMetrics.getFlakyCount());

        aiSummary.info("AI Insight:");
        aiSummary.info(AIInsightGenerator.generateInsight());

        // ===== FLUSH REPORT =====
        ExtentManager.flushReports();

        suiteLogger.info("Test suite finished: " + context.getName());
    }


    // ================== LOG ATTACHMENT ==================
    private void attachLogFile(ExtentTest test, ITestResult result) {
        String className = result.getTestClass().getRealClass().getSimpleName();
        String methodName = result.getMethod().getMethodName();
        String logPath = LoggerUtil.getLogFilePath(className + "_" + methodName);

        File logFile = new File(logPath);
        if (logFile.exists()) {
            test.info("📄 Execution Log: <a href='file:///" + logFile.getAbsolutePath()
                    + "' target='_blank'>" + logFile.getName() + "</a>");
        } else {
            test.info("📄 Execution Log not found");
        }
    }
}
