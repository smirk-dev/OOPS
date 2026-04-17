package experiment7;
import java.util.Scanner;
public class TicketBookingSystem {
    static class TicketBooking {
        private final int totalSeats;
        private int availableSeats;
        private int successfulBookings;
        private int failedBookings;
        public TicketBooking(int totalSeats) {
            if (totalSeats <= 0) {
                throw new IllegalArgumentException("Total seats must be greater than 0.");
            }
            this.totalSeats = totalSeats;
            this.availableSeats = totalSeats;
        }
        public synchronized boolean bookTicket(String userName, int seatsRequested) {
            if (!isValidUserName(userName)) {
                failedBookings++;
                System.out.println("Booking failed: invalid user name. Seats left: " + availableSeats + "\n");
                return false;
            }
            if (seatsRequested <= 0) {
                failedBookings++;
                System.out.println("Booking failed for " + userName + ": seats requested must be > 0. Seats left: " + availableSeats + "\n");
                return false;
            }
            System.out.println(userName + " is trying to book " + seatsRequested + " seat(s)...");
            if (seatsRequested <= availableSeats) {
                availableSeats -= seatsRequested;
                successfulBookings++;
                System.out.println("Booking successful for " + userName + ": " + seatsRequested + " seat(s) booked. Seats left: " + availableSeats + "\n");
                return true;
            }
            failedBookings++;
            System.out.println("Booking failed for " + userName + ": not enough seats. Seats left: " + availableSeats + "\n");
            return false;
        }
        private boolean isValidUserName(String userName) {
            return userName != null && !userName.trim().isEmpty();
        }
        public synchronized int getTotalSeats()        { return totalSeats; }
        public synchronized int getAvailableSeats()    { return availableSeats; }
        public synchronized int getSuccessfulBookings(){ return successfulBookings; }
        public synchronized int getFailedBookings()    { return failedBookings; }
        public synchronized int getTotalRequests()     { return successfulBookings + failedBookings; }
    }
    static class User extends Thread {
        private final TicketBooking ticketBooking;
        private final int seatsToBook;
        public User(String name, TicketBooking ticketBooking, int seatsToBook) {
            super(name);
            this.ticketBooking = ticketBooking;
            this.seatsToBook = seatsToBook;
        }
        @Override
        public void run() {
            ticketBooking.bookTicket(getName(), seatsToBook);
        }
    }
    private static final String DIVIDER = "═".repeat(50);
    private static final String SECTION = "┌" + "─".repeat(48) + "┐";
    private static final String FOOTER = "└" + "─".repeat(48) + "┘";
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        printHeader();
        int totalSeats = getValidIntInput(scanner, "Enter total number of seats", 1);
        TicketBooking booking = new TicketBooking(totalSeats);
        int numUsers = getValidIntInput(scanner, "Enter number of users", 1);
        Thread[] users = new Thread[numUsers];
        printSection("User Configuration");
        for (int i = 0; i < numUsers; i++) {
            System.out.println("\n[User " + (i + 1) + "/" + numUsers + "]");
            String userName = getValidUserName(scanner, "Enter name for user " + (i + 1));
            int seatsToBook = getValidIntInput(scanner, "Enter seats to book for " + userName, 1);
            users[i] = new User(userName, booking, seatsToBook);
        }
        scanner.close();
        printSimulationStart();
        for (Thread user : users) user.start();
        try {
            for (Thread user : users) user.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("⚠ Main thread interrupted.");
        }
        printSummary(booking);
    }
    private static int getValidIntInput(Scanner scanner, String prompt, int minValue) {
        int value = 0;
        while (value < minValue) {
            try {
                System.out.print("  " + prompt + ": ");
                value = Integer.parseInt(scanner.nextLine());
                if (value < minValue) {
                    System.out.println("  ✗ Please enter a number ≥ " + minValue);
                }
            } catch (NumberFormatException e) {
                System.out.println("  ✗ Invalid input. Please enter a valid number.");
            }
        }
        return value;
    }
    private static String getValidUserName(Scanner scanner, String prompt) {
        String userName = "";
        while (userName.trim().isEmpty()) {
            System.out.print("  " + prompt + ": ");
            userName = scanner.nextLine();
            if (userName.trim().isEmpty()) {
                System.out.println("  ✗ User name cannot be empty.");
            }
        }
        return userName;
    }
    private static void printHeader() {
        System.out.println("\n" + SECTION);
        System.out.println("│" + centerText("🎫 TICKET BOOKING SYSTEM", 48) + "│");
        System.out.println("│" + centerText("Multi-threaded Simulation", 48) + "│");
        System.out.println(FOOTER);
    }
    private static void printSection(String title) {
        System.out.println("\n" + DIVIDER);
        System.out.println("► " + title);
        System.out.println(DIVIDER);
    }
    private static void printSimulationStart() {
        System.out.println("\n" + DIVIDER);
        System.out.println("▶ Starting booking simulation...");
        System.out.println(DIVIDER + "\n");
    }
    private static void printSummary(TicketBooking booking) {
        System.out.println("\n" + SECTION);
        System.out.println("│" + centerText("BOOKING SUMMARY", 48) + "│");
        System.out.println("├" + "─".repeat(48) + "┤");
        System.out.println(formatRow("Total Seats Available", booking.getTotalSeats()));
        System.out.println(formatRow("Total Requests", booking.getTotalRequests()));
        System.out.println(formatRow("✓ Successful Bookings", booking.getSuccessfulBookings()));
        System.out.println(formatRow("✗ Failed Bookings", booking.getFailedBookings()));
        System.out.println(formatRow("Remaining Seats", booking.getAvailableSeats()));
        System.out.println(FOOTER + "\n");
    }
    private static String centerText(String text, int width) {
        int padding = (width - text.length()) / 2;
        return " ".repeat(Math.max(0, padding)) + text + " ".repeat(Math.max(0, width - padding - text.length()));
    }
    private static String formatRow(String label, int value) {
        int labelWidth = 28;
        int valueWidth = 18;
        String paddedLabel = label + " ".repeat(Math.max(0, labelWidth - label.length()));
        String paddedValue = " ".repeat(Math.max(0, valueWidth - String.valueOf(value).length())) + value;
        return "│ " + paddedLabel + paddedValue + " │";
    }
}