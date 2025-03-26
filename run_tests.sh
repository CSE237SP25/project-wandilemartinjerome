#!/bin/bash
echo "Compiling Banking Application and Tests..."

# Create bin directory if it doesn't exist
mkdir -p bin

# Compile all Java files including tests
javac -d bin src/bankingapp/*.java src/tests/*.java

# Run the tests
echo "Running Tests..."
echo "Note: This script is designed to work with Eclipse's JUnit configuration."
echo "If running outside of Eclipse, please ensure JUnit libraries are in your classpath."
echo "To run tests in Eclipse, right-click on the test files and select 'Run As > JUnit Test'"

echo "Done!"
