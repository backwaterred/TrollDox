import FlowGraph.FlowGraph;
import TrollLang.AngryTrollException;
import TrollLang.TrollParser.FileInput;
import TrollLang.TrollParser.TrollParser;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class TrollDoxLauncher {

    public static void main(String[] args) {
        System.out.println("Starting New TrollDox Instance");
        String inFilePath = args[0];
        String outFilePath = args[0].substring(0,inFilePath.lastIndexOf('.'))+".gv";
        System.out.println("Processing"+inFilePath+"->"+outFilePath);

        try {
            TrollParser tp = new TrollParser (new FileInput(inFilePath), "Test Graph", "Config: Year - Month - Day");
            FlowGraph graph = tp.parse();
            FileOutputStream fileOut = new FileOutputStream(outFilePath);
            fileOut.write(graph.render().getBytes());

        } catch (AngryTrollException e) {
            System.out.println("Error parsing " + inFilePath);
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            System.out.println("Error opening " + outFilePath);
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

