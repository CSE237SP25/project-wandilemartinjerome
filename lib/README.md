# JUnit Testing Setup

## JUnit Libraries
This repository includes the following JUnit libraries:
- junit-jupiter-api-5.9.1.jar (JUnit 5 API)
- junit-platform-console-standalone-1.9.1.jar (JUnit 5 Console Launcher - downloaded automatically when needed)

These libraries are used for running the tests in the `src/tests` directory.

## Running Tests
You can run the tests using the provided scripts:
- Windows: `run_tests.bat`
- Unix/Linux/Mac: `run_tests.sh`

These scripts will:
1. Download the JUnit Platform Console Launcher if it's not already present
2. Compile both the main application and the test files
3. Run all the tests using JUnit 5

## Running Tests in Eclipse
You can also run the tests in Eclipse:

1. Open the project in Eclipse
2. Right-click on any test file in the `src/tests` directory
3. Select "Run As > JUnit Test"

Eclipse will automatically handle the JUnit 5 setup for you.
