import java.util.Scanner;
public class ExceptionHandling {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("exception handling through division of two numbers");
		System.out.print("Enter first number: ");
		String s1 = sc.next();
		System.out.print("Enter second number: ");
		String s2 = sc.next();
		try {
			int a = Integer.parseInt(s1);
			int b = Integer.parseInt(s2);
			int result = a / b;
			System.out.println("Division result: " + result);
		} catch (NumberFormatException e) {
			System.out.println("NumberFormatException: Please enter valid integers.");
		} catch (ArithmeticException e) {
			System.out.println("ArithmeticException: Division by zero is not allowed.");
		}
		try {
			System.out.print("Enter array size: ");
			int size = Integer.parseInt(sc.next());
			int[] arr = new int[size];
			for (int i = 0; i < size; i++) {
				arr[i] = i + 1;
			}
			System.out.println("Array created. Valid indices: 0 to " + (size - 1));
			while (true) {
				System.out.print("Enter index to access: ");
				String idxStr = sc.next();
				try {
					int idx = Integer.parseInt(idxStr);
					System.out.println("Value at index " + idx + " is " + arr[idx]);
					break; // success
				} catch (NumberFormatException nfe) {
					System.out.println("Please enter a valid integer index.");
				} catch (ArrayIndexOutOfBoundsException aioobe) {
					System.out.println("Exception caught: " + aioobe.getClass().getSimpleName()
							+ " — requested index " + idxStr + ", array size " + size
							+ ". Please choose an index between 0 and " + (size - 1) + ".");
				}
			}
		} catch (NumberFormatException e) {
			System.out.println("NumberFormatException while reading array size: Please enter a valid integer for size.");
		}
		sc.close();
	}
}