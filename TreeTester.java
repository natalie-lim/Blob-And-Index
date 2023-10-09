import java.io.File;
import java.io.PrintWriter;

public class TreeTester {
    public static void main (String [] args) throws Exception {
        Tree tree = new Tree();
        System.out.println (tree.addDirectory("test1"));
        PrintWriter pw = new PrintWriter(new File("tester"));
        pw.print("ehll");
        pw.close();
    }
}
