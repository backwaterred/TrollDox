package FlowGraph;

public class FlowGraphEdge extends AbstractFlowGraphElement {

    int fromId, toId;

    public FlowGraphEdge(int fromId, int toId) {
        super(Integer.parseInt(Integer.toString(fromId) + Integer.toString(toId)));
        this.fromId = fromId;
        this.toId = toId;
        this.id = Integer.parseInt(Integer.toString(fromId) + Integer.toString(toId));
    }

    public FlowGraphEdge(int fromId, int toId, String labelText) {
        super(Integer.parseInt(Integer.toString(fromId) + Integer.toString(toId)));
        this.fromId = fromId;
        this.toId = toId;
        this.id = Integer.parseInt(Integer.toString(fromId) + Integer.toString(toId));
        if (!labelText.isEmpty()) this.addAttribute("xlabel", labelText);
    }

    public int getFromId() {
        return fromId;
    }

    public int getToId() {
        return toId;
    }

    @Override
    public iFlowGraphElement addLabel(String labelText) {
        return this.addAttribute("xLabel", labelText);
    }

    @Override
    public String render() {
        StringBuilder rtn = new StringBuilder();
        rtn.append(fromId).append(" -> ").append(toId);
        if (!atts.isEmpty()) {
            rtn.append("[");
            atts.forEach(s -> rtn.append(s));
            rtn.append("]");
        }
        return rtn.toString();
    }

}
