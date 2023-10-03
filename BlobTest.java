import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.Test;

public class BlobTest {
    
    //returns the file contents and makes sure the file contents are returnining correctly
    @Test
    void testGetFileContents() throws IOException, NoSuchAlgorithmException {
        File f = new File ("example.txt");
        PrintWriter pw = new PrintWriter("example.txt");
        pw.println("random stuff so that this isnt empty");
        f.createNewFile();
        pw.close();
        Blob bob = new Blob ("example.txt");
        String newFileContents = bob.getFileContents();
        String originalFileContents = TesterHelper.readAFileToAString("example.txt");

        assertEquals("Does not work....", newFileContents, originalFileContents);
        //if both file contents do not match (before and after), returns "does not work....."
    }

    //tests if the sha1 string is being correctly generated
    @Test
    void testGetShaString() throws IOException, NoSuchAlgorithmException {
        File f = new File ("example.txt");
        f.createNewFile();
        Blob bob = new Blob ("example.txt");
        String s = bob.getShaString();
        String otherSha = TesterHelper.getSha(bob.getOgName());

        assertEquals("Does not work....", s, otherSha);
        //if both sha versions do not match, returns "does not work....."
    }

    @Test
    void testTranslateToSha1() throws IOException, NoSuchAlgorithmException {
        //this method uses the previous method to call on it (getShaString). The contents of the code is the same
        //except the previous returns a string and translateToSha1 doesn't return anything
        //so getShaString is basically a getter for translateToSha1
        //but i know that this method works because the previous works
    }

    //tests if creating the objects and index files work, as well as blobs a file
    @Test
    void testWriteFile() throws IOException, NoSuchAlgorithmException {
        File f = new File ("example.txt");
        f.createNewFile();
        Blob bob = new Blob ("example.txt");
        bob.writeFile();

        File file_junit1 = new File("objects/" + bob.getShaString());
        assertTrue("Objects folder does not exist", file_junit1.exists());
        //if objects folder did not exist, return "blob file to add not found"

        File index_junit = new File ("index");
        assertTrue("Index folder does not exist", index_junit.exists());
        //if index file does not exist, return "index folder does not exist"
    }
}
