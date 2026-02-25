package com.aepl.sam.tests;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aepl.sam.base.TestBase;
import com.aepl.sam.pages.CommonMethods;
import com.aepl.sam.pages.CustomerMasterPage;
import com.aepl.sam.utils.Constants;
import com.aepl.sam.utils.ExcelUtility;

public class CustomerMasterPageTest extends TestBase {
	// Excel Sheet Name
	private static final String CUSTOMER_MASTER_EXCEL_SHEET = "Customer_Master_Test";

	private CustomerMasterPage customerMasterPage;
	private CommonMethods comm;
	private ExcelUtility excelUtility;
	private SoftAssert softAssert;
	private Executor executor;

	@Override
	@BeforeClass
	public void setUp() {
		super.setUp();
		this.customerMasterPage = new CustomerMasterPage(driver, wait);
		this.comm = new CommonMethods(driver, wait);
		this.excelUtility = new ExcelUtility();
		this.softAssert = new SoftAssert();
		this.excelUtility.initializeExcel(CUSTOMER_MASTER_EXCEL_SHEET);
		this.executor = new Executor(excelUtility, softAssert);
		logger.info("Setup completed for CustomerMasterPageTest");
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
	public void testClickNavBar() {
		executor.executeTest("Verify Navigation Bar Click Functionality", "http://aepltest.accoladeelectronics.com:6102/customer-master", customerMasterPage::navBarLink);
	}

	@Test(priority = 4)
	public void testButtons1() {
		executor.executeTest("Verify Buttons on Customer Master Page", Constants.EXP_VALIDATE_BUTTONS_TEXT, comm::validateButtons);
	}

	@Test(priority = 5)
	public void testEmptyInputBoxError() {
		executor.executeTest("Verify Empty Input Box Error", "This field is required and can't be empty.",
				customerMasterPage::emptyInputBoxErrorValidation);
	}

	@Test(priority = 6)
	public void testWrongInputBoxError() {
		executor.executeTest("Verify Wrong Input Box Error", "Only alphabets and spaces are allowed.",
				customerMasterPage::wrongInputBoxErrorValidation);
	}

	@Test(priority = 7)
	public void testAddNewCustomer() {
		executor.executeTest("Verify Add New Customer Functionality", "Customer Added Successfully", customerMasterPage::addNewCustomer);
	}

	@Test(priority = 8)
	public void testComponentTitle() {
		executor.executeTest("Verify Component Title", "Customer List", comm::validateComponentTitle);
	}

	@Test(priority = 9)
	public void testButtons2() {
		executor.executeTest("Verify Buttons on Customer Master Page", Constants.EXP_VALIDATE_BUTTONS_TEXT, comm::validateButtons);
	}

	@Test(priority = 10, enabled = false)
	public void testInputBoxError() {
		executor.executeTest("Verify Input Box Error Handling", " This field is required and can't be empty.", () -> {
			String res = comm.validateInputBoxError();
			if (res.equals(" This field is required and can't be empty.")) {
				return res;
			}
			return "Input Box Error Not Displayed";
		});
	}

	@Test(priority = 11)
	public void testSearchCustomer() {
		executor.executeTest("Verify Search Customer Functionality", "Customer Found", customerMasterPage::searchCustomer);
	}

	@Test(priority = 12)
	public void testIsSearchInputEnabled() {
		executor.executeTest("Verify Search Input Enabled", "input box is enabled", () -> {
			return customerMasterPage.isSearchInputEnabled() ? "input box is enabled" : "input box is not enabled";
		});
	}

	@Test(priority = 13)
	public void testIsSearchInputVisible() {
		executor.executeTest("Verify Search Input Visible", "input box is displayed", () -> {
			return customerMasterPage.isSearchInputVisible() ? "input box is displayed" : "input box is not displayed";
		});
	}

	@Test(priority = 14)
	public void testIsSearchButtonEnabled() {
		executor.executeTest("Verify Search Button Enabled", "search button is enabled", () -> {
			return customerMasterPage.isSearchButtonEnabled() ? "search button is enabled"
					: "search button is not enabled";
		});
	}

	@Test(priority = 15)
	public void testIsSearchButtonVisible() {
		executor.executeTest("Verify Search Button Visible", "search button is visible", () -> {
			return customerMasterPage.isSearchButtonVisible() ? "search button is visible"
					: "search button is not visible";
		});
	}

	@Test(priority = 16)
	public void testEditCustomer() {
		executor.executeTest("Verify Edit Customer Functionality", "Customer Edited Successfully", () -> {
			customerMasterPage.editCustomer();
			return "Customer Edited Successfully";
		});
	}

	@Test(priority = 17)
	public void testIsEditButtonEnabled() {
		executor.executeTest("Verify Edit Button Enabled", "edit button is visible", () -> {
			return customerMasterPage.isEditButtonEnabled() ? "edit button is visible" : "edit button is not visible";
		});
	}

	@Test(priority = 18)
	public void testIsEditButtonDisplayed() {
		executor.executeTest("Verify Edit Button Visible", "edit button is visible", () -> {
			return customerMasterPage.isEditButtonDisplayed() ? "edit button is visible" : "edit button is not visible";
		});
	}

	@Test(priority = 19)
	public void testValidateEditedCustomer() {
		executor.executeTest("Verify Edited Customer Validation", "Customer data validated successfully", () -> {
			customerMasterPage.validateCustomerTable();
			return "Customer data validated successfully";
		});
	}

	@Test(priority = 20)
	public void testIsDeleteButtonEnabled() {
		executor.executeTest("Verify Delete Button Enabled", "delete button is visible", () -> {
			return customerMasterPage.isDeleteButtonEnabled() ? "delete button is visible"
					: "delete button is not visible";
		});
	}

	@Test(priority = 21)
	public void testIsDeleteButtonDisplayed() {
		executor.executeTest("Verify Delete Button Visible", "delete button is visible", () -> {
			return customerMasterPage.isDeleteButtonDisplayed() ? "delete button is visible"
					: "delete button is not visible";
		});
	}

	@Test(priority = 22)
	public void testDeleteCustomer() {
		executor.executeTest("Verify Delete Customer Functionality", "Customer Deleted Successfully", () -> {
			customerMasterPage.deleteCustomer();
			return "Customer Deleted Successfully";
		});
	}

	@Test(priority = 23)
	public void testValidateComponents() {
		executor.executeTest("Verify Components on Customer Master Page", Constants.EXP_VALIDATE_COMPONENTS_TEXT, comm::validateComponents);
	}

	@Test(priority = 24)
	public void testPagination() {
		executor.executeTest("Verify Pagination Functionality", Constants.EXP_PAGINATION_TEXT, () -> {
			comm.checkPagination();
			return Constants.EXP_PAGINATION_TEXT;
		});
	}

	@Test(priority = 25)
	public void testVersion() {
		executor.executeTest("Verify Version Functionality", Constants.EXP_VERSION_TEXT, comm::checkVersion);
	}

	@Test(priority = 26)
	public void testCopyright() {
		executor.executeTest("Verify Copyright Functionality", Constants.EXP_COPYRIGHT_TEXT, comm::checkCopyright);
	}

	@AfterClass
	public void tearDownAssertions() {
		softAssert.assertAll();
	}
}
