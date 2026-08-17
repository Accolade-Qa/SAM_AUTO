package com.aepl.sam.tests.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.aepl.sam.api.AuthApiClient;
import com.aepl.sam.utils.ConfigProperties;
import com.aepl.sam.utils.ExcelUtility;

import io.restassured.response.Response;

@Test(groups = { "api" })
public class AuthApiTest {

	private static final Logger logger = LogManager.getLogger(AuthApiTest.class);
	private AuthApiClient authApiClient;
	private ExcelUtility excelUtility;

	@BeforeClass(alwaysRun = true)
	public void setUpApi() {
		logger.info("Initializing API Test Suite for Auth Service");
		ConfigProperties.initialize("qa");
		authApiClient = new AuthApiClient();
		excelUtility = new ExcelUtility();
		excelUtility.initializeExcel("AuthApiTest");
	}

	@Test(groups = { "api", "sanity" }, description = "Validate /login endpoint accessibility via REST Assured")
	public void testLoginEndpointAccessibility() {
		String testName = "testLoginEndpointAccessibility";
		String expected = "HTTP status code 2xx or 3xx for /login endpoint";
		logger.info("Starting test: {}", testName);

		try {
			Response response = authApiClient.getLoginEndpointStatus();
			int statusCode = response.getStatusCode();
			String actual = "Received HTTP status code: " + statusCode;
			logger.info("Response Status Code for /login endpoint: {}", statusCode);

			boolean success = statusCode >= 200 && statusCode < 400;
			Assert.assertTrue(success,
					"Verify /login API accessibility - Expected 2xx/3xx status code, but received: " + statusCode);

			excelUtility.writeTestDataToExcel(testName, expected, actual, "PASS");
		} catch (AssertionError | Exception e) {
			String failureMsg = "Failed /login API accessibility check: " + e.getMessage();
			excelUtility.writeTestDataToExcel(testName, expected, failureMsg, "FAIL");
			throw e;
		}
	}

	@Test(groups = { "api" }, description = "Validate API response headers and latency for /login")
	public void testLoginResponseHeadersAndLatency() {
		String testName = "testLoginResponseHeadersAndLatency";
		String expected = "API response latency under 5000 ms";
		logger.info("Starting test: {}", testName);

		try {
			Response response = authApiClient.getLoginEndpointStatus();
			long responseTimeMs = response.getTime();
			String actual = "API Response Time: " + responseTimeMs + " ms";
			logger.info(actual);

			boolean success = responseTimeMs < 5000;
			Assert.assertTrue(success,
					"Verify /login API response latency - Expected under 5000 ms, but measured: " + responseTimeMs + " ms");

			excelUtility.writeTestDataToExcel(testName, expected, actual, "PASS");
		} catch (AssertionError | Exception e) {
			String failureMsg = "Failed /login API latency check: " + e.getMessage();
			excelUtility.writeTestDataToExcel(testName, expected, failureMsg, "FAIL");
			throw e;
		}
	}
}
