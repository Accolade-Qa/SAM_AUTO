package com.aepl.sam.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aepl.sam.utils.Constants;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class RestApiSpecFactory {

	private static final Logger logger = LogManager.getLogger(RestApiSpecFactory.class);

	public static RequestSpecification getRequestSpec() {
		return getRequestSpec(Constants.BASE_URL);
	}

	public static RequestSpecification getRequestSpec(String baseUrl) {
		logger.debug("Creating REST Assured RequestSpecification with Base URL: {}", baseUrl);
		return new RequestSpecBuilder()
				.setBaseUri(baseUrl)
				.setContentType(ContentType.JSON)
				.setAccept(ContentType.JSON)
				.setRelaxedHTTPSValidation()
				.build();
	}

	public static ResponseSpecification getResponseSpec(int expectedStatusCode) {
		return new ResponseSpecBuilder()
				.expectStatusCode(expectedStatusCode)
				.build();
	}
}
