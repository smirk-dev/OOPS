import java.util.*;
public class Experiment41 {
	static Scanner sc;
	static int n, m;
	static String[] names;
	static int[][] scores;
	static int[] sum;
	static double[] avg;
	static int topIdx;
	static int bestQuarter;
	static List<Integer> improving;
	static Integer[] idx;
	public static void main(String[] args) {
		sc = new Scanner(System.in);
		input();
		average();
		output();
		sc.close();
	}
	static void input() {
		System.out.print("Enter number of employees: ");
		n = sc.nextInt();
		System.out.print("Enter number of quarters: ");
		m = sc.nextInt();
		sc.nextLine();
		names = new String[n];
		for (int i = 0; i < n; i++) {
			System.out.print("Enter name of employee " + (i + 1) + " (or press Enter to use 'Employee " + (i + 1) + "'): ");
			String line = sc.nextLine().trim();
			names[i] = line.isEmpty() ? ("Employee " + (i + 1)) : line;
		}
		scores = new int[n][m];
		for (int i = 0; i < n; i++) {
			System.out.println("Enter " + m + " scores for " + names[i] + " (separated by spaces):");
			for (int j = 0; j < m; j++) {
				scores[i][j] = sc.nextInt();
			}
			sc.nextLine();
		}
	}
	static void average() {
		sum = new int[n];
		for (int i = 0; i < n; i++) {
			int s = 0;
			for (int j = 0; j < m; j++) s += scores[i][j];
			sum[i] = s;
		}
		avg = new double[n];
		for (int i = 0; i < n; i++) avg[i] = (double) sum[i] / m;
		topIdx = 0;
		for (int i = 1; i < n; i++) if (avg[i] > avg[topIdx]) topIdx = i;
		int[] quarterSum = new int[m];
		bestQuarter = 0;
		for (int j = 0; j < m; j++) {
			int s = 0;
			for (int i = 0; i < n; i++) s += scores[i][j];
			quarterSum[j] = s;
		}
		for (int j = 1; j < m; j++) if (quarterSum[j] > quarterSum[bestQuarter]) bestQuarter = j;
		improving = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			boolean inc = true;
			for (int j = 0; j < m - 1; j++) {
				if (!(scores[i][j] < scores[i][j + 1])) {
					inc = false;
					break;
				}
			}
			if (inc) improving.add(i);
		}
		idx = new Integer[n];
		for (int i = 0; i < n; i++) idx[i] = i;
		Arrays.sort(idx, (a, b) -> Integer.compare(sum[b], sum[a]));
	}
	static void output() {
		System.out.println();
		printInputTable();
		System.out.println("Top Performer: " + names[topIdx]);
		System.out.println("Best Performing Quarter: Quarter " + (bestQuarter + 1));
		System.out.print("Improving Employees: ");
		if (improving.isEmpty()) {
			System.out.println("None");
		} else {
			for (int k = 0; k < improving.size(); k++) {
				if (k > 0) System.out.print(", ");
				System.out.print(names[improving.get(k)]);
			}
			System.out.println();
		}
		System.out.println();
		System.out.println("Sorted Performance Ranking:");
		for (int i = 0; i < n; i++) {
			System.out.println(names[idx[i]]);
		}
	}

	static void printInputTable() {
		System.out.println();
		System.out.println("Input Table:");
		System.out.printf("%-16s", "Employee Name");
		for (int j = 0; j < m; j++) {
			System.out.printf("Q%-8d", j + 1);
		}
		System.out.println();
		for (int i = 0; i < n; i++) {
			System.out.printf("%-16s", names[i]);
			for (int j = 0; j < m; j++) {
				System.out.printf("%-9d", scores[i][j]);
			}
			System.out.println();
		}
	}
}