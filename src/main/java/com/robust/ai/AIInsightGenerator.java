package com.robust.ai;


public class AIInsightGenerator {

    private AIInsightGenerator() {}

    public static String generateInsight() {

        int totalFailures = AIMetrics.getTotalFailures();

        if (totalFailures == 0) {
            return "Automation stability looks good. No failures detected.";
        }

        if (AIMetrics.getFlakyCount() > 0) {
            return "Flaky tests detected. Review waits, synchronization, and test data stability.";
        }

        if (AIMetrics.getUiFailures() > AIMetrics.getApiFailures()) {
            return "Majority of failures are UI-related. Improve locator strategy and DOM stability.";
        }

        if (AIMetrics.getApiFailures() > 0) {
            return "API failures detected. Backend validation or contract issues suspected.";
        }

        return "Mixed failure patterns detected. Further investigation is recommended.";
    }
}
