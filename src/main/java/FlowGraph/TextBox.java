package FlowGraph;

public class TextBox extends AbstractFlowGraphElement {
    public TextBox(int id, String text) {
        super(id);
        this.addAttribute("shape", "box");
        this.addAttribute("label", text);
    }

}
