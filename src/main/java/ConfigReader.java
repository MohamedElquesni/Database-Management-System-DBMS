import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    public static int configReaderInt(String s)
    {
        Properties properties = new Properties();
        FileInputStream inputStream = null;
        try {
            // Load the properties from the file
            inputStream = new FileInputStream("DBApp.config");
            properties.load(inputStream);

            // Retrieve values using keys
            int num = Integer.parseInt(properties.getProperty(s));
            return num;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Close the input stream
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }
}

