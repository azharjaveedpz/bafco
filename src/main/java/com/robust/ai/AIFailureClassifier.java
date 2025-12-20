package com.robust.ai;




public class AIFailureClassifier {

    public static AIFailureType classify(Throwable error) {
        if (error == null) {
            return AIFailureType.AUTOMATION;
        }

        String msg = error.getClass().getSimpleName().toUpperCase();

        if (msg.contains("ASSERTION")) {
            return AIFailureType.ASSERTION;
        } else if (msg.contains("TIMEOUT")) {
            return AIFailureType.TIMEOUT;
        } else if (msg.contains("NO SUCH ELEMENT") || msg.contains("STALE")) {
            return AIFailureType.UI;
        } else if (msg.contains("HTTP") || msg.contains("API")) {
            return AIFailureType.API;
        } else {
            return AIFailureType.AUTOMATION;
        }
    }
}
