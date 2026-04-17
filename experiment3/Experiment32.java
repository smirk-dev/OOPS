// 4.1.  Write a Java program to check whether a number is strong or not. If the sum of factorial of the digits of the number is the number itself than it is a strong number.
package experiment3;
import java.util.Scanner;
public class Experiment32 {
	public static int factorial(int n) {
		int fact = 1;
		for (int i = 2; i <= n; i++) {
			fact = fact * i;
		}
		return fact;
	}
	public static int sumOfFactorials(int num) {
		int sum = 0;
		int temp = num;
		while (temp > 0) {
			int digit = temp % 10;
			sum = sum + factorial(digit);
			temp = temp / 10;
		}
		return sum;
	}
	public static boolean isStrongNumber(int num) {
		if (num < 0) {
			return false;
		}
		return sumOfFactorials(num) == num;
	}
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter a non-negative integer: ");
		if (!sc.hasNextInt()) {
			System.out.println("Invalid input. Please enter an integer.");
			sc.close();
			return;
		}
		int number = sc.nextInt();
		if (number < 0) {
			System.out.println("Please enter a non-negative integer.");
			sc.close();
			return;
		}
		if (isStrongNumber(number)) {
			System.out.println(number + " is a Strong number.");
		} else {
			System.out.println(number + " is not a Strong number.");
		}
		sc.close();
	}
}