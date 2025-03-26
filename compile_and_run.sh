#!/bin/bash
echo "Compiling Banking Application..."

# Create bin directory if it doesn't exist
mkdir -p bin

# Compile all Java files
javac -d bin src/bankingapp/*.java

# Run the application
echo "Running Banking Application..."
java -cp bin bankingapp.Menu

echo "Done!"
