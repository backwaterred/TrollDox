package FlowGraph;

public interface iFlowGraphElement {

    public iFlowGraphElement addAttribute(String attName, String attValue);
    public iFlowGraphElement addLabel(String labelText);
    public String render();
    public int getId();
}
