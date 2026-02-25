package com.aepl.sam.pages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.aepl.sam.locators.DealersManagementLocators;
import com.aepl.sam.utils.FormUtils;
import com.aepl.sam.utils.PageActionsUtil;
import com.aepl.sam.utils.RandomGeneratorUtils;
import com.aepl.sam.utils.TableUtils;

public class DealersManagementPage extends DealersManagementLocators {
	private WebDriver driver;
	private WebDriverWait wait;
	private PageActionsUtil comm;
	private final Logger logger = LogManager.getLogger(this.getClass().getSimpleName());

	public DealersManagementPage(WebDriver driver, WebDriverWait wait) {
		this.driver = driver;
		this.wait = wait;
		this.comm = new PageActionsUtil(driver, wait);
	}

	public Boolean navBarLink() {
		boolean isViewed = false;
		try {
			logger.info("Navigating to Dealer Management page...");
			WebElement dealer = wait.until(ExpectedConditions.visibilityOfElementLocated(DEALER));
			comm.highlightElement(dealer, "violet");
			logger.debug("Dealer nav element text: {}", dealer.getText().trim());
			Assert.assertTrue(dealer.getText().trim().equals("DEALERS UTILITY"), "Dealer nav link text mismatch");
			dealer.click();
			logger.info("Clicked on Dealer nav link.");

			WebElement dealer_management_link = wait
					.until(ExpectedConditions.visibilityOfElementLocated(DEALER_MANAGEMENT));
			comm.highlightElement(dealer_management_link, "violet");
			dealer_management_link.click();
			logger.info("Clicked on Dealer Management link.");

			wait.until(ExpectedConditions.urlToBe("http://aepltest.accoladeelectronics.com:6102/dealers-management"));
			isViewed = driver.getCurrentUrl().equals("http://aepltest.accoladeelectronics.com:6102/dealers-management");

			logger.info("Navigation to Dealer Management page result: {}", isViewed);
		} catch (Exception e) {
			logger.error("Error navigating to Dealer Management: {}", e.getMessage(), e);
		}
		return isViewed;
	}

	public String verifyPageTitle() {
		try {
			WebElement pageTitle = driver.findElement(By.className("page-title"));
			comm.highlightElement(pageTitle, "violet");
			String titleText = pageTitle.getText().trim();
			logger.info("Page title found: {}", titleText);
			return titleText;
		} catch (Exception e) {
			logger.error("Error retrieving page title: {}", e.getMessage(), e);
			return null;
		}
	}

	public Boolean isSearchButtonEnabled() {
		try {
			WebElement search_btn = driver.findElement(SEARCH_BTN);
			boolean enabled = search_btn.isEnabled();
			logger.info("Search button enabled: {}", enabled);
			Assert.assertTrue(enabled, "Search button should be enabled");
			return enabled;
		} catch (Exception e) {
			logger.error("Error checking search button state: {}", e.getMessage(), e);
			return false;
		}
	}

	public Object isSearchBoxEnabled() {
		try {
			WebElement search_box = driver.findElement(SEARCH_BOX);
			boolean enabled = search_box.isEnabled();
			logger.info("Search box enabled: {}", enabled);
			Assert.assertTrue(enabled, "Search box should be enabled");
			return enabled;
		} catch (Exception e) {
			logger.error("Error checking search box state: {}", e.getMessage(), e);
			return false;
		}
	}

	public Boolean validateSearchBoxWithMultipleInputs() {
		List<String> list_of_inputs = List.of("Suraj", "Dhananjay", "Sharukh", "QA", "Manager", "Admin");
		try {
			logger.info("Starting validation of search box with multiple inputs...");
			for (String input : list_of_inputs) {
				WebElement search_box = driver.findElement(SEARCH_BOX);
				if (search_box.isEnabled()) {
					logger.debug("Entering input '{}' into search box", input);
					search_box.clear();
					search_box.sendKeys(input);
					driver.findElement(SEARCH_BTN).click();
				} else {
					logger.warn("Search box is not enabled for input '{}'", input);
					throw new NoSuchElementException("Search box is not enabled");
				}
			}

			String toastMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(TOAST_MESSAGE)).getText();
			logger.info("Toast message after search: {}", toastMsg);
			Assert.assertTrue(toastMsg.contains("Data Fetched Successfully"),
					"Expected success message not found in toast");
			logger.info("âœ… Search box validation passed for all inputs.");
			return true;

		} catch (Exception e) {
			logger.error("Error validating search box with multiple inputs: {}", e.getMessage(), e);
			Assert.fail("Exception occurred while validating search box: " + e.getMessage());
			return false;
		}
	}

	public List<String> validateTableHeaders() {
		TableUtils table = new TableUtils(wait);
		List<String> actualHeaders = table.getTableHeaders(TABLE);
		comm.highlightElements(driver.findElements(TABLE), "violet");
		logger.info("Actual Table Headers: {}", actualHeaders);
		if (actualHeaders.isEmpty()) {
			logger.warn("Table headers could not be retrieved or are empty!");
		} else {
			logger.debug("Headers successfully retrieved: {}", actualHeaders);
		}
		return actualHeaders;
	}

	public Boolean validateTableData() {
		boolean isValid = false;
		try {
			TableUtils table = new TableUtils(wait);
			List<String> headers = table.getTableHeaders(TABLE);
			List<Map<String, String>> tableData = table.getTableData(TABLE, headers);

			logger.info("Extracted Table Data rows: {}", tableData.size());
			if (!tableData.isEmpty()) {
				isValid = true;
				logger.debug("Sample row data: {}", tableData.get(0));
			} else {
				logger.warn("Table data is empty!");
			}
		} catch (Exception e) {
			logger.error("Error validating table data: {}", e.getMessage(), e);
		}
		return isValid;
	}

	public List<Map<String, Object>> collectViewDeleteAndStatus() {
		List<Map<String, Object>> buttonStates = new ArrayList<>();
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			logger.info("Starting collection of View/Delete buttons across pages...");

			js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
			WebElement rowPerPage = wait.until(ExpectedConditions.elementToBeClickable(ROW_PER_PAGE));
			comm.highlightElement(rowPerPage, "solid purple");

			Select select = new Select(rowPerPage);
			int rowsPerPage = Integer.parseInt(select.getFirstSelectedOption().getText().trim());
			logger.info("Rows per page selected: {}", rowsPerPage);

			Thread.sleep(800);

			WebElement pageInfoSpan = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
					"/html/body/app-root/app-dealers-management/div/div[2]/div[5]/app-common-component-pagination/div/div[2]/span[2]")));
			String pageInfoText = pageInfoSpan.getText().trim();
			logger.debug("Page info text retrieved: {}", pageInfoText);

			int totalRows = Integer.parseInt(pageInfoText.substring(pageInfoText.lastIndexOf("of") + 2).trim());
			int totalPages = (int) Math.ceil(totalRows / (double) rowsPerPage);
			logger.info("Total rows = {}, Total pages = {}", totalRows, totalPages);

			for (int page = 1; page <= totalPages; page++) {
				js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
				List<WebElement> rows = driver.findElements(TABLE_ROWS);
				logger.info("Page {}: {} rows found.", page, rows.size());

				for (int i = 0; i < rows.size(); i++) {
					Map<String, Object> state = new HashMap<>();
					try {
						WebElement row = rows.get(i);
						String status = row.findElement(By.xpath("./td[5]")).getText().trim();
						WebElement viewBtn = row
								.findElement(By.xpath(".//button[contains(.,'view') or contains(.,'visibility')]"));
						WebElement deleteBtn = row.findElement(By.xpath(".//button[contains(.,'delete')]"));

						comm.highlightElement(viewBtn, "green");
						comm.highlightElement(deleteBtn, "red");

						state.put("status", status);
						state.put("viewEnabled", viewBtn.isEnabled());
						state.put("deleteEnabled", deleteBtn.isEnabled());
						logger.debug("Row {}: Status={}, ViewEnabled={}, DeleteEnabled={}", i + 1, status,
								viewBtn.isEnabled(), deleteBtn.isEnabled());
					} catch (Exception e) {
						logger.warn("Row {} skipped (buttons not found): {}", i + 1, e.getMessage());
					}
					buttonStates.add(state);
				}

				if (page < totalPages) {
					WebElement rightArrow = wait.until(ExpectedConditions.elementToBeClickable(RIGHT_ARROW));
					js.executeScript("arguments[0].scrollIntoView({behavior:'auto',block:'center'});", rightArrow);
					rightArrow.click();
					Thread.sleep(800);
					logger.info("Moved to page {}", page + 1);
				}
			}

			logger.info("Collection complete. Total rows processed: {}", buttonStates.size());

		} catch (Exception e) {
			logger.error("Error during pagination button collection: {}", e.getMessage(), e);
		}

		return buttonStates;
	}

	public boolean validateViewButtons() {
		try {
			List<Map<String, Object>> states = collectViewDeleteAndStatus();
			boolean allEnabled = states.stream().allMatch(s -> Boolean.TRUE.equals(s.get("viewEnabled")));

			if (!allEnabled) {
				logger.error("Some view buttons are disabled!");
			} else {
				logger.info("All view buttons are enabled âœ…");
			}
			return allEnabled;
		} catch (Exception e) {
			logger.error("Error validating view buttons: {}", e.getMessage(), e);
			return false;
		}
	}

	public boolean validateDeleteButtons() {
		try {
			List<Map<String, Object>> states = collectViewDeleteAndStatus();

			for (int i = 0; i < states.size(); i++) {
				String status = (String) states.get(i).get("status");
				boolean deleteEnabled = Boolean.TRUE.equals(states.get(i).get("deleteEnabled"));

				if ("In-active".equalsIgnoreCase(status) && deleteEnabled) {
					logger.error("Row {}: Delete button should be DISABLED for In-active entry!", i + 1);
					return false;
				}
				if ("Active".equalsIgnoreCase(status) && !deleteEnabled) {
					logger.error("Row {}: Delete button should be ENABLED for Active entry!", i + 1);
					return false;
				}
			}

			logger.info("Delete buttons validated successfully âœ…");
			return true;

		} catch (Exception e) {
			logger.error("Error validating delete buttons: {}", e.getMessage(), e);
			return false;
		}
	}

	// âœ… Validation: All mobile numbers must be valid (10 digits)
	public void validateMobileNumbers() {
		try {
			TableUtils table = new TableUtils(wait);
			List<String> headers = table.getTableHeaders(TABLE);
			List<Map<String, String>> tableData = table.getTableData(TABLE, headers);

			String mobileRegex = "^[0-9]{10}$";
			Pattern pattern = Pattern.compile(mobileRegex);

			logger.info("Starting validation of mobile numbers for {} rows", tableData.size());

			for (int i = 0; i < tableData.size(); i++) {
				String mobile = tableData.get(i).getOrDefault("Mobile No.", "").trim();
				boolean isValid = pattern.matcher(mobile).matches();

				if (!isValid) {
					logger.error("Row {}: Invalid Mobile Number -> {}", i + 1, mobile);
				} else {
					logger.debug("Row {}: Valid Mobile Number -> {}", i + 1, mobile);
				}

				Assert.assertTrue(isValid, "Invalid Mobile Number at row " + (i + 1) + ": " + mobile);
			}

			logger.info("âœ… All mobile numbers passed validation.");
		} catch (Exception e) {
			logger.error("Error validating mobile numbers: {}", e.getMessage(), e);
			Assert.fail("Exception occurred while validating mobile numbers: " + e.getMessage());
		}
	}

	// âœ… Validation: All emails must be valid format
	public void validateEmails() {
		try {
			TableUtils table = new TableUtils(wait);
			List<String> headers = table.getTableHeaders(TABLE);
			List<Map<String, String>> tableData = table.getTableData(TABLE, headers);

			String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
			Pattern pattern = Pattern.compile(emailRegex);

			logger.info("Starting validation of emails for {} rows", tableData.size());

			for (int i = 0; i < tableData.size(); i++) {
				String email = tableData.get(i).getOrDefault("Email", "").trim();
				boolean isValid = pattern.matcher(email).matches();

				if (!isValid) {
					logger.error("Row {}: Invalid Email -> {}", i + 1, email);
				} else {
					logger.debug("Row {}: Valid Email -> {}", i + 1, email);
				}

				Assert.assertTrue(isValid, "Invalid Email at row " + (i + 1) + ": " + email);
			}

			logger.info("âœ… All emails passed validation.");
		} catch (Exception e) {
			logger.error("Error validating emails: {}", e.getMessage(), e);
			Assert.fail("Exception occurred while validating emails: " + e.getMessage());
		}
	}

	// âœ… Check if Add Dealer button is enabled
	public Boolean isAddDealerButtonEnabled() {
		try {
			WebElement add_dealer = driver.findElement(ADD_DEALER);
			boolean enabled = add_dealer.isEnabled();
			logger.info("Add Dealer button enabled: {}", enabled);
			Assert.assertTrue(enabled, "Add Dealer button should be enabled!");

			if (enabled) {
				comm.highlightElement(add_dealer, "violet");
			} else {
				logger.warn("Add Dealer button is not enabled!");
			}
			return enabled;
		} catch (Exception e) {
			logger.error("Error checking Add Dealer button state: {}", e.getMessage(), e);
			Assert.fail("Exception occurred while checking Add Dealer button: " + e.getMessage());
			return false;
		}
	}

	// âœ… Check if Add Dealer button is visible
	public Boolean isAddDealerButtonVisible() {
		try {
			WebElement add_dealer = driver.findElement(ADD_DEALER);
			boolean visible = add_dealer.isDisplayed();
			logger.info("Add Dealer button visible: {}", visible);
			Assert.assertTrue(visible, "Add Dealer button should be visible!");

			if (visible) {
				comm.highlightElement(add_dealer, "violet");
			} else {
				logger.warn("Add Dealer button is not visible!");
			}
			return visible;
		} catch (Exception e) {
			logger.error("Error checking Add Dealer button visibility: {}", e.getMessage(), e);
			Assert.fail("Exception occurred while checking Add Dealer button visibility: " + e.getMessage());
			return false;
		}
	}

	// âœ… Click Add Dealer button and validate component
	public String clickAddDealerButton() {
		String componentTitle = null;
		try {
			WebElement addDealerBtn = driver.findElement(ADD_DEALER);
			logger.info("Scrolling to Add Dealer button...");

			((JavascriptExecutor) driver)
					.executeScript("arguments[0].scrollIntoView({behavior:'smooth', block:'center'});", addDealerBtn);
			comm.highlightElement(addDealerBtn, "violet");

			logger.info("Clicking on Add Dealer button...");
			addDealerBtn.click();

			componentTitle = comm.validateComponentTitle();
			logger.info("Navigated to component with title: {}", componentTitle);

			Assert.assertNotNull(componentTitle, "Component title should not be null after clicking Add Dealer button");

		} catch (Exception e) {
			logger.error("Error clicking Add Dealer button: {}", e.getMessage(), e);
			Assert.fail("Exception occurred while clicking Add Dealer button: " + e.getMessage());
		}
		return componentTitle;
	}

	public Boolean testAllFormFields() {
		try {
			WebElement container = driver.findElement(By.cssSelector("div.image-form-section"));
			List<WebElement> inputBoxes = container.findElements(By.cssSelector("input.mat-mdc-input-element"));
			List<WebElement> selects = container.findElements(By.cssSelector("mat-select"));

			return FormUtils.validateForm(inputBoxes, selects);

		} catch (Exception e) {
			logger.error("Error validating form fields: {}", e.getMessage(), e);
			Assert.fail("Exception occurred while validating form fields: " + e.getMessage());
			return false;
		}
	}

	// âœ… Validate all error messages for all input boxes in dealer form
	public Boolean testAllFormFieldsErrors() {
		boolean hasErrors = false;
		try {
			WebElement formContainer = driver.findElement(By.cssSelector("div.image-form-section"));
			WebElement submitButton = driver.findElement(By.className("submit-button"));

			// ðŸ”¹ Invalid test values per field (each entry should trigger an error)
			Map<String, List<String>> testValues = new HashMap<>();
			testValues.put("firstName", List.of("abcdefghijk", "123@", " Suraj ", " "));
			testValues.put("lastName", List.of(" ", "123@", " Bhalerao ", "abcdefghijk"));
			testValues.put("state", List.of("abcdefghijk", "123@", " Maharashtra ", " "));
			testValues.put("mobileNumber", List.of(" ", "abc123", "12345678901"));
			testValues.put("userEmail", List.of(" ", "invalid-email"));

			// ðŸ”¹ Expected errors per test value (order must match testValues order)
			Map<String, List<String>> expectedErrors = new HashMap<>();
			expectedErrors.put("firstName", List.of("Maximum 10 characters allowed.", //
					"Only alphabets and spaces are allowed.", //
					"Remove leading or trailing spaces.", //
					"This field is required and can't be only spaces." //
			));
			expectedErrors.put("lastName",
					List.of("This field is required and can't be only spaces.",
							" Only alphabets and spaces are allowed.", // case 2
							"Remove leading or trailing spaces.", // case 3
							"Maximum 10 characters allowed." // case 4
					));

			expectedErrors.put("state", List.of("Maximum 10 characters allowed.", //
					"Only alphabets and spaces are allowed.", //
					"Remove leading or trailing spaces.", //
					"This field is required and can't be only spaces." //
			));
			expectedErrors.put("mobileNumber", List.of("This field is required and can't be only spaces.", //
					"Enter a valid mobile number.", "Maximum 10 characters allowed." //
			));
			expectedErrors.put("userEmail",
					List.of("This field is required and can't be only spaces.", "Please enter a valid Email ID."));

			// ðŸ”¹ Validate using updated FormUtils (returns all errors per field)
			Map<String, List<String>> actualErrors = FormUtils.validateFormFields(driver, formContainer, testValues,
					submitButton, expectedErrors);

			// ðŸ”¹ Log actual results
			actualErrors.forEach((field, errors) -> logger.info("Field: {} | Errors captured: {}", field, errors));

			// ðŸ”¹ Validate field by field
			for (String field : expectedErrors.keySet()) {
				List<String> expectedList = expectedErrors.get(field);
				List<String> actualList = actualErrors.getOrDefault(field, List.of());

				for (int i = 0; i < expectedList.size(); i++) {
					String expected = expectedList.get(i);
					if (i < actualList.size()) {
						String actual = actualList.get(i);
						if (expected.equals(actual)) {
							logger.info("âœ… Field '{}' case {} passed with error: {}", field, i + 1, actual);
						} else {
							logger.error("âŒ Field '{}' case {} mismatch. Expected: '{}', Got: '{}'", field, i + 1,
									expected, actual);
						}
						Assert.assertEquals(actual, expected,
								"Validation mismatch for field '" + field + "' case " + (i + 1));
					} else {
						logger.error("âŒ Field '{}' missing expected error: {}", field, expected);
						Assert.fail("Missing validation error for field '" + field + "'. Expected: " + expected);
					}
				}
			}

			// âœ… Ensure at least one error appeared
			hasErrors = actualErrors.values().stream().anyMatch(list -> !list.isEmpty());
			Assert.assertTrue(hasErrors, "Validation errors should appear for invalid input.");

		} catch (Exception e) {
			logger.error("Error validating dealer form fields: {}", e.getMessage(), e);
			Assert.fail("Exception occurred while validating dealer form fields: " + e.getMessage());
			hasErrors = true;
		}

		return hasErrors;
	}

	public boolean isSubmitButtonIsVisibleIfNoDataIsInputed() {
		driver.navigate().refresh();
		WebElement submit_btn = wait.until(ExpectedConditions.visibilityOfElementLocated(SUBMIT_BTN));
//		WebElement submit_btn = driver.findElement(SUBMIT_BTN);
		comm.highlightElement(submit_btn, "Black");
		return submit_btn.isEnabled() ? false : true;
	}

	public boolean isDataSubmittedSuccessfully() {
		RandomGeneratorUtils random = new RandomGeneratorUtils();
		WebElement formContainer = driver.findElement(By.cssSelector("div.image-form-section"));
		WebElement submitButton = driver.findElement(By.className("submit-button"));

		List<WebElement> inputBoxes = formContainer.findElements(By.cssSelector("input.mat-mdc-input-element"));
		logger.info("Found {} input boxes in the form.", inputBoxes.size());

		// âœ… Step 1: Clear all input fields
		for (WebElement inputBox : inputBoxes) {
			try {
				if (inputBox.isDisplayed() && inputBox.isEnabled()) {
					inputBox.click();
					inputBox.sendKeys(Keys.CONTROL + "a");
					inputBox.sendKeys(Keys.DELETE);
					logger.debug("Cleared field '{}'", inputBox.getDomAttribute("formcontrolname"));
				}
			} catch (Exception e) {
				logger.warn("Unable to clear field '{}': {}", inputBox.getDomAttribute("formcontrolname"), e.getMessage());
			}
		}

		// âœ… Step 2: Enter random data into each input
		for (WebElement inputBox : inputBoxes) {
			try {
				String fieldName = inputBox.getDomAttribute("formcontrolname");
				if (fieldName == null || fieldName.isEmpty()) {
					fieldName = inputBox.getDomAttribute("placeholder");
				}

				if (!inputBox.isDisplayed() || !inputBox.isEnabled()) {
					logger.warn("Skipping field '{}' as it is disabled/hidden", fieldName);
					continue;
				}

				switch (fieldName.toLowerCase()) {
				case "mobilenumber":
				case "mobile":
				case "phone":
					String randomMobile = random.generateRandomNumber(10);
					inputBox.sendKeys(randomMobile);
					logger.info("Entered mobile number '{}' in field '{}'", randomMobile, fieldName);
					break;

				case "useremail":
				case "email":
					String randomEmail = random.generateRandomEmail();
					inputBox.sendKeys(randomEmail);
					logger.info("Entered email '{}' in field '{}'", randomEmail, fieldName);
					break;

				default:
					String randomText = random.generateRandomString(8);
					inputBox.sendKeys(randomText);
					logger.info("Entered text '{}' in field '{}'", randomText, fieldName);
					break;
				}

			} catch (Exception e) {
				logger.error("Error entering data for one of the fields: {}", e.getMessage());
			}
		}

		// âœ… Step 3: Submit form
		try {
			submitButton.click();
			logger.info("Clicked on submit button.");
			return true; // Later we can enhance with toast/snackbar confirmation
		} catch (Exception e) {
			logger.error("Failed to submit form: {}", e.getMessage());
			return false;
		}
	}

}

