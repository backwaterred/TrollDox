package FlowGraph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class FlowGraph {

    HashMap<Integer, iFlowGraphElement> nodes;
    HashSet<iFlowGraphElement> edges;
    LinkedList<String> atts;

    public FlowGraph(String title, String date) {
        nodes = new HashMap<>();
//        edges = new LinkedList<>();
        edges = new HashSet<>();
        atts = new LinkedList<>();
        this.addAttribute("page", "\"8,11\"");
        this.addAttribute("ratio", "auto");
        this.addAttribute("fontsize", "12");

        // Add header and date nodes
        iFlowGraphElement headerNode, dateNode;
        headerNode = new TextBox(99999, title);
        headerNode.addAttribute("style", "filled").addAttribute("fillcolor", "grey");
        this.addNode(headerNode);
        dateNode = new TextBox(  99998, date);
        dateNode.addAttribute("style", "filled").addAttribute("fillcolor", "grey");
        this.addNode(dateNode);
    }

    /**
     * Gets an existing node with the given id or creates a new one if it does not yet exist.
     * @param id The id (line number) of the element
     * @return the element with the given id or null if it dne
     **/
    public iFlowGraphElement getNode(int id) {
        return nodes.get(id);
    }

    public void addAttribute(String name, String value) {
        atts.add(name + "=" + value);
    }

    public String render() {
        StringBuilder rtn = new StringBuilder();
        rtn.append("digraph {\n");

        rtn.append("\n// Graph Attributes\n");
        rtn.append("graph[");
        atts.forEach(s -> rtn.append(s).append(" "));
        rtn.append("]\n");

        rtn.append("\n// Nodes\n");
        nodes.values().forEach(nd -> rtn.append(nd.render()).append("\n"));

        rtn.append("\n// Edges\n");
        edges.forEach(e -> rtn.append(e.render()).append("\n"));

        rtn.append("}\n");

        return rtn.toString();
    }

    /**
     * Adds an node to the graph
     **/
    public void addNode(iFlowGraphElement nd) {
        nodes.put(nd.getId(), nd);
    }

    /**
     * Adds an edge to the graph
    **/
    public void addEdge(iFlowGraphElement e) {
        edges.add(e);
    }
}