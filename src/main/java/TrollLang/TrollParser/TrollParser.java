package TrollLang.TrollParser;

import GML.GMLException;
import GML.TextBox;
import Graph.GraphNode;
import TrollLang.AngryTrollException;
import TrollLang.TrollParam;
import TrollLang.TrollSpeak;

import java.io.IOException;
import java.util.*;

public class TrollParser {
    private static final String exceptionBase = "TrollParser::";

    private ParserInput input;
    private LinkedList<TodoEntry> todo;

    private class TodoEntry {
        private int lineNum;
        private GraphNode parentNode;

        public TodoEntry(int lineNum, GraphNode parentNode) {
            this.lineNum = lineNum;
            this.parentNode = parentNode;
        }
    }

    private HashSet<Integer> visitedLines;

    public TrollParser(ParserInput input, GraphNode rootNode) throws AngryTrollException, GMLException, IOException {
        this.input = input;
        this.todo = new LinkedList<>();

        parseAll(rootNode);
    }

    private void parseAll(GraphNode rootNode) throws GMLException, AngryTrollException, IOException {
        TodoEntry currItem;

        todo.push(new TodoEntry(getFirstLineNumber(), rootNode));
        while(!todo.isEmpty()) {
            currItem = todo.pop();
            parseOne(currItem.lineNum, currItem.parentNode);
        }

    }

    private int getFirstLineNumber() {
        String firstLine = "";

        int i = 1;
        try {
            for (firstLine = input.getLine(i); !TrollParser.validInputLine(firstLine); firstLine = input.getLine(++i));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return i;
    }

    private GraphNode parseOne(int lineNum, GraphNode parentNode) throws AngryTrollException, GMLException, IOException {
        String currLine = "";

        try {
            currLine = input.getLine(lineNum);
        } catch (IOException e) {
            throw new AngryTrollException(exceptionBase + "parseOne - Caught exception while trying to get line: " + lineNum +
                    " -- msg:(" + e.getMessage() + ")");
        }

        // LOGEVENT
        if (currLine.startsWith(TrollSpeak.LOGEVENT.toString())) {
            GraphNode newNode = new GraphNode(
                    lineNum,
                    new TextBox(TrollSpeak.LOGEVENT.getPrefix() +
                            getLogEventMsg(currLine)));
            if (parentNode != null) parentNode.addConnectedNode(newNode);
            addTodoItem(++lineNum, newNode);
            return newNode;
        // IF - ELSE
        } else if (currLine.startsWith(TrollSpeak.IF.toString())) {
            GraphNode newNode = new GraphNode(
                    lineNum,
                    new TextBox(getIfQuestion(currLine)) // Remove TrollSpeak command text
            );
            String gotoLabel = currLine.split("GOTO")[1];
            if (parentNode != null) parentNode.addConnectedNode(newNode);
            addTodoItem(++lineNum, newNode);
            addTodoItem(input.getLineNumberStartingWith("#" + gotoLabel.trim()), newNode);
            return newNode;
        // GOTO
        } else if (currLine.startsWith(TrollSpeak.GOTO.toString())) {
            String gotoLabel = currLine.split("GOTO")[1];
            GraphNode newNode = new GraphNode(
                    lineNum,
                    new TextBox(TrollSpeak.GOTO.getPrefix() + gotoLabel)
            );
            if (parentNode != null) parentNode.addConnectedNode(newNode);
            addTodoItem(input.getLineNumberStartingWith("#" + gotoLabel.trim()), null);
            return newNode;
        // Unknown function
        } else {
            throw new AngryTrollException(exceptionBase +
                    "parseOne - Invalid function at line: " + lineNum + " text: " + currLine);
        }
    }

    protected static boolean validInputLine(String line) {
        return !line.equals("") && !line.startsWith("//") && !line.startsWith("#");
    }

    private boolean addTodoItem(int lineNum, GraphNode parentNode) {
        String line;
        try {
            line = input.getLine(lineNum);
            if (TrollParser.validInputLine(line)) {
                todo.push(new TodoEntry(lineNum, parentNode));
                return true;
            } else {
                return addTodoItem(++lineNum, parentNode);
            }
        } catch (IOException e) {
            return false;
        }


    }

    private String getIfQuestion(String args) {
        args = args.substring(TrollSpeak.IF.toString().length());
        args = args.split("GOTO")[0];

        return TrollParam.makeParamsPretty(args) + "?";
    }


    /**
     * Parse Strings containing quoted strings and TrollParams
     * <p>
     *     Input is some combination of parameters like:
     *     Application:Project_Name.Connection_Style:Connetion_Style_Repeated.DO:_1234_Parameter_Name_Here.Value (TrollParam)
     *     and quoted input like:
     *     "quoted input string" (with quotes)
     * </p>
     * <list>
     *      This method achieves 3 tasks:
     *      <li>Complain if there are mismatched quotes (by throwing an exception)</li>
     *      <li>Removes the extraneous quotes</li>
     *      <li>Make the TrollParams readable using the toString() implementation in TrollParam</li>
     * </list>
     *
     * @param input A mixed bag of *quoted strings* and TrollParams
     * @return A string with each TrollParam converted to string and quotes removed.
     *  */
    private String getLogEventMsg(String input) throws AngryTrollException {
        input = input.substring(TrollSpeak.LOGEVENT.toString().length()); // Remove 'LOGEVENT' command text

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
