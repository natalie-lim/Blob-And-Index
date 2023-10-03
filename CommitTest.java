import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.jupiter.api.Test;

public class CommitTest {
    @Test
    void testCommitMain() throws Exception {
        String Sha1PArent = "radofiaaadsfawe1o";
        String author = "Grady";
        String summary = "this is a test";
        String treeSha = "a67a4e6190d11dea06fbd38affc52dcc33cc4564";
        String date = Utils.getDate();
        String content = treeSha + "\n" + Sha1PArent + "\n\n" + author + "\n" + date + "\n" + summary;
        File commitFile = new File("objects/" + Utils.getSHA(content));
        Commit commit = new Commit(Sha1PArent, author, summary);
        assertTrue("Commit File was not created", commitFile.exists());
        assertEquals("Commit file does not have the correct contents", content, Utils.getFileContents(commitFile));
    }

    @Test
    void testCreateTree() throws Exception {
        Commit commit = new Commit("Author", "summary");

        assertEquals("hello", "a67a4e6190d11dea06fbd38affc52dcc33cc4564", commit.createTree());
    }

    @Test
    void testGetContents() throws Exception {
        Commit commit = new Commit("Author", "summary");
        File file = new File("example1");
        String treeSha = "a67a4e6190d11dea06fbd38affc52dcc33cc4564";
        String date = Utils.getDate();
        String content = treeSha + "\n" + "" + "\n" + "\n" + "Author" + "\n" + date + "\n" + "summary";
        assertEquals("Get contents doesn't work", commit.getContents(),
                content);
    }

    @Test
    void testGetDate() throws Exception {
        Commit commit = new Commit("Author", "summary");
        assertEquals("date doesn't work", Utils.getDate(), commit.getDate());
    }

    @Test
    void testGetSHA() throws Exception {
        Commit commit = new Commit("Author", "summary");
        assertEquals("get sha doesn't work", Utils.getSHA("testing"), commit.getSHA("testing"));
    }
}
