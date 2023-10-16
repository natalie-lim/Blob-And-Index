import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Formatter;

public class Tree {

    private File currentTree;
    ArrayList<String> t; // list of all the entries of the tree
    private String prevTreeHash;
    private ArrayList<String> deletedAndEdited;;

    public Tree(ArrayList<String> t, String prevTreeHash) {
        this.t = t;
        this.prevTreeHash = prevTreeHash;
        deletedAndEdited = new ArrayList<String>();
    }

    public Tree () {
        this(new ArrayList<String>(), "");
    }

    public void setCurrentTree(File currentTree) {
        this.currentTree = currentTree;
    }

    public String addDirectory(String directoryPath) throws Exception {
        File dir = new File (directoryPath);
        dir.mkdirs();
        if (!dir.exists()) {
            throw new Exception("Directory path does not exist.");
        }
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                if (child.isFile()) {
                    Blob blob = new Blob(child.getAbsolutePath());
                    String shaOfFile = blob.getShaString();
                    add("blob: " + shaOfFile + ": " + child.getName());
                } else {
                    Tree childTree = new Tree();
                    childTree.addDirectory(child.getAbsolutePath());
                    add("tree: " + childTree.getShaString() + ": " + child.getName());
                }
            }
        }
        return getShaString();
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

    public void copyIdx(String prevTreeHash) throws IOException {
        this.prevTreeHash = prevTreeHash;
        File treeFile = new File("objects/" + getShaString()); // actualFile = file you write to
        this.t =new ArrayList<String>();
        treeFile.createNewFile();
        BufferedReader br = new BufferedReader(new FileReader("index"));
        String currentLine = "";
        while (br.ready()) {
            currentLine = br.readLine();
            if (currentLine.contains("*deleted*") || currentLine.contains("*edited*")) {
                deletedAndEdited.add(currentLine.substring(currentLine.indexOf(" ") + 1));
            } 
            t.add(currentLine);
        }
        br.close();
        if (!prevTreeHash.equals("")) {
            t.add("tree: " + prevTreeHash);
        }
        putInObjects();
        treeFile.delete();
        if (deletedAndEdited.size() > 0) {
            goBackAndDeleteFile(deletedAndEdited, getShaString());
        }
    }

    public static String getPrevTreeHash(String currentHash) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("objects/" + currentHash));
        String lastLine = "";
        while (br.ready()) {
            lastLine = br.readLine();
        }
        br.close();
        lastLine = lastLine.substring(lastLine.indexOf(":") + 2);
        return lastLine;
    }

    public void goBackAndDeleteFile(ArrayList<String> deletedAndEdited, String currrentFileName) throws IOException {
        File temp = new File("temp");
        for (int i = 0; i < deletedAndEdited.size(); i ++) {
            BufferedReader br = new BufferedReader(new FileReader ("objects/" + currrentFileName));
            PrintWriter pw = new PrintWriter(temp);
            String line = "";
            String toDeleteOrEdit = deletedAndEdited.get(i);
            boolean isFirstLine = true;
            while (br.ready()) {
                line = br.readLine();
                if (!line.contains(toDeleteOrEdit) || line.contains("*")) {
                    if (isFirstLine) {
                        pw.print(line);
                        isFirstLine = false;
                    } else {
                        pw.print("\n" + line);
                    }
                } else {
                    deletedAndEdited.remove(i);
                }
            }
            br.close();
            pw.close();
        }
        String newFileName = Utils.getSHA(Utils.getFileContents(temp));
        File oldFile = new File("objects/" + currrentFileName);
        oldFile.delete();
        temp.renameTo(new File ("objects/" + newFileName));
        if (deletedAndEdited.size() > 0) {
            String previousTree = getPrevTreeHash(getShaString());
            goBackAndDeleteFile(deletedAndEdited, previousTree);
        }
    }

    public void add(String entry) throws IOException {
        if (!t.contains(entry)) // no duplicates!
        {
            t.add(entry);
        }
        if (currentTree != null && currentTree.exists()) {
            currentTree.delete();
        }
        File treeFile = new File("objects/" + getShaString()); // actualFile = file you write to
        currentTree = treeFile;

        // print entry into the tree
        putInObjects();
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
        currentTree.delete();
        currentTree = new File("objects/" + getShaString());
        PrintWriter pw = new PrintWriter("objects/" + getShaString());
        for (int i = 0; i < t.size(); i++) {
            if (i != t.size()-1) {
                pw.println(t.get(i));
            } else {
                pw.print(t.get(i));
            }
        }
        pw.close();
    }


    // puts tree into the objects folder by taking the hash and stuff
    public void putInObjects() throws IOException {
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < t.size(); i++) {
            if (i != t.size()-1) {
                sb.append("" + t.get(i)+ "\n");
            } else {
                sb.append("" + t.get(i));
            }
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
        this.currentTree = file;
        pw.print(sb);
        pw.close();
    }

    public String getShaString() {
        String toSha = "";
        for (int i = 0; i < t.size(); i++) {
            if (i != t.size()-1) {
                toSha += (t.get(i) + "\n");
            } else {
                toSha += t.get(i);
            }
        }
        return Utils.getSHA (toSha);
    }
    public String getContents() {
        String get = "";
        for (int i = 0; i < t.size(); i++) {
            if (i != t.size()-1) {
                get += (t.get(i) + "\n");
            } else {
                get += t.get(i);
            }
        }
        return get;
    }

}
