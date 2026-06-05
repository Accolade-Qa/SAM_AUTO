package com.aepl.sam.locators;

import org.openqa.selenium.By;

public class ProductionDevicePageLocators extends CommonPageLocators {

	// Production Device Page

	public static final By ADD_PRODUCTION_DEVICE = By.xpath("//button[@class='primary-button']");
	public static final By PRODUCTION_DEVICES = By.xpath("//a[contains(text(),'PRODUCTION DEVICE')]");
	public static final By MODEL_TO_SEARCH = By.xpath("//td[normalize-space()='ACON4IA202200049619']");

	// Create Production Device

	public static final By ADD_PROD_DEVICE = By.xpath("//button[@class='primary-button']");
	public static final By UID = By.xpath("//input[@id='uid']");
	public static final By IMEI = By.xpath("//input[@id='imei']");
	public static final By ICCID = By.xpath("//input[@id='iccid']");
	public static final By DEVICE_MODEL_NAME = By.xpath("//mat-select[contains(@formcontrolname, 'modelName')]");
	public static final By DEVICE_MODEL_OPTIONS = By.xpath("//div/div/mat-option");
	public static final By FIRMWARE = By.xpath("//input[@id='firmware' or formcontrolname='firmware']");
	public static final By MOBILE_NUMBER = By.xpath("//input[@id='mobile']");
	public static final By ALT_MOBILE_NO = By.xpath("//input[@id='altMobile']");
	public static final By SERVICE_PROVIDER = By
			.xpath("//input[@id='serviceProvider' or formcontrolname='serviceProvider']");
	public static final By ALT_SERVICE_PROVIDER = By.xpath("//input[@id='altServiceProvider']");
	public static final By BOOTSTRAP_EXPIRY_DATE = By.xpath("//input[@id='bootstrapExpDate1']");
	public static final By CAL_BTN = By.xpath("//button[@aria-label='Open calendar']");
	public static final By SIM_VENDOR = By.xpath("//input[@id='simVendor' or formcontrolname='simVendor']");

	// Submit Button

	public static final By SUBMIT_BTN = By.xpath("//button[contains(text(), 'Submit')]");
	public static final By UPDATE_BTN = By.xpath("//button[contains(text(), 'Update')]");
	public static final By REFRESH_BTN = By.xpath("//mat-icon[normalize-space()='refresh']");
	public static final By TOAST_MSG = By.xpath("//simple-snack-bar/div[1]");
	public static final By BULK_UPLOAD = By.xpath("//button[@class='primary-button'][2]");
	public static final By DOWNLOAD_SAMPLE = By.xpath("//button[@class='primary-button' and contains(text(), 'Download')]");
	public static final By SEARCHED_UIN_DEVICE = By.xpath("//button[@class='primary-button' and contains(text(), 'Download')]");
	
}
