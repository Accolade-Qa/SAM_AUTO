package com.aepl.sam.tests;

import com.aepl.sam.base.TestBase;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aepl.sam.utils.Constants;
import com.aepl.sam.utils.PageActionsUtil;
import com.aepl.sam.utils.PageAssertionsUtil;
import com.aepl.sam.pages.OtaPage;
import com.aepl.sam.utils.ExcelUtility;

public class OtaPageTest extends TestBase {
	private static final String SHEET_NAME = "OTA_Test";

	private ExcelUtility excelUtility;
	private OtaPage ota;
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
		this.ota = new OtaPage(driver, wait);
		this.excelUtility = new ExcelUtility();
		this.softAssert = new SoftAssert();
		this.executor = new Executor(excelUtility, softAssert);
		excelUtility.initializeExcel(SHEET_NAME);
	}

	@Test(priority = -1)
	public void testCompanyLogo() {
		executor.executeTest("Verify Company Logo on Webpage", Constants.EXP_LOGO_DISPLAYED,
				() -> assertion.verifyWebpageLogo() ? Constants.EXP_LOGO_DISPLAYED : "Logo Not Displayed");
	}

	@Test(priority = 0)
	public void testPageTitle() {
		executor.executeTest("Verify Page Title on Webpage", Constants.EXP_PAGE_TITLE_TEXT, assertion::verifyPageTitle);
	}

	@Test(priority = 1)
	public void testNavBarLink() {
		executor.executeTest("Test Navigate to Device Utility Tab", Constants.OTA_LINK, ota::navBarLink);
	}

	@Test(priority = 2)
	public void testAllButtons() {
		executor.executeTest("Test All Buttons on OTA Page", Constants.EXP_VALIDATE_BUTTONS_TEXT, assertion::validateButtons);
	}

	@Test(priority = 3)
	public void testAllComponents() {
		executor.executeTest("Test All Components on OTA Page", Constants.EXP_VALIDATE_COMPONENTS_TEXT, assertion::validateComponents);
	}

	@Test(priority = 4)
	public void testOtaPagePagination() {
		executor.executeTest("Test Pagination on OTA Page", "Pagination is working correctly.", () -> {
			comm.checkPagination();
			return "Pagination is working correctly.";
		});
	}

	@Test(priority = 5)
	public void testManualOtaFeature() {
		executor.executeTest("Test Manual OTA Feature", "New OTA added successfully for IMEI: " + Constants.IMEI, ota::testManualOtaFeature);
	}

	@Test(priority = 6)
	public void testOtaDetails() {
		executor.executeTest("Test OTA Details", "OTA details displayed successfully.", ota::testOtaDetails);
	}

	@Test(priority = 7)
	public void testOtaPagination() {
		executor.executeTest("Test OTA Pagination", "Pagination is working correctly.", () -> {
			comm.checkPagination();
			return "Pagination is working correctly.";
		});
	}

	@Test(priority = 8)
	public void testExportButton() {
		executor.executeTest("Test Export Button on OTA Page", "Export functionality is working correctly.", () -> {
			return comm.validateExportButton() ? "Export functionality is working correctly." : "Export functionality failed.";
		});
	}

	@Test(priority = 9)
	public void testAbortButton() {
		executor.executeTest("Test Abort Button on OTA Page", "Abort functionality is working correctly.", () -> {
			return ota.testAbortButton() ? "Abort functionality is working correctly." : "Abort functionality failed.";
		});
	}

	@Test(priority = 10)
	public void testOtaBatch() {
		executor.executeTest("Test OTA Batch Functionality", "OTA batch functionality is working correctly.", ota::testOtaBatch);
	}

	@AfterClass
	public void tearDownAssertions() {
		softAssert.assertAll();
	}
}




