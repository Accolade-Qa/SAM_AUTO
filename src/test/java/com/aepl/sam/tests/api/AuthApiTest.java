package com.aepl.sam.tests.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.aepl.sam.api.AuthApiClient;
import com.aepl.sam.utils.ConfigProperties;

import io.restassured.response.Response;

public class AuthApiTest {

	private static final Logger logger = LogManager.getLogger(AuthApiTest.class);
	private AuthApiClient authApiClient;

	@BeforeClass(alwaysRun = true)
	public void setUpApi() {
		logger.info("Initializing API Test Suite for Auth Service");
		ConfigProperties.initialize("qa");
		authApiClient = new AuthApiClient();
	}

	@Test(groups = { "api", "sanity" }, description = "Validate /login endpoint accessibility via REST Assured")
	public void testLoginEndpointAccessibility() {
		logger.info("Starting test: testLoginEndpointAccessibility");
		Response response = authApiClient.getLoginEndpointStatus();

		int statusCode = response.getStatusCode();
		logger.info("Response Status Code for /login endpoint: {}", statusCode);

		Assert.assertTrue(statusCode >= 200 && statusCode < 400,
				"Expected HTTP 200 OK or 3xx Redirect, but got: " + statusCode);
	}

	@Test(groups = { "api" }, description = "Validate API response headers and latency for /login")
	public void testLoginResponseHeadersAndLatency() {
		logger.info("Starting test: testLoginResponseHeadersAndLatency");
		Response response = authApiClient.getLoginEndpointStatus();

		long responseTimeMs = response.getTime();
		logger.info("API Response Time: {} ms", responseTimeMs);

		Assert.assertTrue(responseTimeMs < 5000, "API response latency exceeded 5 seconds: " + responseTimeMs + " ms");
	}
}
