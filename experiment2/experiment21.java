//input two numbers as command line arguments and perform basic arithmetic operations
package experiment2;
public class experiment21 {
    public static void main(String[] args) {
        System.out.println("Suryansh Mishra 590012069");
        int num1 = Integer.parseInt(args[0]);
        int num2 = Integer.parseInt(args[1]);
        System.out.println("Addition: " + (num1 + num2));
        System.out.println("Subtraction: " + (num1 - num2));
        System.out.println("Multiplication: " + (num1 * num2));
        System.out.println("Division: " + (num1 / num2));
    }
}