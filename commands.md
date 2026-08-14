# SAM_AUTO Test Execution & Command Reference

This document provides a comprehensive reference of all possible commands to build, execute, filter, and generate reports for the **Sampark Automation (SAM_AUTO)** test automation suite.

---

## 📋 Table of Contents
1. [Prerequisites](#-prerequisites)
2. [Maven Test Execution Commands](#-maven-test-execution-commands)
   - [Run Full Test Suites](#1-run-full-test-suites)
   - [Run Specific Test Classes & Methods](#2-run-specific-test-classes--methods)
   - [Filter Tests by Group / Marker](#3-filter-tests-by-group--marker)
   - [Filter Tests by Target Project](#4-filter-tests-by-target-project)
   - [Browser & Execution Overrides](#5-browser--execution-overrides)
   - [Build & Compilation Commands](#6-build--compilation-commands)
3. [Python Report Generator Commands](#-python-report-generator-commands)
   - [Generate Reports Only (Skip Test Execution)](#1-generate-reports-only-skip-test-execution)
   - [Run Tests and Auto-Generate Reports](#2-run-tests-and-auto-generate-reports)
4. [Command Cheat Sheet / Quick Reference](#-command-cheat-sheet--quick-reference)

---

## 🛠 Prerequisites

Ensure the following prerequisites are installed and configured on your system before running commands:
- **Java JDK 17+**: Configured in system `PATH` and `JAVA_HOME`.
- **Apache Maven 3.9+**: Configured in system `PATH`.
- **Python 3.10+**: (Optional, required for `generate_reports.py` HTML/Excel reporting).

> [!NOTE]
> All commands should be executed from the root directory of the project: `d:\AEPL_AUTOMATION\SAM_AUTO`.

---

## 🚀 Maven Test Execution Commands

### 1. Run Specific Test Suites

* **Run Default Test Suite (`testng.xml`)**  
  Executes all standard UI & system tests defined in [`testng.xml`](file:///d:/AEPL_AUTOMATION/SAM_AUTO/testng.xml).
  ```bash
  mvn test
  ```

* **Run Smoke Test Suite (`testNG-smoke.xml`)**  
  Executes critical path sanity tests (Login, Dashboard, Auth API).
  ```bash
  mvn test -DsuiteXmlFile=testNG-smoke.xml
  ```

* **Run Full Regression Suite (`testNG-regression.xml`)**  
  Executes the full suite of UI, API, and System Integration tests.
  ```bash
  mvn test -DsuiteXmlFile=testNG-regression.xml
  ```

* **Run REST Assured API Suite (`testNG-api.xml`)**  
  Executes REST API endpoint tests configured in [`testNG-api.xml`](file:///d:/AEPL_AUTOMATION/SAM_AUTO/testNG-api.xml).
  ```bash
  mvn test -DsuiteXmlFile=testNG-api.xml
  ```

* **Run Device & Firmware Management Suite (`testNG-device-management.xml`)**  
  Executes tests for Device Dashboard, Models, FOTA, OTA, and Production/Dispatched Devices.
  ```bash
  mvn test -DsuiteXmlFile=testNG-device-management.xml
  ```

* **Run User Administration Suite (`testNG-user-admin.xml`)**  
  Executes tests for Users, Roles, Groups, Dealers, Profiles, and Customer Master.
  ```bash
  mvn test -DsuiteXmlFile=testNG-user-admin.xml
  ```

* **Clean Build and Run Default Suite**  
  Deletes previous build outputs (`target/` directory) and executes the default test suite from scratch.
  ```bash
  mvn clean test
  ```

---

### 2. Run Specific Test Classes & Methods

* **Run a Single Test Class**  
  Executes all test methods within a specific test class (e.g., `DispatchedDevicesPageTest`).
  ```bash
  mvn test -Dtest=DispatchedDevicesPageTest
  ```

* **Run Multiple Specific Test Classes**  
  Executes a comma-separated list of test classes.
  ```bash
  mvn test -Dtest=LoginPageTest,DispatchedDevicesPageTest
  ```

* **Run a Single Specific Test Method**  
  Executes only one test method within a test class.
  ```bash
  mvn test -Dtest=DispatchedDevicesPageTest#validateRemarkInUploadedDispatchDeviceListOnValidFileUpload
  ```

* **Run Test Methods Matching a Pattern**  
  Executes test methods matching wildcard patterns.
  ```bash
  mvn test -Dtest=LoginPageTest#test*
  ```

---

### 3. Filter Tests by Group / Marker

TestNG `@Test(groups = {...})` annotations serve as markers in this project.

* **Run Tests in a Specific Group (e.g., `regression` or `smoke`)**  
  Executes only tests decorated with `@Test(groups = {"regression"})`.
  ```bash
  mvn test -Dgroups=regression
  ```

* **Run Tests Matching Multiple Groups (OR logic)**  
  Executes tests tagged with either `smoke` or `regression`.
  ```bash
  mvn test -Dgroups=smoke,regression
  ```

* **Exclude Specific Groups**  
  Executes all tests except those belonging to the `flaky` group.
  ```bash
  mvn test -Dexcludegroups=flaky
  ```

---

### 4. Filter Tests by Target Project

The framework includes a custom `ProjectFilterInterceptor` that filters tests based on the target project environment.

* **Run Tests for Target Project `sampark` (Default)**  
  ```bash
  mvn test -Dproject=sampark
  ```

* **Run Tests for Target Project `lct`, `trio`, `swaraj`, or `atcu`**  
  ```bash
  mvn test -Dproject=lct
  ```

---

### 5. Thread Count & Parallel Execution Overrides

By default, [`testNG.xml`](file:///d:/AEPL_AUTOMATION/SAM_AUTO/testng.xml#L20) runs tests in parallel across classes with `thread-count="4"`. You can customize the parallel execution mode and thread count using `-Dparallel` and `-DthreadCount` (or `--parallel` and `--threads` in Python).

#### **Available Parallel Execution Modes**

| Option Value | Description | Recommended Usage |
| :--- | :--- | :--- |
| **`classes`** *(Recommended)* | Runs **different test classes** in parallel on separate threads. | **Selenium UI Automation** (prevents session collisions). |
| **`methods`** | Runs **individual `@Test` methods** in parallel across classes. | Independent REST API tests or fast unit tests. |
| **`tests`** | Runs **different `<test>` blocks** inside `testng.xml` in parallel. | Multi-environment XML tag testing. |
| **`none`** | Disables parallel execution (runs tests sequentially 1-by-1). | Debugging test failures / single-threaded execution. |

* **Recommended UI Parallel Execution (4 Threads, Class-Level Parallel)**  
  ```bash
  mvn test -Dparallel=classes -DthreadCount=4
  python generate_reports.py --parallel classes --threads 4
  ```

* **High-Concurrency Parallel Execution (8 Threads)**  
  ```bash
  mvn test -Dparallel=classes -DthreadCount=8
  python generate_reports.py --parallel classes --threads 8
  ```

* **Method-Level Parallel Execution (API Tests)**  
  ```bash
  mvn test -DsuiteXmlFile=testNG-api.xml -Dparallel=methods -DthreadCount=4
  python generate_reports.py --suite testNG-api.xml --parallel methods --threads 4
  ```

* **Sequential Execution (Turn Off Parallel / 1 Thread)**  
  ```bash
  mvn test -Dparallel=none -DthreadCount=1
  python generate_reports.py --parallel none --threads 1
  ```

---

### 6. Browser & Execution Overrides

You can pass runtime properties to override configurations defined in `config/sampark.yaml` or `.env`.

* **Run Tests Headless (No UI Browser Window)**  
  ```bash
  mvn test -Dheadless=true
  ```

* **Run Tests on a Specific Browser (`chrome`, `firefox`, `edge`)**  
  ```bash
  mvn test -Dbrowser=firefox
  ```

* **Combine Multiple Override Options**  
  Run `DispatchedDevicesPageTest` headlessly on Chrome for project `sampark`:
  ```bash
  mvn test -Dtest=DispatchedDevicesPageTest -Dproject=sampark -Dheadless=true -Dbrowser=chrome
  ```

---

### 6. Build & Compilation Commands

* **Compile Source and Test Files (Without Executing Tests)**  
  Validates Java syntax, annotations, and null type safety without triggering test runs.
  ```bash
  mvn test-compile
  ```

* **Clean `target` Directory**  
  Removes generated class files, compiled binaries, and temporary surefire reports.
  ```bash
  mvn clean
  ```

---

## 📊 Python Report Generator Commands

The [`generate_reports.py`](file:///d:/AEPL_AUTOMATION/SAM_AUTO/generate_reports.py) script compiles TestNG test results, log outputs, and Excel data into visual HTML dashboard reports (`Reports/report.html`) and formatted Excel summaries (`Reports/report.xlsx`).

### 1. Generate Reports Only (Skip Test Execution)

* **Generate Reports from Existing Results (Fastest)**  
  Parses previously executed test results in `target/surefire-reports/testng-results.xml` and updates HTML & Excel reports without re-running any tests.
  ```bash
  python generate_reports.py --skip-tests
  ```

---

### 2. Run Suites and Auto-Generate Reports

* **Run Default Test Suite (`testNG.xml`)**  
  ```bash
  python generate_reports.py --suite testNG.xml --project sampark --threads 4
  ```

* **Run Smoke Test Suite (`testNG-smoke.xml`)**  
  ```bash
  python generate_reports.py --suite testNG-smoke.xml --project sampark --threads 2
  ```

* **Run Full Regression Suite (`testNG-regression.xml`)**  
  ```bash
  python generate_reports.py --suite testNG-regression.xml --project sampark --threads 4
  ```

* **Run Device Management Suite (`testNG-device-management.xml`)**  
  ```bash
  python generate_reports.py --suite testNG-device-management.xml --project sampark --threads 4
  ```

* **Run User Admin Suite (`testNG-user-admin.xml`)**  
  ```bash
  python generate_reports.py --suite testNG-user-admin.xml --project sampark --threads 3
  ```

* **Run REST API Suite (`testNG-api.xml`)**  
  ```bash
  python generate_reports.py --suite testNG-api.xml --project sampark
  ```

* **Run Any Suite Sequentially / Single-Threaded (`--threads 1`)**  
  ```bash
  python generate_reports.py --suite testNG-regression.xml --project sampark --threads 1 --parallel none
  ```

* **Run a Specific Test Class + Generate Reports**  
  ```bash
  python generate_reports.py --test DispatchedDevicesPageTest --project sampark
  ```

* **Run Tests by Marker/Group + Generate Reports**  
  ```bash
  python generate_reports.py --markers regression --project sampark
  ```

---

## 💡 Command Cheat Sheet / Quick Reference

| Goal / Requirement | Recommended Command |
| :--- | :--- |
| **Run Default Suite (Maven)** | `mvn test` |
| **Run Smoke Suite (Maven)** | `mvn test -DsuiteXmlFile=testNG-smoke.xml` |
| **Run Regression Suite (Maven)** | `mvn test -DsuiteXmlFile=testNG-regression.xml` |
| **Run Device Management Suite (Maven)** | `mvn test -DsuiteXmlFile=testNG-device-management.xml` |
| **Run User Admin Suite (Maven)** | `mvn test -DsuiteXmlFile=testNG-user-admin.xml` |
| **Run API Suite (Maven)** | `mvn test -DsuiteXmlFile=testNG-api.xml` |
| **Run Single Class (Maven)** | `mvn test -Dtest=DispatchedDevicesPageTest` |
| **Run Single Method (Maven)** | `mvn test -Dtest=LoginPageTest#testEmptyUsernameWithValidPassword` |
| **Run Regression Group (Maven)** | `mvn test -Dgroups=regression` |
| **Run Headless (Maven)** | `mvn test -Dheadless=true` |
| **Run Parallel Classes (Recommended)** | `mvn test -Dparallel=classes -DthreadCount=4` |
| **Run Sequentially / Single-Threaded** | `mvn test -Dparallel=none -DthreadCount=1` |
| **Compile Project (Maven)** | `mvn test-compile` |
| **Generate Report Only (Python)** | `python generate_reports.py --skip-tests` |
| **Run Smoke Suite + Report (Python)** | `python generate_reports.py --suite testNG-smoke.xml --project sampark` |
| **Run Regression Suite + Report (Python)** | `python generate_reports.py --suite testNG-regression.xml --project sampark` |
| **Run Parallel + Report (Python)** | `python generate_reports.py --parallel classes --threads 4` |
| **Run Sequentially + Report (Python)** | `python generate_reports.py --parallel none --threads 1` |
| **Run Device Suite + Report (Python)** | `python generate_reports.py --suite testNG-device-management.xml --project sampark` |
| **Run User Suite + Report (Python)** | `python generate_reports.py --suite testNG-user-admin.xml --project sampark` |
| **Run Single Class + Report (Python)** | `python generate_reports.py --test DispatchedDevicesPageTest` |

---
