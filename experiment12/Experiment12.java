package experiment12;
import java.sql.*;
import java.util.Scanner;
public class Experiment12 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter DB host (e.g., localhost):");
        String host = sc.nextLine().trim();
        System.out.println("Enter DB port (e.g., 3306):");
        String port = sc.nextLine().trim();
        System.out.println("Enter database name:");
        String db = sc.nextLine().trim();
        System.out.println("Enter DB username:");
        String user = sc.nextLine().trim();
        String password;
        if (System.console() != null) {
            password = new String(System.console().readPassword("Enter DB password:\n"));
        } else {
            System.out.println("Enter DB password:");
            password = sc.nextLine();}
        String url = String.format("jdbc:mysql://%s:%s/%s?useSSL=false&serverTimezone=UTC", host, port, db);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found. Add the connector to classpath.");
            return;}
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {
            String createTable = "CREATE TABLE IF NOT EXISTS student (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "name VARCHAR(100) NOT NULL, " +
                    "age INT, " +
                    "email VARCHAR(100)" +
                    ")";
            stmt.execute(createTable);
            String insertSql = "INSERT INTO student(name, age, email) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                System.out.println("How many student records do you want to insert?");
                int n = Integer.parseInt(sc.nextLine().trim());
                for (int i = 1; i <= n; i++) {
                    System.out.printf("--- Student %d ---\n", i);
                    System.out.println("Name:");
                    String name = sc.nextLine().trim();
                    System.out.println("Age:");
                    String ageStr = sc.nextLine().trim();
                    int age = ageStr.isEmpty() ? 0 : Integer.parseInt(ageStr);
                    System.out.println("Email:");
                    String email = sc.nextLine().trim();
                    pstmt.setString(1, name);
                    if (age == 0) pstmt.setNull(2, Types.INTEGER); else pstmt.setInt(2, age);
                    if (email.isEmpty()) pstmt.setNull(3, Types.VARCHAR); else pstmt.setString(3, email);
                    int affected = pstmt.executeUpdate();
                    if (affected == 1) System.out.println("Inserted successfully.");
                    else System.out.println("Insert may have failed for this record.");}}
            System.out.println("\nCurrent rows in student table:");
            try (ResultSet rs = stmt.executeQuery("SELECT id, name, age, email FROM student")) {
                while (rs.next()) {
                    System.out.printf("%d | %s | %s | %s\n",
                            rs.getInt("id"), rs.getString("name"),
                            rs.getObject("age") == null ? "NULL" : rs.getInt("age"),
                            rs.getString("email"));}}
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            sc.close();}}}