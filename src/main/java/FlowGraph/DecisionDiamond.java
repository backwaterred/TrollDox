package FlowGraph;

public class TextOval extends AbstractFlowGraphNode {

    public TextOval(int id, String text) {
        super(id);
        this.addAttribute("shape", "oval");
        this.addAttribute("label", text);
    }
}
