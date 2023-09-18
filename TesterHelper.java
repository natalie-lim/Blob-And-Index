import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TesterHelper {
    public static String readAFileToAString (String str) throws IOException
    {
        StringBuilder sb = new StringBuilder ("");
        BufferedReader reader = new BufferedReader (new FileReader ("str"));
        
        while (reader.ready())
        {
            sb.append ((char) reader.read());
        }

        reader.close();

        return sb.toString();
    }
}
