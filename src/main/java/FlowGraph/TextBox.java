package FlowGraph;

public class TextBox extends AbstractFlowGraphNode {
    public TextBox(int id, String text) {
        super(id);
        this.addAttribute("shape", "box");
        this.addAttribute("label", text);
    }

}
