package com.aepl.sam.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

public class ExtentTestManager {

    private static final Logger logger = LogManager.getLogger(ExtentTestManager.class);
    private static final ThreadLocal<ExtentTest> extentTestLocal = new ThreadLocal<>();
    private static final ExtentReports extent = ExtentManager.getInstance();

    public static ExtentTest startTest(String testName) {
        String threadKey = Thread.currentThread().getName();
        logger.info("Starting test '{}' on thread: {}", testName, threadKey);

        ExtentTest test = extent.createTest(testName);
        extentTestLocal.set(test);

        logger.debug("Test instance stored in ThreadLocal for thread: {}", threadKey);
        return test;
    }

    public static ExtentTest getTest() {
        String threadKey = Thread.currentThread().getName();
        ExtentTest test = extentTestLocal.get();

        if (test != null) {
            logger.debug("Retrieved ExtentTest for thread: {}", threadKey);
        } else {
            logger.warn("No ExtentTest found for thread: {}", threadKey);
        }

        return test;
    }

    public static void removeTest() {
        String threadKey = Thread.currentThread().getName();
        extentTestLocal.remove();
        logger.debug("Removed ExtentTest from ThreadLocal for thread: {}", threadKey);
    }
}
