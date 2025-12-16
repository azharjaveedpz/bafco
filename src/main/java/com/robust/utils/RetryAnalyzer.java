package com.robust.utils;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;




public class RetryAnalyzer implements IRetryAnalyzer {

    private int retryCount = 0;
    private final int maxRetryCount;

    public RetryAnalyzer() {
        // Fetch retry count from config.properties
        maxRetryCount = ConfigReader.getRetryCount();
    }

    @Override
    public boolean retry(ITestResult result) {

        if (retryCount < maxRetryCount) {
            retryCount++;
            return true;
        }
        return false;
    }
}
