package com.aepl.sam.tests;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aepl.sam.base.TestBase;
import java.util.List;

import com.aepl.sam.pages.CommonMethods;
import com.aepl.sam.pages.DealersManagementPage;
import com.aepl.sam.utils.Constants;
import com.aepl.sam.utils.ExcelUtility;

public class DealersManagementPageTest extends TestBase {
	// Excel Sheet Name
	private static final String SHEET_NAME = "Dealer_Management_Test";

	private ExcelUtility excelUtility;
	private DealersManagementPage dealerPage;
	private CommonMethods comm;
	private SoftAssert softAssert;
	private Executor executor;

	@Override
	@BeforeClass
	public void setUp() {
		super.setUp();
		this.comm = new CommonMethods(driver, wait);
		this.dealerPage = new DealersManagementPage(driver, wait, comm);
		this.excelUtility = new ExcelUtility();
		this.softAssert = new SoftAssert();
		excelUtility.initializeExcel(SHEET_NAME);
		this.executor = new Executor(excelUtility, softAssert);
	}

	@Test(priority = 1)
	public void testCompanyLogo() {
		executor.executeTest("Test Company logo", true, () -> comm.verifyWebpageLogo());
	}

	@Test(priority = 2)
	public void testNavBarLink() {
		executor.executeTest("Test Nav Bar Link for {Dealer Management}", true, dealerPage::navBarLink);
	}

	@Test(priority = 3)
	public void testPageTitle() {
		executor.executeTest("Test page title for {Sim Batch Data Details}", "Sensorise SIM Data Details", dealerPage::verifyPageTitle);
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
	public void testSearchButtonIsEnabled() {
		executor.executeTest("Test search button is enabled? ", true, dealerPage::isSearchButtonEnabled);
	}

	@Test(priority = 8)
	public void testSearchBoxIsEnabled() {
		executor.executeTest("Test search box is enabled? ", true, dealerPage::isSearchBoxEnabled);
	}

	@Test(priority = 9)
	public void testSearchBoxByMultipleInputs() {
		executor.executeTest("Test input box with multiple inputs", true,
				dealerPage::validateSearchBoxWithMultipleInputs);
	}

	@Test(priority = 10)
	public void testTableHeadersOfDealerManagement() {
		final List<String> EXP_TABLE_HEADERS = List.of("FULL NAME", "EMAIL", "MOBILE NO.", "CREATED BY",
				"STATUS", "ACTION");
		executor.executeTest("Test Table headers of {Dealer Management Page}", EXP_TABLE_HEADERS, dealerPage::validateTableHeaders);
	}

	@Test(priority = 11)
	public void testTableDataOfDealerManagement() {
		executor.executeTest("Validate table data of Dealer Management", true, dealerPage::validateTableData);
	}

	@Test(priority = 12)
	public void testViewButtonsEnabled() {
		executor.executeTest("Validate View Buttons in Dealer Management", true, dealerPage::validateViewButtons);
	}

	@Test(priority = 13)
	public void testDeleteButtonsStatusWise() {
		executor.executeTest("Validate Delete Buttons in Dealer Management", true, dealerPage::validateDeleteButtons);
	}

	@Test(priority = 14)
	public void testPaginationOnDealersManagementPage() {
		executor.executeTest("Test Pagination", true, () -> {
			try {
				comm.checkPagination();
				return true;
			} catch (Exception e) {
				logger.error("Pagination test failed: {}", e.getMessage(), e);
				return false;
			}
		});
	}

	@Test(priority = 15)
	public void testAddDealersButtonEnabled() {
		executor.executeTest("Test add dealer button enabled", true,
				dealerPage::isAddDealerButtonEnabled);
	}

	@Test(priority = 16)
	public void testAddDealersButtonVisible() {
		executor.executeTest("Test add dealer button enabled", true,
				dealerPage::isAddDealerButtonVisible);
	}

	@Test(priority = 17)
	public void testClickOnAddDealerBtn() {
		executor.executeTest("Test Add Dealer Button", "Save Dealers Details", dealerPage::clickAddDealerButton);
	}

	@Test(priority = 18)
	public void testAllInputBoxesEnabled() {
		executor.executeTest("Test all input boxes for adding new dealer details", true, dealerPage::testAllFormFields);
	}

	@Test(priority = 19)
	public void testDealerFormValidations() {
		executor.executeTest("Test all input boxes and validations for dealer form", true, dealerPage::testAllFormFieldsErrors);
	}

	@Test(priority = 20)
	public void testSubmitButtonVisibleIfNoDataInputed() {
		executor.executeTest("Test submit button is visible on if not data is visible", true,
				dealerPage::isSubmitButtonIsVisibleIfNoDataIsInputed);
	}

	@Test(priority = 21)
	public void testSubmitButton() {
		executor.executeTest("Test the submit button", true, dealerPage::isDataSubmittedSuccessfully);
	}

	@AfterClass
	public void tearDownAssertions() {
		softAssert.assertAll();
	}
}
