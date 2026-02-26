package com.aepl.sam.base;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import com.aepl.sam.utils.Constants;
import com.aepl.sam.pages.LoginPage;
import com.aepl.sam.utils.ConfigProperties;
import com.aepl.sam.utils.MouseActions;
import com.aepl.sam.utils.WebDriverFactory;

public class TestBase {

	protected static WebDriver driver;
	protected static WebDriverWait wait;
	protected MouseActions action;
	protected LoginPage loginPage;

	protected final Logger logger = LogManager.getLogger(this.getClass());

	@BeforeClass
	public void setUp() {
		logger.info("========== Test Suite Setup Started ==========");
		if (driver == null) {
			try {
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

				if (!this.getClass().getSimpleName().equals("LoginPageTest")) {
					logger.info("Auto-login initiated for test class: {}", this.getClass().getSimpleName());
					login();
				}

			} catch (Exception e) {
				logger.error("Exception during setup in {}: {}", this.getClass().getSimpleName(), e.getMessage(), e);
				throw e;
			}
		} else {
			logger.warn("Driver is already initialized. Skipping setup.");
		}
		logger.info("========== Test Suite Setup Completed ==========");
	}

	@BeforeMethod
	public void zoomChrome() {
		if (driver != null) {
			logger.debug("Zooming out Chrome browser to 67% for test execution.");
			((JavascriptExecutor) driver).executeScript("document.body.style.zoom='67%'");
		} else {
			logger.warn("Cannot apply zoom - WebDriver instance is null.");
		}
	}

	@AfterSuite
	public void tearDown() {
		logger.info("========== Test Suite Teardown Started ==========");
		if (driver != null) {
			try {
				logger.info("Attempting logout before closing browser.");
				logout();

				driver.quit();
				driver = null;
				logger.info("Browser closed and WebDriver instance reset to null.");
			} catch (Exception e) {
				logger.error("Exception during teardown in {}: {}", this.getClass().getSimpleName(), e.getMessage(), e);
			}
		} else {
			logger.warn("WebDriver is already null; skipping browser closure.");
		}
		logger.info("========== Test Suite Teardown Completed ==========");
	}

	// ------------------ Helper Methods ------------------

	protected void login() {
		try {
			logger.debug("Filling login form with credentials.");
			loginPage.enterUsername(ConfigProperties.getProperty("username"))
					.enterPassword(ConfigProperties.getProperty("password")).clickLogin();

			logger.info("Login successful for user: {}", ConfigProperties.getProperty("username"));
		} catch (Exception e) {
			logger.error("Login failed in {}: {}", this.getClass().getSimpleName(), e.getMessage(), e);
			throw e;
		}
	}

	protected void logout() {
		try {
			logger.debug("Attempting logout action.");
			loginPage.clickLogout();
			logger.info("Logout action completed successfully.");
		} catch (Exception e) {
			logger.error("Logout failed in {}: {}", this.getClass().getSimpleName(), e.getMessage(), e);
			throw e;
		}
	}
}


