package ParserTests;

import GML.GMLException;
import GML.TextBox;
import Graph.GraphNode;
import TrollLang.TrollParser.FileInput;
import TrollLang.TrollParser.TrollParser;
import org.junit.jupiter.api.*;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

public class LinearControlFlow_ParserTest {

    TrollParser parser;
    GraphNode rootNode;

    @BeforeAll
    static void beforeAll() {}
    @BeforeEach
    void beforeEach() {
        try {
            rootNode = new GraphNode(0, new TextBox("START"));
        } catch (GMLException e) {
            e.printStackTrace();
            fail();
        }

    }
    @AfterEach
    void afterEach() {}
    @AfterAll
    static void afterAll() {}

    @Test
    void greenlightTest() {
        assertTrue(true);
    }

    void testLinearFlowFile(String filePath) {

        try {
            parser = new TrollParser(new FileInput(filePath), rootNode);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        LinkedList<GraphNode> cxns;
        cxns = rootNode.getOutgoingConnections();

        for (int i=1; i<=100; i++) {
            assertTrue(cxns.size() == 1);

            GraphNode currNode = cxns.pop();
            assertTrue(currNode.getId() == i);

            cxns = currNode.getOutgoingConnections();
        }
    }

    @Test
    void test100LEs() {
        // "./src/test/resources/OneHundredLogEvents.txt"
        testLinearFlowFile("./src/test/resources/OneHundredLogEvents.txt");
    }

    @Test
    void test100LEs_withCommentsAndEmptyLines() {
        // "./src/test/resources/OneHundredLogEvents_withCommentsAndEmptyLines.txt"
        testLinearFlowFile("./src/test/resources/OneHundredLogEvents_withCommentsAndEmptyLines.txt");
    }
}

