package Doc;

import GML.GMLEdge;
import GML.GMLException;
import GML.GMLNode;
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
    }

    public boolean addNode(GMLNode node) {
        // Todo: check page has (printable) space!
        return currentPage.addNode(node);
    }

    public boolean addEdge(GMLEdge edge) {
        // todo: check nodes are on current page
        return currentPage.addEdge(edge);
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
