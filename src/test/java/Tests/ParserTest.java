package Tests;

import FlowGraph.FlowGraph;
import FlowGraph.AbstractFlowGraphElement;
import TrollLang.AngryTrollException;
import TrollLang.TrollParam;
import TrollLang.TrollParser.FileInput;
import TrollLang.TrollParser.ParserInput;
import TrollLang.TrollParser.TrollParser;
import TrollLang.TrollSpeak;
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
        void linearGraphEdgeTest() throws IOException, AngryTrollException {
            input = new FileInput("./src/test/resources/OneHundredLogEvents.txt");
            parser = new TrollParser(input, "OneHundredLEsProduces100Nodes", "0000-00-00");
            graph = parser.parse();


            assertTrue(graph.hasConnection(TrollParser.START_NODE_ID, 1));
            for (int i=1; i<100; i++) {
                assertTrue(graph.hasConnection(i, i+1));
            }
            assertTrue(graph.hasConnection(100, TrollParser.END_NODE_ID));
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

            input = new FileInput("./src/test/resources/DecisionsDecisions.txt");
            parser = new TrollParser(input, "DecisionsDecisions", "0000-00-00");
            graph = parser.parse();

            assertEquals(8 + 4, graph.getNodeCount());

            // todo: check edges
        }

        @Test
        void simpleDesicionsEdgeTest() throws IOException, AngryTrollException {

            input = new FileInput("./src/test/resources/DecisionsDecisions.txt");
            parser = new TrollParser(input, "DecisionsDecisions", "0000-00-00");
            graph = parser.parse();

//            assertTrue(graph.hasConnection(2, 4));
//            assertTrue(graph.hasConnection(2, 6));
        }

        @Test
        void testSetTrioText() throws IOException, AngryTrollException {

            input = new FileInput("./src/test/resources/SETtrio.txt");
            parser = new TrollParser(input, "Sets and things", "0000-00-00");
            graph = parser.parse();

            AbstractFlowGraphElement age;
            age = (AbstractFlowGraphElement) graph.getNode(1);
            assertEquals(TrollSpeak.SET.getMsgText() + TrollParam.makeParamsPretty("Application:Mood_Graph.Console_Parameter:Console_Parameters.AI:_1234_Are_You_Feeling_Happy.Value") + " to " + 1,
                    age.getAttributeValue("label"));
            age = (AbstractFlowGraphElement) graph.getNode(2);
            assertEquals("Open: " + TrollParam.makeParamsPretty("Application:Mood_Graph.Console_Parameter:Console_Parameters.AI:_1234_SV1_This_Is_A_Valve.Value"),
                    age.getAttributeValue("label"));
            age = (AbstractFlowGraphElement) graph.getNode(3);
            assertEquals("Close: " + TrollParam.makeParamsPretty("Application:Mood_Graph.Console_Parameter:Console_Parameters.AI:_1234_SV2_This_Is_Another_Valve.Value"),
                    age.getAttributeValue("label"));
            age = (AbstractFlowGraphElement) graph.getNode(4);
            assertEquals(TrollSpeak.SET.getMsgText() + TrollParam.makeParamsPretty("Application:Mood_Graph.Console_Parameter:Console_Parameters.AI:_1234_This_Is_A_Running_Flag.Value") + " to " + 1,
                    age.getAttributeValue("label"));
        }

        @Test
        void testIF_IFbool() throws IOException, AngryTrollException {


            input = new FileInput("./src/test/resources/IF&IFbool.txt");
            parser = new TrollParser(input, "Test for IF, IFLESS, IFLESSEQUAL, IFGREATER, & IFGREATEREQUAL", "0000-00-00");
            graph = parser.parse();

//            assertTrue(graph.hasConnection(1, 7));
//            assertTrue(graph.hasConnection(1, 2));
//            assertTrue(graph.hasConnection(2, 7));
//            assertTrue(graph.hasConnection(2, 3));
//            assertTrue(graph.hasConnection(3, 7));
//            assertTrue(graph.hasConnection(3, 4));
//            assertTrue(graph.hasConnection(4, 7));
//            assertTrue(graph.hasConnection(4, 5));
//            assertTrue(graph.hasConnection(5, 7));
        }
        @Test
        void checkOddErrata() throws IOException, AngryTrollException {
            input = new FileInput("./src/test/resources/oddErrata.txt");
            parser = new TrollParser(input, "Odds", "0000-00-00");
            graph = parser.parse();

            assertEquals(2 + 4, graph.getNodeCount()); // START, END, Title, & Date
        }

        @Test
        void checkGotoLongJump() throws IOException, AngryTrollException {
            input = new FileInput("./src/test/resources/GOTOLong.txt");
            parser = new TrollParser(input, "Test Goto long jump behaviour", "0000-00-00");
            graph = parser.parse();

            assertFalse(graph.hasConnection(1,input.getLineNumberStartingWith("#Long")));
            // A 'bonus' check that doesn't truly belong here. The filler LEs between this jump and the destination shouldn't be parsed.
            assertEquals(2+4, graph.getNodeCount());
        }

        @Test
        void checkGotoShortJump() throws IOException, AngryTrollException {
            input = new FileInput("./src/test/resources/GOTOShort.txt");
            parser = new TrollParser(input, "Test Goto short jump behaviour", "0000-00-00");
            graph = parser.parse();

            assertTrue(graph.hasConnection(1,input.getLineNumberStartingWith("#Short")));
        }

        @Test
        void testIFLongAndShort() throws IOException, AngryTrollException {
            input = new FileInput("./src/test/resources/IFLongAndShort.txt");
            parser = new TrollParser(input, "Test Conditional statement long and short jump behaviour", "0000-00-00");
            graph = parser.parse();

            assertTrue(graph.hasConnection(1,2));
            assertTrue(graph.hasConnection(1,3));

            assertTrue(graph.hasConnection(4,5));
            assertFalse(graph.hasConnection(4,input.getLineNumberStartingWith("#Long")));
        }
    }
