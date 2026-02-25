package com.aepl.sam.utils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class FormUtils {
	private static final Logger logger = LogManager.getLogger(FormUtils.class);

	/**
	 * Validates that all input boxes are displayed and enabled
	 *
	 * @param inputBoxes list of WebElements representing input boxes
	 * @return true if all valid, false otherwise
	 */
	public static boolean validateInputBoxes(List<WebElement> inputBoxes) {
		boolean allValid = true;

		if (inputBoxes.isEmpty()) {
			logger.warn("No input boxes found!");
			Assert.fail("No input boxes present on the page.");
			return false;
		}

		for (int i = 0; i < inputBoxes.size(); i++) {
			WebElement input = inputBoxes.get(i);
			boolean displayed = input.isDisplayed();
			boolean enabled = input.isEnabled();
			String key = input.getDomAttribute("formcontrolname");
			if (key == null || key.isEmpty()) {
				key = input.getDomAttribute("placeholder");
			}

			logger.info("Input [{}] - Key: {} | Displayed: {}, Enabled: {}", i + 1, key, displayed, enabled);

			Assert.assertTrue(displayed, "Input box '" + key + "' should be visible.");
			Assert.assertTrue(enabled, "Input box '" + key + "' should be enabled.");

			if (!displayed || !enabled) {
				allValid = false;
				logger.error("Input [{}] '{}' is invalid (displayed: {}, enabled: {})", i + 1, key, displayed, enabled);
			}
		}

		return allValid;
	}

	/**
	 * Validates that all mat-select dropdowns are displayed and enabled
	 *
	 * @param selects list of WebElements representing mat-select elements
	 * @return true if all valid, false otherwise
	 */
	public static boolean validateSelects(List<WebElement> selects) {
		boolean allValid = true;

		if (selects.isEmpty()) {
			logger.warn("No dropdowns found!");
			return true; // Optional: Not failing if no selects
		}

		for (int i = 0; i < selects.size(); i++) {
			WebElement select = selects.get(i);
			boolean displayed = select.isDisplayed();
			boolean enabled = select.isEnabled();
			String key = select.getDomAttribute("formcontrolname");

			logger.info("Select [{}] - Key: {} | Displayed: {}, Enabled: {}", i + 1, key, displayed, enabled);

			Assert.assertTrue(displayed, "Dropdown '" + key + "' should be visible.");
			Assert.assertTrue(enabled, "Dropdown '" + key + "' should be enabled.");

			if (!displayed || !enabled) {
				allValid = false;
				logger.error("Dropdown [{}] '{}' is invalid (displayed: {}, enabled: {})", i + 1, key, displayed,
						enabled);
			}
		}

		return allValid;
	}

	/**
	 * Validates all form fields (inputs + selects) in a container
	 *
	 * @param inputBoxes list of input elements
	 * @param selects    list of select elements
	 * @return true if all fields are valid, false otherwise
	 */
	public static boolean validateForm(List<WebElement> inputBoxes, List<WebElement> selects) {
		boolean inputsValid = validateInputBoxes(inputBoxes);
		boolean selectsValid = validateSelects(selects);

		if (inputsValid && selectsValid) {
			logger.info("✅ All form fields are valid.");
		} else {
			logger.warn("❌ Some form fields are invalid.");
		}

		return inputsValid && selectsValid;
	}

	/**
	 * Validate input fields dynamically against all expected error messages
	 *
	 * @param driver         WebDriver instance
	 * @param container      Form container element
	 * @param fieldValues    Map of field name -> list of invalid test values (one
	 *                       per error scenario)
	 * @param submitButton   Form submit button element
	 * @param expectedErrors Map of field name -> list of expected error messages
	 * @return Map of field name -> list of actual error messages captured
	 */
	public static Map<String, List<String>> validateFormFields(WebDriver driver, WebElement container,
			Map<String, List<String>> fieldValues, // 🔹 Each field can have multiple test inputs
			WebElement submitButton, Map<String, List<String>> expectedErrors) {

		Map<String, List<String>> actualErrors = new HashMap<>();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

		// Iterate over all input boxes in the container
		List<WebElement> inputBoxes = container.findElements(By.cssSelector("input.mat-mdc-input-element"));
		logger.info("Found {} input boxes in the form.", inputBoxes.size());

		for (WebElement inputBox : inputBoxes) {
			try {
				String fieldName = inputBox.getDomAttribute("formcontrolname");
				if (fieldName == null || fieldName.isEmpty()) {
					fieldName = inputBox.getDomAttribute("placeholder");
				}

				// Skip hidden/disabled fields
				if (!inputBox.isDisplayed() || !inputBox.isEnabled()) {
					logger.warn("Skipping field '{}' as it is disabled/hidden", fieldName);
					continue;
				}

				logger.info("🔎 Validating field: {}", fieldName);

				List<String> capturedErrors = new ArrayList<>();

				// Check only if we have expected errors for this field
				if (expectedErrors.containsKey(fieldName) && fieldValues.containsKey(fieldName)) {
					List<String> testInputs = fieldValues.get(fieldName);

					for (int i = 0; i < testInputs.size(); i++) {
						String testValue = testInputs.get(i);

						try {
							// Clear old value
							inputBox.click();
//							inputBox.sendKeys(Keys.CONTROL + "a");
//							inputBox.sendKeys(Keys.DELETE);
							driver.findElement(By.className("//action-button reload-button")).click();

							// Enter new invalid value
							inputBox.sendKeys(testValue);
							logger.info("Entered '{}' in field '{}'", testValue, fieldName);

							Thread.sleep(500);
							// Submit form
							submitButton.click();

							// Find scoped error message inside same field container
//							WebElement fieldContainer = inputBox.findElement(By.xpath("./ancestor::mat-form-field"));
//							WebElement errorEl = wait.until(ExpectedConditions
//									.visibilityOf(fieldContainer.findElement(By.xpath(".//div/mat-error"))));
							WebElement errorEl = wait.until(
									ExpectedConditions.visibilityOf(driver.findElement(By.xpath(".//div/mat-error"))));

							String actualError = errorEl.getText().trim();
							capturedErrors.add(actualError);

							if (expectedErrors.get(fieldName).contains(actualError)) {
								logger.info("✅ Field '{}' produced a valid error: {}", fieldName, actualError);
							} else {
								logger.error("❌ Field '{}' unexpected error. Got: '{}'", fieldName, actualError);
								Assert.fail("Validation mismatch for field '" + fieldName + "' expected one of "
										+ expectedErrors.get(fieldName) + " but found [" + actualError + "]");
							}

						} catch (Exception inner) {
							logger.error("Exception while validating '{}' with test value '{}': {}", fieldName,
									testValue, inner.getMessage());
							capturedErrors.add("Exception: " + inner.getMessage());
						}
					}
				}

				actualErrors.put(fieldName, capturedErrors);

			} catch (Exception e) {
				logger.error("Exception while validating input box: {}", e.getMessage(), e);
				actualErrors.put(inputBox.getDomAttribute("formcontrolname"), List.of("Exception: " + e.getMessage()));
			}
		}

		return actualErrors;
	}

}
