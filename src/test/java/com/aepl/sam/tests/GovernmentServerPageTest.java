package com.aepl.sam.tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aepl.sam.base.TestBase;
import com.aepl.sam.utils.Constants;
import com.aepl.sam.utils.PageActionsUtil;
import com.aepl.sam.utils.PageAssertionsUtil;
import com.aepl.sam.pages.GovernmentServerPage;
import com.aepl.sam.utils.ExcelUtility;

public class GovernmentServerPageTest extends TestBase {
	private static final String SHEET_NAME = "Government_Server_Test";

	private GovernmentServerPage govServerPage;
	private ExcelUtility excelUtility;
	private PageActionsUtil comm;
	private PageAssertionsUtil assertion;
	private SoftAssert softAssert;
	private Executor executor;

	@Override
	@BeforeClass
	public void setUp() {
		super.setUp();
		this.govServerPage = new GovernmentServerPage(driver, wait, action);
		this.comm = new PageActionsUtil(driver, wait);
		this.assertion = new PageAssertionsUtil(driver, wait);
		this.excelUtility = new ExcelUtility();
		this.softAssert = new SoftAssert();
		this.executor = new Executor(excelUtility, softAssert);
		excelUtility.initializeExcel(SHEET_NAME);
		logger.info("Setup completed for GovernmentServerPageTest");
	}

	// Validate the company logo on the left of the page. if it is displayed or not.
	@Test(priority = 1)
	public void testCompanyLogo() {
		executor.executeTest("Verify Company Logo on Webpage", true, () -> assertion.verifyWebpageLogo());
	}

	// Validate the navigation bar
	@Test(priority = 2)
	public void testClickNavBar() {
		executor.executeTest("Verify Navigation Bar Click Functionality", true, govServerPage::navBarLink);
	}

	// Validate the page title of the government server page
	@Test(priority = 3)
	public void testPageTitle() {
		executor.executeTest("Verify Page Title on Webpage", "Government Server", govServerPage::verifyPageTitle);
	}

	// Validate the component title of the government server page
	@Test(priority = 4)
	public void testComponentTitle() {
		executor.executeTest("Test Page Component Title", "Government Servers List", comm::validateComponentTitle);
	}

	// Validate the all buttons on the screen that is appeared.
	@Test(priority = 5)
	public void testButtons() {
		executor.executeTest("Test all button on page {Government Server}", Constants.EXP_VALIDATE_BUTTONS_TEXT, assertion::validateButtons);
	}

	// Validate the all components on the screen.
	@Test(priority = 6)
	public void testComponents() {
		executor.executeTest("Test All Components on the page {Government Server}", Constants.EXP_VALIDATE_COMPONENTS_TEXT, assertion::validateComponents);
	}

	// Search box is enabled
	@Test(priority = 7)
	public void testSearchBoxIsEnabled() {
		executor.executeTest("Test the search box is enabled", true, govServerPage::isSearchBoxEnabled);
	}

	// Search box is visible
	@Test(priority = 8)
	public void testSearchBoxIsVisible() {
		executor.executeTest("Test the search box is visible", true, govServerPage::isSeachBoxDisplayed);
	}

	// Search button is enabled
	@Test(priority = 9)
	public void testSearchButtonIsEnabled() {
		executor.executeTest("Test the search button is enabled", true, govServerPage::isSeachButtonEnabled);
	}

	// Search button is visible
	@Test(priority = 10)
	public void testSearchButtonIsVisible() {
		executor.executeTest("Test the search button is enabled", true, govServerPage::isSeachButtonDisplayed);
	}

	// Search a gov server
	@Test(priority = 11)
	public void testSearchGovernmentServer() {
		executor.executeTest("Test a government server form the list", "Data Fetched Successfully",
				govServerPage::searchGovernmentServer);
	}

	// Check table headers
	@Test(priority = 12)
	public void testTableHeadersOfGovernmetServerListTable() {
		List<String> expectedHeaders = Arrays.asList("STATE NAME", "STATE CODE", "STATE ENABLE OTA COMMAND",
				"STATE PRIMARY IP:PORT", "STATE SECONDARY IP:PORT", "ACTION");
		executor.executeTest("Test the table headers of government server list page", expectedHeaders,
				govServerPage::validaterGovernmentServerListTableHeaders);
	}

	// Check table data
	/**
	 * this is failing, but dont know why it is happening in future should do update
	 * -- update follow
	 *
	 *
	 * if instead of just action checking other fields then the data matched ÃƒÂ°Ã…Â¸Ã‹Å“Ã¢â‚¬â„¢.
	 * don't know why ÃƒÂ°Ã…Â¸Ã‹Å“Ã¢â‚¬Å¡
	 **/
	@Test(priority = 13)
	public void testTableDataOfGovernmetServerListTable() {
		Map<String, String> expectedTableData = new LinkedHashMap<>();
		expectedTableData.put("STATE NAME", "SURAJ");
		expectedTableData.put("STATE CODE", "SA");
		expectedTableData.put("STATE ENABLE OTA COMMAND", "TRUE");
		expectedTableData.put("STATE PRIMARY IP:PORT", "primary:9730");
		expectedTableData.put("STATE SECONDARY IP:PORT", "secondary:7176");
		expectedTableData.put("ACTION", "visibility\ndelete");

		List<Map<String, String>> expectedTableDataOfGov = Collections.singletonList(expectedTableData);
		executor.executeTest("Test table data of the government server table list", expectedTableDataOfGov,
				govServerPage::validateTableDataOfGovernmentServerTable);
	}

	// Check eye buttons are visible enabled for all table data
	@Test(priority = 14)
	public void testEyeButtonsOnTheTable() {
		executor.executeTest("Test the eye buttons on the table", true, govServerPage::isEyeButtonsAreVisibleOnTable);
	}

	// Check delete buttons are visible enabele for all table data
	@Test(priority = 15)
	public void testDeleteButtonsOnTheTable() {
		executor.executeTest("Test the eye buttons on the table", true,
				govServerPage::isDeleteButtonsAreVisibleOnTable);
	}

	// Check pagination on the gov server's list page
	@Test(priority = 16)
	public void testPaginationOnTheGovernmentSeverListTable() {
		executor.executeTest("Test pagination on the government server page list table", true, () -> {
			try {
				comm.checkPagination();
				return true;
			} catch (Exception e) {
				logger.error("Pagination test failed: {}", e.getMessage(), e);
				return false;
			}
		});
	}

	// Validate the add gov ser button is visible
	@Test(priority = 17)
	public void testAddGovServerButtonIsVisible() {
		executor.executeTest("Test the add gov ser button", true, govServerPage::isAddGovernmentServerButtonIsVisible);
	}

	// Validate the add gov ser button is enabled
	@Test(priority = 18)
	public void testAddGovServerButtonIsEnabled() {
		executor.executeTest("Test the add gov ser button", true, govServerPage::isAddGovernmentServerButtonIsEnabled);
	}

	// Click on add government server button. Check for page title to verify the -
	// add button is clicked correctly - it
	// must be - {Add Government Servers}
	@Test(priority = 19)
	public void testClickOnAddGovServerButton() {
		executor.executeTest("Test the add gov server button ", "Add Government Servers",
				govServerPage::validateClickOnAddGovServerButton);
	}

	@Test(priority = 20)
	public void testAddGovernmentServer() {
		executor.executeTest("Verify Add Government Server Functionality", "Government Servers Details", () -> {
			String result = govServerPage.addGovernmentServer();
			return (result != null && !result.isEmpty()) ? "Government Servers Details" : "Government Server Addition Failed";
		});
	}

	// Check all input fields of the form in the add government server page are
	// enabled and clickable or displayed
	@Test(priority = 21)
	public void testCheckInputFieldsAreEnabled() {
		executor.executeTest("Test the input fields are enabled", true,
				govServerPage::validateGovernmentServerDetailsInputs);
	}

	// Check errors for state the input box - blank
	@Test(priority = 22)
	public void testCheckErrorValidationInStateInputBoxBlank() {
		executor.executeTest("Test the state input box error validation for empty box",
				"This field is required and can't be only spaces.",
				() -> govServerPage.validateSingleInputBox("state", " "));
	}

	// Check errors for state input box - characters
	@Test(priority = 23)
	public void testCheckErrorValidationInStateInputBoxChars() {
		executor.executeTest("Test the state input box error validation for charaters",
				"Only alphabets and spaces are allowed.", () -> govServerPage.validateSingleInputBox("state", "@#$"));
	}

	// Check errors for state input box - numbers
	@Test(priority = 24)
	public void testCheckErrorValidationInStateInputBoxNums() {
		executor.executeTest("Test the state input box error validation for numbers",
				"Only alphabets and spaces are allowed.", () -> govServerPage.validateSingleInputBox("state", "12314"));
	}

	// Check errors for state input box - leading space
	@Test(priority = 25)
	public void testCheckErrorValidationInStateInputBoxLeadingSpaces() {
		executor.executeTest("Test the state input box error validation for leading spaces",
				"Remove leading or trailing spaces.", () -> govServerPage.validateSingleInputBox("state", " demo"));
	}

	// Check errors for state abbrevation the input box - blank
	@Test(priority = 26)
	public void testCheckErrorValidationInStateAbrInputBoxBlank() {
		executor.executeTest("Test the state abr input box error validation for empty box",
				"This field is required and can't be only spaces.",
				() -> govServerPage.validateSingleInputBox("stateCode", " "));
	}

	// Check errors for state abbrevation input box - characters
	@Test(priority = 27)
	public void testCheckErrorValidationInStateAbrInputBoxChars() {
		executor.executeTest("Test the state abr input box error validation for charaters",
				"Only alphabets and spaces are allowed.",
				() -> govServerPage.validateSingleInputBox("stateCode", "@#$"));
	}

	// Check errors for state abbrevation input box - numbers
	@Test(priority = 28)
	public void testCheckErrorValidationInStateAbrInputBoxNums() {
		executor.executeTest("Test the state abr input box error validation for numbers",
				"Only alphabets and spaces are allowed.",
				() -> govServerPage.validateSingleInputBox("stateCode", "12314"));
	}

	// Check errors for state abbrevation input box - leading space
	@Test(priority = 29)
	public void testCheckErrorValidationInStateAbrInputBoxLeadingSpaces() {
		executor.executeTest("Test the state abr input box error validation for leading spaces",
				"Remove leading or trailing spaces.", () -> govServerPage.validateSingleInputBox("stateCode", " demo"));
	}

	// Check errors for gov ip 1 input box - long input
	@Test(priority = 30)
	public void testCheckErrorValidationInGovIp1InputBoxLeadingSpaces() {
		executor.executeTest("Test the gov ip 1 input box error validation for leading spaces",
				"Maximum 20 characters allowed.",
				() -> govServerPage.validateSingleInputBox("govtIp1", "2345675785678657856342"));
	}

	// Check errors for the port input box - character/ strings
	@Test(priority = 31)
	public void testCheckErrorValidationInPort1InputBoxCharacter() {
		executor.executeTest("Test the port input box error validation for characters",
				"Enter a valid port number (1-65535).", () -> govServerPage.validateSingleInputBox("port1", "wrong"));
	}

	// Check errors for the port input box - long port number (exceeds the range
	// number)
	@Test(priority = 32)
	public void testCheckErrorValidationInPort1InputBoxLongPort() {
		executor.executeTest("Test the port input box error validation for long port number",
				"Enter a valid port number (1-65535).", () -> govServerPage.validateSingleInputBox("port1", "9876732"));
	}

	// Check errors for gov ip 2 input box - long input
	@Test(priority = 33)
	public void testCheckErrorValidationInGovIp2InputBoxLeadingSpaces() {
		executor.executeTest("Test the gov ip 2 input box error validation for leading spaces",
				"Maximum 20 characters allowed.",
				() -> govServerPage.validateSingleInputBox("govtIp2", "2345675785678657856342"));
	}

	// Check errors for the port input box - character/ strings
	@Test(priority = 34)
	public void testCheckErrorValidationInPort2InputBoxCharacter() {
		executor.executeTest("Test the port input box error validation for characters",
				"Enter a valid port number (1-65535).", () -> govServerPage.validateSingleInputBox("port2", "wrong"));
	}

	// Check errors for the port input box - long port number (exceeds the range
	// number)
	@Test(priority = 35)
	public void testCheckErrorValidationInPort2InputBoxLongPort() {
		executor.executeTest("Test the port input box error validation for long port number",
				"Enter a valid port number (1-65535).", () -> govServerPage.validateSingleInputBox("port2", "9876732"));
	}

	// Check errors for the state enabled ota input box - Special characters
	@Test(priority = 36)
	public void testCheckErrorValidationInStateEnabledOtaInputBoxSpecialCharaters() {
		executor.executeTest("Test the port input box error validation for special character",
				"Please enter a valid State Enable.",
				() -> govServerPage.validateSingleInputBox("stateEnable", "@@#!$"));
	}

	// Test the submit button is disabled or not if no data is inputs in the fields
	// of state and state abr
	@Test(priority = 37)
	public void testSubmitButtonIsDisabledOnNoDataInputs() {
		executor.executeTest("Tes the submit button is disabled if no data is inputed", true,
				govServerPage::isSubmitButtonDisabledWhenRequiredFieldsEmpty);
	}

	// fill the already present state and state abr and validate the toast message
	// if data is already present.
	@Test(priority = 38)
	public void testDuplicateStateNameOrCodeValidation() {
		executor.executeTest("Test that submitting an already existing state name or code shows duplicate warning",
				"State Name or Code already present in the system",
				() -> govServerPage.validateDuplicateStateEntry("SURAJ", "SA"));
	}

	// fill the correct data - state and atate abr only and press submit button -
	// for new state
	@Test(priority = 39)
	public void testFillForm() {
		executor.executeTest("Verify Government Server Form Filling", "Data Fetched Successfully", () -> govServerPage.manageGovServer("add"));
	}

	// Verify the given details are added is correctly displyed on the table
	@Test(priority = 40)
	public void testDataInputedIsReflectOnTheTable() {
		executor.executeTest("Test the inputed data is reflected on table or not", true,
				govServerPage::validateTableDataThatIsInputed);
	}

	// Click view button in front of the details entered
	@Test(priority = 41)
	public void testClickOnViewButton() {
		executor.executeTest("Test click on view button", "View/Update Government Servers",
				govServerPage::clickOnViewButton);
	}

	// Check all the details OR just state name get from the table displayed in the
	// input boxes or not
	@Test(priority = 42)
	public void testTableDataWithInputFields() {
		executor.executeTest("Test the table data with the input to update", true,
				govServerPage::validateInputsWithTableData);
	}

	// Check if no changes in the input boxes then the update button should not be
	// visible
	@Test(priority = 43)
	public void testUpdateButtonShouldBeDisabledIfNoInput() {
		executor.executeTest("Test the update button is disable if no data is inputed or changed", true,
				govServerPage::validateUpdateButtonNotVisibleWhenNoChanges);
	}

	// fill any data into the input box and then try to update the record
	@Test(priority = 44)
	public void testUpdateGovServer() {
		executor.executeTest("Verify Update Government Server Functionality", "Data Saved successfully!!", () -> govServerPage.manageGovServer("update"));
	}

	// scroll down to verify the page title as {Device Firmware List}
	@Test(priority = 45)
	public void testDeviceFirmwareListComponentIsVisible() {
		executor.executeTest("Test the device fimware list component is visible",
				Constants.EXP_VALIDATE_COMPONENTS_TEXT, () -> assertion.validateComponents());
	}

	@Test(priority = 46)
	public void testDeviceFirmwareListComponentTitle() {
		executor.executeTest("Test the device fimware list component title ", "Device Firmware List",
				() -> govServerPage.validateComponentTitle());
	}

	// Verify the buttons
	@Test(priority = 47)
	public void testButtonsValidations() {
		executor.executeTest("Test the device fimware list component title ",
				Constants.EXP_VALIDATE_BUTTONS_TEXT, () -> assertion.validateButtons());
	}

	// Search box is enabled
	@Test(priority = 48)
	public void testSearchBoxIsEnabledOnFirmwareTable() {
		executor.executeTest("Test the search box is enabled On Firmware Table", true,
				govServerPage::isSearchBoxEnabled);
	}

	// Search box is visible
	@Test(priority = 49)
	public void testSearchBoxIsVisibleOnFirmwareTable() {
		executor.executeTest("Test the search box is visible On Firmware Table", true,
				govServerPage::isSeachBoxDisplayed);
	}

	// Search button is enabled
	@Test(priority = 50)
	public void testSearchButtonIsEnabledOnFirmwareTable() {
		executor.executeTest("Test the search button is enabled On Firmware Table", true,
				govServerPage::isSeachButtonEnabled);
	}

	// Search button is visible
	@Test(priority = 51)
	public void testSearchButtonIsVisibleOnFirmwareTable() {
		executor.executeTest("Test the search button is enabled On Firmware Table", true,
				govServerPage::isSeachButtonDisplayed);
	}

	// Check table headers
	@Test(priority = 52)
	public void testTableHeadersOfFirmwareTable() {
		// Firmware Type Firmware Version Upload File / File Name Description Release
		// Date Action
		List<String> expectedHeaders = Arrays.asList("FIRMWARE TYPE", "FIRMWARE VERSION", "UPLOAD FILE / FILE NAME",
				"DESCRIPTION", "RELEASE DATE", "ACTION");
		executor.executeTest("Test the table headers of the fimware table", expectedHeaders,
				govServerPage::verifyTableHeadersOfFirmwareTable);
	}

	// validate add firmware device button is visible
	@Test(priority = 53)
	public void testAddFirmwareDeviceButtonIsVisible() {
		executor.executeTest("Test the add firmware device button is visible", true,
				govServerPage::isAddFirmwareDeviceButtonVisible);
	}

	// Validate add firmware device button is enable
	@Test(priority = 54)
	public void testAddFirmwareDeviceButtonIsEnable() {
		executor.executeTest("Test the add firmware device button is enable", true,
				govServerPage::isAddFirmwareDeviceButtonEnabled);
	}

	// Validate the component title - {Firmware Master List} on clicking the add
	// firmware device
	@Test(priority = 55)
	public void testClickAndValidateAddFirmwareDeviceButton() {
		// Select Firmware Type Firmware Version Upload File / File Name Description
		// Release Date Created By
		List<String> tableHeaders = Arrays.asList("SELECT", "FIRMWARE TYPE", "FIRMWARE VERSION",
				"UPLOAD FILE / FILE NAME", "DESCRIPTION", "RELEASE DATE", "CREATED BY");
		executor.executeTest("Test the add firmware device button ", tableHeaders,
				govServerPage::validateClickAddFirmwareDevice);
	}

	// Search box is enabled
	@Test(priority = 56)
	public void testSearchBoxIsEnabledOnFirmwareMasterListTable() {
		executor.executeTest("Test the search box is enabled on firmware master list table", true,
				govServerPage::isSearchBoxEnabled);
	}

	// Search box is visible
	@Test(priority = 57)
	public void testSearchBoxIsVisibleOnFirmwareMasterListTable() {
		executor.executeTest("Test the search box is visible on firmware master list table", true,
				govServerPage::isSeachBoxDisplayed);
	}

	// Search button is enabled
	@Test(priority = 58)
	public void testSearchButtonIsEnabledOnFirmwareMasterListTable() {
		executor.executeTest("Test the search button is enabled on firmware master list table", true,
				govServerPage::isSeachButtonEnabled);
	}

	// Search button is visible
	@Test(priority = 59)
	public void testSearchButtonIsVisibleOnFirmwareMasterListTable() {
		executor.executeTest("Test the search button is enabled on firmware master list table", true,
				govServerPage::isSeachButtonDisplayed);
	}

	// Search bin in search box
	@Test(priority = 60)
	public void testSearchBinFile() {
		executor.executeTest("Test bin file search on firmware device list", true, govServerPage::searchBinFile);
	}

	// Check table headers on firmware master list table
	@Test(priority = 61)
	public void testTableHeadersOnFirmwareMasterListTable() {
		List<String> expectedHeaders = Arrays.asList("SELECT", "FIRMWARE TYPE", "FIRMWARE VERSION",
				"UPLOAD FILE / FILE NAME", "DESCRIPTION", "RELEASE DATE", "CREATED BY");
		executor.executeTest("Test the table headers of the firmware master table", expectedHeaders,
				govServerPage::validateTableHeadersOnFirmwareMaster);
	}

	// Check table data on firmware master list table
	@Test(priority = 62)
	public void testTableDataOnFirmwareMasterListTable() {
		List<Map<String, String>> expectedData = new ArrayList<>();

		Map<String, String> expectedRow = new LinkedHashMap<>();
		expectedRow.put("SELECT", "Unchecked"); // optional ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â only if checkbox exists
		expectedRow.put("FIRMWARE TYPE", "Device");
		expectedRow.put("FIRMWARE VERSION", "TCP01.bin");
		expectedRow.put("UPLOAD FILE / FILE NAME", "Default_TCP01.bin");
		expectedRow.put("DESCRIPTION", "demo file");
		expectedRow.put("RELEASE DATE", "2002-08-27");
		expectedRow.put("CREATED BY", "Suraj Bhalerao");
		expectedData.add(expectedRow);

		executor.executeTest("Validate all table data on Firmware Master List table", expectedData,
				govServerPage::validateTableDataOnFirmwareMasterListTable);
	}

	@Test(priority = 63)
	public void testCheckboxIsValidAndSelectIt() {
		executor.executeTest("Test the select checkbox from the list", true, govServerPage::validateSelectCheckbox);
	}

	// check submit button is displayed and enabled
	@Test(priority = 64)
	public void testSubmitButtonOnFirmwareMasterIsVisible() {
		executor.executeTest("Test the submit button is visible", true, govServerPage::isSubmitButtonVisibleAndEnabled);
	}

	// click on submit button and validate the toast message
	@Test(priority = 65)
	public void testClickOnSubmitButton() {
		executor.executeTest("Test the click on submit button", "Data Fetched Successfully",
				govServerPage::validateClickOnSubmitButton);
	}

	// See for the delete button is visible and clickable in the table data of the
	// table.
	@Test(priority = 66)
	public void testDeleteButtonIsVisibleOnFirmwareMasterTable() {
		executor.executeTest("Test the delete button is visible on the table", true,
				govServerPage::isDeleteButtonVisibleOnTable);
	}

	// After clicking the delete button the data should be gone and the "No data
	// found" img should be appeared on the screen
	@Test(priority = 67)
	public void testClickOnDeleteButtonAndValidate() {
		executor.executeTest("Test the delete button", true, govServerPage::clickOnDeleteAndValidate);
	}

	// Come to the gov server list page and click on the firmware master button
	@Test(priority = 68)
	public void testFirmwareMasterButtonIsVisible() {
		executor.executeTest("Test firmware master button is visible", true,
				govServerPage::isFirmwareMasterButtonVisible);
	}

	@Test(priority = 69)
	public void testFirmwareMasterButtonIsEnabled() {
		executor.executeTest("Test firmware master button is enabled", true,
				govServerPage::isFirmwareMasterButtonEnabled);
	}

	@Test(priority = 70)
	public void testClickFirmwareMasterButtonAndValidate() {
		executor.executeTest("Test click on firmware master button and validate", "Firmware Master",
				govServerPage::clickAndValidateFirmwareMasterButton);
	}

	// Verify the buttons
	@Test(priority = 71)
	public void testAllButtonsOnTheFirmwareMasterPage() {
		executor.executeTest("Test all buttons on the firmware master page", Constants.EXP_VALIDATE_BUTTONS_TEXT,
				assertion::validateButtons);
	}

	// Verify the components
	@Test(priority = 72)
	public void testAllComponentOnTheFirmwareMasterPage() {
		executor.executeTest("Test all components on the firmware master page", Constants.EXP_VALIDATE_COMPONENTS_TEXT,
				assertion::validateComponents);
	}

	// Test component title
	@Test(priority = 73)
	public void testComponetTitleOnFirmwareMasterPage() {
		executor.executeTest("Test component title on firmware master page", "Firmware List",
				() -> comm.validateComponentTitle());
	}

	// Validate the add fimware button is visible
	@Test(priority = 74)
	public void testAddFirmwareButtonIsVisible() {
		executor.executeTest("Test add firmware button is visible", true, govServerPage::isAddFirmwareButtonIsVisible);
	}

	// Validate the add firmware button is enable
	@Test(priority = 74, dependsOnMethods = "testAddFirmwareButtonIsVisible")
	public void testAddFirmwareButtonIsEnabled() {
		executor.executeTest("Test add firmware button is enable", true, govServerPage::isAddFirmwareButtonIsEnabled);
	}

	// Click on the add firmware button
	@Test(priority = 75)
	public void testClickAddFirmwareButtonAndValidate() {
		executor.executeTest("Test clicking on add firmware button in firmware master", "Add Firmware",
				govServerPage::clickAndValidateAddFimwareMasterButton);
	}

	// Validate the component tiltle
	@Test(priority = 76)
	public void testComponentTitleOnAddFirmwareInFirmwareMaster() {
		executor.executeTest("Test component title of add firmware in firmware master", "Firmware Details",
				govServerPage::validateComponentTitle1);
	}

	// Validate firmware version for empty box
	// not being tested for the empty box
	// @Test(priority = 77)
	// public void testErrorMsgForFirmwareVersionOnEmptyInput() {
	// executor.executeTest("Test the error msg of firmware version on empty input",
	// "This field is required and can't be empty.",
	// () -> govServerPage.validateSingleInputBox("firmwareName", ""));
	// }

	// Validate firmware version for space in input
	@Test(priority = 78)
	public void testErrorMsgForFirmwareVersionOnSpaceInInput() {
		executor.executeTest("Test the error msg of firmware version on empty input",
				"This field is required and can't be only spaces.",
				() -> govServerPage.validateSingleInputBox("firmwareName", " "));
	}

	// Validate firmware version for the leading spaces
	@Test(priority = 79)
	public void testErrorMsgForFirmwareVersionOnLeadingSpaces() {
		executor.executeTest("Test the error msg of firmware version on leading spaces",
				"Remove leading or trailing spaces.",
				() -> govServerPage.validateSingleInputBox("firmwareName", " leading"));
	}

	// Validate firmware version for the trailing spaces
	@Test(priority = 80)
	public void testErrorMsgForFirmwareVersionOnTrailinSpaces() {
		executor.executeTest("Test the error msg of firmware version on trailing spaces",
				"Remove leading or trailing spaces.",
				() -> govServerPage.validateSingleInputBox("firmwareName", "trailing "));
	}

	// Validate firmware version for the input lenght between 1-50
	@Test(priority = 81)
	public void testErrorMsgForFirmwareVersionOnInputLengthOf50() {
		executor.executeTest("Test the error msg of firmware version on input lenght",
				"Length must be between 1 and 50 characters.",
				() -> govServerPage.validateSingleInputBox("firmwareName", "a".repeat(51)));
	}

	// Validate firmware description for empty box
	// not tested for the empty input box
	// @Test(priority = 82)
	// public void testErrorMsgForFirmwareDescriptionOnEmptyInput() {
	// executor.executeTest("Test the error msg of firmware description on empty
	// input",
	// "This field is required and can't be empty.",
	// () -> govServerPage.validateSingleInputBox("description", ""));
	// }

	// Validate firmware description for only space inputed
	@Test(priority = 83)
	public void testErrorMsgForFirmwareDescriptionOnSpaceInput() {
		executor.executeTest("Test the error msg of firmware description on Space input",
				"This field is required and can't be only spaces.",
				() -> govServerPage.validateSingleInputBox("description", " "));
	}

	// Validate firmware version for the leading spaces
	@Test(priority = 84)
	public void testErrorMsgForFirmwareDescriptionOnLeadingSpaces() {
		executor.executeTest("Test the error msg of firmware description on leading spaces",
				"Remove leading or trailing spaces.",
				() -> govServerPage.validateSingleInputBox("description", " leading"));
	}

	// Validate firmware version for the trailing spaces
	@Test(priority = 85)
	public void testErrorMsgForFirmwareDescriptionOnTrailinSpaces() {
		executor.executeTest("Test the error msg of firmware description on trailing spaces",
				"Remove leading or trailing spaces.",
				() -> govServerPage.validateSingleInputBox("description", "trailing "));
	}

	// Validate firmware version for the input lenght between 1-50
	@Test(priority = 86)
	public void testErrorMsgForFirmwareDescriptionOnInputLengthOf50() {
		executor.executeTest("Test the error msg of firmware description on input length",
				"Length must be between 1 and 50 characters.",
				() -> govServerPage.validateSingleInputBox("description", "a".repeat(51)));
	}

	// Validate that the upload file input box have a validation of - this field is
	// mandatory
	// @Test(priority = 87)
	public void testErrorMsgForUploadFileSection() {
		executor.executeTest("Test the error msg of upload file input box", "This field is mandatory.",
				() -> govServerPage.validateSingleInputBox("mat-input-3", " "));
	}

	// validate that the upload file input box only accepts .bin files
	// NOTE : the input box is only click and then window open to select the file
	// path
	@Test(priority = 88)
	public void testErrorMsgForUploadFileSectionOnvalidFile() {
		executor.executeTest("Test the error msg of upload file input box on valid file", true,
				() -> govServerPage.validateUploadFileInputBoxWithvalidFile());
	}

	// Validate the file upload button is visible
	@Test(priority = 89)
	public void testFileUploadButtonIsVisible() {
		executor.executeTest("Test the file upload button is visible", true, govServerPage::isFileUploadButtonVisible);
	}

	// Validate the file upload button is clickable
	@Test(priority = 90)
	public void testFileUploadButtonIsClickable() {
		executor.executeTest("Test the file upload button is clickable", true,
				govServerPage::isFileUploadButtonClickable);
	}

	// Validate the Release date field should be already selected the currunt date
	@Test(priority = 91)
	public void testReleaseDateFieldHasCurrentDateSelected() {
		executor.executeTest("Test the release date field has current date selected", true,
				govServerPage::isReleaseDateFieldHasCurrentDateSelected);
	}

	// validate that the select date button should be enabled and clickable
	@Test(priority = 92)
	public void testSelectDateButtonIsEnabledAndClickable() {
		executor.executeTest("Test the select date button is enabled and clickable", true,
				govServerPage::isSelectDateButtonEnabledAndClickable);
	}

	// validate that the click on select date button should open the box to select
	// the date
	// @Test(priority = 93)
	public void testClickOnSelectDateButtonOpensDatePicker() {
		executor.executeTest("Test the click on select date button opens date picker", true,
				govServerPage::doesClickOnSelectDateButtonOpensDatePicker);
	}

	// validate that the future date should not be selected
	// @Test(priority = 94)
	public void testFutureDateShouldNotBeSelectable() {
		executor.executeTest("Test the future date should not be selectable", true,
				govServerPage::isFutureDateNotSelectable);
	}

	// validate that the past date should be selected
	@Test(priority = 95)
	public void testPastDateShouldBeSelectable() {
		executor.executeTest("Test the past date should be selectable", true, govServerPage::isPastDateSelectable);
	}

	// Valiadate the submit button should disabled if no data is inputted in the all
	// fields
	@Test(priority = 96)
	public void testSubmitButtonIsDisabledIfNoDataInputted() {
		executor.executeTest("Test the submit button is disabled if no data is inputted", true,
				govServerPage::isSubmitButtonDisabledWhenFieldsEmpty);
	}

	// Validate if file is already present then toast message apppears on clicking
	// on submit. if no then successfully submit data
	// filling the data
	@Test(priority = 97)
	public void testAddFirmware() {
		executor.executeTest("Test the form data filing accurately", "Data Fetched Successfully",
				govServerPage::fillFirmwareDetailsForm);
	}

	// VAlidate the latest firmware added is added at the last of the page
	@Test(priority = 98)
	public void testLatestAddedFirmwareIsAtLast() {
		executor.executeTest("Test the latest added firmware is at last", true,
				govServerPage::isLatestAddedFirmwareIsAtLast);
	}

	// View and delete buttons should enabled
	@Test(priority = 99)
	public void testViewAndDeleteButtonsAreEnabled() {
		executor.executeTest("Test the view and delete buttons are enabled", true,
				govServerPage::areViewAndDeleteButtonsEnabled);
	}

	// VAlidate after clicking on the delete button the added firmware should be
	// delete and the Toast message should appears on the screen
	@Test(priority = 100)
	public void testDeleteFirmwareAndValidate() {
		executor.executeTest("Test the delete firmware and validate", "Data Fetched Successfully",
				govServerPage::deleteFirmwareAndValidate);
	}

	// Come back to government server page and check paginations
	@Test(priority = 101)
	public void testPaginationOnGovernmentServerPage() {
		executor.executeTest("Test pagination on the government server page list table", true,
				govServerPage::checkPagination);
	}

	// Validate the Version
	@Test(priority = 102)
	public void testVersion() {
		executor.executeTest("Verify Version Functionality", Constants.EXP_VERSION_TEXT, assertion::checkVersion);
	}

	// Validate the copyright
	@Test(priority = 103)
	public void testCopyright() {
		executor.executeTest("Verify Copyright Functionality", Constants.EXP_COPYRIGHT_TEXT, assertion::checkCopyright);
	}

	@AfterClass
	public void tearDownAssertions() {
		softAssert.assertAll();
	}
}



