import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

public class TreeJUnit {
    private File test1;
    private File examplefile1;
    private File examplefile2;
    private File examplefile3;
    private String examplefile1contents = "the sha of this is ... ?";
    private String examplefile2contents = "zomg wut are u doing. LAWL";
    private String examplefile3contents = "LOL please dont read this.  Good job being thorough tho!";

    private File test2;
    private File folder1;
    private File folder2;
    private File examplefile12;
    private File examplefile22;
    private File examplefile32;
    private File examplefile4;
    private File examplefile5;
    private File examplefile6;
    private String examplefile4contents = "testing testing testing";
    private String examplefile5contents = "i am testing the limits of my own patience";
    private String examplefile6contents = "senioritis is so real";

    @Test
    void setUpFiles() throws FileNotFoundException {
        //files for simple
        this.test1 = new File ("test1");
        this.test1.mkdirs();

        this.examplefile1 = new File("test1/examplefile1.txt");
        PrintWriter pw1 = new PrintWriter (examplefile1);
        pw1.print(examplefile1contents);
        pw1.close();

        this.examplefile2 = new File("test1/examplefile2.txt");
        PrintWriter pw2 = new PrintWriter (examplefile2);
        pw2.print(examplefile2contents);
        pw2.close();

        this.examplefile3 = new File("test1/examplefile3.txt");
        PrintWriter pw3 = new PrintWriter (examplefile3);
        pw3.print(examplefile3contents);
        pw3.close();

        //files for complicated
        this.test2 = new File ("test2");
        this.test2.mkdir();
        this.folder1 = new File ("test2/folder1");
        this.folder1.mkdir();
        this.folder2 = new File ("test2/folder2");
        this.folder2.mkdir();

        this.examplefile12 = new File("test2/examplefile12.txt");
        PrintWriter pw12 = new PrintWriter (examplefile12);
        pw12.print(examplefile1contents);
        pw12.close();

        this.examplefile22 = new File("test2/examplefile22.txt");
        PrintWriter pw22 = new PrintWriter (examplefile22);
        pw22.print(examplefile2contents);
        pw22.close();

        this.examplefile32 = new File("test2/examplefile32.txt");
        PrintWriter pw32 = new PrintWriter (examplefile32);
        pw32.print(examplefile3contents);
        pw32.close();
        
        this.examplefile4 = new File("test2/folder1/examplefile4.txt");
        PrintWriter pw4 = new PrintWriter (examplefile4);
        pw4.print(examplefile4contents);
        pw4.close();

        this.examplefile5 = new File("test2/folder1/examplefile5.txt");
        PrintWriter pw5 = new PrintWriter (examplefile5);
        pw5.print(examplefile5contents);
        pw5.close();

        this.examplefile6 = new File("test2/folder2/example6.txt");
        PrintWriter pw6 = new PrintWriter(examplefile6);
        pw6.println(examplefile6contents);
        pw6.close();
    }

    @Test
    void checkBlobs() throws Exception { 
        //checks if blobs are in object folder
        Blob check1 = new Blob("test1/examplefile1.txt");
        assertTrue((new File ("objects/" + check1.getShaString())).exists());
        Blob check2 = new Blob("test1/examplefile2.txt");
        assertTrue((new File ("objects/" + check2.getShaString())).exists());
        Blob check3 = new Blob("test1/examplefile3.txt");
        assertTrue((new File ("objects/" + check3.getShaString())).exists());
        Blob check4 = new Blob("test2/folder1/examplefile4.txt");
        assertTrue((new File ("objects/" + check4.getShaString())).exists());
        Blob check5 = new Blob("test2/folder1/examplefile5.txt");
        assertTrue((new File ("objects/" + check5.getShaString())).exists());
        Blob check6 = new Blob("test2/folder2/example6.txt");
        assertTrue((new File ("objects/" + check6.getShaString())).exists());
    }

    @Test
    void checkTree() throws Exception {
        //test1
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

        //test2
        Tree tree2 = new Tree();
        tree2.addDirectory("test2");

        //check that tree exists
        String shaContents2 = tree2.getSHA1();
        File treeFile2 = new File("objects/" + shaContents2);
        assertTrue(treeFile2.exists());

        //checking that the contents of the tree are correct
        BufferedReader br = new BufferedReader(new FileReader(treeFile2));
        String actualContents2 = "";
        while(br.ready()) {
            actualContents2 += (char) br.read();
        }
        br.close();
        assertEquals(tree2.getContents(), actualContents2);

        //check that subtrees exist
        String subtreeContents1 = "blob: bcee1ae00ebc3cd1079476c4aa510a8e1ad0540b: example6.txt\n";
        assertTrue(new File("objects/" + Utils.getSHA(subtreeContents1)).exists());
        String subtreeContents2 = "blob: ee9d24a50065b43a310b971d0d08667d5dc99c0e: examplefile5.txt\nblob: bbe99d58c301c90f0ea9354275468f3c84de710e: examplefile4.txt\n";
        assertTrue((new File("objects/" + Utils.getSHA(subtreeContents2))).exists());
    }
}
