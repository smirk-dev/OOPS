package experiment10;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
public class Experiment10 {
    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- ArrayList Operations ---");
            System.out.println("1. Add Element");
            System.out.println("2. Remove Element");
            System.out.println("3. Search Element");
            System.out.println("4. Display Elements");
            System.out.println("5. Sort Elements");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.print("Enter element to add: ");
                    int addElement = scanner.nextInt();
                    list.add(addElement);
                    System.out.println(addElement + " added successfully.");
                    break;
                case 2:
                    if (list.isEmpty()) {
                        System.out.println("List is empty. Nothing to remove.");
                    } else {
                        System.out.print("Enter element to remove: ");
                        int removeElement = scanner.nextInt();
                        if (list.remove(Integer.valueOf(removeElement))) {
                            System.out.println(removeElement + " removed successfully.");
                        } else {
                            System.out.println(removeElement + " not found in the list.");}}
                    break;
                case 3:
                    if (list.isEmpty()) {
                        System.out.println("List is empty. Nothing to search.");
                    } else {
                        System.out.print("Enter element to search: ");
                        int searchElement = scanner.nextInt();
                        int index = list.indexOf(searchElement);
                        if (index != -1) {
                            System.out.println(searchElement + " found at index " + index + ".");
                        } else {
                            System.out.println(searchElement + " not found in the list.");}}
                    break;
                case 4:
                    if (list.isEmpty()) {
                        System.out.println("List is empty.");
                    } else {
                        System.out.println("Current elements: " + list);}
                    break;
                case 5:
                    if (list.isEmpty()) {
                        System.out.println("List is empty. Nothing to sort.");
                    } else {
                        Collections.sort(list);
                        System.out.println("Elements sorted in ascending order: " + list);}
                    break;
                case 6:
                    System.out.println("Exiting program...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please enter a number from 1 to 6.");}}}}