package com.aepl.sam.pages;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aepl.sam.locators.GroupManagementPageLocators;
import com.aepl.sam.utils.PageActionsUtil;
import com.aepl.sam.utils.RandomGeneratorUtils;

public class GroupManagementPage extends GroupManagementPageLocators {
	private WebDriver driver;
	private WebDriverWait wait;
	private PageActionsUtil comm;
	public String randomGroupName;
	private RandomGeneratorUtils random;
	private static final Logger logger = LogManager.getLogger(GroupManagementPage.class);

	public GroupManagementPage(WebDriver driver, WebDriverWait wait) {
		this.driver = driver;
		this.wait = wait;
		this.comm = new PageActionsUtil(driver, wait);
		this.random = new RandomGeneratorUtils();
		this.randomGroupName = random.generateRandomString(5);
	}

	public String navBarLink() {
		try {
			WebElement user = wait.until(ExpectedConditions.visibilityOfElementLocated(USER));
			user.click();
			logger.info("Clicked user menu");

			WebElement userRole = wait.until(ExpectedConditions.visibilityOfElementLocated(USER_ROLE_LINK));
			userRole.click();
			logger.info("Clicked user role link");
		} catch (Exception e) {
			logger.error("Error navigating to user role: {}", e.getMessage(), e);
		}
		return driver.getCurrentUrl();
	}

	public String backButton() {
		try {
			WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(BACK_BUTTON));

			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].style.border = 'solid purple'", element);

			element.click();
			logger.info("Clicked back button");
			Thread.sleep(1000);
		} catch (Exception e) {
			logger.error("Error clicking back button: {}", e.getMessage(), e);
		}
		return navBarLink();
	}

	public String refreshButton() {
		try {
			WebElement refreshBtn = wait.until(ExpectedConditions.elementToBeClickable(REFRESH_BUTTON));
			((JavascriptExecutor) driver).executeScript("arguments[0].style.border = 'solid purple'", refreshBtn);
			Thread.sleep(1000);

			refreshBtn.click();
			logger.info("Clicked refresh button");

			WebElement pageTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(PAGE_TITLE));
			return pageTitle.getText();
		} catch (Exception e) {
			logger.error("Error clicking refresh button: {}", e.getMessage(), e);
		}
		return "No Data Found!!!";
	}

	public void addGroup() {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("window.scrollTo(0, -document.body.scrollHeight);");
			Thread.sleep(1000);

			WebElement addUserRole = wait.until(ExpectedConditions.elementToBeClickable(ADD_ROLE_GRP));
			addUserRole.click();
			logger.info("Clicked add role group button");

			Thread.sleep(1000);

			WebElement roleName = driver.findElement(ROLE_GRP_NAME);
			roleName.sendKeys(randomGroupName);
			logger.info("Entered group name: {}", randomGroupName);

			WebElement submitBtn = driver.findElement(SUBMIT_BTN);
			submitBtn.click();
			logger.info("Submitted new role group");

			backButton();
		} catch (Exception e) {
			logger.error("Error while adding role group: {}", e.getMessage(), e);
		}
	}

	public void searchRoleGroup() {
		WebElement search;
		List<WebElement> roleList;

		try {
			search = driver.findElement(SEARCH_FIELD);
			roleList = driver.findElements(ROLE_TABLE);

			if (roleList.isEmpty()) {
				logger.warn("No roles found");
				return;
			}

			logger.info("Starting role group search...");

			for (int i = 0; i < roleList.size(); i++) {
				try {
					WebElement role = roleList.get(i);
					String roleText = role.getText().trim();

					logger.info("Searching for role: {}", roleText);

					search.clear();
					wait.until(ExpectedConditions.elementToBeClickable(search)).sendKeys(roleText);
					search.sendKeys(Keys.ENTER);
					Thread.sleep(500);

					roleList = driver.findElements(ROLE_TABLE);
					boolean roleFound = roleList.stream().anyMatch(r -> r.getText().trim().equals(roleText));

					if (roleFound) {
						logger.info("Role found: {}", roleText);
					} else {
						logger.warn("Role not found: {}", roleText);
					}

					search.clear();
					Thread.sleep(500);

				} catch (Exception e) {
					logger.error("Error processing role at index {}: {}", i, e.getMessage(), e);
				}
			}
			logger.info("Role group search completed successfully");
		} catch (Exception e) {
			logger.error("Error during role group search: {}", e.getMessage(), e);
		}
	}

	public boolean isGroupManagementFound(String roleName) {
		try {
			List<WebElement> roleList = driver.findElements(ROLE_TABLE);

			for (WebElement role : roleList) {
				if (role.getText().trim().equalsIgnoreCase(roleName)) {
					logger.info("Role group found: {}", roleName);
					return true;
				}
			}
			logger.warn("Role group not found: {}", roleName);
			return false;
		} catch (Exception e) {
			logger.error("Error checking if role group exists: {}", e.getMessage(), e);
			return false;
		}
	}

	public String deleteRoleGroup() {
		try {
			List<WebElement> deleteButton = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(DELETE_BUTTON));
			WebElement last = deleteButton.get(deleteButton.size() - 1);
			Thread.sleep(1000);

			last.click();
			logger.info("Clicked delete for group: {}", randomGroupName);

			Alert alert = driver.switchTo().alert();
			alert.accept();
			logger.info("Confirmed deletion alert");

			return "Role group deleted successfully";
		} catch (Exception e) {
			logger.error("Error deleting role group: {}", e.getMessage(), e);
			return "Error while deleting role group";
		}
	}
}

