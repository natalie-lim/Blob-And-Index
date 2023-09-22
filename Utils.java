import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;
import java.util.Formatter;

public class Utils {
    public static String getSHA(String fileContents) {
        String sha1 = "";
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(fileContents.getBytes("UTF-8"));
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

    public static String getDate() {
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

    public static String getFileContents(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file.getPath()));
        StringBuilder contents = new StringBuilder();
        while (reader.ready()) {
            contents.append((char) reader.read());
        }
        reader.close();
        return contents.toString();
    }
}
