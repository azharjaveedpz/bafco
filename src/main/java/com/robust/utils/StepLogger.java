package com.robust.utils;

import org.apache.logging.log4j.Logger;
import com.aventstack.extentreports.Status;
import com.robust.reports.ExtentManager;

public class StepLogger {

    public static void step(Logger log, String step, String expected, String actual) {

        // 🖥 Console
        System.out.println("STEP: " + step);
        System.out.println("EXPECTED: " + expected);
        System.out.println("ACTUAL: " + actual);

        // 📄 Logger file
        log.info("STEP: " + step);
        log.info("EXPECTED: " + expected);
        log.info("ACTUAL: " + actual);

        // 📊 Extent Report
        ExtentManager.getTest().log(Status.INFO,
                "<b>Step:</b> " + step +
                "<br><b>Expected:</b> " + expected +
                "<br><b>Actual:</b> " + actual);
    }
}
