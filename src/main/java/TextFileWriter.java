import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class TextFileWriter {
    public static void metaWrite(String s) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("metadata.csv", true))) {
            // Write content to the file
            writer.write(s + "\n"); // Append newline to separate each entry
        } catch (IOException e) {
            // Handle any IO exceptions
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
    }
}
