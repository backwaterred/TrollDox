package TrollLang.TrollParser;

import FlowGraph.*;
import TrollLang.AngryTrollException;
import TrollLang.TrollParam;
import TrollLang.TrollSpeak;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;

public class TrollParser {

    private static final int START_NODE_ID  = 0;
    private static final int END_NODE_ID    = 9999;
    private static final int TITLE_NODE_ID  = 9998;
    private static final int DATE_NODE_ID   = 9997;
    private static final int ORPHAN_NODE_ID = -1;
    private ParserInput input;
    private LinkedList<TodoEntry> todo;
    private HashSet<Integer> visited;
    private FlowGraph graph;

    protected static boolean validInputLine(String line) {
        return !line.equals("") && !line.startsWith("//");
    }

    /**
     * Inner class for ToDo List Items.
     **/
    private class TodoEntry {
        int lineNum;
        int parentId;
        String edgeLabelText;

        TodoEntry(int lineNum, int parentId) {
            this.lineNum = lineNum;
            this.parentId = parentId;
            this.edgeLabelText = "";
        }

        TodoEntry(int lineNum, int parentId, String edgeLabelText) {
            this.lineNum = lineNum;
            this.parentId = parentId;
            this.edgeLabelText = edgeLabelText;
        }

    }

    public TrollParser(ParserInput input, String graphTitle, String graphDate) {
        this.input = input;
        this.todo = new LinkedList<>();
        this.visited = new HashSet<>();
        this.graph = new FlowGraph();

        // Add title, date, start, and end nodes
        iFlowGraphElement headerNode, dateNode;
        headerNode = new TextBox(TITLE_NODE_ID, graphTitle);
        headerNode.addAttribute("style", "filled").addAttribute("fillcolor", "grey");
        graph.addNode(headerNode);
        dateNode = new TextBox(  DATE_NODE_ID, graphDate);
        dateNode.addAttribute("style", "filled").addAttribute("fillcolor", "grey");
        graph.addNode(dateNode);

        graph.addNode(new TextOval(START_NODE_ID, "START"));
        graph.addNode(new TextOval(END_NODE_ID, "END"));
    }

    public FlowGraph parse() throws AngryTrollException {

        int startingLine;
        try {
            startingLine = input.getNextValidLineNumber(0); // Next valid will be > 0
        } catch (IOException e) {
            e.printStackTrace();
            throw new AngryTrollException("TrollParser::parse could not find any valid line to parse");
        }


//        graph.addEdge(new FlowGraphEdge(START_NODE_ID, startingLine));
        todo.push(new TodoEntry(startingLine, START_NODE_ID));
        while(!todo.isEmpty()) {
            processTodoEntry(todo.pop());
        }

        return graph;
    }

    /**
     * Process a single TD list item. Fail-fast if previously visited.
     * @param currentEntry The TD list item to process
     */
    private void processTodoEntry(TodoEntry currentEntry) {
        // Check visited. Add edge if previously visited, else mark visited and proceed.
        if (visited.contains(currentEntry.lineNum)) {
            addEdgeFromParent(currentEntry);
            return;
        }
        visited.add(currentEntry.lineNum);

        // Get line from input
        String line = null;
        try {
            line = input.getLine(currentEntry.lineNum);
        } catch (IOException e) {
            System.out.println("Warning: TrollParser could not parse line " + currentEntry.lineNum);
            e.printStackTrace();
        }
        String[] parts = line.split(" ");

        // Process line
        iFlowGraphElement currentNode = graph.getNode(currentEntry.lineNum);
        if (line.isEmpty() || line.startsWith("//")) {
            // Do not process empty lines or comments. Assume the entry was created as a next line, and add the next next line instead.
            todo.push(new TodoEntry(1+currentEntry.lineNum, currentEntry.parentId, currentEntry.edgeLabelText));
            return;
        } else if (parts[0].equals(TrollSpeak.LOGEVENT.getCommandText())) {
           try {
               if (currentNode == null)
                   graph.addNode(new TextBox(currentEntry.lineNum, getLogEventMsg(line)));
               addEdgeFromParent(currentEntry);
               addNextLineTodo(currentEntry);
           } catch (AngryTrollException e) {
               System.out.println("Couldn't parse LOGEVENT statement at line " + currentEntry.lineNum + ":\n\t" + line);
               e.printStackTrace();
           }
       } else if (parts[0].startsWith(TrollSpeak.LABEL.getCommandText())) {
            try {
                if (currentNode == null)
                    graph.addNode(new TextOval(currentEntry.lineNum, getLabelName(line)));
                addEdgeFromParent(currentEntry);
                addNextLineTodo(currentEntry);
            } catch (AngryTrollException e) {
                System.out.println("Couldn't parse label name at line " + currentEntry.lineNum + ":\n\t" + line);
                e.printStackTrace();
            }
        } else if (parts[0].equals(TrollSpeak.GOTO.getCommandText())) {
            try {
                if (currentNode == null)
                    graph.addNode(new GotoDart(currentEntry.lineNum, getGotoMsg(line)));
                addEdgeFromParent(currentEntry);
                addGotoTodo(currentEntry, line);
            } catch (AngryTrollException | IOException e) {
                System.out.println("Couldn't parse GOTO statement at line " + currentEntry.lineNum + ":\n\t" + line);
                e.printStackTrace();
            }
        } else if (parts[0].equals(TrollSpeak.IF.getCommandText())) {
            try {
                if (currentNode == null)
                    graph.addNode(new DecisionDiamond(currentEntry.lineNum, getIfQuestion(line)));
                addEdgeFromParent(currentEntry);
                addGotoTodo(currentEntry, line.substring(line.indexOf("GOTO")), "Yes");
                addNextLineTodo(currentEntry, "No");
            } catch (AngryTrollException | IOException e) {
                System.out.println("Couldn't parse IF statement at line " + currentEntry.lineNum + ":\n\t" + line);
                e.printStackTrace();
            }
        } else if (parts[0].equalsIgnoreCase(TrollSpeak.WAIT.getCommandText())) {
            try {
                if (currentNode == null)
                    graph.addNode(new TextBox(currentEntry.lineNum, getWaitMsg(line)));
                addEdgeFromParent(currentEntry);
                addNextLineTodo(currentEntry);
            } catch (AngryTrollException e) {
                e.printStackTrace();
                System.out.println("Couldn't parse WAIT statement at line " + currentEntry.lineNum + ":\n\t" + line);
                e.printStackTrace();
            }
        } else if (parts[0].equals(TrollSpeak.ADD.getCommandText())) {
           try {
               if (currentNode == null)
                   graph.addNode(new TextBox(currentEntry.lineNum, getAddMsg(line)));
               addEdgeFromParent(currentEntry);
               addNextLineTodo(currentEntry);
           } catch (AngryTrollException e) {
               System.out.println("Error while parsing ADD instruction at line " + currentEntry.lineNum + "\n\t" + line);
               e.printStackTrace();
           }
        } else if (parts[0].equals(TrollSpeak.SUB.getCommandText())) {
            try {
                if (currentNode == null)
                    graph.addNode(new TextBox(currentEntry.lineNum, getSubMsg(line)));
                addEdgeFromParent(currentEntry);
                addNextLineTodo(currentEntry);
            } catch (AngryTrollException e) {
                System.out.println("Error while parsing SUB instruction at line " + currentEntry.lineNum + "\n\t" + line);
                e.printStackTrace();
            }
        } else if (parts[0].equals(TrollSpeak.MUL.getCommandText())) {

            try {
                if (currentNode == null)
                    graph.addNode(new TextBox(currentEntry.lineNum, getMultMsg(line)));
                addEdgeFromParent(currentEntry);
                addNextLineTodo(currentEntry);
            } catch (AngryTrollException e) {
                System.out.println("Error while parsing MUL instruction at line " + currentEntry.lineNum + "\n\t" + line);
                e.printStackTrace();
            }
        } else if (parts[0].equals(TrollSpeak.DIV.getCommandText())) {
            try {
                if (currentNode == null)
                    graph.addNode(new TextBox(currentEntry.lineNum, getDivMsg(line)));
                addEdgeFromParent(currentEntry);
                addNextLineTodo(currentEntry);
            } catch (AngryTrollException e) {
                System.out.println("Error while parsing DIV instruction at line " + currentEntry.lineNum + "\n\t" + line);
                e.printStackTrace();
            }
        } else if (parts[0].equals(TrollSpeak.WAITFOR.getCommandText())) {
            try {
                if (currentNode == null)
                    graph.addNode(new DecisionDiamond(currentEntry.lineNum, getWaitForQuestion(line)));
                addEdgeFromParent(currentEntry);
                addGotoTodo(currentEntry, line.substring(line.indexOf("GOTO")), "Yes");
                addNextLineTodo(currentEntry, "No");
            } catch (AngryTrollException | IOException e) {
                System.out.println("Error while parsing WAITFOR instruction at line " + currentEntry.lineNum + "\n\t" + line);
                e.printStackTrace();
            }
        } else if (parts[0].equals(TrollSpeak.WAITFORL.getCommandText())) {
            try {
                if (currentNode == null)
                    graph.addNode(new DecisionDiamond(currentEntry.lineNum, getWaitForLessQuestion(line)));
                addEdgeFromParent(currentEntry);
                addGotoTodo(currentEntry, line.substring(line.indexOf("GOTO")), "Yes");
                addNextLineTodo(currentEntry, "No");
            } catch (AngryTrollException | IOException e) {
                System.out.println("Error while parsing WAITFORLESS instruction at line " + currentEntry.lineNum + "\n\t" + line);
                e.printStackTrace();
            }
        } else if (parts[0].equals(TrollSpeak.WAITFORLEQ.getCommandText())) {
            try {
                if (currentNode == null)
                    graph.addNode(new DecisionDiamond(currentEntry.lineNum, getWaitForLessEqualQuestion(line)));
                addEdgeFromParent(currentEntry);
                addGotoTodo(currentEntry, line.substring(line.indexOf("GOTO")), "Yes");
                addNextLineTodo(currentEntry, "No");
            } catch (AngryTrollException | IOException e) {
                System.out.println("Error while parsing WAITFORLESSEQUAL instruction at line " + currentEntry.lineNum + "\n\t" + line);
                e.printStackTrace();
            }
        } else if (parts[0].equals(TrollSpeak.WAITFORG.getCommandText())) {
            try {
                if (currentNode == null)
                    graph.addNode(new DecisionDiamond(currentEntry.lineNum, getWaitForGreaterQuestion(line)));
                addEdgeFromParent(currentEntry);
                addGotoTodo(currentEntry, line.substring(line.indexOf("GOTO")), "Yes");
                addNextLineTodo(currentEntry, "No");
            } catch (AngryTrollException | IOException e) {
                System.out.println("Error while parsing WAITFORGREATER instruction at line " + currentEntry.lineNum + "\n\t" + line);
                e.printStackTrace();
            }
        } else if (parts[0].equals(TrollSpeak.WAITFORGEQ.getCommandText())) {
            try {
                if (currentNode == null)
                    graph.addNode(new DecisionDiamond(currentEntry.lineNum, getWaitForGreaterEqualQuestion(line)));
                addEdgeFromParent(currentEntry);
                addGotoTodo(currentEntry, line.substring(line.indexOf("GOTO")), "Yes");
                addNextLineTodo(currentEntry, "No");
            } catch (AngryTrollException | IOException e) {
                System.out.println("Error while parsing WAITFORGREATEREQUAL instruction at line " + currentEntry.lineNum + "\n\t" + line);
                e.printStackTrace();
            }
        } else if (parts[0].equals(TrollSpeak.SET.getCommandText())) {
        } else {
            // todo: Decide on default behaviour when nothing matches. For now do nothing.
            System.out.println("TrollParser::ProcessTodoEntry unrecognised statement at line: " + currentEntry.lineNum + "\n\t" + line);
           return;
       }
    }


    /**
     * Adds the next line to the TD list if present in the file. Sets the given item as parent.
     * @param currentEntry the line before the line to be added.
     */
    private void addNextLineTodo(TodoEntry currentEntry) {
        addNextLineTodo(currentEntry, "");
    }
    /**
     * Adds the next line to the TD list if present in the file. Sets the given item as parent.
     * @param currentEntry the line before the line to be added.
     * @param labelText String to add to edgeLabel
     */
    private void addNextLineTodo(TodoEntry currentEntry, String labelText) {
        try {
            todo.push(new TodoEntry(input.getNextValidLineNumber(currentEntry.lineNum), currentEntry.lineNum, labelText));
        } catch (IOException e) {
            graph.addEdge(new FlowGraphEdge(currentEntry.lineNum, END_NODE_ID));
        }
    }

    /**
     * Adds a TD for a GOTO statement. Called by both the GOTO instruction and any instruction containing a GOTO statement.
     * @param currentEntry The current TD entry being processed
     * @param line The line beginning with the GOTO instruction
     */
    private void addGotoTodo(TodoEntry currentEntry, String line) throws IOException {
        addGotoTodo(currentEntry, line, "");
    }
    /**
     * Adds a TD for a GOTO statement. Called by both the GOTO instruction and any instruction containing a GOTO statement.
     * @param currentEntry The current TD entry being processed
     * @param line The line beginning with the GOTO instruction
     * @param labelText String to add to edgeLabel
     */
    private void addGotoTodo(TodoEntry currentEntry, String line, String labelText) throws IOException {
        String label = line.split(" ")[1]; // Given GOTO #label want #label
        todo.push(new TodoEntry(input.getLineNumberStartingWith("#" + label.trim()),currentEntry.lineNum, labelText));
    }

    /**
     * Adds edge from parent node to current node if not an orphan.
     * @param currentEntry The element to use as edge destination. Will use the parentId as edge source if not set to ORPHAN_NODE_ID.
     */
    private void addEdgeFromParent(TodoEntry currentEntry) {
        if ((currentEntry.parentId != ORPHAN_NODE_ID))
            graph.addEdge(new FlowGraphEdge(currentEntry.parentId, currentEntry.lineNum, currentEntry.edgeLabelText));
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
        input = input.substring(1+ TrollSpeak.LOGEVENT.getCommandText().length()); // Remove 'LOGEVENT' command text

        // Check for mismatched quotemarks
        if (!areQuotemarksMatched(input))
            throw new AngryTrollException("LogElement::getLogEventMsg - Quotemark mismatch detected");

        // Remove all quotemarks from the string. We know they match so they are no longer necessary for our purposes.
        input = Arrays.stream(input.split("\""))
                .reduce((acc, ele) -> acc + ele)
                .get();

        // Map each troll param to it's pretty string equivalent
        String rtn = Arrays.stream(input.split(" "))
                .map(word -> (TrollParam.isValidParam(word)) ? (new TrollParam(word)).getPrettyText() : word)
                .reduce((acc, ele) -> acc + " " + ele)
                .get();

        return TrollSpeak.LOGEVENT.getMsgText() + rtn;
    }

    private boolean areQuotemarksMatched(String input) {
        long quoteCount = Arrays.stream(input.split(""))
                .filter(s -> s.equals("\""))
                .count();
        return quoteCount % 2 == 0;
    }

    /**
     * Input is a string of the form GOTO labelName
     * Removes the GOTO command and replaces it with GOTO msg as specified in TrollSpeak.
     * Formats the label text for readability
     * @param line The fulls string in the format mentioned above.
     * @return The string as mentioned above
     **/
    private String getGotoMsg(String line) throws AngryTrollException {
        String[] parts = line.split(" ");
        if (parts.length != 2) throw new AngryTrollException("Malformed GOTO string in getGotoMsg " + line);

        return parts[1];
    }


    /**
     * Parses the wait message from WAIT param|number.
     * If parameter then makes pretty.
     * @param line The line in the form WAIT param|number
     * @return A nice string
     */
    private String getWaitMsg(String line) throws AngryTrollException {
        String[] parts = line.split(" ");
        if (parts.length != 2) throw new AngryTrollException("Malformed WAIT string sent to getWaitMsg " + line);

        String msg = TrollParam.isValidParam(parts[1])? TrollParam.makeParamsPretty(parts[1]) : parts[1];
        return TrollSpeak.WAIT.getMsgText() + msg;
    }

    /**
     * Parse the ADD instruction message from the line ADD Parameter Parameter|Value
     * @param line ADD Param1 Param2|Value2
     * @return Increment Param1 by Param2|Value2
     */
    private String getAddMsg(String line) throws AngryTrollException {
        String[] parts = line.split(" ");
        if (parts.length != 3) throw new AngryTrollException("Malformed ADD instruction string sent to getAddMsg " + line);

        return TrollSpeak.ADD.getMsgText() + TrollParam.makeParamsPretty(parts[1]) + " by " + TrollParam.makeParamsPretty(parts[2]);
    }

    /**
     * Parse the SUB instruction message from the line ADD Parameter Parameter|Value
     * @param line SUB Param1 Param2|Value2
     * @return Decrement Param1 by Param2|Value2
     */
    private String getSubMsg(String line) throws AngryTrollException {
        String[] parts = line.split(" ");
        if (parts.length != 3) throw new AngryTrollException("Malformed SUB instruction string sent to getSubMsg" + line);

        return TrollSpeak.SUB.getMsgText() + TrollParam.makeParamsPretty(parts[1]) + " by " + TrollParam.makeParamsPretty(parts[2]);
    }

    /**
     * Parse the MUL instruction message from the line ADD Parameter Parameter|Value
     * @param line ADD Param1 Param2|Value2
     * @return Multiply Param1 by Param2|Value2
     */
    private String getMultMsg(String line) throws AngryTrollException{
        String[] parts = line.split(" ");
        if (parts.length != 3) throw new AngryTrollException("Malformed MUL instruction string sent to getMulMsg " + line);

        return TrollSpeak.MUL.getMsgText() + TrollParam.makeParamsPretty(parts[1]) + " by " + TrollParam.makeParamsPretty(parts[2]);
    }

    /**
     * Parse the DIV instruction message from the line ADD Parameter Parameter|Value
     * @param line DIV Param1 Param2|Value2
     * @return Divide Param1 by Param2|Value2
     */
    private String getDivMsg(String line) throws AngryTrollException {
        String[] parts = line.split(" ");
        if (parts.length != 3) throw new AngryTrollException("Malformed DIV instruction string sent to getDivMsg " + line);

        return TrollSpeak.DIV.getMsgText() + TrollParam.makeParamsPretty(parts[1]) + " by " + TrollParam.makeParamsPretty(parts[2]);
    }

    /**
     * Parse the name of the label shown by #LabelNameHere
     * @param line A label name of the form #LabelNameHere
     * @return The parsed label name: Label Name Here
     */
    private String getLabelName(String line) throws AngryTrollException {
        if (!line.startsWith("#")) throw new AngryTrollException("Malformed label name sent ot parseLabelName " + line);

        // todo: Convert from camel case to normal writing.
        return line.substring(1 + line.indexOf("#"));
    }

    /**
     *
     * @param left
     * @param comparator
     * @param right
     * @return
     */
    private String getConditionQuestion(String left, String comparator, String right) {
        return TrollParam.makeParamsPretty(left) + comparator + TrollParam.makeParamsPretty(right) + "?";
    }

    /**
     * Parse the question from a given IF statement
     * @param line Statement of the form IF parameter|number parameter|number GOTO #Label
     * @return A question of the form TODO
     */
    private String getIfQuestion(String line) throws AngryTrollException {
        String[] parts = line.split(" ");
        if (parts.length != 5) throw new AngryTrollException("Malformed IF string in getIfQuestion" + line);

        return getConditionQuestion(parts[1], TrollSpeak.IF.getMsgText(), parts[2]);
    }

    /**
     * get the message from a WAITFOR statement
     * @throws AngryTrollException when the number of parts is wrong
     * @param line A string of the form WAITFOR
     * @return the formatted string
     */
    private String getWaitForQuestion(String line) throws AngryTrollException {
        String[] parts = line.split(" ");
        if (parts.length != 7) throw new AngryTrollException("Malformed WAITFOR instruction string sent to getWaitForQuestion " + line);

        // WAITFOR param str|num|param TIMEOUT num GOTO Label
        return getConditionQuestion(parts[1], TrollSpeak.WAITFOR.getMsgText(), parts[2]);
    }

    /**
     * get the message from a WAITFOR statement
     * @throws AngryTrollException when the number of parts is wrong
     * @param line A string of the form WAITFOR
     * @return the formatted string
     */
    private String getWaitForLessQuestion(String line) throws AngryTrollException {
        String[] parts = line.split(" ");
        if (parts.length != 7) throw new AngryTrollException("Malformed WAITFOR instruction string sent to getWaitForQuestion " + line);

        // WAITFOR param str|num|param TIMEOUT num GOTO Label
        return getConditionQuestion(parts[1], TrollSpeak.WAITFORL.getMsgText(), parts[2]);
    }

    /**
     * get the message from a WAITFOR statement
     * @throws AngryTrollException when the number of parts is wrong
     * @param line A string of the form WAITFOR
     * @return the formatted string
     */
    private String getWaitForLessEqualQuestion(String line) throws AngryTrollException {
        String[] parts = line.split(" ");
        if (parts.length != 7) throw new AngryTrollException("Malformed WAITFOR instruction string sent to getWaitForQuestion " + line);

        // WAITFOR param str|num|param TIMEOUT num GOTO Label
        return getConditionQuestion(parts[1], TrollSpeak.WAITFORLEQ.getMsgText(), parts[2]);
    }

    /**
     * get the message from a WAITFOR statement
     * @throws AngryTrollException when the number of parts is wrong
     * @param line A string of the form WAITFOR
     * @return the formatted string
     */
    private String getWaitForGreaterQuestion(String line) throws AngryTrollException {
        String[] parts = line.split(" ");
        if (parts.length != 7) throw new AngryTrollException("Malformed WAITFOR instruction string sent to getWaitForQuestion " + line);

        // WAITFOR param str|num|param TIMEOUT num GOTO Label
        return getConditionQuestion(parts[1], TrollSpeak.WAITFORG.getMsgText(), parts[2]);
    }

        /**
     * get the message from a WAITFOR statement
     * @throws AngryTrollException when the number of parts is wrong
     * @param line A string of the form WAITFOR
     * @return the formatted string
     */
    private String getWaitForGreaterEqualQuestion(String line) throws AngryTrollException {
        String[] parts = line.split(" ");
        if (parts.length != 7) throw new AngryTrollException("Malformed WAITFOR instruction string sent to getWaitForQuestion " + line);

        // WAITFOR param str|num|param TIMEOUT num GOTO Label
        return getConditionQuestion(parts[1], TrollSpeak.WAITFORGEQ.getMsgText(), parts[2]);
    }
}
