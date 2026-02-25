package com.aepl.sam.tests;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aepl.sam.base.TestBase;
import com.aepl.sam.utils.Constants;
import com.aepl.sam.pages.CommonMethods;
import com.aepl.sam.pages.UserManagementPage;
import com.aepl.sam.utils.ExcelUtility;

public class UserManagementPageTest extends TestBase {
	private static final String SHEET_NAME = "User_Management_Test";

	private ExcelUtility excelUtility;
	private UserManagementPage userManagement;
	private CommonMethods comm;
	private SoftAssert softAssert;
	private Executor executor;

	@Override
	@BeforeClass
	public void setUp() {
		super.setUp();
		this.comm = new CommonMethods(driver, wait);
		this.userManagement = new UserManagementPage(driver, wait, comm);
		this.excelUtility = new ExcelUtility();
		this.softAssert = new SoftAssert();
		this.executor = new Executor(excelUtility, softAssert);
		excelUtility.initializeExcel(SHEET_NAME);
	}

	@Test(priority = 1)
	public void testCompanyLogo() {
		executor.executeTest("Verify Company Logo on Webpage", Constants.EXP_LOGO_DISPLAYED, () -> comm.verifyWebpageLogo() ? Constants.EXP_LOGO_DISPLAYED : "Logo Not Displayed");
	}

	@Test(priority = 2)
	public void testPageTitle() {
		executor.executeTest("Verify Page Title on Webpage", Constants.EXP_PAGE_TITLE_TEXT, comm::verifyPageTitle);
	}

	@Test(priority = 3)
	public void testNavBarLink() {
		executor.executeTest("Verify Navigation Bar Link", Constants.USR_MAN, userManagement::navBarLink);
	}

	@Test(priority = 4)
	public void testRefreshButton() {
		executor.executeTest("Verify Refresh Button Functionality", "User Management", userManagement::refreshButton);
	}

	@Test(priority = 5)
	public void testClickAddUserBtn() {
		executor.executeTest("Verify Add User Button Click", "Add User Button Clicked Successfully", userManagement::clickAddUserBtn);
	}

	@Test(priority = 6)
	public void testAddUserProfilePicture() {
		executor.executeTest("Verify Add User Profile Picture", "Profile picture uploaded successfully.", () -> {
			userManagement.addUserProfilePicture();
			return "Profile picture uploaded successfully.";
		});
	}

	@Test(priority = 7)
	public void testAddNewUser() {
		executor.executeTest("Verify Add New User Functionality", "User 'add' operation completed successfully.", () -> {
			userManagement.addAndUpdateUser("add");
			return "User 'add' operation completed successfully.";
		});
	}

	@Test(priority = 8)
	public void testCheckDropdown() {
		executor.executeTest("Verify Dropdown Options Functionality", "Dropdown options verified successfully.", () -> {
			userManagement.checkDropdown();
			return "Dropdown options verified successfully.";
		});
	}

	@Test(priority = 9)
	public void testSearchUser() {
		executor.executeTest("Verify User Search and View Functionality", "User search and view successful.", () -> {
			userManagement.searchAndViewUser();
			return "User search and view successful.";
		});
	}

	@Test(priority = 10)
	public void testViewAndUpdateUser() {
		executor.executeTest("Verify Update User Functionality", "User 'update' operation completed successfully.", () -> {
			userManagement.addAndUpdateUser("update");
			return "User 'update' operation completed successfully.";
		});
	}

	@Test(priority = 11)
	public void testDeleteUser() {
		executor.executeTest("Verify Delete User Functionality", "User deleted successfully", () -> {
			String actual = userManagement.deleteUser();
			if (actual.equalsIgnoreCase("User deleted successfully") || actual.startsWith("No user found")) {
				return actual;
			}
			throw new AssertionError("Delete operation failed: " + actual);
		});
	}

	@Test(priority = 12)
	public void testPagination() {
		executor.executeTest("Verify Pagination Functionality", Constants.EXP_PAGINATION_TEXT, () -> {
			comm.checkPagination();
			return Constants.EXP_PAGINATION_TEXT;
		});
	}

	@Test(priority = 13)
	public void testVersion() {
		executor.executeTest("Verify Version Functionality", Constants.EXP_VERSION_TEXT, comm::checkVersion);
	}

	@Test(priority = 14)
	public void testCopyright() {
		executor.executeTest("Verify Copyright Functionality", Constants.EXP_COPYRIGHT_TEXT, comm::checkCopyright);
	}

	@AfterClass
	public void tearDownAssertions() {
		softAssert.assertAll();
	}
}
