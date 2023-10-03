import java.io.IOException;

public class AllPurposeTester {
    public static void main (String [] args) throws Exception {
        Index idx = new Index();
        idx.init();
        Index.addTree("test1");
        Index.addBlob("test1/examplefile1.txt");
        Commit commit = new Commit("Author", "Summary");
    }
}
