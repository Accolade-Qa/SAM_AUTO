package com.aepl.sam.listeners;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aepl.sam.base.TestBase;
import com.aepl.sam.utils.ExtentManager;
import com.aepl.sam.utils.ExtentTestManager;
import com.aepl.sam.utils.PageActionsUtil;
import com.aepl.sam.utils.WebDriverFactory;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.ExtentTest;

public class TestListener extends TestBase implements ITestListener {
	private static final Logger logger = LogManager.getLogger(TestListener.class);

	@Override
	public void onTestStart(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		logger.info("Test started: {}", testName);
		ExtentTestManager.startTest(testName);

		ExtentTest test = ExtentTestManager.getTest();
		if (test != null) {
			test.log(Status.INFO, "Test Started: " + testName);
		}
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		logger.info("Test passed: {}", testName);

		ExtentTest test = ExtentTestManager.getTest();
		if (test != null) {
			test.log(Status.PASS, "Test Passed: " + testName);
		}
	}

	@Override
	public void onTestFailure(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		Throwable throwable = result.getThrowable();

		logger.error("Test failed: {} | Reason: {}", testName,
				(throwable != null ? throwable.getMessage() : "Unknown error"));

		ExtentTest test = ExtentTestManager.getTest();
		if (test != null) {
			test.log(Status.FAIL, "Test Failed: " + testName);
			test.log(Status.FAIL, "Cause: " + (throwable != null ? throwable.getMessage() : "Unknown"));
		}

		try {
			WebDriver currentDriver = WebDriverFactory.getWebDriver();
			if (currentDriver == null) {
				logger.error("Current WebDriver is null. Cannot capture screenshot.");
				if (test != null) {
					test.log(Status.WARNING, "Screenshot not captured: driver is null.");
				}
				return;
			}

			WebDriverWait currentWait = new WebDriverWait(currentDriver, Duration.ofSeconds(10));
			new PageActionsUtil(currentDriver, currentWait).captureScreenshot(testName);
			if (test != null) {
				test.log(Status.FAIL, "Screenshot captured for failure");
			}

		} catch (Exception e) {
			logger.error("Error while capturing screenshot: {}", e.getMessage(), e);
			if (test != null) {
				test.log(Status.WARNING, "Failed to capture screenshot: " + e.getMessage());
			}
		}
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		logger.warn("Test skipped: {}", testName);

		ExtentTest test = ExtentTestManager.getTest();
		if (test != null) {
			test.log(Status.SKIP, "Test Skipped: " + testName);
		}
	}

	@Override
	public void onStart(ITestContext context) {
		logger.info("Test suite started: {}", context.getName());
		ExtentManager.createInstance();

		logger.info("Ensuring Extent report is ready.");
	}

	@Override
	public void onFinish(ITestContext context) {
		logger.info("Test suite finished: {}", context.getName());

		// Note: onFinish() runs on main thread, not test thread
		// So ExtentTestManager.getTest() will return null here
		// Do not try to log to ExtentTest - just flush the report

		try {
			ExtentManager.flush();
			logger.info("Test report flushed successfully.");
		} catch (Exception e) {
			logger.error("Error flushing test report: {}", e.getMessage(), e);
		}
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
