package FlowGraph;

public class GotoDart extends AbstractFlowGraphElement {

    public GotoDart(int id, String text) {
        super(id);
        this.addAttribute("shape", "invhouse");
        this.addAttribute("label", text);
    }
}
