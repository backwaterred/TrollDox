package ParserTests;

import GML.GMLException;
import GML.GMLNode;
import GML.TextBox;
import TrollLang.TrollParser.FileInput;
import TrollLang.TrollParser.TrollParser;
import org.junit.jupiter.api.*;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class Decisions_ParserTest {

    GMLNode startNode;
    TrollParser parser;

    @BeforeAll
    static void beforeAll() {}
    @BeforeEach
    void before() {
        try {
            startNode = new TextBox(0, "START");
        } catch (GMLException e) {
            e.printStackTrace();
            fail();
        }
    }
    @AfterEach
    void after() {}
    @AfterAll
    static void afterAll() {}

    @Test
    void GLtest() {
        assertTrue(true);
    }

    @Test
    void simpleDecisions() {
        try {
            // "./src/test/resources/DecisionsDecisions.txt"
//            parser = new TrollParser(new FileInput("./src/test/resources/DecisionsDecisions.txt"), startNode);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        LinkedList<GMLNode> cxns = startNode.getConnectedNodes();
        assertEquals(cxns.size(), 1);
        GMLNode currNode = cxns.pop();
        assertEquals(currNode.getId(), "n" + 2);
        cxns = currNode.getConnectedNodes();
        assertEquals(cxns.size(), 2);
        // todo: finish test
        fail("todo");

    }
}
