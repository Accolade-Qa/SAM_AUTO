package com.aepl.sam.pages;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.aepl.sam.utils.Constants;
import com.aepl.sam.locators.SimBatchDataDetailsPageLocators;
import com.aepl.sam.utils.PageActionsUtil;

public class SimBatchDataDetailsPage extends SimBatchDataDetailsPageLocators {
	private WebDriver driver;
	private WebDriverWait wait;
	private PageActionsUtil comm;
	private final Logger logger = LogManager.getLogger(this.getClass().getSimpleName());

	public SimBatchDataDetailsPage(WebDriver driver, WebDriverWait wait) {
		this.driver = driver;
		this.wait = wait;
		this.comm = new PageActionsUtil(driver, wait);
	}

	public Boolean navBarLink() {
		boolean isViewed = false;
		try {
			logger.info("Navigating to User Management page...");
			WebElement reports = wait.until(ExpectedConditions.visibilityOfElementLocated(REPORTS));
			Assert.assertTrue(reports.getText().equalsIgnoreCase("reports"));
			reports.click();

			WebElement sim_batch = wait.until(ExpectedConditions.visibilityOfElementLocated(SIM_BATCH_DATA));
			sim_batch.click();

			if (driver.getCurrentUrl()
					.equals("http://aepltest.accoladeelectronics.com:6102/sensorise-sim-data-details")) {
				isViewed = true;
			}
			logger.info("Successfully navigated to User Management page.");
		} catch (Exception e) {
			logger.error("Error navigating to User Management: {}", e.getMessage(), e);
		}
		return isViewed;
	}

	public String verifyPageTitle() {
		WebElement pageTitle = driver.findElement(By.className("page-title"));
		comm.highlightElement(pageTitle, "violet");
		return pageTitle.getText();
	}

	public Boolean validateUpload() {
		try {
			logger.info("Starting profile picture upload...");

			WebElement uploadInput = driver.findElement(By.xpath("//input[@type='file']"));

			uploadInput.sendKeys(
					"D:\\AEPL_AUTOMATION\\SAM_AUTO\\src\\test\\resources\\SampleUpload\\Sensorise_SIM_data_Details.xlsx");

			logger.info("File path sent to input.");
			logger.info("Profile picture uploaded successfully.");
			return true;
		} catch (Exception e) {
			logger.error("Error uploading profile picture: {}", e.getMessage(), e);
			return false;
		}
	}

	public Boolean validateSubmitButton() {
		try {
			WebElement submit = wait
					.until(ExpectedConditions.visibilityOfElementLocated(By.className("submit-button")));

			if (submit.isDisplayed() && submit.isEnabled()) {
				logger.info("âœ… Submit button is displayed and enabled. Clicking now...");
				submit.click();
				return true;
			} else {
				logger.warn("âš ï¸ Submit button is either not displayed or disabled.");
				return false;
			}

		} catch (Exception e) {
			logger.error("âŒ Unexpected error while validating Submit button: {}", e.getMessage());
			return false;
		}
	}

	public List<String> validateTableHeaders(String tableType) {
		By locator;
		switch (tableType.toLowerCase()) {
			case "upload":
				locator = UPLOADED_ICCID_TABLE_HEADERS;
				break;

			case "duplicate":
				locator = DUPLICATE_ICCID_TABLE_HEADERS;
				break;

			case "not present":
				locator = NOT_PRESENT_ICCID_TABLE_HEADERS;
				break;

			default:
				throw new IllegalArgumentException("Invalid table type: " + tableType);
		}

		List<WebElement> actual_headers = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));

		if (!actual_headers.isEmpty()) {
			((JavascriptExecutor) driver).executeScript(
					"arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", actual_headers.get(0));
		}

		List<String> head = new ArrayList<>();
		for (WebElement header : actual_headers) {
			head.add(header.getText());
		}

		logger.info("Extracted {} table headers: {}", tableType, head);
		return head;
	}

	public boolean validateExportButton() {
		JavascriptExecutor js = (JavascriptExecutor) driver;

		for (int attempt = 0; attempt < 3; attempt++) {
			try {
				logger.info("Attempt {} to validate Export buttons.", attempt + 1);

				if (attempt == 0) {
					js.executeScript("window.scrollTo(0, 0);");
					Thread.sleep(500);
				}

				List<WebElement> exportButtons = wait
						.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(DOWNLOAD_EXCEL_BUTTONS));

				if (exportButtons.size() < 2) {
					logger.warn("Less than 2 export buttons found.");
					return false;
				}

				for (int i = 1; i < exportButtons.size(); i++) {
					WebElement exportButton = exportButtons.get(i);

					if (exportButton.isDisplayed() && exportButton.isEnabled()) {
						logger.info("Clicking on Export button at index {}.", i);

						js.executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'center'});",
								exportButton);

						comm.highlightElement(exportButton, "solid blue");
						exportButton.click();

						Alert alert = wait.until(ExpectedConditions.alertIsPresent());
						logger.debug("Alert detected: {}", alert.getText());
						alert.accept();
						logger.info("Alert accepted after clicking button at index {}.", i);

						Thread.sleep(500);
					}
				}

				return true;

			} catch (Exception e) {
				logger.error("Attempt {} failed: {}", attempt + 1, e.getMessage(), e);
			}
		}

		logger.warn("Export button validation failed after 3 attempts.");
		return false;
	}

	public Boolean isManualUploadButtonsVisible() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollTo(0, 0);");

		return driver.findElement(MANUAL_UPLOAD_BUTTON).isDisplayed();
	}

	public Boolean isManualUploadButtonsClickable() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollTo(0, 0);");

		return driver.findElement(MANUAL_UPLOAD_BUTTON).isEnabled();
	}

	public List<String> manualUploadButtonClickedAndOpened() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		String currentUrl = "";
		String component_title = "";

		try {
			js.executeScript("window.scrollTo(0, 0);");

			WebElement manualUpload = wait.until(ExpectedConditions.elementToBeClickable(MANUAL_UPLOAD_BUTTON));
			manualUpload.click();

			wait.until(ExpectedConditions.urlContains("manual-upload"));
			currentUrl = driver.getCurrentUrl();

			component_title = wait.until(driver -> comm.validateComponentTitle());

		} catch (Exception e) {
			logger.error("Error while clicking Manual Upload button: {}", e.getMessage(), e);
		}

		return Arrays.asList(currentUrl, component_title);
	}

	public Boolean isInputBoxEnabled() {
		return driver.findElement(INPUT_BOX).isEnabled();
	}

	// public Object isInputBoxHaveProperValidations() {
	// Map<String, String> actual_validations = new HashMap<>();
	// String error;
	//
	// WebElement inputBox = driver.findElement(INPUT_BOX);
	// WebElement submit_button = driver.findElement(By.className("submit-button"));
	//
	// // 1. Empty input (only click & submit)
	// inputBox.clear();
	// inputBox.click();
	// submit_button.click();
	// error = driver.findElement(By.xpath("//mat-error/span")).getText();
	// actual_validations.put("empty click", error);
	//
	// // 2. Short input (< 20 chars)
	// inputBox.clear();
	// inputBox.sendKeys("shortText");
	// submit_button.click();
	// error = driver.findElement(By.xpath("//mat-error/span")).getText();
	// actual_validations.put("short input", error);
	//
	// // 3. Long input (> 20 chars)
	// inputBox.clear();
	// inputBox.sendKeys("thisIsMoreThan20CharactersInput");
	// submit_button.click();
	// error = driver.findElement(By.xpath("//mat-error/span")).getText();
	// actual_validations.put("long input", error);
	//
	// // 4. Special characters
	// inputBox.clear();
	// inputBox.sendKeys("Invalid@#%CharsInput!!");
	// submit_button.click();
	// error = driver.findElement(By.xpath("//mat-error/span")).getText();
	// actual_validations.put("special char", error);
	//
	// return actual_validations;
	// }

	public String isInputBoxHaveProperValidations(String inputValue) {
		WebElement inputBox = driver.findElement(INPUT_BOX);
		WebElement submitButton = driver.findElement(By.className("submit-button"));

		inputBox.clear();
		if (inputValue != null && !inputValue.isEmpty()) {
			inputBox.sendKeys(inputValue);
		}
		submitButton.click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		WebElement errorElement = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//mat-error/span")));

		return errorElement.getText();
	}

	public Boolean isSubmitButtonEnabled() {
		// WebElement inputBox = driver.findElement(INPUT_BOX);
		// WebElement submit = driver.findElement(SUBMIT_BUTTON);
		//
		// String text = inputBox.getAttribute("value");
		//
		// if (text != null && !text.isBlank())
		// return submit.isEnabled();
		//
		// return false;

		driver.navigate().refresh();
		return wait.until(ExpectedConditions.visibilityOfElementLocated(SUBMIT_BUTTON)).isEnabled();
	}

	public Boolean clickSubmitButton() {
		WebElement submit = driver.findElement(SUBMIT_BUTTON);
		if (isSubmitButtonEnabled()) {
			submit.click();
			return false;
		} else {
			WebElement inputBox = driver.findElement(INPUT_BOX);
			inputBox.clear();
			inputBox.sendKeys(Constants.ICCID);
			submit.click();
			return true;
		}
	}

	public String validateCorrectBox() {
		return driver.findElement(BOX_HEADER).getText();
	}

	public Boolean isUploadButtonIsEnabled() {
		return driver.findElement(UPLOAD_BTN_FILE_UPLOAD).isEnabled();
	}
}

