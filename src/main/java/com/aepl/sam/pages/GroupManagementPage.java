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
import com.aepl.sam.utils.RandomGeneratorUtils;

public class GroupManagementPage extends GroupManagementPageLocators {
	private final WebDriver driver;
	private final WebDriverWait wait;
	public String randomGroupName;
	private final RandomGeneratorUtils random;
	private static final Logger logger = LogManager.getLogger(GroupManagementPage.class);

	public GroupManagementPage(WebDriver driver, WebDriverWait wait) {
		this.driver = driver;
		this.wait = wait;
		this.random = new RandomGeneratorUtils();
		this.randomGroupName = random.generateRandomString(5);
	}

	public String navBarLink() {
		WebElement user = wait.until(ExpectedConditions.visibilityOfElementLocated(USER));
		user.click();
		logger.info("Clicked user menu");

		WebElement userRole = wait.until(ExpectedConditions.visibilityOfElementLocated(USER_ROLE_LINK));
		userRole.click();
		return driver.getCurrentUrl();
	}

	public String backButton() {
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(BACK_BUTTON));

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].style.border = 'solid purple'", element);

		element.click();
		logger.info("Clicked back button");
		return navBarLink();
	}

	public String refreshButton() {
		WebElement refreshBtn = wait.until(ExpectedConditions.elementToBeClickable(REFRESH_BUTTON));
		((JavascriptExecutor) driver).executeScript("arguments[0].style.border = 'solid purple'", refreshBtn);

		refreshBtn.click();
		logger.info("Clicked refresh button");

		WebElement pageTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(PAGE_TITLE));
		return pageTitle.getText();
	}

	public void addGroup() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollTo(0, -document.body.scrollHeight);");

		WebElement addUserRole = wait.until(ExpectedConditions.elementToBeClickable(ADD_ROLE_GRP));
		addUserRole.click();
		logger.info("Clicked add role group button");


		WebElement roleName = driver.findElement(ROLE_GRP_NAME);
		roleName.sendKeys(randomGroupName);
		logger.info("Entered group name: {}", randomGroupName);

		WebElement submitBtn = driver.findElement(SUBMIT_BTN);
		submitBtn.click();
		logger.info("Submitted new role group");

		backButton();
	}

	public void searchRoleGroup() {
		WebElement search;
		List<WebElement> roleList;

		search = driver.findElement(SEARCH_FIELD);
		roleList = driver.findElements(ROLE_TABLE);

		if (roleList.isEmpty()) {
			logger.warn("No roles found");
			return;
		}

		logger.info("Starting role group search...");

			for (int i = 0; i < roleList.size(); i++) {
				WebElement role = roleList.get(i);
				String roleText = role.getText().trim();

				logger.info("Searching for role: {}", roleText);

				search.clear();
				wait.until(ExpectedConditions.elementToBeClickable(search)).sendKeys(roleText);
				search.sendKeys(Keys.ENTER);

				roleList = driver.findElements(ROLE_TABLE);
				boolean roleFound = roleList.stream().anyMatch(r -> r.getText().trim().equals(roleText));

				if (roleFound) {
					logger.info("Role found: {}", roleText);
				} else {
					logger.warn("Role not found: {}", roleText);
				}

				search.clear();
			}
			logger.info("Role group search completed successfully");
	}

	public boolean isGroupManagementFound(String roleName) {
		List<WebElement> roleList = driver.findElements(ROLE_TABLE);

		for (WebElement role : roleList) {
			if (role.getText().trim().equalsIgnoreCase(roleName)) {
				logger.info("Role group found: {}", roleName);
				return true;
			}
		}
		logger.warn("Role group not found: {}", roleName);
		return false;

	}

	public String deleteRoleGroup() {
		List<WebElement> deleteButton = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(DELETE_BUTTON));
		WebElement last = deleteButton.get(deleteButton.size() - 1);

		last.click();
		logger.info("Clicked delete for group: {}", randomGroupName);

		Alert alert = driver.switchTo().alert();
		alert.accept();
		logger.info("Confirmed deletion alert");

		return "Role group deleted successfully";
	}
}

