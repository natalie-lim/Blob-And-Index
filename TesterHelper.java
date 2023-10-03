import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;

public class TesterHelper {
    public static String readAFileToAString (String str) throws IOException
    {
        StringBuilder sb = new StringBuilder ("");
        BufferedReader reader = new BufferedReader (new FileReader (str));
        
        while (reader.ready())
        {
            sb.append ((char) reader.read());
        }

        reader.close();

        return sb.toString();
    }

    public static void writeStringToFile (String fileName, String fileContents) throws FileNotFoundException
    {
        PrintWriter pw = new PrintWriter (fileName);
        pw.println (fileContents);
        pw.close();
    }

    public static void deleteFile (String fileName) throws IOException
    {
        Files.delete (Paths.get(fileName));
    }

    public static void deleteDirectory (String fileName) throws IOException
    {
        Files.delete (Paths.get(fileName));
    }

    public static String getSha (String fileName) throws IOException
    {
        BufferedReader reader = new BufferedReader (new FileReader (fileName));
        StringBuilder sb = new StringBuilder ("");

        while (reader.ready())
        {
            sb.append ((char) reader.read());
        }
        reader.close();

        String value = sb.toString ();

		String sha1 = "";
		
		// With the java libraries
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
	        digest.reset();
	        digest.update(value.getBytes("utf8"));
	        sha1 = String.format("%040x", new BigInteger(1, digest.digest()));
		} catch (Exception e){
			e.printStackTrace();
		}

		return sha1;
        
    }

}
