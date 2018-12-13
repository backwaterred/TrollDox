package TrollElementTests;

import GML.GraphElementException;
import TrollLang.AngryTrollException;
import TrollLang.TrollParser.MockInput;
import TrollLang.TrollParser.TrollParser;
import TrollLang.TrollSpeak;
import org.junit.jupiter.api.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BasicLogEventTest extends AbstractTrollElementTestClass {


    @BeforeAll
    static void beforeAll() {}
    @BeforeEach
    void beforeEach() {
        input = new MockInput();
        parser = new TrollParser(input);
    }
    @AfterEach
    void afterEach() throws IOException {
        if (input != null) {
            input.close();
            input = null;
        }
    }
    @AfterAll
    static void afterAll() {}

    @Test
    public void greenlightTest() {
        assertTrue(true);
    }

    @Test
    public void testOnlyTrollParam() throws AngryTrollException, GraphElementException {
        String exp;

        for (String param : validParams) {
            ((MockInput) input).addLine(TrollSpeak.LOGEVENT.toString() + param);
        }
        ((MockInput) input).finalizeInput();

        for (int i=0; i<validParams.length; i++) {
            element = parser.getNextElement();
            exp = "LogEvent:\n" + validParamsPretty[i];
            assertEquals(exp, element.getText());
        }
    }

    @Test
    public void testInterleavedStringLotsofLitterals() throws AngryTrollException, GraphElementException {
        final String literalText = "\"zero: \"" +
                "\"alpha\"" +
                "\" one: \"" +
                "\"beta\"" +
                "\" two: \"" +
                "\"charlie\"" ;
        ((MockInput) input).addLine(TrollSpeak.LOGEVENT.toString() + literalText).finalizeInput();

        element = parser.getNextElement();

        final String exp = TrollSpeak.LOGEVENT.getPrefix() +
                "zero: " +
                "alpha" +
                " one: " +
                "beta" +
                " two: " +
                "charlie" ;

        assertEquals(exp, element.getText());
    }

    @Test
    public void testMultipleTrollParam() throws AngryTrollException, GraphElementException {
        final String literalText = validParams[0] + " " +
                validParams[1] + " " +
                validParams[2];
        ((MockInput) input).addLine(TrollSpeak.LOGEVENT.toString() + literalText).finalizeInput();

        element = parser.getNextElement();

        final String exp = TrollSpeak.LOGEVENT.getPrefix() +
                validParamsPretty[0] + " " +
                validParamsPretty[1] + " " +
                validParamsPretty[2];

        assertEquals(exp, element.getText());
    }

    @Test
    public void testInterleavedStringLiteralandParam() throws AngryTrollException, GraphElementException {
        final String literalText = "\"zero: \"" +
                validParams[0] +
                "\" one: \"" +
                validParams[1] +
                "\" two: \"" +
                validParams[2];
        ((MockInput) input).addLine(TrollSpeak.LOGEVENT.toString() + literalText).finalizeInput();

        element = parser.getNextElement();

        final String exp = TrollSpeak.LOGEVENT.getPrefix() +
                "zero: " +
                validParamsPretty[0] +
                " one: " +
                validParamsPretty[1] +
                " two: " +
                validParamsPretty[2];

        assertEquals(exp, element.getText());
    }
}

