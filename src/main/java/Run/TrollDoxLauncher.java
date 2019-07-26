package Run;

import FlowGraph.FlowGraph;
import TrollLang.AngryTrollException;
import TrollLang.TrollParser.FileInput;
import TrollLang.TrollParser.TrollParser;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class TrollDoxLauncher {

    public static void main(String[] args) {
        System.out.println("Starting New TrollDox Instance");

        String inFilePath = "", outFilePath = "", dateString = "<config-date>";

        try {
            inFilePath = args[0];
            outFilePath = inFilePath.substring(0, inFilePath.lastIndexOf('.')) + ".gv";
            dateString = args[1];

            TrollParser tp = new TrollParser (new FileInput(inFilePath), inFilePath.substring(0, inFilePath.lastIndexOf('.')),"Config:"+dateString);
            FlowGraph graph = tp.parse();
            FileOutputStream fileOut = new FileOutputStream(outFilePath);
            fileOut.write(graph.render().getBytes());

        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Error: Could not find file extension in "+inFilePath);
            e.printStackTrace();
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

