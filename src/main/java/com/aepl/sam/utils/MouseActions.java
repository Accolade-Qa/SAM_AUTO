package com.aepl.sam.utils;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MouseActions {

	private WebDriver driver;
	private Actions actions;
	private static final Logger logger = LogManager.getLogger(MouseActions.class);

	public MouseActions(WebDriver driver) {
		if (driver == null) {
			throw new IllegalArgumentException("WebDriver instance cannot be null");
		}
		this.driver = driver;
		this.actions = new Actions(this.driver);
		logger.debug("MouseActions initialized with WebDriver.");
	}

	public void moveToElement(WebElement element) {
		if (element == null) {
			throw new IllegalArgumentException("WebElement cannot be null");
		}

		try {
			new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOf(element));
			actions.moveToElement(element).perform();
			logger.info("Moved to element: {}", element);
		} catch (Exception e) {
			logger.error("Failed to move to element: {}", e.getMessage(), e);
		}
	}

	public void clickElement(WebElement element) {
		if (element == null) {
			throw new IllegalArgumentException("WebElement cannot be null");
		}

		try {
			new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOf(element));
			actions.click(element).build().perform();
			logger.info("Clicked on element: {}", element);
		} catch (Exception e) {
			logger.error("Failed to click element: {}", e.getMessage(), e);
		}
	}

	public void hoverOverElement(WebElement element) {
		if (element == null) {
			throw new IllegalArgumentException("WebElement cannot be null");
		}

		try {
			actions.moveToElement(element).build().perform();
			logger.info("Hovered over element: {}", element);
		} catch (Exception e) {
			logger.error("Failed to hover over element: {}", e.getMessage(), e);
			throw e;
		}
	}

	public void doubleClickElement(WebElement element) {
		if (element == null) {
			throw new IllegalArgumentException("WebElement cannot be null");
		}

		try {
			Thread.sleep(1000);
			actions.doubleClick(element).build().perform();
			logger.info("Double-clicked on element: {}", element);
		} catch (Exception e) {
			logger.error("Failed to double-click element: {}", e.getMessage(), e);
		}
	}

	public void rightClickElement(WebElement element) {
		if (element == null) {
			throw new IllegalArgumentException("WebElement cannot be null");
		}

		try {
			actions.contextClick(element).build().perform();
			logger.info("Right-clicked on element: {}", element);
		} catch (Exception e) {
			logger.error("Failed to right-click element: {}", e.getMessage(), e);
			throw e;
		}
	}

	public void selectAndCopy(WebElement startElement, WebElement endElement) {
		if (startElement == null || endElement == null) {
			throw new IllegalArgumentException("WebElement(s) cannot be null");
		}

		try {
			actions.clickAndHold(startElement).moveToElement(endElement).release().build().perform();
			actions.sendKeys(Keys.CONTROL, "c").build().perform();
			logger.info("Selected content from {} to {} and copied to clipboard.", startElement, endElement);
		} catch (Exception e) {
			logger.error("Failed to select and copy: {}", e.getMessage(), e);
			throw e;
		}
	}

	public void pasteText(WebElement targetElement) {
		if (targetElement == null) {
			throw new IllegalArgumentException("WebElement cannot be null");
		}

		try {
			actions.moveToElement(targetElement).click().build().perform();
			actions.sendKeys(Keys.CONTROL, "v").build().perform();
			logger.info("Pasted text into element: {}", targetElement);
		} catch (Exception e) {
			logger.error("Failed to paste text: {}", e.getMessage(), e);
			throw e;
		}
	}

	public void dragAndDrop(WebElement source, WebElement target) {
		if (source == null || target == null) {
			throw new IllegalArgumentException("Source or target WebElement cannot be null");
		}

		try {
			actions.dragAndDrop(source, target).build().perform();
			logger.info("Dragged element from {} to {}", source, target);
		} catch (Exception e) {
			logger.error("Failed to drag and drop: {}", e.getMessage(), e);
			throw e;
		}
	}

	public void dragAndDropByOffset(WebElement source, int xOffset, int yOffset) {
		if (source == null) {
			throw new IllegalArgumentException("Source WebElement cannot be null");
		}

		try {
			actions.dragAndDropBy(source, xOffset, yOffset).build().perform();
			logger.info("Dragged element {} by offset x: {}, y: {}", source, xOffset, yOffset);
		} catch (Exception e) {
			logger.error("Failed to drag and drop by offset: {}", e.getMessage(), e);
			throw e;
		}
	}
}
