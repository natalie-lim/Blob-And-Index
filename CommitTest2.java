import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

public class CommitTest2 {
    private File test1;
    private File examplefile1;
    private File examplefile2;
    private File examplefile3;
    private String examplefile1contents = "the sha of this is ... ?";
    private String examplefile2contents = "zomg wut are u doing. LAWL";
    private String examplefile3contents = "LOL please dont read this.  Good job being thorough tho!";


    @Test
    void TestOneCommit() throws Exception {
        //create directory
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

        //index with two files
        Index idx = new Index();
        idx.init();
        Index.addTree("test1");
    }

}
