package DocumentTests;

import Doc.Doc;
import Doc.Page;
import GML.GMLNode;
import TrollLang.TrollParser.FileInput;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class PageTest {

    Doc doc;

    private void init_doc(String filePath) {
        try {
            doc = new Doc(new FileInput(filePath));
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @BeforeAll
    static void beforeAll() {}
    @BeforeEach
    void before() {}
    @AfterEach
    void after() {}
    @AfterAll
    static void afterAll() {}

    @Test
    void GLtest() {
        assertTrue(true);
    }
}
