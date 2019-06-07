package FlowGraph;

import java.util.LinkedList;

public abstract class AbstractFlowGraphNode implements iFlowGraphElement {

    int id;
    private LinkedList<String> atts;

    public AbstractFlowGraphNode(int id) {
        this.id = id;
        atts = new LinkedList<>();
    }

    @Override
    public iFlowGraphElement addAttribute(String attLabel, String attValue) {
        atts.add(attLabel + "=\"" + attValue + "\"");
        return this;
    }

    @Override
    public String render() {
        StringBuilder rtn = new StringBuilder();
        rtn.append(id).append("[");
        atts.forEach(s -> rtn.append(s));
        rtn.append("]");
        return rtn.toString();
    }

    @Override
    public int getId() {
        return id;
    }
}
