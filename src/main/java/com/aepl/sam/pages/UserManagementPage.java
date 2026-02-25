package com.aepl.sam.pages;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.time.Duration;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aepl.sam.utils.Constants;
import com.aepl.sam.locators.UserManagementPageLocators;
import com.aepl.sam.utils.RandomGeneratorUtils;
import com.aepl.sam.utils.Result;

public class UserManagementPage extends UserManagementPageLocators {
	private WebDriver driver;
	private WebDriverWait wait;
	private RandomGeneratorUtils random;
	private final Logger logger = LogManager.getLogger(this.getClass().getSimpleName());

	public UserManagementPage(WebDriver driver, WebDriverWait wait) {
		this.driver = driver;
		this.wait = wait;
		this.random = new RandomGeneratorUtils();
	}

	private String randomFirstName;
	private String randomFirstName2;
	private String randomLastName;
	private String randomLastName2;

	public String navBarLink() {
		try {
			logger.info("Navigating to User Management page...");
			WebElement user = wait.until(ExpectedConditions.visibilityOfElementLocated(USER));
			user.click();

			WebElement user_man = wait.until(ExpectedConditions.visibilityOfElementLocated(USR_MANAGEMENT_LINK));
			user_man.click();

			logger.info("Successfully navigated to User Management page.");
		} catch (Exception e) {
			logger.error("Error navigating to User Management: {}", e.getMessage(), e);
		}
		return driver.getCurrentUrl();
	}

	public String backButton() {
		try {
			logger.info("Clicking back button...");
			WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(BACK_BUTTON));

			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].style.border = 'solid purple'", element);

			element.click();
			Thread.sleep(1000);

			logger.info("Clicked on back button: {}", element.getText());
		} catch (Exception e) {
			logger.error("Error clicking back button: {}", e.getMessage(), e);
		}
		return navBarLink();
	}

	public String refreshButton() {
		try {
			logger.info("Clicking refresh button...");
			WebElement refreshBtn = wait.until(ExpectedConditions.elementToBeClickable(REFRESH_BUTTON));

			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].style.border = 'solid purple'", refreshBtn);

			Thread.sleep(20);
			refreshBtn.click();

			WebElement pageTitleElement = wait.until(ExpectedConditions.visibilityOfElementLocated(PAGE_TITLE));
			String pageTitle = pageTitleElement.getText();
			logger.info("Page refreshed, current title: {}", pageTitle);
			return pageTitle;
		} catch (Exception e) {
			logger.error("Error clicking refresh button: {}", e.getMessage(), e);
		}
		return "No Data Found!!!";
	}

	public String clickAddUserBtn() {
		String result = Result.FAIL.getValue();
		try {
			logger.info("Clicking 'Add User' button...");
			Thread.sleep(1000);

			WebElement addUser = driver.findElement(ADD_USR_BTN);
			addUser.click();
			Thread.sleep(1000);

			logger.info("'Add User' button clicked successfully.");
			result = "Add User Button Clicked Successfully";
		} catch (Exception e) {
			logger.error("Error clicking 'Add User' button: {}", e.getMessage(), e);
			result = "Error clicking 'Add User' button.";
		}
		return result;
	}

	public void addUserProfilePicture() {
		try {
			logger.info("Uploading user profile picture...");
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");

			WebElement uploadProfile = driver.findElement(PROF_BTN);
			uploadProfile.click();

			StringSelection selection = new StringSelection(
					"D:\\Sampark_Automation\\SAM_AUTO\\src\\test\\resources\\SampleUpload\\dp.jpg");
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);

			Thread.sleep(1000);

			Robot fileHandler = new Robot();
			fileHandler.keyPress(KeyEvent.VK_CONTROL);
			fileHandler.keyPress(KeyEvent.VK_V);
			fileHandler.keyRelease(KeyEvent.VK_V);
			fileHandler.keyRelease(KeyEvent.VK_CONTROL);
			Thread.sleep(1000);
			fileHandler.keyPress(KeyEvent.VK_ENTER);
			fileHandler.keyRelease(KeyEvent.VK_ENTER);
		} catch (Exception e) {
			logger.error("Error uploading user profile picture: {}", e.getMessage(), e);
		}
	}

	public void addAndUpdateUser(String operation) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		randomFirstName = random.generateRandomString(4);
		randomFirstName2 = random.generateRandomString(5);
		randomLastName = random.generateRandomString(4);
		randomLastName2 = random.generateRandomString(5);

		String userTypeToSelect = "QA Manager";

		try {
			logger.info("Performing '{}' operation for user...", operation);

			WebElement userTypeDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(USR_TYPE));
			js.executeScript("arguments[0].click();", userTypeDropdown);
			Thread.sleep(500);

			List<WebElement> options = driver.findElements(USR_TYPE_OPTIONS);

			boolean found = false;
			for (WebElement option : options) {
				if (option.getText().equalsIgnoreCase(userTypeToSelect)) {
					js.executeScript("arguments[0].scrollIntoView(true);", option);
					js.executeScript("arguments[0].click();", option);
					found = true;
					logger.info("Selected user type: {}", userTypeToSelect);
					break;
				}
			}

			if (!found) {
				logger.warn("User type '{}' not found in dropdown. Selecting first available option.",
						userTypeToSelect);
				js.executeScript("arguments[0].scrollIntoView(true);", options.get(0));
				js.executeScript("arguments[0].click();", options.get(0));
			}

			WebElement firstName = driver.findElement(FIRST_NAME);
			firstName.clear();
			firstName.sendKeys(operation.equalsIgnoreCase("add") ? randomFirstName : randomFirstName2);

			WebElement lastName = driver.findElement(LAST_NAME);
			lastName.clear();
			lastName.sendKeys(operation.equalsIgnoreCase("add") ? randomLastName : randomLastName2);

			WebElement email = driver.findElement(EMAIL);
			email.clear();
			email.sendKeys(operation.equalsIgnoreCase("add") ? random.generateRandomEmail().toLowerCase()
					: random.generateRandomEmail().toLowerCase());

			WebElement mobile = driver.findElement(MOBILE);
			mobile.clear();
			mobile.sendKeys(operation.equalsIgnoreCase("add") ? random.generateRandomNumber(10)
					: random.generateRandomNumber(10));

			WebElement country = driver.findElement(COUNTRY);
			country.clear();
			country.sendKeys(Constants.COUNTRY);

			WebElement state = driver.findElement(STATE);
			state.clear();
			state.sendKeys(Constants.STATE);

			WebElement status = driver.findElement(STATUS);
			status.click();
			// status.sendKeys(Keys.DOWN);
			status.sendKeys(Keys.ENTER);

			((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");

			WebElement actionBtn = driver.findElement(operation.equalsIgnoreCase("add") ? SUBMIT_BTN : UPDATE_BTN);
			actionBtn.click();
			Thread.sleep(1000);

			driver.get(Constants.USR_MAN);

			logger.info("User '{}' operation completed successfully.", operation);
		} catch (Exception e) {
			logger.error("Error during '{}' operation: {}", operation, e.getMessage(), e);
		}
	}

	public void checkDropdown() {
		try {
			logger.info("Checking dropdown options...");
			WebElement select = wait.until(ExpectedConditions.visibilityOfElementLocated(SELECT));
			select.click();

			List<WebElement> options = driver.findElements(DRP_OPTION);
			WebElement noDataImage = null;

			long end = System.currentTimeMillis() + 5000;
			while (System.currentTimeMillis() < end) {
				options = driver.findElements(DRP_OPTION);
				if (!options.isEmpty()) {
					break;
				}

				List<WebElement> noDataImages = driver.findElements(NO_DATA_IMAGE);
				if (!noDataImages.isEmpty() && noDataImages.get(0).isDisplayed()) {
					noDataImage = noDataImages.get(0);
					break;
				}

				Thread.sleep(200);
			}

			if (!options.isEmpty()) {
				for (WebElement op : options) {
					Thread.sleep(1000);
					op.click();
				}

				for (int i = options.size() - 1; i >= 0; i--) {
					Thread.sleep(1000);
					options.get(i).click();
				}

				logger.info("Dropdown options verified successfully.");
			} else if (noDataImage != null) {
				select.click();

				if (noDataImage.isDisplayed()) {
					logger.info("No data available: 'no data' image displayed correctly.");
				} else {
					logger.warn("No data image found but not visible.");
				}
			} else {
				logger.warn("Neither dropdown options nor no data image appeared.");
			}
		} catch (Exception e) {
			logger.error("Error checking dropdown: {}", e.getMessage(), e);
		}
	}

	public void searchAndViewUser() {
		try {
			logger.info("Searching and viewing user...");

			WebElement search = driver.findElement(SEARCH_FIELD);
			search.clear();
			search.sendKeys(randomFirstName);
			search.sendKeys(Keys.ENTER);
			Thread.sleep(1000); // Prefer WebDriverWait in real scenarios

			List<WebElement> eyeIcons = driver.findElements(EYE_ICON);

			if (!eyeIcons.isEmpty() && eyeIcons.get(0).isDisplayed()) {
				eyeIcons.get(0).click();
				Thread.sleep(1000);
				logger.info("User search and view successful.");
			} else {
				// Check for toast message
				List<WebElement> toastMessages = driver.findElements(TOAST_MESSAGE);
				boolean toastValidated = false;

				for (WebElement toast : toastMessages) {
					if (toast.isDisplayed() && toast.getText().contains("No data found")) {
						logger.info("No user found: Toast message displayed - {}", toast.getText());
						toastValidated = true;
						break;
					}
				}

				if (!toastValidated) {
					logger.warn("No user found: Eye icon not present and no 'No data found' toast/message shown.");
				}
			}
		} catch (Exception e) {
			logger.error("Error searching and viewing user: {}", e.getMessage(), e);
		}
	}

	public String deleteUser() {
		try {
			searchAndViewUser();
			Thread.sleep(1000);

			List<WebElement> deleteButtons = driver.findElements(DELETE_ICON);

			if (deleteButtons.size() >= 3 && deleteButtons.get(2).isDisplayed()) {
				deleteButtons.get(2).click();

				WebDriverWait alertWait = new WebDriverWait(driver, Duration.ofSeconds(3));
				alertWait.until(ExpectedConditions.alertIsPresent());
				Alert alert = driver.switchTo().alert();
				alert.accept();

				Thread.sleep(1000);
				logger.info("User deleted successfully.");
				return "User deleted successfully";
			} else {
				logger.warn("Delete button not found or not clickable.");

				List<WebElement> toastMessages = driver.findElements(TOAST_MESSAGE);
				for (WebElement toast : toastMessages) {
					if (toast.isDisplayed() && toast.getText().contains("No data found")) {
						logger.info("Toast displayed: {}", toast.getText());
						return "No user found: " + toast.getText();
					}
				}

				return "Delete button not found and no user available.";
			}

		} catch (Exception e) {
			logger.error("Error deleting user: {}", e.getMessage(), e);
			return "Error deleting user: " + e.getMessage();
		}
	}

}

