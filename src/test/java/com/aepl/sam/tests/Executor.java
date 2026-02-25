package com.aepl.sam.tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.asserts.SoftAssert;

import com.aepl.sam.utils.ExcelUtility;
import com.aepl.sam.utils.Result;

public class Executor {
	private static final Logger logger = LogManager.getLogger(Executor.class);
	private ExcelUtility excelUtility;
	private SoftAssert softAssert;

	public Executor(ExcelUtility excelUtility, SoftAssert softAssert) {
		this.excelUtility = excelUtility;
		this.softAssert = softAssert;
	}

	public <T> void executeTest(String testCaseName, T expected, Supplier<T> actualSupplier) {
		T actual = null;
		Result result = Result.FAIL;
		logger.info("Executing test case: {}", testCaseName);

		try {
			actual = actualSupplier.get();
			boolean isPass = compareValues(expected, actual);

			softAssert.assertTrue(isPass, testCaseName + " failed! Expected: " + expected + " but got: " + actual);

			result = isPass ? Result.PASS : Result.FAIL;
			logger.info("Test result: {}", result.getValue());

		} catch (Exception e) {
			logger.error("Error in test case {}: {}", testCaseName, e.getMessage(), e);
			result = Result.ERROR;
		} finally {
			synchronized (excelUtility) {
				excelUtility.writeTestDataToExcel(testCaseName, expected != null ? expected.toString() : "null",
						actual != null ? actual.toString() : "null", result.getValue());
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private boolean compareValues(Object expected, Object actual) {
		if (expected == null && actual == null) {
			return true;
		}
		if (expected == null || actual == null) {
			return false;
		}

		if (expected instanceof String && actual instanceof String) {
			return ((String) expected).equalsIgnoreCase((String) actual);
		}

		if (expected.getClass().isArray() && actual.getClass().isArray()) {
			return Arrays.deepEquals(wrapArray(expected), wrapArray(actual));
		}

		if (expected instanceof Collection && actual instanceof Collection) {
			return new ArrayList<>((Collection<?>) expected).equals(new ArrayList<>((Collection<?>) actual));
		}

		if (expected instanceof Map && actual instanceof Map) {
			Map<?, ?> expMap = (Map<?, ?>) expected;
			Map<?, ?> actMap = (Map<?, ?>) actual;

			if (expMap.size() != actMap.size()) {
				return false;
			}

			for (Object key : expMap.keySet()) {
				Object expVal = expMap.get(key);
				Object actVal = actMap.get(key);

				if (!String.valueOf(expVal).equalsIgnoreCase(String.valueOf(actVal))) {
					logger.debug("Value mismatch for key {} â†’ expected: {}, actual: {}", key, expVal, actVal);
					return false;
				}
			}
			return true;
		}

		// Fallback to string-based equality for mismatched types
		return String.valueOf(expected).equalsIgnoreCase(String.valueOf(actual));
	}

	private Object[] wrapArray(Object array) {
		if (array instanceof Object[]) {
			return (Object[]) array;
		}
		int length = java.lang.reflect.Array.getLength(array);
		Object[] result = new Object[length];
		for (int i = 0; i < length; i++) {
			result[i] = java.lang.reflect.Array.get(array, i);
		}
		return result;
	}

}
