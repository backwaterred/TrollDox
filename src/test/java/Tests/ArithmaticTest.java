package Tests;

import FlowGraph.FlowGraph;
import TrollLang.TrollParser.FileInput;
import TrollLang.TrollParser.TrollParser;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class ArithmaticTest {

    TrollParser parser;

    @BeforeAll
    static void beforeAll() {}
    @BeforeEach
    void before() {
        parser = new TrollParser(new FileInput("./src/test/resources/OneHundredLogEvents.txt"));
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
    void testAdd() {
        assertTrue(true);
    }
}
