package com.aepl.sam.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

public class ExtentTestManager {

    private static final Logger logger = LogManager.getLogger(ExtentTestManager.class);
    private static final Map<Integer, ExtentTest> extentTestMap = new HashMap<>();
    private static final ExtentReports extent = ExtentManager.getInstance();

    public static synchronized ExtentTest startTest(String testName) {
        String threadKey = Thread.currentThread().getName();
        logger.info("Starting test '{}' on thread: {}", testName, threadKey);

        ExtentTest test = extent.createTest(testName);
        extentTestMap.put(threadKey.hashCode() & 0xfffffff, test);

        logger.debug("Test instance stored in extentTestMap for thread: {}", threadKey);
        return test;
    }

    public static synchronized ExtentTest getTest() {
        String threadKey = Thread.currentThread().getName();
        int key = threadKey.hashCode() & 0xfffffff;
        ExtentTest test = extentTestMap.get(key);

        if (test != null) {
            logger.debug("Retrieved ExtentTest for thread: {}", threadKey);
        } else {
            logger.warn("No ExtentTest found for thread: {}", threadKey);
        }

        return test;
    }
}
