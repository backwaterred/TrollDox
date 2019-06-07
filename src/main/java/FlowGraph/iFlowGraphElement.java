package FlowGraph;

public interface iFlowGraphElement {

    public iFlowGraphElement addAttribute(String attName, String attValue);
    public String render();
    public int getId();
}
