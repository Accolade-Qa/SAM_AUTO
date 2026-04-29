package com.aepl.sam.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryFailedTestListener implements IRetryAnalyzer {

	private static final ThreadLocal<Integer> retryCountLocal = ThreadLocal.withInitial(() -> 0);
	private static final int maxRetryCount = 3;
	private static final Logger logger = LogManager.getLogger(RetryFailedTestListener.class);

	@Override
	public boolean retry(ITestResult result) {
		int currentRetryCount = retryCountLocal.get();

		if (currentRetryCount < maxRetryCount) {
			currentRetryCount++;
			retryCountLocal.set(currentRetryCount);
			logger.info("Retrying test: {} | Attempt: {} | Thread: {}", result.getName(), currentRetryCount,
					Thread.currentThread().getName());
			return true;
		}

		// Clean up ThreadLocal after max retries reached
		retryCountLocal.remove();
		logger.info("Max retry count ({}) reached for test: {}", maxRetryCount, result.getName());
		return false;
	}
}
