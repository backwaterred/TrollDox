package Tests;

import FlowGraph.FlowGraph;
import TrollLang.AngryTrollException;
import TrollLang.TrollParser.FileInput;
import TrollLang.TrollParser.ParserInput;
import TrollLang.TrollParser.TrollParser;
import org.junit.jupiter.api.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

    public class ParserTest {

        ParserInput input;
        TrollParser parser;
        FlowGraph graph;

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
        void OneHundredLEsProduces100Nodes() throws IOException, AngryTrollException {
            input = new FileInput("./src/test/resources/OneHundredLogEvents.txt");
            parser = new TrollParser(input, "OneHundredLEsProduces100Nodes", "0000-00-00");
            graph = parser.parse();

            assertEquals(100 + 4, graph.getNodeCount()); // START, END, Title, & Date
        }

        @Test
        void OneHundredLEsWithCommentsAndNLsProduces100Nodes() throws IOException, AngryTrollException {
            //
            input = new FileInput("./src/test/resources/OneHundredLogEvents_withCommentsAndEmptyLines.txt");
            parser = new TrollParser(input, "OneHundredLEsProduces100Nodes", "0000-00-00");
            graph = parser.parse();

            assertEquals( 100 + 4, graph.getNodeCount()); // START, END, Title, & Date
        }

        @Test
        void SimpleDecision() throws IOException, AngryTrollException {
            //
            input = new FileInput("./src/test/resources/DecisionsDecisions.txt");
            parser = new TrollParser(input, "DecisionsDecisions", "0000-00-00");
            graph = parser.parse();

            assertEquals(8 + 4, graph.getNodeCount());

            // todo: check edges
        }


    }
