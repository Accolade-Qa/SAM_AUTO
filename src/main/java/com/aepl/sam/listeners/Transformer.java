package com.aepl.sam.listeners;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

public class Transformer implements IAnnotationTransformer {

	private static final Logger logger = LogManager.getLogger(Transformer.class);

	@Override
	@SuppressWarnings({"rawtypes"})
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
		String methodName = (testMethod != null) ? testMethod.getName() : "Unknown Method";

		annotation.setRetryAnalyzer(RetryFailedTestListener.class);
		logger.info("Retry analyzer set for method: {}", methodName);
	}
}
