package Tests;

import FlowGraph.AbstractFlowGraphElement;
import TrollLang.AngryTrollException;
import TrollLang.TrollParam;
import TrollLang.TrollParser.FileInput;
import TrollLang.TrollParser.TrollParser;
import TrollLang.TrollSpeak;
import org.junit.jupiter.api.*;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

public class ArithmeticTest extends AbstractTestRunner {

    private String TEST_PARAM = TrollParam.makeParamsPretty("Application:Cust_System.CanOpen:CANopen_Transducers.DI:_0100_Air_Pressure_Normally_Open.Value");

    @BeforeAll
    static void beforeAll() {}
    @BeforeEach
    void before() throws IOException, AngryTrollException {
        parser = new TrollParser(new FileInput("./src/test/resources/Arithmetic.txt"), "Arithmetic Test Graph", "0000-00-00");
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
    void testMath() throws AngryTrollException {
        g = parser.parse();

        assertEquals(4+4, g.getNodeCount());

        AbstractFlowGraphElement ag;
        // ADD
        ag = (AbstractFlowGraphElement) g.getNode(1);
        assertEquals(TrollSpeak.ADD.getMsgText() + TEST_PARAM + " by " + 1, ag.getAttributeValue("label"));
        // SUB
        ag = (AbstractFlowGraphElement) g.getNode(2);
        assertEquals(TrollSpeak.SUB.getMsgText() + TEST_PARAM + " by " + 2, ag.getAttributeValue("label"));
        // MUL
        ag = (AbstractFlowGraphElement) g.getNode(3);
        assertEquals(TrollSpeak.MUL.getMsgText() + TEST_PARAM + " by " + 3, ag.getAttributeValue("label"));
        // DIV
        ag = (AbstractFlowGraphElement) g.getNode(4);
        assertEquals(TrollSpeak.DIV.getMsgText() + TEST_PARAM + " by " + 4, ag.getAttributeValue("label"));
    }

    @Test
    void testAdd() throws AngryTrollException {
        g = parser.parse();
        testStatement(TrollSpeak.ADD, 1);
    }

    @Test
    void testSub() throws AngryTrollException {
        g = parser.parse();
        testStatement(TrollSpeak.SUB, 2);
    }

    @Test
    void testMul() throws AngryTrollException {
        g = parser.parse();
        testStatement(TrollSpeak.MUL, 3);
    }

    @Test
    void testDiv() throws AngryTrollException {
        g = parser.parse();
        testStatement(TrollSpeak.DIV, 4);
    }

    private void testStatement(TrollSpeak type, int line) {

        AbstractFlowGraphElement ag = (AbstractFlowGraphElement) g.getNode(line);
        assertEquals(type.getMsgText() + TEST_PARAM + " by " + line, ag.getAttributeValue("label"));
    }
}
