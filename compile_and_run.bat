@echo off
echo Compiling Banking Application...

:: Create bin directory if it doesn't exist
if not exist bin mkdir bin

:: Compile only the main application files (not tests)
javac -d bin src/bankingapp/*.java

:: Run the application
echo Running Banking Application...
java -cp bin bankingapp.Menu

echo Done!
