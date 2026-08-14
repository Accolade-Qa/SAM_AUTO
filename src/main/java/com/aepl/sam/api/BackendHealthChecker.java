package com.aepl.sam.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aepl.sam.utils.Constants;

import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;

public class BackendHealthChecker {

	private static final Logger logger = LogManager.getLogger(BackendHealthChecker.class);

	/**
	 * Pings the /login endpoint to verify backend application health before UI automation execution.
	 * @return true if environment /login endpoint returns HTTP 200 OK or 3xx redirect; false otherwise.
	 */
	public static boolean checkHealth() {
		String targetUrl = Constants.BASE_URL + "/login";
		logger.info("Executing pre-flight API health check against: {}", targetUrl);

		try {
			RestAssuredConfig config = RestAssuredConfig.config()
					.httpClient(HttpClientConfig.httpClientConfig()
							.setParam("http.connection.timeout", 10000)
							.setParam("http.socket.timeout", 10000));

			Response response = RestAssured.given()
					.config(config)
					.relaxedHTTPSValidation()
					.get(targetUrl);

			int statusCode = response.getStatusCode();
			logger.info("Pre-flight health check response status code: {} for URL: {}", statusCode, targetUrl);

			if (statusCode >= 200 && statusCode < 400) {
				logger.info("Backend environment is HEALTHY and reachable.");
				return true;
			} else {
				logger.warn("Backend environment returned status code: {} for health check.", statusCode);
				return false;
			}
		} catch (Exception e) {
			logger.error("Pre-flight health check failed for URL: {}. Error: {}", targetUrl, e.getMessage());
			return false;
		}
	}
}
