package experiment6;

import java.util.Scanner;

public class BankManagementSystem {
	static class BankAccount {
		private double balance;
		public BankAccount(double initialBalance) {
			this.balance = initialBalance;
		}
		public synchronized void deposit(String user, double amount) {
			balance += amount;
			System.out.println(user + " deposited " + amount + ". Balance: " + balance);
		}
		public synchronized void withdraw(String user, double amount) {
			if (amount <= balance) {
				balance -= amount;
				System.out.println(user + " withdrew " + amount + ". Balance: " + balance);
			} else {
				System.out.println(user + " failed to withdraw " + amount + " (Insufficient balance). Balance: " + balance);
			}
		}
		public synchronized double getBalance() {
			return balance;
		}
	}
	static class UserTransaction extends Thread {
		private final BankAccount account;
		private final String user;
		private final double[] deposits;
		private final double[] withdrawals;
		public UserTransaction(BankAccount account, String user, double[] deposits, double[] withdrawals) {
			this.account = account;
			this.user = user;
			this.deposits = deposits;
			this.withdrawals = withdrawals;
		}
		@Override
		public void run() {
			for (double amount : deposits) {
				account.deposit(user, amount);
			}
			for (double amount : withdrawals) {
				account.withdraw(user, amount);
			}
		}
	}

	private static int readPositiveInt(Scanner scanner, String prompt) {
		int value;
		while (true) {
			System.out.print(prompt);
			if (scanner.hasNextInt()) {
				value = scanner.nextInt();
				scanner.nextLine();
				if (value > 0) {
					return value;
				}
				System.out.println("Please enter a value greater than 0.");
			} else {
				System.out.println("Invalid input. Please enter an integer.");
				scanner.nextLine();
			}
		}
	}

	private static int readNonNegativeInt(Scanner scanner, String prompt) {
		int value;
		while (true) {
			System.out.print(prompt);
			if (scanner.hasNextInt()) {
				value = scanner.nextInt();
				scanner.nextLine();
				if (value >= 0) {
					return value;
				}
				System.out.println("Please enter a value greater than or equal to 0.");
			} else {
				System.out.println("Invalid input. Please enter an integer.");
				scanner.nextLine();
			}
		}
	}

	private static double readNonNegativeDouble(Scanner scanner, String prompt) {
		double value;
		while (true) {
			System.out.print(prompt);
			if (scanner.hasNextDouble()) {
				value = scanner.nextDouble();
				scanner.nextLine();
				if (value >= 0) {
					return value;
				}
				System.out.println("Please enter a value greater than or equal to 0.");
			} else {
				System.out.println("Invalid input. Please enter a numeric value.");
				scanner.nextLine();
			}
		}
	}

	private static double[] readTransactionArray(Scanner scanner, String userName, String type) {
		int count = readNonNegativeInt(scanner, "Enter number of " + type + "s for " + userName + ": ");
		double[] transactions = new double[count];
		for (int i = 0; i < count; i++) {
			transactions[i] = readNonNegativeDouble(scanner,
					"Enter " + type + " amount " + (i + 1) + " for " + userName + ": ");
		}
		return transactions;
	}

	private static boolean askToRunAgain(Scanner scanner) {
		while (true) {
			System.out.print("Do you want to run another simulation? (y/n): ");
			String choice = scanner.nextLine().trim().toLowerCase();
			if (choice.equals("y") || choice.equals("yes")) {
				return true;
			}
			if (choice.equals("n") || choice.equals("no")) {
				return false;
			}
			System.out.println("Invalid input. Please enter y or n.");
		}
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		boolean runAgain;
		do {
			double initialBalance = readNonNegativeDouble(scanner, "Enter initial account balance: ");
			int userCount = readPositiveInt(scanner, "Enter number of users: ");

			BankAccount sharedAccount = new BankAccount(initialBalance);
			UserTransaction[] users = new UserTransaction[userCount];

			for (int i = 0; i < userCount; i++) {
				System.out.print("Enter name for user " + (i + 1) + ": ");
				String userName = scanner.nextLine().trim();
				if (userName.isEmpty()) {
					userName = "User-" + (i + 1);
				}

				double[] deposits = readTransactionArray(scanner, userName, "deposit");
				double[] withdrawals = readTransactionArray(scanner, userName, "withdrawal");

				users[i] = new UserTransaction(sharedAccount, userName, deposits, withdrawals);
			}

			for (UserTransaction user : users) {
				user.start();
			}

			try {
				for (UserTransaction user : users) {
					user.join();
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				System.out.println("Main thread interrupted while waiting for transactions.");
			}

			System.out.println("Final Balance after all transactions: " + sharedAccount.getBalance());
			runAgain = askToRunAgain(scanner);
		} while (runAgain);

		scanner.close();
		System.out.println("Program ended.");
	}
}