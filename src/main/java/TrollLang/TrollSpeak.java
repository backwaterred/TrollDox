package TrollLang;

public enum TrollSpeak {
    LOGEVENT ("LOGEVENT", "LogEvent:\n"),
    IF ("IF", " = "),
    GOTO ("GOTO", "Goto:\n"),
    LABEL ("#", ""),
    SET ("SET", "Set:\n"),
    ADD("ADD","Increment "),
    SUB("SUB","Decrement "),
    MUL("MUL","Multiply "),
    DIV("DIV","Divide "),
    FILECOPY("FILECOPY","Copy file for"),
    IFEXIST("IFEXIST","IFEXIST"),
    IFL("IFLESS"," < "),
    IFLEQ("IFLESSEQUAL"," <= "),
    IFG("IFGREATER"," > "),
    IFGEQ("IFGREATEREQUAL", " >= "),
    WAIT ("WAIT", "Wait:\n"),
    WAITFOR("WAITFOR",""),
    WAITFORL("WAITFORLESS", ""),
    WAITFORLEQ("WAITFORLESSEQUAL", ""),
    WAITFORG("WAITFORGREATER", ""),
    WAITFORGEQ("WAITFORGREATEREQUAL", ""),
    WAITFORRESULTS("WAITFORRESULTS", ""),
    EXECUTE("EXECUTE", "Scan Sample"),
    DIALOK("DIALOGOK", "Dialogue Box:\n"),
    DIALINFO("DIALOGINFO","Dialogue Box:\n"),
    DIALINFOCL("DIALOGEINFOCLOSE","Close dialogue box"),
    DIALYN("DIALOGUEYESNO", "Dialogue box Y/N:\n");

    private String displayText;
    private String commandText;

    private TrollSpeak(String command, String displayText) {
        this.commandText = command;
        this.displayText = displayText;
    }

    public String getMsgText() {
        return this.displayText;
    }

    public String getCommandText() {
        return this.commandText;
    }




}
