package com.aepl.sam.tests;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aepl.sam.base.TestBase;
import com.aepl.sam.utils.Constants;
import com.aepl.sam.pages.CommonMethods;
import com.aepl.sam.pages.RoleManagementPage;
import com.aepl.sam.utils.ExcelUtility;

public class RoleManagementPageTest extends TestBase {
	private static final String SHEET_NAME = "User_Role_Test";

	private ExcelUtility excelUtility;
	private RoleManagementPage userRole;
	private CommonMethods comm;
	private SoftAssert softAssert;
	private Executor executor;

	@Override
	@BeforeClass
	public void setUp() {
		super.setUp();
		this.comm = new CommonMethods(driver, wait);
		this.userRole = new RoleManagementPage(driver, wait);
		this.excelUtility = new ExcelUtility();
		this.softAssert = new SoftAssert();
		this.executor = new Executor(excelUtility, softAssert);
		excelUtility.initializeExcel(SHEET_NAME);
	}

	@Test(priority = 1)
	public void testCompanyLogo() {
		executor.executeTest("Verify Company Logo on Webpage", Constants.EXP_LOGO_DISPLAYED,
				() -> comm.verifyWebpageLogo() ? Constants.EXP_LOGO_DISPLAYED : "Logo Not Displayed");
	}

	@Test(priority = 2)
	public void testPageTitle() {
		executor.executeTest("Verify Page Title on Webpage", Constants.EXP_PAGE_TITLE_TEXT, comm::verifyPageTitle);
	}

	@Test(priority = 3)
	public void testNavBarLink() {
		executor.executeTest("Verify NavBar Link Navigation", Constants.ROLE_MANAGEMENT, userRole::navBarLink);
	}

	// @Test(priority = 4)
	public void testBackButton() {
		executor.executeTest("Verify Back Button Navigation", "Back button navigated successfully.", () -> {
			userRole.backButton();
			return "Back button navigated successfully.";
		});
	}

	// @Test(priority = 5)
	public void testRefreshButton() {
		executor.executeTest("Verify Refresh Button Functionality", "Role Management", userRole::refreshButton);
	}

	@Test(priority = 6)
	public void testClickAddUserRole() {
		executor.executeTest("Verify 'Add User Role' Button Click", "Role Management", userRole::clickAddUserRoleBtn);
	}

	@Test(priority = 7)
	public void testSelectingOptions() {
		executor.executeTest("Verify Selecting Options in Add User Role", "Options selected successfully", () -> {
			userRole.selectingOptions();
			return "Options selected successfully";
		});
	}

	@Test(priority = 8)
	public void testSelectOptionsAndSubmit() {
		executor.executeTest("Verify Select All Permissions and Submit Role", "User role permissions selected and submitted", () -> {
			userRole.selectOptionsAndSubmit();
			return "User role permissions selected and submitted";
		});
	}

	@Test(priority = 9)
	public void testSearchUserRole() {
		executor.executeTest("Verify Search User Role Functionality", "Role 'DEMO' should be found and searched successfully", () -> {
			userRole.searchUserRole();
			return "Role 'DEMO' should be found and searched successfully";
		});
	}

	// @Test(priority = 10)
	public void testUpdateUserRole() {
		executor.executeTest("Verify Update User Role Functionality", "User role should be updated successfully", () -> {
			userRole.updateUserRole();
			return "User role should be updated successfully";
		});
	}

	// @Test(priority = 11)
	public void testDeleteUserRole() {
		executor.executeTest("Verify Delete User Role Functionality", "User role should be deleted successfully", () -> {
			userRole.deleteUserRole();
			return "User role should be deleted successfully";
		});
	}

	// // @Test(priority = 12)
	public void testPagination() {
		executor.executeTest("Verify Pagination Functionality", Constants.EXP_PAGINATION_TEXT, () -> {
			comm.checkPagination();
			return Constants.EXP_PAGINATION_TEXT;
		});
	}

	// @Test(priority = 13)
	public void testVersion() {
		executor.executeTest("Verify Version Functionality", Constants.EXP_VERSION_TEXT, comm::checkVersion);
	}

	// @Test(priority = 14)
	public void testCopyright() {
		executor.executeTest("Verify Copyright Functionality", Constants.EXP_COPYRIGHT_TEXT, comm::checkCopyright);
	}

	@AfterClass
	public void tearDownAssertions() {
		softAssert.assertAll();
	}
}
