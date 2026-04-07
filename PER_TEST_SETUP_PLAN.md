# Per-Test Setup & Teardown Action Plan

## Objective
Ensure each test class opens and tears down its own browser/login session, while keeping the base setup thread-safe so multiple classes can run in parallel without reusing the same driver.

## Current snapshot
- `src/main/java/com/aepl/sam/base/TestBase.java:22-123` initializes the driver in `@BeforeClass`, reuses one shared session that only closes at `@AfterSuite`, so running classes in parallel would conflict on the static driver.
- `WebDriverFactory` (`src/main/java/com/aepl/sam/utils/WebDriverFactory.java:14-90`) already provides thread-local drivers and exposes `quitDriver()`.
- Many tests (including `CustomerMasterPageTest` and `DeviceModelsPageTest`) expect to launch from an authenticated dashboard, so per-class login/teardown matches the current flow but must be handled in a thread-safe way.

## Steps
1. **Make `TestBase` thread-safe for per-class sessions.**
   - Switch the shared driver/wait from `static` to instance-level so each test class owns its own references while the `WebDriverFactory` still keeps browser instances thread-local.
   - During `@BeforeClass`, always spin up a new browser via `WebDriverFactory`, create a fresh `WebDriverWait`, navigate to `Constants.BASE_URL`, and auto-login for non-login suites.
   - Add a base `@AfterClass` (called after derived classes’ cleanup) that logs out, calls `WebDriverFactory.quitDriver()`, and nulls the local fields so the next class can restart cleanly.

2. **Adjust derived tests for the new flow.**
   - Continue calling `super.setUp()` inside each `@BeforeClass`, instantiate page helpers once, and rely on the shared driver for all `@Test` methods in the class.
   - Keep derived `@AfterClass` focused on assertions (e.g., `softAssert.assertAll()`); the base cleanup runs afterward.
   - Only the login-focused suites should bypass the automatic login so they can validate the unauthenticated UI explicitly.

3. **Update listeners/reports to handle per-class drivers.**
   - Have `TestListener` request the current driver/wait from `WebDriverFactory` whenever it needs to capture a screenshot instead of relying on a static `driver` field.
   - Keep `ExtentManager`/`ExtentTestManager` usage, but log and skip screenshot capture when the thread-local driver already closed.

4. **Enable and validate parallel execution for the target classes.**
   - Update `testNG.xml` to use `parallel="classes"` with `thread-count="2"` so suites run CustomerMaster and DeviceModels tests concurrently.
   - Run `mvn -Dtest=CustomerMasterPageTest,DeviceModelsPageTest test` (or `mvn test` to run the full suite) to confirm both classes log in/out with their own browsers.
   - Check `test-results/ExtentReport.html` and the new `screenshots` entries (if any) to ensure each failure still captures a screenshot, and make sure Excel writes remain atomic.

## Risks & Notes
- Class-level cleanup still opens one browser per class, so the total runtime is higher than sharing a session but far faster than per-method.
- Keep `.env` secrets refreshed so every class login uses valid credentials before reinitializing the driver.
