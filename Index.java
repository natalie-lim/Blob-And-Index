import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map.Entry;

public class Index {
    private static HashMap<String, Blob> blobs;
    public Index () {
    }
    //initializes a project by creating an index file and a n obj folder
    public void init() throws IOException {
        blobs = new HashMap<String, Blob>();
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
        blobs.put(fileName, newBlob);
        writeHashMap();
    }
    
    public void removeBlob (String fileName) throws IOException, NoSuchAlgorithmException {
        //String SHAstring = blobs.get(fileName).getShaString();
        blobs.remove(fileName);
        writeHashMap();
    }
    
    public void printMap () {
        for (Entry<String, Blob> mapElement: blobs.entrySet()) {
            String key = mapElement.getKey();
            String SHA1 = mapElement.getValue().getShaString();
            System.out.println (key + ": " + SHA1);
        }
    }
    
    private static void writeHashMap () throws FileNotFoundException, NoSuchAlgorithmException {
        PrintWriter pw = new PrintWriter ("index");
        for (Entry<String, Blob> mapElement: blobs.entrySet()) {
            String key = mapElement.getKey();
            String SHA1 = mapElement.getValue().getShaString();
            pw.println (key + ": " + SHA1);
        }
        pw.close();
    }
}
