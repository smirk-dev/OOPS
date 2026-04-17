// 4.2.  Write a java program to print the N bit binary counter in increasing order.
package experiment3;
import java.util.Scanner;
public class Experiment33 {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int n = readN(sc);
		if (!validateN(n)) {
			sc.close();
			return;
		}
		System.out.println("Printing " + n + "-bit binary counter in increasing order:");
		printBinaryCounter(n);
		sc.close();
	}
	public static int readN(Scanner sc) {
		System.out.print("Enter number of bits N: ");
		int n = sc.nextInt();
		return n;
	}
	public static boolean validateN(int n) {
		if (n <= 0) {
			System.out.println("Please enter a positive number.");
			return false;
		}
		if (n > 30) {
			System.out.println("Please enter N <= 30 for this simple program.");
			return false;
		}
		return true;
	}
	public static void printBinaryCounter(int n) {
		int total = 1 << n;
		for (int i = 0; i < total; i++) {
			String bin = Integer.toBinaryString(i);
			if (bin.length() < n) {
				int need = n - bin.length();
				StringBuilder sb = new StringBuilder();
				for (int k = 0; k < need; k++) sb.append('0');
				sb.append(bin);
				bin = sb.toString();
			}
			System.out.println(bin);
		}
	}
}