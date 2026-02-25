package com.aepl.sam.tests;

import java.util.List;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aepl.sam.base.TestBase;

import com.aepl.sam.utils.PageActionsUtil;
import com.aepl.sam.utils.PageAssertionsUtil;
import com.aepl.sam.pages.DeviceDashboardPage;
import com.aepl.sam.utils.Constants;
import com.aepl.sam.utils.ExcelUtility;

public class DeviceDashboardPageTest extends TestBase {
	private static final String DEVICE_DASHBOARD_EXCEL_SHEET = "DeviceDashboardTests";

	private DeviceDashboardPage deviceDashboardPage;
	private PageActionsUtil comm;
	private PageAssertionsUtil assertion;
	private ExcelUtility excelUtility;
	private SoftAssert softAssert;
	private Executor executor;

	@Override
	@BeforeClass
	public void setUp() {
		super.setUp();
		this.deviceDashboardPage = new DeviceDashboardPage(driver, wait, action);
		this.comm = new PageActionsUtil(driver, wait);
		this.assertion = new PageAssertionsUtil(driver, wait);
		this.softAssert = new SoftAssert();
		this.excelUtility = new ExcelUtility();
		this.executor = new Executor(excelUtility, softAssert);
		this.excelUtility.initializeExcel(DEVICE_DASHBOARD_EXCEL_SHEET);
		logger.info("Setup completed for DeviceDashboardPageTest");
	}

	// =========================================================
	// ÃƒÂ°Ã…Â¸Ã‚Â§Ã‚Âª General Tests
	// =========================================================

	@Test(priority = 1)
	public void testCompanyLogo() {
		executor.executeTest("Verify Company Logo", true, comm::verifyWebpageLogo);
	}

	@Test(priority = 2)
	public void testPageTitle() {
		executor.executeTest("Verify Page Title", Constants.EXP_PAGE_TITLE_TEXT, assertion::verifyPageTitle);
	}

	@Test(priority = 3)
	public void testClickNavBar() {
		executor.executeTest("Verify Navigation Bar Link", "Link is verified", deviceDashboardPage::clickNavBar);
	}

	@Test(priority = 4)
	public void testValidateComponents() {
		executor.executeTest("Validate Components", Constants.EXP_VALIDATE_COMPONENTS_TEXT, assertion::validateComponents);
	}

	@Test(priority = 5)
	public void testValidateButtons() {
		executor.executeTest("Validate Buttons", Constants.EXP_VALIDATE_BUTTONS_TEXT, assertion::validateButtons);
	}

	// Validate all cards
	@Test(priority = 6)
	public void testValidateAllCards() {
		executor.executeTest("Test all cards visible", true, deviceDashboardPage::validateCardAreVisible);
	}

	// Validate the graph visibility
	@Test(priority = 7)
	public void testValidateGraph() {
		executor.executeTest("Test graph visible", true, deviceDashboardPage::validateGraphIsVisible);
	}

	// validate the graph click and headers matches with the tables headers
	@Test(priority = 8)
	public void testValidateGraphClick() {
		executor.executeTest("Test graph click and headers", true, deviceDashboardPage::validateGraphClick);
	}
	// =========================================================
	// ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã…Â  Total Production Devices Table
	// =========================================================

	@Test(priority = 9)
	public void testTotalProductionDevicesTableHeaders() {
		final List<String> TOTAL_PRODUCTION_DEVICES_HEADERS = List.of("UIN NO.", "IMEI NO.", "ICCID NO.",
				"MODEL NAME.",
				"ACTION");
		executor.executeTest("Total Production Devices Headers", TOTAL_PRODUCTION_DEVICES_HEADERS,
				deviceDashboardPage::validateTotalProductionDevicesTableHeaders);
	}

	@Test(priority = 10)
	public void testTotalProductionDevicesTableButtons() {
		executor.executeTest("Total Production Devices Table Buttons", true,
				deviceDashboardPage::validateTotalProductionDevicesTableButtons);
	}

	@Test(priority = 11)
	public void testIsSearchButtonVisibleOnTotalProductionDevicesTable() {
		executor.executeTest("Search Button Visible", "Search Button Visible",
				() -> deviceDashboardPage.isSearchButtonVisible() ? "Search Button Visible"
						: "Search Button Not Visible");
	}

	@Test(priority = 12)
	public void testIsSearchButtonEnabledOnTotalProductionDevicesTable() {
		executor.executeTest("Search Button Enabled", "Search Button Enabled",
				() -> deviceDashboardPage.isSearchButtonEnabled() ? "Search Button Enabled"
						: "Search Button Not Enabled");
	}

	@Test(priority = 13)
	public void testIsSearchInputVisibleOnTotalProductionDevicesTable() {
		executor.executeTest("Search Input Visible", "Search Input Visible",
				() -> deviceDashboardPage.isSearchInputVisible() ? "Search Input Visible"
						: "Search Input Not Visible");
	}

	@Test(priority = 14)
	public void testIsSearchInputEnabledOnTotalProductionDevicesTable() {
		executor.executeTest("Search Input Enabled", "Search Input Enabled",
				() -> deviceDashboardPage.isSearchInputEnabled() ? "Search Input Enabled"
						: "Search Input Not Enabled");
	}

	@Test(priority = 15)
	public void testDeviceSearchOnTotalProductionDevicesTable() {
		executor.executeTest("Search Device", true, deviceDashboardPage::searchDevice);
	}

	@Test(priority = 16)
	public void testIsExportButtonVisibleOnTotalProductionDevicesTable() {
		executor.executeTest("Export Button Visible", true, deviceDashboardPage::isExportButtonVisible);
	}

	@Test(priority = 17)
	public void testIsExportButtonEnabledOnTotalProductionDevicesTable() {
		executor.executeTest("Export Button Enabled", true, deviceDashboardPage::isExportButtonEnabled);
	}

	@Test(priority = 18)
	public void testExportFunctionalityOnTotalProductionDevicesTable() {
		executor.executeTest("Export Functionality", true, comm::validateExportButton);
	}

	@Test(priority = 19)
	public void testPaginationOnTotalProductionDevicesTable() {
		executor.executeTest("Verify Pagination", "Pagination Working", () -> {
			comm.checkPagination();
			return "Pagination Working";
		});
	}

	// **** Total Dispatched Devices Table ****//

	@Test(priority = 20)
	public void testTotalDispatchedDevicesTableHeaders() {
		final List<String> TOTAL_DISPATCHED_DEVICES_HEADERS = List.of("UIN NO.", "IMEI NO.", "ICCID NO.",
				"MODEL NAME.",
				"CUSTOMER NAME", "ACTION");
		executor.executeTest("Test total dispatched devices table", TOTAL_DISPATCHED_DEVICES_HEADERS,
				deviceDashboardPage::validateTotalDispatchedDevicesTableHeaders);
	}

	@Test(priority = 21)
	public void testTotalDispatchedDevicesTableButtons() {
		executor.executeTest("Test total dispatched devices table buttons", true,
				deviceDashboardPage::validateTotalDispatchedDevicesTableButtons);
	}

	@Test(priority = 22)
	public void testIsSearchButtonVisibleOnTotalDispatchedDevicesTable() {
		executor.executeTest("Search Button Visible", "Search Button Visible",
				() -> deviceDashboardPage.isSearchButtonVisible() ? "Search Button Visible"
						: "Search Button Not Visible");
	}

	// validate search button is enabled
	@Test(priority = 23)
	public void testIsSearchButtonEnabledOnTotalDispatchedDevicesTable() {
		executor.executeTest("Search Button Enabled", "Search Button Enabled",
				() -> deviceDashboardPage.isSearchButtonEnabled() ? "Search Button Enabled"
						: "Search Button Not Enabled");
	}

	// validate search input is visible
	@Test(priority = 24)
	public void testIsSearchInputVisibleOnTotalDispatchedDevicesTable() {
		executor.executeTest("Search Input Visible", "Search Input Visible",
				() -> deviceDashboardPage.isSearchInputVisible() ? "Search Input Visible"
						: "Search Input Not Visible");
	}

	// validate search input is enabled
	@Test(priority = 25)
	public void testIsSearchInputEnabledOnTotalDispatchedDevicesTable() {
		executor.executeTest("Search Input Enabled", "Search Input Enabled",
				() -> deviceDashboardPage.isSearchInputEnabled() ? "Search Input Enabled"
						: "Search Input Not Enabled");
	}

	// validate the search functionality
	@Test(priority = 26)
	public void testDeviceSearchOnTotalDispatchedDevicesTable() {
		executor.executeTest("Test search functionality", true, deviceDashboardPage::searchDevice);
	}

	// validate the export button is visible
	@Test(priority = 27)
	public void testIsExportButtonVisibleOnTotalDispatchedDevicesTable() {
		executor.executeTest("Test export button visible", true, deviceDashboardPage::isExportButtonVisible);
	}

	// validate the export button is enabled
	@Test(priority = 28)
	public void testIsExportButtonEnabledOnTotalDispatchedDevicesTable() {
		executor.executeTest("Test export button enabled", true, deviceDashboardPage::isExportButtonEnabled);
	}

	// validate the export functionality
	@Test(priority = 29)
	public void testExportFunctionalityOnTotalDispatchedDevicesTable() {
		executor.executeTest("Test export functionality", true, comm::validateExportButton);
	}

	// validate the pagination of the total production devices table
	@Test(priority = 30)
	public void testPaginationOnTotalDispatchedDevicesTable() {
		executor.executeTest("Verify Pagination", "Pagination Working", () -> {
			comm.checkPagination();
			return "Pagination Working";
		});
	}

	// **** Total Installed Devices Table ****//
	@Test(priority = 31)
	public void testTotalInstalledDevicesTableHeaders() {
		final List<String> TOTAL_INSTALLED_DEVICES_HEADERS = List.of("UIN NO.", "IMEI NO.", "ICCID NO.",
				"CHASSIS NO.",
				"MODEL NAME.", "CUSTOMER NAME", "ACTION");
		executor.executeTest("Test total dispatched devices table", TOTAL_INSTALLED_DEVICES_HEADERS,
				deviceDashboardPage::validateTotalInstalledDevicesTableHeaders);
	}

	@Test(priority = 32)
	public void testTotalInstalledDevicesTableButtons() {
		executor.executeTest("Test total dispatched devices table buttons", true,
				deviceDashboardPage::validateTotalInstalledDevicesTableButtons);
	}

	@Test(priority = 33)
	public void testIsSearchButtonVisibleOnTotalInstalledDevicesTable() {
		executor.executeTest("Search Button Visible", "Search Button Visible",
				() -> deviceDashboardPage.isSearchButtonVisible() ? "Search Button Visible"
						: "Search Button Not Visible");
	}

	// validate search button is enabled
	@Test(priority = 34)
	public void testIsSearchButtonEnabledOnTotalInstalledDevicesTable() {
		executor.executeTest("Search Button Enabled", "Search Button Enabled",
				() -> deviceDashboardPage.isSearchButtonEnabled() ? "Search Button Enabled"
						: "Search Button Not Enabled");
	}

	// validate search input is visible
	@Test(priority = 35)
	public void testIsSearchInputVisibleOnTotalInstalledDevicesTable() {
		executor.executeTest("Search Input Visible", "Search Input Visible",
				() -> deviceDashboardPage.isSearchInputVisible() ? "Search Input Visible"
						: "Search Input Not Visible");
	}

	// validate search input is enabled
	@Test(priority = 36)
	public void testIsSearchInputEnabledOnTotalInstalledDevicesTable() {
		executor.executeTest("Search Input Enabled", "Search Input Enabled",
				() -> deviceDashboardPage.isSearchInputEnabled() ? "Search Input Enabled"
						: "Search Input Not Enabled");
	}

	// validate the search functionality
	@Test(priority = 37)
	public void testDeviceSearchOnTotalInstalledDevicesTable() {
		executor.executeTest("Test search functionality", true, deviceDashboardPage::searchDevice);
	}

	// validate the export button is visible
	@Test(priority = 38)
	public void testIsExportButtonVisibleOnTotalInstalledDevicesTable() {
		executor.executeTest("Test export button visible", true, deviceDashboardPage::isExportButtonVisible);
	}

	// validate the export button is enabled
	@Test(priority = 39)
	public void testIsExportButtonEnabledOnTotalInstalledDevicesTable() {
		executor.executeTest("Test export button enabled", true, deviceDashboardPage::isExportButtonEnabled);
	}

	// validate the export functionality
	@Test(priority = 40)
	public void testExportFunctionalityOnTotalInstalledDevicesTable() {
		executor.executeTest("Test export functionality", true, comm::validateExportButton);
	}

	// validate the pagination of the total production devices table
	@Test(priority = 41)
	public void testPaginationOnTotalInstalledDevicesTable() {
		executor.executeTest("Verify Pagination", "Pagination Working", () -> {
			comm.checkPagination();
			return "Pagination Working";
		});
	}

	// **** Total Discarded Devices Table ****//
	@Test(priority = 42)
	public void testTotalDiscardedDevicesTableHeaders() {
		final List<String> TOTAL_DISCARDED_DEVICES_HEADERS = List.of("UIN NO.", "IMEI NO.", "ICCID NO.",
				"CHASSIS NO.",
				"MODEL NAME.", "INSTALLED AT", "DISCARDED AT", "ACTION");
		executor.executeTest("Test total Discarded devices table", TOTAL_DISCARDED_DEVICES_HEADERS,
				deviceDashboardPage::validateTotalDiscardedDevicesTableHeaders);
	}

	@Test(priority = 43)
	public void testTotalDiscardedDevicesTableButtons() {
		executor.executeTest("Test total dispatched devices table buttons", true,
				deviceDashboardPage::validateTotalDiscardedDevicesTableButtons);
	}

	@Test(priority = 44)
	public void testIsSearchButtonVisibleOnTotalDiscardedDevicesTable() {
		executor.executeTest("Search Button Visible", "Search Button Visible",
				() -> deviceDashboardPage.isSearchButtonVisible() ? "Search Button Visible"
						: "Search Button Not Visible");
	}

	// validate search button is enabled
	@Test(priority = 45)
	public void testIsSearchButtonEnabledOnTotalDiscardedDevicesTable() {
		executor.executeTest("Search Button Enabled", "Search Button Enabled",
				() -> deviceDashboardPage.isSearchButtonEnabled() ? "Search Button Enabled"
						: "Search Button Not Enabled");
	}

	// validate search input is visible
	@Test(priority = 46)
	public void testIsSearchInputVisibleOnTotalDiscardedDevicesTable() {
		executor.executeTest("Search Input Visible", "Search Input Visible",
				() -> deviceDashboardPage.isSearchInputVisible() ? "Search Input Visible"
						: "Search Input Not Visible");
	}

	// validate search input is enabled
	@Test(priority = 47)
	public void testIsSearchInputEnabledOnTotalDiscardedDevicesTable() {
		executor.executeTest("Search Input Enabled", "Search Input Enabled",
				() -> deviceDashboardPage.isSearchInputEnabled() ? "Search Input Enabled"
						: "Search Input Not Enabled");
	}

	// validate the search functionality
	@Test(priority = 48)
	public void testDeviceSearchOnTotalDiscardedDevicesTable() {
		executor.executeTest("Test search functionality", true, deviceDashboardPage::searchDevice);
	}

	// validate the export button is visible
	@Test(priority = 49)
	public void testIsExportButtonVisibleOnTotalDiscardedDevicesTable() {
		executor.executeTest("Test export button visible", true, deviceDashboardPage::isExportButtonVisible);
	}

	// validate the export button is enabled
	@Test(priority = 50)
	public void testIsExportButtonEnabledOnTotalDiscardedDevicesTable() {
		executor.executeTest("Test export button enabled", true, deviceDashboardPage::isExportButtonEnabled);
	}

	// validate the export functionality
	@Test(priority = 51)
	public void testExportFunctionalityOnTotalDiscardedDevicesTable() {
		executor.executeTest("Test export functionality", true, comm::validateExportButton);
	}

	// validate the pagination of the total production devices table
	@Test(priority = 52)
	public void testPaginationOnTotalDiscardedDevicesTable() {
		executor.executeTest("Verify Pagination", "Pagination Working", () -> {
			comm.checkPagination();
			return "Pagination Working";
		});
	}

	// **** Device Activity Overview Graph **** //
	@Test(priority = 53)
	public void testDeviceActivityOverviewGraphIsVisible() {
		executor.executeTest("Test Device Activity Overview Graph is visible", true,
				deviceDashboardPage::isDeviceActivityOverviewGraphVisible);
	}

	@Test(priority = 54)
	public void testDeviceActivityOverviewGraphClick() {
		executor.executeTest("Test Device Activity Overview Graph click", true,
				deviceDashboardPage::validateDeviceActivityOverviewGraphClick);
	}

	@Test(priority = 55)
	public void testDeviceActivityOverviewGraphTableHeaders() {
		final List<String> DEVICE_ACTIVITY_OVERVIEW_HEADERS = List.of("UIN NO.", "IMEI NO.", "ICCID NO.",
				"MODEL NAME.",
				"LOG IN TIME", "ACTION");
		executor.executeTest("Test Device Activity Overview Graph Legend", DEVICE_ACTIVITY_OVERVIEW_HEADERS,
				deviceDashboardPage::validateDeviceActivityOverviewGraphTableHeaders);
	}

	@Test(priority = 56)
	public void testDeviceActivityOverviewTableButtons() {
		executor.executeTest("Test total dispatched devices table buttons", true,
				deviceDashboardPage::validateDeviceActivityOverviewTableButtons);
	}

	@Test(priority = 57)
	public void testIsSearchButtonVisibleOnDeviceActivityOverviewTable() {
		executor.executeTest("Search Button Visible", "Search Button Visible",
				() -> deviceDashboardPage.isSearchButtonVisible() ? "Search Button Visible"
						: "Search Button Not Visible");
	}

	// validate search button is enabled
	@Test(priority = 58)
	public void testIsSearchButtonEnabledOnDeviceActivityOverviewTable() {
		executor.executeTest("Search Button Enabled", "Search Button Enabled",
				() -> deviceDashboardPage.isSearchButtonEnabled() ? "Search Button Enabled"
						: "Search Button Not Enabled");
	}

	// validate search input is visible
	@Test(priority = 59)
	public void testIsSearchInputVisibleOnDeviceActivityOverviewTable() {
		executor.executeTest("Search Input Visible", "Search Input Visible",
				() -> deviceDashboardPage.isSearchInputVisible() ? "Search Input Visible"
						: "Search Input Not Visible");
	}

	// validate search input is enabled
	@Test(priority = 60)
	public void testIsSearchInputEnabledOnDeviceActivityOverviewTable() {
		executor.executeTest("Search Input Enabled", "Search Input Enabled",
				() -> deviceDashboardPage.isSearchInputEnabled() ? "Search Input Enabled"
						: "Search Input Not Enabled");
	}

	// validate the search functionality
	@Test(priority = 61)
	public void testDeviceSearchOnDeviceActivityOverviewTable() {
		executor.executeTest("Test search functionality", true, deviceDashboardPage::searchDevice);
	}

	// validate the export button is visible
	@Test(priority = 62)
	public void testIsExportButtonVisibleOnDeviceActivityOverviewTable() {
		executor.executeTest("Test export button visible", true, deviceDashboardPage::isExportButtonVisible);
	}

	// validate the export button is enabled
	@Test(priority = 63)
	public void testIsExportButtonEnabledOnDeviceActivityOverviewTable() {
		executor.executeTest("Test export button enabled", true, deviceDashboardPage::isExportButtonEnabled);
	}

	// validate the export functionality
	@Test(priority = 64)
	public void testExportFunctionalityOnDeviceActivityOverviewTable() {
		executor.executeTest("Test export functionality", true, comm::validateExportButton);
	}

	@Test(priority = 65)
	public void testSelectActivityDurationDropdown() {
		final List<String> ACTIVITY_DURATION_DROPDOWN_OPTIONS = List.of("All", "Today", "Five Days",
				"Ten Days", "Fifteen Days",
				"More Than Fifteen", "Not Active");
		executor.executeTest("Test select activity duration dropdown", ACTIVITY_DURATION_DROPDOWN_OPTIONS,
				deviceDashboardPage::selectActivityDurationDropdown);
	}

	// Validate the table data of the Device Activity Overview table
	@Test(priority = 66)
	public void testValidateTableDateOfDeviceActivityOverviewTable() {
		executor.executeTest("Test table data of Device Activity Overview table", true,
				deviceDashboardPage::validateTableDataOfDeviceActivityOverviewTable);
	}

	// validate that the view button is enabled in the Device Activity Overview
	// table
	@Test(priority = 67)
	public void testIsViewButtonEnabledInDeviceActivityOverviewTable() {
		executor.executeTest("Test view button is enabled in Device Activity Overview table", true,
				deviceDashboardPage::isViewButtonEnabledInDeviceActivityOverviewTable);
	}

	// **** Firmware Wise Devices graph **** //
	@Test(priority = 68)
	public void testFirmwareWiseDevicesGraphIsVisible() {
		executor.executeTest("Test Firmware Wise Devices Graph is visible", true,
				deviceDashboardPage::isFirmwareWiseDevicesGraphVisible);
	}

	@Test(priority = 69)
	public void testFirmwareWiseDevicesGraphClick() {
		executor.executeTest("Test Firmware Wise Devices Graph click", true,
				deviceDashboardPage::validateFirmwareWiseDevicesGraphClick);
	}

	@Test(priority = 70)
	public void testFirmwareWiseDevicesGraphTableHeaders() {
		final List<String> FIRMWARE_WISE_DEVICES_HEADERS = List.of("UIN NO.", "IMEI NO.", "ICCID.",
				"MODEL NAME.", "VERSION.",
				"ACTION");
		executor.executeTest("Test Firmware Wise Devices Graph Legend", FIRMWARE_WISE_DEVICES_HEADERS,
				deviceDashboardPage::validateFirmwareWiseDevicesGraphTableHeaders);
	}

	@Test(priority = 71)
	public void testFirmwareWiseDevicesTableButtons() {
		executor.executeTest("Test total dispatched devices table buttons", true,
				deviceDashboardPage::validateFirmwareWiseDevicesTableViewButtons);
	}

	@Test(priority = 72)
	public void testIsSearchButtonVisibleOnFirmwareWiseDevicesTable() {
		executor.executeTest("Search Button Visible", "Search Button Visible",
				() -> deviceDashboardPage.isSearchButtonVisible() ? "Search Button Visible"
						: "Search Button Not Visible");
	}

	// validate search button is enabled
	@Test(priority = 73)
	public void testIsSearchButtonEnabledOnFirmwareWiseDevicesTable() {
		executor.executeTest("Search Button Enabled", "Search Button Enabled",
				() -> deviceDashboardPage.isSearchButtonEnabled() ? "Search Button Enabled"
						: "Search Button Not Enabled");
	}

	// validate search input is visible
	@Test(priority = 74)
	public void testIsSearchInputVisibleOnFirmwareWiseDevicesTable() {
		executor.executeTest("Search Input Visible", "Search Input Visible",
				() -> deviceDashboardPage.isSearchInputVisible() ? "Search Input Visible"
						: "Search Input Not Visible");
	}

	// validate search input is enabled
	@Test(priority = 75)
	public void testIsSearchInputEnabledOnFirmwareWiseDevicesTable() {
		executor.executeTest("Search Input Enabled", "Search Input Enabled",
				() -> deviceDashboardPage.isSearchInputEnabled() ? "Search Input Enabled"
						: "Search Input Not Enabled");
	}

	// validate the search functionality
	@Test(priority = 76)
	public void testDeviceSearchOnFirmwareWiseDevicesTable() {
		executor.executeTest("Test search functionality", true, deviceDashboardPage::searchDevice);
	}

	// validate the export button is visible
	@Test(priority = 77)
	public void testIsExportButtonVisibleOnFirmwareWiseDevicesTable() {
		executor.executeTest("Test export button visible", true, deviceDashboardPage::isExportButtonVisible);
	}

	// validate the export button is enabled
	@Test(priority = 78)
	public void testIsExportButtonEnabledOnFirmwareWiseDevicesTable() {
		executor.executeTest("Test export button enabled", true, deviceDashboardPage::isExportButtonEnabled);
	}

	// validate the export functionality
	@Test(priority = 79)
	public void testExportFunctionalityOnFirmwareWiseDevicesTable() {
		executor.executeTest("Test export functionality", true, comm::validateExportButton);
	}

	// for this dropdown I have to just check the dropdown is clickable or not and
	// not checking all the options just the dropdown is clickable and visible on
	// the above of the table
	@Test(priority = 80)
	public void testSelectFirmwareVersionDropdown() {
		executor.executeTest("Test select Firmware Version dropdown", true,
				deviceDashboardPage::isFirmwareVersionDropdownVisibleAndClickable);
	}

	// validate the table data of the Firmware Wise Devices table
	@Test(priority = 81)
	public void testValidateTableDateOfFirmwareWiseDevicesTable() {
		executor.executeTest("Test table data of Firmware Wise Devices table", true,
				deviceDashboardPage::validateTableDataOfFirmwareWiseDevicesTable);
	}

	// validate the pagination of the firmware wise devices table
	@Test(priority = 82)
	public void testPaginationOnFirmwareWiseDevicesTable() {
		executor.executeTest("Verify Pagination", "Pagination Working", () -> {
			comm.checkPagination();
			return "Pagination Working";
		});
	}

	@Test(priority = 99)
	public void testVersion() {
		executor.executeTest("Verify Application Version", Constants.EXP_VERSION_TEXT, assertion::checkVersion);
	}

	@Test(priority = 100)
	public void testCopyright() {
		executor.executeTest("Verify Copyright", Constants.EXP_COPYRIGHT_TEXT, assertion::checkCopyright);
	}

	@AfterClass
	public void tearDownAssertions() {
		softAssert.assertAll();
	}
}

