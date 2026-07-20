package com.aepl.sam.tests;

import java.util.List;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aepl.sam.base.TestBase;

import com.aepl.sam.utils.PageActionsUtil;
import com.aepl.sam.utils.PageAssertionsUtil;
import com.aepl.sam.pages.DeviceDashboardPage;
import com.aepl.sam.utils.Constants;
import com.aepl.sam.utils.ExcelUtility;

@Test(groups = {"sampark", "lct", "trio", "swaraj", "atcu", "regression"})
public class DeviceDashboardPageTest extends TestBase {
	private static final String DEVICE_DASHBOARD_EXCEL_SHEET = "DeviceDashboardTests";

	private DeviceDashboardPage deviceDashboardPage;
	private PageActionsUtil comm;
	private PageAssertionsUtil assertion;
	private ExcelUtility excelUtility;
	private SoftAssert softAssert;
	private Executor executor;

	@Override
	@BeforeMethod(alwaysRun = true)
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

	@Test
	public void testCompanyLogo() {
		executor.executeTest("Verify Company Logo", true, comm::verifyWebpageLogo);
	}

	@Test
	public void testPageTitle() {
		executor.executeTest("Verify Page Title", Constants.EXP_PAGE_TITLE_TEXT, assertion::verifyPageTitle);
	}

	@Test
	public void testClickNavBar() {
		executor.executeTest("Verify Navigation Bar Link", "Link is verified", deviceDashboardPage::clickNavBar);
	}

	@Test
	public void testValidateComponents() {
		executor.executeTest("Validate Components", Constants.EXP_VALIDATE_COMPONENTS_TEXT, assertion::validateComponents);
	}

	@Test
	public void testValidateButtons() {
		executor.executeTest("Validate Buttons", Constants.EXP_VALIDATE_BUTTONS_TEXT, assertion::validateButtons);
	}

	// Validate all cards
	@Test
	public void testValidateAllCards() {
		executor.executeTest("Test all cards visible", true, deviceDashboardPage::validateCardAreVisible);
	}

	// Validate the graph visibility
	@Test
	public void testValidateGraph() {
		executor.executeTest("Test graph visible", true, deviceDashboardPage::validateGraphIsVisible);
	}

	// validate the graph click and headers matches with the tables headers
	@Test
	public void testValidateGraphClick() {
		executor.executeTest("Test graph click and headers", true, deviceDashboardPage::validateGraphClick);
	}
	// =========================================================
	// ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã…Â  Total Production Devices Table
	// =========================================================

	@Test
	public void testTotalProductionDevicesTableHeaders() {
		final List<String> TOTAL_PRODUCTION_DEVICES_HEADERS = List.of("UIN NO.", "IMEI NO.", "ICCID NO.",
				"MODEL NAME.",
				"ACTION");
		executor.executeTest("Total Production Devices Headers", TOTAL_PRODUCTION_DEVICES_HEADERS,
				deviceDashboardPage::validateTotalProductionDevicesTableHeaders);
	}

	@Test
	public void testTotalProductionDevicesTableButtons() {
		executor.executeTest("Total Production Devices Table Buttons", true,
				deviceDashboardPage::validateTotalProductionDevicesTableButtons);
	}

	@Test
	public void testIsSearchButtonVisibleOnTotalProductionDevicesTable() {
		executor.executeTest("Search Button Visible", "Search Button Visible",
				() -> deviceDashboardPage.isSearchButtonVisible() ? "Search Button Visible"
						: "Search Button Not Visible");
	}

	@Test
	public void testIsSearchButtonEnabledOnTotalProductionDevicesTable() {
		executor.executeTest("Search Button Enabled", "Search Button Enabled",
				() -> deviceDashboardPage.isSearchButtonEnabled() ? "Search Button Enabled"
						: "Search Button Not Enabled");
	}

	@Test
	public void testIsSearchInputVisibleOnTotalProductionDevicesTable() {
		executor.executeTest("Search Input Visible", "Search Input Visible",
				() -> deviceDashboardPage.isSearchInputVisible() ? "Search Input Visible"
						: "Search Input Not Visible");
	}

	@Test
	public void testIsSearchInputEnabledOnTotalProductionDevicesTable() {
		executor.executeTest("Search Input Enabled", "Search Input Enabled",
				() -> deviceDashboardPage.isSearchInputEnabled() ? "Search Input Enabled"
						: "Search Input Not Enabled");
	}

	@Test
	public void testDeviceSearchOnTotalProductionDevicesTable() {
		executor.executeTest("Search Device", true, deviceDashboardPage::searchDevice);
	}

	@Test
	public void testIsExportButtonVisibleOnTotalProductionDevicesTable() {
		executor.executeTest("Export Button Visible", true, deviceDashboardPage::isExportButtonVisible);
	}

	@Test
	public void testIsExportButtonEnabledOnTotalProductionDevicesTable() {
		executor.executeTest("Export Button Enabled", true, deviceDashboardPage::isExportButtonEnabled);
	}

	@Test
	public void testExportFunctionalityOnTotalProductionDevicesTable() {
		executor.executeTest("Export Functionality", true, comm::validateExportButton);
	}

	@Test
	public void testPaginationOnTotalProductionDevicesTable() {
		executor.executeTest("Verify Pagination", "Pagination Working", () -> {
			comm.checkPagination();
			return "Pagination Working";
		});
	}

	// **** Total Dispatched Devices Table ****//

	@Test
	public void testTotalDispatchedDevicesTableHeaders() {
		final List<String> TOTAL_DISPATCHED_DEVICES_HEADERS = List.of("UIN NO.", "IMEI NO.", "ICCID NO.",
				"MODEL NAME.",
				"CUSTOMER NAME", "ACTION");
		executor.executeTest("Test total dispatched devices table", TOTAL_DISPATCHED_DEVICES_HEADERS,
				deviceDashboardPage::validateTotalDispatchedDevicesTableHeaders);
	}

	@Test
	public void testTotalDispatchedDevicesTableButtons() {
		executor.executeTest("Test total dispatched devices table buttons", true,
				deviceDashboardPage::validateTotalDispatchedDevicesTableButtons);
	}

	@Test
	public void testIsSearchButtonVisibleOnTotalDispatchedDevicesTable() {
		executor.executeTest("Search Button Visible", "Search Button Visible",
				() -> deviceDashboardPage.isSearchButtonVisible() ? "Search Button Visible"
						: "Search Button Not Visible");
	}

	// validate search button is enabled
	@Test
	public void testIsSearchButtonEnabledOnTotalDispatchedDevicesTable() {
		executor.executeTest("Search Button Enabled", "Search Button Enabled",
				() -> deviceDashboardPage.isSearchButtonEnabled() ? "Search Button Enabled"
						: "Search Button Not Enabled");
	}

	// validate search input is visible
	@Test
	public void testIsSearchInputVisibleOnTotalDispatchedDevicesTable() {
		executor.executeTest("Search Input Visible", "Search Input Visible",
				() -> deviceDashboardPage.isSearchInputVisible() ? "Search Input Visible"
						: "Search Input Not Visible");
	}

	// validate search input is enabled
	@Test
	public void testIsSearchInputEnabledOnTotalDispatchedDevicesTable() {
		executor.executeTest("Search Input Enabled", "Search Input Enabled",
				() -> deviceDashboardPage.isSearchInputEnabled() ? "Search Input Enabled"
						: "Search Input Not Enabled");
	}

	// validate the search functionality
	@Test
	public void testDeviceSearchOnTotalDispatchedDevicesTable() {
		executor.executeTest("Test search functionality", true, deviceDashboardPage::searchDevice);
	}

	// validate the export button is visible
	@Test
	public void testIsExportButtonVisibleOnTotalDispatchedDevicesTable() {
		executor.executeTest("Test export button visible", true, deviceDashboardPage::isExportButtonVisible);
	}

	// validate the export button is enabled
	@Test
	public void testIsExportButtonEnabledOnTotalDispatchedDevicesTable() {
		executor.executeTest("Test export button enabled", true, deviceDashboardPage::isExportButtonEnabled);
	}

	// validate the export functionality
	@Test
	public void testExportFunctionalityOnTotalDispatchedDevicesTable() {
		executor.executeTest("Test export functionality", true, comm::validateExportButton);
	}

	// validate the pagination of the total production devices table
	@Test
	public void testPaginationOnTotalDispatchedDevicesTable() {
		executor.executeTest("Verify Pagination", "Pagination Working", () -> {
			comm.checkPagination();
			return "Pagination Working";
		});
	}

	// **** Total Installed Devices Table ****//
	@Test
	public void testTotalInstalledDevicesTableHeaders() {
		final List<String> TOTAL_INSTALLED_DEVICES_HEADERS = List.of("UIN NO.", "IMEI NO.", "ICCID NO.",
				"CHASSIS NO.",
				"MODEL NAME.", "CUSTOMER NAME", "ACTION");
		executor.executeTest("Test total dispatched devices table", TOTAL_INSTALLED_DEVICES_HEADERS,
				deviceDashboardPage::validateTotalInstalledDevicesTableHeaders);
	}

	@Test
	public void testTotalInstalledDevicesTableButtons() {
		executor.executeTest("Test total dispatched devices table buttons", true,
				deviceDashboardPage::validateTotalInstalledDevicesTableButtons);
	}

	@Test
	public void testIsSearchButtonVisibleOnTotalInstalledDevicesTable() {
		executor.executeTest("Search Button Visible", "Search Button Visible",
				() -> deviceDashboardPage.isSearchButtonVisible() ? "Search Button Visible"
						: "Search Button Not Visible");
	}

	// validate search button is enabled
	@Test
	public void testIsSearchButtonEnabledOnTotalInstalledDevicesTable() {
		executor.executeTest("Search Button Enabled", "Search Button Enabled",
				() -> deviceDashboardPage.isSearchButtonEnabled() ? "Search Button Enabled"
						: "Search Button Not Enabled");
	}

	// validate search input is visible
	@Test
	public void testIsSearchInputVisibleOnTotalInstalledDevicesTable() {
		executor.executeTest("Search Input Visible", "Search Input Visible",
				() -> deviceDashboardPage.isSearchInputVisible() ? "Search Input Visible"
						: "Search Input Not Visible");
	}

	// validate search input is enabled
	@Test
	public void testIsSearchInputEnabledOnTotalInstalledDevicesTable() {
		executor.executeTest("Search Input Enabled", "Search Input Enabled",
				() -> deviceDashboardPage.isSearchInputEnabled() ? "Search Input Enabled"
						: "Search Input Not Enabled");
	}

	// validate the search functionality
	@Test
	public void testDeviceSearchOnTotalInstalledDevicesTable() {
		executor.executeTest("Test search functionality", true, deviceDashboardPage::searchDevice);
	}

	// validate the export button is visible
	@Test
	public void testIsExportButtonVisibleOnTotalInstalledDevicesTable() {
		executor.executeTest("Test export button visible", true, deviceDashboardPage::isExportButtonVisible);
	}

	// validate the export button is enabled
	@Test
	public void testIsExportButtonEnabledOnTotalInstalledDevicesTable() {
		executor.executeTest("Test export button enabled", true, deviceDashboardPage::isExportButtonEnabled);
	}

	// validate the export functionality
	@Test
	public void testExportFunctionalityOnTotalInstalledDevicesTable() {
		executor.executeTest("Test export functionality", true, comm::validateExportButton);
	}

	// validate the pagination of the total production devices table
	@Test
	public void testPaginationOnTotalInstalledDevicesTable() {
		executor.executeTest("Verify Pagination", "Pagination Working", () -> {
			comm.checkPagination();
			return "Pagination Working";
		});
	}

	// **** Total Discarded Devices Table ****//
	@Test
	public void testTotalDiscardedDevicesTableHeaders() {
		final List<String> TOTAL_DISCARDED_DEVICES_HEADERS = List.of("UIN NO.", "IMEI NO.", "ICCID NO.",
				"CHASSIS NO.",
				"MODEL NAME.", "INSTALLED AT", "DISCARDED AT", "ACTION");
		executor.executeTest("Test total Discarded devices table", TOTAL_DISCARDED_DEVICES_HEADERS,
				deviceDashboardPage::validateTotalDiscardedDevicesTableHeaders);
	}

	@Test
	public void testTotalDiscardedDevicesTableButtons() {
		executor.executeTest("Test total dispatched devices table buttons", true,
				deviceDashboardPage::validateTotalDiscardedDevicesTableButtons);
	}

	@Test
	public void testIsSearchButtonVisibleOnTotalDiscardedDevicesTable() {
		executor.executeTest("Search Button Visible", "Search Button Visible",
				() -> deviceDashboardPage.isSearchButtonVisible() ? "Search Button Visible"
						: "Search Button Not Visible");
	}

	// validate search button is enabled
	@Test
	public void testIsSearchButtonEnabledOnTotalDiscardedDevicesTable() {
		executor.executeTest("Search Button Enabled", "Search Button Enabled",
				() -> deviceDashboardPage.isSearchButtonEnabled() ? "Search Button Enabled"
						: "Search Button Not Enabled");
	}

	// validate search input is visible
	@Test
	public void testIsSearchInputVisibleOnTotalDiscardedDevicesTable() {
		executor.executeTest("Search Input Visible", "Search Input Visible",
				() -> deviceDashboardPage.isSearchInputVisible() ? "Search Input Visible"
						: "Search Input Not Visible");
	}

	// validate search input is enabled
	@Test
	public void testIsSearchInputEnabledOnTotalDiscardedDevicesTable() {
		executor.executeTest("Search Input Enabled", "Search Input Enabled",
				() -> deviceDashboardPage.isSearchInputEnabled() ? "Search Input Enabled"
						: "Search Input Not Enabled");
	}

	// validate the search functionality
	@Test
	public void testDeviceSearchOnTotalDiscardedDevicesTable() {
		executor.executeTest("Test search functionality", true, deviceDashboardPage::searchDevice);
	}

	// validate the export button is visible
	@Test
	public void testIsExportButtonVisibleOnTotalDiscardedDevicesTable() {
		executor.executeTest("Test export button visible", true, deviceDashboardPage::isExportButtonVisible);
	}

	// validate the export button is enabled
	@Test
	public void testIsExportButtonEnabledOnTotalDiscardedDevicesTable() {
		executor.executeTest("Test export button enabled", true, deviceDashboardPage::isExportButtonEnabled);
	}

	// validate the export functionality
	@Test
	public void testExportFunctionalityOnTotalDiscardedDevicesTable() {
		executor.executeTest("Test export functionality", true, comm::validateExportButton);
	}

	// validate the pagination of the total production devices table
	@Test
	public void testPaginationOnTotalDiscardedDevicesTable() {
		executor.executeTest("Verify Pagination", "Pagination Working", () -> {
			comm.checkPagination();
			return "Pagination Working";
		});
	}

	// **** Device Activity Overview Graph **** //
	@Test
	public void testDeviceActivityOverviewGraphIsVisible() {
		executor.executeTest("Test Device Activity Overview Graph is visible", true,
				deviceDashboardPage::isDeviceActivityOverviewGraphVisible);
	}

	@Test
	public void testDeviceActivityOverviewGraphClick() {
		executor.executeTest("Test Device Activity Overview Graph click", true,
				deviceDashboardPage::validateDeviceActivityOverviewGraphClick);
	}

	@Test
	public void testDeviceActivityOverviewGraphTableHeaders() {
		final List<String> DEVICE_ACTIVITY_OVERVIEW_HEADERS = List.of("UIN NO.", "IMEI NO.", "ICCID NO.",
				"MODEL NAME.",
				"LOG IN TIME", "ACTION");
		executor.executeTest("Test Device Activity Overview Graph Legend", DEVICE_ACTIVITY_OVERVIEW_HEADERS,
				deviceDashboardPage::validateDeviceActivityOverviewGraphTableHeaders);
	}

	@Test
	public void testDeviceActivityOverviewTableButtons() {
		executor.executeTest("Test total dispatched devices table buttons", true,
				deviceDashboardPage::validateDeviceActivityOverviewTableButtons);
	}

	@Test
	public void testIsSearchButtonVisibleOnDeviceActivityOverviewTable() {
		executor.executeTest("Search Button Visible", "Search Button Visible",
				() -> deviceDashboardPage.isSearchButtonVisible() ? "Search Button Visible"
						: "Search Button Not Visible");
	}

	// validate search button is enabled
	@Test
	public void testIsSearchButtonEnabledOnDeviceActivityOverviewTable() {
		executor.executeTest("Search Button Enabled", "Search Button Enabled",
				() -> deviceDashboardPage.isSearchButtonEnabled() ? "Search Button Enabled"
						: "Search Button Not Enabled");
	}

	// validate search input is visible
	@Test
	public void testIsSearchInputVisibleOnDeviceActivityOverviewTable() {
		executor.executeTest("Search Input Visible", "Search Input Visible",
				() -> deviceDashboardPage.isSearchInputVisible() ? "Search Input Visible"
						: "Search Input Not Visible");
	}

	// validate search input is enabled
	@Test
	public void testIsSearchInputEnabledOnDeviceActivityOverviewTable() {
		executor.executeTest("Search Input Enabled", "Search Input Enabled",
				() -> deviceDashboardPage.isSearchInputEnabled() ? "Search Input Enabled"
						: "Search Input Not Enabled");
	}

	// validate the search functionality
	@Test
	public void testDeviceSearchOnDeviceActivityOverviewTable() {
		executor.executeTest("Test search functionality", true, deviceDashboardPage::searchDevice);
	}

	// validate the export button is visible
	@Test
	public void testIsExportButtonVisibleOnDeviceActivityOverviewTable() {
		executor.executeTest("Test export button visible", true, deviceDashboardPage::isExportButtonVisible);
	}

	// validate the export button is enabled
	@Test
	public void testIsExportButtonEnabledOnDeviceActivityOverviewTable() {
		executor.executeTest("Test export button enabled", true, deviceDashboardPage::isExportButtonEnabled);
	}

	// validate the export functionality
	@Test
	public void testExportFunctionalityOnDeviceActivityOverviewTable() {
		executor.executeTest("Test export functionality", true, comm::validateExportButton);
	}

	@Test
	public void testSelectActivityDurationDropdown() {
		final List<String> ACTIVITY_DURATION_DROPDOWN_OPTIONS = List.of("All", "Today", "Five Days",
				"Ten Days", "Fifteen Days",
				"More Than Fifteen", "Not Active");
		executor.executeTest("Test select activity duration dropdown", ACTIVITY_DURATION_DROPDOWN_OPTIONS,
				deviceDashboardPage::selectActivityDurationDropdown);
	}

	// Validate the table data of the Device Activity Overview table
	@Test
	public void testValidateTableDateOfDeviceActivityOverviewTable() {
		executor.executeTest("Test table data of Device Activity Overview table", true,
				deviceDashboardPage::validateTableDataOfDeviceActivityOverviewTable);
	}

	// validate that the view button is enabled in the Device Activity Overview
	// table
	@Test
	public void testIsViewButtonEnabledInDeviceActivityOverviewTable() {
		executor.executeTest("Test view button is enabled in Device Activity Overview table", true,
				deviceDashboardPage::isViewButtonEnabledInDeviceActivityOverviewTable);
	}

	// **** Firmware Wise Devices graph **** //
	@Test
	public void testFirmwareWiseDevicesGraphIsVisible() {
		executor.executeTest("Test Firmware Wise Devices Graph is visible", true,
				deviceDashboardPage::isFirmwareWiseDevicesGraphVisible);
	}

	@Test
	public void testFirmwareWiseDevicesGraphClick() {
		executor.executeTest("Test Firmware Wise Devices Graph click", true,
				deviceDashboardPage::validateFirmwareWiseDevicesGraphClick);
	}

	@Test
	public void testFirmwareWiseDevicesGraphTableHeaders() {
		final List<String> FIRMWARE_WISE_DEVICES_HEADERS = List.of("UIN NO.", "IMEI NO.", "ICCID.",
				"MODEL NAME.", "VERSION.",
				"ACTION");
		executor.executeTest("Test Firmware Wise Devices Graph Legend", FIRMWARE_WISE_DEVICES_HEADERS,
				deviceDashboardPage::validateFirmwareWiseDevicesGraphTableHeaders);
	}

	@Test
	public void testFirmwareWiseDevicesTableButtons() {
		executor.executeTest("Test total dispatched devices table buttons", true,
				deviceDashboardPage::validateFirmwareWiseDevicesTableViewButtons);
	}

	@Test
	public void testIsSearchButtonVisibleOnFirmwareWiseDevicesTable() {
		executor.executeTest("Search Button Visible", "Search Button Visible",
				() -> deviceDashboardPage.isSearchButtonVisible() ? "Search Button Visible"
						: "Search Button Not Visible");
	}

	// validate search button is enabled
	@Test
	public void testIsSearchButtonEnabledOnFirmwareWiseDevicesTable() {
		executor.executeTest("Search Button Enabled", "Search Button Enabled",
				() -> deviceDashboardPage.isSearchButtonEnabled() ? "Search Button Enabled"
						: "Search Button Not Enabled");
	}

	// validate search input is visible
	@Test
	public void testIsSearchInputVisibleOnFirmwareWiseDevicesTable() {
		executor.executeTest("Search Input Visible", "Search Input Visible",
				() -> deviceDashboardPage.isSearchInputVisible() ? "Search Input Visible"
						: "Search Input Not Visible");
	}

	// validate search input is enabled
	@Test
	public void testIsSearchInputEnabledOnFirmwareWiseDevicesTable() {
		executor.executeTest("Search Input Enabled", "Search Input Enabled",
				() -> deviceDashboardPage.isSearchInputEnabled() ? "Search Input Enabled"
						: "Search Input Not Enabled");
	}

	// validate the search functionality
	@Test
	public void testDeviceSearchOnFirmwareWiseDevicesTable() {
		executor.executeTest("Test search functionality", true, deviceDashboardPage::searchDevice);
	}

	// validate the export button is visible
	@Test
	public void testIsExportButtonVisibleOnFirmwareWiseDevicesTable() {
		executor.executeTest("Test export button visible", true, deviceDashboardPage::isExportButtonVisible);
	}

	// validate the export button is enabled
	@Test
	public void testIsExportButtonEnabledOnFirmwareWiseDevicesTable() {
		executor.executeTest("Test export button enabled", true, deviceDashboardPage::isExportButtonEnabled);
	}

	// validate the export functionality
	@Test
	public void testExportFunctionalityOnFirmwareWiseDevicesTable() {
		executor.executeTest("Test export functionality", true, comm::validateExportButton);
	}

	// for this dropdown I have to just check the dropdown is clickable or not and
	// not checking all the options just the dropdown is clickable and visible on
	// the above of the table
	@Test
	public void testSelectFirmwareVersionDropdown() {
		executor.executeTest("Test select Firmware Version dropdown", true,
				deviceDashboardPage::isFirmwareVersionDropdownVisibleAndClickable);
	}

	// validate the table data of the Firmware Wise Devices table
	@Test
	public void testValidateTableDateOfFirmwareWiseDevicesTable() {
		executor.executeTest("Test table data of Firmware Wise Devices table", true,
				deviceDashboardPage::validateTableDataOfFirmwareWiseDevicesTable);
	}

	// validate the pagination of the firmware wise devices table
	@Test
	public void testPaginationOnFirmwareWiseDevicesTable() {
		executor.executeTest("Verify Pagination", "Pagination Working", () -> {
			comm.checkPagination();
			return "Pagination Working";
		});
	}

	@Test
	public void testVersion() {
		executor.executeTest("Verify Application Version", Constants.EXP_VERSION_TEXT, assertion::checkVersion);
	}

	@Test
	public void testCopyright() {
		executor.executeTest("Verify Copyright", Constants.EXP_COPYRIGHT_TEXT, assertion::checkCopyright);
	}

	@AfterMethod(alwaysRun = true)
	public void tearDownAssertions() {
		softAssert.assertAll();
	}
}



