package FlowGraph;

public class TextOval extends AbstractFlowGraphElement {

    public TextOval(int id, String text) {
        super(id);
        this.addAttribute("shape", "oval");
        this.addAttribute("label", text);
    }
}
