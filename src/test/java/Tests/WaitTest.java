package Tests;

import FlowGraph.FlowGraph;
import FlowGraph.AbstractFlowGraphElement;
import TrollLang.AngryTrollException;
import TrollLang.TrollParser.FileInput;
import TrollLang.TrollParser.TrollParser;
import TrollLang.TrollSpeak;
import org.junit.jupiter.api.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class WaitTest extends AbstractTestRunner {

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
        assertTrue(false);
    }


    @Test
    void testSimpleWait() throws AngryTrollException{
        g = parser.parse();
        testStatement(TrollSpeak.WAIT, TEST_PARAM, 1);
        testStatement(TrollSpeak.WAIT, "2", 2);
        testStatement(TrollSpeak.WAIT, "3", 3);
    }

    @Test
    void testWaitFor() throws AngryTrollException {
        g = parser.parse();

        assertTrue(false);
    }

    private void testStatement(TrollSpeak type, String paramText, int line) {

        AbstractFlowGraphElement ag = (AbstractFlowGraphElement) g.getNode(line);
        assertEquals(type.getMsgText() + paramText + "seconds", ag.getAttributeValue("label"));
    }
}
