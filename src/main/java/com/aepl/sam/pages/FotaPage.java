package com.aepl.sam.pages;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.aepl.sam.locators.FotaPageLocators;
import com.aepl.sam.utils.PageActionsUtil;

public class FotaPage extends FotaPageLocators {

	private static final Logger logger = LogManager.getLogger(FotaPage.class);

	private WebDriver driver;
	private WebDriverWait wait;
	private PageActionsUtil comm;

	public FotaPage(WebDriver driver, WebDriverWait wait) {
		this.driver = driver;
		this.wait = wait;
		this.comm = new PageActionsUtil(driver, wait);
		logger.info("Initialized FotaPage");
	}

	public void clickDeviceUtility() {
		logger.info("Clicking on Device Utility link");
		WebElement device_utils = driver.findElement(DEVICE_UTILITY);
		if (device_utils.isDisplayed() && device_utils.isEnabled()) {
			comm.highlightElement(device_utils, "solid purple");
			device_utils.click();
			Assert.assertTrue(device_utils.getText().contains("DEVICE UTILITY"),
					"Device Utility page not loaded correctly.");
		} else {
			logger.error("Device Utility link is not displayed or enabled.");
			throw new RuntimeException("Device Utility link is not displayed or enabled.");
		}
	}

	public void clickFota() {
		logger.info("Clicking on FOTA link");
		WebElement fotaLink = wait.until(ExpectedConditions.elementToBeClickable(FOTA_LINK));
		if (fotaLink.isDisplayed() && fotaLink.isEnabled()) {
			comm.highlightElement(fotaLink, "solid purple");
			fotaLink.click();
			Assert.assertTrue(driver.getCurrentUrl().contains("fota"), "FOTA page not loaded correctly.");
		} else {
			logger.error("FOTA link is not displayed or enabled.");
			throw new RuntimeException("FOTA link is not displayed or enabled.");
		}
	}

	public void selectFOTATypeButton(String type) {
		if (type.equalsIgnoreCase("manual")) {
			logger.info("Selecting Manual FOTA");
			WebElement manualFOTA = driver.findElement(MANUAL_FOTA_BTN);
			comm.highlightElement(manualFOTA, "solid purple");
			if (manualFOTA.isDisplayed() && manualFOTA.isEnabled()) {
				manualFOTA.click();
			} else {
				logger.error("Manual FOTA button is not displayed or enabled.");
				throw new RuntimeException("Manual FOTA button is not displayed or enabled.");
			}
		} else if (type.equalsIgnoreCase("bulk")) {
			logger.info("Selecting Bulk FOTA");
			driver.navigate().back();
			((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -500);");

			WebElement bulkFOTA = wait.until(ExpectedConditions.elementToBeClickable(BULK_FOTA_BTN));
			comm.highlightElement(bulkFOTA, "solid purple");

			if (bulkFOTA.isDisplayed() && bulkFOTA.isEnabled()) {
				bulkFOTA.click();
			} else {
				logger.error("Bulk FOTA button is not displayed or enabled.");
				throw new RuntimeException("Bulk FOTA button is not displayed or enabled.");
			}
		} else {
			logger.error("Invalid FOTA type: " + type);
			throw new IllegalArgumentException("Invalid FOTA type: " + type);
		}
	}

	public void createManualFotaBatch(String imeiNumber) {
		try {
			logger.info("Creating Manual FOTA batch for IMEI: {}", imeiNumber);

			WebElement manualFOTA = wait.until(ExpectedConditions.visibilityOfElementLocated(MANUAL_FOTA));
			manualFOTA.click();
			manualFOTA.clear();
			manualFOTA.sendKeys(imeiNumber);

			WebElement search = driver.findElement(SEARCH_BTN);
			comm.highlightElement(search, "solid purple");
			search.click();

			getDeviceFirmwareDetails();
			Thread.sleep(500);

			createNewFOTA();
			Thread.sleep(500);

			getFotaHistoryFromTable();
			Thread.sleep(500);

		} catch (Exception e) {
			logger.error("Error in createManualFotaBatch: ", e);
		}
	}

	public void getDeviceFirmwareDetails() {
		logger.info("Fetching device firmware details");
		try {
			((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 200);");

			ExtractAndPrintDetails(IMEI, "IMEI");
			ExtractAndPrintDetails(UIN, "UIN");
			ExtractAndPrintDetails(VIN, "VIN");
			ExtractAndPrintDetails(ICCID, "ICCID");
			ExtractAndPrintDetails(LOGGED_IN_AT, "Logged In At");
			ExtractAndPrintDetails(JOINED_AT, "Joined At");
			ExtractAndPrintDetails(VERSION, "Version");
			ExtractAndPrintDetails(UFW, "UFW");
		} catch (Exception e) {
			logger.error("Error in getDeviceFirmwareDetails: ", e);
		}
	}

	private void ExtractAndPrintDetails(By locator, String label) {
		try {
			WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
			comm.highlightElement(element, "solid purple");

			String text = element.getText();
			if (text == null || text.isEmpty()) {
				text = element.getAttribute("value");
			}
			logger.info("{}: {}", label, text);
		} catch (Exception e) {
			logger.warn("Could not find or read element for: {}", label);
		}
	}

	public void createNewFOTA() {
		try {
			logger.info("Starting new FOTA batch");
			JavascriptExecutor js = (JavascriptExecutor) driver;

			WebElement newFota = driver.findElement(NEW_FOTA_BTN);
			comm.highlightElement(newFota, "solid purple");
			newFota.click();

			WebElement state = wait.until(ExpectedConditions.elementToBeClickable(STATE));
			state.click();
			comm.highlightElement(state, "solid purple");

			List<WebElement> stateNames = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(STATES_NAME));

			for (WebElement rajya : stateNames) {
				js.executeScript("arguments[0].scrollIntoView({block: 'center'});", rajya);
				comm.highlightElement(rajya, "YELLOW");

				if ("MH".equals(rajya.getText().trim())) {
					rajya.click();
					Thread.sleep(300);
					logger.info("Selected state: MH");
					break;
				}
			}

			WebElement ufw = wait.until(ExpectedConditions.elementToBeClickable(NEW_UFW));
			ufw.click();

			List<WebElement> elements = driver.findElements(NEW_UFW_NAME);
			WebElement lastOption = elements.get(elements.size() - 1);
			js.executeScript("arguments[0].scrollIntoView(true);", lastOption);
			comm.highlightElement(lastOption, "solid purple");
			lastOption.click();

			WebElement fota_type = wait.until(ExpectedConditions.elementToBeClickable(FOTA_TYPE));
			fota_type.click();
			comm.highlightElement(fota_type, "solid purple");

			WebElement fota_type_name = wait.until(ExpectedConditions.elementToBeClickable(FOTA_TYPE_NAME));
			fota_type_name.click();
			comm.highlightElement(fota_type_name, "solid purple");

			WebElement startFota = wait.until(ExpectedConditions.elementToBeClickable(START_FOTA));
			comm.highlightElement(startFota, "solid purple");

			if (startFota.isDisplayed() && startFota.isEnabled()) {
				startFota.click();
				logger.info("FOTA started successfully");
			} else {
				logger.error("Start FOTA button is not displayed or enabled.");
				throw new RuntimeException("Start FOTA button is not displayed or enabled.");
			}
		} catch (Exception e) {
			logger.error("Error in createNewFOTA: ", e);
		}
	}

	private void getFotaHistoryFromTable() {
		try {
			logger.info("Retrieving FOTA history");
			((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 20);");

			List<WebElement> fota_history = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(FOTA_HISTORY));
			for (int i = 0; i < fota_history.size(); i++) {
				WebElement freshRow = driver.findElements(FOTA_HISTORY).get(i);
				logger.info("FOTA History Row [{}]: {}", i + 1, freshRow.getText());
			}
		} catch (Exception e) {
			logger.error("Error retrieving FOTA history table: ", e);
		}
	}

	public void createBulkFotaBatch() {
		try {
			logger.info("Creating Bulk FOTA batch");

			WebElement batch_name = driver.findElement(FOTA_BATCH_NAME);
			comm.highlightElement(batch_name, "solid purple");
			batch_name.clear();
			batch_name.sendKeys("DEMO FOTA BATCH");

			WebElement fota_desc = driver.findElement(FOTA_BATCH_DESC);
			comm.highlightElement(fota_desc, "solid purple");
			fota_desc.clear();
			fota_desc.sendKeys("DEMO FOTA BATCH DESCRIPTION");

			WebElement fota_type = wait.until(ExpectedConditions.elementToBeClickable(B_FOTA_TYPE));
			fota_type.click();
			comm.highlightElement(fota_type, "solid purple");

			WebElement fota_type_name = wait.until(ExpectedConditions.elementToBeClickable(B_FOTA_TYPE_NAME));
			fota_type_name.click();
			comm.highlightElement(fota_type_name, "solid purple");

			WebElement upload_file = wait.until(ExpectedConditions.elementToBeClickable(UPLOAD_FILE));
			comm.highlightElement(upload_file, "solid purple");

			if (upload_file.isDisplayed() && upload_file.isEnabled()) {
				upload_file.click();
				Thread.sleep(500);

				String filePath = "D:\\Sampark_Automation\\SAM_AUTO\\src\\test\\resources\\SampleUpload\\SampleFOTATemplate.csv";
				StringSelection selection = new StringSelection(filePath);
				Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);

				Robot robot = new Robot();
				robot.delay(500);
				robot.keyPress(KeyEvent.VK_CONTROL);
				robot.keyPress(KeyEvent.VK_V);
				robot.keyRelease(KeyEvent.VK_V);
				robot.keyRelease(KeyEvent.VK_CONTROL);
				robot.delay(500);
				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);
				logger.info("File uploaded for Bulk FOTA");
			} else {
				logger.error("Upload file button is not displayed or enabled.");
				throw new RuntimeException("Upload file button is not displayed or enabled.");
			}

			WebElement submit = wait.until(ExpectedConditions.elementToBeClickable(SUBMIT_BTN));
			comm.highlightElement(submit, "solid purple");

			if (submit.isDisplayed() && submit.isEnabled()) {
				submit.click();
				logger.info("Bulk FOTA batch created successfully.");
			} else {
				logger.error("Submit button is not displayed or enabled.");
				throw new RuntimeException("Submit button is not displayed or enabled.");
			}
		} catch (Exception e) {
			logger.error("Error in createBulkFotaBatch: ", e);
		}
	}

	public String getFotaBatchList() {
		try {
			((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
			List<WebElement> fotaRows = wait
					.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(FOTA_HISTORY_TABLE));

			if (fotaRows.isEmpty()) {
				logger.warn("No data rows found in FOTA history table.");
				return "No rows found in FOTA history table.";
			}

			WebElement firstRow = fotaRows.get(0);
			List<WebElement> cells = firstRow.findElements(By.tagName("td"));

			WebElement batchNameCell = cells.get(1);
			WebElement batchDescCell = cells.get(2);
			WebElement createdByCell = cells.get(3);

			comm.highlightElement(batchNameCell, "solid purple");
			comm.highlightElement(batchDescCell, "solid purple");
			comm.highlightElement(createdByCell, "solid purple");

			String batchName = batchNameCell.getText().trim();
			String batchDescription = batchDescCell.getText().trim();
			String createdBy = createdByCell.getText().trim();

			logger.info("Batch Name: {}", batchName);
			logger.info("Batch Description: {}", batchDescription);
			logger.info("Created By: {}", createdBy);

			if ("DEMO FOTA BATCH".equals(batchName) && "DEMO FOTA BATCH DESCRIPTION".equals(batchDescription)
					&& "Suraj Bhalerao".equals(createdBy)) {
				return "Batch seted successfully!";
			} else {
				logger.warn("FOTA batch data does not match expected values.");
				return "Batch data does not match expected values.";
			}

		} catch (Exception e) {
			logger.error("Exception in getFotaBatchList: ", e);
			return "An error occurred while fetching the FOTA batch list.";
		}
	}
}

