import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;

//import static java.nio.file.StandardCopyOption.*;

public class Blob {
    private String translatedToSHA1;
    private String fileContents;
    
    public Blob (String fileOnDisk) throws IOException, NoSuchAlgorithmException {
        this.fileContents = "";
        BufferedReader br = new BufferedReader(new FileReader(fileOnDisk));
        while (br.ready()) {
            fileContents += (char) (br.read());
        }
        br.close();
        translateToSha1();
        writeFile();
    }
    
    public void translateToSha1 () throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] messageDigest = md.digest(fileContents.getBytes());
        BigInteger idk = new BigInteger(1, messageDigest);
        this.translatedToSHA1 = idk.toString(16);
        while (translatedToSHA1.length()<32) {
            translatedToSHA1 = "0" + translatedToSHA1;
        }
    }
    
    public String getShaString() {
        return translatedToSHA1;
    }
    
    public void writeFile () throws IOException {
        
        //girl idk what kind of errors ur code was throwing, so i rewrote the 'create objects folder' stuff
        //it works the same way as the old way (i think), but should run on all computers now.
        File dir = new File ("objects");
        dir.mkdirs();
        File file = new File ("objects/" + getShaString()); //file = file you write to
        file.createNewFile();

        PrintWriter pw = new PrintWriter (file);
        pw.print(fileContents);
        pw.close();
        // Files.move(Paths.get("/Users/zhang/Desktop/HonorsTopics/Blob-And-Index-NatalieSophia/" + fileName),
        //    (Paths.get("/Users/zhang/Desktop/HonorsTopics/Blob-And-Index-NatalieSophia/objects/" + fileName)));
        
    
    }    
}
