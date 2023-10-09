import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class IndexTest {
    static Index blobIndex;
    static Blob bob;

    //before stuff runs, it should create a new file and initialize the index and blob
        @BeforeAll
        static void setUpBeforeClass() throws Exception {
            
            TesterHelper.writeStringToFile("junit_example.txt", "test file contents");
            bob = new Blob ("junit_example.txt");
            blobIndex = new Index ();
            
            File dir = new File ("objects");
            dir.mkdirs();
            File file = new File ("objects/" + bob.getShaString()); //file = file you write to
            file.createNewFile();
            
            
        }

        //after file runs, it should delete the test files and test directories
    @AfterAll
    static void tearDownAfterClass() throws Exception {
        
        TesterHelper.deleteFile("junit_example.txt");
        TesterHelper.deleteFile("index");
        File obj = new File ("objects");
        obj.mkdir();
        obj.delete();
    }
    
    //adds a blob to the index
    @Test
    void testAddBlob() throws IOException, NoSuchAlgorithmException {
        // Manually create the files and folders before the 'testAddFile'
        blobIndex.init();
        Index.addBlob ("junit_example.txt");

        try {
        //FancyTester.runTestSuiteMethods("testCreateBlob", myBlob);

        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }

        //check if the blob obj exists in the objects folder
        File file_junit1 = new File("objects/" + bob.getShaString());
        assertTrue("Blob file to add not found", file_junit1.exists()); 
        //if file does not exist, then "Blob file to add not found" is output

        // Read file contents
        String indexFileContents = TesterHelper.readAFileToAString("objects/" + bob.getShaString());
        assertEquals(indexFileContents, bob.getFileContents());
            //if the file contents of both tests don't match up, then the test will return "file contents of blob..."
    }

    //initialzes the index
    @Test
    void testInit() throws IOException {
        blobIndex.init ();

        // check if the file exists
        File file = new File("index");
        Path path = Paths.get("objects");

        assertTrue("File does not exist :(", file.exists());
        //if file does not exist, returns "file does not exist :("
        assertTrue("File path does not exist :(", Files.exists(path));
        //if the file path does not exist, return "file path does not exist :("
    }

    @Test
    void testWriteHashMap() throws IOException {
        // Index ix = new Index();
        // File index = new File("index");
        // index.createNewFile();
        // ix.init ();
        // ix.writeHashMap ();

        // try {
        // File f = new File ("f");
        // PrintWriter pw = new PrintWriter ("f");
        

        // } catch (Exception e) {
        //     System.out.println("An error occurred: " + e.getMessage());
        // }

        // pw.close();
        
        // index.list ();

        // assertEquals("Map is incorrect", , myBlob.getFileContents());
    }

    //removes the blob
    @Test
    void testRemoveBlob() throws NoSuchAlgorithmException, IOException {
        blobIndex.init();
        blobIndex.removeBlob("junit_example.txt");

        try {
        //FancyTester.runTestSuiteMethods("testRemoveBlob", myBlob);

        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }

        //check if the blob obj exists in the objects folder
        File file_junit1 = new File("objects/" + bob.getShaString());
        assertTrue("Blob is not being removed", file_junit1.exists());
        //if the file does not exist, return "blob is not being removed"

    }
}
