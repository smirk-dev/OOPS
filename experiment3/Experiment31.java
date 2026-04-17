// 3.1.  Write a Java program to find the binomial coefficient.
package experiment3;
import java.util.Scanner;
public class Experiment31 {
	public static long factorial(int n) {
		long fact = 1L;
		for (int i = 1; i <= n; i++) {
			fact = fact * i;
		}
		return fact;
	}
	public static long binomial(int n, int r) {
		if (r < 0 || r > n) {
			return 0L;
		}
		if (n > 20) {
			return -1L;
		}
		long nFact = factorial(n);
		long rFact = factorial(r);
		long nrFact = factorial(n - r);
		long denominator = rFact * nrFact;
		return nFact / denominator;
	}
	public static boolean isNonNegativeInputs(int n, int r) {
		return n >= 0 && r >= 0;
	}
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter n: ");
		int n = sc.nextInt();
		System.out.print("Enter r: ");
		int r = sc.nextInt();
		if (!isNonNegativeInputs(n, r)) {
			System.out.println("Negative values are not allowed. Use n >= 0 and r >= 0.");
			sc.close();
			return;
		}
		long c = binomial(n, r);
		if (c == -1L) {
			System.out.println("n is too large for long-based factorials. Use n <= 20.");
		} else {
			System.out.println("Binomial Coefficient C(" + n + "," + r + ") = " + c);
		}
		sc.close();
	}
}