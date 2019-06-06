package Graph;

public abstract class AbstractGraphNode implements iGraphNode {

    StringBuilder atts;

    public AbstractGraphNode() {
        atts = new StringBuilder();
    }

    protected void addAttribute(String att) {
        atts.append(" " + att);
    }

    protected void addLabel(String labelText) {
        addAttribute("label=\"" + labelText + "\"");
    }

    @Override
    public String render() {
        return "[" + atts.toString().trim() + "]";
    }

}
