@echo off
echo Compiling Banking Application and Tests...

:: Create bin directory if it doesn't exist
if not exist bin mkdir bin

:: Check if JUnit Platform Console Launcher exists
if not exist lib\junit-platform-console-standalone-1.9.1.jar (
    echo Downloading JUnit Platform Console Launcher...
    powershell -Command "(New-Object System.Net.WebClient).DownloadFile('https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.9.1/junit-platform-console-standalone-1.9.1.jar', 'lib\junit-platform-console-standalone-1.9.1.jar')"
)

:: Compile all Java files including tests
javac -d bin -cp lib\junit-jupiter-api-5.9.1.jar src/bankingapp/*.java src/tests/*.java

:: Run the tests
echo Running Tests...

:: Use JUnit 5 for the tests
echo Running tests with JUnit 5...
java -jar lib\junit-platform-console-standalone-1.9.1.jar --class-path bin --scan-class-path

echo Done!
