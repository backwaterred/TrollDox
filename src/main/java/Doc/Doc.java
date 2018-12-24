package Doc;

import GML.GMLException;
import GML.GMLNode;
import GML.TextBox;
import TrollLang.AngryTrollException;
import TrollLang.TrollParser.ParserInput;
import TrollLang.TrollParser.TrollParser;
import Util.Util;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

public class Doc implements DocumentElement {

    private LinkedList<Page> pages;
    private Page currentPage;
    private LinkedList<GMLNode> roots;
    private StringBuilder documentBoilerPlateGML_open;
    private StringBuilder documentBoilerPlateGML_close;

    public Doc(ParserInput input) throws AngryTrollException, GMLException, IOException {
        // init document (first)
        pages = new LinkedList<>();
        roots = new LinkedList<>();
        currentPage = new Page();
        pages.add(currentPage);
        getBoilerPlateCode();

        // init parser (second)
        TrollParser parser = new TrollParser(input, this);

        // add parser-created nodes to document
        for (GMLNode r : roots) {
            this.addConnectedNodes(r);
        }
    }

    private boolean addNewElement(GMLNode e) {
        // Todo: check page has (printable) space!
        return currentPage.addElement(e);
    }

    /**
     * Add new Document Root. Each root will be used to render a graph.
     * @param root A root to start rendering from.
     * @return The updated document.
     * */
    public Doc addDocRoot(GMLNode root) {
        roots.add(root);
        return this;
    }

    private void addConnectedNodes(GMLNode root) {
        LinkedList<GMLNode> queue = new LinkedList<>(); queue.push(root);
        GMLNode currNode;
        while (!queue.isEmpty()) {
            currNode = queue.pop();
            this.addNewElement(currNode);

            for (GMLNode n : currNode.getConnectedNodes()) {
                queue.push(n);
            }
        }
    }

    private void getBoilerPlateCode() throws GMLException {

        try {
            documentBoilerPlateGML_open = Util.readFileToStringBuilder("./src/main/resources/empty_graph_open.txt");
            documentBoilerPlateGML_close = Util.readFileToStringBuilder("./src/main/resources/empty_graph_close.txt");
        } catch (Exception e) {
            throw new GMLException("Doc::getBoilerPlateCode - Failed to read boilerplate code from file");
        }
    }

    @Override
    public String render() {
        StringBuilder rtn = documentBoilerPlateGML_open;
        for (Page p : pages) {
            rtn.append(p.render());
        }
        rtn.append(documentBoilerPlateGML_close);

        return Arrays.stream(rtn.toString().split(">\\s*<"))
                .reduce((acc, ele)-> acc + ">\n<" + ele)
                .get();
    }
}
