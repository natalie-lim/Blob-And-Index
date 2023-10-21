import java.io.File;
import java.io.IOException;

public class AllPurposeTester {
    public static void main (String [] args) throws Exception {
        Index index = new Index();
        index.init();
        Index.addBlob("example.txt");
        Index.addBlob("words.txt");
        Commit commit = new Commit("Author", "commit 1");
        index.init();
        Index.addBlob("example1");
        Commit commit2 = new Commit(commit.getSHA1FileContents(), "natalie lim", "commit 2");
        index.init();
        Index.addTree("test1");
        Index.addBlob("hello");
        Index.deleteFile("example1");
        Commit commit3 = new Commit (commit2.getSHA1FileContents(), "bob", "commit 3");
        index.init();
        Index.addTree("commit1");
        Index.addTree("commit2");
        Commit commit4 = new Commit(commit3.getSHA1FileContents(), "bella", "commit 4");
        // index.init();
        // Index.deleteFile("example.txt");
        // Index.editFiles("test1");
        // Index.addTree("commit4");
        // Commit commit5 = new Commit(commit4.getSHA1FileContents(), "bartholemew", "commit 5");
    }
}
