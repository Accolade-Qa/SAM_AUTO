package com.aepl.sam.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

public class ConfigProperties {

	private static final Logger logger = LogManager.getLogger(ConfigProperties.class);

	private static Properties properties;
	private static String environment;
	private static String project;
	private static final String CONFIG_FILE_FORMAT = "src/main/resources/%s.config.properties";
	private static final Map<String, String> ENV_OVERRIDES = new HashMap<>();
	private static final Map<String, Object> yamlConfig = new HashMap<>();

	static {
		ENV_OVERRIDES.put("username", "SAM_USERNAME");
		ENV_OVERRIDES.put("password", "SAM_PASSWORD");
		ENV_OVERRIDES.put("valid.username", "SAM_USERNAME");
		ENV_OVERRIDES.put("valid.password", "SAM_PASSWORD");
		ENV_OVERRIDES.put("qa_man", "SAM_QA_MANAGER_USERNAME");
		ENV_OVERRIDES.put("qa_pass", "SAM_QA_MANAGER_PASSWORD");
		ENV_OVERRIDES.put("soft_man", "SAM_SOFT_MANAGER_USERNAME");
		ENV_OVERRIDES.put("soft_pass", "SAM_SOFT_MANAGER_PASSWORD");
		ENV_OVERRIDES.put("current.password", "SAM_CURRENT_PASSWORD");
		ENV_OVERRIDES.put("new.password", "SAM_NEW_PASSWORD");
	}

	private ConfigProperties() {
	}

	public static synchronized void initialize(String env) {
		if (env == null || env.isEmpty()) {
			logger.error("Environment is null or empty during initialization.");
			throw new IllegalArgumentException("Environment must not be null or empty.");
		}

		environment = env.toLowerCase();
		project = System.getProperty("project", System.getenv("PROJECT"));
		if (project == null || project.isEmpty()) {
			project = "sampark";
		}
		project = project.toLowerCase();

		logger.info("Initializing ConfigProperties for environment: {} and project: {}", environment, project);

		// Step 1: Load Project YAML Configuration
		loadYamlConfig(project);

		// Step 2: Load Environment properties file
		String propertiesFile = String.format(CONFIG_FILE_FORMAT, environment);
		properties = new Properties();

		logger.debug("Loading properties from file: {}", propertiesFile);
		try (FileInputStream fis = new FileInputStream(propertiesFile)) {
			properties.load(fis);
			logger.info("Successfully loaded properties for '{}'", environment);
		} catch (IOException e) {
			logger.warn("Failed to load properties file: {}. Continuing with YAML and Env defaults.", propertiesFile, e);
		}

		// Step 3: Propagate dynamic values to Constants class
		propagateConstants();
	}

	@SuppressWarnings("unchecked")
	private static void loadYamlConfig(String projectName) {
		yamlConfig.clear();
		String yamlFileName = projectName + ".yaml";
		
		// Search paths for project YAML file
		File[] searchPaths = {
			new File("config/" + yamlFileName),
			new File("../ALL_PROJECTS_AUTOMATION/config/" + yamlFileName),
			new File("src/main/resources/" + yamlFileName)
		};

		File foundFile = null;
		for (File path : searchPaths) {
			if (path.exists()) {
				foundFile = path;
				break;
			}
		}

		if (foundFile != null) {
			logger.info("Loading YAML configuration from file: {}", foundFile.getAbsolutePath());
			try (InputStream is = new FileInputStream(foundFile)) {
				Yaml yaml = new Yaml();
				Map<String, Object> data = yaml.load(is);
				if (data != null) {
					yamlConfig.putAll(data);
				}
				logger.info("Successfully loaded YAML config. Keys found: {}", yamlConfig.keySet());
			} catch (Exception e) {
				logger.error("Error loading YAML configuration from file: {}", foundFile.getAbsolutePath(), e);
			}
		} else {
			// Try classpath/resources
			logger.warn("YAML file not found on disk: {}. Attempting to load from classpath resources...", yamlFileName);
			try (InputStream is = ConfigProperties.class.getClassLoader().getResourceAsStream(yamlFileName)) {
				if (is != null) {
					Yaml yaml = new Yaml();
					Map<String, Object> data = yaml.load(is);
					if (data != null) {
						yamlConfig.putAll(data);
					}
					logger.info("Successfully loaded YAML config from classpath resource.");
				} else {
					logger.error("Project YAML configuration file {} not found anywhere.", yamlFileName);
				}
			} catch (Exception e) {
				logger.error("Error loading YAML configuration from classpath resource: {}", yamlFileName, e);
			}
		}
	}

	private static void propagateConstants() {
		try {
			String baseUrl = getProperty("base_url");
			if (baseUrl != null && !baseUrl.isBlank()) {
				Constants.BASE_URL = baseUrl;
				Constants.LOGIN_URL = baseUrl + "/login";
				Constants.EXP_FRGT_PWD_URL = baseUrl + "/login";
				Constants.DASH_URL = baseUrl + "/device-dashboard-page";
				Constants.GOV_LINK = baseUrl + "/govt-servers";
				Constants.DEVICE_LINK = baseUrl + "/model";
				Constants.ADD_MODEL_LINK = baseUrl + "/model-firmware";
				Constants.USR_MAN = baseUrl + "/user-tab";
				Constants.USR_PROFILE = baseUrl + "/profile";
				Constants.ROLE_MANAGEMENT = baseUrl + "/user-role";
				Constants.PROD_DEVICE_LINK = baseUrl + "/production-device-page";
				Constants.DISP_DEVICE_LINK = baseUrl + "/dispatch-device-page";
				Constants.CREATE_DIS_DEVICE_LINK = baseUrl + "/dispatch-device-add-page";
				Constants.ROLE_GROUP = baseUrl + "/role-group";
				Constants.OTA_LINK = baseUrl + "/ota-batch-page";
				Constants.SIM_MANUAL_UPLOAD = baseUrl + "/sensorise-sim-manual-upload";
			}

			String pageTitle = getProperty("page_title");
			if (pageTitle != null && !pageTitle.isBlank()) {
				Constants.EXP_PAGE_TITLE_TEXT = pageTitle;
			} else {
				String displayProj = project.equals("lct") ? "LCT a4g - tarang" : project.substring(0, 1).toUpperCase() + project.substring(1);
				Constants.EXP_PAGE_TITLE_TEXT = "AEPL " + displayProj + " QA Diagnostic Cloud";
			}

			String imei = getProperty("imei");
			if (imei != null && !imei.isBlank()) {
				Constants.IMEI = imei;
			}

			String iccid = getProperty("iccid");
			if (iccid != null && !iccid.isBlank()) {
				Constants.ICCID = iccid;
			}

			String uin = getProperty("uin");
			if (uin != null && !uin.isBlank()) {
				Constants.UIN = uin;
			}

			String vin = getProperty("vin");
			if (vin != null && !vin.isBlank()) {
				Constants.VIN = vin;
			}

			logger.info("Dynamic constants propagated successfully for project: {}", project);
		} catch (Exception e) {
			logger.error("Failed to propagate dynamic constants: {}", e.getMessage(), e);
		}
	}

	public static synchronized String getProperty(String key) {
		if (properties == null) {
			logger.error("Attempted to access property before initialization.");
			throw new IllegalStateException("ConfigProperties is not initialized. Call initialize(env) first.");
		}

		// 1. Try Environment Overrides
		String envKey = ENV_OVERRIDES.get(key);
		if (envKey != null) {
			String envValue = DotEnvUtil.get(envKey);
			if (envValue != null && !envValue.isBlank()) {
				logger.debug("Resolved property '{}' from env key '{}'.", key, envKey);
				return envValue;
			}
		}

		// 2. Try Yaml Configuration
		String searchKey = key.toLowerCase();
		Object yamlValue = yamlConfig.get(searchKey);
		if (yamlValue == null) {
			yamlValue = yamlConfig.get(searchKey.replace('.', '_'));
		}
		if (yamlValue != null) {
			logger.debug("Resolved property '{}' from YAML configuration.", key);
			return String.valueOf(yamlValue);
		}

		// 3. Try Properties File
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

	public static String getProject() {
		return project;
	}

	public static void setProperty(String key, String value) {
		if (properties == null) {
			logger.error("Attempted to set property before initialization.");
			throw new IllegalStateException("ConfigProperties is not initialized. Call initialize(env) first.");
		}

		String envKey = ENV_OVERRIDES.get(key);
		if (envKey != null) {
			logger.info("Persisting credential property '{}' to .env key '{}'", key, envKey);
			DotEnvUtil.set(envKey, value, true);
			return;
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
