package com.aepl.sam.pages;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.locators.RelativeLocator;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aepl.sam.utils.Constants;
import com.aepl.sam.locators.DispatchedDevicesPageLocators;
import com.aepl.sam.utils.PageActionsUtil;
import com.aepl.sam.utils.TableUtils;

public class DispatchedDevicesPage extends DispatchedDevicesPageLocators {
	private static final Logger logger = LogManager.getLogger(DispatchedDevicesPage.class);

	private WebDriver driver;
	private WebDriverWait wait;
	private PageActionsUtil comm;
	private JavascriptExecutor js;
	private TableUtils table;

	public DispatchedDevicesPage(WebDriver driver, WebDriverWait wait) {
		this.driver = driver;
		this.wait = wait;
		this.comm = new PageActionsUtil(driver, wait);
		this.js = (JavascriptExecutor) driver;
		this.table = new TableUtils(driver, wait);
		logger.info("Initialized DispatchedDevicesPage");
	}

	public String validateSingleInputBox(String fieldName, String inputValue) {
		// Search for both input and mat-select fields
		String xpath = String.format("//*[@id='%1$s' or @name='%1$s' or @class='%1$s' or @formcontrolname='%1$s']",
				fieldName);
		// System.out.println("XPath used for locating the field: " + xpath);

		List<WebElement> elements = driver.findElements(By.xpath(xpath));

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
		logger.info("Navigating to Dispatched Devices page via nav bar");
		WebElement device_utility = wait.until(ExpectedConditions.visibilityOfElementLocated(DEVICE_UTILITY));
		comm.highlightElement(device_utility, "solid purple");
		device_utility.click();

		// WebElement devModel =
		// wait.until(ExpectedConditions.visibilityOfElementLocated(DISPATCHED_DEVICE));
		WebElement DisDevice = driver.findElement(DISPATCHED_DEVICE);
		comm.highlightElement(DisDevice, "solid purple");
		DisDevice.click();

		String currentUrl = driver.getCurrentUrl();
		logger.info("Navigation successful. URL: " + currentUrl);
		return currentUrl;
	}

	public String ClickAddDisDevice() {
		logger.info("Clicking Add Dispatched Device button");
		WebElement AddDisDevice = wait.until(ExpectedConditions.visibilityOfElementLocated(MANUAL_UPLOAD));
		comm.highlightElement(AddDisDevice, "solid purple");
		AddDisDevice.click();

		WebElement AddDisDevicePageTitle = driver.findElement(PAGE_TITLE);
		comm.highlightElement(AddDisDevicePageTitle, "solid purple");
		String title = AddDisDevicePageTitle.getText();
		logger.info("Page title after click: " + title);
		return title;
	}

	public String NewInputFields(String action) {
		switch (action.toLowerCase()) {
			case "add":
				return addDevice();
			case "update":
				return updateDevice();
			default:
				logger.warn("Invalid operation type: " + action);
				throw new IllegalArgumentException("Invalid action: " + action);
		}
	}

	public String addDevice() {
		logger.info("Filling input fields to ADD dispatched device");

		driver.findElement(REFRESHBTN).click();

		WebElement addUID = wait.until(ExpectedConditions.visibilityOfElementLocated(UID));
		comm.highlightElement(addUID, "GREEN");
		addUID.sendKeys(Constants.DEVICE_UID);

		WebElement customerPartNo = wait.until(ExpectedConditions.elementToBeClickable(CUST_PART_NO));
		comm.highlightElement(customerPartNo, "solid purple");
		customerPartNo.sendKeys(Constants.PART_NO_ADD);

		selectCustomer("AEPL");

		WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(SUBMIT_BTN));
		js.executeScript("arguments[0].scrollIntoView(true);", submitButton);
		submitButton.click();

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		WebElement toastMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(TOAST_MSG));
		comm.highlightElement(toastMessage, "solid purple");

		String toast = toastMessage.getText().trim();

		if (toast.contains("Device already dispatched for")) {
			logger.warn("Device already dispatched: " + toast);
			driver.findElement(BACK_BUTTON).click();

			// search that device and delete it to make way for new addition
			// This is only for test automation purpose - in real scenario this step won't
			// be needed
			SearchDevice();
			DeleteDevice();

		}

		String title = wait.until(ExpectedConditions.visibilityOfElementLocated(PAGE_TITLE)).getText();
		logger.info("Device added. Page title: " + title);
		return title;
	}

	private String updateDevice() {
		logger.info("Updating dispatched device");

		WebElement customerPartNo = wait.until(ExpectedConditions.elementToBeClickable(CUST_PART_NO));
		customerPartNo.sendKeys(Constants.PART_NO_UPDATE);

		selectCustomer("TML");

		WebElement updateButton = wait.until(ExpectedConditions.elementToBeClickable(UPDATE_BTN));
		js.executeScript("arguments[0].scrollIntoView(true);", updateButton);
		updateButton.click();

		return driver.findElement(PAGE_TITLE).getText();
	}

	private void selectCustomer(String customerName) {
		driver.findElement(RelativeLocator.with(By.tagName("span")).toRightOf(CUST_PART_NO)).click();
		List<WebElement> options = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(CUST_OPTIONS));
		for (WebElement option : options) {
			if (option.getText().equals(customerName)) {
				option.click();
				break;
			}
		}
	}

	public String SearchDevice() {
		logger.info("Searching for dispatched device");
		WebElement search = wait.until(ExpectedConditions.visibilityOfElementLocated(SEARCH_FIELD));
		comm.highlightElement(search, "solid purple");
		search.sendKeys("ACON4SA212240006474");

		WebElement searchButton = wait.until(ExpectedConditions.visibilityOfElementLocated(SEARCH_BUTTON));
		comm.highlightElement(searchButton, "solid purple");
		searchButton.click();

		js.executeScript("window.scrollBy(0,5000);");

		WebElement AddDisDevicePageTitle = driver.findElement(PAGE_TITLE);
		String title = AddDisDevicePageTitle.getText();
		logger.info("Search completed. Page title: " + title);
		return title;
	}

	public String viewDevice() {
		logger.info("Viewing dispatched device details");
		WebElement viewButton = wait.until(ExpectedConditions.visibilityOfElementLocated(EYE_ICON));
		viewButton.click();

		WebElement AddDisDevicePageTitle = driver.findElement(PAGE_TITLE);
		String title = AddDisDevicePageTitle.getText();
		logger.info("View loaded. Page title: " + title);
		return title;
	}

	public String DeleteDevice() {
		logger.info("Deleting dispatched device");
		WebElement DeleteButton = wait.until(ExpectedConditions.visibilityOfElementLocated(DELETE_ICON));
		comm.highlightElement(DeleteButton, "solid purple");
		DeleteButton.click();

		Alert alert = driver.switchTo().alert();
		alert.accept();

		WebElement AddDisDevicePageTitle = driver.findElement(PAGE_TITLE);
		String title = AddDisDevicePageTitle.getText();
		logger.info("Device deleted. Page title: " + title);
		return title;
	}

	public boolean isManualUploadButtonVisible() {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(MANUAL_UPLOAD)).isDisplayed();
	}

	public boolean isManualUploadButtonClickable() {
		return wait.until(ExpectedConditions.elementToBeClickable(MANUAL_UPLOAD)).isEnabled();
	}

	public String getPageTitleAfterClickingManualUpload() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollTo(0, 0);");
		wait.until(ExpectedConditions.elementToBeClickable(MANUAL_UPLOAD)).click();
		return wait.until(ExpectedConditions.visibilityOfElementLocated(PAGE_TITLE)).getText();
	}

	public boolean isCustomerNameFieldClickable() {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(CUST_PART_NO)).isEnabled()
				&& driver.findElement(RelativeLocator.with(By.tagName("span")).toRightOf(CUST_PART_NO)).isDisplayed();
	}

	public boolean isSubmitButtonDisabledOnMandatoryFieldsEmpty() {
		WebElement submitButton = wait.until(ExpectedConditions.visibilityOfElementLocated(SUBMIT_BTN));
		return !submitButton.isEnabled();
	}

	public boolean getSuccessToastMessage() {
		WebElement toastMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(TOAST_MSG));
		comm.highlightElement(toastMessage, "solid purple");

		String toast = toastMessage.getText().trim();
		logger.info("Toast message received: " + toast);

		return toast.equals("Data Fetched Successfully") || toast.contains("Device already dispatched for");
	}

	public boolean isBulkUploadButtonVisible() {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(BULK_UPLOAD)).isDisplayed();
	}

	public boolean isBulkUploadButtonClickable() {
		return wait.until(ExpectedConditions.elementToBeClickable(BULK_UPLOAD)).isEnabled();
	}

	public String getPageTitleAfterClickingBulkUpload() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		// scroll upwards to make the button visible
		js.executeScript("window.scrollTo(0, 0);");
		wait.until(ExpectedConditions.elementToBeClickable(BULK_UPLOAD)).click();
		return wait.until(ExpectedConditions.visibilityOfElementLocated(PAGE_TITLE)).getText();
	}

	public boolean isDownloadSampleLinkClickable() {
		for (int i = 0; i < 3; i++) {
			try {
				WebElement downloadLink = wait.until(ExpectedConditions.elementToBeClickable(DOWNLOAD_SAMPLE_LINK));
				comm.highlightElement(downloadLink, "solid purple");
				downloadLink.click();

				// Handle alert if present
				try {
					wait.until(ExpectedConditions.alertIsPresent()).accept();
				} catch (TimeoutException e) {
					// no alert shown, continue
				}

				return downloadLink.isEnabled();
			} catch (StaleElementReferenceException e) {
				logger.warn("StaleElementReferenceException caught. Retrying... Attempt: " + (i + 1));
			}
		}
		return false;
	}

	public boolean isSampleDownloadFileContentCorrect() {
		return comm.verifyCSVHeader(
				Paths.get(System.getProperty("user.home"), "Downloads", "Sample_Dispatch_Sheet.xlsx").toString(),
				"UID,Customer Name,TML Part Number");
	}

	public boolean isAttachmentButtonClickable() {
		return wait.until(ExpectedConditions.elementToBeClickable(ATTACHMENT_BTN)).isEnabled();
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

	// public String uploadFileAndSubmit() {
	// try {
	// WebElement fileInput =
	// wait.until(ExpectedConditions.presenceOfElementLocated(ATTACHMENT_BTN));
	//
	// // Use sendKeys directly (MUCH more reliable than Robot!)
	// String filePath =
	// Paths.get("src/test/resources/SampleUpload/Sample_Dispatch_Sheet.xlsx").toAbsolutePath()
	// .toString();
	//
	// Thread.sleep(200);
	//
	// fileInput.sendKeys(filePath);
	//
	// WebElement submitButton =
	// wait.until(ExpectedConditions.elementToBeClickable(SUBMIT_BTN));
	// submitButton.click();
	// logger.info("Submit button clicked.");
	//
	// WebElement toastMessage =
	// wait.until(ExpectedConditions.visibilityOfElementLocated(TOAST_MSG));
	// comm.highlightElement(toastMessage, "solid purple");
	// String message = toastMessage.getText().trim();
	// logger.info("Toast message received: {}", message);
	//
	// return message;
	//
	// } catch (Exception e) {
	// logger.error("Error during file upload and submission: {}", e.getMessage(),
	// e);
	// return "Error during file upload and submission.";
	// }
	// }

	public String uploadFileAndSubmit() {
		String message;
		try {
			WebElement fileInput = wait.until(ExpectedConditions.elementToBeClickable(ATTACHMENT_BTN));
			fileInput.click();
			String filePath = "D:\\AEPL_AUTOMATION\\SAM_AUTO\\src\\test\\resources\\SampleUpload\\Sample_Dispatch_Sheet.xlsx";
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
			comm.highlightElement(toastMessage, "solid purple");
			message = toastMessage.getText().trim().toString();
			logger.info("Toast message received: {}", message);
		} catch (Exception e) {
			logger.error("Error during file upload and submission: {}", e.getMessage(), e);
			return "Error during file upload and submission.";
		}
		return message;
	}

	public boolean isUploadedDispatchDeviceListVisible() {
		WebElement first = wait
				.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(UPLOADED_DISPATCHED_DEVICE_LIST)).get(0);
		comm.highlightElement(first, "solid purple");
		return first.isDisplayed();
	}

	public boolean isExportButtonDisabledOnNoDataInUploadedDispatchDeviceList() {
		driver.findElement(By.xpath("//img[@class='no-data-img']")).isDisplayed(); // ensure no data state
		WebElement exportButton = wait.until(ExpectedConditions.visibilityOfElementLocated(EXPORT_BTN));
		comm.highlightElement(exportButton, "solid purple");
		return !exportButton.isEnabled();
	}

	// check for no data state and if no data then simply return false and if data
	// is present then click export and verify file download
	public boolean doesExportButtonDownloadFileInUploadedDispatchDeviceList() {
		return comm.validateExportButton();
	}

	public List<String> getTableHeadersOfUploadedDispatchDeviceList() {
		if (driver.findElements(COMPONENT_TITLE).get(0).getText().contains("Uploaded Dispatch Device List")) {
			return table.getTableHeaders(TABLE_1);
		}

		return new ArrayList<>();
	}

	public boolean getDataOfUploadedDispatchDeviceListOnInvalidFileUpload() {
		return table.isNoDataImagePresent(TABLE);
	}

	public boolean validateRemarkInUploadedDispatchDeviceListOnValidFileUpload() {

		// Extract table data using normalized headers
		List<Map<String, String>> tableData = table.getTableData(TABLE_1, table.getTableHeaders(TABLE_1));

		for (Map<String, String> row : tableData) {

			// Get the value of the REMARK column (case-insensitive header search)
			String remark = row.entrySet().stream().filter(e -> e.getKey().equalsIgnoreCase("REMARK"))
					.map(Map.Entry::getValue).findFirst().orElse(null);

			// Validate remark
			if (remark == null || !remark.equalsIgnoreCase("OK")) {
				logger.warn("Invalid remark found: " + remark);
				return false;
			}
		}

		return true; // All remarks were OK
	}

	public boolean isSearchBoxVisible() {
		driver.findElement(BACK_BUTTON).click();
		return wait.until(ExpectedConditions.visibilityOfElementLocated(SEARCH_FIELD)).isDisplayed();
	}

	public boolean isSearchBoxClickable() {
		return wait.until(ExpectedConditions.elementToBeClickable(SEARCH_FIELD)).isEnabled();
	}

	public String validateSearchFunctionalityWithValidData() {
		return SearchDevice();
	}

	public boolean isSelectCustomerDropdownVisible() {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(DROPDOWN_MENU)).isDisplayed();
	}

	public boolean isSelectCustomerDropdownClickable() {
		return wait.until(ExpectedConditions.elementToBeClickable(DROPDOWN_MENU)).isEnabled();
	}

	public String validateSelectCustomerDropdownFunctionalityWithValidData() {

		WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(DROPDOWN_MENU));
		dropdown.click();

		List<WebElement> options = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(DRP_OPTIONS));
		for (WebElement option : options) {
			if (option.getText().equals("AEPL")) {
				option.click();
				break;
			}
		}

		js.executeScript("window.scrollBy(0,5000);");

		WebElement AddDisDevicePageTitle = driver.findElement(PAGE_TITLE);
		String title = AddDisDevicePageTitle.getText();
		logger.info("Search completed. Page title: " + title);
		return title;
	}

	public List<String> getTableHeadersOfDispatchedDeviceList() {
		return table.getTableHeaders(TABLE_1);
	}

	public boolean isViewButtonVisibleOnDispatchedDeviceList() {
		return table.areViewButtonsEnabled(TABLE_1);
	}

	public boolean clickOnViewButtonOnDispatchedDeviceTable() {
		return table.clickFirstViewButton(TABLE_1);
	}

	public String getPageTitleAfterClickingViewButton() {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(PAGE_TITLE)).getText();
	}

	public boolean isUIDFieldNonEditableOnUpdateDispatchedDevicePage() {
		WebElement uidField = wait.until(ExpectedConditions.visibilityOfElementLocated(UID));
		String readonlyAttr = uidField.getDomAttribute("readonly");
		return readonlyAttr != null && (readonlyAttr.equals("true") || readonlyAttr.equals("readonly"));
	}

	public String getUpdateDispatchedDeviceFormWithValidData() {
		// Assuming the UID is already filled and non-editable
		// fill other fields as needed
		WebElement customerPartNo = wait.until(ExpectedConditions.elementToBeClickable(CUST_PART_NO));
		customerPartNo.clear();
		customerPartNo.sendKeys(Constants.PART_NO_UPDATE);

		selectCustomer(Constants.CUSTOMER_UPDATE);
		return driver.findElement(PAGE_TITLE).getText();
	}

	public boolean isUpdateButtonDisabledOnMandatoryFieldsEmpty() {
		WebElement updateButton = wait.until(ExpectedConditions.visibilityOfElementLocated(UPDATE_BTN));
		return !updateButton.isEnabled();
	}

	public String getSuccessToastMessageAfterUpdatingDispatchedDeviceDetails() {
		// click on update button
		WebElement updateButton = wait.until(ExpectedConditions.elementToBeClickable(UPDATE_BTN));
		updateButton.click();

		WebElement toastMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(TOAST_MSG));
		comm.highlightElement(toastMessage, "solid purple");

		String toast = toastMessage.getText().trim();
		logger.info("Toast message received: " + toast);

		return toast;
	}

	public boolean isEditedDataUpdatedInDispatchedDeviceList() {
		List<Map<String, String>> tableData = table.getTableData(TABLE_1, table.getTableHeaders(TABLE_1));

		for (Map<String, String> row : tableData) {
			String customerNumber = row.entrySet().stream().filter(e -> e.getKey().equalsIgnoreCase("CUSTOMER NAME"))
					.map(Map.Entry::getValue).findFirst().orElse(null);

			if (customerNumber == null || !customerNumber.equalsIgnoreCase(Constants.CUSTOMER_UPDATE)) {
				logger.warn("Customer is not updated : " + customerNumber);
				return false;
			}
		}

		return true;
	}

	// public boolean isDeleteButtonVisibleOnDispatchedDeviceList() {
	// return table.areDeleteButtonsEnabled(TABLE_1);
	// }
	//
	// public boolean isDeleteButtonClickableOnDispatchedDeviceList() {
	// List<WebElement> deleteButtons = driver.findElements(DELETE_ICON);
	// for (WebElement deleteButton : deleteButtons) {
	// comm.highlightElement(deleteButton, "solid purple");
	// if (!deleteButton.isEnabled()) {
	// return false;
	// }
	// }
	// return true;
	// }
	//
	// public boolean clickDeleteButtonAndConfirmDeletion() {
	// SearchDevice();
	//
	// driver.findElement(DELETE_ICON).click();
	// return true;
	// }
	//
	// public String isClickCancelMakesCancellationOfPopUp() {
	// Alert alert = wait.until(ExpectedConditions.alertIsPresent());
	// String alertText = alert.getText();
	// alert.dismiss();
	// return alertText;
	// }
	//
	// public String isDispachedDeviceDeleted() {
	// driver.findElement(DELETE_ICON).click();
	// Alert alert = wait.until(ExpectedConditions.alertIsPresent());
	// alert.accept();
	//
	// WebElement toastMessage =
	// wait.until(ExpectedConditions.visibilityOfElementLocated(TOAST_MSG));
	// comm.highlightElement(toastMessage, "solid purple");
	//
	// String toast = toastMessage.getText().trim();
	// logger.info("Toast message received: " + toast);
	//
	// return toast;
	// }

	public boolean areDeleteButtonsVisibleAndEnabled() {
		List<WebElement> deleteButtons = driver.findElements(DELETE_ICON);

		if (deleteButtons.isEmpty()) {
			return false;
		}

		for (WebElement deleteButton : deleteButtons) {
			if (!deleteButton.isDisplayed() || !deleteButton.isEnabled()) {
				return false;
			}
		}
		return true;
	}

	public String openDeletePopupAndGetText() {
		WebElement deleteButton = driver.findElement(DELETE_ICON);
		deleteButton.click();

		Alert alert = wait.until(ExpectedConditions.alertIsPresent());
		return alert.getText();
	}

	public boolean cancelDeletePopup() {
		Alert alert = wait.until(ExpectedConditions.alertIsPresent());
		alert.dismiss();

		// After cancel, verify popup is gone
		try {
			wait.until(ExpectedConditions.alertIsPresent());
			return false; // popup still exists
		} catch (Exception e) {
			return true; // popup dismissed correctly
		}
	}

	public String confirmDeletionAndGetToast() {
		driver.findElement(SEARCH_BOX_INPUT).sendKeys(Constants.DEVICE_UID);
		driver.findElement(SEARCH_BOX_BTN).click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(DELETE_ICON)).click();

		Alert alert = wait.until(ExpectedConditions.alertIsPresent());
		alert.accept();

		WebElement toastMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(TOAST_MSG));
		return toastMessage.getText().trim();
	}

}


