package com.aepl.sam.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfigProperties {

	private static final Logger logger = LogManager.getLogger(ConfigProperties.class);

	private static Properties properties;
	private static String environment;
	private static final String CONFIG_FILE_FORMAT = "src/main/resources/%s.config.properties";
	private static final Map<String, String> ENV_OVERRIDES = new HashMap<>();

	static {
		ENV_OVERRIDES.put("username", "SAM_USERNAME");
		ENV_OVERRIDES.put("password", "SAM_PASSWORD");
		ENV_OVERRIDES.put("valid.username", "SAM_USERNAME");
		ENV_OVERRIDES.put("valid.password", "SAM_PASSWORD");
	}

	private ConfigProperties() {
	}

	public static synchronized void initialize(String env) {
		if (env == null || env.isEmpty()) {
			logger.error("Environment is null or empty during initialization.");
			throw new IllegalArgumentException("Environment must not be null or empty.");
		}

		environment = env.toLowerCase();
		String propertiesFile = String.format(CONFIG_FILE_FORMAT, environment);
		properties = new Properties();

		logger.info("Initializing ConfigProperties for environment: {}", environment);
		logger.debug("Loading properties from file: {}", propertiesFile);

		try (FileInputStream fis = new FileInputStream(propertiesFile)) {
			properties.load(fis);
			logger.info("Successfully loaded properties for '{}'", environment);
		} catch (IOException e) {
			logger.error("Failed to load properties file: {}", propertiesFile, e);
			throw new RuntimeException("Failed to load properties file: " + propertiesFile, e);
		}
	}

	public static String getProperty(String key) {
		if (properties == null) {
			logger.error("Attempted to access property before initialization.");
			throw new IllegalStateException("ConfigProperties is not initialized. Call initialize(env) first.");
		}

		String envKey = ENV_OVERRIDES.get(key);
		if (envKey != null) {
			String envValue = DotEnvUtil.get(envKey);
			if (envValue != null && !envValue.isBlank()) {
				logger.debug("Resolved property '{}' from env key '{}'.", key, envKey);
				return envValue;
			}
		}

		String value = properties.getProperty(key);
		logger.debug("Retrieved property '{}': '{}'", key, value);
		return value;
	}

	public static synchronized void reloadProperties() {
		if (environment == null) {
			logger.error("Reload failed. Environment not initialized.");
			throw new IllegalStateException("ConfigProperties is not initialized. Call initialize(env) first.");
		}

		logger.info("Reloading properties for environment: {}", environment);
		initialize(environment);
	}

	public static String getEnvironment() {
		logger.debug("Returning current environment: {}", environment);
		return environment;
	}

	public static void setProperty(String key, String value) {
		if (properties == null) {
			logger.error("Attempted to set property before initialization.");
			throw new IllegalStateException("ConfigProperties is not initialized. Call initialize(env) first.");
		}

		logger.info("Setting property '{}'", key);
		properties.setProperty(key, value);

		try (FileOutputStream fos = new FileOutputStream(String.format(CONFIG_FILE_FORMAT, environment))) {
			properties.store(fos, null);
			logger.info("Successfully saved property '{}' to file.", key);
		} catch (IOException e) {
			logger.error("Failed to save property '{}' to file.", key, e);
			throw new RuntimeException("Failed to save properties file", e);
		}
	}
}
