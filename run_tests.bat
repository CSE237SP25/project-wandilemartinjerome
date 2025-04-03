@echo off
echo Compiling Banking Application and Tests...

:: Create bin directory if it doesn't exist
if not exist bin mkdir bin

:: Check if JUnit Platform Console Launcher exists
if not exist lib\junit-platform-console-standalone-1.9.1.jar (
    echo Downloading JUnit Platform Console Launcher...
    powershell -Command "(New-Object System.Net.WebClient).DownloadFile('https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.9.1/junit-platform-console-standalone-1.9.1.jar', 'lib\junit-platform-console-standalone-1.9.1.jar')"
)

:: Check if JUnit 4 exists
if not exist lib\junit-4.13.2.jar (
    echo Downloading JUnit 4...
    powershell -Command "(New-Object System.Net.WebClient).DownloadFile('https://repo1.maven.org/maven2/junit/junit/4.13.2/junit-4.13.2.jar', 'lib\junit-4.13.2.jar')"
)

:: Check if Hamcrest exists
if not exist lib\hamcrest-core-1.3.jar (
    echo Downloading Hamcrest...
    powershell -Command "(New-Object System.Net.WebClient).DownloadFile('https://repo1.maven.org/maven2/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar', 'lib\hamcrest-core-1.3.jar')"
)

:: Compile all Java files including tests
javac -d bin -cp lib\junit-4.13.2.jar;lib\junit-jupiter-api-5.9.1.jar;lib\hamcrest-core-1.3.jar src/bankingapp/*.java src/tests/*.java

:: Run the tests
echo Running Tests...

:: Use JUnit 5 for the tests
echo Running tests with JUnit 5...
java -jar lib\junit-platform-console-standalone-1.9.1.jar --class-path bin --scan-classpath

echo Done!
