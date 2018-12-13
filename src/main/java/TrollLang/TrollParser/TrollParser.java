package TrollLang.TrollParser;

import GML.GraphNode;
import GML.GraphElementException;
import GML.TextBox;
import TrollLang.AngryTrollException;
import TrollLang.TrollParam;
import TrollLang.TrollSpeak;

import java.io.IOException;
import java.util.Arrays;

public class TrollParser {
    private static final String exceptionBase = "TrollParser::";

    private ParserInput input;
    private GraphNode lastNode;

    public TrollParser(ParserInput input) {
        this.input = input;
    }

    public boolean hasNextElement() {
        return input.hasNextLine();
    }

    public GraphNode getNextElement() throws AngryTrollException, GraphElementException {
        String currentLine = this.getNextValidLine();
        GraphNode newNode;


        if (currentLine.startsWith(TrollSpeak.LOGEVENT.toString())) {
            String args = currentLine.substring(TrollSpeak.LOGEVENT.toString().length());

            newNode =  new TextBox(TrollSpeak.LOGEVENT.getPrefix() + parseLogEventArgs(args));

        } else {
            throw new AngryTrollException(exceptionBase + "getNextElement - Function not supported at line: " +
                    currentLine + "(" + input.getLineNumber() + ")");
        }

        if (lastNode != null) {
            lastNode.connectsTo(newNode);
        }

        this.lastNode = newNode;
        return newNode;
    }

    protected static boolean validInputLine(String line) {
        return !line.equals("") && !line.startsWith("//");
    }

    private String getNextValidLine() throws AngryTrollException {
        String line;

        try {
            line = input.getNextLine();
        } catch (IOException e) {
            throw new AngryTrollException(e.getMessage());
        }

        return line;
    }

    /**
     * Parse Strings containing quoted strings and TrollParams
     * <p>
     *     Input is some combination of parameters like:
     *     Application:Monsteras_Recaust.ModBUS:Modbus_RTU_slave.DO:_0540_Backflushing_Flag_FSS_B.Value (TrollParam)
     *     and quoted input like:
     *     "quoted input string" (with quotes)
     * </p>
     * <list>
     *      This method achieves 3 tasks:
     *      <li>Complain if there are mismatched quotes (by throwing an exception)</li>
     *      <li>Removes the (now) extraneous quotes</li>
     *      <li>Make the TrollParams readable using the toString() implementation in TrollParam</li>
     * </list>
     *
     * @param input A mixed bag of *quoted strings* and TrollParams
     * @return A string with each TrollParam converted to string and quotes removed.
     *  */
    private String parseLogEventArgs(String input) throws AngryTrollException {
        // Check for mismatched quotemarks
        if (!quotemarkMatched(input))
            throw new AngryTrollException("LogElement::parseInterleaved - Quotemark mismatch detected");

        // Remove all quotemarks from the string. We know they match so they are no longer necessary for our purposes.
        input = Arrays.stream(input.split("\""))
                .reduce((acc, ele) -> acc + ele)
                .get();

        // Map each troll param to it's pretty string equivalent
        String rtn = Arrays.stream(input.split(" "))
                .map(word -> (TrollParam.isValidParam(word)) ? (new TrollParam(word)).toString() : word)
                .reduce((acc, ele) -> acc + " " + ele)
                .get();

        return rtn;
    }

    private boolean quotemarkMatched(String input) {
        long quoteCount = Arrays.stream(input.split(""))
                .filter(s -> s.equals("\""))
                .count();
        return quoteCount % 2 == 0;
    }

}
