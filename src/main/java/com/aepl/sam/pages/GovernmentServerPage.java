package com.aepl.sam.pages;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import com.aepl.sam.locators.GovernmentServerPageLocators;
import com.aepl.sam.utils.CalendarActions;
import com.aepl.sam.utils.ConfigProperties;
import com.aepl.sam.utils.Constants;
import com.aepl.sam.utils.MouseActions;
import com.aepl.sam.utils.PageActionsUtil;
import com.aepl.sam.utils.RandomGeneratorUtils;
import com.aepl.sam.utils.TableUtils;

public class GovernmentServerPage extends GovernmentServerPageLocators {
	private WebDriver driver;
	private WebDriverWait wait;
	private CalendarActions calAct;
	private LoginPage loginPage;
	private PageActionsUtil comm;
	private String randomStateName;
	private String randomStateAbr;
	private RandomGeneratorUtils random;
	private TableUtils table;
	private static final Logger logger = LogManager.getLogger(GovernmentServerPage.class);
	private SoftAssert softAssert = new SoftAssert();

	public GovernmentServerPage(WebDriver driver, WebDriverWait wait, MouseActions action) {
		this.driver = driver;
		this.wait = wait;
		this.calAct = new CalendarActions(this.driver, this.wait);
		this.loginPage = new LoginPage(driver, wait);
		this.comm = new PageActionsUtil(driver, wait);
		this.random = new RandomGeneratorUtils();
		this.table = new TableUtils(wait);
	}

	public Boolean navBarLink() {
		boolean isViewed = false;
		try {
			logger.info("Navigating to Government Server page...");
			WebElement deviceUtil = wait.until(ExpectedConditions.visibilityOfElementLocated(DEVICE_UTILITY));
			softAssert.assertTrue(deviceUtil.getText().equalsIgnoreCase("DEVICE UTILITY"));
			deviceUtil.click();

			WebElement govServer = wait.until(ExpectedConditions.visibilityOfElementLocated(GOVERNMENT_NAV_LINK));
			govServer.click();

			if (driver.getCurrentUrl().equals(Constants.GOV_LINK)) {
				isViewed = true;
			}
			logger.info("Successfully navigated to User Management page.");
		} catch (Exception e) {
			logger.error("Error navigating to User Management: {}", e.getMessage(), e);
		}
		return isViewed;
	}

	public String verifyPageTitle() {
		WebElement pageTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("page-title")));
		comm.highlightElement(pageTitle, "solid purple");
		softAssert.assertEquals(pageTitle, "Government Server", "The title of the page did not matched");
		return pageTitle.getText();
	}

	public String refreshButton() {
		try {
			WebElement refreshBtn = wait.until(ExpectedConditions.elementToBeClickable(REFRESH_BUTTON));
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].style.border = 'solid purple'", refreshBtn);
			Thread.sleep(20);
			refreshBtn.click();

			WebElement page_title = wait.until(ExpectedConditions.visibilityOfElementLocated(PAGE_TITLE));
			String pageTitle = page_title.getText();
			logger.info("Page refreshed, title: {}", pageTitle);
			return pageTitle;
		} catch (Exception e) {
			logger.error("Error in refreshButton: {}", e.getMessage());
		}
		return "No Data Found!!!";
	}

	public String addGovernmentServer() {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("window.scrollTo(0, 0);");
			WebElement addGovButton = wait.until(ExpectedConditions.visibilityOfElementLocated(ADD_GOV_SER));
			comm.highlightElement(addGovButton, "solid purple");
			Thread.sleep(500);
			addGovButton.click();

			WebElement componentTitle = driver.findElement(COMPONENT_TITLE);
			logger.info("Navigated to Add Government Server form.");
			return componentTitle.getText();
		} catch (Exception e) {
			logger.error("Error adding government server: {}", e.getMessage());
		}
		return "Failed to click the add government server button or navigate to the next page.";
	}

	public String manageGovServer(String actionType) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		try {

			WebElement reload = wait.until(
					ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".action-button.reload-button")));

			reload.click();

			Thread.sleep(200);

			if (actionType.equalsIgnoreCase("add")) {
				randomStateName = "SURAJ" + random.generateRandomString(5);
				randomStateAbr = "SA" + random.generateRandomString(3);
			}

			fillGovServerForm(actionType, randomStateName, randomStateAbr);
			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");

			WebElement button = wait.until(
					ExpectedConditions.elementToBeClickable(actionType.equalsIgnoreCase("add") ? SUBMIT : UPDATE));
			comm.highlightElement(button, "solid purple");
			button.click();

			Thread.sleep(200);

			WebElement toast = wait.until(ExpectedConditions.visibilityOfElementLocated(TOAST_MSG));
			String message = toast.getText();
			logger.info("Manage government server result: {}", message);
			return message;
		} catch (Exception e) {
			logger.error("Error in manageGovServer: {}", e.getMessage());
			return "Error: " + e.getMessage();
		}
	}

	private void fillGovServerForm(String actionType, String stateName, String stateAbr) {
		boolean isUpdate = actionType.equalsIgnoreCase("update");

		try {
			if (!isUpdate) {
				driver.findElement(STATE).sendKeys(stateName);
				driver.findElement(STATE_ABR).sendKeys(stateAbr);
			}

			WebElement ip1 = driver.findElement(GOV_IP1);
			if (isUpdate) {
				ip1.clear();
			}
			ip1.sendKeys(isUpdate ? "255.255.255.001" : "255.255.255.255");

			WebElement port1 = driver.findElement(GOV_PORT1);
			if (isUpdate) {
				port1.clear();
			}
			port1.sendKeys(isUpdate ? "9999" : "8888");

			WebElement ip2 = driver.findElement(GOV_IP2);
			if (isUpdate) {
				ip2.clear();
			}
			ip2.sendKeys(isUpdate ? "255.255.255.001" : "255.255.255.255");

			WebElement port2 = driver.findElement(GOV_PORT2);
			if (isUpdate) {
				port2.clear();
			}
			port2.sendKeys(isUpdate ? "6666" : "7777");

			WebElement enabled = driver.findElement(STATE_ENABLED);
			if (isUpdate) {
				enabled.clear();
			}
			enabled.sendKeys(isUpdate ? "FALSE" : "TRUE");

			logger.debug("Filled form with state: {}, abbreviation: {}", stateName, stateAbr);
		} catch (Exception e) {
			logger.error("Error filling government server form: {}", e.getMessage());
		}
	}

	public void searchGovServer(String name) {
		try {
			WebElement search = driver.findElement(SEARCH_BOX_INPUT);
			search.clear();
			search.sendKeys(name);
			Thread.sleep(500);
			driver.findElement(SEARCH_BOX_BTN).click();
			logger.info("Searched for government server: {}", name);
		} catch (Exception e) {
			logger.error("Error searching government server: {}", e.getMessage());
		}
	}

	public boolean searchAndView() {
		try {
			wait.until(ExpectedConditions.urlToBe(Constants.GOV_LINK));

			WebElement search = driver.findElement(SEARCH_BOX_INPUT);
			comm.highlightElement(search, "solid purple");

			search.sendKeys(randomStateName);

			WebElement searchBtn = driver.findElement(SEARCH_BOX_BTN);
			comm.highlightElement(searchBtn, "solid purple");
			searchBtn.click();

			Thread.sleep(500);
			driver.findElement(EYE_ICON).click();

			Thread.sleep(500);
			logger.info("Search and view completed for: {}", randomStateName);
			return true;
		} catch (Exception e) {
			logger.error("Error in searchAndView: {}", e.getMessage());
			return false;
		}
	}

	public boolean addFirmware() {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");

			driver.findElement(ADD_FIRM).click();
			driver.findElement(FRM_NAME).sendKeys(Constants.FIRMWARE);
			driver.findElement(FRM_DSC).sendKeys("This is an auto generated desc from selenium");

			driver.findElement(FILE_UPLOAD).click();
			Thread.sleep(500);

			StringSelection selection = new StringSelection(
					"D:\\Sampark_Automation\\SAM_AUTO\\src\\test\\resources\\SampleUpload\\TCP01.bin");
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
			Robot robot = new Robot();
			Thread.sleep(500);
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_CONTROL);
			Thread.sleep(500);
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);

			LocalDateTime date = LocalDateTime.now();
			String currentDate = String.format("%02d-%02d-%04d", date.getDayOfMonth(), date.getMonthValue(),
					date.getYear());
			calAct.selectDate(CAL_BTN, currentDate);

			selectManager(QA_MANAGER_SELECT, Constants.QA_MAN);
			selectManager(SOFT_MANAGER_SELECT, Constants.SOFT_MAN);

			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
			Thread.sleep(500);
			driver.findElement(SUBMIT).click();

			logger.info("Firmware added successfully.");
			driver.navigate().back();
			return true;
		} catch (Exception e) {
			logger.error("Error in addFirmware: {}", e.getMessage());
			return false;
		}
	}

	private void selectManager(By dropdown, String name) {
		try {
			WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(dropdown));
			element.click();
			List<WebElement> options = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(DRP_OPTIONS));
			for (WebElement option : options) {
				if (option.getText().trim().equals(name)) {
					option.click();
					Thread.sleep(500);
					break;
				}
			}
			logger.debug("{} selected from dropdown", name);
		} catch (Exception e) {
			logger.error("Manager selection failed for {}: {}", name, e.getMessage());
		}
	}

	public String deleteGovServer() {
		try {
			searchGovServer(randomStateName);
			driver.findElement(DELETE_ICON).click();

			WebDriverWait alertWait = new WebDriverWait(driver, Duration.ofSeconds(5));
			Alert alert = alertWait.until(ExpectedConditions.alertIsPresent());
			alert.accept();

			WebElement toast = wait.until(ExpectedConditions.visibilityOfElementLocated(TOAST_MSG));
			String msg = toast.getText();
			logger.info("Deleted government server. Message: {}", msg);

			driver.navigate().refresh();
			return msg;
		} catch (Exception e) {
			logger.error("Error deleting government server: {}", e.getMessage());
			return "Deletion failed.";
		}
	}

	public boolean waitForApprovalMultipleWindows() {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("window.open()");
			ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());

			driver.switchTo().window(tabs.get(1));
			driver.get(Constants.BASE_URL);
			loginAsManager("QA Manager");

			js.executeScript("window.open()");
			tabs = new ArrayList<>(driver.getWindowHandles());
			driver.switchTo().window(tabs.get(2));
			driver.get(Constants.BASE_URL);
			loginAsManager("Software Manager");

			driver.close();
			driver.switchTo().window(tabs.get(1));
			driver.close();
			driver.switchTo().window(tabs.get(0));

			logger.info("Approval check completed in multiple windows.");
			return true;
		} catch (Exception e) {
			logger.error("Approval waiting failed: {}", e.getMessage());
			return false;
		}
	}

	public void loginAsManager(String role) {
		if (role.equalsIgnoreCase("QA Manager")) {
			loginPage.enterUsername(ConfigProperties.getProperty("qa_man"))
					.enterPassword(ConfigProperties.getProperty("qa_pass"));
		} else if (role.equalsIgnoreCase("Software Manager")) {
			loginPage.enterUsername(ConfigProperties.getProperty("soft_man"))
					.enterPassword(ConfigProperties.getProperty("soft_pass"));
		}
		loginPage.clickLogin();
		logger.info("{} logged in successfully.", role);
	}

	public boolean isSearchBoxEnabled() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		WebElement searchBox = driver.findElement(By.xpath("//input[contains(@formcontrolname, 'searchInput')]"));
		js.executeScript("arguments[0].scrollIntoView(true);", searchBox);
		comm.highlightElement(searchBox, "solid purple");
		softAssert.assertTrue(searchBox.isEnabled(), "No search box is enabled");
		return searchBox.isEnabled();
	}

	public boolean isSeachBoxDisplayed() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		WebElement searchBox = driver.findElement(By.xpath("//input[contains(@formcontrolname, 'searchInput')]"));
		js.executeScript("arguments[0].scrollIntoView(true);", searchBox);
		comm.highlightElement(searchBox, "solid purple");
		softAssert.assertTrue(searchBox.isDisplayed(), "No search box is displayed");
		return searchBox.isDisplayed();
	}

	public boolean isSeachButtonEnabled() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		WebElement searchButton = driver.findElement(By.className("search-btn"));
		js.executeScript("arguments[0].scrollIntoView(true);", searchButton);
		comm.highlightElement(searchButton, "solid purple");
		softAssert.assertTrue(searchButton.isEnabled(), "No search button is enabled");
		return searchButton.isEnabled();
	}

	public boolean isSeachButtonDisplayed() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		WebElement searchButton = driver.findElement(By.className("search-btn"));
		js.executeScript("arguments[0].scrollIntoView(true);", searchButton);
		comm.highlightElement(searchButton, "solid purple");
		softAssert.assertTrue(searchButton.isDisplayed(), "No search button is displayed");
		return searchButton.isEnabled();
	}

	public List<String> validaterGovernmentServerListTableHeaders() {
		return table.getTableHeaders(By.xpath("//table"));
	}

	// Data Fetched Successfully
	public String searchGovernmentServer() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0, -window.innerHeight);");
		WebElement searchBox = driver.findElement(By.xpath("//input[contains(@formcontrolname, 'searchInput')]"));
		searchBox.clear();
		searchBox.sendKeys(Constants.GOV_SERVER_NAME);

		WebElement searchButton = driver.findElement(By.className("search-btn"));
		searchButton.click();

		WebElement toastMessage = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//simple-snack-bar/div[1]")));
		comm.highlightElement(toastMessage, "solid purple");
		String message = toastMessage.getText().trim();

		softAssert.assertEquals(message, "Data Fetched Successfully",
				"No toast bar is appeared on screen or not data is searched");

		searchBox.clear();
		searchBox.sendKeys(Keys.ENTER);
		searchButton.click();

		return message;
	}

	public List<Map<String, String>> validateTableDataOfGovernmentServerTable() {
		List<Map<String, String>> data = table.getTableData(By.xpath("//table"),
				table.getTableHeaders(By.xpath("//table")));

		List<Map<String, String>> returnData = new ArrayList<>();
		if (data != null && !data.isEmpty()) {
			Map<String, String> normalizedRow = new LinkedHashMap<>();
			data.get(0).forEach((k, v) -> normalizedRow.put(k, String.valueOf(v)));
			returnData.add(normalizedRow);
		}

		return returnData;
	}

	// public List<Map<String, String>> validateTableDataOfGovernmentServerTable() {
	// List<Map<String, String>> data = table.getTableData(By.xpath("//table"),
	// table.getTableHeaders(By.xpath("//table")));
	//
	// List<Map<String, String>> returnData = new ArrayList<>();
	// if (data != null && !data.isEmpty()) {
	// Map<String, String> normalizedRow = new LinkedHashMap<>();
	//
	// data.get(0).forEach((k, v) -> {
	// if (!"ACTION".equalsIgnoreCase(k)) { // Ignore the ACTION column
	// normalizedRow.put(k, String.valueOf(v));
	// }
	// });
	//
	// returnData.add(normalizedRow);
	// }
	//
	// return returnData;
	// }

	public boolean isEyeButtonsAreVisibleOnTable() {
		return table.areViewButtonsEnabled(By.xpath("//table"));
	}

	public boolean isDeleteButtonsAreVisibleOnTable() {
		return table.areDeleteButtonsEnabled(By.xpath("//table"));
	}

	public boolean isAddGovernmentServerButtonIsVisible() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		WebElement add_gov = driver.findElement(By.xpath("//button[contains(text(), 'Add Government')]"));
		js.executeScript("arguments[0].scrollIntoView(true);", add_gov);
		comm.highlightElement(add_gov, "solid purple");

		return add_gov.isDisplayed();
	}

	public boolean isAddGovernmentServerButtonIsEnabled() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		WebElement add_gov = driver.findElement(By.xpath("//button[contains(text(), 'Add Government')]"));
		js.executeScript("arguments[0].scrollIntoView(true);", add_gov);
		return add_gov.isEnabled();
	}

	public String validateClickOnAddGovServerButton() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollTo(0, 0);");

		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		WebElement add_gov = wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath("//button[contains(text(), 'Add Government')]")));
		add_gov.click();

		return driver.findElement(By.className("page-title")).getText();
	}

	public boolean validateGovernmentServerDetailsInputs() {
		boolean allEnabled = true;
		try {
			WebElement form = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("form-section")));

			// Expected field names and placeholders
			Map<String, String> expectedFields = new LinkedHashMap<>();
			expectedFields.put("state", "State");
			expectedFields.put("stateCode", "State Abbreviation");
			expectedFields.put("govtIp1", "Govt. IP1");
			expectedFields.put("port1", "Port1");
			expectedFields.put("govtIp2", "Govt. IP2");
			expectedFields.put("port2", "Port2");
			expectedFields.put("stateEnable", "State Enable");

			for (Map.Entry<String, String> entry : expectedFields.entrySet()) {
				String controlName = entry.getKey();
				String placeholder = entry.getValue();

				// Locate input by its formcontrolname
				By inputLocator = By.xpath(".//input[@formcontrolname='" + controlName + "']");
				List<WebElement> inputs = form.findElements(inputLocator);

				softAssert.assertTrue(!inputs.isEmpty(),
						"âŒ Input with formcontrolname='" + controlName + "' not found in the form!");

				if (!inputs.isEmpty()) {
					WebElement input = inputs.get(0);
					boolean displayed = input.isDisplayed();
					boolean enabled = input.isEnabled();

					// Verify visibility and enabled state
					softAssert.assertTrue(displayed, "âŒ Input '" + placeholder + "' is not visible!");
					softAssert.assertTrue(enabled, "âŒ Input '" + placeholder + "' is not enabled!");

					logger.info("âœ… Verified input '{}' (formcontrolname='{}') - Visible: {}, Enabled: {}", placeholder,
							controlName, displayed, enabled);

					if (!displayed || !enabled) {
						allEnabled = false;
					}
				}
			}
		} catch (Exception e) {
			logger.error("Error validating Government Server Details form: {}", e.getMessage(), e);
			softAssert.fail("Exception during validation: " + e.getMessage());
			allEnabled = false;
		}
		return allEnabled;
	}

	public String validateSingleInputBox(String fieldName, String inputValue) {
		String xpath = String.format("//input[@id='%1$s' or @name='%1$s' or @class='%1$s' or @formcontrolname='%1$s']",
				fieldName);
		List<WebElement> inputBoxes = driver.findElements(By.xpath(xpath));

		if (inputBoxes.isEmpty()) {
			throw new NoSuchElementException("No input found for: " + fieldName);
		}

		return validateInputField(inputBoxes.get(0), inputValue);
	}

	private String validateInputField(WebElement inputBox, String inputValue) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		WebElement submitButton = driver.findElement(By.className("submit-button"));

		try {
			wait.until(ExpectedConditions.elementToBeClickable(inputBox));

			if (!inputBox.isEnabled() || Boolean.parseBoolean(inputBox.getDomAttribute("readonly"))) {
				throw new IllegalStateException("Input field is disabled or readonly");
			}

			String type = inputBox.getDomAttribute("type");

			if ("file".equalsIgnoreCase(type)) {
				if (inputValue != null && !inputValue.isEmpty()) {
					inputBox.sendKeys(inputValue); // file path
				}
			} else {
				inputBox.clear();
				if (inputValue != null && !inputValue.isEmpty()) {
					inputBox.sendKeys(inputValue);
				}
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

	public boolean isSubmitButtonDisabledWhenRequiredFieldsEmpty() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

		// Locate required input boxes
		WebElement stateInput = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@formcontrolname='state']")));
		WebElement stateAbrInput = wait.until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@formcontrolname='stateCode']")));
		WebElement submitButton = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("submit-button")));

		// Clear both required fields
		stateInput.clear();
		stateAbrInput.clear();

		// Small wait to let form validation trigger
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		boolean isDisabled = !submitButton.isEnabled();
		logger.info("Submit button is {} when required fields are empty", isDisabled ? "disabled" : "enabled");

		return isDisabled;
	}

	public String validateDuplicateStateEntry(String existingStateName, String existingStateCode) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(8));

		try {
			// Locate the input fields
			WebElement reload = wait.until(
					ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".action-button.reload-button")));

			reload.click();

			Thread.sleep(200);

			WebElement stateInput = wait.until(
					ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@formcontrolname='state']")));
			// Clear and enter existing data
			stateInput.click();
			stateInput.clear();
			stateInput.sendKeys(existingStateName);

			WebElement stateCodeInput = wait.until(
					ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@formcontrolname='stateCode']")));
			stateCodeInput.click();
			stateCodeInput.clear();
			stateCodeInput.sendKeys(existingStateCode);

			WebElement submitButton = wait
					.until(ExpectedConditions.elementToBeClickable(By.className("submit-button")));

			// Submit the form
			submitButton.click();

			// Wait for toast/snackbar message to appear
			WebElement toastMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
					"//simple-snack-bar/div | //div[contains(@class,'mat-mdc-snack-bar-label') or contains(@class,'toast-message')]")));

			String messageText = toastMessage.getText().replaceAll("(?i)close$", "").trim();
			logger.info("Toast message appeared: {}", messageText);
			return messageText;

		} catch (TimeoutException e) {
			logger.error("âŒ No toast message appeared after submitting duplicate state data");
			return "No toast message found";
		} catch (Exception e) {
			logger.error("âŒ Error validating duplicate entry: {}", e.getMessage(), e);
			return "Error: " + e.getMessage();
		}
	}

	public boolean validateTableDataThatIsInputed() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		WebElement searchBox = driver.findElement(By.xpath("//input[contains(@formcontrolname, 'searchInput')]"));
		js.executeScript("arguments[0].scrollIntoView(true);", searchBox);

		searchBox.clear();
		searchBox.sendKeys(randomStateName);
		searchBox.sendKeys(Keys.ENTER);

		List<Map<String, String>> tableData = table.getTableData(By.xpath("//table"),
				table.getTableHeaders(By.xpath("//table")));

		for (Map<String, String> row : tableData) {
			for (String cellValue : row.values()) {
				if (randomStateName.equals(cellValue)) {
					return true; // Found it
				}
			}
		}
		return false;
	}

	public String clickOnViewButton() {
		if (table.areViewButtonsEnabled(By.xpath("//table"))) {
			WebElement view = driver.findElement(By.xpath("//button[contains(., 'visibility')]"));
			view.click();
		}
		WebElement title = driver.findElement(By.className("page-title"));
		comm.highlightElement(title, "solid purple");
		return title.getText();
	}

	public boolean validateInputsWithTableData() {
		// Wait for the input to be visible
		WebElement stateInput = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@formcontrolname='state']")));

		String inputValue = stateInput.getDomAttribute("value").trim();

		logger.info("State input value: {}", inputValue);
		logger.info("Expected randomStateName: {}", randomStateName);

		if (inputValue.equals(randomStateName)) {
			logger.info("âœ… Input value matches the randomStateName");
			return true;
		} else {
			logger.error("âŒ Input value does NOT match. Expected: {}, Found: {}", randomStateName, inputValue);
			return false;
		}
	}

	public boolean validateUpdateButtonNotVisibleWhenNoChanges() {
		WebElement stateInput = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@formcontrolname='state']")));
		WebElement stateCodeInput = wait.until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@formcontrolname='stateCode']")));

		String initialStateName = stateInput.getDomAttribute("value").trim();

		String initialStateCode = stateCodeInput.getDomAttribute("value").trim();

		logger.info("Initial State Name: {}", initialStateName);
		logger.info("Initial State Code: {}", initialStateCode);

		List<WebElement> updateButtons = driver.findElements(By.xpath("//button[contains(@class,'update-button')]"));

		if (updateButtons.isEmpty()) {
			logger.info("âœ… Update button is not visible as expected when no changes are made.");
			return true;
		} else {
			WebElement updateButton = updateButtons.get(0);
			boolean isDisplayed = updateButton.isDisplayed();
			logger.error("âŒ Update button visibility status: {}", isDisplayed);
			return !isDisplayed;
		}
	}

	public List<String> verifyTableHeadersOfFirmwareTable() {
		return table.getTableHeaders(By.xpath("//table"));
	}

	public List<Map<String, String>> validateTableDataOfFirmwareListTable() {
		By tableLocator = By.xpath("//table");

		List<String> headers = table.getTableHeaders(tableLocator);
		if (headers == null || headers.isEmpty()) {
			logger.error("Validation failed: No table headers found for Firmware List table.");
			throw new IllegalStateException("Firmware List table headers are missing or not visible.");
		}

		List<Map<String, String>> tableData = table.getTableData(tableLocator, headers);

		if (tableData.isEmpty()) {
			logger.warn("Firmware List table has no data rows (possibly due to 'no-data-img').");
			return tableData;
		}

		for (Map<String, String> row : tableData) {
			if (row.size() != headers.size()) {
				logger.warn("Row data size ({}) does not match header count ({}). Row data: {}", row.size(),
						headers.size(), row);
			}
		}

		for (Map<String, String> row : tableData) {
			String firmwareName = row.getOrDefault("Firmware Name", "").trim();
			String version = row.getOrDefault("Version", "").trim();

			if (firmwareName.isEmpty()) {
				logger.warn("Missing Firmware Name in row: {}", row);
			}
			if (version.isEmpty()) {
				logger.warn("Missing Firmware Version in row: {}", row);
			}
		}

		logger.info("Firmware List table validated successfully. Total rows: {}", tableData.size());
		return tableData;
	}

	public String validateComponentTitle() {
		// Get the second element matching COMPONENT_TITLE
		WebElement component_title = driver.findElements(COMPONENT_TITLE).get(1);

		// Highlight
		comm.highlightElement(component_title, "violet");

		// Soft assert on element text
		softAssert.assertEquals(component_title.getText(), "Government Servers List", "Component title did not match");

		return component_title.getText();
	}

	public String validateComponentTitle1() {
		// Get the first element matching COMPONENT_TITLE
		WebElement component_title = driver.findElement(COMPONENT_TITLE);

		// Highlight
		comm.highlightElement(component_title, "violet");

		// Soft assert on element text
		softAssert.assertEquals(component_title.getText(), "Firmware Details", "Component title did not match");

		return component_title.getText();
	}

	public boolean isAddFirmwareDeviceButtonVisible() {
		return driver.findElement(ADD_FIRM).isDisplayed();
	}

	public boolean isAddFirmwareDeviceButtonEnabled() {
		return driver.findElement(ADD_FIRM).isEnabled();
	}

	public List<String> validateClickAddFirmwareDevice() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		WebElement add_firm = wait.until(ExpectedConditions.visibilityOfElementLocated(ADD_FIRM));
		js.executeScript("arguments[0].scrollIntoView(true);", add_firm);
		comm.highlightElement(add_firm, "solid purple");
		add_firm.click();

		List<WebElement> componentTitles = driver.findElements(COMPONENT_TITLE);
		WebElement component_title = componentTitles.get(componentTitles.size() - 1);
		js.executeScript("arguments[0].scrollIntoView(true);", component_title);
		comm.highlightElement(component_title, "solid purple");
		String title = component_title.getText();

		softAssert.assertEquals(title, "Firmware Master List", "No title is matched");

		return table.getTableHeaders(TABLE);
	}

	public boolean validateXButtonOnFirmwareDeviceListTable() {
		WebElement close_btn = wait.until(ExpectedConditions.elementToBeClickable((CLOSE_BUTTON)));
		close_btn.click();

		softAssert.assertEquals(driver.findElement(COMPONENT_TITLE), "Device Firmware List",
				"No component is visible on the screen");

		return driver.findElement(ADD_FIRM).isDisplayed();
	}

	public boolean searchBinFile() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		// Wait until the page is fully loaded
		wait.until(webDriver -> js.executeScript("return document.readyState").equals("complete"));

		// Scroll to top and wait until scroll completes
		js.executeScript("window.scrollTo(0, 0);");
		wait.until(webDriver -> {
			Number yOffset = (Number) js.executeScript("return window.pageYOffset;");
			return yOffset.longValue() == 0;
		});

		// Find and highlight search box
		WebElement search_box = wait.until(ExpectedConditions.visibilityOfElementLocated(SEARCH_BOX_INPUT));
		js.executeScript("arguments[0].scrollIntoView({block: 'center'});", search_box);
		comm.highlightElement(search_box, "solid purple");
		search_box.clear();
		search_box.sendKeys("TCP");

		// Click search button
		WebElement search = wait.until(ExpectedConditions.elementToBeClickable(SEARCH_BOX_BTN));
		js.executeScript("arguments[0].scrollIntoView({block: 'center'});", search);
		comm.highlightElement(search, "solid purple");
		search.click();

		// Wait for and validate toast message
		WebElement toast = wait.until(ExpectedConditions.visibilityOfElementLocated(TOAST_MSG));
		comm.highlightElement(toast, "solid purple");
		String toast_text = toast.getText();
		softAssert.assertEquals(toast_text, "Data Fetched Successfully", "No toast message appeared on screen");

		return toast.isDisplayed();
	}

	public List<String> validateTableHeadersOnFirmwareMaster() {
		return table.getTableHeaders(TABLE);
	}

	public List<Map<String, String>> validateTableDataOnFirmwareMasterListTable() {
		return table.getTableData(TABLE, table.getTableHeaders(TABLE));
	}

	public boolean validateSelectCheckbox() {
		List<WebElement> check_boxes = driver.findElements(CHECK_BOX);
		boolean isAnySelected = false;

		for (WebElement box : check_boxes) {
			comm.highlightElement(box, "solid purple");
			if (box.isDisplayed() && box.isSelected()) {
				isAnySelected = true;
				break;
			}
		}

		// If none are selected, select the first visible one
		if (!isAnySelected) {
			for (WebElement box : check_boxes) {
				if (box.isDisplayed()) {
					comm.highlightElement(box, "solid green");
					box.click(); // select the checkbox
					logger.info("No checkbox was selected. Selected one automatically.");
					isAnySelected = true;
					break;
				}
			}
		}

		return isAnySelected;
	}

	public boolean isSubmitButtonVisibleAndEnabled() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		WebElement submit = driver.findElement(SUBMIT);
		comm.highlightElement(submit, "solid purple");
		js.executeScript("arguments[0].scrollIntoView(true);", submit);

		return submit.isDisplayed() && submit.isEnabled();
	}

	public String validateClickOnSubmitButton() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		WebElement submit = driver.findElement(SUBMIT);
		comm.highlightElement(submit, "solid purple");
		js.executeScript("arguments[0].scrollIntoView(true);", submit);

		submit.click();

		WebElement toast = driver.findElement(TOAST_MSG_2);
		comm.highlightElement(toast, "solid purple");
		String toast_text = toast.getText();
		softAssert.assertEquals(toast_text, "Data Fetched Successfully", "No toast message is appeared on screen");

		return toast_text;
	}

	public boolean isDeleteButtonVisibleOnTable() {
		return table.areDeleteButtonsEnabled(TABLE);
	}

	public boolean clickOnDeleteAndValidate() {
		WebElement delete_btn = driver.findElement(DELETE_ICON);
		comm.highlightElement(delete_btn, "solid purple");
		delete_btn.click();

		Alert alert = wait.until(ExpectedConditions.alertIsPresent());
		alert.accept();

		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return table.isNoDataImagePresent(TABLE);
	}

	public boolean isFirmwareMasterButtonVisible() {
		driver.get(Constants.GOV_LINK);
		wait.until(ExpectedConditions.urlToBe(Constants.GOV_LINK));

		WebElement firmware_master = wait.until(ExpectedConditions.visibilityOfElementLocated(FIRMWARE_MASTER_BTN));
		return firmware_master.isDisplayed();
	}

	public boolean isFirmwareMasterButtonEnabled() {
		WebElement firmware_master = wait.until(ExpectedConditions.visibilityOfElementLocated(FIRMWARE_MASTER_BTN));
		return firmware_master.isEnabled();
	}

	public String clickAndValidateFirmwareMasterButton() {
		WebElement firmware_master = wait.until(ExpectedConditions.visibilityOfElementLocated(FIRMWARE_MASTER_BTN));
		comm.highlightElement(firmware_master, "solid purple");
		firmware_master.click();

		WebElement firmware_master_title = driver.findElement(PAGE_TITLE);
		comm.highlightElement(firmware_master_title, "solid purple");
		return firmware_master_title.getText();
	}

	public boolean isAddFirmwareButtonIsVisible() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollTo(0,0);");

		WebElement add_master_firmware = wait
				.until(ExpectedConditions.visibilityOfElementLocated(ADD_FIRM_FIRM_MASTER));

		comm.highlightElement(add_master_firmware, "solid purple");
		return add_master_firmware.isDisplayed();
	}

	public boolean isAddFirmwareButtonIsEnabled() {
		WebElement add_master_firmware = driver.findElement(ADD_FIRM_FIRM_MASTER);
		comm.highlightElement(add_master_firmware, "solid purple");
		return add_master_firmware.isEnabled();
	}

	public String clickAndValidateAddFimwareMasterButton() {
		WebElement add_master_firmware = driver.findElement(ADD_FIRM_FIRM_MASTER);
		comm.highlightElement(add_master_firmware, "solid purple");
		add_master_firmware.click();
		return driver.findElement(PAGE_TITLE).getText();
	}

	public boolean validateUploadFileInputBoxWithvalidFile() {
		try {
			// Locate the hidden input[type='file'] element
			WebElement fileUpload = driver.findElement(By.xpath("//input[@type='file' and contains(@accept, '.bin')]"));
			comm.highlightElement(fileUpload, "solid purple");

			// Upload the .bin file directly
			// Wait for the uploaded file name to appear in the readonly text box
			WebElement uploadedFileName = wait.until(
					ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@formcontrolname='fileName']")));
			comm.highlightElement(uploadedFileName, "solid purple");

			String fileNameText = uploadedFileName.getDomAttribute("value");
			logger.info("Uploaded File Name: {}", fileNameText);

			// Assert and return result
			softAssert.assertEquals(fileNameText, Constants.EXPECTED_FILE_NAME,
					"Uploaded file name does not match expected");

			if (fileNameText.equals(Constants.EXPECTED_FILE_NAME)) {
				logger.info("âœ… File uploaded successfully: {}", fileNameText);
				return true;
			} else {
				logger.warn("âš ï¸ Uploaded file name mismatch: {}", fileNameText);
				return false;
			}

		} catch (TimeoutException e) {
			logger.error("âŒ Timeout waiting for uploaded file name: {}", e.getMessage());
			return false;
		} catch (NoSuchElementException e) {
			logger.error("âŒ File upload element not found: {}", e.getMessage());
			return false;
		} catch (Exception e) {
			logger.error("âŒ Exception during file upload: {}", e.getMessage());
			return false;
		}
	}

	public boolean isReleaseDateFieldHasCurrentDateSelected() {
		WebElement releaseDateInput = driver.findElement(RELEASE_DATE_INPUT);
		comm.highlightElement(releaseDateInput, "solid purple");

		String selectedDate = releaseDateInput.getDomAttribute("value");
		LocalDate currentDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		String formattedCurrentDate = currentDate.format(formatter);

		softAssert.assertEquals(selectedDate, formattedCurrentDate,
				"Release Date field does not have the current date selected");

		return selectedDate.equals(formattedCurrentDate);
	}

	public boolean isFileUploadButtonVisible() {
		WebElement file_upload = wait.until(ExpectedConditions.visibilityOfElementLocated(FILE_UPLOAD));
		comm.highlightElement(file_upload, "solid purple");
		return file_upload.isDisplayed();
	}

	public boolean isFileUploadButtonClickable() {
		WebElement file_upload = wait.until(ExpectedConditions.visibilityOfElementLocated(FILE_UPLOAD));
		comm.highlightElement(file_upload, "solid purple");
		return file_upload.isEnabled();
	}

	public boolean isSelectDateButtonEnabledAndClickable() {
		WebElement date_picker_btn = wait.until(ExpectedConditions.visibilityOfElementLocated(CAL_BTN));
		comm.highlightElement(date_picker_btn, "solid purple");
		return date_picker_btn.isDisplayed() && date_picker_btn.isEnabled();
	}

	public boolean doesClickOnSelectDateButtonOpensDatePicker() {
		WebElement date_picker_btn = wait.until(ExpectedConditions.visibilityOfElementLocated(CAL_BTN));
		comm.highlightElement(date_picker_btn, "solid purple");
		date_picker_btn.click();

		WebElement date_picker_panel = wait.until(ExpectedConditions.visibilityOfElementLocated(DATE_PICKER_PANEL));
		comm.highlightElement(date_picker_panel, "solid purple");

		boolean isDisplayed = date_picker_panel.isDisplayed();
		date_picker_btn.click();
		return isDisplayed;
	}

	public boolean isFutureDateNotSelectable() {
		WebElement date_picker_btn = wait.until(ExpectedConditions.visibilityOfElementLocated(CAL_BTN));
		comm.highlightElement(date_picker_btn, "solid purple");
		date_picker_btn.click();

		WebElement date_picker_panel = wait.until(ExpectedConditions.visibilityOfElementLocated(DATE_PICKER_PANEL));
		comm.highlightElement(date_picker_panel, "solid purple");

		LocalDate futureDate = LocalDate.now().plusDays(10);
		String futureDay = String.valueOf(futureDate.getDayOfMonth());

		List<WebElement> dateCells = driver.findElements(By.xpath(
				"//div[contains(@class, 'mat-calendar-body-cell') and not(contains(@class, 'mat-calendar-body-disabled'))]"));

		for (WebElement cell : dateCells) {
			if (cell.getText().equals(futureDay)) {
				comm.highlightElement(cell, "solid red");
				logger.error("Future date {} is selectable, which is incorrect.", futureDate);
				return false;
			}
		}

		logger.info("Future date {} is not selectable as expected.", futureDate);
		return true;
	}

	public boolean isPastDateSelectable() {
		LocalDate pastDate = LocalDate.now().minusDays(10);
		String formattedDate = pastDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

		try {
			calAct.selectDate(CAL_BTN, formattedDate);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean isSubmitButtonDisabledWhenFieldsEmpty() {
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//mat-icon[normalize-space()='refresh']")))
				.click();
		WebElement submit_button = wait.until(ExpectedConditions.visibilityOfElementLocated(SUBMIT));
		comm.highlightElement(submit_button, "solid purple");
		return !submit_button.isEnabled();
	}

	public String fillFirmwareDetailsForm() {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//mat-icon[normalize-space()='refresh']")))
					.click();

			wait.until(
					ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@formcontrolname='firmwareName']")))
					.sendKeys("TestFirmware");

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("description")))
					.sendKeys("This is a test firmware description.");

			// driver.findElement(By.xpath("//button/mat-icon[contains(text(),
			// 'attach')]")).click();
			// wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@formcontrolname='fileName']")))
			// .sendKeys(BIN_FILE_PATH);

			WebElement fileInput = driver
					.findElement(By.xpath("//input[@type='file' and @formcontrolname='fileName']"));
			((JavascriptExecutor) driver).executeScript("arguments[0].style.display = 'block';", fileInput);
			fileInput.sendKeys(Constants.BIN_FILE_PATH);

			wait.until(ExpectedConditions.visibilityOfElementLocated(RELEASE_DATE_INPUT))
					.sendKeys(LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

			WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(SUBMIT));
			comm.highlightElement(submitButton, "solid purple");
			submitButton.click();

			WebElement toast = wait.until(ExpectedConditions.visibilityOfElementLocated(TOAST_MSG_2));
			comm.highlightElement(toast, "solid purple");

			String toastText = toast.getText();
			softAssert.assertEquals(toastText, "Data Fetched Successfully", "No toast message appeared on screen");
			return toastText;
		} catch (Exception e) {
			logger.error("Error while filling firmware details: {}", e.getMessage());
			return null;
		}
	}

	public boolean isLatestAddedFirmwareIsAtLast() {
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//mat-icon[normalize-space()='last_page']")))
				.click();

		List<String> tableHeaders = table.getTableHeaders(TABLE);

		List<Map<String, String>> tableData = table.getTableData(TABLE, tableHeaders);
		if (tableData.isEmpty()) {
			logger.warn("Firmware Master table has no data rows.");
			return false;
		}

		Map<String, String> lastRow = tableData.get(tableData.size() - 1);
		String firmwareName = lastRow.getOrDefault("Firmware Name", "").trim();

		if ("TestFirmware".equals(firmwareName)) {
			logger.info("âœ… Latest added firmware 'TestFirmware' is at the last row as expected.");
			return true;
		} else {
			logger.error("âŒ Latest added firmware is not at the last row. Found: {}", firmwareName);
			return false;
		}
	}

	public boolean areViewAndDeleteButtonsEnabled() {
		return table.areViewButtonsEnabled(TABLE) && table.areDeleteButtonsEnabled(TABLE);
	}

	public String deleteFirmwareAndValidate() {
		WebElement delete_btn = driver.findElement(DELETE_ICON);
		comm.highlightElement(delete_btn, "solid purple");
		delete_btn.click();

		Alert alert = wait.until(ExpectedConditions.alertIsPresent());
		alert.accept();

		WebElement toast = wait.until(ExpectedConditions.visibilityOfElementLocated(TOAST_MSG_2));
		comm.highlightElement(toast, "solid purple");
		String toast_text = toast.getText();
		softAssert.assertEquals(toast_text, "Data Fetched Successfully", "No toast message is appeared on screen");

		return toast_text;
	}

	public boolean checkPagination() {
		driver.get(Constants.GOV_LINK);
		try {
			comm.checkPagination();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}

