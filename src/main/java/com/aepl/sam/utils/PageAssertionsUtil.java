package com.aepl.sam.utils;

import java.time.Duration;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import static com.aepl.sam.locators.CommonPageLocators.*;

public class PageAssertionsUtil {

	private static final Logger logger = LogManager.getLogger(PageAssertionsUtil.class);

	private final WebDriver driver;
	private final WebDriverWait wait;
	private final PageActionsUtil actions;
	private final SoftAssert softAssert = new SoftAssert();

	public PageAssertionsUtil(WebDriver driver, WebDriverWait wait) {
		this.driver = driver;
		this.wait = (wait != null) ? wait : new WebDriverWait(driver, Duration.ofSeconds(10));
		this.actions = new PageActionsUtil(driver, this.wait);
	}

	public boolean verifyWebpageLogo() {
		try {
			WebElement logo = wait.until(ExpectedConditions.visibilityOfElementLocated(ORG_LOGO));
			actions.highlightElement(logo, "solid purple");
			return logo.isDisplayed();
		} catch (TimeoutException e) {
			logger.error("Logo element was not visible in time.", e);
			return false;
		}
	}

	public String verifyPageTitle() {
		String expectedTitle = "AEPL Sampark Diagnostic Cloud";
		WebElement titleElement = wait.until(ExpectedConditions.visibilityOfElementLocated(PROJECT_TITLE));
		actions.highlightElement(titleElement, "solid purple");
		String actualTitle = titleElement.getText();
		if (!actualTitle.equalsIgnoreCase(expectedTitle)) {
			throw new RuntimeException(
					"Project title does not match. Expected: " + expectedTitle + ", but found: " + actualTitle);
		}
		return actualTitle;
	}

	public String validateComponents() {
		try {
			WebElement headerContainer = driver.findElement(HEADER_CONTAINER);
			WebElement pageHeader = driver.findElement(PAGE_HEADER);
			WebElement componentContainer = driver.findElement(COMPONENT_CONTAINER);
			WebElement separator = driver.findElement(SEPARATOR);
			WebElement footer = driver.findElement(FOOTER);

			actions.highlightElement(headerContainer, "solid purple");
			actions.highlightElement(pageHeader, "solid purple");
			actions.highlightElement(componentContainer, "solid purple");
			actions.highlightElement(separator, "solid purple");
			actions.highlightElement(footer, "solid purple");

			softAssert.assertTrue(headerContainer.isDisplayed(), "No header container is displayed");
			softAssert.assertTrue(pageHeader.isDisplayed(), "No pageHeader container is displayed");
			softAssert.assertTrue(componentContainer.isDisplayed(), "No componentContainer container is displayed");
			softAssert.assertTrue(separator.isDisplayed(), "No separator container is displayed");
			softAssert.assertTrue(footer.isDisplayed(), "No footer container is displayed");

			return "All components are displayed and validated successfully.";
		} catch (Exception e) {
			logger.error("Error validating page components.", e);
			return "Error validating components: " + e.getMessage();
		}
	}

	public String validateButtons() {
		try {
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(ALL_BTN));
			List<WebElement> buttons = driver.findElements(ALL_BTN);

			softAssert.assertFalse(buttons.isEmpty(), "No buttons found on the page!");

			for (WebElement button : buttons) {
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", button);
				String label = button.getText().isBlank() ? button.getDomAttribute("aria-label") : button.getText();
				softAssert.assertTrue(button.isDisplayed(), "Button not displayed: " + label);
				softAssert.assertTrue(button.isEnabled(), "Button not enabled: " + label);
				actions.highlightElement(button, "solid purple");
			}

			((JavascriptExecutor) driver).executeScript("window.scrollTo(0,0)");
			return "All buttons are displayed and enabled successfully.";
		} catch (Exception e) {
			logger.error("Error validating page buttons.", e);
			return "Error validating buttons: " + e.getMessage();
		}
	}

	public String checkCopyright() {
		WebElement copyRight = driver.findElement(COPYRIGHT);
		actions.highlightElement(copyRight, "solid purple");
		return copyRight.getText();
	}

	public String checkVersion() {
		try {
			WebElement version = driver.findElement(VERSION);
			actions.highlightElement(version, "solid purple");
			return version.getText();
		} catch (NoSuchElementException e) {
			return "No version was found on page!!!";
		}
	}
}
