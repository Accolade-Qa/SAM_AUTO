# SAM Automation Suite

SAM Automation Suite is a Java-based UI automation framework for Sampark workflows. It combines Selenium WebDriver, TestNG, Maven, and modern reporting utilities to validate end-to-end business flows for device, user, and management modules.

## Overview

This project is designed for fast, maintainable regression and functional automation. The suite includes:

- End-to-end browser automation for Sampark web modules
- Page Object Model (POM) driven test design
- Parallel test execution support with TestNG and thread coordination
- Environment-based configuration for credentials and runtime settings
- Rich HTML reporting and structured logging
- Optional report generation helpers for consolidated test summaries

## Tech stack

- Java 17
- Maven 3.9+
- Selenium WebDriver 4.27+
- TestNG 7.9+
- WebDriverManager 5.9+
- ExtentReports 5.0+
- Log4j2 2.24+
- Apache POI 5.2+
- REST Assured 5.5+

## Project structure

- src/main/java/com/aepl/sam/base – base test setup, browser lifecycle, and shared test hooks
- src/main/java/com/aepl/sam/pages – page classes and UI workflow logic
- src/main/java/com/aepl/sam/locators – element locators and page-specific selectors
- src/main/java/com/aepl/sam/utils – configuration, drivers, reporting, helpers, and runtime utilities
- src/test/java/com/aepl/sam/tests – TestNG test classes for each module
- src/main/resources – runtime properties and logging configuration
- testNG.xml – main suite configuration for the full automated run
- generate_reports.py – helper script for generating report artifacts from test execution data

## Prerequisites

Before running the suite, make sure you have:

- JDK 17 or newer
- Apache Maven installed and available on PATH
- Google Chrome, Microsoft Edge, or Firefox installed
- A valid test environment URL and credentials

## Getting started

1. Clone the repository.
2. Create a local environment file from .env.example:

```bash
copy .env.example .env
```

3. Fill in the required values in .env for your environment.
4. Review non-sensitive settings in src/main/resources/qa.config.properties.
5. Run the suite from the project root:

```bash
mvn clean test
```

## Configuration

The framework separates secrets from non-secret settings:

- .env – local-only file for secrets and sensitive credentials
- src/main/resources/qa.config.properties – non-sensitive configuration such as browser preference

Example environment variables:

```env
SAM_USERNAME=your_username
SAM_PASSWORD=your_password
SAM_QA_MANAGER_USERNAME=your_qa_manager_username
SAM_QA_MANAGER_PASSWORD=your_qa_manager_password
SAM_SOFT_MANAGER_USERNAME=your_software_manager_username
SAM_SOFT_MANAGER_PASSWORD=your_software_manager_password
SAM_CURRENT_PASSWORD=your_current_password
SAM_NEW_PASSWORD=your_new_password
```

Optional runtime overrides:

- PROJECT=your_project_name – used for project-specific configuration lookup
- ENVIRONMENT=QA – included in report metadata

## Running tests

Run the full suite:

```bash
mvn test
```

Run a single test class:

```bash
mvn -Dtest=LoginPageTest test
```

Run a specific test method:

```bash
mvn -Dtest=LoginPageTest#loginTest test
```

## Reports and logs

The framework produces execution artifacts in the following locations:

- HTML report: Results/test-results/ExtentReport.html
- Log file: Results/logs/test-automation.log
- Screenshots and additional temporary artifacts: Results/
- Consolidated report helper output: Reports/

## Notes

- The suite uses TestNG parallel execution for selected classes and a queue-based mechanism to manage browser resource contention.
- Keep secrets in .env and do not commit local credentials to source control.
- This repository is intended for internal QA automation use and should be shared only with authorized team members.
