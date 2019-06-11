package FlowGraph;

public class DecisionDiamond extends AbstractFlowGraphElement {

    public DecisionDiamond(int id, String text) {
        super(id);
        this.addAttribute("shape", "diamond");
        this.addAttribute("label", text);
    }
}
