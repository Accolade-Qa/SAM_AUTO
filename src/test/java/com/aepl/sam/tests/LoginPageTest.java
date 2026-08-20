package com.aepl.sam.tests;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aepl.sam.base.TestBase;
import com.aepl.sam.pages.LoginPage;
import com.aepl.sam.utils.ConfigProperties;
import com.aepl.sam.utils.Constants;
import com.aepl.sam.utils.ExcelUtility;
import com.aepl.sam.utils.PageAssertionsUtil;
import com.aepl.sam.utils.RandomGeneratorUtils;

@Test(groups = {"sampark", "lct", "trio", "swaraj", "atcu", "regression"})
public class LoginPageTest extends TestBase {

	private LoginPage loginPage;
	private ExcelUtility excelUtility;
	private PageAssertionsUtil assertion;
	private SoftAssert softAssert;
	private Executor executor;
	private RandomGeneratorUtils randomGen;

	@Override
	@BeforeClass(alwaysRun = true)
	public void setUp() {
		super.setUp();
		this.loginPage = new LoginPage(driver, wait);
		this.assertion = new PageAssertionsUtil(driver, wait);
		this.excelUtility = new ExcelUtility();
		this.softAssert = new SoftAssert();
		this.executor = new Executor(excelUtility, softAssert);
		this.randomGen = new RandomGeneratorUtils();
		excelUtility.initializeExcel("Login_Page_Test");
	}

	@Test(priority = 1)
	public void testEmptyUsernameWithValidPassword() {
		executor.executeTest("Test empty username with valid password error message",
				Constants.EMAIL_ERROR_MSG_REQUIRED, () -> {
					loginPage.enterUsername(" ").enterPassword(ConfigProperties.getProperty("password")).clickLogin();
					return loginPage.getEmailFieldErrorMessage();
				});
	}

	@Test(priority = 2)
	public void testValidUsernameWithLongInvalidPassword() {
		executor.executeTest("Test valid username with long invalid password error toast", true, () -> {
			loginPage.enterUsername(ConfigProperties.getProperty("username"))
					.enterPassword(randomGen.generateRandomString(16)).clickLogin();
			return isInvalidLoginToast(loginPage.getToastMessage());
		});
	}

	@Test(priority = 3)
	public void testValidUsernameWithEmptyPassword() {
		executor.executeTest("Test valid username with empty password error message",
				Constants.PASSWORD_ERROR_MSG_MIN_LENGTH, () -> {
					loginPage.enterUsername(ConfigProperties.getProperty("username")).enterPassword(" ").clickLogin();
					return loginPage.getPasswordFieldErrorMessage();
				});
	}

	@Test(priority = 4)
	public void testInvalidUsernameWithValidPassword() {
		executor.executeTest("Test invalid username with valid password error toast", true, () -> {
			loginPage.enterUsername(randomGen.generateRandomEmail())
					.enterPassword(ConfigProperties.getProperty("password")).clickLogin();
			return isInvalidLoginToast(loginPage.getToastMessage());
		});
	}

	@Test(priority = 5)
	public void testEmptyUsernameAndEmptyPassword() {
		executor.executeTest("Test empty username and empty password validation error",
				Constants.EMAIL_ERROR_MSG_REQUIRED, () -> {
					loginPage.enterUsername(" ").enterPassword(" ").clickLogin();
					return loginPage.getEmailFieldErrorMessage();
				});
	}

	@Test(priority = 6)
	public void testInvalidUsernameWithInvalidPassword() {
		executor.executeTest("Test invalid username with invalid password error toast", true, () -> {
			loginPage.enterUsername(randomGen.generateRandomEmail())
					.enterPassword(randomGen.generateRandomString(8)).clickLogin();
			return isInvalidLoginToast(loginPage.getToastMessage());
		});
	}

	@Test(priority = 7)
	public void testValidUsernameWithShortPassword() {
		executor.executeTest("Test valid username with short password error message",
				Constants.PASSWORD_ERROR_MSG_MIN_LENGTH, () -> {
					loginPage.enterUsername(ConfigProperties.getProperty("username")).enterPassword("short")
							.clickLogin();
					return loginPage.getPasswordFieldErrorMessage();
				});
	}

	@Test(priority = 8)
	public void testValidUsernameWithWhitespacePassword() {
		executor.executeTest("Test valid username with whitespace password validation", true, () -> {
			loginPage.enterUsername(ConfigProperties.getProperty("username")).enterPassword("       ").clickLogin();
			String actual = loginPage.getPasswordFieldErrorMessage();
			if (actual.isBlank()) {
				return isInvalidLoginToast(loginPage.getToastMessage());
			}
			return actual.equals(Constants.PASSWORD_ERROR_MSG_REQUIRED)
					|| actual.equals(Constants.PASSWORD_ERROR_MSG_MIN_LENGTH);
		});
	}

	@Test(priority = 9)
	public void testCorrectUrl() {
		executor.executeTest("Verify Correct URL for Sampark Cloud", true, loginPage::isCorrectUrl);
	}

	@Test(priority = 10)
	public void testLoginContainerIsDisplayed() {
		executor.executeTest("Verify Login Container Visibility", true, loginPage::isLoginContainerIsDisplayed);
	}

	@Test(priority = 11)
	public void testSiteNameIsMatched() {
		executor.executeTest("Verify Site Name Matching", Constants.EXP_PAGE_TITLE_TEXT, loginPage::siteNameMaching);
	}

	@Test(priority = 12)
	public void testLoginFormContainerIsVisible() {
		executor.executeTest("Verify Login Form Container Visibility", true, loginPage::isLoginFormContainerVisible);
	}

	@Test(priority = 13)
	public void testHeaderOfLoginFormContainer() {
		executor.executeTest("Verify Login Form Container Header", "Welcome Back !",
				loginPage::validateLoginFormHeader);
	}

	@Test(priority = 14)
	public void testLabelHeaderOfEmail() {
		executor.executeTest("Verify Email Field Label", "Your Email Address",
				loginPage::validateLabelOfEmailField);
	}

	@Test(priority = 15)
	public void testPersonIconInEmailField() {
		executor.executeTest("Verify Person Icon in Email Field", true, loginPage::isPersonIconPresent);
	}

	@Test(priority = 16)
	public void testLabelHeaderOfPassword() {
		executor.executeTest("Verify Password Field Label", "Password",
				loginPage::validateLabelOfPasswordField);
	}

	@Test(priority = 17)
	public void testLockIconInPasswordField() {
		executor.executeTest("Verify Lock Icon in Password Field", true, loginPage::isLockIconPresent);
	}

	@Test(priority = 18)
	public void testEyeIconDisplayedInPasswordField() {
		executor.executeTest("Verify Eye Icon Visibility in Password Field", true, loginPage::isEyeIconPresent);
	}

	@Test(priority = 19)
	public void testEyeIconEnabledInPasswordField() {
		executor.executeTest("Verify Eye Icon Enabled in Password Field", true, loginPage::isEyeIconEnabled);
	}

	@Test(priority = 20)
	public void testClickOnEyeIcon() {
		executor.executeTest("Verify Click on Eye Icon", true, loginPage::isEyeIconClicked);
	}

	@Test(priority = 21)
	public void testPasswordLink() {
		executor.executeTest("Verify Forgot Password Link Presence and Status", true,
				loginPage::isForgotPasswordLinkPresentAndEnabled);
	}

	@Test(priority = 22)
	public void testCopyright() {
		executor.executeTest("Verify Copyright Functionality", Constants.EXP_COPYRIGHT_TEXT, assertion::checkCopyright);
	}

	@Test(priority = 23)
	public void testVersion() {
		executor.executeTest("Verify Version Functionality", Constants.EXP_VERSION_TEXT, assertion::checkVersion);
	}

	@Test(priority = 24, groups = {"sampark", "lct", "trio", "swaraj", "atcu", "smoke", "regression"})
	public void testLoginSuccess() {
		executor.executeTest("Verify Successful Login with Valid Credentials", true, () -> {
			loginPage.enterUsername(ConfigProperties.getProperty("username"))
					.enterPassword(ConfigProperties.getProperty("password")).clickLogin();
			return wait.until(ExpectedConditions.urlToBe(Constants.DASH_URL));
		});
	}

	@AfterClass
	public void tearDownAssertions() {
		softAssert.assertAll();
	}

	private boolean isInvalidLoginToast(String actualToast) {
		return actualToast.equals(Constants.TOAST_ERROR_MSG_INVALID_CREDENTIALS)
				|| actualToast.equals(Constants.TOAST_ERROR_MSG_VALIDATION)
				|| actualToast.equals(Constants.TOAST_ERROR_MSG_LOGIN_FAILED);
	}
}


