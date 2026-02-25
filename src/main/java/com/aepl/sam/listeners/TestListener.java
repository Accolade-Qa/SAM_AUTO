package com.aepl.sam.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aepl.sam.base.TestBase;
import com.aepl.sam.utils.PageActionsUtil;
import com.aepl.sam.utils.ExtentManager;
import com.aepl.sam.utils.ExtentTestManager;
import com.aventstack.extentreports.Status;

public class TestListener extends TestBase implements ITestListener {
	PageActionsUtil commonMethod;
	private static final Logger logger = LogManager.getLogger(TestListener.class);

	@Override
	public void onTestStart(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		logger.info("Test started: {}", testName);
		ExtentTestManager.startTest(testName);
		ExtentTestManager.getTest().log(Status.INFO, "Test Started: " + testName);
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		logger.info("Test passed: {}", testName);
		ExtentTestManager.getTest().log(Status.PASS, "Test Passed: " + testName);
	}

	@Override
	public void onTestFailure(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		Throwable throwable = result.getThrowable();

		logger.error("Test failed: {} | Reason: {}", testName,
				(throwable != null ? throwable.getMessage() : "Unknown error"));

		ExtentTestManager.getTest().log(Status.FAIL, "Test Failed: " + testName);
		ExtentTestManager.getTest().log(Status.FAIL,
				"Cause: " + (throwable != null ? throwable.getMessage() : "Unknown"));

		try {
			// Lazy initialize commonMethod if not yet done
			if (commonMethod == null) {
				if (driver == null || wait == null) {
					logger.error("Driver or Wait is null. Cannot capture screenshot.");
				} else {
					commonMethod = new PageActionsUtil(driver, wait);
				}
			}

			if (commonMethod != null) {
				commonMethod.captureScreenshot(testName);
				ExtentTestManager.getTest().log(Status.FAIL, "Screenshot captured for failure");
			} else {
				ExtentTestManager.getTest().log(Status.WARNING,
						"Screenshot not captured due to uninitialized PageActionsUtil.");
			}

		} catch (Exception e) {
			logger.error("Error while capturing screenshot: {}", e.getMessage(), e);
			ExtentTestManager.getTest().log(Status.WARNING, "Failed to capture screenshot: " + e.getMessage());
		}
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		logger.warn("Test skipped: {}", testName);
		ExtentTestManager.getTest().log(Status.SKIP, "Test Skipped: " + testName);
	}

	@Override
	public void onStart(ITestContext context) {
		logger.info("Test suite started: {}", context.getName());
		ExtentManager.createInstance();

		if (driver == null || wait == null) {
			logger.warn("Driver or Wait not initialized at suite start. Will retry later.");
		} else {
			this.commonMethod = new PageActionsUtil(driver, wait);
		}
	}

	@Override
	public void onFinish(ITestContext context) {
		logger.info("Test suite finished: {}", context.getName());
		ExtentTestManager.getTest().log(Status.INFO, "Test Suite Finished: " + context.getName());
		ExtentManager.flush();
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		logger.info("Test failed but within success percentage: {}", result.getMethod().getMethodName());
	}

	@Override
	public void onTestFailedWithTimeout(ITestResult result) {
		logger.error("Test failed due to timeout: {}", result.getMethod().getMethodName());
		onTestFailure(result); // Treat timeout as a failure
	}
}

