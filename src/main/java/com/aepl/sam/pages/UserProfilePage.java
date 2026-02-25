package com.aepl.sam.pages;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aepl.sam.utils.Constants;
import com.aepl.sam.locators.UserProfilePageLocators;

import io.restassured.http.ContentType;

public class UserProfilePage extends UserProfilePageLocators {
	private WebDriver driver;
	private WebDriverWait wait;
	private final Logger logger = LogManager.getLogger(this.getClass().getSimpleName());

	public UserProfilePage(WebDriver driver, WebDriverWait wait) {
		this.driver = driver;
		this.wait = wait;
	}

	public String navBarLink() {
		try {
			logger.info("Navigating to User Profile page...");
			WebElement user = wait.until(ExpectedConditions.visibilityOfElementLocated(USER_PROFILE));
			user.click();

			WebElement govServer = wait.until(ExpectedConditions.visibilityOfElementLocated(PROFILE_LINK));
			govServer.click();
			logger.info("Successfully navigated to User Profile page.");
		} catch (Exception e) {
			logger.error("Error navigating to User Profile page: {}", e.getMessage(), e);
		}
		return driver.getCurrentUrl();
	}

	public String refreshButton() {
		try {
			logger.info("Clicking refresh button...");
			WebElement refreshBtn = wait.until(ExpectedConditions.elementToBeClickable(REFRESH_BUTTON));

			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].style.border = 'solid purple'", refreshBtn);

			Thread.sleep(20);
			refreshBtn.click();

			WebElement page_title = wait.until(ExpectedConditions.visibilityOfElementLocated(PAGE_TITLE));
			String pageTitle = page_title.getText();
			logger.info("Page refreshed, current title: {}", pageTitle);
			return pageTitle;
		} catch (Exception e) {
			logger.error("Error clicking refresh button: {}", e.getMessage(), e);
		}
		return "No Data Found!!!";
	}

	public void changePassword() {
		try {
			logger.info("Attempting to change password...");
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			JavascriptExecutor js = (JavascriptExecutor) driver;

			// Scroll to bottom to locate the Change Password button
			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");

			// Click on the "Change Password" button to open the modal
			WebElement changePass = wait.until(ExpectedConditions.elementToBeClickable(CHANGE_PASS));
			changePass.click();

			// Inside modal: locate and interact with fields
			WebElement curPass = wait.until(ExpectedConditions.visibilityOfElementLocated(CUR_PASS));
			curPass.clear();
			curPass.sendKeys(Constants.CUR_PASS);

			WebElement newPass = wait.until(ExpectedConditions.visibilityOfElementLocated(NEW_PASS));
			newPass.clear();
			newPass.sendKeys(Constants.NEW_PASS);

			// Click on confirm/change password button inside modal
			WebElement changePasswordBtn = wait.until(ExpectedConditions.elementToBeClickable(CHANGE_BTN));
			js.executeScript("arguments[0].scrollIntoView(true);", changePasswordBtn);
			changePasswordBtn.click();

			driver.findElement(By.className("custom-close-btn")).click();

			logger.info("Password changed successfully via modal.");
		} catch (Exception e) {
			logger.error("Error changing password: {}", e.getMessage(), e);
		}
	}

	public boolean uploadProfilePicture() {
		try {
			logger.info("Starting profile picture upload...");

			WebElement uploadProfile = driver.findElement(UPLOAD_PROFILE);
			uploadProfile.click();
			logger.info("Upload button clicked.");

			StringSelection selection = new StringSelection(FILE_PATH);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
			logger.info("Image path copied to clipboard.");

			Robot fileHandler = new Robot();
			Thread.sleep(500);

			fileHandler.keyPress(KeyEvent.VK_CONTROL);
			fileHandler.keyPress(KeyEvent.VK_V);
			fileHandler.keyRelease(KeyEvent.VK_V);
			fileHandler.keyRelease(KeyEvent.VK_CONTROL);
			logger.info("Image path pasted.");

			Thread.sleep(500);
			fileHandler.keyPress(KeyEvent.VK_ENTER);
			fileHandler.keyRelease(KeyEvent.VK_ENTER);
			logger.info("ENTER key pressed to confirm upload.");

			logger.info("Profile picture uploaded successfully.");
			return true;
		} catch (Exception e) {
			logger.error("Error uploading profile picture: {}", e.getMessage(), e);
			return false;
		}
	}

	public boolean updateProfileDetails() {
		try {
			logger.info("Updating profile details...");

			WebElement firstName = driver.findElement(FIRST_NAME);
			WebElement lastName = driver.findElement(LAST_NAME);
			WebElement email = driver.findElement(EMAIL);
			WebElement mobileNumber = driver.findElement(MOBILE_NUMBER);
			WebElement country = driver.findElement(COUNTRY);
			WebElement state = driver.findElement(STATE);
			WebElement updateBtn = driver.findElement(UPDATE);

			String firstNameValue = firstName.getDomAttribute("value");
			String lastNameValue = lastName.getDomAttribute("value");
			String emailValue = email.getDomAttribute("value");
			String mobileNumberValue = mobileNumber.getDomAttribute("value");
			String countryValue = country.getDomAttribute("value");
			String stateValue = state.getDomAttribute("value");

			firstName.clear();
			firstName.sendKeys(firstNameValue);
			lastName.clear();
			lastName.sendKeys(lastNameValue);
			email.clear();
			email.sendKeys(emailValue);
			mobileNumber.clear();
			mobileNumber.sendKeys(mobileNumberValue);
			country.clear();
			country.sendKeys(countryValue);
			state.clear();
			state.sendKeys(stateValue);

			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");

			Thread.sleep(2000);

			updateBtn.click();
			logger.info("Profile details updated successfully.");
			return true;
		} catch (Exception e) {
			logger.error("Error updating profile details: {}", e.getMessage(), e);
			return false;
		}
	}

	public String validateUserData() {
		try {
			logger.info("Validating User Profile data (UI vs API)...");

			// --- Collect UI Data ---
			Map<String, String> uiData = new HashMap<>();
			uiData.put("adminName", driver.findElement(ADM_NAME).getDomAttribute("value"));
			uiData.put("firstName", driver.findElement(FIRST_NAME).getDomAttribute("value"));
			uiData.put("lastName", driver.findElement(LAST_NAME).getDomAttribute("value"));
			uiData.put("email", driver.findElement(EMAIL).getDomAttribute("value"));
			uiData.put("mobileNumber", driver.findElement(MOBILE_NUMBER).getDomAttribute("value"));
			uiData.put("country", driver.findElement(COUNTRY).getDomAttribute("value"));
			uiData.put("state", driver.findElement(STATE).getDomAttribute("value"));
			uiData.put("role", driver.findElement(USR_ROLE).getDomAttribute("value"));

			logger.info("UI Profile Data: {}", uiData);

			// --- API Call ---
			io.restassured.response.Response response = io.restassured.RestAssured.given().relaxedHTTPSValidation()
					.accept(ContentType.JSON)
					.header("Authorization",
							"Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdXJhai5iaGFsZXJhb0BhY2NvbGFkZWVsZWN0cm9uaWNzLmNvbSIsImlhdCI6MTc1NTY2NTQ3NSwiZXhwIjoxNzU1Njg3MDc1fQ.6f-LWpuw7tbVDJVuTRfXVO6iWCT2sRRY5VdxLxqnAjE")
					.when().get("http://aepltest.accoladeelectronics.com:9090/users/getUserdetails?id=9").then()
					.statusCode(200).extract().response();

			// Print full response JSON on console
			// System.out.println("API Response:\n" + response.asPrettyString());

			// --- Extract API Data ---
			Map<String, String> apiData = new HashMap<>();
			apiData.put("adminName", response.jsonPath().getString("data.adminName"));
			apiData.put("firstName", response.jsonPath().getString("data.firstName"));
			apiData.put("lastName", response.jsonPath().getString("data.lastName"));
			apiData.put("email", response.jsonPath().getString("data.userEmail")); // <- note field difference
			apiData.put("mobileNumber", response.jsonPath().getString("data.mobileNumber"));
			apiData.put("country", response.jsonPath().getString("data.country"));
			apiData.put("state", response.jsonPath().getString("data.state"));
			apiData.put("role", response.jsonPath().getString("data.roleName")); // <- maps correctly

			logger.info("API Profile Data: {}", apiData);

			// --- Compare ---
			for (Map.Entry<String, String> entry : apiData.entrySet()) {
				String field = entry.getKey();
				String expected = entry.getValue();
				String actual = uiData.get(field);

				if (actual == null || !actual.equalsIgnoreCase(expected)) {
					logger.error("Mismatch in {}: API = {}, UI = {}", field, expected, actual);
					return "Mismatch in field: " + field;
				}
			}

			logger.info("UI and API profile data match successfully.");
			return "User Verified successfully";

		} catch (Exception e) {
			logger.error("Error validating user profile data: {}", e.getMessage(), e);
			return "Error in User Profile Data Validation";
		}
	}

}
