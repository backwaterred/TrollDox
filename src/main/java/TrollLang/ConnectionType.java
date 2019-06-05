package TrollLang;

public enum ConnectionType {
    MODBUS ("Modbus"),
    CANOPEN ("CanOpen"),
    CONSOLE_PARAM ("ConsoleParam");

    private String strValue;
    ConnectionType (String strValue) {
        this.strValue = strValue;
    }
    @Override
    public String toString() {
        return this.strValue;
    }
}

