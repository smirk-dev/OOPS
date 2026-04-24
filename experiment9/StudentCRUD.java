package experiment9;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
public class StudentCRUD {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/oops_lab";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC driver not found.");
            System.out.println("Add mysql-connector-j JAR to classpath and run again.");
            return;}
        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String createTableSQL = "CREATE TABLE IF NOT EXISTS student ("
                    + "`is` INT PRIMARY KEY, "
                    + "name VARCHAR(50) NOT NULL, "
                    + "marks INT NOT NULL)";
            try (Statement stmt = con.createStatement()) {
                stmt.executeUpdate(createTableSQL);
                System.out.println("Table 'student' is ready.");}
            try (Statement stmt = con.createStatement()) {
                stmt.executeUpdate("DELETE FROM student");
                stmt.executeUpdate("INSERT INTO student (`is`, name, marks) VALUES (101, 'Arun', 78)");
                stmt.executeUpdate("INSERT INTO student (`is`, name, marks) VALUES (102, 'Bina', 85)");
                stmt.executeUpdate("INSERT INTO student (`is`, name, marks) VALUES (103, 'Charan', 91)");
                System.out.println("3 student records inserted.");}
            String updateSQL = "UPDATE student SET marks = ? WHERE `is` = ?";
            try (PreparedStatement ps = con.prepareStatement(updateSQL)) {
                ps.setInt(1, 95);
                ps.setInt(2, 102);
                int rows = ps.executeUpdate();
                System.out.println("Updated rows: " + rows);}
            String deleteSQL = "DELETE FROM student WHERE `is` = ?";
            try (PreparedStatement ps = con.prepareStatement(deleteSQL)) {
                ps.setInt(1, 101);
                int rows = ps.executeUpdate();
                System.out.println("Deleted rows: " + rows);}
            String selectSQL = "SELECT `is`, name, marks FROM student";
            try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(selectSQL)) {
                System.out.println("\nFinal student records:");
                System.out.println("ID\tName\tMarks");
                System.out.println("------------------------");
                while (rs.next()) {
                    int id = rs.getInt("is");
                    String name = rs.getString("name");
                    int marks = rs.getInt("marks");

                    System.out.println(id + "\t" + name + "\t" + marks);}}
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            System.out.println("Check: MySQL service is running, database exists, and credentials are correct.");
        }
    }
}