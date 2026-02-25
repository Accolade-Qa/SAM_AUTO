package com.aepl.sam.pages;

import java.util.NoSuchElementException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aepl.sam.utils.Constants;
import com.aepl.sam.locators.LoginPageLocators;
import com.aepl.sam.utils.ConfigProperties;
import com.aepl.sam.utils.EmailReader;
import com.aepl.sam.utils.PageActionsUtil;

public class LoginPage extends LoginPageLocators {
	private WebDriver driver;
	private WebDriverWait wait;
	private PageActionsUtil comm;
	private static final Logger logger = LogManager.getLogger(LoginPage.class);

	public LoginPage(WebDriver driver, WebDriverWait wait) {
		this.driver = driver;
		this.wait = wait;
		this.comm = new PageActionsUtil(driver, wait);
	}

	public LoginPage enterUsername(String username) {
		logger.info("Entering username: {}", username);
		WebElement usernameInput = waitForVisibility(LOGIN_FLD);
		comm.highlightElement(usernameInput, "solid purple");
		usernameInput.clear();
		usernameInput.sendKeys(username);
		logger.debug("Username entered successfully.");
		return this;
	}

	public LoginPage enterPassword(String password) {
		logger.info("Entering password (masked).");
		WebElement passwordInput = waitForVisibility(PASSWORD_FLD);
		comm.highlightElement(passwordInput, "solid purple");
		passwordInput.clear();
		passwordInput.sendKeys(password);
		logger.debug("Password entered successfully.");
		return this;
	}

	public LoginPage clickLogin() {
		logger.info("Clicking on 'Login' button...");
		// scroll to bottom to make button visible
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollTo(0, document.body.scrollHeight);");

		WebElement loginBtn = waitForVisibility(SIGN_IN_BTN);
		comm.highlightElement(loginBtn, "solid purple");
		if (!loginBtn.isEnabled()) {
			logger.warn("'Login' button is disabled. Cannot proceed.");
			throw new IllegalStateException("'Login' button is disabled.");
		}
		loginBtn.click();
		logger.info("Clicked 'Login' button successfully.");
		return this;
	}

	public void clickForgotPassword() {
		logger.info("Clicking 'Forgot Password' link...");
		WebElement forgotLink = waitForVisibility(FORGOT_PASSWORD_LNK);
		comm.highlightElement(forgotLink, "solid purple");
		forgotLink.click();
		logger.info("'Forgot Password' link clicked.");
	}

	public void clickLogout() {
		logger.info("Attempting to log out...");
		WebElement profileIcon = driver.findElement(PROFILE_ICON);
		comm.highlightElement(profileIcon, "solid purple");
		profileIcon.click();

		WebElement logoutBtn = waitForVisibility(LOGOUT_BTN);
		comm.highlightElement(logoutBtn, "solid purple");
		logoutBtn.click();

		logger.info("Successfully logged out.");
	}

	public String inputErrMessage() {
		try {
			logger.info("Checking for input field error message...");
			WebElement inputField = waitForVisibility(FORGOT_INPUT_FLD);
			comm.highlightElement(inputField, "solid purple");

			inputField.sendKeys(Keys.ENTER);
			inputField.sendKeys(Keys.TAB);

			WebElement errMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(FORGOT_ERROR_MSG));
			comm.highlightElement(errMsg, "solid purple");

			logger.info("Error message displayed: {}", errMsg.getText());
			return errMsg.getText();
		} catch (Exception e) {
			logger.error("Exception while checking error message: {}", e.getMessage(), e);
			return "No error message is displayed on the page!!!";
		}
	}

	public String resetPassword() {
		try {
			logger.info("Attempting password reset...");
			WebElement inputField = waitForVisibility(FORGOT_INPUT_FLD);
			inputField.sendKeys(ConfigProperties.getProperty("username"));

			WebElement resetBtn = waitForVisibility(RESET_BTN);
			resetBtn.click();

			// Wait for toast message
			WebElement toastMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(RESET_TOAST_MSG));
			String confirmationMsg = toastMsg.getText();
			logger.info("Reset toast: {}", confirmationMsg);

			// Now read email
			String newPassword = EmailReader.getPasswordFromOutlook();
			logger.info("New password retrieved: {}", newPassword);

			// Save to config file
			ConfigProperties.setProperty("password", newPassword);
			logger.info("Password updated in config.properties.");

			return confirmationMsg;
		} catch (Exception e) {
			logger.error("Error during password reset: {}", e.getMessage(), e);
			return "No Password is changed...";
		}
	}

	public boolean isCorrectUrl() {
		String currentUrl = driver.getCurrentUrl();
		logger.info("Current URL: {}", currentUrl);
		return currentUrl.equals(Constants.LOGIN_URL);
	}

	public boolean isLoginContainerIsDisplayed() {
		logger.info("Checking if login container is displayed...");
		WebElement container = driver.findElement(LOGIN_CONTAINER);
		comm.highlightElement(container, "solid purple");
		return container.isDisplayed();
	}

	public String siteNameMaching() {
		logger.info("Retrieving site name text...");
		WebElement siteNameElement = driver.findElement(By.className("site-name-text-section"));
		comm.highlightElement(siteNameElement, "solid purple");
		String siteName = siteNameElement.getText();
		logger.info("Site name found: {}", siteName);
		return siteName;
	}

	public boolean isLoginFormContainerVisible() {
		logger.info("Checking if login form container is visible...");
		WebElement formContainer = driver.findElement(By.className("card-block"));
		comm.highlightElement(formContainer, "solid purple");
		return formContainer.isDisplayed();
	}

	public String validateLoginFormHeader() {
		logger.info("Validating login form header text...");
		WebElement header = driver.findElement(By.className("welcome-text"));
		comm.highlightElement(header, "solid purple");
		String text = header.getText();
		logger.info("Login form header text: {}", text);
		return text;
	}

	public String validateLabelOfEmailField() {
		logger.info("Validating email field label...");
		WebElement label = driver.findElement(By.xpath("//label[@for='email' and @class='form-label']"));
		comm.highlightElement(label, "solid purple");
		String text = label.getText();
		logger.info("Email label text: {}", text);
		return text;
	}

	public boolean isPersonIconPresent() {
		logger.info("Checking if person icon is present...");
		WebElement icon = driver.findElement(By.xpath("//mat-icon[contains(text(), 'person')]"));
		comm.highlightElement(icon, "solid purple");
		return icon.isDisplayed();
	}

	public String validateLabelOfPasswordField() {
		logger.info("Validating password field label...");
		WebElement label = driver.findElement(By.xpath("//label[@for='password' and @class='form-label']"));
		comm.highlightElement(label, "solid purple");
		String text = label.getText();
		logger.info("Password label text: {}", text);
		return text;
	}

	public boolean isLockIconPresent() {
		logger.info("Checking if lock icon is present...");
		WebElement icon = driver.findElement(By.xpath("//mat-icon[contains(text(), 'lock')]"));
		comm.highlightElement(icon, "solid purple");
		return icon.isDisplayed();
	}

	public boolean isEyeIconPresent() {
		logger.info("Checking if eye icon is present...");
		WebElement icon = driver.findElement(By.xpath("//mat-icon[contains(text(), 'visibility')]"));
		comm.highlightElement(icon, "solid purple");
		return icon.isDisplayed();
	}

	public boolean isEyeIconEnabled() {
		logger.info("Checking if eye icon is enabled...");
		WebElement icon = driver.findElement(By.xpath("//mat-icon[contains(text(), 'visibility')]"));
		comm.highlightElement(icon, "solid purple");
		return icon.isEnabled();
	}

	public boolean isEyeIconClicked() {
		try {
			logger.info("Verifying eye icon functionality...");
			WebElement eyeIcon = driver.findElement(By.xpath("//mat-icon[contains(text(), 'visibility')]"));
			WebElement passwordField = driver.findElement(By.id("password"));

			comm.highlightElement(eyeIcon, "solid purple");
			comm.highlightElement(passwordField, "solid purple");

			String beforeType = passwordField.getAttribute("type");
			logger.debug("Before click, password field type: {}", beforeType);

			eyeIcon.click();
			Thread.sleep(500);

			String afterType = passwordField.getAttribute("type");
			logger.debug("After click, password field type: {}", afterType);

			if (beforeType.equalsIgnoreCase("password") && afterType.equalsIgnoreCase("text")) {
				logger.info("âœ… Password visibility toggled ON successfully.");
				return true;
			} else if (beforeType.equalsIgnoreCase("text") && afterType.equalsIgnoreCase("password")) {
				logger.info("âœ… Password visibility toggled OFF successfully.");
				return true;
			} else {
				logger.warn("âš ï¸ Eye icon click did not toggle password visibility.");
				return false;
			}
		} catch (Exception e) {
			logger.error("âŒ Error while verifying eye icon click: {}", e.getMessage(), e);
			return false;
		}
	}

	public boolean isForgotPasswordLinkPresentAndEnabled() {
		try {
			logger.info("Checking 'Forgot Password' link visibility and state...");
			WebElement forgotPasswordLink = driver.findElement(By.className("forgot-password"));
			comm.highlightElement(forgotPasswordLink, "solid purple");

			boolean isDisplayed = forgotPasswordLink.isDisplayed();
			boolean isEnabled = forgotPasswordLink.isEnabled();

			if (isDisplayed && isEnabled) {
				logger.info("âœ… 'Forgot Password' link is present and enabled.");
				return true;
			} else {
				logger.warn("âš ï¸ 'Forgot Password' link is either not visible or disabled.");
				return false;
			}
		} catch (NoSuchElementException e) {
			logger.error("âŒ 'Forgot Password' link not found: {}", e.getMessage());
			return false;
		} catch (Exception e) {
			logger.error("âŒ Unexpected error while checking 'Forgot Password' link: {}", e.getMessage(), e);
			return false;
		}
	}

	public String getEmailFieldErrorMessage() {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div/mat-error"))).getText();
	}

	public String getPasswordFieldErrorMessage() {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div/mat-error"))).getText();
	}

	public String getToastMessage() {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//simple-snack-bar/div[1]"))).getText();
	}

	// Helper method for waiting element visibility
	public WebElement waitForVisibility(By locator) {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}
}

