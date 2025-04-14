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

# Check if JUnit 4 exists
if [ ! -f "lib/junit-4.13.2.jar" ]; then
    echo "Downloading JUnit 4..."
    if command -v wget &> /dev/null; then
        wget -O lib/junit-4.13.2.jar https://repo1.maven.org/maven2/junit/junit/4.13.2/junit-4.13.2.jar
    elif command -v curl &> /dev/null; then
        curl -L https://repo1.maven.org/maven2/junit/junit/4.13.2/junit-4.13.2.jar -o lib/junit-4.13.2.jar
    else
        echo "Error: Neither wget nor curl is available. Please install one of them or download JUnit 4 manually."
        exit 1
    fi
fi

# Check if Hamcrest exists
if [ ! -f "lib/hamcrest-core-1.3.jar" ]; then
    echo "Downloading Hamcrest..."
    if command -v wget &> /dev/null; then
        wget -O lib/hamcrest-core-1.3.jar https://repo1.maven.org/maven2/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar
    elif command -v curl &> /dev/null; then
        curl -L https://repo1.maven.org/maven2/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar -o lib/hamcrest-core-1.3.jar
    else
        echo "Error: Neither wget nor curl is available. Please install one of them or download Hamcrest manually."
        exit 1
    fi
fi

# Compile all Java files including tests
javac -d bin -cp lib/junit-4.13.2.jar:lib/junit-jupiter-api-5.9.1.jar:lib/hamcrest-core-1.3.jar src/bankingapp/*.java src/tests/*.java

# Run the tests
echo "Running Tests..."

# Use JUnit 5 for the tests
echo "Running tests with JUnit 5..."
java -jar lib/junit-platform-console-standalone-1.9.1.jar --class-path bin --scan-classpath

echo "Done!"
