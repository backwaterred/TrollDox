package ParserTests;

import GML.GMLException;
import GML.TextBox;
import Graph.GraphNode;
import TrollLang.TrollParser.FileInput;
import TrollLang.TrollParser.TrollParser;
import org.junit.jupiter.api.*;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class Decisions_ParserTest {

    GraphNode startNode;
    TrollParser parser;

    @BeforeAll
    static void beforeAll() {}
    @BeforeEach
    void before() {
        try {
            startNode = new GraphNode(0, new TextBox("START"));
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
            parser = new TrollParser(new FileInput("./src/test/resources/DecisionsDecisions.txt"), startNode);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        LinkedList<GraphNode> cxns = startNode.getOutgoingConnections();
        assertTrue(cxns.size() == 1);
        GraphNode currNode = cxns.pop();
        assertTrue(currNode.getId() == 2);
        cxns = currNode.getOutgoingConnections();
        assertTrue(cxns.size() == 2);
        // todo: finish
        fail();

    }
}
