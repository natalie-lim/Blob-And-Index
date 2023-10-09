import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map.Entry;

public class Index {
    public static void main (String [] args) throws Exception {
        Index index = new Index();
        index.init();
        Index.addTree("test1");
        Index.addBlob("test1/examplefile1.txt");
    }

    //first string is the fileName, second is either tree/blob: shaContents
    private static HashMap<String, String> entries;
    public Index () {
    }
    //initializes a project by creating an index file and a n obj folder
    public void init() throws IOException {
        entries = new HashMap<String, String>();
        //creating the objects
        File theDir = new File("objects");
        if (!theDir.exists()){
            theDir.mkdirs();
        }
        //creating the index file
        File file = new File("index");
        file.createNewFile();
    }

    public static void addBlob(String fileName) throws NoSuchAlgorithmException, IOException {
        Blob newBlob = new Blob (fileName);
        String sha = newBlob.getShaString();
        entries.put(fileName, "blob: " + sha);
        writeHashMap();
    }

    public static void addTree (String dirName) throws Exception {
        Tree newTree = new Tree();
        File dir = new File(dirName);
        dir.mkdir();
        newTree.addDirectory(dir.getAbsolutePath());
        String sha = newTree.getShaString();
        entries.put(dirName, "tree: " + sha);
        writeHashMap();
    }
    
    public void removeBlob (String fileName) throws IOException, NoSuchAlgorithmException {
        //String SHAstring = blobs.get(fileName).getShaString();
        entries.remove(fileName);
        writeHashMap();
    }
    
    public void deleteFile (String fileToDelete) {
        entries.put("*deleted* "+ fileToDelete, "");
    }

    public void editFiles (String fileToEdit) {
        entries.put("*edited* "+ fileToEdit, "");
    }
    
    private static void writeHashMap () throws FileNotFoundException, NoSuchAlgorithmException {
        PrintWriter pw = new PrintWriter ("index");
        for (Entry<String, String> mapElement: entries.entrySet()) {
            String fileName = mapElement.getKey();
            String typeAndSha = mapElement.getValue();
            if (!typeAndSha.equals("")) {
                pw.println (typeAndSha + ": " + fileName);
            } else {
                pw.println(fileName);
            }
        }
        pw.close();
    }
}
