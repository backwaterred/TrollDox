package ParserInputTests;

import TrollLang.TrollParser.FileInput;
import TrollLang.TrollParser.ParserInput;
import org.junit.jupiter.api.*;

import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

public class BasicFileInputTest {

    FileInput fileInput;

    @BeforeAll
    static void beforeAll() {}
    @BeforeEach
    void beforeEach() {
        try {
            fileInput = new FileInput("./src/test/resources/OneHundredLogEvents.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @AfterEach
    void afterEach() {
        try {
            fileInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @AfterAll
    static void afterAll() {
    }

    @Test
    public void greenLightTest() {
        assertTrue(true);
    }

    @Test
    public void test100LogEvents() throws IOException {
        for (int i=1; i<=100; i++) {
            assertTrue(fileInput.hasNextLine());
            fileInput.getNextLine();
        }

    }
    @Test
    public void test101CallsThrowsException() throws IOException {
        test100LogEvents();
        assertFalse(fileInput.hasNextLine());
        assertThrows(IOException.class,
                () -> fileInput.getNextLine());
    }

    @Test
    public void testElementsReturnedByLE() throws IOException {
        for (int i=1; i<=100; i++) {
            String nl = fileInput.getNextLine();
            assertEquals("LOGEVENT \"Line: " + i + "\"", nl);
        }
    }

    @Test
    public void testEmptyLineAndComments() {
        ParserInput commentfulInput = null;
        try {
            commentfulInput =
                    new FileInput("./src/test/resources/OneHundredLogEvents_withCommentsAndEmptyLines.txt");
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
        int numValidLinesRead = 0;
        while (commentfulInput.hasNextLine()) {
            try {
                commentfulInput.getNextLine();;
                numValidLinesRead++;
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }
        assertEquals(100, numValidLinesRead);
    }

}
