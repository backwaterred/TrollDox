import Doc.Doc;
import TrollLang.TrollParser.FileInput;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class TrollDoxLauncher {

    public static void main(String[] args) {
        System.out.println("Starting New TrollDox Instance");
        // Todo: Prototype code below; change this.
        BufferedOutputStream outStream = null;
        try {
            Doc doc = new Doc(
                    new FileInput("./src/test/resources/OneHundredLogEvents.txt"));
            outStream = new BufferedOutputStream(new FileOutputStream(new File("./out/newout.graphml")));
            outStream.write(doc.render().getBytes());
            if (outStream != null) {
                outStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

