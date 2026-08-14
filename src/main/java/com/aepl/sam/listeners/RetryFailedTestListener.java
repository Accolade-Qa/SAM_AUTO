package com.aepl.sam.listeners;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import com.aepl.sam.utils.ConfigProperties;

public class RetryFailedTestListener implements IRetryAnalyzer {

	private static final Logger logger = LogManager.getLogger(RetryFailedTestListener.class);
	private static final Map<String, Integer> retryCounts = new ConcurrentHashMap<>();

	private int getMaxRetryCount() {
		try {
			String configCount = ConfigProperties.getProperty("max_retry_count");
			if (configCount != null && !configCount.isBlank()) {
				return Integer.parseInt(configCount.trim());
			}
		} catch (Exception e) {
			logger.warn("Could not parse max_retry_count from config. Defaulting to 2.");
		}
		return 2;
	}

	@Override
	public boolean retry(ITestResult result) {
		String testIdentifier = result.getTestClass().getName() + "." + result.getMethod().getMethodName();
		int maxRetryCount = getMaxRetryCount();
		int currentCount = retryCounts.getOrDefault(testIdentifier, 0);

		if (currentCount < maxRetryCount) {
			currentCount++;
			retryCounts.put(testIdentifier, currentCount);
			logger.info("Retrying failed test: {} | Attempt: {}/{} | Thread: {}", testIdentifier, currentCount,
					maxRetryCount, Thread.currentThread().getName());
			return true;
		}

		retryCounts.remove(testIdentifier);
		logger.info("Max retry count ({}) reached for test: {}", maxRetryCount, testIdentifier);
		return false;
	}
}

