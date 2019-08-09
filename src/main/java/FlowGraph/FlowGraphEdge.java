package FlowGraph;

public class FlowGraphEdge extends AbstractFlowGraphElement {

    int fromId, toId;

    public FlowGraphEdge(int fromId, int toId) {
        super(Integer.parseInt(Integer.toString(fromId) + Integer.toString(toId)));
        this.fromId = fromId;
        this.toId = toId;
        this.id = Integer.parseInt(Integer.toString(fromId) + Integer.toString(toId));
        this.setDefaults();
    }

    public FlowGraphEdge(int fromId, int toId, String labelText) {
        super(Integer.parseInt(Integer.toString(fromId) + Integer.toString(toId)));
        this.fromId = fromId;
        this.toId = toId;
        this.id = Integer.parseInt(Integer.toString(fromId) + Integer.toString(toId));
        this.setDefaults();
        if (!labelText.isEmpty()) this.addAttribute("xlabel", labelText);
    }

    public int getFromId() {
        return fromId;
    }

    public int getToId() {
        return toId;
    }

    private void setDefaults() {
        this.addAttribute("weight", "10");
    }

    @Override
    public iFlowGraphElement addLabel(String labelText) {
        return this.addAttribute("xlabel", labelText);
    }

    @Override
    public String render() {
        StringBuilder rtn = new StringBuilder();
        rtn.append(fromId).append(" -> ").append(toId);
        if (!atts.isEmpty()) {
            rtn.append("[");
            atts.forEach(s -> rtn.append(s).append(" "));
            rtn.append("]");
        }
        return rtn.toString();
    }

}
