package com.aepl.sam.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

/**
 * ConfigProperties - Single Source of Truth Configuration Manager.
 * 
 * Sources of Truth:
 * 1. Credentials & Secrets -> Fetched strictly from .env (via DotEnvUtil)
 * 2. Application & Project Config -> Fetched strictly from YAML (config/<project>.yaml)
 */
public class ConfigProperties {

	private static final Logger logger = LogManager.getLogger(ConfigProperties.class);

	private static String environment;
	private static String project;
	private static boolean initialized = false;
	private static final Map<String, String> CREDENTIAL_MAPPINGS = new HashMap<>();
	private static final Map<String, Object> yamlConfig = new HashMap<>();

	static {
		// Mapping logical credential property keys to .env variable names
		CREDENTIAL_MAPPINGS.put("username", "SAM_USERNAME");
		CREDENTIAL_MAPPINGS.put("password", "SAM_PASSWORD");
		CREDENTIAL_MAPPINGS.put("valid.username", "SAM_USERNAME");
		CREDENTIAL_MAPPINGS.put("valid.password", "SAM_PASSWORD");
		CREDENTIAL_MAPPINGS.put("qa_man", "SAM_QA_MANAGER_USERNAME");
		CREDENTIAL_MAPPINGS.put("qa_pass", "SAM_QA_MANAGER_PASSWORD");
		CREDENTIAL_MAPPINGS.put("soft_man", "SAM_SOFT_MANAGER_USERNAME");
		CREDENTIAL_MAPPINGS.put("soft_pass", "SAM_SOFT_MANAGER_PASSWORD");
		CREDENTIAL_MAPPINGS.put("current.password", "SAM_CURRENT_PASSWORD");
		CREDENTIAL_MAPPINGS.put("new.password", "SAM_NEW_PASSWORD");
	}

	private ConfigProperties() {
	}

	public static synchronized void initialize(String env) {
		if (initialized) {
			return;
		}

		initialized = true;

		if (env == null || env.isEmpty()) {
			env = "qa";
		}

		environment = env.toLowerCase();
		project = System.getProperty("project", System.getenv("PROJECT"));
		if (project == null || project.isEmpty()) {
			project = "sampark";
		}
		project = project.toLowerCase();

		logger.info("Initializing ConfigProperties - Credentials [.env] | Project Config [YAML: {}.yaml]", project);

		// Step 1: Load Project YAML Configuration
		loadYamlConfig(project);

		// Step 2: Propagate dynamic values to Constants class
		propagateConstants();
	}

	private static void loadYamlConfig(String projectName) {
		yamlConfig.clear();
		String yamlFileName = projectName + ".yaml";

		File[] searchPaths = {
			new File("config/" + yamlFileName),
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
			logger.warn("YAML file not found on disk: {}. Loading from classpath resources...", yamlFileName);
			try (InputStream is = ConfigProperties.class.getClassLoader().getResourceAsStream(yamlFileName)) {
				if (is != null) {
					Yaml yaml = new Yaml();
					Map<String, Object> data = yaml.load(is);
					if (data != null) {
						yamlConfig.putAll(data);
					}
					logger.info("Successfully loaded YAML config from classpath resource.");
				} else {
					logger.error("Project YAML configuration file {} not found.", yamlFileName);
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
				Constants.EXP_PAGE_TITLE_TEXT = "AEPL Sampark QA Diagnostic Cloud";
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
		if (!initialized) {
			initialize("qa");
		}

		if (key == null || key.isBlank()) {
			return null;
		}

		// 1. Check if key is mapped as a Secret / Credential -> fetch strictly from .env
		String envKey = CREDENTIAL_MAPPINGS.get(key);
		if (envKey == null && key.startsWith("SAM_")) {
			envKey = key;
		}
		if (envKey != null) {
			String envValue = DotEnvUtil.get(envKey);
			if (envValue != null && !envValue.isBlank()) {
				logger.debug("Resolved credential key '{}' from .env key '{}'.", key, envKey);
				return envValue;
			}
		}

		// 2. Fetch non-secret configuration strictly from YAML
		String searchKey = key.toLowerCase();
		Object yamlValue = yamlConfig.get(searchKey);
		if (yamlValue == null) {
			yamlValue = yamlConfig.get(searchKey.replace('.', '_'));
		}

		if (yamlValue != null) {
			logger.debug("Resolved configuration key '{}' from YAML.", key);
			return String.valueOf(yamlValue);
		}

		// Fallback check directly in .env if not found in YAML
		String directEnvVal = DotEnvUtil.get(key);
		if (directEnvVal != null && !directEnvVal.isBlank()) {
			return directEnvVal;
		}

		logger.warn("Property '{}' not found in .env or YAML configuration.", key);
		return null;
	}

	public static synchronized void reloadProperties() {
		logger.info("Reloading configuration for project: {}", project);
		initialize(environment);
	}

	public static String getEnvironment() {
		return environment;
	}

	public static String getProject() {
		return project;
	}

	public static void setProperty(String key, String value) {
		if (key == null || key.isBlank()) {
			return;
		}
		String envKey = CREDENTIAL_MAPPINGS.get(key);
		if (envKey != null || key.startsWith("SAM_")) {
			String targetEnvKey = (envKey != null) ? envKey : key;
			logger.info("Persisting credential property '{}' to .env key '{}'", key, targetEnvKey);
			DotEnvUtil.set(targetEnvKey, value, true);
			return;
		}

		logger.info("Setting runtime YAML config property '{}' = '{}'", key, value);
		yamlConfig.put(key.toLowerCase(), value);
	}
}
