# SAM_AUTO

SAM_AUTO is an automation framework for Sampark AIS-140 and Lite workflows.  
It covers UI and API validation using Java, Selenium, TestNG, and Maven.

## Core capabilities

- End-to-end UI automation with Selenium WebDriver
- API validation with REST Assured
- Page Object Model (POM) based test design
- ExtentReports based execution reporting
- Excel-driven test data support with Apache POI
- Log4j2 logging for debugging and traceability

## Tech stack

- Java 17
- Selenium WebDriver
- TestNG
- Maven
- REST Assured
- ExtentReports
- Apache POI
- Log4j2

## Project structure

- `src/main/java/com/aepl/sam/base`: Base setup and teardown classes
- `src/main/java/com/aepl/sam/pages`: Page classes and workflows
- `src/main/java/com/aepl/sam/locators`: Element locators
- `src/main/java/com/aepl/sam/utils`: Utilities (config, webdriver, reporting, helpers)
- `src/test/java/com/aepl/sam/tests`: TestNG test suites
- `src/main/resources`: Environment property files and logging config

## Prerequisites

- Java JDK 17 or newer
- Apache Maven
- Chrome, Firefox, or Edge

## Setup

1. Clone the repository.
2. Open a terminal in the project root.
3. Install dependencies:

```bash
mvn clean install
```

## Configuration

The framework now separates secrets from non-secret config:

- `src/main/resources/qa.config.properties`
  - Keep only non-sensitive settings, for example:
  - `browser=chrome`
- `.env` (local only, not committed)
  - Keep credentials and other secrets

Required `.env` keys:

```env
SAM_USERNAME=your_login_username
SAM_PASSWORD=your_login_password
SAM_QA_MANAGER_USERNAME=your_qa_manager_username
SAM_QA_MANAGER_PASSWORD=your_qa_manager_password
SAM_SOFT_MANAGER_USERNAME=your_software_manager_username
SAM_SOFT_MANAGER_PASSWORD=your_software_manager_password
SAM_CURRENT_PASSWORD=your_current_profile_password
SAM_NEW_PASSWORD=your_new_profile_password
```

## Running tests

Run the full suite from `testNG.xml`:

```bash
mvn test
```

## Reports and logs

- Extent report path: `test-results/ExtentReport.html`
- Logs are configured through `src/main/resources/log4j2.xml`

## Internal use

This repository is intended for internal use. Share access only with authorized project members.
