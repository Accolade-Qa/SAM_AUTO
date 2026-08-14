package com.aepl.sam.api;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aepl.sam.utils.Constants;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class AuthApiClient {

	private static final Logger logger = LogManager.getLogger(AuthApiClient.class);

	/**
	 * Sends a POST request to authenticate a user via REST Assured.
	 * 
	 * @param username Login username/email
	 * @param password Login password
	 * @return RestAssured Response object
	 */
	public Response login(String username, String password) {
		logger.info("Executing API login for user: {}", username);

		Map<String, String> loginPayload = new HashMap<>();
		loginPayload.put("UserEmail", username);
		loginPayload.put("password", password);

		return RestAssured.given()
				.spec(RestApiSpecFactory.getRequestSpec())
				.body(loginPayload)
				.when()
				.post("/login")
				.then()
				.extract()
				.response();
	}

	/**
	 * Checks server access to the login endpoint.
	 * 
	 * @return HTTP Response from /login endpoint
	 */
	public Response getLoginEndpointStatus() {
		logger.info("Checking /login endpoint status via REST Assured");
		return RestAssured.given()
				.relaxedHTTPSValidation()
				.accept(ContentType.JSON)
				.when()
				.get(Constants.BASE_URL + "/login")
				.then()
				.extract()
				.response();
	}
}
