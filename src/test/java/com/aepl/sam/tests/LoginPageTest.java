package com.aepl.sam.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aepl.sam.base.TestBase;
import com.aepl.sam.utils.Constants;
import com.aepl.sam.pages.CommonMethods;
import com.aepl.sam.pages.LoginPage;
import com.aepl.sam.utils.ConfigProperties;
import com.aepl.sam.utils.ExcelUtility;
import com.aepl.sam.utils.Result;

public class LoginPageTest extends TestBase {

	private LoginPage loginPage;
	private ExcelUtility excelUtility;
	private CommonMethods comm;
	private SoftAssert softAssert;
	private Executor executor;

	@Override
	@BeforeClass
	public void setUp() {
		super.setUp();
		this.loginPage = new LoginPage(driver, wait);
		this.comm = new CommonMethods(driver, wait);
		this.excelUtility = new ExcelUtility();
		this.softAssert = new SoftAssert();
		this.executor = new Executor(excelUtility, softAssert);
		excelUtility.initializeExcel("Login_Page_Test");
	}

	// --------------------------- old error getter -------------------------

	// private By getErrorLocator(String expectedErrorMessage) {
	// if (expectedErrorMessage.equals(Constants.email_error_msg_01)
	// || expectedErrorMessage.equals(Constants.email_error_msg_02)) {
	// // "//mat-error[contains(text(), \"This field is required and can't be only
	// spaces.\")]";
	// return By.xpath("//mat-error[contains(text(), '\" + expectedErrorMessage +
	// \"')]");
	// } else if (expectedErrorMessage.equals(Constants.password_error_msg_01)
	// || expectedErrorMessage.equals(Constants.password_error_msg_02)) {
	// return By.xpath("//mat-error[contains(text(), '" + expectedErrorMessage +
	// "')]");
	// } else if (expectedErrorMessage.equals(Constants.toast_error_msg_01)
	// || expectedErrorMessage.equals(Constants.toast_error_msg_02)) {
	// return By.xpath("//span[text()='" + expectedErrorMessage + "']");
	// } else {
	// throw new IllegalArgumentException("Unknown error message: " +
	// expectedErrorMessage);
	// }
	// }

	private By getErrorLocator(String expectedErrorMessage) {
		logger.debug("Finding error locator for message: {}", expectedErrorMessage);

		String safeMessage;
		if (expectedErrorMessage.contains("'")) {
			String[] parts = expectedErrorMessage.split("'");
			safeMessage = "concat('" + String.join("', \"'\", '", parts) + "')";
		} else {
			safeMessage = "'" + expectedErrorMessage + "'";
		}

		if (expectedErrorMessage.equals(Constants.email_error_msg_01)
				|| expectedErrorMessage.equals(Constants.email_error_msg_02)) {
			return By.xpath("//mat-error[contains(text(), " + safeMessage + ")]");
		} else if (expectedErrorMessage.equals(Constants.password_error_msg_01)
				|| expectedErrorMessage.equals(Constants.password_error_msg_02)) {
			return By.xpath("//mat-error[contains(text()," + safeMessage + ")]");
		} else if (expectedErrorMessage.equals(Constants.toast_error_msg)
				|| expectedErrorMessage.equals(Constants.toast_error_msg_03)) {
			return By.xpath("//simple-snack-bar/div[contains(text()," + safeMessage + ")]");
		} else {
			logger.warn("Unknown error message encountered: {}", expectedErrorMessage);
			throw new IllegalArgumentException("Unknown error message: " + expectedErrorMessage);
		}
	}

	@DataProvider(name = "loginData", parallel = false)
	public Object[][] loginData() {
		return new Object[][] {
				// Empty username with valid password → Should show email error message
				{ " ", ConfigProperties.getProperty("password"), Constants.email_error_msg_01,
						"Empty Username With Valid Password" },

				// Valid username with overly long password → Should show toast error message
				{ ConfigProperties.getProperty("username"), "aaaaaaaaaaaaaaaaa", Constants.toast_error_msg,
						"Valid Username With Long Password" },

				// Valid username with empty password → Should show toast error message
				{ ConfigProperties.getProperty("username"), " ", Constants.password_error_msg_02,
						"Valid Username With Empty Password" },

				// Invalid username with valid password → Should show toast error message
				{ "invalid.email!@domain.com", ConfigProperties.getProperty("password"), Constants.email_error_msg_02,
						"Invalid Username With Valid Password" },

				// Empty username and empty password → Should show toast error message
				{ " ", " ", Constants.email_error_msg_01, "Empty Username With Empty Password" },

				// Invalid username with invalid password → Should show toast error message
				{ "invalid.email!@domain.com", "uu7k2", Constants.toast_error_msg,
						"Invalid Username With Invalid Password" },

				// Valid username with short password → Should show password length error
				// message
				{ ConfigProperties.getProperty("username"), "short", Constants.password_error_msg_02,
						"Valid Username With Short Password" },

				// Valid username with only whitespace in password → Should show toast error
				// message
				{ ConfigProperties.getProperty("username"), "       ", Constants.toast_error_msg_03,
						"Valid Username With White Spaces in Password" } };
	}

	@Test(priority = 1)
	public void testCorrectUrl() {
		executor.executeTest("Test correct url for the {Sampark Cloud}", true, loginPage::isCorrectUrl);
	}

	// Validate that the login container is visible/displayed
	@Test(priority = 2)
	public void testLoginContainerIsDisplayed() {
		executor.executeTest("Test the login container is displayed", true, loginPage::isLoginContainerIsDisplayed);
	}

	// validate that the site name is matched or not
	@Test(priority = 3)
	public void testSiteNameIsMatched() {
		executor.executeTest("Test the site name is matched", Constants.EXP_PAGE_TITLE_TEXT,
				() -> loginPage.siteNameMaching());
	}

	// Validate that the login form container is visible
	@Test(priority = 4)
	public void testLoginFormContainerIsVisible() {
		executor.executeTest("Test the login form container is visible", true, loginPage::isLoginFormContainerVisible);
	}

	// Validate the login form header should be equal to the expected
	@Test(priority = 5)
	public void testHeaderOfLoginFormContainer() {
		executor.executeTest("Test the header of the login form container", "Welcome Back !",
				loginPage::validateLoginFormHeader);
	}

	// Validate the label of the email field is matching or not
	@Test(priority = 6)
	public void testLabelHeaderOfEmail() {
		executor.executeTest("Test the label header of the email field of login form container", "Your Email Address",
				loginPage::validateLabelOfEmailField);
	}

	// Validate that the person icon is present in the email field
	@Test(priority = 7)
	public void testPersonIconInEmailField() {
		executor.executeTest("Test the {person} icon in the email field", true, () -> loginPage.isPersonIconPresent());
	}

	// Validate that the label of password field is matching or not
	@Test(priority = 8)
	public void testLabelHeaderOfPassword() {
		executor.executeTest("Test the label header of the email field of login form container", "Password",
				loginPage::validateLabelOfPasswordField);
	}

	// Validate that the lock icon is present in the password field
	@Test(priority = 9)
	public void testLockIconInPasswordField() {
		executor.executeTest("Test the {Lock} icon in the password field", true, () -> loginPage.isLockIconPresent());
	}

	// Validate that the eye icon is present in the password field
	@Test(priority = 10)
	public void testEyeIconDisplayedInPasswordField() {
		executor.executeTest("Test the {Eye} icon in the password field", true, () -> loginPage.isEyeIconPresent());
	}

	// Validate that the eye icon is enabled in the password field
	@Test(priority = 11)
	public void testEyeIconEnabledInPasswordField() {
		executor.executeTest("Test the {Eye} icon in the password field", true, () -> loginPage.isEyeIconEnabled());
	}

	// Validate - click on the eye icon and see the class changes from the hidden to
	// visible
	@Test(priority = 12)
	public void testClickOnEyeIcon() {
		executor.executeTest("Test the clicking on eye icon in the password field", true, loginPage::isEyeIconClicked);
	}

	// Validate - forgot password link is present and enabled
	@Test(priority = 13)
	public void testPasswordLink() {
		executor.executeTest("Test the forgot password link is present and enabled", true,
				loginPage::isForgotPasswordLinkPresentAndEnabled);
	}

	@Test(priority = 14, dataProvider = "loginData")
	public void testLogin(String username, String password, String expectedErrorMessage, String testCaseName) {
		String actualErr = "";
		logger.info("Executing test: {}", testCaseName);
		logger.info("Attempting login with Username: [{}] and Password: [******]", username);

		loginPage.enterUsername(username).enterPassword(password).clickLogin();

		try {
			if (expectedErrorMessage.isEmpty()) {
				boolean isDashboardURL = wait.until(ExpectedConditions.urlContains("dashboard"));
				Assert.assertTrue(isDashboardURL, "Login did not redirect to dashboard.");
				logger.info("Login successful. Redirected to dashboard.");
			} else {
				By errorLocator = getErrorLocator(expectedErrorMessage);
				WebElement errorMessage = loginPage.waitForVisibility(errorLocator);
				actualErr = errorMessage.getText();
				Assert.assertEquals(actualErr, expectedErrorMessage, "Error message mismatch.");

				logger.info("Error message '{}' displayed as expected.", expectedErrorMessage);
				excelUtility.writeTestDataToExcel(testCaseName, expectedErrorMessage, actualErr,
						Result.PASS.getValue());
			}

		} catch (TimeoutException e) {
			logger.error("Page did not load as expected for test: {}", testCaseName, e);
			excelUtility.writeTestDataToExcel(testCaseName, expectedErrorMessage, "Page did not load",
					Result.FAIL.getValue());
			Assert.fail("Page did not load as expected.");
		} catch (Exception e) {
			logger.error("Unexpected error during login test: {}", testCaseName, e);
			excelUtility.writeTestDataToExcel(testCaseName, expectedErrorMessage, "Unexpected error: " + e.getMessage(),
					Result.FAIL.getValue());
			Assert.fail("Unexpected error: " + e.getMessage());
		}
	}

	// @Test(priority = 15)
	public void testForgotPasswordLink() {
		executor.executeTest("Forgot Password Link Test", Constants.EXP_FRGT_PWD_URL, () -> {
			loginPage.clickForgotPassword();
			return driver.getCurrentUrl();
		});
	}

	// @Test(priority = 16)
	public void testInputErrMessage() {
		executor.executeTest("Input Error Message Test", "This field is required and can't be only spaces.",
				loginPage::inputErrMessage);
	}

	// @Test(priority = 16)
	public void testResetPassword() {
		executor.executeTest("Reset Password Test", "Password reset link sent to your email.",
				loginPage::resetPassword);
	}

	@Test(priority = 17)
	public void testCopyright() {
		executor.executeTest("Copyright Verification Test", Constants.EXP_COPYRIGHT_TEXT, comm::checkCopyright);
	}

	@Test(priority = 18)
	public void testVersion() {
		executor.executeTest("Version Verification Test", Constants.EXP_VERSION_TEXT, comm::checkVersion);
	}

	@Test(priority = 19)
	public void loginSuccess() {
		executor.executeTest("Login Success Test", Constants.DASH_URL, () -> {
			// driver.navigate().refresh();
			loginPage.enterUsername(ConfigProperties.getProperty("username"))
					.enterPassword(ConfigProperties.getProperty("password")).clickLogin();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return driver.getCurrentUrl();
		});
	}

	@AfterClass
	public void tearDownAssertions() {
		softAssert.assertAll();
	}

}
