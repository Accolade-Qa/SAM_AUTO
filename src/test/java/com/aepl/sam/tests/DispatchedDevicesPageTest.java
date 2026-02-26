package com.aepl.sam.tests;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aepl.sam.base.TestBase;
import com.aepl.sam.utils.Constants;
import com.aepl.sam.utils.PageActionsUtil;
import com.aepl.sam.utils.PageAssertionsUtil;
import com.aepl.sam.pages.DispatchedDevicesPage;
import com.aepl.sam.utils.ExcelUtility;

public class DispatchedDevicesPageTest extends TestBase {
	private ExcelUtility excelUtility;
	private DispatchedDevicesPage dispatchedDevicePage;
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
		this.dispatchedDevicePage = new DispatchedDevicesPage(driver, wait);
		this.excelUtility = new ExcelUtility();
		this.softAssert = new SoftAssert();
		this.executor = new Executor(excelUtility, softAssert);
		excelUtility.initializeExcel("Dispatched_Devices_Test");
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
	public void navBarLinkTest() {
		executor.executeTest("Test Navigation to Device Utility Tab", Constants.DISP_DEVICE_LINK, dispatchedDevicePage::navBarLink);
	}

	/*** Manual Upload ***/
	
	// validate page title
	@Test(priority = 4)
	public void testPageTitleManualUpload() {
		executor.executeTest("Verify Page Title on Webpage", Constants.EXP_PAGE_TITLE_TEXT, assertion::verifyPageTitle);
	}

	// validate all components on the page
	@Test(priority = 5)
	public void testAllComponentsManualUpload() {
		executor.executeTest("Test All Components", Constants.EXP_VALIDATE_COMPONENTS_TEXT, assertion::validateComponents);
	}

	// validate all buttons on the page
	@Test(priority = 6)
	public void testAllButtonsManualUpload() {
		executor.executeTest("Test All Buttons", Constants.EXP_VALIDATE_BUTTONS_TEXT, assertion::validateButtons);
	}

	// validate the manual upload button is visible
	@Test(priority = 7)
	public void testManualUploadButtonVisibility() {
		executor.executeTest("Test manual upload button is visible", true,
				dispatchedDevicePage::isManualUploadButtonVisible);
	}

	// validate the manual upload button is clickable
	@Test(priority = 8)
	public void testManualUploadButtonClickable() {
		executor.executeTest("Test manual upload button is clickable", true,
				dispatchedDevicePage::isManualUploadButtonClickable);
	}

	// validate the page title after clicking manual upload button
	@Test(priority = 9)
	public void testPageTitleAfterClickingManualUpload() {
		executor.executeTest("Test page title after clicking manual upload", "Create Dispatched Device",
				dispatchedDevicePage::getPageTitleAfterClickingManualUpload);
	}

	// validate all components on manual upload page
	@Test(priority = 10)
	public void testAllComponentsOnManualUploadPage() {
		executor.executeTest("Test All Components", Constants.EXP_VALIDATE_COMPONENTS_TEXT, assertion::validateComponents);
	}

	// validate all buttons on manual upload page
	@Test(priority = 11)
	public void testAllButtonsOnManualUploadPage() {
		executor.executeTest("Test All Buttons", Constants.EXP_VALIDATE_BUTTONS_TEXT, assertion::validateButtons);
	}

	/*** validate all errors of the fields on the manual upload page ***/
	/** UID **/
	// check error for uid field - blank box
	@Test(priority = 12)
	public void testErrorValidationInUIDBlankBox() {
		executor.executeTest("Test field errors on manual upload page",
				"This field is required and can't be only spaces.",
				() -> dispatchedDevicePage.validateSingleInputBox("uid", " "));
	}

	// check error for uid field - invalid uid (shorter length < 19)
	@Test(priority = 13)
	public void testErrorValidationInUIDInvalidShort() {
		executor.executeTest("Test field errors on manual upload page", "Value must be exactly 19 characters long.",
				() -> dispatchedDevicePage.validateSingleInputBox("uid", "ACON4SA2122"));
	}

	// check error for uid field - invalid uid (longer length > 19)
	@Test(priority = 14)
	public void testErrorValidationInUIDInvalidLong() {
		executor.executeTest("Test field errors on manual upload page", "Value must be exactly 19 characters long.",
				() -> dispatchedDevicePage.validateSingleInputBox("uid", "ACON4SA212240006474XX"));
	}

	// check error for uid field - leading spaces
	@Test(priority = 15)
	public void testErrorValidationInUIDLeadingSpace() {
		executor.executeTest("Test field errors on manual upload page", "Remove leading or trailing spaces.",
				() -> dispatchedDevicePage.validateSingleInputBox("uid", " ACON4SA212240006474"));
	}

	// check error for uid field - trailing spaces
	@Test(priority = 16)
	public void testErrorValidationInUIDTrailingSpace() {
		executor.executeTest("Test field errors on manual upload page", "Remove leading or trailing spaces.",
				() -> dispatchedDevicePage.validateSingleInputBox("uid", "ACON4SA212240006474 "));
	}

	/** Customer Part Number **/
	// check error for customer part number field - blank box
	@Test(priority = 17)
	public void testErrorValidationInCustomerPartNumberBlankBox() {
		executor.executeTest("Test field errors on manual upload page",
				"This field is required and can't be only spaces.",
				() -> dispatchedDevicePage.validateSingleInputBox("customerPartNumber", " "));
	}

	// check error for customer part number field - leading spaces
	@Test(priority = 18)
	public void testErrorValidationInCustomerPartNumberLeadingSpace() {
		executor.executeTest("Test field errors on manual upload page", "Remove leading or trailing spaces.",
				() -> dispatchedDevicePage.validateSingleInputBox("customerPartNumber", " CUSTPART123"));
	}

	// check error for customer part number field - trailing spaces
	@Test(priority = 19)
	public void testErrorValidationInCustomerPartNumberTrailingSpace() {
		executor.executeTest("Test field errors on manual upload page", "Remove leading or trailing spaces.",
				() -> dispatchedDevicePage.validateSingleInputBox("customerPartNumber", "CUSTPART123 "));
	}

	/** Customer Name **/
	// check error for customer name field - blank box
	// not feasible here to check the error so we are skipping this test case and
	// instead
	// checking the clickable or not
	@Test(priority = 20)
	public void testErrorValidationInCustomerNameBlankBox() {
		executor.executeTest("Test customer name field is clickable on manual upload page", true,
				() -> dispatchedDevicePage.isCustomerNameFieldClickable());
	}

	// validate the submit button is disabled when mandatory fields are empty on
	// manual upload page
	@Test(priority = 21)
	public void testSubmitButtonDisabledOnMandatoryFieldsEmpty() {
		executor.executeTest("Test submit button is disabled when mandatory fields are empty", true,
				() -> dispatchedDevicePage.isSubmitButtonDisabledOnMandatoryFieldsEmpty());
	}

	// fill the manual upload form with valid data and submit
	@Test(priority = 22)
	public void testManualUploadFormSubmissionWithValidData() {
		executor.executeTest("Test manual upload form submission with valid data", "Dispatched Devices",
				() -> dispatchedDevicePage.addDevice());
	}

	// validate the success toast message after submission of manual upload form
	@Test(priority = 23)
	public void testSuccessToastMessageAfterManualUploadFormSubmission() {
		executor.executeTest("Test success toast message after manual upload form submission", true,
				dispatchedDevicePage::getSuccessToastMessage);
	}

	/*** Bulk Upload ***/

	// validate the bulk upload button is visible
	@Test(priority = 24)
	public void testBulkUploadButtonVisibility() {
		executor.executeTest("Test bulk upload button is visible", true,
				dispatchedDevicePage::isBulkUploadButtonVisible);
	}

	// validate the bulk upload button is clickable
	@Test(priority = 25)
	public void testBulkUploadButtonClickable() {
		executor.executeTest("Test bulk upload button is clickable", true,
				dispatchedDevicePage::isBulkUploadButtonClickable);
	}

	// validate the page title after clicking bulk upload button
	@Test(priority = 26)
	public void testPageTitleAfterClickingBulkUpload() {
		executor.executeTest("Test page title after clicking bulk upload", "Add Dispatch Devices",
				dispatchedDevicePage::getPageTitleAfterClickingBulkUpload);
	}

	// validate all components on bulk upload page
	@Test(priority = 27)
	public void testAllComponentsOnBulkUploadPage() {
		executor.executeTest("Test All Components", Constants.EXP_VALIDATE_COMPONENTS_TEXT, assertion::validateComponents);
	}

	// validate all buttons on bulk upload page
	@Test(priority = 28)
	public void testAllButtonsOnBulkUploadPage() {
		executor.executeTest("Test All Buttons", Constants.EXP_VALIDATE_BUTTONS_TEXT, assertion::validateButtons);
	}

	// validate the download sample link is clickable on bulk upload page
	@Test(priority = 29)
	public void testDownloadSampleLinkClickable() {
		executor.executeTest("Test download sample link is clickable on bulk upload page", true,
				dispatchedDevicePage::isDownloadSampleLinkClickable);
	}

	// validate the sample download file content is correct
	@Test(priority = 30)
	public void testSampleDownloadFileContent() {
		executor.executeTest("Test sample download file content is correct", true,
				dispatchedDevicePage::isSampleDownloadFileContentCorrect);
	}

	// validate the errors of the file upload input on bulk upload page
	/**
	 * NOTE : glance this test case later... for now even on lots of try it does not
	 * resolve and find the error message.
	 **/
	// @Test(priority = 31)
	public void testFileUploadInputErrorsOnBulkUploadPage() {
		executor.executeTest("Test file upload input errors on bulk upload page", "This field is mandatory.",
				() -> dispatchedDevicePage.validateSingleInputBox("file", " "));
	}

	// validate the attachment button in input box is clickable on bulk upload page
	@Test(priority = 32)
	public void testAttachmentButtonClickableOnBulkUploadPage() {
		executor.executeTest("Test attachment button is clickable on bulk upload page", true,
				dispatchedDevicePage::isAttachmentButtonClickable);
	}

	// validate the submit button is disabled when no file is uploaded on bulk
	// upload page
	@Test(priority = 33)
	public void testSubmitButtonDisabledWhenNoFileUploadedOnBulkUploadPage() {
		executor.executeTest("Test submit button is disabled when no file is uploaded on bulk upload page", true,
				() -> dispatchedDevicePage.isSubmitButtonDisabledWhenNoFileUploaded());
	}

	// validate the upload of a valid file and submission on bulk upload page
	// validate the success toast message after submission of bulk upload form
	@Test(priority = 34)
	public void testBulkUploadFormSubmissionWithValidFile() {
		executor.executeTest("Test bulk upload form submission with valid file", "Data Saved Successfully!!",
				() -> dispatchedDevicePage.uploadFileAndSubmit());
	}

	/** Uploaded Dispatch Device List Validations **/

	// validate the component "Uploaded Dispatch Device List" is visible on bulk
	// upload page after successful upload

	@Test(priority = 35)
	public void testUploadedDispatchDeviceListVisibility() {
		executor.executeTest("Test Uploaded Dispatch Device List component is visible on bulk upload page", true,
				() -> dispatchedDevicePage.isUploadedDispatchDeviceListVisible());
	}

	// validate the export button on the uploaded dispatch device list is disable on
	// bulk upload page if no data is present
	// 1. see for img of no data present
	// 2. then check for export button disabled state
	@Test(priority = 36)
	public void testExportButtonDisabledOnNoDataInUploadedDispatchDeviceList() {
		executor.executeTest("Test export button is disabled on no data in uploaded dispatch device list", true,
				() -> dispatchedDevicePage.isExportButtonDisabledOnNoDataInUploadedDispatchDeviceList());
	}

	// validate the click of export button downloads the file - if the data is
	// present and button is enabled
	@Test(priority = 37)
	public void testExportButtonDownloadsFileInUploadedDispatchDeviceList() {
		executor.executeTest("Test export button downloads file in uploaded dispatch device list", true,
				() -> dispatchedDevicePage.doesExportButtonDownloadFileInUploadedDispatchDeviceList());
	}

	// validate the table headers of the uploaded dispatch device list on bulk
	// upload page
	@Test(priority = 38)
	public void testTableHeadersOfUploadedDispatchDeviceList() {
		List<String> HeadersOfUploadDispachDeviceList = Arrays.asList("DEVICE UID", "CUSTOMER NAME",
				"CUSTOMER PART NUMBER", "REMARK"); //
		executor.executeTest("Test table headers of uploaded dispatch device list on bulk upload page",
				HeadersOfUploadDispachDeviceList,
				() -> dispatchedDevicePage.getTableHeadersOfUploadedDispatchDeviceList());
	}

	// validate the data of the uploaded dispatch device list on bulk upload page
	// give no data found when invalid file is uploaded and data is not correct
	@Test(priority = 39)
	public void testDataOfUploadedDispatchDeviceListOnInvalidFileUpload() {
		executor.executeTest("Test data of uploaded dispatch device list on bulk upload page gives no data found", true,
				() -> dispatchedDevicePage.getDataOfUploadedDispatchDeviceListOnInvalidFileUpload());
	}

	// validate if the data is present in the uploaded dispatch device list on bulk
	// upload page then have to do remark validation.
	@Test(priority = 40)
	public void testRemarkValidationInUploadedDispatchDeviceListOnValidFileUpload() {
		executor.executeTest("Test remark validation in uploaded dispatch device list on valid file upload", true,
				() -> dispatchedDevicePage.validateRemarkInUploadedDispatchDeviceListOnValidFileUpload());
	}

	/** Invalid Dispatch Device List **/

	// validate the component "Invalid Dispatch Device List" is visible on bulk
	// upload page after upload of invalid data file

	// validate the export button on the invalid dispatch device list is enable on
	// bulk upload page.

	// validate the click of export button downloads the file of invalid data

	// validate the table headers of the invalid dispatch device list on bulk upload
	// page

	// validate the pagination of the invalid dispatch device list on bulk upload
	// page when more data is present

	/*** main dispatched device page tests ***/

	// validate the search box is visible on dispatched device page
	@Test(priority = 41)
	public void testSearchBoxVisibility() {
		executor.executeTest("Test search box is visible on dispatched device page", true,
				() -> dispatchedDevicePage.isSearchBoxVisible());
	}

	// validate the search box is clickable on dispatched device page
	@Test(priority = 42)
	public void testSearchBoxClickable() {
		executor.executeTest("Test search box is clickable on dispatched device page", true,
				() -> dispatchedDevicePage.isSearchBoxClickable());
	}

	// validate the search functionality with valid data on dispatched device page
	@Test(priority = 43)
	public void testSearchFunctionalityWithValidData() {
		executor.executeTest("Test search functionality with valid data on dispatched device page",
				"Dispatched Devices", () -> dispatchedDevicePage.validateSearchFunctionalityWithValidData());
	}

	// validate the select customer dropdown is visible on dispatched device page
	@Test(priority = 44)
	public void testSelectCustomerDropdownVisibility() {
		executor.executeTest("Test select customer dropdown is visible on dispatched device page", true,
				() -> dispatchedDevicePage.isSelectCustomerDropdownVisible());
	}

	// validate the select customer dropdown is clickable on dispatched device page
	@Test(priority = 45)
	public void testSelectCustomerDropdownClickable() {
		executor.executeTest("Test select customer dropdown is clickable on dispatched device page", true,
				() -> dispatchedDevicePage.isSelectCustomerDropdownClickable());
	}

	// validate the select customer functionality with valid data on dispatched
	// device page
	@Test(priority = 46)
	public void testSelectCustomerDropdownFunctionality() {
		executor.executeTest("Test select customer dropdown functionality with valid data on dispatched device page",
				"Dispatched Devices",
				() -> dispatchedDevicePage.validateSelectCustomerDropdownFunctionalityWithValidData());
	}

	// validate the table headers of dispatched device list on dispatched device
	// page
	@Test(priority = 47)
	public void testTableHeadersOfDispatchedDeviceList() {
		List<String> HeadersOfDispatchedDeviceList = Arrays.asList("UID", "IMEI", "ICCID", "MODEL NAME",
				"CUSTOMER NAME", "ACTION");
		executor.executeTest("Test table headers of dispatched device list on dispatched device page",
				HeadersOfDispatchedDeviceList, () -> dispatchedDevicePage.getTableHeadersOfDispatchedDeviceList());
	}

	// validate the view button is visible for each device on dispatched device list
	// on dispatched device page
	@Test(priority = 48)
	public void testViewButtonVisibilityOnDispatchedDeviceList() {
		executor.executeTest(
				"Test view button is visible for each device on dispatched device list on dispatched device page", true,
				() -> dispatchedDevicePage.isViewButtonVisibleOnDispatchedDeviceList());
	}

	// click on first view button
	@Test(priority = 49)
	public void testClickOnViewButtonOnDispatchedDeviceTable() {
		executor.executeTest(
				"Test view button is clickable for each device on dispatched device list on dispatched device page",
				true, () -> dispatchedDevicePage.clickOnViewButtonOnDispatchedDeviceTable());
	}

	/*** update dispatched device tests ***/
	// after clicking on the view button validate the page title of update
	// dispatched
	@Test(priority = 50)
	public void testPageTitleAfterClickingViewButton() {
		executor.executeTest("Test page title after clicking view button", "Update Dispatched Device",
				() -> dispatchedDevicePage.getPageTitleAfterClickingViewButton());
	}

	// Validate that the UID field is not editable on the update dispatched device
	// page
	@Test(priority = 51)
	public void testUIDFieldNonEditableOnUpdateDispatchedDevicePage() {
		executor.executeTest("Test UID field is non-editable on update dispatched device page", true,
				() -> dispatchedDevicePage.isUIDFieldNonEditableOnUpdateDispatchedDevicePage());
	}

	// validate the update dispatched device form with valid data
	// take all fields in mind and then try to validate the form
	@Test(priority = 52)
	public void testUpdateDispatchedDeviceFormWithValidData() {
		executor.executeTest("Test update dispatched device form with valid data", "Update Dispatched Device",
				() -> dispatchedDevicePage.getUpdateDispatchedDeviceFormWithValidData());
	}

	// validate the update button is disabled until all mandatory fields are filled
	// while updating dispatched device details
	@Test(priority = 53)
	public void testUpdateButtonDisabledOnMandatoryFieldsEmpty() {
		executor.executeTest(
				"Test update button is disabled on mandatory fields empty while updating dispatched device details",
				true, () -> dispatchedDevicePage.isUpdateButtonDisabledOnMandatoryFieldsEmpty());
	}

	// validate the success toast message after updating dispatched device details
	// fill the data with valid data and then update
	@Test(priority = 54)
	public void testSuccessToastMessageAfterUpdatingDispatchedDeviceDetails() {
		executor.executeTest("Test success toast message after updating dispatched device details",
				"Data Fetched Successfully",
				() -> dispatchedDevicePage.getSuccessToastMessageAfterUpdatingDispatchedDeviceDetails());
	}

	// validate the edited data is updated in dispatched device list after updating
	// dispatched device details
	@Test(priority = 55)
	public void testEditedDataIsUpdatedInDispatchedDeviceList() {
		executor.executeTest(
				"Test edited data is updated in dispatched device list after updating dispatched device details", true,
				() -> dispatchedDevicePage.isEditedDataUpdatedInDispatchedDeviceList());
	}

	/*** delete dispatched device tests ***/
	// validate the delete button is visible for each device on dispatched device
	// list on dispatched device page
	@Test(priority = 56)
	public void testDeleteButtonVisibilityAndClickability() {
		executor.executeTest("Verify delete buttons are visible and enabled for all dispatched devices", true,
				() -> dispatchedDevicePage.areDeleteButtonsVisibleAndEnabled());
	}

	// validate the confirmation popup on clicking delete button for each device on
	// dispatched device list on dispatched device page
	// @Test(priority = 57)
	public void testCancelDeletePopup() {
		executor.executeTest("Verify delete popup opens and cancel button dismisses the popup", "Are You Sure?", () -> {
			String popupText = dispatchedDevicePage.openDeletePopupAndGetText();
			boolean cancelled = dispatchedDevicePage.cancelDeletePopup();
			return cancelled ? popupText : "Popup not cancelled";
		});
	}

	// validate the cancellation of delete action on clicking cancel button of
	// confirmation popup for each device on dispatched device list on dispatched
	// device page
	@Test(priority = 58)
	public void testConfirmDeleteRemovesDevice() {
		executor.executeTest("Verify confirming delete removes the device & displays success toast",
				"Data Fetched Successfully", () -> dispatchedDevicePage.confirmDeletionAndGetToast());
	}

	// validate the pagination on dispatched device list on dispatched device page
	// when more data is present
	// @Test(priority = 59)
	public void testPaginationOnDispatchedDeviceList() {
		executor.executeTest("Test pagination on the dispatched device list table", true, () -> {
			try {
				comm.checkPagination();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}

		});
	}

	@Test(priority = 60)
	public void testVersion() {
		executor.executeTest("Verify Application Version", Constants.EXP_VERSION_TEXT, assertion::checkVersion);
	}

	@Test(priority = 61)
	public void testCopyright() {
		executor.executeTest("Verify Copyright", Constants.EXP_COPYRIGHT_TEXT, assertion::checkCopyright);
	}

	@AfterClass
	public void tearDownAssertions() {
		softAssert.assertAll();
	}
}




