package ParserTests;

import Doc.Doc;
import GML.GMLException;
import GML.GMLNode;
import GML.TextBox;
import TrollLang.TrollParser.FileInput;
import TrollLang.TrollParser.ParserInput;
import TrollLang.TrollParser.TrollParser;
import org.junit.jupiter.api.*;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

public class LinearControlFlow_ParserTest {

    TrollParser parser;
    ParserInput source;
    GMLNode rootNode;

    @BeforeAll
    static void beforeAll() {}
    @BeforeEach
    void beforeEach() {
        try {
            rootNode = new TextBox(0, "START");
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
            source = new FileInput(filePath);
//            parser = new TrollParser(source, !!!);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        LinkedList<GMLNode> cxns;
        cxns = rootNode.getConnectedNodes();

        for (int i=1; i<=100; i++) {
            assertEquals(1, cxns.size());

            GMLNode currNode = cxns.pop();
            assertEquals(i, currNode.getId());

            cxns = currNode.getConnectedNodes();
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

