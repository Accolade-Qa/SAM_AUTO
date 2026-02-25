package com.aepl.sam.pages;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aepl.sam.utils.Constants;
import com.aepl.sam.locators.ProductionDevicePageLocators;
import com.aepl.sam.utils.CalendarActions;
import com.aepl.sam.utils.PageActionsUtil;
import com.aepl.sam.utils.RandomGeneratorUtils;
import com.aepl.sam.utils.TableUtils;

public class ProductionDevicePage extends ProductionDevicePageLocators {
	private WebDriver driver;
	private WebDriverWait wait;
	private PageActionsUtil actions;
	private CalendarActions CalAct;
	JavascriptExecutor js;
	private String randomUIN;
	private String randomIMEI;
	private String randomICCID;
	private RandomGeneratorUtils random;
	private TableUtils table;
	private static final Logger logger = LogManager.getLogger(ProductionDevicePage.class);

	public ProductionDevicePage(WebDriver driver, WebDriverWait wait) {
		this.driver = driver;
		this.wait = wait;
		this.actions = new PageActionsUtil(driver, wait);
		this.CalAct = new CalendarActions(driver, wait);
		this.js = (JavascriptExecutor) driver;
		this.random = new RandomGeneratorUtils();
		this.table = new TableUtils(driver, wait);
	}

	private void scrollToTop() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollTo(0, 0);");
	}

	private void scrollToBottom() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
	}

	public String validateSingleInputBox(String fieldName, String inputValue) {
		// Search for both input and mat-select fields
		String xpath = String.format("//*[@id='%1$s' or @name='%1$s'or @class='%1$s' or @formcontrolname='%1$s']",
				fieldName);
		// System.out.println("XPath used for locating the field: " + xpath);

		List<WebElement> elements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(xpath)));

		if (elements.isEmpty()) {
			throw new NoSuchElementException("No input/mat-select found for: " + fieldName);
		}

		WebElement element = elements.get(0);
		String tagName = element.getTagName();

		// System.out.println("Tag name of the located element: " + tagName);
		if ("mat-select".equalsIgnoreCase(tagName)) {
			return validateMatSelectField(element, inputValue);
		} else {
			return validateInputField(element, inputValue);
		}
	}

	// neglect this... this is not working as expected so we skipped mat-select
	// validation
	private String validateMatSelectField(WebElement matSelect, String optionText) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		WebElement submitButton = driver.findElement(By.className("submit-button"));

		try {
			wait.until(ExpectedConditions.elementToBeClickable(matSelect));
			matSelect.click();

			// Wait for dropdown panel (mat-option) to appear
			By optionLocator = By.xpath(String.format("//mat-select[@formcontrolname='%1$s']", optionText));
			WebElement option = wait.until(ExpectedConditions.elementToBeClickable(optionLocator));
			option.click();

			try {
				Thread.sleep(300);
				option.click();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			submitButton.click();

			WebElement parentField = matSelect.findElement(By.xpath("./ancestor::mat-form-field"));
			List<WebElement> errorElements = parentField.findElements(By.tagName("mat-error"));
			if (!errorElements.isEmpty()) {
				return errorElements.get(0).getText().trim();
			}

			return "âœ… No validation error";

		} catch (org.openqa.selenium.TimeoutException e) {
			return "âš ï¸ No validation message appeared";
		}
	}

	private String validateInputField(WebElement inputBox, String inputValue) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		WebElement submitButton = wait.until(ExpectedConditions.visibilityOfElementLocated(SUBMIT_BTN));

		try {
			wait.until(ExpectedConditions.visibilityOf(inputBox));
			String type = inputBox.getDomAttribute("type");

			if ("file".equalsIgnoreCase(type)) {
				// 1. Click inside file input to enable it
				inputBox.click();

				// 2. Optionally: sendKeys the file path here (if you want to upload), or skip
				// for validation
				if (inputValue != null && !inputValue.trim().isEmpty()) {
					inputBox.sendKeys(inputValue);
				}

				// 3. Click outside on the submit button to trigger validation
				submitButton.click();

				// 4. Get error messages if present
				WebElement parentField = inputBox
						.findElement(By.xpath("./ancestor::div[contains(@class,'form-field')]"));
				List<WebElement> errorElements = parentField.findElements(By.tagName("mat-error"));

				if (!errorElements.isEmpty()) {
					return errorElements.get(0).getText().trim();
				}
				return "âœ… File upload validated successfully";
			}

			// Handle non-file inputs as before.
			wait.until(ExpectedConditions.elementToBeClickable(inputBox));
			inputBox.clear();
			if (inputValue != null && !inputValue.isEmpty()) {
				inputBox.sendKeys(inputValue);
			}
			submitButton.click();

			WebElement parentField = inputBox.findElement(By.xpath("./ancestor::mat-form-field"));
			WebElement errorElement = wait
					.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(parentField, By.tagName("mat-error")))
					.get(0);
			return errorElement.getText().trim();

		} catch (org.openqa.selenium.TimeoutException e) {
			return "âš ï¸ No validation message appeared";
		}
	}

	public String navBarLink() {
		logger.info("Navigating to Production Devices via Device Utility...");
		wait.until(ExpectedConditions.visibilityOfElementLocated(DEVICE_UTILITY)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(PRODUCTION_DEVICES)).click();
		logger.info("Current URL after navigation: {}", driver.getCurrentUrl());
		return driver.getCurrentUrl();
	}

	public boolean isnavBarLinkClickable() {
		return driver.findElement(DEVICE_UTILITY).isEnabled();
	}

	public boolean isAddProdDeviceLinkVisible() {
		logger.info("Checking visibility of Add Production Device button...");
		WebElement addProdDeviceButton = wait.until(ExpectedConditions.visibilityOfElementLocated(ADD_PROD_DEVICE));
		boolean isVisible = addProdDeviceButton.isDisplayed();
		logger.info("Add Production Device button visibility: {}", isVisible);
		return isVisible;
	}

	public boolean isAddProdDeviceLinkClickable() {
		logger.info("Checking if Add Production Device button is clickable...");
		WebElement addProdDeviceButton = wait.until(ExpectedConditions.elementToBeClickable(ADD_PROD_DEVICE));
		boolean isClickable = addProdDeviceButton.isEnabled();
		logger.info("Add Production Device button clickable: {}", isClickable);
		return isClickable;
	}

	public boolean isManualUploadButtonVisible() {
		scrollToTop();

		logger.info("Checking visibility of Manual Upload button...");
		return wait.until(ExpectedConditions.visibilityOfElementLocated(MANUAL_UPLOAD)).isDisplayed();
	}

	public boolean isManualUploadButtonClickable() {
		scrollToTop();

		logger.info("Checking if Manual Upload button is clickable...");
		WebElement manualUploadButton = wait.until(ExpectedConditions.elementToBeClickable(MANUAL_UPLOAD));
		boolean isClickable = manualUploadButton.isEnabled();
		logger.info("Manual Upload button clickable: {}", isClickable);
		return isClickable;
	}

	public String clickManualUploadProductionDevicesButton() {
		scrollToTop();

		logger.info("Clicking on Add Production Device button...");
		WebElement AddProdDevice = wait.until(ExpectedConditions.visibilityOfElementLocated(MANUAL_UPLOAD));
		AddProdDevice.click();

		WebElement addProdDevicePageTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(PAGE_TITLE));
		logger.info("Add Production Device Page Title: {}", addProdDevicePageTitle.getText());
		return addProdDevicePageTitle.getText();
	}

	public String NewInputFields(String para) {
		driver.findElement(REFRESHBTN).click();

		randomUIN = random.generateRandomUIN();
		randomIMEI = random.generateRandomIMEI();
		randomICCID = random.generateRandomICCID();

		logger.info("Generated random UIN: {}", randomUIN);

		if (para.equalsIgnoreCase("add")) {
			logger.info("Filling Add Production Device form...");

			WebElement AddUID = wait.until(ExpectedConditions.visibilityOfElementLocated(UID));
			actions.highlightElement(AddUID, "solid purple");
			AddUID.sendKeys(randomUIN);

			WebElement AddIMEI = wait.until(ExpectedConditions.visibilityOfElementLocated(IMEI));
			actions.highlightElement(AddIMEI, "solid purple");
			AddIMEI.sendKeys(randomIMEI);

			WebElement AddICCID = wait.until(ExpectedConditions.visibilityOfElementLocated(ICCID));
			actions.highlightElement(AddICCID, "solid purple");
			AddICCID.sendKeys(randomICCID);

			WebElement deviceModelDropdown = wait
					.until(ExpectedConditions.visibilityOfElementLocated(DEVICE_MODEL_NAME));
			js.executeScript("arguments[0].click();", deviceModelDropdown);

			List<WebElement> deviceModelOptions = wait
					.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(DEVICE_MODEL_OPTIONS));
			boolean modelFound = false;
			for (WebElement modelOption : deviceModelOptions) {
				String modelText = modelOption.getText().trim();
				js.executeScript("arguments[0].scrollIntoView({block: 'center'});", modelOption);
				actions.highlightElement(modelOption, "solid purple");

				if (modelText.equals(Constants.DEVICE_MODEL)) {
					modelOption.click();
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					logger.info("Selected device model: {}", modelText);
					modelFound = true;
					break;
				}
			}
			if (!modelFound) {
				logger.warn("Device model {} not found in options.", Constants.DEVICE_MODEL);
			}

			wait.until(ExpectedConditions.visibilityOfElementLocated(MOBILE_NUMBER)).sendKeys(Constants.MOBILE_NUMBER);
			wait.until(ExpectedConditions.visibilityOfElementLocated(SERVICE_PROVIDER)).sendKeys(Constants.ISP_1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(ALT_MOBILE_NO))
					.sendKeys(Constants.ALT_MOBILE_NUMBER);
			wait.until(ExpectedConditions.visibilityOfElementLocated(ALT_SERVICE_PROVIDER)).sendKeys(Constants.ISP_2);
			wait.until(ExpectedConditions.visibilityOfElementLocated(FIRMWARE)).sendKeys(Constants.FIRMWARE);
			wait.until(ExpectedConditions.visibilityOfElementLocated(SIM_VENDOR)).sendKeys(Constants.ISP_2);

			LocalDate targetDate = LocalDate.now().minusDays(10);
			String formattedDate = targetDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

			CalAct.selectDate(CAL_BTN, formattedDate);

			WebElement SubmitButton = wait.until(ExpectedConditions.visibilityOfElementLocated(SUBMIT_BTN));
			js.executeScript("arguments[0].scrollIntoView({block: 'center'});", SubmitButton);
			actions.highlightElement(SubmitButton, "solid purple");
			SubmitButton.click();

			WebElement toast_msg = wait.until(ExpectedConditions.visibilityOfElementLocated(TOAST_MSG));
			actions.highlightElement(toast_msg, "solid purple");
			logger.info("Toast message after adding device: {}", toast_msg.getText());
			return toast_msg.getText();

			// WebElement ProdDevicePageTitle =
			// wait.until(ExpectedConditions.visibilityOfElementLocated(PAGE_TITLE));
			// logger.info("Production device added, page title: {}",
			// ProdDevicePageTitle.getText());
			// return ProdDevicePageTitle.getText();

		} else if (para.equalsIgnoreCase("update")) {
			logger.info("Updating Production Device details...");

			WebElement deviceModelDropdown = wait.until(ExpectedConditions.elementToBeClickable(DEVICE_MODEL_NAME));
			js.executeScript("arguments[0].click();", deviceModelDropdown);

			List<WebElement> deviceModelOptions = wait
					.until(ExpectedConditions.presenceOfAllElementsLocatedBy(DEVICE_MODEL_OPTIONS));
			boolean modelFound = false;
			for (WebElement modelOption : deviceModelOptions) {
				String modelText = modelOption.getText().trim();
				js.executeScript("arguments[0].scrollIntoView({block: 'center'});", modelOption);
				actions.highlightElement(modelOption, "solid purple");

				if (modelText.equals(Constants.DEVICE_MODEL)) {
					modelOption.click();
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					logger.info("Selected device model: {}", modelText);
					modelFound = true;
					break;
				}
			}
			if (!modelFound) {
				logger.warn("Device model {} not found in options.", Constants.DEVICE_MODEL);
			}

			WebElement mobile_number = wait.until(ExpectedConditions.visibilityOfElementLocated(MOBILE_NUMBER));
			mobile_number.clear();
			mobile_number.sendKeys(Constants.MOBILE_NUMBER + "00000");

			WebElement service_provider = wait.until(ExpectedConditions.visibilityOfElementLocated(SERVICE_PROVIDER));
			service_provider.clear();
			service_provider.sendKeys(Constants.ISP_2);

			WebElement alt_mobile = wait.until(ExpectedConditions.visibilityOfElementLocated(ALT_MOBILE_NO));
			alt_mobile.clear();
			alt_mobile.sendKeys(Constants.ALT_MOBILE_NUMBER);

			WebElement alt_service_provider = wait
					.until(ExpectedConditions.visibilityOfElementLocated(ALT_SERVICE_PROVIDER));
			alt_service_provider.clear();
			alt_service_provider.sendKeys(Constants.ISP_1);

			WebElement firmware = wait.until(ExpectedConditions.visibilityOfElementLocated(FIRMWARE));
			firmware.clear();
			firmware.sendKeys(Constants.UP_FIRMWARE);

			WebElement sim_vendor = wait.until(ExpectedConditions.visibilityOfElementLocated(SIM_VENDOR));
			sim_vendor.clear();
			sim_vendor.sendKeys(Constants.ISP_1);

			CalAct.selectDate(CAL_BTN, "26-06-2025");

			scrollToBottom();

			WebElement update_btn = wait.until(ExpectedConditions.elementToBeClickable(UPDATE_BTN));
			actions.highlightElement(update_btn, "solid purple");
			update_btn.click();

			WebElement toast_msg = wait.until(ExpectedConditions.visibilityOfElementLocated(TOAST_MSG));
			actions.highlightElement(toast_msg, "solid purple");
			logger.info("Toast message after adding device: {}", toast_msg.getText());
			return toast_msg.getText();

			// WebElement ProdDevicePageTitle =
			// wait.until(ExpectedConditions.visibilityOfElementLocated(PAGE_TITLE));
			// logger.info("Production device updated, page title: {}",
			// ProdDevicePageTitle.getText());
			// return ProdDevicePageTitle.getText();
		}

		driver.navigate().back();
		logger.warn("No valid operation was selected for NewInputFields.");
		return "Not either production device is added or updated";
	}

	public String searchDevice() {
		logger.info("Searching for a device using IMEI: {}", Constants.IMEI);
		driver.navigate().back();
		WebElement search = wait.until(ExpectedConditions.visibilityOfElementLocated(SEARCH_FIELD));
		search.sendKeys(Constants.IMEI);
		WebElement searchButton = wait.until(ExpectedConditions.visibilityOfElementLocated(SEARCH_BUTTON));
		searchButton.click();
		js.executeScript("window.scrollBy(0,5000);");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		WebElement searchPageTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(PAGE_TITLE));
		logger.info("Search result page title: {}", searchPageTitle.getText());
		return searchPageTitle.getText();
	}

	public String viewDevice() {
		logger.info("Viewing device details...");
		WebElement viewButton = wait.until(ExpectedConditions.visibilityOfElementLocated(EYE_ICON));
		viewButton.click();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		WebElement UpdatePageTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(PAGE_TITLE));
		logger.info("View page title: {}", UpdatePageTitle.getText());
		return UpdatePageTitle.getText();
	}

	public String DeleteDevice() {
		try {
			logger.info("Deleting device...");
			WebElement DeleteButton = wait.until(ExpectedConditions.visibilityOfElementLocated(DELETE_ICON));
			DeleteButton.click();
			Thread.sleep(1000);
			Alert alert = driver.switchTo().alert();
			alert.accept();
			Thread.sleep(2000);
			WebElement DeletePageTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(PAGE_TITLE));
			logger.info("Device deleted, page title: {}", DeletePageTitle.getText());
		} catch (Exception e) {
			logger.error("Error while deleting device: {}", e.getMessage(), e);
			WebElement DeletePageTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(PAGE_TITLE));
			logger.info("Delete operation failed, page title: {}", DeletePageTitle.getText());
			return DeletePageTitle.getText();
		} finally {
			driver.navigate().back();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
		return "" + Constants.DEVICE_MODEL + " Device Deleted Successfully";
	}

	public boolean isDeviceModelNameClickable() {
		WebElement deviceModelDropdown = wait.until(ExpectedConditions.elementToBeClickable(DEVICE_MODEL_NAME));
		actions.highlightElement(deviceModelDropdown, "solid purple");
		boolean isClickable = deviceModelDropdown.isEnabled();
		logger.info("Device Model Name dropdown clickable: {}", isClickable);
		return isClickable;
	}

	public boolean isSubmitButtonDisabledNoData() {
		WebElement submitButton = wait.until(ExpectedConditions.visibilityOfElementLocated(SUBMIT_BTN));
		boolean isDisabled = !submitButton.isEnabled();
		logger.info("Submit button disabled with no data: {}", isDisabled);
		return isDisabled;
	}

	public boolean isBulkUploadButtonEnabled() {
		WebElement bulkUploadButton = wait.until(ExpectedConditions.visibilityOfElementLocated(BULK_UPLOAD));
		actions.highlightElement(bulkUploadButton, "solid purple");
		boolean isEnabled = bulkUploadButton.isEnabled();
		logger.info("Bulk Upload button enabled: {}", isEnabled);
		return isEnabled;
	}

	public boolean isBulkUploadButtonClickable() {
		WebElement bulkUploadButton = wait.until(ExpectedConditions.elementToBeClickable(BULK_UPLOAD));
		boolean isClickable = bulkUploadButton.isEnabled();
		logger.info("Bulk Upload button clickable: {}", isClickable);
		return isClickable;
	}

	public String clickBulkUploadProductionDevicesButton() {
		scrollToTop();

		logger.info("Clicking on Bulk Upload button...");
		WebElement bulkUploadButton = wait.until(ExpectedConditions.visibilityOfElementLocated(BULK_UPLOAD));
		actions.highlightElement(bulkUploadButton, "solid purple");
		bulkUploadButton.click();

		WebElement bulkUploadPageTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(PAGE_TITLE));
		actions.highlightElement(bulkUploadPageTitle, "solid purple");
		logger.info("Bulk Upload Page Title: {}", bulkUploadPageTitle.getText());
		return bulkUploadPageTitle.getText();
	}

	public boolean isDownloadSampleButtonEnabled() {
		WebElement downloadSampleButton = wait.until(ExpectedConditions.visibilityOfElementLocated(DOWNLOAD_SAMPLE));
		actions.highlightElement(downloadSampleButton, "solid purple");
		boolean isEnabled = downloadSampleButton.isEnabled();
		logger.info("Download Sample button enabled: {}", isEnabled);
		return isEnabled;
	}

	public boolean isDownloadSampleButtonClickable() {
		WebElement downloadSampleButton = wait.until(ExpectedConditions.elementToBeClickable(DOWNLOAD_SAMPLE));
		actions.highlightElement(downloadSampleButton, "solid purple");
		boolean isClickable = downloadSampleButton.isEnabled();
		logger.info("Download Sample button clickable: {}", isClickable);
		return isClickable;
	}

	public String downloadSampleFile() {
		return actions.clickSampleFileButton();
	}

	public boolean isAttachButtonEnabled() {
		WebElement attachButton = wait.until(ExpectedConditions.visibilityOfElementLocated(ATTACH_FILE));
		actions.highlightElement(attachButton, "solid purple");
		boolean isEnabled = attachButton.isEnabled();
		logger.info("Attach button enabled: {}", isEnabled);
		return isEnabled;
	}

	public boolean isAttachButtonClickable() {
		WebElement attachButton = wait.until(ExpectedConditions.elementToBeClickable(ATTACH_FILE));
		actions.highlightElement(attachButton, "solid purple");
		boolean isClickable = attachButton.isEnabled();
		logger.info("Attach button clickable: {}", isClickable);
		return isClickable;
	}

	public String uploadFileAndSubmit() {
		String message;
		try {
			WebElement fileInput = wait.until(ExpectedConditions.elementToBeClickable(ATTACH_FILE));
			fileInput.click();
			String filePath = "D:\\AEPL_AUTOMATION\\SAM_AUTO\\src\\test\\resources\\SampleUpload\\Sample_Production_Sheet.xlsx";
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
			WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(SUBMIT_BTN));
			submitButton.click();
			logger.info("Submit button clicked.");
			WebElement toastMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(TOAST_MSG));
			actions.highlightElement(toastMessage, "solid purple");
			message = toastMessage.getText().trim().toString();
			logger.info("Toast message received: {}", message);
		} catch (Exception e) {
			logger.error("Error during file upload and submission: {}", e.getMessage(), e);
			return "Error during file upload and submission.";
		}
		return message;
	}

	public boolean isSubmitButtonDisabledWhenNoFileUploaded() {
		// 1. Find the file input field (no click)
		WebElement fileInput = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@formcontrolname='file']")));

		// 2. Check that it is empty
		String filePath = fileInput.getDomAttribute("value");
		if (filePath != null && !filePath.isEmpty()) {
			logger.warn("File input is not empty. Current value: {}", filePath);
		} else {
			logger.info("File input is empty as expected.");
		}

		// 3. Locate the submit button
		WebElement submitButton = wait.until(ExpectedConditions.presenceOfElementLocated(SUBMIT_BTN));

		// 4. Check if button is disabled
		boolean isEnabled = submitButton.isEnabled();

		// 5. Optional: Double-check using the 'disabled' attribute (covers
		// Angular/React cases)
		String disabledAttr = submitButton.getDomAttribute("disabled");
		boolean isDisabledAttr = disabledAttr != null
				&& (disabledAttr.equals("true") || disabledAttr.equals("disabled"));

		boolean isDisabled = !isEnabled || isDisabledAttr;

		logger.info("Submit button disabled state: {}", isDisabled);
		return isDisabled;
	}

	public String validateAddedDeviceInList() {
		logger.info("Validating added device in the list using UIN: {}", randomUIN);
		driver.navigate().back();
		WebElement search = wait.until(ExpectedConditions.visibilityOfElementLocated(SEARCH_FIELD));
		search.sendKeys(Constants.DEVICE_UID);
		WebElement searchButton = wait.until(ExpectedConditions.visibilityOfElementLocated(SEARCH_BUTTON));
		searchButton.click();
		js.executeScript("window.scrollBy(0,5000);");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return driver.findElement(By.xpath(".//td[1]")).getText();
	}

	public List<String> areTableHeadersPresent() {
		return table.getTableHeaders(TABLE);
	}

	public boolean areAllViewButtonsEnabled() {
		return table.areViewButtonsEnabled(TABLE);
	}

	public boolean areAllDeleteButtonsEnabled() {
		return table.areDeleteButtonsEnabled(TABLE);
	}

	public boolean isSearchBoxEnabled() {
		WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(SEARCH_BOX_INPUT));
		actions.highlightElement(searchBox, "solid purple");
		boolean isEnabled = searchBox.isEnabled();
		logger.info("Search box enabled: {}", isEnabled);
		return isEnabled;
	}

	public boolean isSearchBoxClickable() {
		WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(SEARCH_BOX_INPUT));
		actions.highlightElement(searchBox, "solid purple");
		boolean isClickable = searchBox.isEnabled();
		logger.info("Search box clickable: {}", isClickable);
		return isClickable;
	}

	public boolean isSearchButtonEnabled() {
		WebElement searchButton = wait.until(ExpectedConditions.visibilityOfElementLocated(SEARCH_BOX_BTN));
		actions.highlightElement(searchButton, "solid purple");
		boolean isEnabled = searchButton.isEnabled();
		logger.info("Search button enabled: {}", isEnabled);
		return isEnabled;
	}

	public boolean isSearchButtonClickable() {
		WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(SEARCH_BOX_BTN));
		actions.highlightElement(searchButton, "solid purple");
		boolean isClickable = searchButton.isEnabled();
		logger.info("Search button clickable: {}", isClickable);
		return isClickable;
	}

	public String searchProductionDevice() {
		WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(SEARCH_BOX_INPUT));
		actions.highlightElement(searchBox, "solid purple");
		searchBox.clear();
		searchBox.sendKeys(Constants.DEVICE_UID);

		WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(SEARCH_BOX_BTN));
		actions.highlightElement(searchButton, "solid purple");
		searchButton.click();

		WebElement uinOfDevice = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//td[1]")));
		return uinOfDevice.getText();
	}

	public boolean clickViewButtonForUpdate() {
		return table.clickFirstViewButton(TABLE);
	}

	public boolean areUIDIMEIICCIDReadOnly() {
		WebElement uidField = wait.until(ExpectedConditions.visibilityOfElementLocated(UID));
		WebElement imeiField = wait.until(ExpectedConditions.visibilityOfElementLocated(IMEI));
		WebElement iccidField = wait.until(ExpectedConditions.visibilityOfElementLocated(ICCID));

		boolean isUIDReadOnly = uidField.getDomAttribute("readonly") != null;
		boolean isIMEIReadOnly = imeiField.getDomAttribute("readonly") != null;
		boolean isICCIDReadOnly = iccidField.getDomAttribute("readonly") != null;

		logger.info("UID field read-only: {}", isUIDReadOnly);
		logger.info("IMEI field read-only: {}", isIMEIReadOnly);
		logger.info("ICCID field read-only: {}", isICCIDReadOnly);

		return isUIDReadOnly && isIMEIReadOnly && isICCIDReadOnly;
	}

	public boolean isUpdateButtonVisible() {
		WebElement updateButton = wait.until(ExpectedConditions.visibilityOfElementLocated(UPDATE_BTN));
		actions.highlightElement(updateButton, "solid purple");
		boolean isVisible = updateButton.isDisplayed();
		logger.info("Update button visibility: {}", isVisible);
		return isVisible;
	}
}

