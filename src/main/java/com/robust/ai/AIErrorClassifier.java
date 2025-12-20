package com.robust.ai;

public class AIErrorClassifier {

    private AIErrorClassifier() {}

    /**
     * Classify the type of failure based on the exception
     */
    public static AIFailureType classify(Throwable error) {

        if (error == null) {
            return AIFailureType.AUTOMATION; // default fallback
        }

        String message = error.getMessage() != null ? error.getMessage().toLowerCase() : "";

        // Basic classification based on common keywords
        if (message.contains("no such element") ||
            message.contains("element not found") ||
            message.contains("timeout") ||
            message.contains("stale element")) {
            return AIFailureType.UI;
        }

        if (message.contains("http") || 
            message.contains("response") || 
            message.contains("api") || 
            message.contains("connection refused")) {
            return AIFailureType.API;
        }

        // Otherwise, treat as automation/code error
        return AIFailureType.AUTOMATION;
    }
}
