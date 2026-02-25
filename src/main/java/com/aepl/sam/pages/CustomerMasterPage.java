package com.aepl.sam.pages;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aepl.sam.locators.CustomerMasterLocators;
import com.aepl.sam.utils.PageActionsUtil;
import com.aepl.sam.utils.RandomGeneratorUtils;
import com.aepl.sam.utils.TableUtils;

public class CustomerMasterPage extends CustomerMasterLocators {
	private WebDriver driver;
	private WebDriverWait wait;
	private PageActionsUtil comm;
	private String editedUser;
	private String randomName;
	private TableUtils tableUtils;
	private RandomGeneratorUtils random;

	private static final Logger logger = LogManager.getLogger(CustomerMasterPage.class);

	public CustomerMasterPage(WebDriver driver, WebDriverWait wait) {
		this.driver = driver;
		this.wait = wait;
		this.comm = new PageActionsUtil(driver, wait);
		this.random = new RandomGeneratorUtils();
		this.tableUtils = new TableUtils(wait);
	}

	public String navBarLink() {
		logger.info("Navigating to Customer Master from Navbar");

		clickElement(DEVICE_UTILITY);
		clickElement(CUSTOMER_MASTER_LINK);

		String url = driver.getCurrentUrl();
		logger.info("Navigation successful, current URL: {}", url);
		return url;
	}

	public String addNewCustomer() {
		randomName = random.generateRandomString(4).toUpperCase();
		logger.info("Attempting to add a new customer: {}", randomName);

		try {
			// clickElement(ADD_CUSTOMER_BTN);
			fillInputField(CUSTOMER_NAME, randomName);

			WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(SAVE_BTN));
			highlightElement(saveButton);

			if (saveButton.isEnabled()) {
				saveButton.click();
				wait.until(ExpectedConditions.invisibilityOf(saveButton));
				logger.info("Customer added successfully: {}", randomName);
				return "Customer Added Successfully";
			} else {
				logger.warn("Save button is not enabled.");
				return "Save Button Disabled - Customer Not Added";
			}
		} catch (TimeoutException te) {
			logger.error("Timeout while adding customer: {}", te.getMessage(), te);
			return "Operation Timed Out";
		} catch (Exception e) {
			logger.error("Error adding new customer: {}", e.getMessage(), e);
			return "No Customer Added";
		}
	}

	public String searchCustomer() {
		logger.info("Searching for customer: {}", randomName);
		try {
			fillInputField(SEARCH_CUSTOMER, randomName);
			clickElement(SEARCH_BTN);
			sleep(500);

			WebElement foundCustomer = findCustomerRow(randomName, DATA);
			if (foundCustomer != null) {
				logger.info("Customer found: {}", randomName);
				return "Customer Found";
			}
			logger.info("Customer not found in search results.");
		} catch (Exception e) {
			logger.error("Error during customer search: {}", e.getMessage(), e);
		}
		return "No Customer Found";
	}

	public void editCustomer() {
		editedUser = random.generateRandomString(4).toUpperCase();
		logger.info("Editing customer. New name: {}", editedUser);

		try {
			clickElement(EDIT_BTN);
			sleep(500);

			fillInputField(CUSTOMER_NAME, editedUser);
			WebElement updateBtn = driver.findElement(UPDATE_BTN);
			highlightElement(updateBtn);

			if (updateBtn.isEnabled()) {
				updateBtn.click();
				logger.info("Customer updated to: {}", editedUser);
			} else {
				logger.warn("Update button is not enabled.");
			}
		} catch (Exception e) {
			logger.error("Error editing customer: {}", e.getMessage(), e);
		}

		driver.navigate().refresh();
	}

	public void deleteCustomer() {
		logger.info("Attempting to delete customer: {}", editedUser);
		try {
			driver.navigate().refresh();
			Thread.sleep(500);

			List<WebElement> rows = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(D_DATA));
			boolean isDeleted = false;

			for (WebElement row : rows) {
				highlightElement(row);
				scrollDown(500);
				String rowText = row.getText().trim();
				if (rowText.equalsIgnoreCase(editedUser)) {
					logger.info("Customer row matched for deletion: {}", rowText);

					WebElement deleteBtn = row
							.findElement(By.xpath(".//following-sibling::td//button[contains(@class, 'delete')]"));
					highlightElement(deleteBtn);
					deleteBtn.click();
					logger.info("Delete button clicked for: {}", editedUser);

					Alert alert = wait.until(ExpectedConditions.alertIsPresent());
					alert.accept();
					sleep(500);
					logger.info("Alert accepted for deletion.");
					isDeleted = true;
					break;
				}
			}

			if (!isDeleted) {
				logger.warn("Customer not found for deletion: {}", editedUser);
			}

		} catch (Exception e) {
			logger.error("Error deleting customer: {}", e.getMessage(), e);
		}
	}

	// ---------------------- Private Utility Methods -----------------------

	private void clickElement(By locator) {
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
		highlightElement(element);
		element.click();
	}

	private void fillInputField(By locator, String text) {
		WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		highlightElement(field);
		field.clear();
		field.sendKeys(text);
	}

	private void highlightElement(WebElement element) {
		comm.highlightElement(element, "solid purple");
	}

	private void scrollDown(int value) {
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0, " + value + ")");
		sleep(500);
	}

	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			logger.warn("Sleep interrupted: {}", e.getMessage());
		}
	}

	private WebElement findCustomerRow(String nameToMatch, By locator) {
		try {
			List<WebElement> rows = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
			for (WebElement row : rows) {
				scrollDown(250);
				if (row.getText().equalsIgnoreCase(nameToMatch)) {
					return row;
				}
			}
		} catch (Exception e) {
			logger.error("Error locating customer row: {}", e.getMessage(), e);
		}
		return null;
	}

	public void validateCustomerTable() {
		List<Map<String, String>> customerData = tableUtils.getTableData(TABLE_LOCATOR,
				tableUtils.getTableHeaders(TABLE_LOCATOR));

		for (int i = 0; i < customerData.size(); i++) {
			Map<String, String> row = customerData.get(i);
			logger.debug("Row {}", (i + 1));
			for (Map.Entry<String, String> entry : row.entrySet()) {
				logger.debug("{}'th row - {}: {}", (i + 1), entry.getKey(), entry.getValue());
			}
		}
	}

	public boolean isSearchInputEnabled() {
		return driver.findElement(SEARCH_BOX_INPUT).isEnabled();
	}

	public boolean isSearchInputVisible() {
		return driver.findElement(SEARCH_BOX_INPUT).isDisplayed();
	}

	public boolean isSearchButtonEnabled() {
		return driver.findElement(SEARCH_BOX_BTN).isEnabled();
	}

	public boolean isSearchButtonVisible() {
		return driver.findElement(SEARCH_BOX_BTN).isDisplayed();
	}

	public boolean isEditButtonEnabled() {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(EYE_ICON)).isEnabled();
		// return wait.driver.findElement(EYE_ICON).isEnabled();
	}

	public boolean isEditButtonDisplayed() {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(EYE_ICON)).isDisplayed();
		// return driver.findElement(EYE_ICON).isDisplayed();
	}

	public boolean isDeleteButtonEnabled() {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(DELETE_ICON)).isEnabled();
		// return driver.findElement(DELETE_ICON).isEnabled();
	}

	public boolean isDeleteButtonDisplayed() {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(DELETE_ICON)).isDisplayed();
		// return driver.findElement(DELETE_ICON).isDisplayed();
	}

	public String emptyInputBoxErrorValidation() {
		clickElement(ADD_CUSTOMER_BTN);
		driver.findElement(By.id("customerName")).click();
		driver.findElement(By.className("component-header")).click();
		return driver.findElement(By.xpath("//div/mat-error")).getText();
	}

	public String wrongInputBoxErrorValidation() {
		driver.findElement(By.id("customerName")).click();
		driver.findElement(By.id("customerName")).clear();
		driver.findElement(By.id("customerName")).sendKeys("1234");
		return driver.findElement(By.xpath("//div/mat-error")).getText();
	}
}

