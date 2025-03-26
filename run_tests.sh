#!/bin/bash
echo "Compiling Banking Application and Tests..."

# Create bin directory if it doesn't exist
mkdir -p bin

# Check if JUnit Platform Console Launcher exists
if [ ! -f "lib/junit-platform-console-standalone-1.9.1.jar" ]; then
    echo "Downloading JUnit Platform Console Launcher..."
    if command -v wget &> /dev/null; then
        wget -O lib/junit-platform-console-standalone-1.9.1.jar https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.9.1/junit-platform-console-standalone-1.9.1.jar
    elif command -v curl &> /dev/null; then
        curl -L https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.9.1/junit-platform-console-standalone-1.9.1.jar -o lib/junit-platform-console-standalone-1.9.1.jar
    else
        echo "Error: Neither wget nor curl is available. Please install one of them or download the JUnit Platform Console Launcher manually."
        exit 1
    fi
fi

# Compile all Java files including tests
javac -d bin -cp lib/junit-jupiter-api-5.9.1.jar src/bankingapp/*.java src/tests/*.java

# Run the tests
echo "Running Tests..."

# Use JUnit 5 for the tests
echo "Running tests with JUnit 5..."
java -jar lib/junit-platform-console-standalone-1.9.1.jar --class-path bin --scan-class-path

echo "Done!"
