import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class Tester {
    public static void main (String [] args) throws FileNotFoundException, NoSuchAlgorithmException, IOException {
        Index index = new Index ();
        index.init();
        Index.addBlob("example1");
        Index.addBlob("example2");
        Index.addBlob("example3");
        Index.addBlob("example.txt");
        Index.addBlob("example.txt");
        Index.removeBlob("example1");

        Tree treeTest = new Tree ();
        treeTest.add ("blob : 5d48ce5c0d685f302b07ff6b9e2f5bd5a5a80370 : example1");
        treeTest.add ("blob : 40760667e85b087f664366da3fa4e1809e21f2de : example2");
        treeTest.add ("blob : 5d48ce5c0d685f302b07ff6b9e2f5bd5a5a80370 : example1"); //test duplicates, works
        //treeTest.remove ("example1"); //remove works with blob
        treeTest.putInObjects ();
        
        //attempt to add and remove a tree
        Tree tTest = new Tree ();
        tTest.add ("tree: 2a75a7448ce60b034b2e43480229e1b628e52af3");
        tTest.remove ("2a75a7448ce60b034b2e43480229e1b628e52af3");
        //add/remove tree works
        tTest.putInObjects();
        //nice
        
    }
}
