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

import org.junit.After;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CommitTestJunit {
    @AfterAll
    static void deleteAll() {
        File idx = new File("index");
        if (idx.exists()) {
            idx.delete();
        }
        File objects = new File ("objects");
        if (objects.exists()) {
            for (File f : objects.listFiles()) {
            f.delete();
            }
            objects.delete();
        }
    }

    @Test
    void setUp() throws Exception {
        File file = new File("objects");
        file.mkdirs();
        file.delete();
    }
    @Test
    void TestOneCommit() throws Exception {
        Index idx = new Index();
        idx.init();
        Index.addTree("test1");

        Commit commit1 = new Commit("natalie lim", "please work");

        //checking tree
        assertEquals(new File ("objects/7d49e5a5157571e704505eea5806fba0be59d6b7"), new File ("objects/" + commit1.getTree().getShaString()));

        assertEquals("", commit1.getSHA1Parent());
        assertEquals("", commit1.getSHA1NextCommit());
    }

    @Test
    void setUpTwoCommits() throws Exception {
        Index idx1 = new Index();
        idx1.init();
        Index.addTree("test1");
        Commit commit1 = new Commit("natalie lim", "commit 1");
        Index.addTree("test2");
        Commit commit2 = new Commit(commit1.getSHA1FileContents(), "hello", "commit 2");

        //testing trees
        assertEquals(new File ("objects/7d49e5a5157571e704505eea5806fba0be59d6b7"), new File ("objects/" + commit1.getTree().getShaString()));
        assertEquals(new File ("objects/631e522497653b839ee7005158ef20e54b6a7d07"), new File ("objects/" + commit2.getTree().getShaString()));

        //testing previous
        assertEquals("", commit1.getSHA1Parent());
        assertEquals(commit1.getSHA1FileContents(), commit2.getSHA1Parent());

        //testing next
        assertEquals(commit1.getSHA1NextCommit(), commit2.getSHA1FileContents());
        assertEquals("", commit2.getSHA1NextCommit());

        //verifying tree contents are correct
        String supposed;
        //commit 1
        supposed = "tree: b92558ffb9dc97ddbd26f24a9e3658ba7f82e030: test1";
        String actual = "";
        BufferedReader br1 = new BufferedReader(new FileReader("objects/7d49e5a5157571e704505eea5806fba0be59d6b7"));
        while (br1.ready()) {
            actual += br1.readLine();
        }
        assertEquals(supposed, actual);

        //commit 2
        supposed = ("tree: f7977a96041c46bf44718aa01d0d07559b94b3df: test2\ntree: b92558ffb9dc97ddbd26f24a9e3658ba7f82e030: test1\ntree: 7d49e5a5157571e704505eea5806fba0be59d6b7");
        actual = "";
        BufferedReader br2 = new BufferedReader(new FileReader("objects/631e522497653b839ee7005158ef20e54b6a7d07"));
        while (br2.ready()) {
            actual += (br2.readLine() + "\n");
        }
        actual = actual.substring(0, actual.length()-1);
        br2.close();
        assertEquals(supposed, actual);
    }

    @Test
    void setUpFourCommits() throws Exception {
        Index idx1 = new Index();
        idx1.init();
        Index.addTree("commit1");
        Commit commit1 = new Commit("natalie lim", "commit 1");
        Index.addTree("commit2");
        Commit commit2 = new Commit(commit1.getSHA1FileContents(), "hello", "commit 2");
        Index.addTree("commit3");
        Commit commit3 = new Commit(commit2.getSHA1FileContents(), "hello", "commit 3");
        Index.addTree("commit4");
        Commit commit4 = new Commit(commit3.getSHA1FileContents(), "hello", "commit 4");

        //testing trees
        assertEquals(new File ("objects/547ab1fd02ad3fa4325415b59cf34d9be2755147"), new File ("objects/" + commit1.getTree().getShaString()));
        assertEquals(new File ("objects/cb9a61d23eef9edc557a99fa7f750b9c28fc4dbd"), new File ("objects/" + commit2.getTree().getShaString()));
        assertEquals(new File ("objects/885a61a62811b85cd96c7792b838c4068fa5760e"), new File ("objects/" + commit3.getTree().getShaString()));
        assertEquals(new File ("objects/1a4c2f45fd81d15e2b241587f2c57378f114359b"), new File ("objects/" + commit4.getTree().getShaString()));

        //testing previous
        assertEquals("", commit1.getSHA1Parent());
        assertEquals(commit1.getSHA1FileContents(), commit2.getSHA1Parent());
        assertEquals(commit2.getSHA1FileContents(), commit3.getSHA1Parent());
        assertEquals(commit3.getSHA1FileContents(), commit4.getSHA1Parent());

        //testing next
        assertEquals(commit1.getSHA1NextCommit(), commit2.getSHA1FileContents());
        assertEquals(commit2.getSHA1NextCommit(), commit3.getSHA1FileContents());
        assertEquals(commit3.getSHA1NextCommit(), commit4.getSHA1FileContents());
        assertEquals("", commit4.getSHA1NextCommit());

        //verifying tree contents are correct
        String supposed;
        //commit 1
        supposed = "tree: 2b2afc5e313f05755f33422acfe48cb81f2ba38c: commit1";
        String actual = "";
        BufferedReader br1 = new BufferedReader(new FileReader("objects/547ab1fd02ad3fa4325415b59cf34d9be2755147"));
        while (br1.ready()) {
            actual += br1.readLine();
        }
        assertEquals(supposed, actual);

        //commit 2
        supposed = ("tree: 65db59ab1ea650128b2e384f5eeea5e5a8bbd5fd: commit2\ntree: 2b2afc5e313f05755f33422acfe48cb81f2ba38c: commit1\ntree: 547ab1fd02ad3fa4325415b59cf34d9be2755147");
        actual = "";
        BufferedReader br2 = new BufferedReader(new FileReader("objects/cb9a61d23eef9edc557a99fa7f750b9c28fc4dbd"));
        while (br2.ready()) {
            actual += (br2.readLine() + "\n");
        }
        actual = actual.substring(0, actual.length()-1);
        br2.close();
        assertEquals(supposed, actual);

        //commit 3
        supposed = ("tree: 0dc94a192f992830c841bd4ae72633ccbf1d96a3: commit3\n" + //
                "tree: 65db59ab1ea650128b2e384f5eeea5e5a8bbd5fd: commit2\n" + //
                "tree: 2b2afc5e313f05755f33422acfe48cb81f2ba38c: commit1\n" + //
                "tree: cb9a61d23eef9edc557a99fa7f750b9c28fc4dbd");
        actual = "";
        BufferedReader br3 = new BufferedReader(new FileReader("objects/885a61a62811b85cd96c7792b838c4068fa5760e"));
        while (br3.ready()) {
            actual += (br3.readLine() + "\n");
        }
        actual = actual.substring(0, actual.length()-1);
        br3.close();
        assertEquals(supposed, actual);

        //commit 4
        supposed = ("tree: cb9c1d8512d88b550dd38910edf37c491e8bb473: commit4\ntree: 0dc94a192f992830c841bd4ae72633ccbf1d96a3: commit3\ntree: 65db59ab1ea650128b2e384f5eeea5e5a8bbd5fd: commit2\ntree: 2b2afc5e313f05755f33422acfe48cb81f2ba38c: commit1\ntree: 885a61a62811b85cd96c7792b838c4068fa5760e");
        actual = "";
        BufferedReader br4 = new BufferedReader(new FileReader("objects/1a4c2f45fd81d15e2b241587f2c57378f114359b"));
        while (br4.ready()) {
            actual += (br4.readLine() + "\n");
        }
        actual = actual.substring(0, actual.length()-1);
        br4.close();
        assertEquals(supposed, actual);
    }

}
