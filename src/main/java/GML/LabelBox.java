package GML;


public class LabelBox extends TextBox {

    public LabelBox(int id, String msg) throws GMLException {
        super(id, msg,
                "./src/main/resources/LabelBox_close.txt",
                "./src/main/resources/LabelBox_open.txt");


    }
}
