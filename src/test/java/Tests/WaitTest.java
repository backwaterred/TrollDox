package Tests;

import FlowGraph.AbstractFlowGraphElement;
import FlowGraph.FlowGraph;
import TrollLang.AngryTrollException;
import TrollLang.TrollParam;
import TrollLang.TrollParser.FileInput;
import TrollLang.TrollParser.TrollParser;
import TrollLang.TrollSpeak;
import org.junit.jupiter.api.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class WaitTest {

    TrollParser parser;
    FlowGraph g;

    private String TEST_PARAM = TrollParam.makeParamsPretty("Application:Cust_System.Console_Parameter:Console_Parameters.AO:_hidden_Digester_Cell_Temperature.Value");

    @BeforeAll
    static void beforeAll() {}
    @BeforeEach
    void before() throws IOException, AngryTrollException {
        parser = new TrollParser(new FileInput("./src/test/resources/Wait&WaitFors.txt"), "Arithmetic Test Graph", "0000-00-00");
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
    void testSimpleWait() throws AngryTrollException{
        g = parser.parse();
        testStatement(TrollSpeak.WAIT, TEST_PARAM, 1);
        testStatement(TrollSpeak.WAIT, "\"2\"", 2);
        testStatement(TrollSpeak.WAIT, "3", 3);
    }

    @Test
    void testConnections() throws AngryTrollException {
        g = parser.parse();

        assertTrue(g.hasConnection(4,9));
        assertTrue(g.hasConnection(4,5));
        assertTrue(g.hasConnection(5,9));
        assertTrue(g.hasConnection(5,6));
        assertTrue(g.hasConnection(6,9));
        assertTrue(g.hasConnection(6,7));
        assertTrue(g.hasConnection(7,9));
        assertTrue(g.hasConnection(7,8));
        assertTrue(g.hasConnection(8,9));
    }

    private void testStatement(TrollSpeak type, String expectedText, int line) {

        AbstractFlowGraphElement ag = (AbstractFlowGraphElement) g.getNode(line);
        assertEquals(type.getMsgText() + expectedText, ag.getAttributeValue("label"));
    }
}
