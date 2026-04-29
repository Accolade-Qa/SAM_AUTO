package com.aepl.sam.utils;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {

	private static volatile ExtentReports extent;
	private static final Logger logger = LogManager.getLogger(ExtentManager.class);
	private static final Object lock = new Object();

	public static ExtentReports createInstance() {
		if (extent != null) {
			String errorMsg = "ExtentReports instance already created. Use getInstance() instead.";
			logger.error(errorMsg);
			throw new IllegalStateException(errorMsg);
		}

		String filePath = System.getProperty("user.dir") + "/Results/test-results/ExtentReport.html";
		logger.info("Initializing ExtentReports at path: {}", filePath);

		File reportFile = new File(filePath);
		if (!reportFile.getParentFile().exists()) {
			if (reportFile.getParentFile().mkdirs()) {
				logger.info("Created directories for report file.");
			} else {
				String errorMsg = "Failed to create directories for report file: " + filePath;
				logger.error(errorMsg);
				throw new IllegalStateException(errorMsg);
			}
		}

		try {
			ExtentSparkReporter sparkReporter = new ExtentSparkReporter(filePath);
			sparkReporter.config().setDocumentTitle("Sampark Automation Test Report");
			sparkReporter.config().setReportName("SAM Test Execution Report");
			sparkReporter.config().setTheme(com.aventstack.extentreports.reporter.configuration.Theme.DARK);

			extent = new ExtentReports();
			extent.attachReporter(sparkReporter);
			extent.setSystemInfo("Environment",
					System.getenv("ENVIRONMENT") != null ? System.getenv("ENVIRONMENT") : "QA");
			extent.setSystemInfo("User", System.getProperty("user.name"));
			extent.setSystemInfo("OS", System.getProperty("os.name"));
			extent.setAnalysisStrategy(AnalysisStrategy.TEST);

			logger.info("ExtentReports instance created successfully.");

		} catch (Exception e) {
			logger.error("Error initializing ExtentReports: {}", e.getMessage(), e);
			throw new RuntimeException("Failed to initialize ExtentReports", e);
		}

		return extent;
	}

	public static ExtentReports getInstance() {
		if (extent == null) {
			synchronized (lock) {
				if (extent == null) {
					logger.warn("ExtentReports instance not found. Creating new instance...");
					createInstance();
				}
			}
		}
		return extent;
	}

	public static synchronized void flush() {
		if (extent == null) {
			String errorMsg = "ExtentReports instance is null. Cannot flush reports.";
			logger.error(errorMsg);
			throw new IllegalStateException(errorMsg);
		}
		logger.info("Flushing ExtentReports to output file...");
		extent.flush();
		logger.info("ExtentReports flushed successfully.");
	}
}
