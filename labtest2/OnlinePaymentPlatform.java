package labtest2;
import java.util.Scanner;
interface Payment {
    void pay(double amount, double balance) throws Exception;}
class CreditCardPayment implements Payment {
    private String cardNumber;
    public CreditCardPayment(String cardNumber) {
        this.cardNumber = cardNumber;}
    @Override
    public void pay(double amount, double balance) throws Exception {
        if (amount > balance) {
            throw new Exception("Insufficient balance: amount is greater than balance.");}
        if (!cardNumber.matches("\\d{16}")) {
            throw new Exception("Credit card not valid: card number must be exactly 16 digits.");}
        System.out.println("Credit Card Payment Successful. Amount Paid: " + amount);}}
class UPIPayment implements Payment {
    private String upiId;
    public UPIPayment(String upiId) {
        this.upiId = upiId;}
    @Override
    public void pay(double amount, double balance) throws Exception {
        if (amount > balance) {
            throw new Exception("Insufficient balance: amount is greater than balance.");}
        if (!upiId.matches("^[a-zA-Z0-9._-]{2,}@[a-zA-Z]{2,}$")) {
            throw new Exception("Invalid UPI ID.");}
        System.out.println("UPI Payment Successful. Amount Paid: " + amount);}}
public class OnlinePaymentPlatform {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("Enter available balance: ");
            double balance = scanner.nextDouble();
            System.out.print("Enter amount to pay: ");
            double amount = scanner.nextDouble();
            System.out.println("Choose Payment Method:");
            System.out.println("1. Credit Card Payment");
            System.out.println("2. UPI Payment");
            System.out.print("Enter choice (1 or 2): ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            Payment payment;
            if (choice == 1) {
                System.out.print("Enter 16-digit Credit Card Number: ");
                String cardNumber = scanner.nextLine();
                payment = new CreditCardPayment(cardNumber);
            } else if (choice == 2) {
                System.out.print("Enter UPI ID (example: name@bank): ");
                String upiId = scanner.nextLine();
                payment = new UPIPayment(upiId);
            } else {
                System.out.println("Invalid choice.");
                scanner.close();
                return;
            }
            payment.pay(amount, balance);
        } catch (Exception e) {
            System.out.println("Payment Failed: " + e.getMessage());
        } finally {
            scanner.close();}}}