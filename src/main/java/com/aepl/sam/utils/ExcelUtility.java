package com.aepl.sam.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtility {

	private static final Logger logger = LogManager.getLogger(ExcelUtility.class);

	private Workbook workbook;
	private Sheet sheet;
	private String filePath;

	public void initializeExcel(String sheetName) {
		filePath = System.getProperty("user.dir") + "/Results/test-results/" + sheetName + ".xlsx";
		try {
			File file = new File(filePath);
			logger.info("Initializing Excel file at path: {}", filePath);

			if (!file.getParentFile().exists()) {
				logger.debug("Parent directory does not exist. Creating directories.");
				file.getParentFile().mkdirs();
			}

			if (file.exists() && file.length() > 0) {
				logger.info("Excel file exists. Loading existing file.");
				try (FileInputStream fis = new FileInputStream(file)) {
					workbook = new XSSFWorkbook(fis);
					sheet = workbook.getSheetAt(0);
					logger.debug("Loaded existing Excel sheet: {}", sheet.getSheetName());
				}
			} else {
				logger.info("Excel file does not exist. Creating new workbook and sheet.");
				workbook = new XSSFWorkbook();
				sheet = workbook.createSheet("Test Results");
				createHeaderRow();
				logger.debug("Created new sheet with header row.");
			}
		} catch (IOException e) {
			logger.error("Failed to initialize Excel file: {}", filePath, e);
			throw new RuntimeException("Failed to initialize Excel file: " + filePath);
		}
	}

	private void createHeaderRow() {
		logger.debug("Creating header row.");
		Row headerRow = sheet.createRow(0);
		CellStyle headerStyle = workbook.createCellStyle();
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setColor(IndexedColors.BLACK.getIndex());
		headerStyle.setFont(headerFont);

		String[] headers = { "Test Case Name", "Expected Message", "Actual Message", "Status" };
		for (int i = 0; i < headers.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(headers[i]);
			cell.setCellStyle(headerStyle);
		}
		logger.debug("Header row created with columns: {}", String.join(", ", headers));
	}

	public void writeTestDataToExcel(String testCaseName, String expected, String actual, String status) {
		if (workbook == null || sheet == null) {
			logger.error("Attempted to write data without initializing Excel file.");
			throw new IllegalStateException("Excel file is not initialized. Call initializeExcel() first.");
		}

		try {
			logger.info("Writing test data: [{}] [{}] [{}] [{}]", testCaseName, expected, actual, status);

			int rowCount = sheet.getPhysicalNumberOfRows();
			Row row = sheet.createRow(rowCount);

			row.createCell(0).setCellValue(testCaseName);
			row.createCell(1).setCellValue(expected);
			row.createCell(2).setCellValue(actual);

			Cell statusCell = row.createCell(3);
			statusCell.setCellValue(status);

			CellStyle statusStyle = workbook.createCellStyle();
			if ("Fail".equalsIgnoreCase(status)) {
				Font failFont = workbook.createFont();
				failFont.setColor(IndexedColors.RED.getIndex());
				statusStyle.setFont(failFont);
			}
			statusCell.setCellStyle(statusStyle);

			try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
				workbook.write(fileOut);
				logger.debug("Successfully wrote data to Excel file.");
			}

		} catch (IOException e) {
			logger.error("Failed to write data to Excel file at path: {}", filePath, e);
			throw new RuntimeException("Failed to write data to Excel file.");
		}
	}
}
