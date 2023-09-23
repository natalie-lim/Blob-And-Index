import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.jupiter.api.Test;

public class TreeJUnit {
    private File test1;
    private File examplefile1;
    private File examplefile2;
    private File examplefile3;
    private String examplefile1contents = "the sha of this is ... ?";
    private String examplefile2contents = "zomg wut are u doing. LAWL";
    private String examplefile3contents = "LOL please dont read this.  Good job being thorough tho!";
    private String shaContents1 = Utils.getSHA(examplefile1contents);
    private String shaContents2 = Utils.getSHA(examplefile2contents);
    private String shaContents3 = Utils.getSHA(examplefile3contents);

    @Test
    void setUpFiles() {
        test1 = new File ("test1");
        this.test1.mkdirs();
        this.examplefile1 = new File(test1 + "/examplefile1.txt");
        this.examplefile2 = new File(test1 + "/examplefile2.txt");
        this.examplefile3 = new File(test1 + "/examplefile3.txt");
    }

    @Test
    void checkBlobs() throws Exception {
        Tree tree = new Tree();
        tree.addDirectory("test1");

        //checks if blobs are in object folder
        assertTrue((new File("objects/6cecd98f685b1c9bfce96f2bbf3f8f381bcc717e")).exists());
        //assertTrue((new File ("objects/" + shaContents1)).exists());
        assertTrue((new File ("objects/" + shaContents2)).exists());
        assertTrue((new File ("objects/" + shaContents3)).exists());
    }

    @Test
    void checkTree() throws Exception {
        Tree tree = new Tree();
        tree.addDirectory("test1");

        //checks that tree exists
        String treeContents = "blob: 6cecd98f685b1c9bfce96f2bbf3f8f381bcc717e: examplefile1.txt\nblob: 7588059d9f514dcf29aec96e4b3aff9a467f7172: examplefile3.txt\nblob: 7fb1c700700603eef612e0ffedff5e1fa5af50b6: examplefile2.txt\n";
        String shaContents = Utils.getSHA(treeContents);
        File treeFile = new File ("objects/" + shaContents);
        assertTrue(treeFile.exists());

        //checks if the contents are correct
        String actualContents = Utils.getFileContents(treeFile);
        assertEquals(actualContents, treeContents);
    }
}
