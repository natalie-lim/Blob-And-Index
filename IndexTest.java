import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.Test;

public class IndexTest {
    @Test
    void testAddBlob() throws IOException, NoSuchAlgorithmException {
        // Manually create the files and folders before the 'testAddFile'
        Blob myBlob = new Blob("example3");
        Index blobIndex = new Index();
        blobIndex.init();
        Index.addBlob ("example3");

        try {
        //FancyTester.runTestSuiteMethods("testCreateBlob", myBlob);

        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }

        //check if the blob obj exists in the objects folder
        File file_junit1 = new File("objects/" + myBlob.getShaString());
        assertTrue("Blob file to add not found", file_junit1.exists()); //if this test fails, then "Blob file to add not found" is output

        // Read file contents
        String indexFileContents = TesterHelper.readAFileToAString("objects/" + myBlob.getShaString());
        assertEquals("File contents of Blob don't match file contents pre-blob creation", indexFileContents,
                myBlob.getFileContents());
    }

    @Test
    void testInit() throws IOException {
        Index ix = new Index();
        ix.init ();

        // check if the file exists
        File file = new File("index");
        Path path = Paths.get("objects");

        assertTrue("File does not exist :(", file.exists());
        assertTrue("File path does not exist :(", Files.exists(path));
    }

    @Test
    void testPrintMap() {

    }

    @Test
    void testRemoveBlob() {

    }
}
