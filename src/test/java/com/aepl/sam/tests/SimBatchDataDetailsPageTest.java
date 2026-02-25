package com.aepl.sam.tests;

import java.util.List;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aepl.sam.base.TestBase;
import com.aepl.sam.utils.Constants;
import com.aepl.sam.pages.CommonMethods;
import com.aepl.sam.pages.SimBatchDataDetailsPage;
import com.aepl.sam.utils.ExcelUtility;

public class SimBatchDataDetailsPageTest extends TestBase {
	// Excel Sheet Name
	private static final String SHEET_NAME = "Sim_Batch_Data_Details_Test";

	private ExcelUtility excelUtility;
	private SimBatchDataDetailsPage simBatch;
	private CommonMethods comm;
	private SoftAssert softAssert;
	private Executor executor;

	@Override
	@BeforeClass
	public void setUp() {
		super.setUp();
		this.comm = new CommonMethods(driver, wait);
		this.simBatch = new SimBatchDataDetailsPage(driver, wait, comm);
		this.excelUtility = new ExcelUtility();
		this.softAssert = new SoftAssert();
		excelUtility.initializeExcel(SHEET_NAME);
		this.executor = new Executor(excelUtility, softAssert);
	}

	@Test(priority = 1)
	public void testCompanyLogo() {
		executor.executeTest("Test Company logo", true, comm::verifyWebpageLogo);
	}

	@Test(priority = 2)
	public void testNavBarLink() {
		executor.executeTest("Test Nav Bar Link for {Sim Batch Data Details}", true, simBatch::navBarLink);
	}

	@Test(priority = 3)
	public void testPageTitle() {
		executor.executeTest("Test page title for {Sim Batch Data Details}", "Sensorise SIM Data Details", simBatch::verifyPageTitle);
	}

	@Test(priority = 4)
	public void testComponentTitle() {
		executor.executeTest("Test Page Component Title", "SIM Data Details", comm::validateComponentTitle);
	}

	@Test(priority = 5)
	public void testButtons() {
		executor.executeTest("Test all button on page {Sim Batch Data Details}", Constants.EXP_VALIDATE_BUTTONS_TEXT, comm::validateButtons);
	}

	@Test(priority = 6)
	public void testComponents() {
		executor.executeTest("Test All Components on the page {Sim Batch Data Details}", Constants.EXP_VALIDATE_COMPONENTS_TEXT, comm::validateComponents);
	}

	@Test(priority = 7)
	public void testDownloadSampleFile() {
		executor.executeTest("Test the sample file on page {Sim Batch Data Details}", "File downloaded successfully.", comm::clickSampleFileButton);
	}

	@Test(priority = 8)
	public void testUploadBoxIsCorrect() {
		executor.executeTest("Test the input box is correct", "Upload ICCID's to get SIM Data Details", simBatch::validateCorrectBox);
	}

	@Test(priority = 9)
	public void testErrorMessageOfTheInputBox() {
		executor.executeTest("Test error message for blank input", "This field is mandatory.", () -> "");
	}

	@Test(priority = 10)
	public void testUploadButtonIsEnabled() {
		executor.executeTest("Test the upload button is enabled", true, simBatch::isUploadButtonIsEnabled);
	}

	@Test(priority = 11)
	public void testUploadFile() {
		executor.executeTest("Test upload file", true, simBatch::validateUpload);
	}

	@Test(priority = 12)
	public void testSubmitButton() {
		executor.executeTest("Test Submit button", true, simBatch::validateSubmitButton);
	}

	@Test(priority = 16)
	public void testUploadSimDataDetailsComponentsTableHeaders() {
		final List<String> EXP_UPLOAD_TABLE_HEADERS = List.of("ICCID", "CARD STATE", "CARD STATUS",
			"PRIMARY TSP", "FALLBACK TSP",
			"PRIMARY STATUS", "PRIMARY MSISDN", "FALLBACK STATUS", "FALLBACK MSISDN", "ACTIVE PROFILES",
			"CARD EXPIRY DATE", "PRODUCT NAME", "IS RSU REQUIRED", "IS IMSI REQUIRED", "ACTIVE SR NUMBER");
	}

	@Test(priority = 17)
	public void testDuplicateICCIDInUploadedExcelSheetTableHeaders() {
		final List<String> EXP_DUPLICATE_ICCID_HEADERS = List.of("ICCID", "MESSAGE");
	}

	@Test(priority = 18)
	public void testICCIDNotPresentInSensoriseDatabaseTableHeaders() {
		final List<String> EXP_NOT_PRESENT_ICCID_HEADERS = List.of("ICCID", "MESSAGE");
	}

	@Test(priority = 19)
	public void testICCIDNotPresentInSensoriseDatabaseExportButton() {
		executor.executeTest("Test Export button of ICCID Not present", true, simBatch::validateExportButton);
	}

	@Test(priority = 20)
	public void testDuplicateICCIDInUploadedExcelSheetExportButton() {
		executor.executeTest("Test Export button of Duplicate ICCID Uploaded", true, simBatch::validateExportButton);
	}

	@Test(priority = 21)
	public void testUploadSimDataDetailsComponentsExportButton() {
		executor.executeTest("Test Export button of Sim Data Details", true, simBatch::validateExportButton);
	}

	@Test(priority = 22)
	public void testManualUploadButtonIsVisible() {
		final boolean EXP_MANUAL_UPLOAD_VISIBLE = true;
	}

	@Test(priority = 23)
	public void testManualUploadButtonIsClickable() {
		final boolean EXP_MANUAL_UPLOAD_CLICKABLE = true;
	}

	@Test(priority = 24)
	public void testManualUploadClickAndOpen() {
		final List<String> EXP_MANUAL_UPLOAD_CLICKED_OPENED = List.of(Constants.SIM_MANUAL_UPLOAD,
			"SIM Data Details");
	}

	@Test(priority = 27)
	public void testInputBoxEnabled() {
		executor.executeTest("Test input box enabled", true, simBatch::isInputBoxEnabled);
	}

	@Test(priority = 28)
	public void testEmptyInputValidation() {
		final String EXP_EMPTY_INPUT_VALIDATION = "This field is required and can't be only spaces.";
	}

	@Test(priority = 29)
	public void testShortInputValidation() {
		final String EXP_SHORT_INPUT_VALIDATION = "Value must be exactly 20 characters long.";
	}

	@Test(priority = 30)
	public void testLongInputValidation() {
		final String EXP_LONG_INPUT_VALIDATION = "Value must be exactly 20 characters long.";
	}

	@Test(priority = 31)
	public void testSpecialCharValidation() {
		final String EXP_SPECIAL_CHAR_VALIDATION = "Special characters are not allowed.";
	}

	@Test(priority = 32)
	public void testSubmitButtonEnabled() {
		executor.executeTest("Test submit button enabled", true, simBatch::isSubmitButtonEnabled);
	}

	@Test(priority = 33)
	public void testClickSubmitButton() {
		executor.executeTest("Test Clicked the submit button", true, simBatch::clickSubmitButton);
	}

	@Test(priority = 35)
	public void testPaginationofSimDataDetailsWholePage2() {
		executor.executeTest("Test pagination of the whole {Sim Data Details Page}", true, () -> {
			try {
				((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
				Thread.sleep(500);
				comm.checkPagination();
				return true;
			} catch (Exception e) {
				return false;
			}
		});
	}

	@Test(priority = 36)
	public void testVersion() {
		executor.executeTest("Verify Application Version Display", Constants.EXP_VERSION_TEXT, comm::checkVersion);
	}

	@Test(priority = 37)
	public void testCopyright() {
		executor.executeTest("Verify Copyright Text", Constants.EXP_COPYRIGHT_TEXT, comm::checkCopyright);
	}

	@AfterClass
	public void tearDownAssertions() {
		softAssert.assertAll();
	}
}
