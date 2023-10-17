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
        Index.editFiles("example.txt");
        Commit commit2 = new Commit(commit.getSHA1FileContents(), "natalie lim", "commit 2");
        
    }
}
