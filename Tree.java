import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Tree {

    public static void main(String[] args) throws Exception {
        Commit commit = new Commit("hello", "summary");
    }

    ArrayList<String> t; // list of all the entries of the tree

    public Tree() {
        t = new ArrayList<String>();
    }

    // gets the sha part out of a tree entry
    public String shaPart(String entry) {
        int i = entry.indexOf(":") + 2; // index where the sha1 part starts
        String sha1 = entry.substring(i); // sha1 to the end
        if (sha1.indexOf(":") != -1) // if there's another part after the sha1
        {
            int b = sha1.indexOf(":") - 1; // this will be the end of the sha1 (not before the : and the space)
            sha1 = sha1.substring(0, b);
        }

        return sha1;
    }

    // gets the fileName part of the entry; if a tree (no fileName), returns empty
    public String namePart(String entry) {
        int i = entry.indexOf(":") + 2; // index where the sha1 part starts
        String sha1 = entry.substring(i); // sha1 to the end

        if (sha1.indexOf(":") != -1) // if there's another part after the sha1
        {
            i = sha1.indexOf(":") + 2; // where the fileName part starts
            String n = sha1.substring(i); // just the fileName part
            return n;
        }

        return "";
    }

    public void add(String entry) throws IOException {
        if (!t.contains(entry)) // no duplicates!
        {
            t.add(entry);
        }

        File treeFile = new File("TreeIndex"); // actualFile = file you write to
        treeFile.createNewFile();

        // print entry into the tree
        PrintWriter pw = new PrintWriter(treeFile);
        // prints out everything in arrayList to the tree index
        for (int i = 0; i < t.size(); i++) {
            pw.println(t.get(i));
        }
        pw.close();
    }

    public void remove(String str) throws IOException {
        int length = t.size();
        for (int i = 0; i < length; i++) {
            String shPart = shaPart(t.get(i));
            String nPart = namePart(t.get(i));
            if (shPart.equals(str)) {
                // then it is a tree, because that confirms the input was a sha1 value
                t.remove(t.get(i));
            }
            if (nPart.equals(str)) {
                // then it is a blob, because that confirms that the input was a fileName
                t.remove(t.get(i));
            }

        }
        PrintWriter pw = new PrintWriter("TreeIndex");
        for (int i = 0; i < t.size(); i++) {
            pw.println(t.get(i));
        }
        pw.close();

    }

    // puts tree into the objects folder by taking the hash and stuff
    public void putInObjects() throws IOException {
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < t.size() - 1; i++) {
            sb.append("" + t.get(i));
        }

        String sha1 = "";

        // my algorithm for getting the sha1, because yours didn't really work for this
        // format
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            digest.update(sb.toString().getBytes("utf8"));
            sha1 = String.format("%040x", new BigInteger(1, digest.digest()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        File file = new File("objects/" + sha1); // file = file you write to
        if (!file.exists()) {
            file.createNewFile();
        }

        PrintWriter pw = new PrintWriter(file);
        for (int i = 0; i < t.size(); i++) {
            pw.println(t.get(i));
        }
        pw.close();
    }

    public String getSHA1() {
        return "a67a4e6190d11dea06fbd38affc52dcc33cc4564";

    }

}
