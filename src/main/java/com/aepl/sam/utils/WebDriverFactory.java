package com.aepl.sam.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class WebDriverFactory {

	private static final Logger logger = LogManager.getLogger(WebDriverFactory.class);

	private static final ThreadLocal<WebDriver> threadLocalDriver = new ThreadLocal<>();

	public static WebDriver getWebDriver() {
		return threadLocalDriver.get();
	}

	public static void setDriver(String browserName) {
		logger.info("Initializing WebDriver for browser: {}", browserName);

		WebDriver driver;
		switch (browserName.toLowerCase()) {
		case "chrome":
			driver = createChromeDriver();
			break;
		case "firefox":
			driver = createFirefoxDriver();
			break;
		case "brave":
			driver = createBraveDriver();
			break;
		default:
			throw new IllegalArgumentException("Unsupported browser: " + browserName);
		}

		threadLocalDriver.set(driver);
		logger.info("Driver instance stored for thread: {}", Thread.currentThread().getName());
	}

	public static void quitDriver() {
		WebDriver driver = threadLocalDriver.get();
		if (driver != null) {
			driver.quit();
			threadLocalDriver.remove();
			logger.info("Driver closed and removed for thread: {}", Thread.currentThread().getName());
		}
	}

	private static WebDriver createChromeDriver() {
		WebDriverManager.chromedriver().setup(); // Automatically matches local Chrome version
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--disable-notifications", "--disable-popup-blocking", "--start-maximized");

		Map<String, Object> prefs = new HashMap<>();
		prefs.put("download.prompt_for_download", false);
		prefs.put("safebrowsing.enabled", true);
		options.setExperimentalOption("prefs", prefs);

		return new ChromeDriver(options);
	}

	private static WebDriver createFirefoxDriver() {
		WebDriverManager.firefoxdriver().setup();
		return new FirefoxDriver();
	}

	private static WebDriver createBraveDriver() {
		WebDriverManager.chromedriver().setup();

		ChromeOptions options = new ChromeOptions();
		options.setBinary("C:\\Program Files\\BraveSoftware\\Brave-Browser\\Application\\brave.exe");

		Map<String, Object> prefs = new HashMap<>();
		prefs.put("profile.default_content_setting_values.notifications", 2); // Disable notifications
		prefs.put("credentials_enable_service", false); // Disable password manager
		prefs.put("profile.password_manager_enabled", false); // Disable save password prompt
		prefs.put("profile.default_content_setting_values.geolocation", 2); // Block location access
		prefs.put("profile.default_content_setting_values.cookies", 1); // Allow cookies (optional)
		prefs.put("profile.default_content_setting_values.popups", 0); // Block popups

		options.setExperimentalOption("prefs", prefs);

		options.addArguments("--start-maximized", "--disable-infobars", "--disable-popup-blocking",
				"--disable-extensions", "--disable-default-apps", "--no-first-run", "--no-service-autorun",
				"--no-default-browser-check", "--disable-component-update", "--disable-background-networking",
				"--disable-blink-features=AutomationControlled",
				"--disable-features=BraveRewards,BraveNews,BraveWallet,PrivacySandboxSettings4");

		options.setExperimentalOption("excludeSwitches", Arrays.asList("enable-automation"));
		options.setExperimentalOption("useAutomationExtension", false);

		options.addArguments("--ignore-certificate-errors");
		options.addArguments("--disable-dev-shm-usage");
		options.addArguments("--no-sandbox");
		options.addArguments("--log-level=3");

		return new ChromeDriver(options);
	}

}
