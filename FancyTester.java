import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FancyTester {

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        /*
         * Utils.writeStringToFile("junit_example_file_data.txt", "test file contents");
         * Utils.deleteFile("index");
         * Utils.deleteDirectory("objects");
         */
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
        /*
         * Utils.deleteFile("junit_example_file_data.txt");
         * Utils.deleteFile("index");
         * Utils.deleteDirectory("objects");
         */
    }

    @Test
    @DisplayName("[8] Test if initialize and objects are created correctly")
    void testInitialize() throws Exception {

        // Run the person's code
        // TestHelper.runTestSuiteMethods("testInitialize");
        Index ix = new Index();
        ix.init ();

        // check if the file exists
        File file = new File("index");
        Path path = Paths.get("objects");

        assertTrue(file.exists());
        assertTrue(Files.exists(path));
    }

    @Test
    @DisplayName("[15] Test if adding a blob works.  5 for sha, 5 for file contents, 5 for correct location")
    void testCreateBlob() throws Exception {

        try {

            // Manually create the files and folders before the 'testAddFile'
             Blob myBlob = new Blob("example3");
            Index blobIndex = new Index();
            blobIndex.init();

            FancyTester.runTestSuiteMethods("testCreateBlob", myBlob); //????

        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }

        //check if the blob obj exists in the objects folder
         File file_junit1 = new File("objects/" + myBlob.getShaString());
         assertTrue("Blob file to add not found", file_junit1.exists());

        // Read file contents
        String indexFileContents = MyUtilityClass.readAFileToAString("objects/" + file1.methodToGetSha1());
        assertEquals("File contents of Blob don't match file contents pre-blob creation", indexFileContents,
                file1.getContents());
    }
}
