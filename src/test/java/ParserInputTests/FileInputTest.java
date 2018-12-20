package ParserInputTests;

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
    public void test101CallsThrowsException() throws IOException {
        assertThrows(IOException.class,
                () -> fileInput.getLine(101));
    }

}
