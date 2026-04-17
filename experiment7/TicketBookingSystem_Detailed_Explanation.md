# TicketBookingSystem.java - Hyper Detailed Explanation

## 1. Objective of the Program

This program simulates a ticket booking system where multiple users try to book seats concurrently.

Core goals:

- Demonstrate Java multithreading using `Thread`.
- Demonstrate thread safety using `synchronized` methods.
- Validate user input robustly.
- Track successful and failed booking attempts.
- Produce a clear booking summary.

It is designed as a lab-friendly OOP plus concurrency example where correctness under concurrent access is the key concept.

---

## 2. High-Level Architecture

The code is organized into one public class: `TicketBookingSystem`.

Inside it, there are two nested static classes:

1. `TicketBooking`
- Handles core business logic for booking.
- Maintains shared state (available seats, counters).
- Provides synchronized methods so multiple threads cannot corrupt data.

2. `User` (extends `Thread`)
- Represents one booking request from one user.
- In `run()`, it calls `ticketBooking.bookTicket(...)`.

Main flow in `main()`:

1. Read total seats.
2. Read number of users.
3. For each user, read user name and seats requested.
4. Create one `User` thread per booking request.
5. Start all threads.
6. Wait for all threads using `join()`.
7. Print final summary.

---

## 3. Package and Imports

```java
package experiment7;
import java.util.Scanner;
```

- `package experiment7;` groups this class under the experiment folder/package.
- `Scanner` is used for console input.

---

## 4. Deep Dive: `TicketBooking` Class

### 4.1 Purpose

`TicketBooking` is the shared resource object. All user threads interact with the same `TicketBooking` instance.

This is important: because all threads share one object, synchronization can protect shared data consistently.

### 4.2 Fields

```java
private final int totalSeats;
private int availableSeats;
private int successfulBookings;
private int failedBookings;
```

- `totalSeats`:
  - Immutable after construction (`final`).
  - Represents initial capacity.
- `availableSeats`:
  - Mutable remaining seats after bookings.
- `successfulBookings`:
  - Number of requests that succeeded.
- `failedBookings`:
  - Number of requests that failed (invalid name, invalid seat count, or insufficient seats).

### 4.3 Constructor

```java
public TicketBooking(int totalSeats) {
    if (totalSeats <= 0) {
        throw new IllegalArgumentException("Total seats must be greater than 0.");
    }
    this.totalSeats = totalSeats;
    this.availableSeats = totalSeats;
}
```

Key details:

- Defensive programming: rejects invalid system setup (`<= 0`).
- Initializes `availableSeats` equal to initial capacity.
- Ensures object starts in a valid state.

### 4.4 Critical Method: `bookTicket(...)`

```java
public synchronized boolean bookTicket(String userName, int seatsRequested)
```

#### Why `synchronized` matters

Without synchronization, race conditions can occur:

- Two threads read same `availableSeats` value.
- Both pass the availability check.
- Both subtract seats.
- Final seats become incorrect (possibly negative or oversold).

Using `synchronized` ensures:

- Only one thread executes booking logic at a time for this object.
- Availability check and seat deduction happen atomically as one protected section.

#### Full logic sequence

1. Validate username
- Calls `isValidUserName(userName)`.
- If invalid:
  - Increment `failedBookings`.
  - Print failure reason.
  - Return `false`.

2. Validate requested seats
- If `seatsRequested <= 0`:
  - Increment `failedBookings`.
  - Print failure reason.
  - Return `false`.

3. Attempt booking
- Print attempt message.
- If enough seats (`seatsRequested <= availableSeats`):
  - Decrease `availableSeats`.
  - Increment `successfulBookings`.
  - Print success message.
  - Return `true`.
- Else:
  - Increment `failedBookings`.
  - Print not-enough-seats message.
  - Return `false`.

#### Return value semantics

- `true`: booking finalized.
- `false`: booking rejected for any reason.

This gives a clean contract for callers or future extensions.

### 4.5 Helper Validation Method

```java
private boolean isValidUserName(String userName) {
    return userName != null && !userName.trim().isEmpty();
}
```

- Rejects `null`.
- Rejects empty and whitespace-only names.
- `trim()` removes leading/trailing spaces before emptiness check.

### 4.6 Getter Methods (All Synchronized)

```java
public synchronized int getTotalSeats()        { return totalSeats; }
public synchronized int getAvailableSeats()    { return availableSeats; }
public synchronized int getSuccessfulBookings(){ return successfulBookings; }
public synchronized int getFailedBookings()    { return failedBookings; }
public synchronized int getTotalRequests()     { return successfulBookings + failedBookings; }
```

Why synchronized getters are useful:

- They provide a consistent view when accessed after concurrent updates.
- They avoid stale/partial reads in multithreaded contexts.

Note: `getTotalSeats()` reads an immutable field, so synchronization is not strictly required for that field alone, but keeping all getters synchronized gives uniform thread-safe access style.

---

## 5. Deep Dive: `User` Thread Class

```java
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
```

### Design interpretation

- Each `User` object is one thread and one booking request.
- Thread name is used as logical user identity (`super(name)` + `getName()`).
- In `run()`, each thread invokes booking once.

### Why this is good for labs

- Very clear mapping between "thread" and "user action".
- Easy to discuss thread lifecycle:
  - Created -> started -> executes `run()` -> ends.

---

## 6. Display Constants and UI Helpers

The program uses formatting constants:

```java
private static final String DIVIDER = "=".repeat(50);
private static final String SECTION = "+" + "-".repeat(48) + "+";
private static final String FOOTER = "+" + "-".repeat(48) + "+";
```

These improve readability of console output and keep formatting centralized.

### Utility methods

- `printHeader()` -> displays title block.
- `printSection(String title)` -> section separator with a heading.
- `printSimulationStart()` -> start marker.
- `printSummary(TicketBooking booking)` -> final statistics table.
- `centerText(String text, int width)` -> centers title text.
- `formatRow(String label, int value)` -> fixed-width summary rows.

This separation keeps `main()` cleaner and easier to understand.

---

## 7. Main Method: Full Control Flow Walkthrough

### 7.1 Input setup

```java
Scanner scanner = new Scanner(System.in);
printHeader();
```

- Creates console input stream reader.
- Prints heading before asking inputs.

### 7.2 Read total seats

```java
int totalSeats = getValidIntInput(scanner, "Enter total number of seats", 1);
TicketBooking booking = new TicketBooking(totalSeats);
```

- Uses reusable validator to guarantee integer >= 1.
- Creates shared booking object.

### 7.3 Read user count and allocate thread array

```java
int numUsers = getValidIntInput(scanner, "Enter number of users", 1);
Thread[] users = new Thread[numUsers];
```

- Again enforces valid positive count.
- Stores all user threads for later `start()` and `join()` loops.

### 7.4 Configure each user request

Inside loop:

1. Print user index marker.
2. Read validated non-empty user name.
3. Read validated positive seat request.
4. Create `User` thread with shared `booking` object.

Result: all threads are prepared but not yet started.

### 7.5 Close scanner

```java
scanner.close();
```

- Good resource practice: input stream closed once no longer needed.

### 7.6 Start simulation

```java
for (Thread user : users) user.start();
```

- Threads begin running concurrently.
- Scheduling order is decided by JVM/OS thread scheduler.
- Booking attempt order is therefore non-deterministic.

### 7.7 Wait for completion

```java
for (Thread user : users) user.join();
```

- `join()` blocks main thread until each user thread finishes.
- Ensures summary is printed only after all attempts are complete.

Interrupted handling:

```java
catch (InterruptedException e) {
    Thread.currentThread().interrupt();
    System.out.println("Main thread interrupted.");
}
```

- Best practice: restore interrupt status with `interrupt()`.

### 7.8 Print final summary

`printSummary(booking);` reads synchronized getters and outputs final state.

---

## 8. Input Validation Methods

### 8.1 `getValidIntInput(...)`

Behavior:

- Prompts until user gives a valid integer >= `minValue`.
- Handles non-integer input using `NumberFormatException`.
- Prevents invalid internal state from bad console input.

Important design choice:

- Uses `scanner.nextLine()` then `Integer.parseInt(...)`.
- This avoids newline-buffer issues that often occur when mixing `nextInt()` and `nextLine()`.

### 8.2 `getValidUserName(...)`

Behavior:

- Prompts until non-empty trimmed name is entered.
- Prevents blank identity values during thread naming and booking logs.

---

## 9. Concurrency Correctness Analysis

### 9.1 Shared mutable state

Shared variables are:

- `availableSeats`
- `successfulBookings`
- `failedBookings`

All updates happen inside synchronized methods on the same object.

### 9.2 Atomicity and consistency

Booking operation performs:

1. Check availability.
2. Subtract seats.
3. Increment success/failure counters.

Because method is synchronized, another thread cannot interleave between these steps. This preserves correctness.

### 9.3 Visibility

`sychronized` (monitor enter/exit) also creates memory visibility guarantees.

- Writes by one thread become visible to others entering synchronized methods on the same lock.

### 9.4 Determinism vs nondeterminism

- Final totals are deterministic (logically correct).
- Order of individual booking messages is nondeterministic (thread scheduling).

This is expected and normal in concurrent programs.

---

## 10. Data Integrity Invariants

After each booking attempt, these should hold:

1. `0 <= availableSeats <= totalSeats`
2. `successfulBookings >= 0`
3. `failedBookings >= 0`
4. `successfulBookings + failedBookings == total number of processed requests`
5. `availableSeats` only decreases on successful bookings

The current synchronized logic preserves these invariants.

---

## 11. Time and Space Complexity

Let:

- `n` = number of users/requests.

### Booking processing

- Each booking request executes O(1) logic.
- Across all users: O(n) total operations.

### Thread overhead

- Creating and scheduling `n` threads has practical runtime overhead, though algorithmic booking logic is still O(n).

### Space

- `Thread[] users` stores `n` references: O(n) space.
- Shared booking state is O(1).

---

## 12. Example Execution Scenario

Suppose:

- Total seats = 5
- Users:
  - A requests 2
  - B requests 3
  - C requests 1

Possible order:

1. B succeeds (seats left 2)
2. A succeeds (seats left 0)
3. C fails (not enough seats)

Final summary:

- Successful: 2
- Failed: 1
- Remaining: 0

Another run may process A first, then B, then C, etc. Order changes, but overselling never occurs.

---

## 13. Strengths of the Implementation

- Correct use of synchronization around critical section.
- Clear separation of concerns (input, logic, thread behavior, output formatting).
- Defensive input checks and constructor validation.
- Proper thread completion using `join()`.
- Clean summary metrics useful for demonstration and testing.

---

## 14. Practical Limitations and Improvements

1. One thread per user request can be heavy for large `n`.
Possible improvement: use `ExecutorService` thread pool.

2. Booking is only one-shot request per user.
Possible improvement: allow retries or menu-driven operations.

3. No persistence.
Possible improvement: save booking records to file/database.

4. Logging uses `System.out.println`.
Possible improvement: use Java logging framework.

5. Unicode formatting in original output can behave differently on some terminals.
Possible improvement: keep ASCII-only output for maximum compatibility.

---

## 15. Viva / Interview Style Questions and Answers

### Q1. Why is `bookTicket` synchronized?
To prevent race conditions when multiple threads access and modify shared seat and counter data.

### Q2. What could go wrong without synchronization?
Overselling seats, inconsistent counters, and invalid final state due to interleaving updates.

### Q3. Why use `join()` in main?
To make sure summary is printed only after all booking threads complete.

### Q4. Why use `nextLine()` and parse integer manually?
To avoid scanner buffering issues and to handle invalid input cleanly with exceptions.

### Q5. Why is `totalSeats` final?
Because initial capacity should never change after object creation.

### Q6. Is synchronization needed on getters?
Not always for immutable values, but useful here for consistent thread-safe reads of mutable state.

### Q7. Does this guarantee message order?
No. Thread execution order is scheduler-dependent. It only guarantees correctness, not print sequence order.

---

## 16. Section-by-Section Mapping to Source Code

- Class declaration and nested classes: overall architecture setup.
- `TicketBooking` constructor: state initialization plus validity check.
- `bookTicket`: core synchronized critical section.
- `User.run`: thread entry point for booking action.
- `main`: orchestration of input -> thread creation -> execution -> summary.
- `getValidIntInput` and `getValidUserName`: robust input layer.
- print helpers: output structure and readability.

---

## 17. Suggested Test Cases

1. Basic success:
- seats 10, one user requests 5 -> success, remaining 5.

2. Exact fill:
- seats 5, users request 2 and 3 -> both success, remaining 0.

3. Over-request:
- seats 4, one user requests 6 -> fail.

4. Invalid seats requested:
- request 0 or negative -> fail.

5. Invalid name:
- empty or spaces-only user name -> fail.

6. Many concurrent users:
- verify no negative seats, no overselling.

7. Input validation:
- non-numeric values for seat/user counts should reprompt and continue.

---

## 18. Final Conceptual Summary

This program is a clean educational example of safe concurrent resource management in Java.

- One shared object (`TicketBooking`) protects state with synchronized methods.
- Multiple user threads simulate real-world concurrent requests.
- Input validation and summary reporting make behavior predictable and easy to verify.

In short: it demonstrates OOP design + multithreading + synchronization in a practical, lab-ready ticket booking scenario.
