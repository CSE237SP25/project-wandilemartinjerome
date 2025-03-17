package bankingapp;

import java.io.ByteArrayOutputStream;
import java.util.Scanner;

public class Menu {
	private BankAccount tempAccount;
	
	public Menu() {
		tempAccount = new BankAccount();
	}
	
	public void displayOptions() {
		System.out.println("Select one of the following:");
		System.out.println("1. [V]iew Current Balance");
	}
	
	public String getUserInput() {
		Scanner keyboardInput = new Scanner(System.in);
		String userInput = keyboardInput.nextLine();
		return userInput;
	}
	
	public void processUserInput(String userInput) {
		if(userInput.toLowerCase() == "v") {
			System.out.println("Current balance is " + tempAccount.getCurrentBalance());
		}
	}
}
