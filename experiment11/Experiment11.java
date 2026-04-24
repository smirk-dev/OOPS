package experiment11;

import java.util.Stack;

public class Experiment11 {
    public static void main(String[] args) {
        Stack<Integer> stack = new Stack<>();

        System.out.println("Initial stack: " + stack);
        System.out.println("Is stack empty? " + stack.isEmpty());

        // Push operation
        stack.push(10);
        stack.push(20);
        stack.push(30);

        System.out.println("\nAfter push operations: " + stack);
        System.out.println("Top element (peek): " + stack.peek());

        // Pop operations
        System.out.println("\nPopped element: " + stack.pop());
        System.out.println("Stack after 1st pop: " + stack);

        System.out.println("Popped element: " + stack.pop());
        System.out.println("Stack after 2nd pop: " + stack);

        System.out.println("Popped element: " + stack.pop());
        System.out.println("Stack after 3rd pop: " + stack);

        System.out.println("\nIs stack empty after pop operations? " + stack.isEmpty());
    }
}
