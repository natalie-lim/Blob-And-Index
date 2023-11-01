import static org.junit.Assert.assertEquals;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import org.junit.jupiter.api.AfterAll;
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
        
        Index.deleteFile("test1/examplefile1.txt");

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

    @Test
    void testEditDelete() throws Exception {
        Index index = new Index();
        index.init();
        Index.addBlob("example.txt");
        Index.addBlob("words.txt");
        Commit commit = new Commit("Author", "commit 1");
        index.init();
        Index.addBlob("example1");
        Commit commit2 = new Commit(commit.getSHA1FileContents(), "natalie lim", "commit 2");
        BufferedReader br1 = new BufferedReader(new FileReader("objects/" + commit2.getSHA1FileContents()));
        String commit2Tree = "";
        if (br1.ready()) {
            commit2Tree = br1.readLine();
        }
        br1.close();
        
        BufferedReader br1Tree = new BufferedReader(new FileReader("objects/" + commit2Tree));
        String commit2TreeContents = "";
        while (br1Tree.ready()) {
            commit2TreeContents+= br1Tree.readLine();
            if (br1Tree.ready()) {
                commit2TreeContents+="\n";
            }
        }
        br1Tree.close();
        assertEquals("tree of the second commit matches what it should be", commit2TreeContents, "blob: 5d48ce5c0d685f302b07ff6b9e2f5bd5a5a80370: example1\ntree: f6a48463ee46ae5b896b96a219e61e5d9ac1ac21");

        //testing 3 commit
        index.init();
        Index.addTree("test1");
        Index.addBlob("hello");
        Index.deleteFile("example1");
        Commit commit3 = new Commit (commit2.getSHA1FileContents(), "bob", "commit 3");
        BufferedReader br2 = new BufferedReader(new FileReader("objects/" + commit3.getSHA1FileContents()));
        String commit3Tree = "";
        if (br2.ready()) {
            commit3Tree = br2.readLine();
        }
        br2.close();
        
        BufferedReader br2Tree = new BufferedReader(new FileReader("objects/" + commit3Tree));
        String commit3TreeContents = "";
        while (br2Tree.ready()) {
            commit3TreeContents+= br2Tree.readLine();
            if (br2Tree.ready()) {
                commit3TreeContents+="\n";
            }
        }
        br2Tree.close();
        assertEquals("tree of te third commit is correct", commit3TreeContents, "tree: f6a48463ee46ae5b896b96a219e61e5d9ac1ac21\n" + //
                "blob: 6e002e51fc2e0609f91f90e2f45aee734a93421b: hello\n" + //
                "tree: b92558ffb9dc97ddbd26f24a9e3658ba7f82e030: test1");

        //testing 4 commit
        index.init();
        Index.addTree("commit1");
        Index.addTree("commit2");
        Commit commit4 = new Commit(commit3.getSHA1FileContents(), "bella", "commit 4");
        BufferedReader br3 = new BufferedReader(new FileReader("objects/" + commit4.getSHA1FileContents()));
        String commit4Tree = "";
        if (br3.ready()) {
            commit4Tree = br3.readLine();
        }
        br3.close();
        
        BufferedReader br3Tree = new BufferedReader(new FileReader("objects/" + commit4Tree));
        String commit4TreeContents = "";
        while (br3Tree.ready()) {
            commit4TreeContents+= br3Tree.readLine();
            if (br3Tree.ready()) {
                commit4TreeContents+="\n";
            }
        }
        br3Tree.close();
        assertEquals("tree of the fourth commit is correct", commit4TreeContents, "tree: 65db59ab1ea650128b2e384f5eeea5e5a8bbd5fd: commit2\n" + //
            "tree: 2b2afc5e313f05755f33422acfe48cb81f2ba38c: commit1\n" + //
            "tree: 852ed246cb6cd1f81152528a747731f5cbe9f63d");

        //testing fifth commit
        index.init();
        Index.deleteFile("example.txt");
        Index.editFiles("test1");
        Index.addTree("commit4");
        Commit commit5 = new Commit(commit4.getSHA1FileContents(), "bartholemew", "commit 5");
        BufferedReader br4 = new BufferedReader(new FileReader("objects/" + commit5.getSHA1FileContents()));
        String commit5Tree = "";
        if (br4.ready()) {
            commit5Tree = br4.readLine();
        }
        br4.close();
        
        BufferedReader br4Tree = new BufferedReader(new FileReader("objects/" + commit5Tree));
        String commit5TreeContents = "";
        while (br4Tree.ready()) {
            commit5TreeContents+= br4Tree.readLine();
            if (br4Tree.ready()) {
                commit5TreeContents+="\n";
            }
        }
        br4Tree.close();
        assertEquals("commit 5 contents are correct", commit5TreeContents, "tree: 65db59ab1ea650128b2e384f5eeea5e5a8bbd5fd: commit2\n" + //
                "tree: 2b2afc5e313f05755f33422acfe48cb81f2ba38c: commit1\n" + //
                "blob: 6e002e51fc2e0609f91f90e2f45aee734a93421b: hello\n" + //
                "tree: cb9c1d8512d88b550dd38910edf37c491e8bb473: commit4\n" + //
                "blob: ca0bfef0f74cb9e5f2da4245c989b065228d7243: words.txt");
    }

}
