import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilterReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;
import java.util.Formatter;

import javax.print.event.PrintEvent;

public class Commit {
    private String SHA1Parent;
    private String author;
    private String summary;
    private String date;
    Tree tree;
    private String SHA1tree;
    private String SHA1NextCommit;
    private String fileContents;
    private String SHA1FileContents;
    File commitFile;

    public Commit(String SHA1Parent, String author, String summary) throws Exception {
        this.SHA1Parent = SHA1Parent;
        this.author = author;
        this.summary = summary;
        this.SHA1NextCommit = "";
        this.date = getDate();
        this.tree = new Tree();

        tree.copyIdx(SHA1tree);
        PrintWriter writer = new PrintWriter("index");
        writer.print("");
        writer.close();

        if (!SHA1Parent.equals("")) {
            tree.add("tree: " + getTreeHash(SHA1Parent));
            this.SHA1Parent = SHA1Parent;
        } else {
            this.SHA1Parent = "";
        }
        this.SHA1tree = tree.getShaString();
        this.fileContents = getContents();
        this.SHA1FileContents = getSHA(fileContents);
        if (!SHA1Parent.equals("")) {
            writePreviousNext();
        }
        File commitFile = new File("objects/" + SHA1FileContents);
        writeToFile(commitFile, fileContents);

    }

    public Commit (String author, String summary) throws Exception {
        this("", author, summary);
    }

    public Tree getTree() {
        return tree;
    }

    public String getSHA1Parent () {
        return SHA1Parent;
    }

    public String getSHA1NextCommit () throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("objects/" + SHA1FileContents));
        br.readLine();
        br.readLine();
        SHA1NextCommit = br.readLine();
        if (SHA1NextCommit.equals("null")) {
            SHA1NextCommit = "";
        }
        br.close();
        return (SHA1NextCommit);
    }

    public Commit getCommit (String ShaFile) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader (ShaFile));
        br.readLine();
        String previous = br.readLine();
        String author = br.readLine();
        br.readLine();
        String summary = br.readLine();
        br.close();
        if (!previous.equals("")) {
            return new Commit(previous, author, summary);
        } else {
            return new Commit (author, summary);
        }
        
    }

    public void writePreviousNext() throws Exception {
        File previousTree = new File("objects/" + SHA1Parent);
        File temp = new File("temp");
        PrintWriter pw = new PrintWriter(temp);
        BufferedReader br = new BufferedReader(new FileReader(previousTree));
        String line = br.readLine();
        pw.print(line);
        line = br.readLine();
        line = br.readLine();
        pw.println("\n\n" + SHA1FileContents);
        line = br.readLine();
        pw.println(line);
        line = br.readLine();
        pw.println(line);
        line = br.readLine();
        pw.print(line);
        pw.close();
        br.close();
        temp.renameTo(previousTree);
    }

    public void setNext(String next) throws Exception {
        SHA1NextCommit = next;
        writeToFile(commitFile, getContents());
    }

    public static String getTreeHash(String SHACommit) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("objects/" + SHACommit));
        String hash = "";
        if (br.ready()) {
            hash = br.readLine();
        }
        br.close();
        return hash;
    }

    public String getDate() {
        // Date date = new Date();
        // return date.toString();
        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int year = localDate.getYear();
        int month = localDate.getMonthValue();
        String monthName = Month.of(month).name();
        int day = localDate.getDayOfMonth();
        return (monthName + " " + day + ", " + year);
    }

    public String getSHA1FileContents () {
        return SHA1FileContents;
    }

    public static String getSHA(String contents) {
        String sha1 = "";
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(contents.getBytes("UTF-8"));
            sha1 = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sha1;
    }

    // Used for sha1
    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    public String createTree() throws Exception {
        this.tree = new Tree();
        tree.putInObjects();
        return tree.getShaString();
    }

    public String getContents() {
        return (SHA1tree + "\n" + SHA1Parent + "\n" + SHA1NextCommit + "\n" + author + "\n" + date + "\n" + summary);
    }

    public void writeToFile(File file, String contents) throws Exception {
        FileWriter fw = new FileWriter(file);
        fw.write(contents);
        fw.close();
    }

}