package ParserTests;

import GML.GraphElementException;
import TrollLang.AngryTrollException;
import TrollLang.TrollParser.FileInput;
import TrollLang.TrollParser.TrollParser;
import org.junit.jupiter.api.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class BasicParserTest {

    TrollParser parser;

    @BeforeAll
    static void beforeAll() {}
    @BeforeEach
    void beforeEach() {
        try {
            // "./src/test/resources/OneHundredLogEvents.txt"
            parser = new TrollParser(new FileInput("./src/test/resources/OneHundredLogEvents.txt"));
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }
    @AfterEach
    void afterEach() {}
    @AfterAll
    static void afterAll() {}

    @Test
    public void greenlightTest() {
        assertTrue(true);
    }

    @Test
    public void parse100LogEvents() throws GraphElementException {
        for(int i=1; i<=100; i++) {
            assertTrue(parser.hasNextElement());
            try {
                parser.getNextElement();
            } catch (AngryTrollException e) {
                e.printStackTrace();
                fail();
            }
        }
    }

    @Test
    void parse101LogEvents() throws GraphElementException {
        parse100LogEvents();
        if (parser.hasNextElement()) {
            try {
                parser.getNextElement();
            } catch (AngryTrollException e) {
                e.printStackTrace();
                fail();
            }
        }
        assertFalse(parser.hasNextElement());
        assertThrows(AngryTrollException.class,
                () -> parser.getNextElement());



    }
}

