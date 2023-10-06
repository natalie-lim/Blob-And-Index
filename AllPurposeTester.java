import java.io.IOException;

public class AllPurposeTester {
    public static void main (String [] args) throws Exception {
        //linked from test1 dir --> test2 dir
        Index index = new Index();
        index.init();
        Index.addTree("test1");
        Commit commit = new Commit("Author", "commit 1");
        Index.addTree("test2");
        Commit commit2 = new Commit(commit.getSHA1FileContents(), "natalie lim", "commit 2");
    }
}
