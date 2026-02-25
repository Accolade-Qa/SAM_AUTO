package com.aepl.sam.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class DotEnvUtil {

	private static final Logger logger = LogManager.getLogger(DotEnvUtil.class);
	private static final String ENV_FILE = ".env";
	private static final Map<String, String> ENV_MAP = new HashMap<>();
	private static boolean loaded = false;

	private DotEnvUtil() {
	}

	public static synchronized void load() {
		if (loaded) {
			return;
		}

		Path envPath = resolveEnvPath();
		if (!Files.exists(envPath)) {
			logger.warn(".env file not found at {}", envPath.toAbsolutePath());
			loaded = true;
			return;
		}

		try {
			List<String> lines = Files.readAllLines(envPath);
			for (String rawLine : lines) {
				String line = rawLine.trim();
				if (line.isEmpty() || line.startsWith("#") || !line.contains("=")) {
					continue;
				}
				String[] parts = line.split("=", 2);
				String key = parts[0].trim();
				String value = parts.length > 1 ? parts[1].trim() : "";
				if ((value.startsWith("\"") && value.endsWith("\""))
						|| (value.startsWith("'") && value.endsWith("'"))) {
					value = value.substring(1, value.length() - 1);
				}
				ENV_MAP.put(key, value);
			}
			logger.info(".env file loaded successfully.");
		} catch (IOException e) {
			logger.error("Failed to read .env file.", e);
		} finally {
			loaded = true;
		}
	}

	public static String get(String key) {
		load();
		String fromDotEnv = ENV_MAP.get(key);
		if (fromDotEnv != null && !fromDotEnv.isBlank()) {
			return fromDotEnv;
		}
		return System.getenv(key);
	}

	public static synchronized void set(String key, String value, boolean persistToFile) {
		load();
		String safeValue = value == null ? "" : value;
		ENV_MAP.put(key, safeValue);

		if (!persistToFile) {
			return;
		}

		Path envPath = resolveEnvPath();
		List<String> lines = new ArrayList<>();

		try {
			if (Files.exists(envPath)) {
				lines.addAll(Files.readAllLines(envPath));
			}

			boolean updated = false;
			for (int i = 0; i < lines.size(); i++) {
				String rawLine = lines.get(i);
				String line = rawLine.trim();
				if (line.isEmpty() || line.startsWith("#") || !line.contains("=")) {
					continue;
				}

				String existingKey = line.split("=", 2)[0].trim();
				if (existingKey.equals(key)) {
					lines.set(i, key + "=" + safeValue);
					updated = true;
					break;
				}
			}

			if (!updated) {
				lines.add(key + "=" + safeValue);
			}

			Files.write(envPath, lines);
			logger.info("Updated .env key '{}'", key);
		} catch (IOException e) {
			logger.error("Failed to update .env key '{}'", key, e);
			throw new RuntimeException("Failed to update .env file", e);
		}
	}

	private static Path resolveEnvPath() {
		return Paths.get(System.getProperty("user.dir"), ENV_FILE);
	}
}
