package com.aepl.sam.tests;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
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
	@BeforeMethod(alwaysRun = true)
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

	@Test
	public void testEmptyUsernameWithValidPassword() {
		loginPage.enterUsername(" ").enterPassword(ConfigProperties.getProperty("password")).clickLogin();
		Assert.assertEquals(loginPage.getEmailFieldErrorMessage(), Constants.EMAIL_ERROR_MSG_REQUIRED);
	}

	@Test
	public void testValidUsernameWithLongInvalidPassword() {
		loginPage.enterUsername(ConfigProperties.getProperty("username")).enterPassword(randomGen.generateRandomString(16))
				.clickLogin();
		assertInvalidLoginToast(loginPage.getToastMessage());
	}

	@Test
	public void testValidUsernameWithEmptyPassword() {
		loginPage.enterUsername(ConfigProperties.getProperty("username")).enterPassword(" ").clickLogin();
		Assert.assertEquals(loginPage.getPasswordFieldErrorMessage(), Constants.PASSWORD_ERROR_MSG_MIN_LENGTH);
	}

	@Test
	public void testInvalidUsernameWithValidPassword() {
		loginPage.enterUsername(randomGen.generateRandomEmail()).enterPassword(ConfigProperties.getProperty("password"))
				.clickLogin();
		assertInvalidLoginToast(loginPage.getToastMessage());
	}

	@Test
	public void testEmptyUsernameAndEmptyPassword() {
		loginPage.enterUsername(" ").enterPassword(" ").clickLogin();
		String actualEmailError = loginPage.getEmailFieldErrorMessage();
		String actualPasswordError = loginPage.getPasswordFieldErrorMessage();
		Assert.assertEquals(actualEmailError, Constants.EMAIL_ERROR_MSG_REQUIRED);
		Assert.assertTrue(actualPasswordError.isBlank()
				|| actualPasswordError.equals(Constants.PASSWORD_ERROR_MSG_REQUIRED)
				|| actualPasswordError.equals(Constants.PASSWORD_ERROR_MSG_MIN_LENGTH));
	}

	@Test
	public void testInvalidUsernameWithInvalidPassword() {
		loginPage.enterUsername(randomGen.generateRandomEmail()).enterPassword(randomGen.generateRandomString(8))
				.clickLogin();
		assertInvalidLoginToast(loginPage.getToastMessage());
	}

	@Test
	public void testValidUsernameWithShortPassword() {
		loginPage.enterUsername(ConfigProperties.getProperty("username")).enterPassword("short").clickLogin();
		Assert.assertEquals(loginPage.getPasswordFieldErrorMessage(), Constants.PASSWORD_ERROR_MSG_MIN_LENGTH);
	}

	@Test
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

	@Test
	public void testCorrectUrl() {
		executor.executeTest("Test correct url for the {Sampark Cloud}", true, loginPage::isCorrectUrl);
	}

	@Test
	public void testLoginContainerIsDisplayed() {
		executor.executeTest("Test the login container is displayed", true, loginPage::isLoginContainerIsDisplayed);
	}

	@Test
	public void testSiteNameIsMatched() {
		executor.executeTest("Test the site name is matched", Constants.EXP_PAGE_TITLE_TEXT, loginPage::siteNameMaching);
	}

	@Test
	public void testLoginFormContainerIsVisible() {
		executor.executeTest("Test the login form container is visible", true, loginPage::isLoginFormContainerVisible);
	}

	@Test
	public void testHeaderOfLoginFormContainer() {
		executor.executeTest("Test the header of the login form container", "Welcome Back !",
				loginPage::validateLoginFormHeader);
	}

	@Test
	public void testLabelHeaderOfEmail() {
		executor.executeTest("Test the label header of the email field of login form container", "Your Email Address",
				loginPage::validateLabelOfEmailField);
	}

	@Test
	public void testPersonIconInEmailField() {
		executor.executeTest("Test the {person} icon in the email field", true, loginPage::isPersonIconPresent);
	}

	@Test
	public void testLabelHeaderOfPassword() {
		executor.executeTest("Test the label header of the email field of login form container", "Password",
				loginPage::validateLabelOfPasswordField);
	}

	@Test
	public void testLockIconInPasswordField() {
		executor.executeTest("Test the {Lock} icon in the password field", true, loginPage::isLockIconPresent);
	}

	@Test
	public void testEyeIconDisplayedInPasswordField() {
		executor.executeTest("Test the {Eye} icon in the password field", true, loginPage::isEyeIconPresent);
	}

	@Test
	public void testEyeIconEnabledInPasswordField() {
		executor.executeTest("Test the {Eye} icon in the password field", true, loginPage::isEyeIconEnabled);
	}

	@Test
	public void testClickOnEyeIcon() {
		executor.executeTest("Test the clicking on eye icon in the password field", true, loginPage::isEyeIconClicked);
	}

	@Test
	public void testPasswordLink() {
		executor.executeTest("Test the forgot password link is present and enabled", true,
				loginPage::isForgotPasswordLinkPresentAndEnabled);
	}

	@Test
	public void testCopyright() {
		executor.executeTest("Copyright Verification Test", Constants.EXP_COPYRIGHT_TEXT, assertion::checkCopyright);
	}

	@Test
	public void testVersion() {
		executor.executeTest("Version Verification Test", Constants.EXP_VERSION_TEXT, assertion::checkVersion);
	}

	@Test(groups = {"sampark", "lct", "trio", "swaraj", "atcu", "smoke", "regression"})
	public void loginSuccess() {
		executor.executeTest("Login Success Test", true, () -> {
			loginPage.enterUsername(ConfigProperties.getProperty("username"))
					.enterPassword(ConfigProperties.getProperty("password")).clickLogin();
			return wait.until(ExpectedConditions.urlToBe(Constants.DASH_URL));
		});
	}

	@AfterMethod(alwaysRun = true)
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


