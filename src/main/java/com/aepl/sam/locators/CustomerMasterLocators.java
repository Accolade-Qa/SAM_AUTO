package com.aepl.sam.locators;

import org.openqa.selenium.By;

public class CustomerMasterLocators extends CommonPageLocators {
	public static final String CUSTOMER_MASTER_URL = "https://sampark-qa.accoladeelectronics.com/customer-master";
	public static final By CUSTOMER_MASTER_LINK = By.xpath(
			"//a[contains(translate(text(), 'abcdefghijklmnopqrstuvwxyz', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'), 'CUSTOMER MASTER')]");
	public static final By ADD_CUSTOMER_BTN = By.xpath(
			"//button[contains(translate(text(), 'abcdefghijklmnopqrstuvwxyz', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'), 'ADD')]");
	public static final By CUSTOMER_NAME = By
			.xpath("""
					//input[
						  translate(@formcontrolname, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz') = 'customername'
						  and
						  contains(translate(@placeholder, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'customer name')
						]
					""");
	public static final By SAVE_BTN = By.xpath(
			"//button[contains(translate(normalize-space(.), 'abcdefghijklmnopqrstuvwxyz', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'), 'SUBMIT')]");

	public static final By SEARCH_CUSTOMER = By.xpath("//input[@formcontrolname= 'searchInput']");
	public static final By SEARCH_BTN = By.xpath("//button/mat-icon[contains(text(),'search')]");

	public static final By DATA = By.xpath("//table/tbody/tr/td[1]");
	public static final By D_DATA = By.xpath("//table/tbody/tr/td[1]");

	public static final By EDIT_BTN = By.xpath("//button/mat-icon[contains(text(),'visibility')]");
	public static final By UPDATE_BTN = By.xpath("//button[contains(text(),'Update')]");

	public static final By DELETE_BTN = By.xpath("//button/mat-icon[contains(text(),'delete')]");

	public static final By TABLE_LOCATOR = By.xpath("//table");
}
