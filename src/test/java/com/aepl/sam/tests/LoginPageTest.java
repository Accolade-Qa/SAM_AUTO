package com.aepl.sam.tests;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
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

public class LoginPageTest extends TestBase {

	private LoginPage loginPage;
	private ExcelUtility excelUtility;
	private PageAssertionsUtil assertion;
	private SoftAssert softAssert;
	private Executor executor;
	private RandomGeneratorUtils randomGen;

	@Override
	@BeforeClass
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
		loginPage.enterUsername(" ").enterPassword(ConfigProperties.getProperty("password")).clickLogin();
		Assert.assertEquals(loginPage.getEmailFieldErrorMessage(), Constants.EMAIL_ERROR_MSG_REQUIRED);
	}

	@Test(priority = 2)
	public void testValidUsernameWithLongInvalidPassword() {
		loginPage.enterUsername(ConfigProperties.getProperty("username")).enterPassword(randomGen.generateRandomString(16))
				.clickLogin();
		assertInvalidLoginToast(loginPage.getToastMessage());
	}

	@Test(priority = 3)
	public void testValidUsernameWithEmptyPassword() {
		loginPage.enterUsername(ConfigProperties.getProperty("username")).enterPassword(" ").clickLogin();
		Assert.assertEquals(loginPage.getPasswordFieldErrorMessage(), Constants.PASSWORD_ERROR_MSG_MIN_LENGTH);
	}

	@Test(priority = 4)
	public void testInvalidUsernameWithValidPassword() {
		loginPage.enterUsername(randomGen.generateRandomEmail()).enterPassword(ConfigProperties.getProperty("password"))
				.clickLogin();
		assertInvalidLoginToast(loginPage.getToastMessage());
	}

	@Test(priority = 5)
	public void testEmptyUsernameAndEmptyPassword() {
		loginPage.enterUsername(" ").enterPassword(" ").clickLogin();
		String actualEmailError = loginPage.getEmailFieldErrorMessage();
		String actualPasswordError = loginPage.getPasswordFieldErrorMessage();
		Assert.assertEquals(actualEmailError, Constants.EMAIL_ERROR_MSG_REQUIRED);
		Assert.assertTrue(actualPasswordError.isBlank()
				|| actualPasswordError.equals(Constants.PASSWORD_ERROR_MSG_REQUIRED)
				|| actualPasswordError.equals(Constants.PASSWORD_ERROR_MSG_MIN_LENGTH));
	}

	@Test(priority = 6)
	public void testInvalidUsernameWithInvalidPassword() {
		loginPage.enterUsername(randomGen.generateRandomEmail()).enterPassword(randomGen.generateRandomString(8))
				.clickLogin();
		assertInvalidLoginToast(loginPage.getToastMessage());
	}

	@Test(priority = 7)
	public void testValidUsernameWithShortPassword() {
		loginPage.enterUsername(ConfigProperties.getProperty("username")).enterPassword("short").clickLogin();
		Assert.assertEquals(loginPage.getPasswordFieldErrorMessage(), Constants.PASSWORD_ERROR_MSG_MIN_LENGTH);
	}

	@Test(priority = 8)
	public void testValidUsernameWithWhitespacePassword() {
		loginPage.enterUsername(ConfigProperties.getProperty("username")).enterPassword("       ").clickLogin();
		String actual = loginPage.getPasswordFieldErrorMessage();
		if (actual.isBlank()) {
			assertInvalidLoginToast(loginPage.getToastMessage());
			return;
		}
		Assert.assertTrue(actual.equals(Constants.PASSWORD_ERROR_MSG_REQUIRED)
				|| actual.equals(Constants.PASSWORD_ERROR_MSG_MIN_LENGTH));
	}

	@Test(priority = 9)
	public void testCorrectUrl() {
		executor.executeTest("Test correct url for the {Sampark Cloud}", true, loginPage::isCorrectUrl);
	}

	@Test(priority = 10)
	public void testLoginContainerIsDisplayed() {
		executor.executeTest("Test the login container is displayed", true, loginPage::isLoginContainerIsDisplayed);
	}

	@Test(priority = 11)
	public void testSiteNameIsMatched() {
		executor.executeTest("Test the site name is matched", Constants.EXP_PAGE_TITLE_TEXT, loginPage::siteNameMaching);
	}

	@Test(priority = 12)
	public void testLoginFormContainerIsVisible() {
		executor.executeTest("Test the login form container is visible", true, loginPage::isLoginFormContainerVisible);
	}

	@Test(priority = 13)
	public void testHeaderOfLoginFormContainer() {
		executor.executeTest("Test the header of the login form container", "Welcome Back !",
				loginPage::validateLoginFormHeader);
	}

	@Test(priority = 14)
	public void testLabelHeaderOfEmail() {
		executor.executeTest("Test the label header of the email field of login form container", "Your Email Address",
				loginPage::validateLabelOfEmailField);
	}

	@Test(priority = 15)
	public void testPersonIconInEmailField() {
		executor.executeTest("Test the {person} icon in the email field", true, loginPage::isPersonIconPresent);
	}

	@Test(priority = 16)
	public void testLabelHeaderOfPassword() {
		executor.executeTest("Test the label header of the email field of login form container", "Password",
				loginPage::validateLabelOfPasswordField);
	}

	@Test(priority = 17)
	public void testLockIconInPasswordField() {
		executor.executeTest("Test the {Lock} icon in the password field", true, loginPage::isLockIconPresent);
	}

	@Test(priority = 18)
	public void testEyeIconDisplayedInPasswordField() {
		executor.executeTest("Test the {Eye} icon in the password field", true, loginPage::isEyeIconPresent);
	}

	@Test(priority = 19)
	public void testEyeIconEnabledInPasswordField() {
		executor.executeTest("Test the {Eye} icon in the password field", true, loginPage::isEyeIconEnabled);
	}

	@Test(priority = 20)
	public void testClickOnEyeIcon() {
		executor.executeTest("Test the clicking on eye icon in the password field", true, loginPage::isEyeIconClicked);
	}

	@Test(priority = 21)
	public void testPasswordLink() {
		executor.executeTest("Test the forgot password link is present and enabled", true,
				loginPage::isForgotPasswordLinkPresentAndEnabled);
	}

	@Test(priority = 22)
	public void testCopyright() {
		executor.executeTest("Copyright Verification Test", Constants.EXP_COPYRIGHT_TEXT, assertion::checkCopyright);
	}

	@Test(priority = 23)
	public void testVersion() {
		executor.executeTest("Version Verification Test", Constants.EXP_VERSION_TEXT, assertion::checkVersion);
	}

	@Test(priority = 24)
	public void loginSuccess() {
		executor.executeTest("Login Success Test", true, () -> {
			loginPage.enterUsername(ConfigProperties.getProperty("username"))
					.enterPassword(ConfigProperties.getProperty("password")).clickLogin();
			return wait.until(ExpectedConditions.urlToBe(Constants.DASH_URL));
		});
	}

	@AfterClass
	public void tearDownAssertions() {
		softAssert.assertAll();
	}

	private void assertInvalidLoginToast(String actualToast) {
		Assert.assertTrue(actualToast.equals(Constants.TOAST_ERROR_MSG_INVALID_CREDENTIALS)
				|| actualToast.equals(Constants.TOAST_ERROR_MSG_VALIDATION)
				|| actualToast.equals(Constants.TOAST_ERROR_MSG_LOGIN_FAILED),
				"Unexpected login toast: " + actualToast);
	}
}


