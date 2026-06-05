package com.aepl.sam.locators;

import org.openqa.selenium.By;

public class UserManagementPageLocators extends CommonPageLocators {

	// User Management
	public static final By USR_MANAGEMENT_LINK = By.xpath("//li/a[contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'user management')]");
	public static final By ADD_USR_BTN = By.xpath("//button[contains(text(), 'Add')]");
	public static final By DRP_DOWN_BTN = By.xpath("//mat-select[@role=\"combobox\"]");

	// Add user Button
	public static final By USR_TYPE = By.xpath("//mat-select[@formcontrolname=\"roleId\"]");
	public static final By FIRST_NAME = By.xpath("//input[contains(@formcontrolname, 'firstName')]");
	public static final By LAST_NAME = By.xpath("//input[contains(@formcontrolname, 'lastName')]");
	public static final By EMAIL = By.xpath("//input[contains(@formcontrolname, 'userEmail')]");
	public static final By MOBILE = By.xpath("//input[contains(@formcontrolname, 'mobileNumber')]");
	public static final By COUNTRY = By.xpath("//input[contains(@formcontrolname, 'country')]");
	public static final By STATE = By.xpath("//input[contains(@formcontrolname, 'state')]");
	public static final By STATUS = By.xpath("//mat-select[contains(@formcontrolname, 'status')]");
	public static final By USR_TYPE_OPTIONS = By.xpath("//div/div/mat-option");

	// Update Profile Section
	public static final By PROF_BTN = By.xpath("//button[contains(text(), 'Upload Profile Picture')]");

	// Submit Button
	public static final By SUBMIT_BTN = By.xpath("//button[contains(text(), 'Save User Details')]");
	public static final By UPDATE_BTN = By.xpath("//button[contains(text(), 'Update User Details')]");

	public static final By SELECT = By.xpath("//div/span[1][contains(text(),'Select Role')]");
	public static final By DRP_OPTION = By.xpath("//div/div[2]/div[2]/ul/li");
	public static final By NO_DATA_IMAGE = By.xpath("//app-common-component-dropdown-search//img[@alt='No Data Found']");
	public static final By TOAST_MESSAGE = By.xpath("//simple-snack-bar/div");
}
