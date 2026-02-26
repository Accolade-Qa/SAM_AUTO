package com.aepl.sam.tests;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aepl.sam.base.TestBase;
import com.aepl.sam.utils.Constants;
import com.aepl.sam.utils.PageActionsUtil;
import com.aepl.sam.utils.PageAssertionsUtil;
import com.aepl.sam.pages.FotaPage;
import com.aepl.sam.utils.ExcelUtility;

public class FotaPageTest extends TestBase {
	private static final String FOTA_EXCEL_SHEET = "FOTA_Test";

	private FotaPage fota;
	private ExcelUtility excelUtility;
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
		this.fota = new FotaPage(driver, wait);
		this.excelUtility = new ExcelUtility();
		this.softAssert = new SoftAssert();
		this.executor = new Executor(excelUtility, softAssert);
		excelUtility.initializeExcel(FOTA_EXCEL_SHEET);
		logger.info("Setup completed for FotaPageTest");
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
	public void testRefreshButton() {
		executor.executeTest("Verify Refresh Button Functionality", "Clicked on the refreshed button", () -> {
			comm.clickRefreshButton();
			return "Clicked on the refreshed button";
		});
	}

	@Test(priority = 4)
	public void testDeviceUtility() {
		executor.executeTest("Verify Device Utility Functionality", "Clicked on Device Utility", () -> {
			fota.clickDeviceUtility();
			return "Clicked on Device Utility";
		});
	}

	@Test(priority = 5)
	public void testFota() {
		executor.executeTest("Verify FOTA Functionality", "Clicked on FOTA", () -> {
			fota.clickFota();
			return "Clicked on FOTA";
		});
	}

	@Test(priority = 6)
	public void testAllComponents() {
		executor.executeTest("Verify All Components on Webpage", Constants.EXP_VALIDATE_COMPONENTS_TEXT, assertion::validateComponents);
	}

	@Test(priority = 7)
	public void testAllButtons() {
		executor.executeTest("Verify All Buttons on Webpage", Constants.EXP_VALIDATE_BUTTONS_TEXT, assertion::validateButtons);
	}

	@Test(priority = 8)
	public void testCreateManualFotaBatch() {
		executor.executeTest("Create FOTA Batch", "FOTA batch created successfully.", () -> {
			fota.selectFOTATypeButton("manual");
			fota.createManualFotaBatch(Constants.IMEI);
			return "FOTA batch created successfully.";
		});
	}

	@Test(priority = 9)
	public void testCreateBulkFotaBatch() {
		executor.executeTest("Create Bulk FOTA Batch", "Bulk FOTA batch created successfully.", () -> {
			fota.selectFOTATypeButton("bulk");
			comm.clickSampleFileButton();
			fota.createBulkFotaBatch();
			return "Bulk FOTA batch created successfully.";
		});
	}

	@Test(priority = 10)
	public void testPagination() {
		executor.executeTest("Pagination Check", "Pagination verified successfully!", () -> {
			comm.checkPagination();
			return "Pagination verified successfully!";
		});
	}

	@Test(priority = 11)
	public void testGetFotaBatchList() {
		executor.executeTest("Get FOTA Batch List", "Batch seted successfully!", fota::getFotaBatchList);
	}

	@Test(priority = 12)
	public void testFotaBatchButtons() {
		executor.executeTest("Verify All Buttons on Webpage", Constants.EXP_VALIDATE_BUTTONS_TEXT, assertion::validateButtons);
	}

	@Test(priority = 13)
	public void testVersion() {
		executor.executeTest("Verify Version Functionality", Constants.EXP_VERSION_TEXT, assertion::checkVersion);
	}

	@Test(priority = 14)
	public void testCopyright() {
		executor.executeTest("Verify Copyright Functionality", Constants.EXP_COPYRIGHT_TEXT, assertion::checkCopyright);
	}

	@AfterClass
	public void tearDownAssertions() {
		softAssert.assertAll();
	}
}




