package com.aepl.sam.tests;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aepl.sam.base.TestBase;
import com.aepl.sam.utils.Constants;
import com.aepl.sam.utils.PageActionsUtil;
import com.aepl.sam.utils.PageAssertionsUtil;
import com.aepl.sam.pages.UserProfilePage;
import com.aepl.sam.utils.ExcelUtility;

public class UserProfilePageTest extends TestBase {

	private static final String SHEET_NAME = "User_Profile_Test";

	private ExcelUtility excelUtility;
	private UserProfilePage userProf;
	private PageActionsUtil comm;
	private PageAssertionsUtil assertion;
	private SoftAssert softAssert;
	private Executor executor;

	@Override
	@BeforeClass
	public void setUp() {
		super.setUp();
		this.comm = new PageActionsUtil(driver, wait);
		this.assertion = new PageAssertionsUtil(driver, wait);
		this.userProf = new UserProfilePage(driver, wait);
		this.excelUtility = new ExcelUtility();
		this.softAssert = new SoftAssert();
		this.executor = new Executor(excelUtility, softAssert);
		excelUtility.initializeExcel(SHEET_NAME);
	}

	@Test(priority = 1)
	public void testCompanyLogo() {
		executor.executeTest("Verify Company Logo on Webpage", "Logo Displayed", () -> assertion.verifyWebpageLogo() ? Constants.EXP_LOGO_DISPLAYED : "Logo Not Displayed");
	}

	@Test(priority = 2)
	public void testPageTitle() {
		executor.executeTest("Verify Page Title on Webpage", Constants.EXP_PAGE_TITLE_TEXT, assertion::verifyPageTitle);
	}

	@Test(priority = 3)
	public void testNavBarLink() {
		executor.executeTest("Verify Navbar Link Navigation", Constants.USR_PROFILE, userProf::navBarLink);
	}

	@Test(priority = 4)
	public void testRefreshButton() {
		executor.executeTest("Verify Refresh Button Functionality", "Page refreshed successfully.", () -> {
			userProf.refreshButton();
			return "Page refreshed successfully.";
		});
	}

	@Test(priority = 5)
	public void testButtons1() {
		executor.executeTest("Verify Buttons on Customer Master Page", Constants.EXP_VALIDATE_BUTTONS_TEXT, assertion::validateButtons);
	}

	@Test(priority = 6)
	public void testUploadProfilePicture() {
		executor.executeTest("Verify Upload Profile Picture Functionality", "Profile picture uploaded successfully.", () -> {
			boolean isUploaded = userProf.uploadProfilePicture();
			return isUploaded ? "Profile picture uploaded successfully." : "Profile picture upload failed.";
		});
	}

	@Test(priority = 7)
	public void testUserProfileData() {
		executor.executeTest("Test the user profile data", "User Verified successfully", userProf::validateUserData);
	}

	@Test(priority = 8)
	public void testUpdateProfileDetails() {
		executor.executeTest("Verify Update Profile Details Functionality", "Profile updated successfully.", () -> {
			boolean isUpdated = userProf.updateProfileDetails();
			return isUpdated ? "Profile updated successfully." : "Profile update failed.";
		});
	}

	@Test(priority = 9)
	public void testChangePassword() {
		executor.executeTest("Verify Change Password Functionality", "Password changed successfully.", () -> {
			userProf.changePassword();
			return "Password changed successfully.";
		});
	}

	@Test(priority = 10)
	public void testVersion() {
		executor.executeTest("Verify Version Functionality", Constants.EXP_VERSION_TEXT, assertion::checkVersion);
	}

	@Test(priority = 11)
	public void testCopyright() {
		executor.executeTest("Verify Copyright Functionality", Constants.EXP_COPYRIGHT_TEXT, assertion::checkCopyright);
	}

	@AfterClass
	public void tearDownAssertions() {
		softAssert.assertAll();
	}
}

