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

    public static final int END_NODE_ID = 9999;
    public static final int START_NODE_ID = 0;

    protected static boolean validInputLine(String line) {
        return !line.equals("") && !line.startsWith("//");
    }

    private class TodoEntry {
        private int lineNum;
        private GMLNode parentNode;
        public String labelText;
        public boolean isSuper;

        public TodoEntry(int lineNum, GMLNode parentNode, boolean isSuper) {
            this.isSuper = isSuper;
            this.lineNum = lineNum;
            this.parentNode = parentNode;
            this.labelText = "";
        }

        public TodoEntry(int lineNum, GMLNode parentNode, boolean isSuper, String labelText) {
            this.isSuper = isSuper;
            this.lineNum = lineNum;
            this.parentNode = parentNode;
            this.labelText = labelText;
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

        GMLNode root = new TextBox(START_NODE_ID, "START");
        doc.addDocRoot(root);
        todo.push(new TodoEntry(getFirstLineNumber(), root, false));

        while(!todo.isEmpty()) {
            currItem = todo.pop();
            parseOneLine(currItem);
        }
        this.addEndNode();
    }

    // Assumes default path is added to node first
    private void addEndNode() throws GMLException {
        int lastRootIndex = doc.getRoots().size() -1;
        GMLNode node = doc.getRoots().get(lastRootIndex);

        while (node.getConnectedNodes().size() > 0) {
            node = node.getConnectedNodes().get(0);
        }

        GMLNode end = new TextBox(END_NODE_ID, "END");
        node.addConnection(end);
        doc.addNode(end);
        this.connectToParent(node, end, false,"");
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

    private void parseOneLine(TodoEntry currEntry) throws AngryTrollException, GMLException, IOException {
        int lineNum = currEntry.lineNum;
        GMLNode parentNode = currEntry.parentNode;
        String labelText = currEntry.labelText;
        boolean isCurrEntrySuper = currEntry.isSuper;

        // Don't re-process lines
        if (visited.contains(lineNum)) {
            return;
        }
        visited.add(lineNum);

        String currLine = input.getLine(lineNum);


        if (currLine.startsWith(TrollSpeak.LOGEVENT.getCommandText())) {
            // LOGEVENT
            GMLNode newNode = new TextBox(lineNum,
                    TrollSpeak.LOGEVENT.getMsgPrefix() + getLogEventMsg(currLine));

            this.connectToParent(parentNode, newNode, isCurrEntrySuper, labelText);
            addTodoItem(++lineNum, newNode, isCurrEntrySuper);

        } else if (currLine.startsWith(TrollSpeak.IF.getCommandText())) {
            // IF - ELSE
            GMLNode newNode = new DecisionDiamond(
                    lineNum,
                    getIfQuestion(currLine));

            this.connectToParent(parentNode, newNode, isCurrEntrySuper, labelText);

            String gotoLabel = currLine.split("GOTO")[1];
            addTodoItem(++lineNum, newNode, isCurrEntrySuper,"No");
            addTodoItem(input.getLineNumberStartingWith("#" + gotoLabel.trim()), newNode, true,"Yes");

        } else if (currLine.startsWith(TrollSpeak.GOTO.getCommandText())) {
            // GOTO
            String gotoLabel = currLine.split("GOTO")[1];

            GMLNode newNode = new TextBox(
                    lineNum,
                    TrollSpeak.GOTO.getMsgPrefix() + gotoLabel);

            this.connectToParent(parentNode, newNode, isCurrEntrySuper, labelText);

            addTodoItem(input.getLineNumberStartingWith("#" + gotoLabel.trim()), null, isCurrEntrySuper);

        } else if (currLine.startsWith(TrollSpeak.LABEL.getCommandText())) {
            // Label
            GMLNode newNode = new TextBox(lineNum,
                    currLine.substring(TrollSpeak.LABEL.getCommandText().length()));

            this.connectToParent(parentNode, newNode, isCurrEntrySuper, labelText);

            addTodoItem(++lineNum, newNode, false);

        } else  {
            // CMD not recognized
            throw new AngryTrollException(exceptionBase +
                    "parseOneLine - Invalid function at line: " + lineNum + " text: " + currLine);
        }
    }

    // if parent == null, fn is nullipotent
    private void connectToParent(GMLNode parent, GMLNode child, boolean isSuper, String label) throws GMLException {
        if (parent != null) {
            parent.addConnection(child);
            doc.addEdge(new GMLEdge(parent, child, label));
            if (isSuper) {
                doc.addSuperNode(child);
            } else {
                doc.addNode(child);
            }
        } else {
            doc.addDocRoot(child);
        }
    }

    private boolean addTodoItem(int lineNum, GMLNode parentNode, boolean isSuper) {
        return addTodoItem(lineNum, parentNode, isSuper, null);
    }

    private boolean addTodoItem(int lineNum, GMLNode parentNode, boolean isSuper, String labelText) {
        String line;
        try {
            line = input.getLine(lineNum);
            if (TrollParser.validInputLine(line)) {
                if (labelText != null) {
                    todo.push(new TodoEntry(lineNum, parentNode, isSuper, labelText));
                } else {
                    todo.push(new TodoEntry(lineNum, parentNode, isSuper));
                }
                return true;
            } else {
                if (labelText != null) {
                    return addTodoItem(++lineNum, parentNode, isSuper, labelText);
                } else {
                    return addTodoItem(++lineNum, parentNode, isSuper);
                }
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
