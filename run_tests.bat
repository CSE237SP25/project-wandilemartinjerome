@echo off
echo Compiling Banking Application and Tests...

:: Create bin directory if it doesn't exist
if not exist bin mkdir bin

:: Compile all Java files including tests
javac -d bin -cp lib\junit-4.13.2.jar;lib\junit-jupiter-api-5.9.1.jar;lib\hamcrest-core-1.3.jar src/bankingapp/*.java src/tests/*.java

:: Run the tests
echo Running Tests...
java -cp bin;lib\junit-4.13.2.jar;lib\junit-jupiter-api-5.9.1.jar;lib\hamcrest-core-1.3.jar org.junit.runner.JUnitCore tests.BankAccountTests tests.MenuTests tests.AdminAccountTests

echo Done!
