package com.robust.ai;


import org.testng.ITestResult;



public class AIMetrics {

    private static int uiFailures = 0;
    private static int apiFailures = 0;
    private static int automationFailures = 0;
    private static int timeoutFailures = 0;
    private static int flakyTests = 0;
    private static int totalFailures = 0;

    public static void reset() {
        uiFailures = 0;
        apiFailures = 0;
        automationFailures = 0;
        timeoutFailures = 0;
        flakyTests = 0;
        totalFailures = 0;
    }

    public static void recordFailure(AIFailureType type, ITestResult result) {
        totalFailures++;
        switch (type) {
            case UI: uiFailures++; break;
            case API: apiFailures++; break;
            case AUTOMATION: automationFailures++; break;
            case TIMEOUT: timeoutFailures++; break;
            case ASSERTION: automationFailures++; break;
        }
    }

    public static void recordPass(ITestResult result) {
        // Optional: track passes for AI insight
    }

    public static void recordSkipped(ITestResult result) {
        // Optional: track skipped
    }

    // Getters
    public static int getUiFailures() { return uiFailures; }
    public static int getApiFailures() { return apiFailures; }
    public static int getAutomationFailures() { return automationFailures; }
    public static int getTimeoutFailures() { return timeoutFailures; }
    public static int getFlakyCount() { return flakyTests; }
    public static int getTotalFailures() { return totalFailures; }
}
