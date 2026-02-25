package com.aepl.sam.tests;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aepl.sam.base.TestBase;
import com.aepl.sam.utils.Constants;
import com.aepl.sam.utils.PageAssertionsUtil;
import com.aepl.sam.pages.ProductionDevicePage;
import com.aepl.sam.utils.ExcelUtility;

public class ProductionDevicePageTest extends TestBase {

	private static final String DEVICE_EXCEL_SHEET = "Device_Dashboard_Test";

	private ProductionDevicePage productionDevicePage;
	private PageAssertionsUtil assertion;
	private ExcelUtility excelUtility;
	private SoftAssert softAssert;
	private Executor executor;

	@Override
	@BeforeClass
	public void setUp() {
		super.setUp();
		this.productionDevicePage = new ProductionDevicePage(driver, wait);
		this.assertion = new PageAssertionsUtil(driver, wait);
		this.excelUtility = new ExcelUtility();
		this.softAssert = new SoftAssert();
		this.executor = new Executor(excelUtility, softAssert);
		this.excelUtility.initializeExcel(DEVICE_EXCEL_SHEET);
		logger.info("Setup completed for ProductionDevicePageTest");
	}

	@Test(priority = 1)
	public void testCompanyLogo() {
		executor.executeTest("Verify Company Logo on Webpage", Constants.EXP_LOGO_DISPLAYED,
				() -> assertion.verifyWebpageLogo() ? Constants.EXP_LOGO_DISPLAYED : "Logo Not Displayed");
	}

	// this is main page title here - AEPL Sampark Diagnostic Cloud
	@Test(priority = 2)
	public void testPageTitle() {
		executor.executeTest("Verify Page Title on Webpage", Constants.EXP_PAGE_TITLE_TEXT, assertion::verifyPageTitle);
	}

	// validate that the navbar link is clickable
	@Test(priority = 3)
	public void testNavBarLinkClickable() {
		executor.executeTest("Test nav bar link is clickable", true, () -> {
			return productionDevicePage.isnavBarLinkClickable();
		});
	}

	@Test(priority = 4)
	public void testNavBarLink() {
		executor.executeTest("Test Navigate to Device Utility Tab", Constants.PROD_DEVICE_LINK, () -> {
			return productionDevicePage.navBarLink();
		});
	}

	// validate that the add production devices link is visible
	@Test(priority = 5)
	public void testAddProdDeviceLinkVisible() {
		executor.executeTest("Test is add production device link is visible", true, () -> {
			return productionDevicePage.isAddProdDeviceLinkVisible();
		});
	}

	// validate that the add production devices link is clickable
	@Test(priority = 6)
	public void testAddProdDeviceLinkClickable() {
		executor.executeTest("Test is add production device link is clickable", true, () -> {
			return productionDevicePage.isAddProdDeviceLinkClickable();
		});
	}

	// validate all buttons
	@Test(priority = 7)
	public void testAllButtonsVisible() {
		executor.executeTest("Test all buttons are visible", Constants.EXP_VALIDATE_BUTTONS_TEXT,
				assertion::validateButtons);
	}

	// validate all components
	@Test(priority = 8)
	public void testAllComponentsVisible() {
		executor.executeTest("Test all components are visible",
				Constants.EXP_VALIDATE_COMPONENTS_TEXT, assertion::validateComponents);
	}

	// validate the manual upload button is visible
	@Test(priority = 9)
	public void testManualUploadButtonVisible() {
		executor.executeTest("Test is manual upload button is visible", true, () -> {
			return productionDevicePage.isManualUploadButtonVisible();
		});
	}

	// validate the manual upload button is clickable
	@Test(priority = 10)
	public void testManualUploadButtonClickable() {
		executor.executeTest("Test is manual upload button is clickable", true, () -> {
			return productionDevicePage.isManualUploadButtonClickable();
		});
	}

	/*** Manual Upload ***/
	// Change this below link to manual upload
	// validate that correct link is clicked and opened for manual upload -
	// production device
	// validating with the page title.
	@Test(priority = 11)
	public void testClickManualUploadProductionDevices() {
		executor.executeTest("Test Navigate to Add Production Device button", "Create Production Device",
				productionDevicePage::clickManualUploadProductionDevicesButton);
	}

	/** UID validations **/

	// validate error msg of UID for blank input
	@Test(priority = 12)
	public void testErrorValidationInUIDBlankBox() {
		executor.executeTest("Validate error message for blank UID input",
				"This field is required and can't be only spaces.",
				() -> productionDevicePage.validateSingleInputBox("uid", " "));
	}

	// validate error msg of UID for short input - 19 chars
	@Test(priority = 13)
	public void testErrorValidationInUIDInvalidShort() {
		executor.executeTest("Validate error message for short UID input", "Value must be exactly 19 characters long.",
				() -> productionDevicePage.validateSingleInputBox("uid", "ACON4SA2122"));
	}

	// validate error msg of UID for long input - 19 chars
	@Test(priority = 14)
	public void testErrorValidationInUIDInvalidLong() {
		executor.executeTest("Validate error message for long UID input", "Value must be exactly 19 characters long.",
				() -> productionDevicePage.validateSingleInputBox("uid", "ACON4SA212240006474XX"));
	}

	// validate error msg of UID for leading space
	@Test(priority = 15)
	public void testErrorValidationInUIDLeadingSpace() {
		executor.executeTest("Validate error message for leading space in UID input",
				"Remove leading or trailing spaces.",
				() -> productionDevicePage.validateSingleInputBox("uid", " ACON4SA212240006474"));
	}

	// validate error msg of UID for trailing space
	@Test(priority = 16)
	public void testErrorValidationInUIDTrailingSpace() {
		executor.executeTest("Validate error message for trailing space in UID input",
				"Remove leading or trailing spaces.",
				() -> productionDevicePage.validateSingleInputBox("uid", "ACON4SA212240006474 "));
	}

	/** IMEI validations **/

	// validate error msg of IMEI for blank input
	@Test(priority = 17)
	public void testErrorValidationInIMEIBlankBox() {
		executor.executeTest("Validate error message for blank IMEI input",
				"This field is required and can't be only spaces.",
				() -> productionDevicePage.validateSingleInputBox("imei", " "));
	}

	// validate error msg of IMEI for short input - 15 chars
	@Test(priority = 18)
	public void testErrorValidationInIMEIInvalidShort() {
		executor.executeTest("Validate error message for short IMEI input", "Value must be exactly 15 characters long.",
				() -> productionDevicePage.validateSingleInputBox("imei", "123456789012"));
	}

	// validate error msg of IMEI for long input - 15 chars
	@Test(priority = 19)
	public void testErrorValidationInIMEIInvalidLong() {
		executor.executeTest("Validate error message for long IMEI input", "Value must be exactly 15 characters long.",
				() -> productionDevicePage.validateSingleInputBox("imei", "12345678901234567"));
	}

	// validate error msg of IMEI for leading space
	@Test(priority = 20)
	public void testErrorValidationInIMEILeadingSpace() {
		executor.executeTest("Validate error message for leading space in IMEI input",
				"Remove leading or trailing spaces.",
				() -> productionDevicePage.validateSingleInputBox("imei", " 123456789012345"));
	}

	// validate error msg of IMEI for trailing space
	@Test(priority = 21)
	public void testErrorValidationInIMEITrailingSpace() {
		executor.executeTest("Validate error message for trailing space in IMEI input",
				"Remove leading or trailing spaces.",
				() -> productionDevicePage.validateSingleInputBox("imei", "123456789012345 "));
	}

	/** ICCID validations **/

	// validate error msg of ICCID for blank input
	@Test(priority = 22)
	public void testErrorValidationInICCIDBlankBox() {
		executor.executeTest("Validate error message for blank ICCID input",
				"This field is required and can't be only spaces.",
				() -> productionDevicePage.validateSingleInputBox("iccid", " "));
	}

	// validate error msg of ICCID for short input - 20 chars
	@Test(priority = 23)
	public void testErrorValidationInICCIDInvalidShort() {
		executor.executeTest("Validate error message for short ICCID input",
				"Value must be exactly 20 characters long.",
				() -> productionDevicePage.validateSingleInputBox("iccid", "89148000001234567"));
	}

	// validate error msg of ICCID for long input - 20 chars
	@Test(priority = 24)
	public void testErrorValidationInICCIDInvalidLong() {
		executor.executeTest("Validate error message for long ICCID input", "Value must be exactly 20 characters long.",
				() -> productionDevicePage.validateSingleInputBox("iccid", "89148000001234567890XX"));
	}

	// validate error msg of ICCID for leading space
	@Test(priority = 25)
	public void testErrorValidationInICCIDLeadingSpace() {
		executor.executeTest("Validate error message for leading space in ICCID input",
				"Remove leading or trailing spaces.",
				() -> productionDevicePage.validateSingleInputBox("iccid", " 89148000001234567890"));
	}

	// validate error msg of ICCID for trailing space
	@Test(priority = 26)
	public void testErrorValidationInICCIDTrailingSpace() {
		executor.executeTest("Validate error message for trailing space in ICCID input",
				"Remove leading or trailing spaces.",
				() -> productionDevicePage.validateSingleInputBox("iccid", "89148000001234567890 "));
	}

	/** Device Model Name Validations **/
	// validate device model name should be clickable
	/*** Commenting this method because this is impact on the below methods ***/
	// @Test(priority = 27)
	public void testDeviceModelNameClickable() {
		executor.executeTest("Test is device model name dropdown is clickable", true, () -> {
			return productionDevicePage.isDeviceModelNameClickable();
		});
	}

	// validate error msg for blank input
	// No input/mat-select found for: mat-select---- this will gives this error
	// every time
	// @Test(priority = 28)
	public void testErrorValidationInDeviceModelNameBlankBox() {
		executor.executeTest("Validate error message for blank Device Model Name input",
				"This field is required and can't be only spaces.",
				() -> productionDevicePage.validateSingleInputBox("modelName", " "));
	}

	/** Mobile Validations **/

	// validate error msg of Mobile for blank input
	@Test(priority = 29)
	public void testErrorValidationInMobileBlankBox() {
		executor.executeTest("Validate error message for blank Mobile input",
				"This field is required and can't be only spaces.",
				() -> productionDevicePage.validateSingleInputBox("mobile", " "));
	}

	// validate error msg of Mobile for short input - 10 chars
	@Test(priority = 30)
	public void testErrorValidationInMobileInvalidShort() {
		executor.executeTest("Validate error message for short Mobile input",
				"Value must be exactly 10 characters long.",
				() -> productionDevicePage.validateSingleInputBox("mobile", "987654321"));
	}

	// validate error msg of Mobile for long input - 10 chars
	@Test(priority = 31)
	public void testErrorValidationInMobileInvalidLong() {
		executor.executeTest("Validate error message for long Mobile input",
				"Value must be exactly 10 characters long.",
				() -> productionDevicePage.validateSingleInputBox("mobile", "987654321012"));
	}

	// validate error msg of Mobile for leading space
	@Test(priority = 32)
	public void testErrorValidationInMobileLeadingSpace() {
		executor.executeTest("Validate error message for leading space in Mobile input",
				"Remove leading or trailing spaces.",
				() -> productionDevicePage.validateSingleInputBox("mobile", " 9876543210"));
	}

	// validate error msg of Mobile for trailing space
	@Test(priority = 33)
	public void testErrorValidationInMobileTrailingSpace() {
		executor.executeTest("Validate error message for trailing space in Mobile input",
				"Remove leading or trailing spaces.",
				() -> productionDevicePage.validateSingleInputBox("mobile", "9876543210 "));
	}

	// validate error msg of Mobile for chars and special chars input
	@Test(priority = 34)
	public void testErrorValidationInMobileCharsSpecialChars() {
		executor.executeTest("Validate error message for chars and special chars in Mobile input",
				"Only numbers are allowed.", () -> productionDevicePage.validateSingleInputBox("mobile", "AB@#%@#$@#"));
	}

	/** Service Provider Validations **/

	// validate error msg of Service Provider for blank input
	@Test(priority = 35)
	public void testErrorValidationInServiceProviderBlankBox() {
		executor.executeTest("Validate error message for blank Service Provider input",
				"This field is required and can't be only spaces.",
				() -> productionDevicePage.validateSingleInputBox("serviceProvider", " "));
	}

	// validate error msg of Service Provider for leading space
	@Test(priority = 36)
	public void testErrorValidationInServiceProviderLeadingSpace() {
		executor.executeTest("Validate error message for leading space in Service Provider input",
				"Remove leading or trailing spaces.",
				() -> productionDevicePage.validateSingleInputBox("serviceProvider", " Airtel"));
	}

	// validate error msg of Service Provider for trailing space
	@Test(priority = 37)
	public void testErrorValidationInServiceProviderTrailingSpace() {
		executor.executeTest("Validate error message for trailing space in Service Provider input",
				"Remove leading or trailing spaces.",
				() -> productionDevicePage.validateSingleInputBox("serviceProvider", "Airtel "));
	}

	/** Alt Mobile Validations **/
	// NOTE : Alt Mobile is not mandatory field and it does not contains error msg.
	// also does not contains '*' mark

	// validate error msg of Alt Mobile for blank input
	// @Test(priority = 38)
	public void testErrorValidationInAltMobileBlankBox() {
		executor.executeTest("Validate error message for blank Alt Mobile input",
				"This field is required and can't be only spaces.",
				() -> productionDevicePage.validateSingleInputBox("altMobile", " "));
	}

	// validate error msg of Alt Mobile for short input - 10 chars
	// @Test(priority = 39)
	public void testErrorValidationInAltMobileInvalidShort() {
		executor.executeTest("Validate error message for short Alt Mobile input",
				"Value must be exactly 10 characters long.",
				() -> productionDevicePage.validateSingleInputBox("altMobile", "987654321"));
	}

	// validate error msg of Alt Mobile for long input - 10 chars
	// @Test(priority = 40)
	public void testErrorValidationInAltMobileInvalidLong() {
		executor.executeTest("Validate error message for long Alt Mobile input",
				"Value must be exactly 10 characters long.",
				() -> productionDevicePage.validateSingleInputBox("altMobile", "987654321012"));
	}

	// validate error msg of Alt Mobile for leading space
	// @Test(priority = 41)
	public void testErrorValidationInAltMobileLeadingSpace() {
		executor.executeTest("Validate error message for leading space in Alt Mobile input",
				"Remove leading or trailing spaces.",
				() -> productionDevicePage.validateSingleInputBox("altMobile", " 9876543210"));
	}

	// validate error msg of Alt Mobile for trailing space
	// @Test(priority = 42)
	public void testErrorValidationInAltMobileTrailingSpace() {
		executor.executeTest("Validate error message for trailing space in Alt Mobile input",
				"Remove leading or trailing spaces.",
				() -> productionDevicePage.validateSingleInputBox("altMobile", "9876543210 "));
	}

	// validate error msg of Alt Mobile for chars and special chars input
	// @Test(priority = 43)
	public void testErrorValidationInAltMobileCharsSpecialChars() {
		executor.executeTest("Validate error message for chars and special chars in Alt Mobile input",
				"Only numbers are allowed.",
				() -> productionDevicePage.validateSingleInputBox("altMobile", "AB@#%@#$@#"));
	}

	/** ALT Service Provider Validations **/

	// validate error msg of ALT Service Provider for blank input
	// NOTE : Alt Mobile is not mandatory field and it does not contains error msg.
	// also does not contains '*' mark
	// @Test(priority = 44)
	public void testErrorValidationInAltServiceProviderBlankBox() {
		executor.executeTest("Validate error message for blank ALT Service Provider input",
				"This field is required and can't be only spaces.",
				() -> productionDevicePage.validateSingleInputBox("altServiceProvider", " "));
	}

	// validate error msg of ALT Service Provider for leading space
	// @Test(priority = 45)
	public void testErrorValidationInAltServiceProviderLeadingSpace() {
		executor.executeTest("Validate error message for leading space in ALT Service Provider input",
				"Remove leading or trailing spaces.",
				() -> productionDevicePage.validateSingleInputBox("altServiceProvider", " Jio"));
	}

	// validate error msg of ALT Service Provider for trailing space
	// @Test(priority = 46)
	public void testErrorValidationInAltServiceProviderTrailingSpace() {
		executor.executeTest("Validate error message for trailing space in ALT Service Provider input",
				"Remove leading or trailing spaces.",
				() -> productionDevicePage.validateSingleInputBox("altServiceProvider", "Jio "));
	}

	/** Firmware Validations **/
	// validate error msg of Firmware for blank input
	@Test(priority = 47)
	public void testErrorValidationInFirmwareBlankBox() {
		executor.executeTest("Validate error message for blank Firmware input",
				"This field is required and can't be only spaces.",
				() -> productionDevicePage.validateSingleInputBox("firmware", " "));
	}

	// validate error msg of Firmware for leading space
	@Test(priority = 48)
	public void testErrorValidationInFirmwareLeadingSpace() {
		executor.executeTest("Validate error message for leading space in Firmware input",
				"Remove leading or trailing spaces.",
				() -> productionDevicePage.validateSingleInputBox("firmware", " v1.0.0"));
	}

	// validate error msg of Firmware for trailing space
	@Test(priority = 49)
	public void testErrorValidationInFirmwareTrailingSpace() {
		executor.executeTest("Validate error message for trailing space in Firmware input",
				"Remove leading or trailing spaces.",
				() -> productionDevicePage.validateSingleInputBox("firmware", "v1.0.0 "));
	}

	/** SIM Vendor **/
	// validate error msg of SIM Vendor for blank input
	@Test(priority = 50)
	public void testErrorValidationInSIMVendorBlankBox() {
		executor.executeTest("Validate error message for blank SIM Vendor input",
				"This field is required and can't be only spaces.",
				() -> productionDevicePage.validateSingleInputBox("simVendor", " "));
	}

	/** Submit Button **/
	// validate submit buttons should be disable if no data is inputed
	@Test(priority = 51)
	public void testSubmitButtonDisabledNoData() {
		executor.executeTest("Validate submit button is disabled on no data input",
				"Submit button is disabled as expected.", () -> {
					return productionDevicePage.isSubmitButtonDisabledNoData()
							? "Submit button is disabled as expected."
							: "Submit button is enabled unexpectedly.";
				});
	}

	// validate click on submit button with all correct data should submit the data
	// and toast msg should be validated
	@Test(priority = 52)
	public void testAddProdDevice() {
		executor.executeTest("Test submit data with all valid data", "Data Fetched Successfully",
				() -> productionDevicePage.NewInputFields("add"));
	}

	/*** Bulk Upload ***/
	// validate the bulk upload button is enabled
	@Test(priority = 53)
	public void testBulkUploadButtonEnabled() {
		executor.executeTest("Test is bulk upload button is enabled", true, () -> {
			return productionDevicePage.isBulkUploadButtonEnabled();
		});
	}

	// validate the bulk upload button is clickabled
	@Test(priority = 54)
	public void testBulkUploadButtonClickable() {
		executor.executeTest("Test is bulk upload button is clickable", true, () -> {
			return productionDevicePage.isBulkUploadButtonClickable();
		});
	}

	// validate click on bulk upload button should opens correct page - page title
	// validate
	@Test(priority = 55)
	public void testClickBulkUploadProductionDevices() {
		executor.executeTest("Test Navigate to Bulk Upload Production Device button", "Add Production Devices",
				productionDevicePage::clickBulkUploadProductionDevicesButton);
	}

	// validate download sample button is enabled
	@Test(priority = 56)
	public void testDownloadSampleButtonEnabled() {
		executor.executeTest("Test is download sample button is enabled", true, () -> {
			return productionDevicePage.isDownloadSampleButtonEnabled();
		});
	}

	// validate the download sample button is clickable
	@Test(priority = 57)
	public void testDownloadSampleButtonClickable() {
		executor.executeTest("Test is download sample button is clickable", true, () -> {
			return productionDevicePage.isDownloadSampleButtonClickable();
		});
	}

	// validate the sample download button
	@Test(priority = 58)
	public void testDownloadSampleButton() {
		executor.executeTest("Test download sample button", "File downloaded successfully.", () -> {
			return productionDevicePage.downloadSampleFile();
		});
	}

	// validate attach button is enable
	@Test(priority = 59)
	public void testAttachButtonEnabled() {
		executor.executeTest("Test is attach button is enabled", true, () -> {
			return productionDevicePage.isAttachButtonEnabled();
		});
	}

	// validate attach button is clickable
	@Test(priority = 60)
	public void testAttachButtonClickable() {
		executor.executeTest("Test is attach button is clickable", true, () -> {
			return productionDevicePage.isAttachButtonClickable();
		});
	}

	// validate submit button should be disable on no data/file selected
	@Test(priority = 61)
	public void testSubmitButtonDisabledWhenNoFileUploadedOnBulkUploadPage() {
		executor.executeTest("Test submit button is disabled when no file is uploaded on bulk upload page", true,
				() -> productionDevicePage.isSubmitButtonDisabledWhenNoFileUploaded());
	}

	// validate click and open window and send the file path
	@Test(priority = 62)
	public void testAttachFile() {
		executor.executeTest("Test attach file", "Data Saved Successfully!!", () -> {
			return productionDevicePage.uploadFileAndSubmit();
		});
	}

	/***
	 * NOTE : These below components test cases were pending to automate...Those are
	 * after the bulk upload file upload test cases 1. Uploaded Production Device
	 * List 2. Duplicate Device List 3. Invalid Device List
	 ***/

	/*** main list of production device list ***/
	// validate that on successful addition of device will reflect it on the main
	// page
	@Test(priority = 63)
	public void testValidateAddedDeviceInList() {
		executor.executeTest("Validate added device is reflected in the production device list", Constants.DEVICE_UID,
				() -> productionDevicePage.validateAddedDeviceInList());
	}

	// validate that the table headers are present
	@Test(priority = 64)
	public void testTableHeadersPresent() {
		// UID IMEI ICCID Model Name Action // write below with upper cases
		List<String> expectedHeaders = Arrays.asList("UID", "IMEI", "ICCID", "MODEL NAME", "ACTION");
		executor.executeTest("Validate table headers are present", expectedHeaders,
				() -> productionDevicePage.areTableHeadersPresent());
	}

	// validate all view buttons enabled
	@Test(priority = 65)
	public void testAllViewButtonsEnabled() {
		executor.executeTest("Test all view buttons are enabled", true,
				() -> productionDevicePage.areAllViewButtonsEnabled());
	}

	// validate all delete buttons enabled
	@Test(priority = 66)
	public void testAllDeleteButtonsEnabled() {
		executor.executeTest("Test all delete buttons are enabled", true,
				() -> productionDevicePage.areAllDeleteButtonsEnabled());
	}

	// validate search box is enabled
	@Test(priority = 67)
	public void testSearchBoxEnabled() {
		executor.executeTest("Test is search box is enabled", true, () -> {
			return productionDevicePage.isSearchBoxEnabled();
		});
	}

	// validate search box is clickable
	@Test(priority = 68)
	public void testSearchBoxClickable() {
		executor.executeTest("Test is search box is clickable", true, () -> {
			return productionDevicePage.isSearchBoxClickable();
		});
	}

	// validate search button is enabled
	@Test(priority = 69)
	public void testSearchButtonEnabled() {
		executor.executeTest("Test is search button is enabled", true, () -> {
			return productionDevicePage.isSearchButtonEnabled();
		});
	}

	// validate search button is clickable
	@Test(priority = 70)
	public void testSearchButtonClickable() {
		executor.executeTest("Test is search button is clickable", true, () -> {
			return productionDevicePage.isSearchButtonClickable();
		});
	}

	// validate search production device by inputting proper details
	@Test(priority = 71)
	public void testSearchProductionDevice() {
		executor.executeTest("Test search production device by UID", Constants.DEVICE_UID,
				() -> productionDevicePage.searchProductionDevice());
	}

	/** Update Page **/
	// validate click on view button against searched opens page for update the
	// device details
	@Test(priority = 72)
	public void testClickViewButtonForUpdate() {
		executor.executeTest("Test click view button for update device", true,
				() -> productionDevicePage.clickViewButtonForUpdate());
	}

	// validate that UID, IMEI, ICCID these fields are in read only mode
	@Test(priority = 73)
	public void testUIDIMEIICCIDReadOnly() {
		executor.executeTest("Validate UID, IMEI, ICCID fields are in read only mode", true,
				() -> productionDevicePage.areUIDIMEIICCIDReadOnly());
	}

	// validate that update button is visible
	@Test(priority = 74)
	public void testUpdateButtonVisible() {
		executor.executeTest("Test is update button is visible", true, () -> {
			return productionDevicePage.isUpdateButtonVisible();
		});
	}

	// validate update some fields and press update button
	@Test(priority = 75)
	public void testUpdateProductionDevice() {
		executor.executeTest("Test update production device details", "Data Fetched Successfully",
				() -> productionDevicePage.NewInputFields("update"));
	}

	// @Test(priority = 3)
	// public void testAddProdDevice() {
	// executor.executeTest(TC_ADD_DEVICE, EXP_ADD_DEVICE, () ->
	// productionDevicePage.NewInputFields("add"));
	// }
	//
	// @Test(priority = 4)
	// public void testSearchDevice() {
	// executor.executeTest(TC_SEARCH_DEVICE, EXP_SEARCH_DEVICE,
	// productionDevicePage::searchDevice);
	// }
	//
	// @Test(priority = 5)
	// public void testViewDevice() {
	// executor.executeTest(TC_VIEW_DEVICE, EXP_VIEW_DEVICE,
	// productionDevicePage::viewDevice);
	// }
	//
	// @Test(priority = 6)
	// public void testUpdateDevice() {
	// executor.executeTest(TC_UPDATE_DEVICE, EXP_UPDATE_DEVICE, () ->
	// productionDevicePage.NewInputFields("update"));
	// }
	//
	// @Test(priority = 7)
	// public void testSearchDeviceAgain() {
	// executor.executeTest(TC_SEARCH_DEVICE, EXP_SEARCH_DEVICE,
	// productionDevicePage::searchDevice);
	// }
	//
	// @Test(priority = 8)
	// public void testDeleteDevice() {
	// executor.executeTest(TC_DELETE_DEVICE, EXP_DELETE_DEVICE,
	// productionDevicePage::DeleteDevice);
	// }
	//
	// @Test(priority = 9)
	// public void testPagination() {
	// executor.executeTest(TC_PAGINATION, Constants.EXP_PAGINATION_TEXT, () -> {
	// comm.checkPagination();
	// return Constants.EXP_PAGINATION_TEXT;
	// });
	// }

	@Test(priority = 99)
	public void testVersion() {
		executor.executeTest("Verify Version Functionality", Constants.EXP_VERSION_TEXT, assertion::checkVersion);
	}

	@Test(priority = 100)
	public void testCopyright() {
		executor.executeTest("Verify Copyright Functionality", Constants.EXP_COPYRIGHT_TEXT, assertion::checkCopyright);
	}

	@AfterClass
	public void tearDownAssertions() {
		softAssert.assertAll();
	}
}

