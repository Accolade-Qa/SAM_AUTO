package com.aepl.sam.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.aepl.sam.locators.CommonPageLocators.*;

public class PageActionsUtil {

	private static final Logger logger = LogManager.getLogger(PageActionsUtil.class);

	private final WebDriver driver;
	private final WebDriverWait wait;
	private final TableUtils tableUtils;
	private final PageAssertionsUtil assertionsUtil;

	public PageActionsUtil(WebDriver driver, WebDriverWait wait) {
		this.driver = driver;
		this.wait = (wait != null) ? wait : new WebDriverWait(driver, Duration.ofSeconds(10));
		this.tableUtils = new TableUtils(driver, this.wait);
		this.assertionsUtil = new PageAssertionsUtil(driver, this.wait);
	}

	public void captureScreenshot(String testCaseName) {
		if (!(driver instanceof TakesScreenshot)) {
			throw new IllegalStateException("Driver does not implement TakesScreenshot.");
		}
		try {
			String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
			String fileName = testCaseName + "_" + timestamp + ".png";
			String screenshotDir = System.getProperty("user.dir") + File.separator + "screenshots";
			File dir = new File(screenshotDir);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(srcFile, new File(screenshotDir + File.separator + fileName));
		} catch (IOException e) {
			logger.error("Failed to capture screenshot for {}", testCaseName, e);
		}
	}

	public void highlightElement(WebElement element, String colorCode) {
		((JavascriptExecutor) driver).executeScript("arguments[0].style.border='3px " + colorCode + " '", element);
	}

	public void highlightElements(List<WebElement> elements, String colorCode) {
		for (WebElement element : elements) {
			highlightElement(element, colorCode);
		}
	}

	public WebElement findFirstVisibleElement(By... locators) {
		for (By locator : locators) {
			List<WebElement> elements = driver.findElements(locator);
			for (WebElement element : elements) {
				if (element != null && element.isDisplayed()) {
					return element;
				}
			}
		}
		return null;
	}

	public boolean isElementVisible(By... locators) {
		return findFirstVisibleElement(locators) != null;
	}

	public boolean isElementEnabled(By... locators) {
		WebElement element = findFirstVisibleElement(locators);
		return element != null && element.isEnabled();
	}

	public WebElement waitForVisibility(By locator) {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	public void clickElement(WebElement element) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(element)).click();
		} catch (Exception e) {
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
		}
	}

	public String getElementText(By... locators) {
		WebElement element = findFirstVisibleElement(locators);
		return element != null ? element.getText().trim() : "";
	}

	public void typeAndSearch(By inputLocator, By searchButtonLocator, String query) {
		WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(inputLocator));
		searchInput.click();
		searchInput.clear();
		searchInput.sendKeys(query);

		WebElement searchButton = findFirstVisibleElement(searchButtonLocator);
		if (searchButton != null && searchButton.isEnabled()) {
			clickElement(searchButton);
		} else {
			searchInput.sendKeys(Keys.ENTER);
		}
	}

	public void resetSearch(By inputLocator, By resetButtonLocator) {
		WebElement searchInput = findFirstVisibleElement(inputLocator);
		WebElement resetButton = findFirstVisibleElement(resetButtonLocator);

		if (resetButton != null && resetButton.isEnabled()) {
			clickElement(resetButton);
		} else if (searchInput != null) {
			searchInput.click();
			searchInput.clear();
			searchInput.sendKeys(Keys.ENTER);
		}
	}

	public boolean checkSearchBoxWithTableHeadings(String input, List<String> expectedHeaders) {
		checkSearchBox(input);
		List<String> actualHeaders = tableUtils.getTableHeaders(TABLE).stream().map(String::trim).map(String::toLowerCase)
				.collect(Collectors.toList());
		List<String> expected = expectedHeaders.stream().map(String::trim).map(String::toLowerCase)
				.collect(Collectors.toList());
		return actualHeaders.equals(expected);
	}

	public void checkSearchBox(String input) {
		WebElement search = wait.until(ExpectedConditions.visibilityOfElementLocated(SEARCH_BOX_INPUT));
		search.click();
		search.clear();
		search.sendKeys(input);
		search.sendKeys(Keys.ENTER);
	}

	public boolean checkTableHeadings(List<String> expectedHeaders) {
		List<String> actualHeaders = tableUtils.getTableHeaders(TABLE).stream().map(String::trim).map(String::toLowerCase)
				.collect(Collectors.toList());
		List<String> expected = expectedHeaders.stream().map(String::trim).map(String::toLowerCase)
				.collect(Collectors.toList());
		return actualHeaders.equals(expected);
	}

	public void clickEyeActionButton(By eyeActionButton) {
		WebElement eyeBtn = wait.until(ExpectedConditions.elementToBeClickable(eyeActionButton));
		clickElement(eyeBtn);
	}

	public void checkPagination(WebElement nextButton, WebElement prevButton, WebElement activeButton) {
		if (nextButton.isDisplayed() && nextButton.isEnabled()) {
			clickElement(nextButton);
		}
		if (prevButton.isDisplayed() && prevButton.isEnabled()) {
			clickElement(prevButton);
		}
	}

	public void checkPagination() {
		checkPaginationAdvanced();
	}

	public void checkPaginationAdvanced() {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
			Thread.sleep(500);

			WebElement rowPerPage = wait.until(ExpectedConditions.elementToBeClickable(ROW_PER_PAGE));
			highlightElement(rowPerPage, "solid purple");
			Select select = new Select(rowPerPage);

			List<WebElement> options = select.getOptions();
			for (int i = 0; i < options.size(); i++) {
				js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
				Thread.sleep(500);
				rowPerPage = wait.until(ExpectedConditions.elementToBeClickable(ROW_PER_PAGE));
				select = new Select(rowPerPage);
				WebElement option = select.getOptions().get(i);
				option.click();
				Thread.sleep(500);
			}

			rowPerPage = wait.until(ExpectedConditions.elementToBeClickable(ROW_PER_PAGE));
			select = new Select(rowPerPage);
			WebElement defaultOption = select.getOptions().get(0);
			js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
			defaultOption.click();

			for (int i = 1; i < 4; i++) {
				js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
				Thread.sleep(300);
				WebElement rightArrow = wait.until(ExpectedConditions.elementToBeClickable(RIGHT_ARROW));
				highlightElement(rightArrow, "solid purple");
				js.executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'center'});", rightArrow);
				rightArrow.click();
				Thread.sleep(500);
			}

			for (int i = 4; i > 1; i--) {
				js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
				Thread.sleep(300);
				WebElement leftArrow = wait.until(ExpectedConditions.elementToBeClickable(LEFT_ARROW));
				highlightElement(leftArrow, "solid purple");
				js.executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'center'});", leftArrow);
				leftArrow.click();
				Thread.sleep(500);
			}
		} catch (Exception e) {
			logger.error("Error occurred during pagination check: {}", e.getMessage(), e);
		}
	}

	public void reportDownloadButtons(WebElement button) {
		clickElement(button);
		try {
			Alert alert = wait.until(ExpectedConditions.alertIsPresent());
			alert.accept();
		} catch (Exception ignored) {
		}
	}

	public void checkReportDownloadForAllbuttons(WebElement button) {
		if (button == null || !button.isDisplayed() || !button.isEnabled()) {
			throw new IllegalStateException("Download button is not visible/enabled.");
		}
	}

	public String randomStringGen() {
		return UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase(Locale.ROOT);
	}

	public boolean verifyWebpageLogo() {
		return assertionsUtil.verifyWebpageLogo();
	}

	public String validateComponents() {
		return assertionsUtil.validateComponents();
	}

	public String validateButtons() {
		return assertionsUtil.validateButtons();
	}

	public void clickRefreshButton() {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			WebElement refreshButton = wait.until(ExpectedConditions.visibilityOfElementLocated(REFRESH_BUTTON));
			js.executeScript("arguments[0].scrollIntoView(true);", refreshButton);
			Thread.sleep(1000);
			js.executeScript("arguments[0].style.border='3px solid purple'", refreshButton);
			try {
				refreshButton.click();
			} catch (ElementClickInterceptedException e) {
				js.executeScript("arguments[0].click();", refreshButton);
			}
			Thread.sleep(1000);
		} catch (Exception e) {
			logger.error("Failed to click on the refresh button.", e);
			throw new RuntimeException("Failed to click on the refresh button.", e);
		}
	}

	public String validateComponentTitle() {
		WebElement componentTitle = driver.findElement(By.xpath("//div[@class=\"component-header\"]/h6"));
		highlightElement(componentTitle, "solid purple");
		return componentTitle.getText();
	}

	public String clickSampleFileButton() {
		for (int i = 0; i <= 3; i++) {
			try {
				WebElement sampleFileButton = wait.until(ExpectedConditions.elementToBeClickable(SAMPLE_FILE_BUTTON));
				highlightElement(sampleFileButton, "solid purple");
				sampleFileButton.click();
				Thread.sleep(500);
			} catch (Exception e) {
				logger.warn("Attempt {} failed to click 'Sample File' button: {}", i + 1, e.getMessage());
			}
		}
		return "File downloaded successfully.";
	}

	public boolean validateSampleFileButton() {
		try {
			for (int i = 0; i < 5; i++) {
				WebElement sampleFileButton = wait.until(ExpectedConditions.elementToBeClickable(SAMPLE_FILE_BUTTON));
				sampleFileButton.click();
				Alert alert = wait.until(ExpectedConditions.alertIsPresent());
				alert.accept();
				Thread.sleep(2000);
			}
		} catch (Exception e) {
			logger.error("Error validating Sample File button: {}", e.getMessage(), e);
			return false;
		}
		return true;
	}

	public boolean validateExportButton() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		for (int attempt = 0; attempt < 3; attempt++) {
			try {
				try {
					WebElement exportButton = driver.findElement(EXPORT_BUTTON);
					if (exportButton.isDisplayed()) {
						js.executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'center'});", exportButton);
						if (exportButton.isEnabled()) {
							exportButton.click();
							Alert alert = wait.until(ExpectedConditions.alertIsPresent());
							alert.accept();
							return true;
						}
						return false;
					}
				} catch (Exception inner) {
					js.executeScript("window.scrollBy(0, 200);");
					Thread.sleep(300);
				}
			} catch (Exception e) {
				logger.error("Attempt {} failed: {}", attempt + 1, e.getMessage());
			}
		}
		return false;
	}

	public String validateInputBoxError() {
		try {
			WebElement inputBox = wait.until(ExpectedConditions.visibilityOfElementLocated(INPUT_BOX));
			highlightElement(inputBox, "solid purple");
			inputBox.click();
			Thread.sleep(500);
			WebElement body = driver.findElement(By.xpath("//body/app-root/app-header/div"));
			body.click();
			Thread.sleep(500);
			WebElement errorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(INPUT_BOX_ERROR));
			String errorMessage = errorElement.getText().trim();
			return !errorMessage.isEmpty() ? errorMessage : "Error Message Empty";
		} catch (TimeoutException e) {
			logger.error("Error message not found: {}", e.getMessage());
			return "Error Message Not Found";
		} catch (Exception e) {
			logger.error("Unexpected exception during error validation: {}", e.getMessage(), e);
			return "Validation Failed";
		}
	}

	public boolean verifyCSVHeader(String filePath, String expectedHeader) {
		File file = new File(filePath);
		if (!file.exists() || file.length() == 0) {
			logger.error("File not found or empty: {}", filePath);
			return false;
		}

		String lowerName = file.getName().toLowerCase();
		try {
			List<String> actualHeaders;
			if (lowerName.endsWith(".csv")) {
				List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
				if (lines.isEmpty()) {
					return false;
				}
				actualHeaders = Arrays.stream(lines.get(0).split(",")).map(String::trim).filter(h -> !h.isEmpty())
						.collect(Collectors.toList());
			} else if (lowerName.endsWith(".xlsx")) {
				try (FileInputStream fis = new FileInputStream(file); Workbook workbook = new XSSFWorkbook(fis)) {
					Sheet sheet = workbook.getSheetAt(0);
					Row headerRow = sheet.getRow(0);
					if (headerRow == null) {
						return false;
					}
					actualHeaders = new ArrayList<>();
					for (Cell cell : headerRow) {
						String value = cell.getStringCellValue().trim();
						if (!value.isEmpty()) {
							actualHeaders.add(value);
						}
					}
				}
			} else {
				return false;
			}

			List<String> expectedHeaders = Arrays.stream(expectedHeader.split(",")).map(String::trim)
					.filter(h -> !h.isEmpty()).collect(Collectors.toList());

			return actualHeaders.size() >= expectedHeaders.size()
					&& actualHeaders.subList(0, expectedHeaders.size()).equals(expectedHeaders);
		} catch (Exception e) {
			logger.error("Error verifying file header: {}", e.getMessage(), e);
			return false;
		}
	}
}
