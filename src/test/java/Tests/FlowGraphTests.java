package Tests;

import FlowGraph.FlowGraph;
import org.junit.jupiter.api.*;
import FlowGraph.TextBox;
import FlowGraph.FlowGraphEdge;

import static org.junit.jupiter.api.Assertions.*;

public class FlowGraphTests {

    FlowGraph g;

    @BeforeAll
    static void beforeAll() {}

    @BeforeEach
    void before() {
        g = new FlowGraph();
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
    void testNodeCount() {
        for (int i=1; i < 101; i++) {
            g.addNode(new TextBox(i, "Element " + i));
        }
        assertEquals(g.getNodeCount(), 100);
    }
}
