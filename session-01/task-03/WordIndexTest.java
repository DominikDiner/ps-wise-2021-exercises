import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class WordIndexTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void test_lineIndexToPage_startOfPage() {
        assertThat(WordIndex.lineIndexToPage(1), equalTo(1));
    }

    @Test
    public void test_lineIndexToPage_endOfPage() {
        assertThat(WordIndex.lineIndexToPage(45), equalTo(1));
    }

    @Test
    public void test_lineIndexToPage_secondPage() {
        assertThat(WordIndex.lineIndexToPage(46), equalTo(2));
    }

    @Test
    public void test_joinCollection() {
        assertThat(WordIndex.joinCollection(List.of(1, 2, 3)), equalTo("1, 2, 3"));
    }

    @Test
    public void test_main() throws IOException {
        String file = "This is one line\nThis is the second line" + "\n".repeat(44) + "This is the first line on the second page";

        File testFile = folder.newFile();
        Files.writeString(testFile.toPath(), file, Charset.defaultCharset());

        WordIndex.main(new String[]{testFile.getAbsolutePath()});

        String consoleOutPut = outContent.toString();
        assertEquals("This 1, 2\n"
                + "first 2\n"
                + "is 1, 2\n"
                + "line 1, 2\n"
                + "on 2\n"
                + "one 1\n"
                + "page 2\n"
                + "second 1, 2\n"
                + "the 1, 2\n", consoleOutPut);
    }
}
