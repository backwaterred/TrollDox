package DocumentTests;

import Doc.Doc;
import Doc.Page;
import GML.GMLNode;
import TrollLang.TrollParser.FileInput;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

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

    @Test
    void test100LEs() {
        init_doc("./src/test/resources/OneHundredLogEvents.txt");

        for (int i=1; i<=doc.getTotalPages(); i++) {
            for (GMLNode node : doc.getPage(i).getNodes()) {
                assertEquals(0.0, node.getX());
            }
        }
    }

    @Test
    void test100LEsWCommentsAndMTLines() {
        init_doc("./src/test/resources/OneHundredLogEvents_withCommentsAndEmptyLines.txt");

        for (int i=1; i<=doc.getTotalPages(); i++) {
            for (GMLNode node : doc.getPage(i).getNodes()) {
                assertEquals(0.0, node.getX());
            }
        }
    }

    @Test
    void testDecisionsDecisons() {
        init_doc("./src/test/resources/DecisionsDecisions.txt");

        for (int i=1; i<=doc.getTotalPages(); i++) {
            for (GMLNode node : doc.getPage(i).getNodes()) {
                node.render();
                String id = node.getId();
                assertEquals( (id.equalsIgnoreCase("n6") || id.equalsIgnoreCase("n7") || id.equalsIgnoreCase("n8"))? 360.0 : 0.0,
                        node.getX());
            }
        }
    }
}
