package com.aepl.sam.pages;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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
import org.testng.annotations.DataProvider;

import com.aepl.sam.locators.DeviceModelsPageLocators;
import com.aepl.sam.utils.PageActionsUtil;
import com.aepl.sam.utils.RandomGeneratorUtils;
import com.aepl.sam.utils.TableUtils;

public class DeviceModelsPage extends DeviceModelsPageLocators {
	private WebDriver driver;
	private WebDriverWait wait;
	private PageActionsUtil comm;
	private String randomModelCode;
	private RandomGeneratorUtils random;
	private TableUtils table;

	private static final Logger logger = LogManager.getLogger(DeviceModelsPage.class);

	public DeviceModelsPage(WebDriver driver, WebDriverWait wait) {
		this.driver = driver;
		this.wait = wait;
		this.comm = new PageActionsUtil(driver, wait);
		this.random = new RandomGeneratorUtils();
		this.table = new TableUtils(driver, wait);
	}

	public String navBarLink() {
		logger.info("Navigating to Device Models via navbar.");
		WebElement device_utility = wait.until(ExpectedConditions.visibilityOfElementLocated(DEVICE_UTILITY));
		Assert.assertEquals(device_utility.getText(), "DEVICE UTILITY", "The link is not matched");
		comm.highlightElement(device_utility, "solid purple");
		device_utility.click();

		WebElement devModel = wait.until(ExpectedConditions.visibilityOfElementLocated(DEVICE_MODELS));
		Assert.assertEquals(devModel.getText(), "MODEL", "No model name is matched");
		comm.highlightElement(devModel, "solid purple");
		devModel.click();

		String url = driver.getCurrentUrl();
		logger.debug("Navigated to URL: {}", url);
		return url;
	}

	public String ClickAddDeviceModel() {
		logger.info("Clicking on 'Add Device Model' button.");
		WebElement AddDeviceModel = wait.until(ExpectedConditions.visibilityOfElementLocated(ADD_DEVICE_MODELS));
		comm.highlightElement(AddDeviceModel, "solid purple");
		AddDeviceModel.click();

		WebElement addDeviceModelPageTitle = driver.findElement(PAGE_TITLE);
		comm.highlightElement(addDeviceModelPageTitle, "solid purple");
		String title = addDeviceModelPageTitle.getText();
		logger.debug("Add Device Model Page Title: {}", title);
		return title;
	}

	public String NewInputFields(String para) throws InterruptedException {
		randomModelCode = random.generateRandomString(3);
		String randomModelName = random.generateRandomString(6);
		String randomSerialSeq = random.generateRandomNumber(4);
		String randomHardwareVer = random.generateRandomNumber(4);

		logger.info("Filling input fields for '{}'", para);

		try {
			if (para.equalsIgnoreCase("add")) {
				logger.debug("Model code: SAM{}", randomModelCode);
				WebElement modelCode = wait.until(ExpectedConditions.visibilityOfElementLocated(MODEL_CODE));
				comm.highlightElement(modelCode, "solid purple");
				modelCode.sendKeys("SAM" + randomModelCode);

				WebElement AddModelName = wait.until(ExpectedConditions.visibilityOfElementLocated(ADD_MODEL_NAME));
				comm.highlightElement(AddModelName, "solid purple");
				AddModelName.sendKeys(randomModelName);

				WebElement AddModelSerialSequence = wait
						.until(ExpectedConditions.visibilityOfElementLocated(ADD_MODEL_SERIAL_SEQUENCE));
				comm.highlightElement(AddModelSerialSequence, "solid purple");
				AddModelSerialSequence.sendKeys(randomSerialSeq);

				WebElement AddHardwareVersion = wait
						.until(ExpectedConditions.visibilityOfElementLocated(ADD_HARDWARE_VERSION));
				comm.highlightElement(AddHardwareVersion, "solid purple");
				AddHardwareVersion.sendKeys(randomHardwareVer);

				Thread.sleep(500);

				WebElement AddSubmitButton = wait
						.until(ExpectedConditions.visibilityOfElementLocated(ADD_SUBMIT_BUTTON));
				comm.highlightElement(AddSubmitButton, "solid purple");
				AddSubmitButton.click();

				Thread.sleep(500);

				WebElement DeviceModelPageTitle = driver.findElement(PAGE_TITLE);
				comm.highlightElement(DeviceModelPageTitle, "solid purple");
				String title = DeviceModelPageTitle.getText();
				logger.info("Device model added successfully. Page title: {}", title);
				return title;

			} else if (para.equalsIgnoreCase("update")) {
				logger.debug("Updating model: SAM{}Updated", randomModelCode);
				WebElement modelCode = wait.until(ExpectedConditions.visibilityOfElementLocated(MODEL_CODE));
				comm.highlightElement(modelCode, "GREEN");
				modelCode.clear();
				modelCode.sendKeys("SAM" + randomModelCode + "Updated");

				WebElement UpdateModelName = wait.until(ExpectedConditions.visibilityOfElementLocated(ADD_MODEL_NAME));
				UpdateModelName.clear();
				comm.highlightElement(UpdateModelName, "solid purple");
				UpdateModelName.sendKeys(randomModelName + "Updated");

				WebElement UpdateModelSerialSequence = wait
						.until(ExpectedConditions.visibilityOfElementLocated(ADD_MODEL_SERIAL_SEQUENCE));
				UpdateModelSerialSequence.clear();
				comm.highlightElement(UpdateModelSerialSequence, "solid purple");
				UpdateModelSerialSequence.sendKeys(randomSerialSeq + "Updated");

				WebElement UpdateHardwareVersion = wait
						.until(ExpectedConditions.visibilityOfElementLocated(ADD_HARDWARE_VERSION));
				UpdateHardwareVersion.clear();
				comm.highlightElement(UpdateHardwareVersion, "solid purple");
				UpdateHardwareVersion.sendKeys(randomHardwareVer + "Updated");

				WebElement UpdateButton = wait.until(ExpectedConditions.visibilityOfElementLocated(ADD_UPDATE_BUTTON));
				comm.highlightElement(UpdateButton, "solid purple");
				UpdateButton.click();

				Thread.sleep(500);

				WebElement DeviceModelPageTitle = driver.findElement(PAGE_TITLE);
				String title = DeviceModelPageTitle.getText();
				logger.info("Device model updated successfully. Page title: {}", title);
				return title;
			}
		} catch (Exception e) {
			logger.error("Error while filling form for '{}': {}", para, e.getMessage(), e);
		}

		return "Not either device model is added or updated";
	}

	public String searchModel() throws InterruptedException {
		JavascriptExecutor js = (JavascriptExecutor) driver;

		js.executeScript("window.scrollTo(0, 0)");
		Thread.sleep(500);

		logger.info("Searching for model: SAM{}", randomModelCode);
		WebElement search = wait.until(ExpectedConditions.visibilityOfElementLocated(SEARCH_FIELD));
		comm.highlightElement(search, "solid purple");
		search.sendKeys("SAM" + randomModelCode);

		WebElement searchButton = wait.until(ExpectedConditions.visibilityOfElementLocated(SEARCH_BUTTON));
		comm.highlightElement(searchButton, "solid purple");
		searchButton.click();

		Thread.sleep(500);

		WebElement DeviceModelPageTitle = driver.findElement(PAGE_TITLE);
		comm.highlightElement(DeviceModelPageTitle, "solid purple");
		String title = DeviceModelPageTitle.getText();
		logger.info("Search completed. Page title: {}", title);
		return title;
	}

	public String viewModel() throws InterruptedException {
		logger.info("Viewing device model details.");
		WebElement viewButton = wait.until(ExpectedConditions.visibilityOfElementLocated(EYE_ICON));
		comm.highlightElement(viewButton, "solid purple");
		viewButton.click();

		Thread.sleep(500);

		WebElement DeviceModelPageTitle = driver.findElement(PAGE_TITLE);
		comm.highlightElement(DeviceModelPageTitle, "solid purple");
		String title = DeviceModelPageTitle.getText();
		logger.debug("View page title: {}", title);
		return title;
	}

	public String searchModel2() throws InterruptedException {
		logger.info("Searching for updated model: SAM{}Updated", randomModelCode);
		WebElement search = wait.until(ExpectedConditions.visibilityOfElementLocated(SEARCH_FIELD));
		comm.highlightElement(search, "solid purple");
		search.clear();
		search.sendKeys("SAM" + randomModelCode + "Updated");

		WebElement searchButton = wait.until(ExpectedConditions.visibilityOfElementLocated(SEARCH_BUTTON));
		comm.highlightElement(searchButton, "solid purple");
		searchButton.click();

		Thread.sleep(500);

		WebElement DeviceModelPageTitle = driver.findElement(PAGE_TITLE);
		comm.highlightElement(DeviceModelPageTitle, "solid purple");
		String title = DeviceModelPageTitle.getText();
		logger.info("Updated model search completed. Page title: {}", title);
		return title;
	}

	public String DeleteModel() throws InterruptedException {
		logger.info("Attempting to delete the model: SAM{}", randomModelCode);
		WebElement DeleteButton = wait.until(ExpectedConditions.visibilityOfElementLocated(DELETE_ICON));
		comm.highlightElement(DeleteButton, "solid purple");
		DeleteButton.click();

		Alert alert = driver.switchTo().alert();
		alert.accept();
		driver.navigate().refresh();
		Thread.sleep(500);

		WebElement DeviceModelPageTitle = driver.findElement(PAGE_TITLE);
		String title = DeviceModelPageTitle.getText();
		logger.info("Model deleted. Page title: {}", title);
		return title;
	}

	public String ValidatePageTitle() {
		return driver.findElement(PAGE_TITLE).getText();
	}

	public boolean isAddDeviceModelButtonVisible() {
		return driver.findElement(ADD_DEVICE_MODELS).isDisplayed();
	}

	public boolean isAddDeviceModelButtonEnable() {
		return driver.findElement(ADD_DEVICE_MODELS).isEnabled();
	}

	public String validateComponentTitle() {
		return driver.findElement(COMPONENT_TITLE).getText();
	}

	@DataProvider(name = "fieldValidationData")
	public static Object[][] fieldValidationData() {
		return new Object[][] {
				// only for blank inputs
				{ MODEL_CODE_INPUT, "", "This field is required and can't be only spaces." },
				{ MODEL_NAME_INPUT, "", "This field is required and can't be only spaces." },
				{ MODEL_SERIAL_INPUT, "", "This field is required and can't be only spaces." },
				{ HARDWARE_VERSION_INPUT, "", "This field is required and can't be only spaces." },

				// for spaces
//				{ MODEL_CODE_INPUT, " ", "Special characters and spaces are not allowed" },
//				{ MODEL_NAME_INPUT, " ", "Special characters and spaces are not allowed" },
//				{ MODEL_SERIAL_INPUT, " ", "Special characters and spaces are not allowed" },
//				{ HARDWARE_VERSION_INPUT, " ", "Special characters and spaces are not allowed" },

				// for special characters
				{ MODEL_CODE_INPUT, "@#@", "Special characters and spaces are not allowed" },
				{ MODEL_NAME_INPUT, "%#$^", "Special characters and spaces are not allowed" },
				{ MODEL_SERIAL_INPUT, " _)(", "Special characters and spaces are not allowed" },
				{ HARDWARE_VERSION_INPUT, "&* ", "Special characters and spaces are not allowed" }, };
	}

	public String isInputBoxHaveProperValidations(By inputLocator, String inputValue) {
		try {
			WebElement inputBox = driver.findElement(inputLocator);
			WebElement submitButton = driver.findElement(By.className("submit-button"));

			inputBox.click();

			Thread.sleep(500);

			inputBox.clear();
			if (inputValue != null && !inputValue.trim().isEmpty()) {
				inputBox.sendKeys(inputValue);
			}

			submitButton.click();

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
			WebElement errorElement = wait
					.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//mat-error")));

			return errorElement.getText();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return inputValue;
	}

	public boolean isSubmitButtonIsDisabled() {
		List<WebElement> inputBoxes = driver.findElements(By.xpath("//input"));
		for (WebElement input : inputBoxes) {
			input.clear();
		}

		WebElement submitButton = driver.findElement(By.className("submit-button"));

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
		wait.until(ExpectedConditions.visibilityOf(submitButton));

		boolean isDisabledByState = !submitButton.isEnabled();
		String disabledAttr = submitButton.getDomAttribute("disabled");
		boolean isDisabledByAttribute = disabledAttr != null
				&& (disabledAttr.equals("true") || disabledAttr.equals("disabled"));

		return isDisabledByState || isDisabledByAttribute;
	}

	// ------------------- SEARCH BOX & BUTTON VALIDATIONS ---------------------

	public boolean isSearchInputVisible() {
		WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(SEARCH_FIELD));
		comm.highlightElement(searchInput, "solid purple");
		return searchInput.isDisplayed();
	}

	public boolean isSearchInputEnabled() {
		WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(SEARCH_FIELD));
		return searchInput.isEnabled();
	}

	public boolean isSearchButtonVisible() {
		WebElement searchButton = wait.until(ExpectedConditions.visibilityOfElementLocated(SEARCH_BUTTON));
		comm.highlightElement(searchButton, "solid purple");
		return searchButton.isDisplayed();
	}

	public boolean isSearchButtonEnabled() {
		WebElement searchButton = wait.until(ExpectedConditions.visibilityOfElementLocated(SEARCH_BUTTON));
		return searchButton.isEnabled();
	}

	public List<String> validateTableHeaders() {
		return table.getTableHeaders(By.xpath("//table"));
	}

	public boolean validateTableDataWithRegex(List<Map<String, String>> expectedPatterns) {
		List<Map<String, String>> actualData = table.getTableData(By.xpath("//table"),
				table.getTableHeaders(By.xpath("//table")));

		// Log actual data for debugging
		for (Map<String, String> row : actualData) {
			logger.info("Actual Table Row: {}", row);
		}

		if (actualData.size() != expectedPatterns.size()) {
			logger.error("Row count mismatch! Expected: {}, Actual: {}", expectedPatterns.size(), actualData.size());
			return false;
		}

		for (int i = 0; i < expectedPatterns.size(); i++) {
			Map<String, String> expectedRowPattern = expectedPatterns.get(i);
			Map<String, String> actualRow = actualData.get(i);

			for (String column : expectedRowPattern.keySet()) {
				String pattern = expectedRowPattern.get(column);
				String actualValue = actualRow.get(column) != null
						? actualRow.get(column).replaceAll("[\\t\\u00A0]", "").trim()
						: "";

				if (!Pattern.compile(pattern, Pattern.CASE_INSENSITIVE).matcher(actualValue).matches()) {
					logger.error("Mismatch in column '{}': expected pattern='{}', actual='{}'", column, pattern,
							actualValue);
					return false;
				}
			}
		}

		logger.info("Table data matches expected patterns successfully!");
		return true;
	}

	public boolean isUpdateButtonEnabled() {
		WebElement modelCode = driver.findElement(MODEL_CODE_INPUT);
		WebElement modelName = driver.findElement(MODEL_NAME_INPUT);
		WebElement serialInput = driver.findElement(MODEL_SERIAL_INPUT);
		WebElement hardwareVersion = driver.findElement(HARDWARE_VERSION_INPUT);

		List<WebElement> listOfInputs = Arrays.asList(modelCode, modelName, serialInput, hardwareVersion);
		WebElement updateBtn = driver.findElement(ADD_UPDATE_BUTTON);

		// Check if any input field is dirty
		boolean isAnyDirty = listOfInputs.stream()
				.anyMatch(element -> element.getDomAttribute("class").contains("ng-dirty"));

		// Return true if button is enabled and any input is dirty
		return updateBtn.isEnabled() && isAnyDirty;
	}

}

