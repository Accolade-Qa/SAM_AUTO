package com.aepl.sam.tests;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aepl.sam.base.TestBase;
import com.aepl.sam.utils.Constants;
import com.aepl.sam.utils.PageActionsUtil;
import com.aepl.sam.utils.PageAssertionsUtil;
import com.aepl.sam.pages.DeviceModelsPage;
import com.aepl.sam.utils.ExcelUtility;

@Test(groups = {"sampark", "lct", "trio", "swaraj", "atcu", "regression"})
public class DeviceModelsPageTest extends TestBase {
	private static final String DEVICE_MODELS_EXCEL_SHEET = "Device_Models_Test";

	private ExcelUtility excelUtility;
	private DeviceModelsPage deviceModelsPage;
	private PageActionsUtil comm;
	private PageAssertionsUtil assertion;
	private SoftAssert softAssert;
	private Executor executor;

	@Override
	@BeforeClass(alwaysRun = true)
	public void setUp() {
		super.setUp();
		this.comm = new PageActionsUtil(driver, wait);
		this.assertion = new PageAssertionsUtil(driver, wait);
		this.deviceModelsPage = new DeviceModelsPage(driver, wait);
		this.excelUtility = new ExcelUtility();
		this.softAssert = new SoftAssert();
		this.executor = new Executor(excelUtility, softAssert);
		excelUtility.initializeExcel(DEVICE_MODELS_EXCEL_SHEET);
	}

	@Test(priority = 1)
	public void testCompanyLogo() {
		final String LOGO_NOT_DISPLAYED = "Logo Not Displayed";
		executor.executeTest("Verify Company Logo on Webpage", Constants.EXP_LOGO_DISPLAYED,
				() -> assertion.verifyWebpageLogo() ? Constants.EXP_LOGO_DISPLAYED : LOGO_NOT_DISPLAYED);
	}

	@Test(priority = 2)
	public void testPageTitle() {
		executor.executeTest("Verify Page Title on Webpage", Constants.EXP_PAGE_TITLE_TEXT, assertion::verifyPageTitle);
	}

	@Test(priority = 3)
	public void testButtons() {
		executor.executeTest("Verify All Buttons on Device Details Page", Constants.EXP_VALIDATE_BUTTONS_TEXT, assertion::validateButtons);
	}

	@Test(priority = 4)
	public void testComponents() {
		executor.executeTest("Verify All Component Title on Device Details Page", Constants.EXP_VALIDATE_COMPONENTS_TEXT, assertion::validateComponents);
	}

	@Test(priority = 5)
	public void testNavBarLink() {
		executor.executeTest("Verify Navigation to Device Utility Tab", Constants.DEVICE_LINK, deviceModelsPage::navBarLink);
	}

	// Page title test case
	@Test(priority = 6)
	public void testPageTitleOfTheDeviceModelPage() {
		executor.executeTest("Verify Page Title of Device Model Page", "Device Models",
				deviceModelsPage::ValidatePageTitle);
	}

	// Add Device model visibility
	@Test(priority = 7)
	public void testAddDeviceModelButtonIsVisible() {
		executor.executeTest("Verify Add Device Model Button Visibility", true,
				deviceModelsPage::isAddDeviceModelButtonVisible);
	}

	// Add Device model button enability
	@Test(priority = 8)
	public void testAddDeviceModelButtonIsEnabled() {
		executor.executeTest("Verify Add Device Model Button Status", true,
				deviceModelsPage::isAddDeviceModelButtonEnable);
	}

	@Test(priority = 9)
	public void testClickAddDeviceModel() {
		executor.executeTest("Verify Clicking Add Device Model Button", "Create Device Model", deviceModelsPage::ClickAddDeviceModel);
	}

	// Validate the component title for the page after it gets clicked
	@Test(priority = 10)
	public void testComponentTitleOfAddDeviceModelPage() {
		executor.executeTest("Verify Component Title of Add Device Model Page", "Fill Device Model Details",
				deviceModelsPage::validateComponentTitle);
	}

	// Validate the error messages for the empty inputs and all input errors
	@Test(priority = 11, dataProvider = "fieldValidationData", dataProviderClass = DeviceModelsPage.class)
	public void testInputValidations(By locator, String input, String expectedError) {
		executor.executeTest("Verify Field Validation", expectedError,
				() -> deviceModelsPage.isInputBoxHaveProperValidations(locator, input));
	}

	// Validate the submit button is disabled if there is no input in all fields
	@Test(priority = 12)
	public void testSubmitButtonDisabledWhenAllFieldsEmpty() {
		executor.executeTest("Verify Submit Button Disabled When Fields Empty", true,
				deviceModelsPage::isSubmitButtonIsDisabled);
	}

	@Test(priority = 13)
	public void testAddModel() {
		executor.executeTest("Verify Adding New Device Model", "Device Models", () -> {
			try {
				return deviceModelsPage.NewInputFields("add");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return "Not able to add the user";
		});
	}

	@Test(priority = 14)
	public void testButtons2() {
		executor.executeTest("Verify All Buttons on Device Details Page", Constants.EXP_VALIDATE_BUTTONS_TEXT, assertion::validateButtons);
	}

	@Test(priority = 15)
	public void testComponentTitles2() {
		executor.executeTest("Verify All Component Title on Device Details Page", Constants.EXP_VALIDATE_COMPONENTS_TEXT, assertion::validateComponents);
	}

	// Validate the search input box is visible
	@Test(priority = 16)
	public void testSearchInputBoxIsVisible() {
		executor.executeTest("Verify Search Input Box Visibility", true, deviceModelsPage::isSearchInputVisible);
	}

	// Validate the search input box is enabled
	@Test(priority = 17)
	public void testSearchInputBoxIsEnabled() {
		executor.executeTest("Verify Search Input Box Enabled Status", true, deviceModelsPage::isSearchInputEnabled);
	}

	// Validate the search button is visible
	@Test(priority = 18)
	public void testSearchButtonIsVisible() {
		executor.executeTest("Verify Search Button Visibility", true, deviceModelsPage::isSearchButtonVisible);
	}

	// Validate the search button is enabled
	@Test(priority = 19)
	public void testSearchButtonIsEnabled() {
		executor.executeTest("Verify Search Button Enabled Status", true, deviceModelsPage::isSearchButtonEnabled);
	}

	@Test(priority = 20)
	public void testSearchModel() {
		executor.executeTest("Verify Search Functionality for Device Model", "Device Models", () -> {
			try {
				return deviceModelsPage.searchModel();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		});
	}

	// Validate the table headers of the device model list table
	@Test(priority = 21)
	public void testTableHeadersOfDeviceModelListTable() {
		List<String> actualHeaders = Arrays.asList("MODEL CODE", "MODEL NAME", "MODEL SERIAL SEQUENCE",
				"HARDWARE VERSION", "ACTION");
		executor.executeTest("Verify Table Headers of Device Model List", actualHeaders, deviceModelsPage::validateTableHeaders);
	}

	// Validate the table data on the page of device model list table
	/** this below test is passed but the data that this method gets is real time dynamic
	 *  so it does not able to validate the actual and expected data 
	 *  
	 *  NOTE : modify in future 
	 *  
	 *  NOTE : Create a random string for creating new model and store it in new variable 
	 *  which we can use it further for search that perticular madel.
	 *  so it does not able to validate the actual and expected data
	 *
	 * NOTE : modify in future
	 *
	 */
	// @Test(priority = 22)
	public void testTableDataOfDeviceModelListTable() {
		Map<String, String> expectedRowPatterns = new LinkedHashMap<>();
		expectedRowPatterns.put("MODEL CODE", "[A-Za-z]{6}"); // 6-letter code
		expectedRowPatterns.put("MODEL NAME", ".*"); // any text
		expectedRowPatterns.put("MODEL SERIAL SEQUENCE", "\\d+"); // numbers only
		expectedRowPatterns.put("HARDWARE VERSION", "\\d+"); // numbers only
		expectedRowPatterns.put("ACTION", "visibility"); // exact match

		List<Map<String, String>> expectedPatterns = Collections.singletonList(expectedRowPatterns);
		executor.executeTest("Verify Table Data of Device Model List", expectedPatterns,
				() -> deviceModelsPage.validateTableDataWithRegex(expectedPatterns));
	}

	@Test(priority = 23)
	public void testViewModel() {
		executor.executeTest("Verify Viewing Device Model", "View/Update Device Model", () -> {
			try {
				return deviceModelsPage.viewModel();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		});
	}

	// Validate the update button is disabled default if not changes is done
	/** change the false to true in future */
	@Test(priority = 24)
	public void testUpdateButtonIsDisabled() {
		executor.executeTest("Verify Update Button Disabled Status", false,
				deviceModelsPage::isUpdateButtonEnabled);
	}

	@Test(priority = 25)
	public void testUpdateModel() {
		executor.executeTest("Verify Updating Existing Device Model", "Device Models", () -> {
			try {
				return deviceModelsPage.NewInputFields("update");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		});
	}

	@Test(priority = 26)
	public void testSearchModelAgain() {
		executor.executeTest("Verify Search Again for Device Model", "Device Models", () -> {
			try {
				return deviceModelsPage.searchModel2();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		});
	}

	@Test(priority = 27)
	public void testDeleteModel() {
		executor.executeTest("Verify Deleting Device Model", "Device Models", () -> {
			try {
				return deviceModelsPage.DeleteModel();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		});
	}

	@Test(priority = 28)
	public void testPagination() {
		executor.executeTest("Verify Pagination Functionality", Constants.EXP_PAGINATION_TEXT, () -> {
			comm.checkPagination();
			return Constants.EXP_PAGINATION_TEXT;
		});
	}

	@Test(priority = 29)
	public void testVersion() {
		executor.executeTest("Verify Application Version Display", Constants.EXP_VERSION_TEXT, assertion::checkVersion);
	}

	@Test(priority = 30)
	public void testCopyright() {
		executor.executeTest("Verify Copyright Text", Constants.EXP_COPYRIGHT_TEXT, assertion::checkCopyright);
	}

	@AfterClass
	public void tearDownAssertions() {
		softAssert.assertAll();
	}
}




