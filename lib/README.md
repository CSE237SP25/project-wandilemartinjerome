# JUnit Testing Setup

## Running Tests in Eclipse (Recommended)
This project is configured to use Eclipse's built-in JUnit container. To run tests in Eclipse:

1. Open the project in Eclipse
2. Right-click on any test file in the `src/tests` directory
3. Select "Run As > JUnit Test"

Eclipse will automatically handle all JUnit dependencies for you.

## Running Tests from Command Line
If you want to run tests from the command line outside of Eclipse, you'll need to:

1. Download the following JUnit libraries and place them in this directory:
   - junit-4.13.2.jar
   - junit-jupiter-api-5.9.1.jar
   - hamcrest-core-1.3.jar

2. You can download these files from:
   - JUnit 4: https://search.maven.org/artifact/junit/junit/4.13.2/jar
   - JUnit 5: https://search.maven.org/artifact/org.junit.jupiter/junit-jupiter-api/5.9.1/jar
   - Hamcrest: https://search.maven.org/artifact/org.hamcrest/hamcrest-core/1.3/jar

3. Then modify the `run_tests.bat` or `run_tests.sh` script to uncomment the line that runs the tests with these libraries.
