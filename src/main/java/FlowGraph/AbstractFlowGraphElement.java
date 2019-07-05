package FlowGraph;

import java.util.LinkedList;

public abstract class AbstractFlowGraphElement implements iFlowGraphElement {

    int id;
    protected LinkedList<String> atts;

    public AbstractFlowGraphElement(int id) {
        this.id = id;
        atts = new LinkedList<>();
    }

    public String getAttributeValue(String attName) {
        String ret = "";
        for (String att : atts) {
            if (att.startsWith(attName)) {
                ret = att.substring(att.indexOf("=")+2, att.length()-1);
                break;
            }
        }
        return ret;
    }

    @Override
    public iFlowGraphElement addAttribute(String attLabel, String attValue) {
        atts.add(attLabel + "=\"" + attValue + "\"");
        return this;
    }

    @Override
    public String render() {
        StringBuilder rtn = new StringBuilder();
        rtn.append(id);
        if (!atts.isEmpty()) {
            rtn.append("[");
            atts.forEach(s -> rtn.append(s));
            rtn.append("]");
        }
        return rtn.toString();
    }

    @Override
    public int getId() {
        return id;
    }
}
