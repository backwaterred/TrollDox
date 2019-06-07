package TrollLang.TrollParser;

import FlowGraph.FlowGraph;
import FlowGraph.TextOval;
import FlowGraph.TextBox;
import TrollLang.AngryTrollException;
import TrollLang.TrollParam;
import TrollLang.TrollSpeak;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

public class TrollParser {

    private static final int START_NODE_ID = 0;
    private static final int END_NODE_ID = 9999;
    private static final int ORPHAN_NODE_ID = -1;
    private ParserInput input;
    private LinkedList<TodoEntry> todo;
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

        TodoEntry(int lineNum, int parentId) {
            this.lineNum = lineNum;
            this.parentId = parentId;
        }
    }

    public TrollParser(ParserInput input, String graphTitle, String graphDate) throws AngryTrollException, IOException {
        this.input = input;
        this.todo = new LinkedList<>();
        this.graph = new FlowGraph(graphTitle, graphDate);
    }

    public FlowGraph parse() throws AngryTrollException {
        graph.addNode(new TextOval(START_NODE_ID, "START"));
        todo.push(new TodoEntry(1, ORPHAN_NODE_ID));

        while(!todo.isEmpty()) {
            processTodoEntry(todo.pop());
        }

        graph.addNode(new TextOval(END_NODE_ID, "END"));

        return graph;
    }

    private void processTodoEntry(TodoEntry currentEntry) {
        String line = null;
        try {
            line = input.getLine(currentEntry.lineNum);
        } catch (IOException e) {
            System.out.println("Warning: TrollParser could not parse line " + currentEntry.lineNum);
            e.printStackTrace();
        }

        if (line.isEmpty() || line.startsWith("//")) {
            return;
        } else if (line.startsWith(TrollSpeak.LOGEVENT.toString())) {
           try {
               // Add new element
               graph.addNode(new TextBox(currentEntry.lineNum, getLogEventMsg(line)));
               // Add edge from parent
               if ((currentEntry.parentId != ORPHAN_NODE_ID))
                   graph.addEdge(currentEntry.parentId, currentEntry.lineNum);
               // Add next
               if (input.hasLine(1+currentEntry.lineNum))
                   todo.push(new TodoEntry(1+currentEntry.lineNum, currentEntry.lineNum));
           } catch (AngryTrollException e) {
               System.out.println("Error parsing Logevent " + line);
               e.printStackTrace();
           }
       } else {
            // todo: Decide on default behaviour when nothing matches. For now do nothing.
           return;
       }
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
