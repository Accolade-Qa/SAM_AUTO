package com.aepl.sam.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CalendarActions {
	private WebDriver driver;
	private WebDriverWait wait;
	private static final Logger logger = LogManager.getLogger(CalendarActions.class);

	public CalendarActions(WebDriver driver, WebDriverWait wait) {
		if (driver == null) {
			throw new IllegalArgumentException("WebDriver instance cannot be null");
		}
		this.driver = driver;
		this.wait = wait;
		logger.debug("CalendarActions initialized with driver and wait.");
	}

	public void selectDate(By calendarLocator, String targetDate) {
		if (calendarLocator == null || targetDate == null || targetDate.isEmpty()) {
			throw new IllegalArgumentException("calendarLocator and targetDate cannot be null or empty");
		}

		logger.info("Attempting to select date: {}", targetDate);

		try {
			logger.debug("Waiting for calendar element to be clickable.");
			WebElement calendarElement = wait.until(ExpectedConditions.elementToBeClickable(calendarLocator));
			calendarElement.click();
			logger.info("Calendar widget opened.");

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			LocalDate targetLocalDate = LocalDate.parse(targetDate, formatter);

			String targetDay = String.valueOf(targetLocalDate.getDayOfMonth());
			String targetMonth = targetLocalDate.getMonth().name().substring(0, 3).toUpperCase();
			String targetYear = String.valueOf(targetLocalDate.getYear());

			logger.info("Parsed target date - Day: {}, Month: {}, Year: {}", targetDay, targetMonth, targetYear);

			logger.debug("Opening date selector dropdown.");
			WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button/span/span")));
			dropdown.click();

			logger.debug("Selecting year: {}", targetYear);
			WebElement yearElement = wait.until(
					ExpectedConditions.elementToBeClickable(By.xpath("//button[@aria-label='" + targetYear + "']")));
			yearElement.click();

			logger.debug("Selecting month: {}", targetMonth);
			WebElement monthElement = wait.until(ExpectedConditions
					.elementToBeClickable(By.xpath("//span[contains(text(),'" + targetMonth + "')]")));
			monthElement.click();

			logger.debug("Selecting day: {}", targetDay);
			WebElement dayElement = wait
					.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(), '" + targetDay
							+ "') and contains(@class, 'mat-calendar-body-cell-content')]")));
			dayElement.click();

			logger.info("Date selection completed successfully for: {}", targetDate);

		} catch (Exception e) {
			logger.error("Failed to select date: {}. Error: {}", targetDate, e.getMessage(), e);
			throw new RuntimeException("Failed to select date: " + targetDate, e);
		}
	}
}
