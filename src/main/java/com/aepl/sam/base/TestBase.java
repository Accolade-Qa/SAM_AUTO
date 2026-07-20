package com.aepl.sam.base;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.aepl.sam.pages.LoginPage;
import com.aepl.sam.utils.ConfigProperties;
import com.aepl.sam.utils.Constants;
import com.aepl.sam.utils.MouseActions;
import com.aepl.sam.utils.ThreadQueueManager;
import com.aepl.sam.utils.WebDriverFactory;

public class TestBase {

	protected WebDriver driver;
	protected WebDriverWait wait;
	protected MouseActions action;
	protected LoginPage loginPage;
	private int threadSlotNumber = -1; // Track acquired slot for cleanup

	protected final Logger logger = LogManager.getLogger(this.getClass());

	@BeforeMethod(alwaysRun = true)
	public void setUp() {
		logger.info("========== Test Class Setup Started [{}] ==========", this.getClass().getSimpleName());
		try {
			// STEP 1: Acquire thread slot from queue (controls concurrent execution)
			ThreadQueueManager queueManager = ThreadQueueManager.getInstance();
			try {
				threadSlotNumber = queueManager.acquireSlot();
				logger.info("Thread slot acquired. Queue Status: {}", queueManager.getQueueStats());
			} catch (InterruptedException e) {
				logger.error("Interrupted while waiting for thread slot", e);
				Thread.currentThread().interrupt();
				throw new RuntimeException("Failed to acquire thread slot", e);
			}

			logger.debug("Initializing properties for QA environment.");
			ConfigProperties.initialize("qa");

			String browserType = ConfigProperties.getProperty("browser").toLowerCase();
			logger.info("Browser configured: {}", browserType);

			WebDriverFactory.setDriver(browserType);
			driver = WebDriverFactory.getWebDriver();

			if (driver == null) {
				logger.error("WebDriver creation returned null. Aborting setup.");
				throw new RuntimeException("WebDriver initialization failed.");
			}

			wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			driver.manage().window().maximize();

			logger.debug("Navigating to base URL: {}", Constants.BASE_URL);
			driver.get(Constants.BASE_URL);

			loginPage = new LoginPage(driver, wait);
			logger.info("Successfully navigated to: {}", Constants.BASE_URL);

			if (driver != null) {
				logger.debug("Zooming out Chrome browser to 67% for test execution.");
				((JavascriptExecutor) driver).executeScript("document.body.style.zoom='67%'");
			}

			if (!this.getClass().getSimpleName().equals("LoginPageTest")) {
				logger.info("Auto-login initiated for test class: {}", this.getClass().getSimpleName());
				login();
			}

		} catch (RuntimeException e) {
			logger.error("Exception during setup in {}: {}", this.getClass().getSimpleName(), e.getMessage(), e);
			// Release slot if setup fails
			releaseThreadSlot();
			throw e;
		}
		logger.info("========== Test Class Setup Completed [{}] ==========", this.getClass().getSimpleName());
	}

	@AfterMethod(alwaysRun = true)
	public void tearDownMethod() {
		logger.info("========== Test Method Teardown Started [{}] ==========", this.getClass().getSimpleName());
		try {
			cleanupDriver();
		} finally {
			// ALWAYS release thread slot to allow next thread to execute
			releaseThreadSlot();
			logger.info("========== Test Method Teardown Completed [{}] ==========", this.getClass().getSimpleName());
		}
	}

	// ------------------ Helper Methods ------------------

	protected void login() {
		try {
			logger.debug("Filling login form with credentials.");
			loginPage.enterUsername(ConfigProperties.getProperty("username"))
					.enterPassword(ConfigProperties.getProperty("password")).clickLogin();

			logger.info("Login successful for user: {}", ConfigProperties.getProperty("username"));
		} catch (RuntimeException e) {
			logger.error("Login failed in {}: {}", this.getClass().getSimpleName(), e.getMessage(), e);
			throw e;
		}
	}

	protected void logout() {
		try {
			logger.debug("Attempting logout action.");
			loginPage.clickLogout();
			logger.info("Logout action completed successfully.");
		} catch (RuntimeException e) {
			logger.error("Logout failed in {}: {}", this.getClass().getSimpleName(), e.getMessage(), e);
			throw e;
		}
	}

	protected void cleanupDriver() {
		if (driver != null) {
			try {
				logger.info("Attempting logout before closing browser.");
				logout();
			} catch (RuntimeException e) {
				logger.warn("Logout during cleanup failed: {}", e.getMessage());
			}

			try {
				WebDriverFactory.quitDriver();
			} catch (RuntimeException e) {
				logger.error("Error while quitting WebDriver: {}", e.getMessage(), e);
			} finally {
				driver = null;
				wait = null;
				loginPage = null;
			}
			logger.info("Browser closed and WebDriver instance reset to null.");
		} else {
			logger.warn("WebDriver is already null; skipping browser closure.");
		}
	}

	/**
	 * Release the thread slot acquired from ThreadQueueManager.
	 * This allows the next waiting thread to proceed with execution.
	 */
	private void releaseThreadSlot() {
		if (threadSlotNumber >= 0) {
			try {
				ThreadQueueManager queueManager = ThreadQueueManager.getInstance();
				queueManager.releaseSlot();
				logger.info("Thread slot #{} released. Queue Status: {}", threadSlotNumber,
						queueManager.getQueueStats());
				threadSlotNumber = -1;
			} catch (RuntimeException e) {
				logger.error("Error releasing thread slot: {}", e.getMessage(), e);
			}
		}
	}
}
