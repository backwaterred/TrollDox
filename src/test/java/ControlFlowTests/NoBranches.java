package ControlFlowTests;

import GML.GraphNode;
import TrollLang.TrollParser.FileInput;
import TrollLang.TrollParser.ParserInput;
import TrollLang.TrollParser.TrollParser;
import org.junit.jupiter.api.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class NoBranches {

    private ParserInput input = null;
    private TrollParser parser = null;

    @BeforeAll
    static void beforeAll() {}
    @BeforeEach
    void before() {
        try {
            input = new FileInput("./src/test/resources/OneHundredLogEvents.txt");
            parser = new TrollParser(input);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }
    @AfterEach
    void after() {
        input = null;
        parser = null;
    }
    @AfterAll
    static void afterAll() {}

    @Test
    void GLtest() {
        assertTrue(true);
    }

    @Test
    void test100LEsConnected() {
        try {
            GraphNode prevNode = parser.getNextElement();
            GraphNode currNode = parser.getNextElement();

            while (parser.hasNextElement()) {

            }
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

}

