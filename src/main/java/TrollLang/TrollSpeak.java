package TrollLang;

public enum TrollSpeak {
    LOGEVENT ("LOGEVENT", "LogEvent:\n"),
    IF ("IF", ""),
    GOTO ("GOTO", "Goto:\n"),
    LABEL ("#", ""),
    SET ("SET", "Set:\n"),
    WAIT ("WAIT", "Wait:\n");


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
