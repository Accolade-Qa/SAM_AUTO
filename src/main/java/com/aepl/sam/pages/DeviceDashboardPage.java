package com.aepl.sam.pages;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aepl.sam.utils.Constants;
import com.aepl.sam.locators.DeviceDashboardPageLocators;
import com.aepl.sam.utils.MouseActions;
import com.aepl.sam.utils.PageActionsUtil;
import com.aepl.sam.utils.TableUtils;

public class DeviceDashboardPage extends DeviceDashboardPageLocators {
	private WebDriver driver;
	private WebDriverWait wait;
	private PageActionsUtil comm;
	private TableUtils tableUtils;

	private static final Logger logger = LogManager.getLogger(DeviceDashboardPage.class);

	public DeviceDashboardPage(WebDriver driver, WebDriverWait wait, MouseActions action) {
		this.driver = driver;
		this.wait = wait;
		this.comm = new PageActionsUtil(driver, wait);
		this.tableUtils = new TableUtils(wait);
	}

	public List<String> cardCounts = new ArrayList<>();

	public String clickNavBar() {
		if (driver.findElement(DEVICE_DASHBOARD).isDisplayed() && driver.findElement(DEVICE_DASHBOARD).isEnabled()) {
			wait.until(ExpectedConditions.visibilityOfElementLocated(DEVICE_DASHBOARD)).click();
			return "Link is verified";
		}
		return "No link is visible";
	}

	public String verifyDashPageTitle() throws InterruptedException {
		String expectedTitle = "Device Dashboard";
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollTo(0, 0);");
		Thread.sleep(500);

		WebElement titleElement = wait.until(ExpectedConditions.visibilityOfElementLocated(DEVICE_DASHBOARD_TITLE));
		js.executeScript("arguments[0].style.border='3px solid purple'", titleElement);

		String actualTitle = titleElement.getText().trim();
		if (!actualTitle.equalsIgnoreCase(expectedTitle)) {
			throw new RuntimeException(
					"Page title does not match. Expected: " + expectedTitle + ", but found: " + actualTitle);
		}
		return actualTitle;
	}

	public String verifyAndClickKPITotalProDevWithCount() {
		String expectedTitle = "TOTAL PRODUCTION DEVICES";
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0,-1500)");

		try {
			WebElement titleElement = wait
					.until(ExpectedConditions.visibilityOfElementLocated(DEVICE_DASHBOARD_TOTALPRODUCTIONDEVICESKPI));
			WebElement countElement = wait.until(
					ExpectedConditions.visibilityOfElementLocated(DEVICE_DASHBOARD_TOTALPRODUCTIONDEVICESKPICOUNT));
			WebElement tableElement = wait
					.until(ExpectedConditions.visibilityOfElementLocated(DEVICEDASHBOARDKPITABLE));

			js.executeScript("window.scrollBy(0,300)");
			js.executeScript("arguments[0].style.border='3px solid purple'", titleElement);
			js.executeScript("arguments[0].style.border='3px solid cyan'", countElement);
			js.executeScript("arguments[0].style.border='3px solid blue'", tableElement);

			titleElement.click();

			String actualTitle = titleElement.getText().trim();
			String actualCount = countElement.getText().trim();

			if (!actualTitle.equalsIgnoreCase(expectedTitle)) {
				throw new AssertionError("Page title does not match. Expected: '" + expectedTitle + "', but found: '"
						+ actualTitle + "'");
			}

			String tablename = tableElement.getText().trim();

			return "KPI Title: " + actualTitle + ", KPI Count: " + actualCount + ",KPI Table: " + tablename;

		} catch (NoSuchElementException ne) {
			throw new RuntimeException("Element not found: " + DEVICE_DASHBOARD_TOTALPRODUCTIONDEVICESKPI, ne);
		} catch (Exception e) {
			throw new RuntimeException("Unexpected error while verifying KPI title and count.", e);
		}
	}

	public String verifyAndClickKPITotalDisDevWithCount() throws InterruptedException {
		String expectedTitle = "TOTAL DISPATCHED DEVICES";
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollTo(0, 0);");
		Thread.sleep(500);

		try {
			WebElement titleElement = wait
					.until(ExpectedConditions.visibilityOfElementLocated(DEVICE_DASHBOARD_TOTALDISPATCHEDDEVICESKPI));
			WebElement countElement = wait.until(
					ExpectedConditions.visibilityOfElementLocated(DEVICE_DASHBOARD_TOTALDISPATCHEDDEVICESKPICOUNT));
			WebElement tableElement = wait
					.until(ExpectedConditions.visibilityOfElementLocated(DEVICEDASHBOARDKPITABLE));

			js.executeScript("arguments[0].style.border='3px solid purple'", titleElement);
			js.executeScript("arguments[0].style.border='3px solid cyan'", countElement);
			js.executeScript("arguments[0].style.border='3px solid blue'", tableElement);

			titleElement.click();
			String actualTitle = titleElement.getText().trim();
			String actualCount = countElement.getText().trim();

			if (!actualTitle.equalsIgnoreCase(expectedTitle)) {
				throw new AssertionError("Page title does not match. Expected: '" + expectedTitle + "', but found: '"
						+ actualTitle + "'");
			}

			String tablename = tableElement.getText().trim();
			return "KPI Title: " + actualTitle + ", KPI Count: " + actualCount + ",KPI Table: " + tablename;
		} catch (NoSuchElementException ne) {
			throw new RuntimeException("Element not found: " + DEVICE_DASHBOARD_TOTALDISPATCHEDDEVICESKPI, ne);
		} catch (Exception e) {
			throw new RuntimeException("Unexpected error while verifying KPI title and count.", e);
		}
	}

	public String verifyAndClickKPITotalInsDevWithCount() {
		String expectedTitle = "TOTAL INSTALLED DEVICES";
		JavascriptExecutor js = (JavascriptExecutor) driver;
		try {
			WebElement titleElement = wait
					.until(ExpectedConditions.visibilityOfElementLocated(DEVICE_DASHBOARD_TOTALINSTALLEDDEVICESKPI));
			WebElement countElement = wait.until(
					ExpectedConditions.visibilityOfElementLocated(DEVICE_DASHBOARD_TOTALINSTALLEDDEVICESKPICOUNT));
			WebElement tableElement = wait
					.until(ExpectedConditions.visibilityOfElementLocated(DEVICEDASHBOARDKPITABLE));

			js.executeScript("arguments[0].style.border='3px solid purple'", titleElement);
			js.executeScript("arguments[0].style.border='3px solid cyan'", countElement);
			js.executeScript("arguments[0].style.border='3px solid blue'", tableElement);

			titleElement.click();
			String actualTitle = titleElement.getText().trim();
			String actualCount = countElement.getText().trim();

			if (!actualTitle.equalsIgnoreCase(expectedTitle)) {
				throw new AssertionError("Page title does not match. Expected: '" + expectedTitle + "', but found: '"
						+ actualTitle + "'");
			}

			String tablename = tableElement.getText().trim();
			return "KPI Title: " + actualTitle + ", KPI Count: " + actualCount + ",KPI Table: " + tablename;
		} catch (NoSuchElementException ne) {
			throw new RuntimeException("Element not found: " + DEVICE_DASHBOARD_TOTALINSTALLEDDEVICESKPI, ne);
		} catch (Exception e) {
			throw new RuntimeException("Unexpected error while verifying KPI title and count.", e);
		}
	}

	public String verifyAndClickKPITotalDiscardDevWithCount() {
		String expectedTitle = "TOTAL DISCARDED DEVICES";
		JavascriptExecutor js = (JavascriptExecutor) driver;
		try {
			WebElement titleElement = wait
					.until(ExpectedConditions.visibilityOfElementLocated(DEVICE_DASHBOARD_TOTALDISCARDEDDEVICESKPI));
			WebElement countElement = wait.until(
					ExpectedConditions.visibilityOfElementLocated(DEVICE_DASHBOARD_TOTALDISCARDEDDEVICESKPICOUNT));
			WebElement tableElement = wait
					.until(ExpectedConditions.visibilityOfElementLocated(DEVICEDASHBOARDKPITABLE));

			js.executeScript("arguments[0].style.border='3px solid purple'", titleElement);
			js.executeScript("arguments[0].style.border='3px solid cyan'", countElement);
			js.executeScript("arguments[0].style.border='3px solid blue'", tableElement);

			titleElement.click();
			String actualTitle = titleElement.getText().trim();
			String actualCount = countElement.getText().trim();

			if (!actualTitle.equalsIgnoreCase(expectedTitle)) {
				throw new AssertionError("Page title does not match. Expected: '" + expectedTitle + "', but found: '"
						+ actualTitle + "'");
			}

			String tablename = tableElement.getText().trim();
			return "KPI Title: " + actualTitle + ", KPI Count: " + actualCount + ",KPI Table: " + tablename;
		} catch (NoSuchElementException ne) {
			throw new RuntimeException("Element not found: " + DEVICE_DASHBOARD_TOTALDISCARDEDDEVICESKPI, ne);
		} catch (Exception e) {
			throw new RuntimeException("Unexpected error while verifying KPI title and count.", e);
		}
	}

	public String clickAndEnterTextInSearchBoxProd() {
		String expectedIMEI = "867409079963166";
		String expectedICCID = "89916431144821180029";
		String expectedUIN = "ACON4IA202200096315";
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

			List<WebElement> navBarLinks = wait
					.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(DEVICE_DASHBOARD_SEARCHBOX));
			boolean isClicked = false;

			for (WebElement link : navBarLinks) {
				js.executeScript("arguments[0].style.border='5px solid cyan'", link);
				if (link.getDomAttribute("placeholder").equalsIgnoreCase("Search")
						|| link.getTagName().equalsIgnoreCase("input")) {
					link.click();
					isClicked = true;
					break;
				}
			}
			if (!isClicked) {
				throw new RuntimeException("Failed to find and click the search box.");
			}
			WebElement titleElement = wait
					.until(ExpectedConditions.visibilityOfElementLocated(DEVICE_DASHBOARD_TOTALPRODUCTIONDEVICESKPI));
			titleElement.click();
			Thread.sleep(2000);

			WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(DEVICE_DASHBOARD_SEARCHBOX));

			searchBox.clear();
			searchBox.sendKeys(expectedIMEI, Keys.ENTER);

			searchBox.clear();
			searchBox.sendKeys(expectedICCID, Keys.ENTER);

			searchBox.clear();
			searchBox.sendKeys(expectedUIN, Keys.ENTER);

			return expectedIMEI + " | " + expectedICCID + " | " + expectedUIN;

		} catch (TimeoutException te) {
			throw new RuntimeException("Timeout: Search box not found within the expected wait time.", te);
		} catch (NoSuchElementException ne) {
			throw new RuntimeException("Element not found: Search box is missing.", ne);
		} catch (Exception e) {
			throw new RuntimeException("Unexpected error while interacting with the search box.", e);
		}
	}

	public void clickExportBtn1() {
		WebElement titleElement = wait
				.until(ExpectedConditions.visibilityOfElementLocated(DEVICE_DASHBOARD_TOTALDISPATCHEDDEVICESKPI));
		titleElement.click();
	}

	public String clickAndEnterTextInSearchBoxdis() {
		String expectedIMEI = "867409079963166";
		String expectedICCID = "89916431144821180029";
		String expectedUIN = "ACON4SA212240006474";
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			List<WebElement> navBarLinks = wait
					.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(DEVICE_DASHBOARD_SEARCHBOX));
			boolean isClicked = false;

			for (WebElement link : navBarLinks) {
				js.executeScript("arguments[0].style.border='5px solid cyan'", link);
				if (link.getDomAttribute("placeholder").equalsIgnoreCase("Search")
						|| link.getTagName().equalsIgnoreCase("input")) {
					link.click();
					isClicked = true;
					break;
				}
			}
			if (!isClicked) {
				throw new RuntimeException("Failed to find and click the search box.");
			}
			WebElement titleElement = wait
					.until(ExpectedConditions.visibilityOfElementLocated(DEVICE_DASHBOARD_TOTALDISPATCHEDDEVICESKPI));
			titleElement.click();
			Thread.sleep(2000);

			WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(DEVICE_DASHBOARD_SEARCHBOX));

			searchBox.clear();
			searchBox.sendKeys(expectedIMEI, Keys.ENTER);

			searchBox.clear();
			searchBox.sendKeys(expectedICCID, Keys.ENTER);

			searchBox.clear();
			searchBox.sendKeys(expectedUIN, Keys.ENTER);

			return expectedIMEI + " | " + expectedICCID + " | " + expectedUIN;

		} catch (TimeoutException te) {
			throw new RuntimeException("Timeout: Search box not found within the expected wait time.", te);
		} catch (NoSuchElementException ne) {
			throw new RuntimeException("Element not found: Search box is missing.", ne);
		} catch (Exception e) {
			throw new RuntimeException("Unexpected error while interacting with the search box.", e);
		}
	}

	public String clickAndEnterTextInSearchBoxIns() {
		String expectedIMEI = "867409079963166";
		String expectedICCID = "89916431144821180029";
		String expectedUIN = "ACON4IA202200096315";
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			List<WebElement> navBarLinks = wait
					.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(DEVICE_DASHBOARD_SEARCHBOX));
			boolean isClicked = false;

			for (WebElement link : navBarLinks) {
				js.executeScript("arguments[0].style.border='5px solid cyan'", link);
				if (link.getDomAttribute("placeholder").equalsIgnoreCase("Search")
						|| link.getTagName().equalsIgnoreCase("input")) {
					link.click();
					isClicked = true;
					break;
				}
			}
			if (!isClicked) {
				throw new RuntimeException("Failed to find and click the search box.");
			}
			WebElement titleElement = wait
					.until(ExpectedConditions.visibilityOfElementLocated(DEVICE_DASHBOARD_TOTALINSTALLEDDEVICESKPI));
			titleElement.click();
			Thread.sleep(2000);

			WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(DEVICE_DASHBOARD_SEARCHBOX));

			searchBox.clear();
			searchBox.sendKeys(expectedIMEI, Keys.ENTER);

			searchBox.clear();
			searchBox.sendKeys(expectedICCID, Keys.ENTER);

			searchBox.clear();
			searchBox.sendKeys(expectedUIN, Keys.ENTER);

			return expectedIMEI + " | " + expectedICCID + " | " + expectedUIN;

		} catch (TimeoutException te) {
			throw new RuntimeException("Timeout: Search box not found within the expected wait time.", te);
		} catch (NoSuchElementException ne) {
			throw new RuntimeException("Element not found: Search box is missing.", ne);
		} catch (Exception e) {
			throw new RuntimeException("Unexpected error while interacting with the search box.", e);
		}
	}

	public void clickExportBtn() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement exportBtn = wait
					.until(ExpectedConditions.visibilityOfElementLocated(DEVICE_DASHBOARD_EXPORTBTN));

			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].style.border='3px solid purple'", exportBtn);

			exportBtn.click();

			Thread.sleep(1000);
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyPress(KeyEvent.VK_TAB);
			robot.keyRelease(KeyEvent.VK_TAB);
			Thread.sleep(500);
			robot.keyPress(KeyEvent.VK_ENTER);
			Thread.sleep(500);
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);

			Thread.sleep(5000);

		} catch (AWTException | InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void clickExportBtn2() {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement exportBtn = wait
					.until(ExpectedConditions.visibilityOfElementLocated(DEVICE_DASHBOARD_EXPORTBTN2));

			js.executeScript("window.scrollTo(500, 0);");
			js.executeScript("arguments[0].scrollIntoView(true);", exportBtn);
			Thread.sleep(500);

			js.executeScript("arguments[0].style.border='3px solid purple'", exportBtn);

			try {
				exportBtn.click();
			} catch (ElementClickInterceptedException e) {
				js.executeScript("arguments[0].click();", exportBtn);
			}

			Thread.sleep(1000);
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);

			robot.keyPress(KeyEvent.VK_TAB);
			robot.keyRelease(KeyEvent.VK_TAB);
			Thread.sleep(500);

			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);

			Thread.sleep(500);
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);

			Thread.sleep(5000);

			robot.keyPress(KeyEvent.VK_TAB);
			robot.keyRelease(KeyEvent.VK_TAB);
			Thread.sleep(500);

			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);

			Thread.sleep(500);
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);

			Thread.sleep(5000);

		} catch (AWTException | InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
		}
	}

	public Boolean validateCardAreVisible() {
		boolean allCardsValidated = false;
		try {
			((JavascriptExecutor) driver).executeScript("window.scrollTo(0,0)");

			Thread.sleep(1000);
			wait.until(ExpectedConditions.textToBePresentInElement(
					driver.findElement(By.cssSelector(".component-title")), "Total Production Devices"));

			List<WebElement> cards = driver.findElements(By.xpath("//div[contains(@class, 'kpi-section')]/div"));
			allCardsValidated = true;

			System.err.println("Total cards found: " + cards.size());

			for (WebElement card : cards) {
				comm.highlightElement(card, "violet");
				String cardName = card.getText().split("\n")[0].trim();

				String cardValue = card.findElement(By.xpath(".//span[contains(@class,'kpi-value')]")).getText();
				cardCounts.add(cardValue);
				logger.debug("Card Name -> {} have count -> {}", cardName, cardValue);

				// Click on the card
				card.click();

				// Wait for the table header
				WebElement tableHeader = wait
						.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".component-title")));

				String headerText = tableHeader.getText().trim();

				if (headerText.equalsIgnoreCase(cardName)) {
					logger.info("âœ… PASS: {} matches table header.", cardName);
				} else {
					logger.error("âŒ FAIL: Card {} but header is {}", cardName, headerText);
					allCardsValidated = false; // mark failure but continue loop
				}

				// Scroll back to top
				((JavascriptExecutor) driver).executeScript("window.scrollTo(0,0)");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return allCardsValidated;
	}

	public Boolean validateGraphIsVisible() {
		return driver.findElements(By.cssSelector(".graph-card")).stream()
				.peek(graph -> logger.info("Graph found: {}", graph.getText()))
				.allMatch(graph -> graph.isDisplayed() && graph.isEnabled());
	}

	public Boolean validateGraphClick() {
		boolean allGraphsPassed = true;

		List<WebElement> graphs = driver.findElements(By.cssSelector(".graph-card"));
		System.out.println("Total graphs found: " + graphs.size());

		for (WebElement graph : graphs) {
			try {
				comm.highlightElement(graph, "orange");
				String graphName = graph.getText().split("\n")[0].trim();

				graph.click();

				WebElement tableHeader = wait
						.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".component-title")));
				String headerText = tableHeader.getText().trim();

				if (headerText.equalsIgnoreCase(graphName)) {
					logger.info("âœ… PASS: {} matches table header.", graphName);
				} else {
					logger.error("âŒ FAIL: Graph {} but header is {}", graphName, headerText);
					allGraphsPassed = false; // mark failure
				}

				// Scroll back to top
				((JavascriptExecutor) driver).executeScript("window.scrollTo(0,0)");

			} catch (Exception e) {
				e.printStackTrace();
				allGraphsPassed = false; // mark failure if exception
			}
		}

		return allGraphsPassed;
	}

	public List<String> validateTotalProductionDevicesTableHeaders() {
		List<String> actualTableHeaders = new ArrayList<>();
		try {
			Thread.sleep(500);
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("window.scrollTo(0, 0);");

			wait.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath("//div/span[contains(@class, 'kpi-content')]"))).click();
			Thread.sleep(500);

			actualTableHeaders = tableUtils.getTableHeaders(By.xpath("//table"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return actualTableHeaders;
	}

	public Boolean validateTotalProductionDevicesTableButtons() {
		return tableUtils.areViewButtonsEnabled(By.xpath("//table"));
	}

	public Boolean isSearchButtonVisible() {
		try {
			WebElement searchBtn = wait
					.until(ExpectedConditions.visibilityOfElementLocated(DEVICE_DASHBOARD_SEARCHBTN));
			comm.highlightElement(searchBtn, "green");
			return searchBtn.isDisplayed();
		} catch (TimeoutException e) {
			logger.error("Search button not found: {}", e.getMessage());
			return false;
		} catch (Exception e) {
			logger.error("Unexpected error while checking search button visibility: {}", e.getMessage());
			return false;
		}
	}

	public boolean isSearchButtonEnabled() {
		try {
			WebElement searchBtn = wait
					.until(ExpectedConditions.visibilityOfElementLocated(DEVICE_DASHBOARD_SEARCHBTN));
			comm.highlightElement(searchBtn, "green");
			return searchBtn.isEnabled();
		} catch (TimeoutException e) {
			logger.error("Search button not found: {}", e.getMessage());
			return false;
		} catch (Exception e) {
			logger.error("Unexpected error while checking search button enabled state: {}", e.getMessage());
			return false;
		}
	}

	public boolean isSearchInputVisible() {

		try {
			WebElement searchInput = wait
					.until(ExpectedConditions.visibilityOfElementLocated(DEVICE_DASHBOARD_SEARCHBOX));
			comm.highlightElement(searchInput, "green");
			return searchInput.isDisplayed();
		} catch (TimeoutException e) {
			logger.error("Search input not found: {}", e.getMessage());
			return false;
		} catch (Exception e) {
			logger.error("Unexpected error while checking search input visibility: {}", e.getMessage());
			return false;
		}
	}

	public boolean isSearchInputEnabled() {
		try {
			WebElement searchInput = wait
					.until(ExpectedConditions.visibilityOfElementLocated(DEVICE_DASHBOARD_SEARCHBOX));
			comm.highlightElement(searchInput, "green");
			return searchInput.isEnabled();
		} catch (TimeoutException e) {
			logger.error("Search input not found: {}", e.getMessage());
			return false;
		} catch (Exception e) {
			logger.error("Unexpected error while checking search input enabled state: {}", e.getMessage());
			return false;
		}
	}

	public boolean searchDevice() {
		try {
			String imei = Constants.IMEI;
			String iccid = Constants.ICCID;
			String uin = Constants.UIN;
			String fallbackDeviceUIN = "ACON4SA210190080220";
			String fallbackDeviceIMEI = "867950076671229";

			WebElement searchInput = wait
					.until(ExpectedConditions.visibilityOfElementLocated(DEVICE_DASHBOARD_SEARCHBOX));
			comm.highlightElement(searchInput, "green");
			searchInput.clear();

			// Locate card title
			String path = "//div/h6[contains(@class, 'component-title')]";
			WebElement titleElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(path)));
			String title = titleElement.getText().trim();
			logger.info("ðŸ“Œ Current card title: {}", title);

			// Pick correct search term
			String searchTerm;
			switch (title) {
				case "Total Production Devices":
					searchTerm = imei;
					break;
				case "Total Dispatched Devices":
				case "Total Installed Devices":
					searchTerm = fallbackDeviceIMEI;
					break;
				case "Total Discarded Devices":
					searchTerm = fallbackDeviceUIN;
					break;
				case "Device Activity Overview":
					searchTerm = imei;
					break;
				default:
					logger.warn("âš ï¸ Unknown card title: {}", title);
					return false;
			}

			searchInput.sendKeys(searchTerm);
			searchInput.sendKeys(Keys.ENTER);

			// ðŸ”„ Wait until at least 1st cell contains non-empty text
			By firstCell = By.xpath("//tbody/tr[1]/td");
			wait.until(driver -> {
				List<WebElement> rows = driver.findElements(firstCell);
				return !rows.isEmpty() && rows.get(0).getText().trim().length() > 0;
			});

			// âœ… Re-fetch every time, donâ€™t reuse stale elements
			List<WebElement> results = driver.findElements(firstCell);

			for (WebElement result : results) {
				String text = result.getText().trim();
				logger.debug("Cell text: {}", text);

				if (text.contains(uin) || text.contains(imei) || text.contains(iccid)
						|| text.contains(fallbackDeviceUIN) || text.contains(fallbackDeviceIMEI)) {
					logger.info("âœ… Search successful, term [{}] found in results.", searchTerm);
					return true;
				}
			}

			logger.error("âŒ Search term [{}] not found in results.", searchTerm);
			return false;

		} catch (TimeoutException e) {
			logger.error("â±ï¸ Search operation timed out: {}", e.getMessage());
			return false;
		} catch (StaleElementReferenceException e) {
			logger.warn("â™»ï¸ Retrying due to stale element...");
			// ðŸ”„ Safer retry with limited attempts
			return retrySearch(3);
		} catch (Exception e) {
			logger.error("âŒ Unexpected error: {}", e.getMessage());
			return false;
		}
	}

	// Helper to avoid infinite recursion
	private boolean retrySearch(int retries) {
		for (int i = 0; i < retries; i++) {
			try {
				return searchDevice();
			} catch (StaleElementReferenceException ignored) {
				logger.warn("â™»ï¸ Attempt {} failed, retrying...", (i + 1));
			}
		}
		return false;
	}

	public boolean isExportButtonVisible() {
		try {
			WebElement exportBtn = wait
					.until(ExpectedConditions.visibilityOfElementLocated(DEVICE_DASHBOARD_EXPORTBTN));
			comm.highlightElement(exportBtn, "green");
			return exportBtn.isDisplayed();
		} catch (TimeoutException e) {
			logger.error("Export button not found: {}", e.getMessage());
			return false;
		} catch (Exception e) {
			logger.error("Unexpected error while checking export button visibility: {}", e.getMessage());
			return false;
		}
	}

	public boolean isExportButtonEnabled() {
		try {
			WebElement exportBtn = wait
					.until(ExpectedConditions.visibilityOfElementLocated(DEVICE_DASHBOARD_EXPORTBTN));
			comm.highlightElement(exportBtn, "green");
			return exportBtn.isEnabled();
		} catch (TimeoutException e) {
			logger.error("Export button not found: {}", e.getMessage());
			return false;
		} catch (Exception e) {
			logger.error("Unexpected error while checking export button enabled state: {}", e.getMessage());
			return false;
		}
	}

	public List<String> validateTotalDispatchedDevicesTableHeaders() {
		List<String> actualTableHeaders = new ArrayList<>();
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("window.scrollTo(0, 0);");

			Thread.sleep(500);
			List<WebElement> cards = wait.until(ExpectedConditions
					.visibilityOfAllElementsLocatedBy(By.xpath("//div/span[contains(@class, 'kpi-content')]")));

			cards.get(1).click();

			Thread.sleep(500);

			actualTableHeaders = tableUtils.getTableHeaders(By.xpath("//table"));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return actualTableHeaders;
	}

	public boolean validateTotalDispatchedDevicesTableButtons() {
		return tableUtils.areViewButtonsEnabled(By.xpath("//table"));
	}

	public boolean validateTotalInstalledDevicesTableButtons() {
		return tableUtils.areViewButtonsEnabled(By.xpath("//table"));
	}

	public boolean validateTotalDiscardedDevicesTableButtons() {
		return tableUtils.areViewButtonsEnabled(By.xpath("//table"));
	}

	public boolean validateDeviceActivityOverviewTableButtons() {
		return tableUtils.areViewButtonsEnabled(By.xpath("//table"));
	}

	public List<String> validateTotalInstalledDevicesTableHeaders() {
		List<String> actualTableHeaders = new ArrayList<>();
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("window.scrollTo(0, 0);");

			Thread.sleep(500);
			List<WebElement> cards = wait.until(ExpectedConditions
					.visibilityOfAllElementsLocatedBy(By.xpath("//div/span[contains(@class, 'kpi-content')]")));

			cards.get(2).click();

			Thread.sleep(500);

			actualTableHeaders = tableUtils.getTableHeaders(By.xpath("//table"));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return actualTableHeaders;
	}

	public List<String> validateTotalDiscardedDevicesTableHeaders() {
		List<String> actualTableHeaders = new ArrayList<>();
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("window.scrollTo(0, 0);");

			Thread.sleep(500);
			List<WebElement> cards = wait.until(ExpectedConditions
					.visibilityOfAllElementsLocatedBy(By.xpath("//div/span[contains(@class, 'kpi-content')]")));

			cards.get(3).click();

			Thread.sleep(500);

			actualTableHeaders = tableUtils.getTableHeaders(By.xpath("//table"));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return actualTableHeaders;
	}

	public boolean isDeviceActivityOverviewGraphVisible() {
		try {
			WebElement graph = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".graph-card")));
			comm.highlightElement(graph, "green");
			return graph.isDisplayed();
		} catch (TimeoutException e) {
			logger.error("Device Activity Overview graph not found: {}", e.getMessage());
			return false;
		} catch (Exception e) {
			logger.error("Unexpected error while checking Device Activity Overview graph visibility: {}",
					e.getMessage());
			return false;
		}
	}

	public boolean isFirmwareWiseDevicesGraphVisible() {
		try {
			List<WebElement> graphs = wait
					.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(".graph-card")));
			comm.highlightElements(graphs, "solid purple");
			return graphs.stream().allMatch(WebElement::isDisplayed);
		} catch (TimeoutException e) {
			logger.error("Device Activity Overview graph not found: {}", e.getMessage());
			return false;
		} catch (Exception e) {
			logger.error("Unexpected error while checking Device Activity Overview graph visibility: {}",
					e.getMessage());
			return false;
		}
	}

	public boolean validateDeviceActivityOverviewGraphClick() {
		try {
			WebElement graph = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".graph-card")));
			comm.highlightElement(graph, "orange");
			String graphName = graph.getText().split("\n")[0].trim();

			graph.click();

			WebElement tableHeader = wait
					.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".component-title")));
			String headerText = tableHeader.getText().trim();

			if (headerText.equalsIgnoreCase(graphName)) {
				logger.info("âœ… PASS: {} matches table header.", graphName);
				return true;
			} else {
				logger.error("âŒ FAIL: Graph {} but header is {}", graphName, headerText);
				return false;
			}

		} catch (TimeoutException e) {
			logger.error("Device Activity Overview graph not found: {}", e.getMessage());
			return false;
		} catch (Exception e) {
			logger.error("Unexpected error while validating Device Activity Overview graph click: {}", e.getMessage());
			return false;
		}
	}

	public boolean validateFirmwareWiseDevicesGraphClick() {
		try {
			((JavascriptExecutor) driver).executeScript("window.scrollTo(0,0)");

			List<WebElement> graphs = wait
					.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(".graph-card")));

			for (WebElement graph : graphs) {

				comm.highlightElement(graph, "orange");

				// Read graph title before clicking
				String graphName = graph.getText().split("\n")[0].trim();

				if (graphName.equalsIgnoreCase("Firmware Wise Devices")) {

					graph.click();

					// Wait for header after clicking
					WebElement tableHeader = wait
							.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".component-title")));
					String headerText = tableHeader.getText().trim();

					if (headerText.equalsIgnoreCase(graphName)) {
						logger.info("âœ… PASS: Firmware Wise Devices graph opened correctly.");
						return true;
					} else {
						logger.error("âŒ FAIL: Expected: Firmware Wise Devices, but header is: {}", headerText);
						return false;
					}
				}
			}

			logger.error("âŒ Firmware Wise Devices graph not found among listed graphs.");
			return false;

		} catch (Exception e) {
			logger.error("Unexpected error while validating Firmware Wise Devices graph: {}", e.getMessage());
			return false;
		}
	}

	public List<String> validateDeviceActivityOverviewGraphTableHeaders() {
		List<String> actualTableHeaders = new ArrayList<>();
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			WebElement graph = wait.until(
					ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'graph-body')]")));
			js.executeScript("arguments[0].scrollIntoView(true);", graph);
			graph.click();
			Thread.sleep(500);

			actualTableHeaders = tableUtils.getTableHeaders(By.xpath("//table"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return actualTableHeaders;
	}

	public List<String> selectActivityDurationDropdown() {

		List<String> selectedOptions = new ArrayList<>();

		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;

			WebElement dropdown = wait.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath("//span[contains(text(),'Select Activity Version')]")));
			comm.highlightElement(dropdown, "solid purple");
			dropdown.click();

			// Get count first (not WebElements)
			List<WebElement> allOptions = wait.until(
					ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@class='list-items']/ul/li")));
			int optionCount = allOptions.size();

			for (int i = 0; i < optionCount; i++) {

				// Re-fetch options fresh every iteration
				List<WebElement> options = wait.until(ExpectedConditions
						.visibilityOfAllElementsLocatedBy(By.xpath("//div[@class='list-items']/ul/li")));

				WebElement option = options.get(i);
				String optionText = option.getText().trim();
				selectedOptions.add(optionText);

				// Scroll into view before clicking
				js.executeScript("arguments[0].scrollIntoView({block: 'center'});", option);
				comm.highlightElement(option, "solid purple");

				option.click();
				Thread.sleep(1000); // wait for table to refresh

				// Reopen dropdown for next iteration (if not last)
				if (i < optionCount - 1) {
					dropdown = wait.until(ExpectedConditions
							.elementToBeClickable(By.xpath("//span[contains(text(),'Select Activity Version')]")));
					dropdown.click();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return selectedOptions;
	}

	public boolean validateTableDataOfDeviceActivityOverviewTable() {
		List<Map<String, String>> tableData = tableUtils.getTableData(By.xpath("//table"),
				tableUtils.getTableHeaders(By.xpath("//table")));
		if (tableData.isEmpty()) {
			logger.error("No data found in Device Activity Overview table.");
			return false;
		} else {
			logger.info("Device Activity Overview table has {} rows of data.", tableData.size());
			return true;
		}
	}

	public boolean isViewButtonEnabledInDeviceActivityOverviewTable() {
		return tableUtils.areViewButtonsEnabled(By.xpath("//table"));
	}

	public List<String> validateFirmwareWiseDevicesGraphTableHeaders() {
		List<String> actualTableHeaders = new ArrayList<>();
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			WebElement graph = wait.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath("(//div[contains(@class, 'graph-body')])[2]")));
			js.executeScript("arguments[0].scrollIntoView(true);", graph);
			graph.click();
			Thread.sleep(500);

			actualTableHeaders = tableUtils.getTableHeaders(By.xpath("//table"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return actualTableHeaders;
	}

	public boolean validateFirmwareWiseDevicesTableViewButtons() {
		return tableUtils.areViewButtonsEnabled(By.xpath("//table"));
	}

	public boolean isFirmwareVersionDropdownVisibleAndClickable() {
		try {
			WebElement dropdown = wait.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath("//span[contains(text(),'Select Firmware Version')]")));
			comm.highlightElement(dropdown, "solid purple");
			if (dropdown.isDisplayed() && dropdown.isEnabled()) {
				dropdown.click();
				return true;
			} else {
				logger.error("Firmware Version dropdown is either not visible or not enabled.");
				return false;
			}
		} catch (TimeoutException e) {
			logger.error("Firmware Version dropdown not found: {}", e.getMessage());
			return false;
		} catch (Exception e) {
			logger.error("Unexpected error while checking Firmware Version dropdown: {}", e.getMessage());
			return false;
		}
	}

	public boolean validateTableDataOfFirmwareWiseDevicesTable() {
		List<Map<String, String>> tableData = tableUtils.getTableData(By.xpath("//table"),
				tableUtils.getTableHeaders(By.xpath("//table")));
		if (tableData.isEmpty()) {
			logger.error("No data found in Firmware Wise Devices table.");
			return false;
		} else {
			logger.info("Firmware Wise Devices table has {} rows of data.", tableData.size());
			return true;
		}
	}
}

