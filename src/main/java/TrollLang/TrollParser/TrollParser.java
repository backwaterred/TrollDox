package TrollLang.TrollParser;

import Doc.Doc;
import GML.*;
import TrollLang.AngryTrollException;
import TrollLang.TrollParam;
import TrollLang.TrollSpeak;

import java.io.IOException;
import java.util.*;

public class TrollParser {
    private static final String exceptionBase = "TrollParser::";

    private ParserInput input;
    private Doc doc;
    private LinkedList<TodoEntry> todo;
    private HashSet<Integer> visited;

    protected static boolean validInputLine(String line) {
        return !line.equals("") && !line.startsWith("//");
    }

    private class TodoEntry {
        private int lineNum;
        private GMLNode parentNode;

        public TodoEntry(int lineNum, GMLNode parentNode) {
            this.lineNum = lineNum;
            this.parentNode = parentNode;
        }
    }

    public TrollParser(ParserInput input, Doc doc) throws AngryTrollException, GMLException, IOException {
        this.input = input;
        this.doc = doc;
        this.todo = new LinkedList<>();
        this.visited = new HashSet<>();


        parseAllLines();
    }

    private void parseAllLines() throws GMLException, AngryTrollException, IOException {
        TodoEntry currItem;

        GMLNode root = new TextBox(0, "START");
        doc.addDocRoot(root);
        todo.push(new TodoEntry(getFirstLineNumber(), root));

        while(!todo.isEmpty()) {
            currItem = todo.pop();
            parseOneLine(currItem.lineNum, currItem.parentNode);
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

    private void parseOneLine(int lineNum, GMLNode parentNode) throws AngryTrollException, GMLException, IOException {
        // Don't re-process lines
        if (visited.contains(lineNum)) {
//            System.out.println("Skipping Line (" + lineNum + ") - already processed");
            return;
        }
        visited.add(lineNum);

        String currLine = input.getLine(lineNum);


        if (currLine.startsWith(TrollSpeak.LOGEVENT.getCommandText())) {
            // LOGEVENT
            GMLNode newNode = new TextBox(lineNum,
                    TrollSpeak.LOGEVENT.getMsgPrefix() + getLogEventMsg(currLine));

            this.connectToParent(parentNode, newNode);

            addTodoItem(++lineNum, newNode);

        } else if (currLine.startsWith(TrollSpeak.IF.getCommandText())) {
            // IF - ELSE
            GMLNode newNode = new DecisionDiamond(
                    lineNum,
                    getIfQuestion(currLine));

            this.connectToParent(parentNode, newNode);

            String gotoLabel = currLine.split("GOTO")[1];
            addTodoItem(++lineNum, newNode);
            addTodoItem(input.getLineNumberStartingWith("#" + gotoLabel.trim()), newNode);

        } else if (currLine.startsWith(TrollSpeak.GOTO.getCommandText())) {
            // GOTO
            String gotoLabel = currLine.split("GOTO")[1];

            GMLNode newNode = new TextBox(
                    lineNum,
                    TrollSpeak.GOTO.getMsgPrefix() + gotoLabel);

            if (parentNode != null) parentNode.addConnection(newNode);

            addTodoItem(input.getLineNumberStartingWith("#" + gotoLabel.trim()), null);

        } else if (currLine.startsWith(TrollSpeak.LABEL.getCommandText())) {
            // Label
            GMLNode newNode = new TextBox(lineNum,
                    currLine.substring(TrollSpeak.LABEL.getCommandText().length()));

            this.connectToParent(parentNode, newNode);

            addTodoItem(++lineNum, newNode);

        } else  {
            // CMD not recognized
            throw new AngryTrollException(exceptionBase +
                    "parseOneLine - Invalid function at line: " + lineNum + " text: " + currLine);
        }
    }

    private void connectToParent(GMLNode parent, GMLNode child) {
//        private void connectToParent(GMLNode parent, GMLNode child, String label) {
        if (parent != null) {
            parent.addConnection(child);
//            doc.addEdge(child, parent, label);
        } else {
            doc.addDocRoot(child);
        }
    }

    private boolean addTodoItem(int lineNum, GMLNode parentNode) {
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
