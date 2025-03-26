# JUnit Testing Setup

## JUnit Libraries
This repository includes the following JUnit libraries (downloaded automatically when needed):
- junit-4.13.2.jar (JUnit 4 for assertions)
- junit-jupiter-api-5.9.1.jar (JUnit 5 API)
- hamcrest-core-1.3.jar (For JUnit 4 assertions)
- junit-platform-console-standalone-1.9.1.jar (JUnit 5 Console Launcher)

These libraries are used for running the tests in the `src/tests` directory.

## Running Tests
You can run the tests using the provided scripts:
- Windows: `run_tests.bat`
- Unix/Linux/Mac: `run_tests.sh`

These scripts will:
1. Download any missing JUnit libraries automatically
2. Compile both the main application and the test files
3. Run all the tests using JUnit 5 Platform

## Running Tests in Eclipse
You can also run the tests in Eclipse:

1. Open the project in Eclipse
2. Right-click on any test file in the `src/tests` directory
3. Select "Run As > JUnit Test"

Eclipse will automatically handle the JUnit setup for you.
