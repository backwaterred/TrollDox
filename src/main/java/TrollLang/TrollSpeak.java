package TrollLang;

public enum TrollSpeak {
    LOGEVENT ("LOGEVENT", "LogEvent:\n"),
    IF ("IF", ""),
    GOTO ("GOTO", "Goto:\n"),
    LABEL ("#", ""),
    SET ("SET", "Set:\n"),
    ADD("ADD",""),
    SUB("SUB",""),
    MUL("MUL",""),
    DIV("DIV",""),
    FILECOPY("FILECOPY",""),
    IFEXIST("IFEXIST",""),
    IFEQ("IFEQUALS",""),
    IFL("IFLESS",""),
    IFLEQ("IFLESSEQUAL",""),
    IFG("IFGREATER",""),
    IFGEQ("IFGREATEREQUAL", ""),
    WAIT ("WAIT", "Wait:\n"),
    WAITFOR("WAITFOR",""),
    WAITFORL("WAITFORLESS", ""),
    WAITFORLEQ("WAITFORLESSEQUAL", ""),
    WAITFORG("WAITFORGREATER", ""),
    WAITFORGEQ("WAITFORGREATEREQUAL", ""),
    WAITFORRESULTS("WAITFORRESULTS", ""),
    EXECUTE("EXECUTE", "Scan Sample"),
    DIALOK("DIALOGOK", "Dialogue Box:\n"),
    DIALINFO("DIALOGINFO",""),
    DIALINFOCL("DIALOGEINFOCLOSE",""),
    DIALYN("DIALOGUEYESNO", "");

    private String displayText;
    private String commandText;

    private TrollSpeak(String command, String displayText) {
        this.commandText = command;
        this.displayText = displayText;
    }

    public String getMsgPrefix() {
        return this.displayText;
    }

    public String getCommandText() {
        return this.commandText;
    }




}
