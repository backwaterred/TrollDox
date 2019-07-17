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

    private static final int ORPHAN_NODE_ID =    -1;
    public static final int  START_NODE_ID  =     0;
    private static final int DATE_NODE_ID   =  9997;
    private static final int TITLE_NODE_ID  =  9998;
    public static final int  END_NODE_ID    =  9999;
    public static final int MAX_NODE_ID    = 10000;
    private static final int GOTO_MIN_CONNECTION_DISTANCE = 5;
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
        iFlowGraphElement headerNode, dateNode, startNode;

        headerNode = new TextBox(TITLE_NODE_ID, graphTitle);
        headerNode.addAttribute("style", "filled").addAttribute("fillcolor", "grey");
        graph.addNode(headerNode);

        dateNode = new TextBox(  DATE_NODE_ID, graphDate);
        dateNode.addAttribute("style", "filled").addAttribute("fillcolor", "grey");
        graph.addNode(dateNode);

        startNode = new TextOval(START_NODE_ID, "START");
        graph.addNode(startNode);

        graph.addNode(new TextOval(END_NODE_ID, "END"));
    }

    public FlowGraph parse() throws AngryTrollException {

        int startingLine;
        try {
            startingLine = input.getNextValidLineNumber(0); // Next valid will be > 0
        } catch (IOException e) {
            e.printStackTrace();
            throw new AngryTrollException("TrollParser::parse could not find any valid lines to parse");
        }


        todo.push(new TodoEntry(startingLine, START_NODE_ID));
        while(!todo.isEmpty()) {
            processTodoEntry(todo.pop());
        }

        return graph;
    }

    private boolean isParsableLine(String line) {
        if (line.isEmpty() || line.startsWith("//")) {
            return false;
        }
        String[] parts = line.split("\\s++");
        if (parts.length <= 0) {
            return false;
        }
        return true;
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
            System.out.println("Warning: TrollParser couldn't retrieve line " + currentEntry.lineNum);
            e.printStackTrace();
        }
        String[] parts = line.split("\\s++");

        // Process line
        iFlowGraphElement currentNode = graph.getNode(currentEntry.lineNum);
        if (!isParsableLine(line)) {
            // Do not process un-parsable lines. Assume the entry was created as a next line, and add the next-next line instead.
            todo.push(new TodoEntry(1+currentEntry.lineNum, currentEntry.parentId, currentEntry.edgeLabelText));
            return;
        } else if (parts[0].equals(TrollSpeak.LOGEVENT.getCommandText())) {
           try {
               if (currentNode == null)
                   graph.addNode(new TextBox(currentEntry.lineNum, getLogEventMsg(line)));
               addEdgeFromParent(currentEntry);
               addNextLineTodo(currentEntry);
           } catch (AngryTrollException e) {
               System.out.println("Error parsing LOGEVENT statement at line " + currentEntry.lineNum + ":\n\t" + line);
               e.printStackTrace();
           }
       } else if (parts[0].startsWith(TrollSpeak.LABEL.getCommandText())) {
            try {
                if (currentNode == null)
                    graph.addNode(new TextOval(currentEntry.lineNum, getLabelName(line)));
                addEdgeFromParent(currentEntry);
                addNextLineTodo(currentEntry);
            } catch (AngryTrollException e) {
                System.out.println("Error parsing label name at line " + currentEntry.lineNum + ":\n\t" + line);
                e.printStackTrace();
            }
        } else if (parts[0].equals(TrollSpeak.GOTO.getCommandText())) {
            try {
                int nextLine = input.getLineNumberStartingWith("#" + parts[1]);

                if (currentNode == null && nextLine - currentEntry.lineNum < GOTO_MIN_CONNECTION_DISTANCE) {
                    // skip adding GOTODart. Next line is too close.
                    addGotoTodo(currentEntry.parentId, parts[1]);
                } else {
                    // add new node as expected.
                    if (currentNode == null)
                        graph.addNode(new GotoDart(currentEntry.lineNum, getGotoMsgFromLabeName(line)));
                    addEdgeFromParent(currentEntry);
                    addGotoTodo(ORPHAN_NODE_ID, parts[1]);
                }
            } catch (AngryTrollException | IOException e) {
                System.out.println("Error parsing GOTO statement at line " + currentEntry.lineNum + ":\n\t" + line);
                e.printStackTrace();
            }
        } else if (parts[0].equals(TrollSpeak.IF.getCommandText())) {
            try {
                if (currentNode == null)
                    graph.addNode(new DecisionDiamond(currentEntry.lineNum, getIfQuestion(line)));
                addEdgeFromParent(currentEntry);
                addNextLineTodo(currentEntry, "No");
                addConditionalBranch(currentEntry, parts[4], "Yes");
            } catch (AngryTrollException | IOException e) {
                System.out.println("Error parsing IF statement at line " + currentEntry.lineNum + ":\n\t" + line);
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
                System.out.println("Error parsing WAIT statement at line " + currentEntry.lineNum + ":\n\t" + line);
                e.printStackTrace();
            }
        } else if (parts[0].equals(TrollSpeak.ADD.getCommandText())) {
           try {
               if (currentNode == null)
                   graph.addNode(new TextBox(currentEntry.lineNum, getAddMsg(line)));
               addEdgeFromParent(currentEntry);
               addNextLineTodo(currentEntry);
           } catch (AngryTrollException e) {
               System.out.println("Error parsing ADD instruction at line " + currentEntry.lineNum + "\n\t" + line);
               e.printStackTrace();
           }
        } else if (parts[0].equals(TrollSpeak.SUB.getCommandText())) {
            try {
                if (currentNode == null)
                    graph.addNode(new TextBox(currentEntry.lineNum, getSubMsg(line)));
                addEdgeFromParent(currentEntry);
                addNextLineTodo(currentEntry);
            } catch (AngryTrollException e) {
                System.out.println("Error parsing SUB instruction at line " + currentEntry.lineNum + "\n\t" + line);
                e.printStackTrace();
            }
        } else if (parts[0].equals(TrollSpeak.MUL.getCommandText())) {

            try {
                if (currentNode == null)
                    graph.addNode(new TextBox(currentEntry.lineNum, getMultMsg(line)));
                addEdgeFromParent(currentEntry);
                addNextLineTodo(currentEntry);
            } catch (AngryTrollException e) {
                System.out.println("Error parsing MUL instruction at line " + currentEntry.lineNum + "\n\t" + line);
                e.printStackTrace();
            }
        } else if (parts[0].equals(TrollSpeak.DIV.getCommandText())) {
            try {
                if (currentNode == null)
                    graph.addNode(new TextBox(currentEntry.lineNum, getDivMsg(line)));
                addEdgeFromParent(currentEntry);
                addNextLineTodo(currentEntry);
            } catch (AngryTrollException e) {
                System.out.println("Error parsing DIV instruction at line " + currentEntry.lineNum + "\n\t" + line);
                e.printStackTrace();
            }
        } else if (parts[0].equals(TrollSpeak.WAITFOR.getCommandText())) {
            try {
                if (currentNode == null)
                    graph.addNode(new DecisionDiamond(currentEntry.lineNum, getWaitForQuestion(line)));
                addEdgeFromParent(currentEntry);
//                addGotoTodo(currentEntry, parts[6], "Yes");
                addConditionalBranch(currentEntry, parts[6], "Yes");
                addNextLineTodo(currentEntry, "No");
            } catch (AngryTrollException | IOException e) {
                System.out.println("Error parsing WAITFOR instruction at line " + currentEntry.lineNum + "\n\t" + line);
                e.printStackTrace();
            }
        } else if (parts[0].equals(TrollSpeak.WAITFORL.getCommandText())) {
            try {
                if (currentNode == null)
                    graph.addNode(new DecisionDiamond(currentEntry.lineNum, getWaitForLessQuestion(line)));
                addEdgeFromParent(currentEntry);
                addConditionalBranch(currentEntry, parts[6], "Yes");
//                addGotoTodo(currentEntry, parts[6], "Yes");
                addNextLineTodo(currentEntry, "No");
            } catch (AngryTrollException | IOException e) {
                System.out.println("Error parsing WAITFORLESS instruction at line " + currentEntry.lineNum + "\n\t" + line);
                e.printStackTrace();
            }
        } else if (parts[0].equals(TrollSpeak.WAITFORLEQ.getCommandText())) {
            try {
                if (currentNode == null)
                    graph.addNode(new DecisionDiamond(currentEntry.lineNum, getWaitForLessEqualQuestion(line)));
                addEdgeFromParent(currentEntry);
                addConditionalBranch(currentEntry, parts[6], "Yes");
//                addGotoTodo(currentEntry, parts[6], "Yes");
                addNextLineTodo(currentEntry, "No");
            } catch (AngryTrollException | IOException e) {
                System.out.println("Error parsing WAITFORLESSEQUAL instruction at line " + currentEntry.lineNum + "\n\t" + line);
                e.printStackTrace();
            }
        } else if (parts[0].equals(TrollSpeak.WAITFORG.getCommandText())) {
            try {
                if (currentNode == null)
                    graph.addNode(new DecisionDiamond(currentEntry.lineNum, getWaitForGreaterQuestion(line)));
                addEdgeFromParent(currentEntry);
                addConditionalBranch(currentEntry, parts[6], "Yes");
//                addGotoTodo(currentEntry, parts[6], "Yes");
                addNextLineTodo(currentEntry, "No");
            } catch (AngryTrollException | IOException e) {
                System.out.println("Error parsing WAITFORGREATER instruction at line " + currentEntry.lineNum + "\n\t" + line);
                e.printStackTrace();
            }
        } else if (parts[0].equals(TrollSpeak.WAITFORGEQ.getCommandText())) {
            try {
                if (currentNode == null)
                    graph.addNode(new DecisionDiamond(currentEntry.lineNum, getWaitForGreaterEqualQuestion(line)));
                addEdgeFromParent(currentEntry);
//                addGotoTodo(currentEntry, parts[6], "Yes");
                addConditionalBranch(currentEntry, parts[6], "Yes");
                addNextLineTodo(currentEntry, "No");
            } catch (AngryTrollException | IOException e) {
                System.out.println("Error parsing WAITFORGREATEREQUAL instruction at line " + currentEntry.lineNum + "\n\t" + line);
                e.printStackTrace();
            }
        } else if (parts[0].equals(TrollSpeak.SET.getCommandText())) {
            try {
                if (currentNode == null)
                    graph.addNode(new TextBox(currentEntry.lineNum, getSetMsg(line)));
                addEdgeFromParent(currentEntry);
                addNextLineTodo(currentEntry);
            } catch (AngryTrollException e) {
                e.printStackTrace();
                System.out.println("Error parsing SET statement at line " + currentEntry.lineNum + ":\n\t" + line);
                e.printStackTrace();
            }
        } else if (parts[0].equals(TrollSpeak.IFL.getCommandText())) {
            try {
                if (currentNode == null)
                    graph.addNode(new DecisionDiamond(currentEntry.lineNum, getIFLQuestion(line)));
                addEdgeFromParent(currentEntry);
                addConditionalBranch(currentEntry, parts[4], "Yes");
//                addGotoTodo(currentEntry, parts[4], "Yes");
                addNextLineTodo(currentEntry, "No");
            } catch (AngryTrollException | IOException e) {
                System.out.println("Error parsing WAITFORGREATEREQUAL instruction at line " + currentEntry.lineNum + "\n\t" + line);
                e.printStackTrace();
            }
        } else if (parts[0].equals(TrollSpeak.IFLEQ.getCommandText())) {
            try {
                if (currentNode == null)
                    graph.addNode(new DecisionDiamond(currentEntry.lineNum, getIFLEQQuestion(line)));
                addEdgeFromParent(currentEntry);
                addConditionalBranch(currentEntry, parts[4], "Yes");
//                addGotoTodo(currentEntry, parts[4], "Yes");
                addNextLineTodo(currentEntry, "No");
            } catch (AngryTrollException | IOException e) {
                System.out.println("Error parsing WAITFORGREATEREQUAL instruction at line " + currentEntry.lineNum + "\n\t" + line);
                e.printStackTrace();
            }
        } else if (parts[0].equals(TrollSpeak.IFG.getCommandText())) {
            try {
                if (currentNode == null)
                    graph.addNode(new DecisionDiamond(currentEntry.lineNum, getIFGQuestion(line)));
                addEdgeFromParent(currentEntry);
                addConditionalBranch(currentEntry, parts[4], "Yes");
//                addGotoTodo(currentEntry, parts[4], "Yes");
                addNextLineTodo(currentEntry, "No");
            } catch (AngryTrollException | IOException e) {
                System.out.println("Error parsing WAITFORGREATEREQUAL instruction at line " + currentEntry.lineNum + "\n\t" + line);
                e.printStackTrace();
            }
        } else if (parts[0].equals(TrollSpeak.IFGEQ.getCommandText())) {
            try {
                if (currentNode == null)
                    graph.addNode(new DecisionDiamond(currentEntry.lineNum, getIFGEQQuestion(line)));
                addEdgeFromParent(currentEntry);
                addConditionalBranch(currentEntry, parts[4], "Yes");
//                addGotoTodo(currentEntry, parts[4], "Yes");
                addNextLineTodo(currentEntry, "No");
            } catch (AngryTrollException | IOException e) {
                System.out.println("Error parsing WAITFORGREATEREQUAL instruction at line " + currentEntry.lineNum + "\n\t" + line);
                e.printStackTrace();
            }
        } else if (parts[0].equals(TrollSpeak.EXECUTE.getCommandText())) {
            if (currentNode == null)
                graph.addNode(new TextBox(currentEntry.lineNum, "Execute"));
            addEdgeFromParent(currentEntry);
            addNextLineTodo(currentEntry);
        } else if (parts[0].equals(TrollSpeak.WAITFORRESULTS.getCommandText())) {
            if (currentNode == null)
                graph.addNode(new TextBox(currentEntry.lineNum, "Wait for results"));
            addEdgeFromParent(currentEntry);
            addNextLineTodo(currentEntry);
        } else {
            // todo: Decide on default behaviour when nothing matches. For now do nothing.
            System.out.println("TrollParser::ProcessTodoEntry unrecognised statement at line: " + currentEntry.lineNum + "\n\t" + line);
           return;
       }
    }

    /**
     * Checks distance between current and target; adds GTDart if less than GOTO_MIN_CONNECTION_DISTANCE.
     * Avoids code repetition since IF, IFLESS, ... WAITFORGREATEREQUAL all perform this task.
     * @param currentEntry A TodoEntry with the currentLine, parentLine, ... state information.
     * @param target The text following the 'GOTO'
     */
    private void addConditionalBranch(TodoEntry currentEntry, String target, String edgeLabelText) throws AngryTrollException, IOException {
        int nextLineNumber = input.getLineNumberStartingWith( "#" + target);

        if (nextLineNumber - currentEntry.lineNum < GOTO_MIN_CONNECTION_DISTANCE) {
            // next line is close; proceed as normal
            addGotoTodo(currentEntry.lineNum, target, edgeLabelText);
        } else {
            // next line is far. Add extra GotoDart
           graph.addNode(new GotoDart(MAX_NODE_ID + currentEntry.lineNum,getGotoMsgFromLabeName("GOTO " + target)));
           addEdgeFromParent(new TodoEntry(MAX_NODE_ID + currentEntry.lineNum, currentEntry.lineNum, edgeLabelText));
           addGotoTodo(ORPHAN_NODE_ID, target);
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
     * @param currentEntry The line right before the 'next line' which is to be added.
     * @param labelText String to add to edgeLabel
     */
    private void addNextLineTodo(TodoEntry currentEntry, String labelText) {
        try {
            todo.push(new TodoEntry(input.getNextValidLineNumber(currentEntry.lineNum), currentEntry.lineNum, labelText));
        } catch (IOException e) {
            System.out.println("TrollParser::addNextLineTodo could not find " + labelText);
            graph.addEdge(new FlowGraphEdge(currentEntry.lineNum, END_NODE_ID));
        }
    }

    /**
     * Adds a TD for a GOTO statement. Called by both the GOTO instruction and any instruction containing a GOTO statement.
     * @param parentLineNum The parent of the new TD
     * @param target The line beginning with the GOTO instruction
     */
    private void addGotoTodo(int parentLineNum, String target) throws IOException {
        addGotoTodo(parentLineNum, target, "");
    }
    /**
     * Adds a TD for a GOTO statement. Called by both the GOTO instruction and any instruction containing a GOTO statement.
     * @param parentLineNum The parent of the new TD
     * @param target The name of the label to add as a goto
     * @param labelText String to add to edgeLabel
     */
    private void addGotoTodo(int parentLineNum, String target, String labelText) throws IOException {
        int targetLineNum = input.getLineNumberStartingWith("#" + target.trim());

        todo.push(new TodoEntry(targetLineNum, parentLineNum, labelText));
    }

    /**
     * Adds edge from parent node to current node if not an orphan.
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
        if (!input.startsWith(TrollSpeak.LOGEVENT.getCommandText()))
            throw new AngryTrollException("Malformed LOGEVENT string sent to getLogEventMsg " + input);

        String body = input.substring(TrollSpeak.LOGEVENT.getCommandText().length());
        // Check for mismatched quotemarks
        if (!areQuotemarksMatched(body))
            throw new AngryTrollException("LogElement::getLogEventMsg - Quotemark mismatch detected");

        // Remove all quotemarks from the string now that we know they match.
        body = Arrays.stream(body.split("\""))
                .reduce((acc, ele) -> acc + ele)
                .get();

        // Map each troll param to it's pretty string equivalent
        String rtn = Arrays.stream(body.split("\\s++"))
                .map(word -> TrollParam.makeParamsPretty(word))
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
    private String getGotoMsgFromLabeName(String line) throws AngryTrollException {
        String[] parts = line.split("\\s++");
        if (parts.length != 2) throw new AngryTrollException("Malformed GOTO string in getGotoMsgFromLabeName " + line);

        return TrollSpeak.GOTO.getMsgText() + parts[1];
    }


    /**
     * Parses the wait message from WAIT param|number.
     * If parameter then makes pretty.
     * @param line The line in the form WAIT param|number
     * @return A nice string
     */
    private String getWaitMsg(String line) throws AngryTrollException {
        String[] parts = line.split("\\s++");
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
        String[] parts = line.split("\\s++");
        if (parts.length != 3) throw new AngryTrollException("Malformed ADD instruction string sent to getAddMsg " + line);

        return TrollSpeak.ADD.getMsgText() + TrollParam.makeParamsPretty(parts[1]) + " by " + TrollParam.makeParamsPretty(parts[2]);
    }

    /**
     * Parse the SUB instruction message from the line ADD Parameter Parameter|Value
     * @param line SUB Param1 Param2|Value2
     * @return Decrement Param1 by Param2|Value2
     */
    private String getSubMsg(String line) throws AngryTrollException {
        String[] parts = line.split("\\s++");
        if (parts.length != 3) throw new AngryTrollException("Malformed SUB instruction string sent to getSubMsg" + line);

        return TrollSpeak.SUB.getMsgText() + TrollParam.makeParamsPretty(parts[1]) + " by " + TrollParam.makeParamsPretty(parts[2]);
    }

    /**
     * Parse the MUL instruction message from the line ADD Parameter Parameter|Value
     * @param line ADD Param1 Param2|Value2
     * @return Multiply Param1 by Param2|Value2
     */
    private String getMultMsg(String line) throws AngryTrollException{
        String[] parts = line.split("\\s++");
        if (parts.length != 3) throw new AngryTrollException("Malformed MUL instruction string sent to getMulMsg " + line);

        return TrollSpeak.MUL.getMsgText() + TrollParam.makeParamsPretty(parts[1]) + " by " + TrollParam.makeParamsPretty(parts[2]);
    }

    /**
     * Parse the DIV instruction message from the line ADD Parameter Parameter|Value
     * @param line DIV Param1 Param2|Value2
     * @return Divide Param1 by Param2|Value2
     */
    private String getDivMsg(String line) throws AngryTrollException {
        String[] parts = line.split("\\s++");
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
     * Forms a question from the left, right and comparator of a condition statement. Parses TrollParams and applies the makePretty method to them.
     * EG:
     * left == "Garbage:Garbage.Garbage_Garbage...Garbage_GAR_Sample_Flow.Garbage"
     * comparator == "="
     * right == "Garbage:Garbage.Garbage_Garbage...Garbage_GAR_Min_Sample_Flow.Garbage"
     * returns -> "Is Sample Flow = Min Sample Flow?"
     *
     * @param left The left side of the condition question. TrollParams will be made pretty.
     * @param comparator The comparator used in the question
     * @param right The right side of the condition statement. TrollParams will be made pretty.
     * @return The condition statement
     */
    private String getConditionQuestion(String left, String comparator, String right) throws AngryTrollException {
        return "Is " + TrollParam.makeParamsPretty(left) + comparator + TrollParam.makeParamsPretty(right) + "?";
    }

    /**
     * Parse the question from a given IF statement
     * @param line Statement of the form IF parameter|number parameter|number GOTO #Label
     * @return A question of the form TODO
     */
    private String getIfQuestion(String line) throws AngryTrollException {
        String[] parts = line.split("\\s++");
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
        String[] parts = line.split("\\s++");
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
        String[] parts = line.split("\\s++");
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
        String[] parts = line.split("\\s++");
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
        String[] parts = line.split("\\s++");
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
        String[] parts = line.split("\\s++");
        if (parts.length != 7) throw new AngryTrollException("Malformed WAITFOR instruction string sent to getWaitForQuestion " + line);

        // WAITFOR param str|num|param TIMEOUT num GOTO Label
        return getConditionQuestion(parts[1], TrollSpeak.WAITFORGEQ.getMsgText(), parts[2]);
    }

    private String getSetMsg(String line) throws AngryTrollException {
        String[] parts = line.split("\\s++");
        if (parts.length != 3) throw new AngryTrollException("Malformed SET instruction string sent to getSetMsg " + line);

        if ((parts[1].contains("valve") || parts[1].contains("Valve")) && parts[2].equals("1")) {
            return "Open: " + TrollParam.makeParamsPretty(parts[1]);
        } else if ((parts[1].contains("valve") || parts[1].contains("Valve")) && parts[2].equals("0")) {
            return "Close: " + TrollParam.makeParamsPretty(parts[1]);
        } else
            return TrollSpeak.SET.getMsgText() + TrollParam.makeParamsPretty(parts[1]) + " to " + TrollParam.makeParamsPretty(parts[2]);
    }

    private String getIFLQuestion(String line) throws AngryTrollException {
        String[] parts = line.split("\\s++");
        if (parts.length != 5) throw new AngryTrollException("Malformed IFL instruction string sent to getIFLQuestion" + line);

        return getConditionQuestion(parts[1], TrollSpeak.IFL.getMsgText(), parts[2]);
    }

    private String getIFLEQQuestion(String line) throws AngryTrollException {
        String[] parts = line.split("\\s++");
        if (parts.length != 5) throw new AngryTrollException("Malformed IFLEQ instruction string sent to getIFLEQQuestion" + line);

        return getConditionQuestion(parts[1], TrollSpeak.IFLEQ.getMsgText(), parts[2]);
    }

    private String getIFGQuestion(String line) throws AngryTrollException {
        String[] parts = line.split("\\s++");
        if (parts.length != 5) throw new AngryTrollException("Malformed IFG instruction string sent to getIFGQQuestion" + line);

        return getConditionQuestion(parts[1], TrollSpeak.IFG.getMsgText(), parts[2]);
    }

    private String getIFGEQQuestion(String line) throws AngryTrollException {
        String[] parts = line.split("\\s++");
        if (parts.length != 5) throw new AngryTrollException("Malformed IFGEQ instruction string sent to getIFGEQQuestion" + line);

        return getConditionQuestion(parts[1], TrollSpeak.IFGEQ.getMsgText(), parts[2]);
    }
}
