//input principal, rate, time and print compound interest
package experiment2;
import java.util.Scanner;
public class experiment23 {
    public static void main(String[] args) {
        System.out.println("Suryansh Mishra 590012069");
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter principal: ");
        double p = sc.nextDouble();
        System.out.print("Enter rate: ");
        double r = sc.nextDouble();
        System.out.print("Enter time: ");
        double t = sc.nextDouble();
        double ci = p * Math.pow(1 + r / 100, t) - p;
        System.out.println("Compound Interest: " + ci);
        sc.close();
    }
}