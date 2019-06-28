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

            assertEquals(graph.getNodeCount(), 100 + 4); // START, END, Title, & Date
        }

        @Test
        void OneHundredLEsWithCommentsAndNLsProduces100Nodes() throws IOException, AngryTrollException {
            //
            input = new FileInput("./src/test/resources/OneHundredLogEvents_withCommentsAndEmptyLines.txt");
            parser = new TrollParser(input, "OneHundredLEsProduces100Nodes", "0000-00-00");
            graph = parser.parse();

            assertEquals(graph.getNodeCount(), 100 + 4); // START, END, Title, & Date
        }

        @Test
        void SimpleDecision() throws IOException, AngryTrollException {
            //
            input = new FileInput("./src/test/resources/SimpleDecision.txt");
            parser = new TrollParser(input, "SimpleDecision", "0000-00-00");
            graph = parser.parse();

            assertEquals(graph.getNodeCount(), 3 + 4);

            // todo: check edges
        }


    }
