import java.io.FileReader;
import java.io.IOException;
public class Labtest {
    public static void main(String[] args) {
        String filename = "sample.txt";
        try {
            FileReader reader = new FileReader(filename);
            System.out.println("Contents of " + filename + ":\n");
            int character;
            while ((character = reader.read()) != -1) {
                System.out.print((char) character);
            }
            System.out.println("\n");
            reader.close();
            System.out.println("File reading completed successfully.");
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}