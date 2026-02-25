package com.aepl.sam.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TableUtils {
	private WebDriverWait wait;
	private static final Logger logger = LogManager.getLogger(TableUtils.class);

	public TableUtils(WebDriverWait wait) {
		this.wait = wait;
	}

	public TableUtils(WebDriver driver, WebDriverWait wait) {
		this.wait = wait;
	}

	public List<String> getTableHeaders(By tableLocator) {
		List<String> headerTexts = new ArrayList<>();
		try {
			WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(tableLocator));

			// MUST be relative to the table
			List<WebElement> headers = table.findElements(By.xpath(".//thead//th"));

			// Fallback: first row as header
			if (headers.isEmpty()) {
				headers = table.findElements(By.xpath(".//tr[1]/*"));
			}

			headerTexts = headers.stream().map(WebElement::getText).filter(t -> !t.isEmpty())
					.collect(Collectors.toList());

			logger.info("Table Headers Found: {}", headerTexts);

		} catch (TimeoutException e) {
			logger.error("Table not found or not visible: {}", e.getMessage());
		} catch (Exception e) {
			logger.error("Unexpected error while extracting table headers: {}", e.getMessage(), e);
		}

		return headerTexts;
	}

	public List<Map<String, String>> getTableData(By tableLocator, List<String> headers) {
		List<Map<String, String>> tableData = new ArrayList<>();

		try {
			WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(tableLocator));

			// Normalize header keys for consistency
			List<String> normalizedHeaders = headers.stream().map(h -> h.trim().toUpperCase())
					.collect(Collectors.toList());

			// 1️⃣ Skip table if "No Data Found" row is present
			if (!table.findElements(By.xpath(".//img[contains(@class,'no-data-img')]")).isEmpty()) {
				logger.info("No data available in table (found no-data-img).");
				return tableData;
			}

			// 2️⃣ Extract rows
			List<WebElement> rows = table.findElements(By.xpath(".//tbody/tr"));
			if (rows.isEmpty()) {
				logger.warn("No rows found in the table, and 'no-data-img' not detected.");
				return tableData;
			}

			// 3️⃣ Process each row
			for (WebElement row : rows) {

				// Skip the no-data row explicitly
				if (!row.findElements(By.xpath(".//img[contains(@class,'no-data-img')]")).isEmpty()) {
					logger.info("Skipping row containing no-data image.");
					continue;
				}

				List<WebElement> cells = row.findElements(By.tagName("td"));
				if (cells.isEmpty()) {
					logger.debug("Skipping empty row.");
					continue; // skip placeholder rows
				}

				Map<String, String> rowData = new LinkedHashMap<>();

				for (int i = 0; i < cells.size(); i++) {
					String header = i < normalizedHeaders.size() ? normalizedHeaders.get(i) : "COLUMN" + (i + 1);

					WebElement cell = cells.get(i);
					String cellText = cell.getText().trim();

					// 4️⃣ Handle checkboxes
					List<WebElement> checkboxes = cell.findElements(By.xpath(".//input[@type='checkbox']"));
					if (!checkboxes.isEmpty()) {
						boolean isChecked = checkboxes.get(0).isSelected();
						rowData.put(header, isChecked ? "CHECKED" : "UNCHECKED");
						continue;
					}

					// 5️⃣ Extract fallback text (inside spans, divs, strong, etc.)
					if (cellText.isEmpty()) {
						List<WebElement> innerTexts = cell.findElements(By.xpath(".//*"));
						for (WebElement inner : innerTexts) {
							if (!inner.getText().trim().isEmpty()) {
								cellText = inner.getText().trim();
								break;
							}
						}
					}

					rowData.put(header, cellText);
				}

				tableData.add(rowData);
			}

			logger.info("Total valid rows extracted: {}", tableData.size());

		} catch (Exception e) {
			logger.error("Unexpected error while extracting table data: {}", e.getMessage(), e);
		}

		return tableData;
	}

	public boolean areViewButtonsEnabled(By tableLocator) {
		boolean allEnabled = true;
		try {
			WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(tableLocator));

			// Find all "view" buttons inside the Action column
			List<WebElement> viewButtons = table
					.findElements(By.xpath(".//tbody//tr//td[last()]//button[contains(., 'visibility')]"));

			logger.info("Found {} view buttons", viewButtons.size());

			for (WebElement btn : viewButtons) {
				if (!btn.isDisplayed() || !btn.isEnabled()) {
					allEnabled = false;
					logger.warn("A view button is not enabled or visible!");
					break;
				}
			}
		} catch (Exception e) {
			logger.error("Error checking view button states: {}", e.getMessage(), e);
			allEnabled = false;
		}
		return allEnabled;
	}

	public boolean areDeleteButtonsEnabled(By tableLocator) {
		boolean allEnabled = true;
		try {
			WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(tableLocator));

			// Find all "delete" buttons inside the Action column
			List<WebElement> deleteButtons = table
					.findElements(By.xpath(".//tbody//tr//td[last()]//button[contains(., 'delete')]"));

			logger.info("Found {} delete buttons", deleteButtons.size());

			for (WebElement btn : deleteButtons) {
				if (!btn.isDisplayed() || !btn.isEnabled()) {
					allEnabled = false;
					logger.warn("A delete button is not enabled or visible!");
					break;
				}
			}
		} catch (Exception e) {
			logger.error("Error checking delete button states: {}", e.getMessage(), e);
			allEnabled = false;
		}
		return allEnabled;
	}

	public boolean clickFirstViewButton(By tableLocator) {
		try {
			By viewButtonLocator = By.xpath(".//td[last()]//button[contains(., 'visibility')]");
			WebElement firstViewButton = wait.until(ExpectedConditions.elementToBeClickable(viewButtonLocator));
			firstViewButton.click();
			logger.info("Clicked the first view button successfully.");
			return true;
		} catch (Exception e) {
			logger.error("Error clicking the first view button: {}", e.getMessage(), e);
			return false;
		}
	}

	public boolean clickFirstDeleteButton(By tableLocator) {
		try {
			By deleteButtonLocator = By.xpath(".//td[last()]//button[contains(., 'delete')]");
			WebElement firstDeleteButton = wait.until(ExpectedConditions.elementToBeClickable(deleteButtonLocator));
			firstDeleteButton.click();
			logger.info("Clicked the first delete button successfully.");
			return true;
		} catch (Exception e) {
			logger.error("Error clicking the first delete button: {}", e.getMessage(), e);
			return false;
		}
	}

	/**
	 * Checks if the "No Data Found" image or message is present inside the table.
	 *
	 * @param tableLocator Locator for the table element.
	 * @return true if a 'no data' indicator (like class 'no-data-img') is found,
	 *         false otherwise.
	 */
	public boolean isNoDataImagePresent(By tableLocator) {
		try {
			WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(tableLocator));

			// Look for no-data image inside this table only
			List<WebElement> noDataElements = table.findElements(By.xpath(".//img[contains(@class,'no-data-img')]"));

			if (!noDataElements.isEmpty()) {
				logger.info("No data available in the table (found no-data-img).");
				return true;
			}

			// Optional fallback for text
			List<WebElement> noDataText = table
					.findElements(By.xpath(".//*[contains(translate(text(), 'NO DATA', 'no data'), 'no data')]"));

			if (!noDataText.isEmpty()) {
				logger.info("No data available (found text 'no data').");
				return true;
			}

		} catch (TimeoutException e) {
			logger.error("Table not found: {}", e.getMessage());
		} catch (Exception e) {
			logger.error("Unexpected error: {}", e.getMessage(), e);
		}

		return false;
	}

}