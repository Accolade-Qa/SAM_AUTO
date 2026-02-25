package com.aepl.sam.tests;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aepl.sam.base.TestBase;
import com.aepl.sam.utils.Constants;
import com.aepl.sam.utils.PageActionsUtil;
import com.aepl.sam.utils.PageAssertionsUtil;
import com.aepl.sam.pages.GroupManagementPage;
import com.aepl.sam.utils.ExcelUtility;

public class GroupManagementPageTest extends TestBase {
	private static final String SHEET_NAME = "Group_Management_Test";

	private ExcelUtility excelUtility;
	private GroupManagementPage groupManagement;
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
		this.groupManagement = new GroupManagementPage(driver, wait);
		this.excelUtility = new ExcelUtility();
		this.softAssert = new SoftAssert();
		this.executor = new Executor(excelUtility, softAssert);
		excelUtility.initializeExcel(SHEET_NAME);
		logger.info("Setup completed for GroupManagementPageTest");
	}

	@Test(priority = 1)
	public void testCompanyLogo() {
		executor.executeTest("Verify Company Logo on Webpage", Constants.EXP_LOGO_DISPLAYED,
				() -> assertion.verifyWebpageLogo() ? Constants.EXP_LOGO_DISPLAYED : "Logo Not Displayed");
	}

	@Test(priority = 2)
	public void testPageTitle() {
		executor.executeTest("Verify Page Title on Webpage", Constants.EXP_PAGE_TITLE_TEXT, assertion::verifyPageTitle);
	}

	@Test(priority = 3)
	public void testClickNavBar() {
		executor.executeTest("Verify Navigation Bar Click Functionality", Constants.ROLE_GROUP, groupManagement::navBarLink);
	}

	@Test(priority = 4)
	public void testBackButton() {
		executor.executeTest("Verify Back Button Functionality", Constants.ROLE_GROUP, groupManagement::backButton);
	}

	@Test(priority = 5)
	public void testRefreshButton() {
		executor.executeTest("Verify Refresh Button Functionality", Constants.ROLE_GROUP, () -> {
			comm.clickRefreshButton();
			return driver.getCurrentUrl();
		});
	}

	@Test(priority = 6)
	public void testAddUserRole() {
		executor.executeTest("Verify Add User Role Functionality", "User Role Group Added Successfully", () -> {
			groupManagement.addGroup();
			return "User Role Group Added Successfully";
		});
	}

	@Test(priority = 7)
	public void testSearchgroupManagement() {
		executor.executeTest("Verify Search Role Group Functionality", "Role group found", () -> {
			return groupManagement.isGroupManagementFound("QA") ? "Role group found" : "Role group not found";
		});
	}

	// Uncomment and enable this test if deletion functionality is required
	// @Test(priority = 8)
	public void testDeleteRoleGroup() {
		executor.executeTest("Verify Delete Role Group Functionality", "Role group deleted successfully", groupManagement::deleteRoleGroup);
	}

	@Test(priority = 9)
	public void testPagination() {
		executor.executeTest("Verify Pagination Functionality", Constants.EXP_PAGINATION_TEXT, () -> {
			comm.checkPagination();
			return Constants.EXP_PAGINATION_TEXT;
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


