package ParserTests;

import Doc.Doc;
import GML.GMLException;
import GML.GMLNode;
import GML.TextBox;
import TrollLang.AngryTrollException;
import TrollLang.TrollParser.FileInput;
import TrollLang.TrollParser.ParserInput;
import TrollLang.TrollParser.TrollParser;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTests {

    Doc doc;

    @BeforeAll
    static void beforeAll() {}
    @BeforeEach
    void beforeEach() {}
    @AfterEach
    void afterEach() {}
    @AfterAll
    static void afterAll() {}


    private void init_doc(String filePath) {
        try {
            doc = new Doc(new FileInput(filePath));
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    void greenlightTest() {
        assertTrue(true);
    }

    @Test
    void test100LEs() {
        init_doc("./src/test/resources/OneHundredLogEvents.txt");
        assertEquals(1, doc.getRoots().size());

        HashSet<Integer> nodes = new HashSet<>();
        for (int i=1; i<=doc.getTotalPages(); i++) {
            for (GMLNode node : doc.getPage(i).getNodes()) {
                nodes.add(Integer.parseInt(node.getId().substring(1)));
            }
        }
        for (int i=1; i<=100; i++) {
            assertTrue(nodes.contains(new Integer(i)));
        }
        assertTrue(nodes.contains(new Integer(TrollParser.START_NODE_ID)));
        assertTrue(nodes.contains(new Integer(TrollParser.END_NODE_ID)));
    }

    @Test
    void test100LEsWithCommentsAndEmptyLines() {
        init_doc("./src/test/resources/OneHundredLogEvents_withCommentsAndEmptyLines.txt");

        HashSet<Integer> nodes = new HashSet<>();
        for (int i=1; i<=doc.getTotalPages(); i++) {
            for (GMLNode node : doc.getPage(i).getNodes()) {
                nodes.add(Integer.parseInt(node.getId().substring(1)));
            }
        }
        assertEquals(100 + 2, nodes.size());
        assertTrue(nodes.contains(new Integer(TrollParser.START_NODE_ID)));
        assertTrue(nodes.contains(new Integer(TrollParser.END_NODE_ID)));

    }

    @Test
    void testDecisionsDecisions() {
        init_doc("./src/test/resources/DecisionsDecisions.txt");

        HashSet<Integer> nodes = new HashSet<>();
        for (int i=1; i<=doc.getTotalPages(); i++) {
            for (GMLNode node : doc.getPage(i).getNodes()) {
                nodes.add(Integer.parseInt(node.getId().substring(1)));
            }
        }
        assertEquals(8 + 2, nodes.size());
        assertTrue(nodes.contains(new Integer(TrollParser.START_NODE_ID)));
        assertTrue(nodes.contains(new Integer(TrollParser.END_NODE_ID)));

    }
}

