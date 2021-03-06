package Tests;

import TrollLang.TrollParser.FileInput;
import org.junit.jupiter.api.*;

import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

public class FileInputTest {

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
    }
    @AfterAll
    static void afterAll() {
    }

    @Test
    public void greenLightTest() {
        assertTrue(true);
    }

    @Test
    public void testEvenLogEvents() throws IOException {
        for (int i=2; i<=100; i+=2) {
            assertEquals("LOGEVENT \"Line: " +i+ "\"",
                    fileInput.getLine(i));
        }

    }

    @Test
    public void testOddLogEvents() throws IOException {
        for (int i=1; i<=100; i+=2) {
            assertEquals("LOGEVENT \"Line: " +i+ "\"",
                    fileInput.getLine(i));
        }

    }

    @Test
    void testAllLEs() throws IOException {
        for (int i=1; i<=100; i++) {
            assertEquals("LOGEVENT \"Line: " +i+ "\"",
                    fileInput.getLine(i));
        }
    }

    @Test
    void testNextLines() throws IOException {
        // Test all valid lines
        for (int i=0; i<100; i++) {
            assertEquals(fileInput.getNextValidLineNumber(i), i+1);
        }
    }

    @Test
    void testLastLineTriggersEOFWarning() {
        // Starting at last line of file should warn
        assertThrows(IOException.class,
                () -> fileInput.getNextValidLineNumber(100));
    }

    @Test
    public void test101CallsThrowsException() throws IOException {
        assertThrows(IOException.class,
                () -> fileInput.getLine(101));
    }

    @Test
    void test2EmptyLine() throws IOException {
        fileInput = new FileInput("./src/test/resources/OneHundredLogEvents_withCommentsAndEmptyLines.txt");
        assertEquals(124, fileInput.getNextValidLineNumber(120)); // 120 immediately preceeds 2 empty lines and a comment (lines 121, 122, & 123).
    }

}
