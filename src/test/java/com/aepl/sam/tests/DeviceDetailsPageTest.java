package com.aepl.sam.tests;

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

import com.aepl.sam.pages.CommonMethods;
import com.aepl.sam.pages.DeviceDetailsPage;
import com.aepl.sam.utils.ExcelUtility;

public class DeviceDetailsPageTest extends TestBase {
	// Excel Sheet Name
	private static final String DEVICE_DETAILS_EXCEL_SHEET = "Device_Details_Test";

	private DeviceDetailsPage deviceDetails;
	private CommonMethods comm;
	private ExcelUtility excelUtility;
	private SoftAssert softAssert;
	private Executor executor;

	@Override
	@BeforeClass
	public void setUp() {
		super.setUp();
		this.comm = new CommonMethods(driver, wait);
		this.deviceDetails = new DeviceDetailsPage(driver, wait, comm);
		this.excelUtility = new ExcelUtility();
		this.softAssert = new SoftAssert();
		this.executor = new Executor(excelUtility, softAssert);
		excelUtility.initializeExcel(DEVICE_DETAILS_EXCEL_SHEET);
		logger.info("Setup completed for DeviceDetailsPageTest");
	}

	// Testing for company logo display
	@Test(priority = 1)
	public void testCompanyLogo() {
		executor.executeTest("Verify Company Logo on Webpage", Constants.EXP_LOGO_DISPLAYED,
				() -> comm.verifyWebpageLogo() ? Constants.EXP_LOGO_DISPLAYED : "Logo Not Displayed");
	}

	// Testing for page title
	@Test(priority = 2)
	public void testPageTitle() {
		executor.executeTest("Verify Page Title on Webpage", Constants.EXP_PAGE_TITLE_TEXT, comm::verifyPageTitle);
	}

	// Testing for refresh button functionality
	@Test(priority = 3)
	public void testRefreshButton() {
		executor.executeTest("Verify Refresh Button Functionality", "Clicked on the refreshed button", () -> {
			comm.clickRefreshButton();
			return "Clicked on the refreshed button";
		});
	}

	// Testing for all buttons on the page
	@Test(priority = 4)
	public void testButtons() {
		executor.executeTest("Verify All Buttons on Device Details Page", Constants.EXP_VALIDATE_BUTTONS_TEXT, comm::validateButtons);
	}

	// Testing for component titles on the page
	@Test(priority = 5)
	public void testComponentTitles() {
		executor.executeTest("Verify All Component Title on Device Details Page", Constants.EXP_VALIDATE_COMPONENTS_TEXT, comm::validateComponents);
	}

	// Testing for the bar graph on the page
	@Test(priority = 6)
	public void testClickOnDeviceActivityBarGraph() {
		executor.executeTest("Verify Bar Graph on Device Details Page", "Device Activity Overview", () -> {
			if (deviceDetails.isBarGraphVisible()) {
				String actual = deviceDetails.clickOnDeviceActivityBarGraph();
				return actual;
			} else {
				logger.info("Bar Graph is not visible, skipping test.");
				return "Bar Graph not visible";
			}
		});
	}

	// ************************************************************************* //

	// validate search button is visible
	@Test(priority = 7)
	public void testIsSearchButtonVisible() {
		executor.executeTest("Search Button Visible", "Search Button Visible",
				() -> deviceDetails.isSearchButtonVisible() ? "Search Button Visible"
						: "Search Button Not Visible");
	}

	// validate search button is enabled
	@Test(priority = 8)
	public void testIsSearchButtonEnabled() {
		executor.executeTest("Search Button Enabled", "Search Button Enabled",
				() -> deviceDetails.isSearchButtonEnabled() ? "Search Button Enabled"
						: "Search Button Not Enabled");
	}

	// validate search input is visible
	@Test(priority = 9)
	public void testIsSearchInputVisible() {
		executor.executeTest("Search Input Visible", "Search Input Visible",
				() -> deviceDetails.isSearchInputVisible() ? "Search Input Visible" : "Search Input Not Visible");
	}

	// validate search input is enabled
	@Test(priority = 10)
	public void testIsSearchInputEnabled() {
		executor.executeTest("Search Input Enabled", "Search Input Enabled",
				() -> deviceDetails.isSearchInputEnabled() ? "Search Input Enabled" : "Search Input Not Enabled");
	}

	// validate the search functionality
	@Test(priority = 11)
	public void testDeviceSearch() {
		executor.executeTest("Test search functionality", true, deviceDetails::searchDevice);
	}

	// validate the table headers
	@Test(priority = 12)
	public void testTableHeaders() {
		List<String> expectedHeaders = Arrays.asList("UIN NO.", "IMEI NO.", "ICCID NO.", "MODEL NAME.", "LOG IN TIME",
				"ACTION");
		executor.executeTest("Test Table Headers of the searched device: ", expectedHeaders,
				deviceDetails::validateTableHeaders);
	}

	// vlaidate the table data
	@Test(priority = 13)
	public void testTableData() {
		Map<String, String> expectedRow = new LinkedHashMap<>();
		expectedRow.put("UIN NO.", "ACON4SA310213796709");
		expectedRow.put("IMEI NO.", "867950076683091");
		expectedRow.put("ICCID NO.", "89916440844825969900");
		expectedRow.put("MODEL NAME.", "Sam");
		expectedRow.put("LOG IN TIME", "--");
		expectedRow.put("ACTION", "visibility");

		List<Map<String, String>> expectedData = Collections.singletonList(expectedRow);
		executor.executeTest("Test Table Data of the searched device: ", expectedData, deviceDetails::getTableData);
	}

	// validate the view button functionality
	@Test(priority = 14)
	public void testViewButtonEnabled() {
		executor.executeTest("Test view button functionality: ", true, deviceDetails::isViewButtonEnabled);
	}

	// Testing for searching and viewing a device
	@Test(priority = 15)
	public void testViewDevice() {
		executor.executeTest("Search and View Device", "Device details displayed successfully", () -> {
			deviceDetails.viewDevice();
			return "Device details displayed successfully";
		});
	}

	// Validate the page title after viewing a device
	@Test(priority = 16)
	public void testPageTitleAfterViewingDevice() {
		executor.executeTest("Test Page Title of the device details page ", "Device Details",
				deviceDetails::validatePageTitle);
	}

	// Testing for all buttons on the page again after viewing a device
	@Test(priority = 17)
	public void testAllButtons() {
		executor.executeTest("Verify All Buttons on Device Details Page", Constants.EXP_VALIDATE_BUTTONS_TEXT, comm::validateButtons);
	}

	// Validationg all components on the page
	@Test(priority = 18)
	public void testComponentTitle() {
		executor.executeTest("Verify All Component Title on Device Details Page", Constants.EXP_VALIDATE_COMPONENTS_TEXT, comm::validateComponents);
	}

	// Validate the imei displayed on the device details page is correct
	@Test(priority = 19)
	public void testValidateIMEIOnDeviceDetailsPage() {
		executor.executeTest("Validate IMEI on Device Details Page", Constants.IMEI,
				deviceDetails::validateIMEIOnDeviceDetailsPage);
	}

	// Validate the input field of the imei is enabled
	@Test(priority = 20)
	public void testIsIMEIInputEnabled() {
		executor.executeTest("Validate IMEI input field is enabled", true, deviceDetails::isIMEIInputVisible);
	}

	// Validate the input field of the imei is clickable
	@Test(priority = 21)
	public void testIsIMEIInputClickable() {
		executor.executeTest("Validate IMEI input field is clickable", true, deviceDetails::isIMEIInputClickable);
	}

	// Validate the wrong input in imei field gives error/ toast message error
	@Test(priority = 22)
	public void testInvalidIMEIInput() {
		executor.executeTest("Validate invalid IMEI input gives error", true, deviceDetails::validateInvalidIMEIInput);
	}

	// Validate the search button is visible
	@Test(priority = 23)
	public void testIsSearchButtonVisibleAfterViewingDevice() {
		executor.executeTest("Search Button Visible", "Search Button Visible",
				() -> deviceDetails.isSearchButtonVisible() ? "Search Button Visible"
						: "Search Button Not Visible");
	}

	// Validate the search button is enabled
	@Test(priority = 24)
	public void testIsSearchButtonEnabledAfterViewingDevice() {
		executor.executeTest("Search Button Enabled", "Search Button Enabled",
				() -> deviceDetails.isSearchButtonEnabled() ? "Search Button Enabled"
						: "Search Button Not Enabled");
	}

	// Validate the valid imei searched
	@Test(priority = 25)
	public void testValidIMEISearch() {
		executor.executeTest("Validate valid IMEI search", Constants.IMEI, deviceDetails::validateValidIMEISearch);
	}

	// Validate the FOTA button is enalbed on the top of the page along with the
	// page header
	@Test(priority = 26)
	public void testIsFOTAButtonEnabled() {
		executor.executeTest("Validate FOTA button is enabled", true, deviceDetails::isFOTAButtonEnabled);
	}

	// Validate the FOTA button link is clickable on the top of the page along with
	// the
	@Test(priority = 27)
	public void testIsFOTAButtonClickable() {
		executor.executeTest("Validate FOTA button is clickable", true, deviceDetails::isFOTAButtonClickable);
	}

	// Validate the OTA button is enalbed on the top of the page along with the
	// page header
	@Test(priority = 28)
	public void testIsOTAButtonEnabled() {
		executor.executeTest("Validate OTA button is enabled", true, deviceDetails::isOTAButtonEnabled);
	}

	// Validate the OTA button link is clickable on the top of the page along with
	// the
	// @Test(priority = 29)
	public void testIsOTAButtonClickable() {
		executor.executeTest("Validate OTA button is clickable", true, deviceDetails::isOTAButtonClickable);
	}

	// Validate the info Cards are displayed on the top of the page
	@Test(priority = 30)
	public void testAreInfoCardsVisible() {
		executor.executeTest("Validate info cards are visible", true, deviceDetails::areInfoCardsVisible);
	}

	// Validate the info Cards are enabled on the top of the page
	@Test(priority = 31)
	public void testAreInfoCardsEnabled() {
		executor.executeTest("Validate info cards are enabled", true, deviceDetails::areInfoCardsEnabled);
	}

	// Testing for all cards on the page -- info cards on the top of the page which
	// shows the IGN,MAINS, TAMPER, PWR, etc
	@Test(priority = 32)
	public void testAllCards() {
		executor.executeTest("Verify All Cards on Device Details Page", "All cards are displayed and validated successfully.", comm::validateCards);
	}

	// Test all cards headers is valid with the expected headers
	@Test(priority = 33)
	public void testAllCardsHeaders() {
		List<String> expectedHeaders = Arrays.asList("IGNITION ON/OFF", "MAINS ON/OFF", "EMERGENCY ON/OFF",
				"TAMPER OPEN/CLOSE", "ACC CALIBRATION ON/OF", "WIRE CUT");
		executor.executeTest("Test All Cards Headers: ", expectedHeaders, deviceDetails::validateAllCardsHeaders);
	}

	/// Starting from here.
	// Test The mains on/off cards values is above some threshould value if it is on
	@Test(priority = 34)
	public void testMainsOnOffCardValue() {
		executor.executeTest("Test Mains On/Off Card Value is above 12 when it is ON: ", true,
				deviceDetails::validateMainsOnOffCardValue);
	}

	// validate all components-cards are visible on the page
	@Test(priority = 35)
	public void testAreAllComponentsVisible() {
		executor.executeTest("Validate all components are visible", true, deviceDetails::areAllComponentsVisible);
	}

	// Test all components list is equal to 4
	@Test(priority = 36)
	public void testAllComponentsCount() {
		executor.executeTest("Test All Components Count: ", 4, deviceDetails::getAllComponentsCount);
	}

	// Test all components headers is valid with the expected headers
	@Test(priority = 37)
	public void testAllComponentsHeaders() {
		List<String> expectedHeaders = Arrays.asList("Device Details", "IP Details", "GPS Details",
				"Accelerometer Details");
		executor.executeTest("Test All Components Headers: ", expectedHeaders,
				deviceDetails::validateAllComponentsHeaders);
	}

	@Test(priority = 38)
	public void testDeviceDetailsComponentCheckForValidImei() {
		executor.executeTest("Test the {Device Details} card for valid IMEI ", true,
				() -> deviceDetails.deviceDetailsComponentCheckForValidImei());
	}

	// Validate the GPS Details component/card have two buttons track device and
	// view location on map.
	@Test(priority = 39)
	public void testGPSDetailsComponentButtons() {
		executor.executeTest("Validate GPS Details component have two buttons", true,
				deviceDetails::validateGPSDetailsComponentButtons);
	}

	// Validate the last 50 login packets component is displayed on the page
	@Test(priority = 40)
	public void testIsLast50LoginPacketsComponentVisible() {
		executor.executeTest("Validate last 50 login packets component is visible", true,
				deviceDetails::isLast50LoginPacketsComponentVisible);
	}

	// validate the export button is visible on the last 50 login packets component
	@Test(priority = 41)
	public void testIsExportButtonVisible() {
		executor.executeTest("Validate export button is visible on last 50 login packets component", true,
				deviceDetails::isExportButtonVisible);
	}

	// validate the export button is enabled on the last 50 login packets component
	@Test(priority = 42)
	public void testIsExportButtonEnabled() {
		executor.executeTest("Validate export button is enabled on last 50 login packets component", true,
				deviceDetails::isExportButtonEnabled);
	}

	// Testing for export button functionality
	@Test(priority = 43)
	public void testvalidateExportButton() {
		executor.executeTest("Verify Last 50 Login Packets on Device Details Page", "Last 50 login packets are displayed successfully", () -> comm.validateExportButton() ? "Last 50 login packets are displayed successfully" : "ERROR");
	}

	// validate the table headers of the last 50 login packets component
	@Test(priority = 44)
	public void testLast50LoginPacketsTableHeaders() {
		List<String> expectedHeaders = Arrays.asList("UIN NO.", "IMEI NO.", "ICCID.", "IGNITION", "DATE & TIME",
				"ACTION");
		executor.executeTest("Test Table Headers of the last 50 login packets component: ", expectedHeaders,
				deviceDetails::validateLast50LoginPacketsTableHeaders);
	}

	// validate the 50 is count of the last 50 login packets component
	@Test(priority = 45)
	public void testLast50LoginPacketsCount() {
		executor.executeTest("Test Last 50 Login Packets Count: ", 50, deviceDetails::getLast50LoginPacketsCount);
	}

	// validate the view button is enabled of the last 50 login table
	@Test(priority = 46)
	public void testIsLast50LoginPacketsViewButtonEnabled() {
		executor.executeTest("Test Last 50 Login Packets View Button is Enabled: ", true,
				deviceDetails::isLast50LoginPacketsViewButtonEnabled);
	}

	// Testing for viewing the login packet of the device - clicked the view button
	// and validate the login packet details
	// view last 10 login packets and create a json file of it.
	@Test(priority = 47)
	public void testViewLoginPacket() {
		executor.executeTest("Verify View Login Packet on Device Details Page", "All login packets viewed and saved successfully", deviceDetails::viewLoginPacket);
	}

	@Test(priority = 48)
	public void testPaginationOnLoginPacketComponent() {
		executor.executeTest("Verify Pagination on Device Details Page", "Pagination is displayed and functional", () -> {
			comm.checkPagination();
			return "Pagination is displayed and functional";
		});
	}

	// from 40 t0 48 all test cases i need it for the health packet also.
	@Test(priority = 49)
	public void testIsLast50HealthPacketsComponentVisible() {
		executor.executeTest("Validate last 50 health packets component is visible", true,
				deviceDetails::isLast50HealthPacketsComponentVisible);
	}

	// validate the export button is visible on the last 50 health packets component
	@Test(priority = 50)
	public void testIsHealthExportButtonVisible() {
		executor.executeTest("Validate export button is visible on last 50 health packets component", true,
				deviceDetails::isHealthExportButtonVisible);
	}

	// validate the export button is enabled on the last 50 health packets component
	@Test(priority = 51)
	public void testIsHealthExportButtonEnabled() {
		executor.executeTest("Validate export button is enabled on last 50 health packets component", true,
				deviceDetails::isHealthExportButtonEnabled);
	}

	// validate the table headers of the last 50 health packets component
	@Test(priority = 52)
	public void testLast50HealthPacketsTableHeaders() {
		List<String> expectedHeaders = Arrays.asList("UIN NO.", "IMEI NO.", "ICCID.", "IGNITION", "DATE & TIME",
				"ACTION");
		executor.executeTest("Test Table Headers of the last 50 health packets component: ", expectedHeaders,
				deviceDetails::validateLast50HealthPacketsTableHeaders);
	}

	// validate the 50 is count of the last 50 health packets component
	@Test(priority = 53)
	public void testLast50HealthPacketsCount() {
		executor.executeTest("Test Last 50 Health Packets Count: ", 50, deviceDetails::getLast50HealthPacketsCount);
	}

	// validate the view button is enabled of the last 50 health table
	@Test(priority = 54)
	public void testIsLast50HealthPacketsViewButtonEnabled() {
		executor.executeTest("Test Last 50 Health Packets View Button is Enabled: ", true,
				deviceDetails::isLast50HealthPacketsViewButtonEnabled);
	}

	@Test(priority = 55)
	public void testViewHealthPacket() {
		executor.executeTest("Verify Health Packet", "Health packet details are displayed successfully", () -> {
			if (deviceDetails.isHealthPacketVisible()) {
				return deviceDetails.viewHealthPacket();
			} else {
				logger.info("Health Packet is not visible, skipping test.");
				return "Health Packet not visible";
			}
		});
	}

	// Testing for pagination functionality
	@Test(priority = 56)
	public void testPaginationOnHealthPacketComponent() {
		executor.executeTest("Verify Pagination on Device Details Page", "Pagination is displayed and functional", () -> {
			comm.checkPagination();
			return "Pagination is displayed and functional";
		});
	}

	// Testing for version text on the page
	@Test(priority = 57)
	public void testVersion() {
		executor.executeTest("Verify Version on Device Details Page", Constants.EXP_VERSION_TEXT, comm::checkVersion);
		logger.info("Version test executed successfully.");
	}

	// Testing for copyright text on the page
	@Test(priority = 58)
	public void testCopyright() {
		executor.executeTest("Verify Copyright on Device Details Page", Constants.EXP_COPYRIGHT_TEXT, comm::checkCopyright);
		logger.info("Copyright test executed successfully.");
	}

	@AfterClass
	public void tearDownAssertions() {
		softAssert.assertAll();
	}
}
