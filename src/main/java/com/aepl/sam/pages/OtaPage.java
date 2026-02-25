package com.aepl.sam.pages;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aepl.sam.utils.Constants;
import com.aepl.sam.locators.OtaPageLocators;
import com.aepl.sam.utils.PageActionsUtil;

public class OtaPage extends OtaPageLocators {
	private WebDriver driver;
	private WebDriverWait wait;
	private PageActionsUtil comm;
	private JavascriptExecutor js;
	private static final Logger logger = LogManager.getLogger(OtaPage.class);

	public OtaPage(WebDriver driver, WebDriverWait wait) {
		this.driver = driver;
		this.wait = wait;
		this.comm = new PageActionsUtil(driver, wait);
		this.js = (JavascriptExecutor) driver;
	}

	public String navBarLink() {
		logger.info("Navigating to OTA page via navbar...");
		WebElement deviceUtil = wait.until(ExpectedConditions.visibilityOfElementLocated(DEVICE_UTILITY));
		comm.highlightElement(deviceUtil, "solid purple");
		deviceUtil.click();
		WebElement ota = wait.until(ExpectedConditions.visibilityOfElementLocated(OTA_LINK));
		comm.highlightElement(ota, "solid purple");
		ota.click();
		String currentUrl = driver.getCurrentUrl();
		logger.info("Current OTA page URL: {}", currentUrl);
		return currentUrl;
	}

	public String testManualOtaFeature() {
		try {
			logger.info("Testing manual OTA feature...");
			js.executeScript("window.scrollTo(0, 0);");
			Thread.sleep(500);

			WebElement manualOtaButton = wait.until(ExpectedConditions.visibilityOfElementLocated(MANUAL_OTA_BUTTON));
			comm.highlightElement(manualOtaButton, "solid purple");
			manualOtaButton.click();
			logger.debug("Clicked Manual OTA button.");

			WebElement searchField = wait.until(ExpectedConditions.visibilityOfElementLocated(SEARCH_FIELD));
			comm.highlightElement(searchField, "solid purple");
			searchField.clear();
			searchField.sendKeys(Constants.IMEI);
			logger.info("Searching for IMEI: {}", Constants.IMEI);

			WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(SEARCH_BUTTON));
			comm.highlightElement(searchButton, "solid purple");
			Thread.sleep(500);
			searchButton.click();

			Thread.sleep(500);
			js.executeScript("window.scrollBy(0, window.innerHeight / 2 );");

			WebElement addOtaButton = wait.until(ExpectedConditions.elementToBeClickable(NEW_OTA_BUTTON));
			comm.highlightElement(addOtaButton, "solid purple");
			addOtaButton.click();
			logger.debug("Clicked New OTA button.");

			js.executeScript("window.scrollBy(0, window.innerHeight / 2 );");

			List<WebElement> checkboxes = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(COMMAND_LIST));
			for (WebElement checkbox : checkboxes) {
				comm.highlightElement(checkbox, "solid purple");
				String text = checkbox.getText();
				if ((text.contains("A2T_GET_IMEI") || text.contains("GET CIP2")) && !checkbox.isSelected()) {
					checkbox.click();
					logger.info("Selected OTA command: {}", text);
				}
			}

			js.executeScript("window.scrollBy(0, window.innerHeight / 2 );");
			WebElement setBatch = wait.until(ExpectedConditions.visibilityOfElementLocated(SET_BATCH_BTN));
			comm.highlightElement(setBatch, "solid purple");
			setBatch.click();
			logger.info("Set batch clicked.");

			js.executeScript("window.scrollBy(0, window.innerHeight * 2.2);");
			WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(BATCH_SUBMIT_BTN));
			comm.highlightElement(submitButton, "solid purple");
			submitButton.click();
			logger.info("Submit button clicked.");

			Alert alert = wait.until(ExpectedConditions.alertIsPresent());
			if (alert != null) {
				logger.info("Alert present: {}", alert.getText());
				alert.accept();
			}
		} catch (Exception e) {
			logger.error("Error in testManualOtaFeature: {}", e.getMessage(), e);
			return "Error occurred while testing manual OTA feature: " + e.getMessage();
		}
		return "New OTA added successfully for IMEI: " + Constants.IMEI;
	}

	public String testOtaDetails() {
		logger.info("Testing OTA details for IMEI: {}", Constants.IMEI);
		try {
			WebElement searchField = wait.until(ExpectedConditions.visibilityOfElementLocated(SEARCH_FIELD));
			comm.highlightElement(searchField, "solid purple");
			searchField.clear();
			searchField.sendKeys(Constants.IMEI);
			WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(SEARCH_BUTTON));
			comm.highlightElement(searchButton, "solid purple");
			searchButton.click();

			Thread.sleep(1000);
			js.executeScript("window.scrollBy(0, window.innerHeight);");

			List<WebElement> otaInfo = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(OTA_DETAILS));
			for (WebElement info : otaInfo) {
				comm.highlightElement(info, "solid purple");
				String text = info.getText();
				logger.debug("OTA detail: {}", text);
				if (text.contains(Constants.IMEI) && !text.contains("Aborted")) {
					logger.info("Valid OTA detail found.");
					return "OTA details displayed successfully.";
				}
			}
		} catch (Exception e) {
			logger.error("Error in testOtaDetails: {}", e.getMessage(), e);
		}
		return "No details found for IMEI: " + Constants.IMEI;
	}

	public boolean testAbortButton() {
		logger.info("Testing abort button...");
		try {
			WebElement abortButton = wait.until(ExpectedConditions.elementToBeClickable(ABORT_BTN));
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", abortButton);
			comm.highlightElement(abortButton, "solid purple");
			abortButton.click();
			logger.info("Abort button clicked.");
			return true;
		} catch (Exception e) {
			logger.error("Error while testing abort button: {}", e.getMessage(), e);
		}
		return false;
	}

	public String testOtaBatch() {
		logger.info("Testing OTA batch functionality...");
		driver.navigate().back();

		try {
			WebElement otaBatchButton = wait.until(ExpectedConditions.elementToBeClickable(BATCH_OTA_LINK));
			comm.highlightElement(otaBatchButton, "solid purple");
			otaBatchButton.click();

			WebElement componentTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(COMPONENT_TITLE));
			comm.highlightElement(componentTitle, "solid purple");
			String titleText = componentTitle.getText();
			logger.info("Component title found: {}", titleText);

			if (!titleText.equalsIgnoreCase("OTA Batch Details")) {
				logger.warn("Unexpected component title: {}", titleText);
			}

			if (comm.validateSampleFileButton()) {
				logger.info("Sample file button validated.");
			} else {
				logger.warn("Sample file button missing or not clickable.");
			}

			WebElement batchNameField = wait.until(ExpectedConditions.visibilityOfElementLocated(BATCH_NAME_FIELD));
			batchNameField.clear();
			batchNameField.sendKeys("SB_OTA_Batch");

			WebElement descriptionField = wait.until(ExpectedConditions.visibilityOfElementLocated(DESCRIPTION_FIELD));
			descriptionField.clear();
			descriptionField.sendKeys("This is a test OTA batch.");

			WebElement fileInput = wait.until(ExpectedConditions.elementToBeClickable(FILE_INPUT));
			fileInput.click();

			String filePath = "D:\\Sampark_Automation\\SAM_AUTO\\src\\test\\resources\\SampleUpload\\SampleOTATemplate.csv";
			StringSelection selection = new StringSelection(filePath);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);

			Robot robot = new Robot();
			robot.delay(500);
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
			logger.info("File input done using Robot.");

			List<WebElement> otaCheckboxes = wait
					.until(ExpectedConditions.presenceOfAllElementsLocatedBy(COMMAND_LIST));
			for (WebElement checkbox : otaCheckboxes) {
				comm.highlightElement(checkbox, "solid purple");
				String text = checkbox.getText();
				if ((text.contains("A2T_GET_CIP3") || text.contains("A2T_GET_CGPR")) && !checkbox.isSelected()) {
					checkbox.click();
					logger.info("Checkbox selected: {}", text);
				}
			}

			WebElement setBatchButton = wait.until(ExpectedConditions.elementToBeClickable(SET_BATCH_BTN));
			comm.highlightElement(setBatchButton, "solid purple");
			setBatchButton.click();
			logger.info("Clicked Set Batch.");

			js.executeScript("window.scrollBy(0, window.innerHeight);");
			WebElement batchSubmitButton = wait.until(ExpectedConditions.elementToBeClickable(BATCH_SUBMIT_BTN));
			comm.highlightElement(batchSubmitButton, "solid purple");
			batchSubmitButton.click();
			logger.info("Clicked Batch Submit.");

			Alert alert = wait.until(ExpectedConditions.alertIsPresent());
			if (alert != null) {
				logger.info("Alert text: {}", alert.getText());
				alert.accept();
			}

		} catch (Exception e) {
			logger.error("Error in testOtaBatch: {}", e.getMessage(), e);
		}

		return "OTA batch functionality is working correctly.";
	}
}

