package FlowGraph;

import java.util.LinkedList;

public abstract class AbstractFlowGraphElement implements iFlowGraphElement {

    int id;
    protected LinkedList<String> atts;
    private static int LABEL_TEXT_MAX_WIDTH = 40;
//    private static int LABEL_TEXT_MAX_WORD_WIDTH = 40;

    public AbstractFlowGraphElement(int id) {
        this.id = id;
        atts = new LinkedList<>();
    }

    public String getAttributeValue(String attName) {
        String ret = "";
        for (String att : atts) {
            if (att.startsWith(attName)) {
                ret = att.substring(att.indexOf("=")+2, att.length()-1);
                break;
            }
        }
        return ret;
    }

    @Override
    public iFlowGraphElement addLabel(String labelText) {
        StringBuilder lineWrappedLabel = new StringBuilder();

        int charSinceLastWrap = 0;
        for (String word : labelText.split("\\s++")) {
            // Shorten any super-long words
//            if (word.length() > LABEL_TEXT_MAX_WORD_WIDTH)
//                word = "..." + word.substring(word.length() - LABEL_TEXT_MAX_WORD_WIDTH - 1);
            // Add NL if width is greater than max
            if (charSinceLastWrap > LABEL_TEXT_MAX_WIDTH) {
                lineWrappedLabel.append("\\n");
                charSinceLastWrap = 0;
            }
            // Add word to final label text
            lineWrappedLabel.append(word).append(" ");
            charSinceLastWrap += word.length() + 1;
        }
        return this.addAttribute("label", lineWrappedLabel.toString().trim());
    }

    @Override
    public iFlowGraphElement addAttribute(String attLabel, String attValue) {
        atts.add(attLabel + "=\"" + attValue + "\"");
        return this;
    }

    @Override
    public String render() {
        StringBuilder rtn = new StringBuilder();
        rtn.append(id);
        if (!atts.isEmpty()) {
            rtn.append("[");
            atts.forEach(s -> rtn.append(s));
            rtn.append("]");
        }
        return rtn.toString();
    }

    @Override
    public int getId() {
        return id;
    }
}
