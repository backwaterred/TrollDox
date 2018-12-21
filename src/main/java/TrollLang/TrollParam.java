package TrollLang;

import java.util.Arrays;

public class TrollParam {

    public static boolean isValidParam(String possibleParam) {
        return possibleParam.startsWith("Application:") &&
               possibleParam.split(":").length == 4 &&
                possibleParam.split("\\.").length == 4;
    }

    // Takes mixed input. Finds the TrollParams and maps them to 'informative' params using the TrollParam toString method
    public static String makeParamsInformative(String paramful) {
        // Map each troll param to it's pretty string equivalent
        return Arrays.stream(paramful.split(" "))
                .map(word -> (TrollParam.isValidParam(word)) ? (new TrollParam(word)).toString() : word)
                .reduce((acc, ele) -> acc + " " + ele)
                .get();
    }

    // Takes mixed input. Finds the TrollParams and maps them to 'pretty' params using the TrollParam getPrettyText method
    public static String makeParamsPretty(String paramful) {
        // Map each troll param to it's pretty string equivalent
        return Arrays.stream(paramful.split(" "))
                .map(word -> (TrollParam.isValidParam(word)) ? (new TrollParam(word)).getPrettyText() : word)
                .reduce((acc, ele) -> acc + " " + ele)
                .get();
    }

    private String fullText;
    private ConnectionType cxnType;
    private IOType ioType;
    private boolean isHidden;
    private String prettyText;

    // Example Input:
    // Application:Monsteras_Recaust.ModBUS:Modbus_RTU_slave.DO:_0540_Backflushing_Flag_FSS_B.Value
    public TrollParam(String paramText) {
        this.fullText = paramText;
        String[] param_split = paramText.split(":");
        setConnectionType(param_split[2]);
        setIOType(param_split[2]);
        setPrettyText(param_split[3]);
    }

    // Example Input:
    // Modbus_RTU_slave.DO
    // CANopen_Transducers.AI
    // Console_Parameters.DI
    private void setConnectionType(String uglyText) {
        if (uglyText.startsWith("Modbus")) {
            this.cxnType = ConnectionType.MODBUS;
        } else if (uglyText.startsWith("CANopen")) {
            this.cxnType = ConnectionType.CANOPEN;
        } else if (uglyText.startsWith("Console_Parameters")) {
            this.cxnType = ConnectionType.CONSOLE_PARAM;
        }
    }

    // Example Input:
    // Modbus_RTU_slave.DO
    // CANopen_Transducers.AI
    // Console_Parameters.DI
    private void setIOType(String uglyText) {
        String type = "";
        try {
            type = uglyText.split("\\.")[1];
        } catch (Exception e) {
            /* Catch array out of bounds errors */
        }

        if (type.equals("DO")) {
            this.ioType = IOType.DO;
        } else if (type.equals("DI")) {
            this.ioType = IOType.DI;
        } else if (type.equals("AO")) {
            this.ioType = IOType.AO;
        } else if (type.equals("AI")) {
            this.ioType = IOType.AI;
        }
    }

    // Example Input:
    // _0540_Backflushing_Flag_FSS_B.Value
    // _hidden_Reference_Requested_Flag
    private void setPrettyText(String uglyText) {
        uglyText = uglyText.substring(0, uglyText.lastIndexOf('.'));
        String[] ug_split = uglyText.split("_");
        ug_split = Arrays.stream(ug_split)
                    .filter((ele)-> !ele.isEmpty())
                    .toArray(String[]::new);

        this.isHidden = ug_split[0].equalsIgnoreCase("hidden");

        if (!isHidden) {
            // This is a little hack-y (but not in a good way)
            // If the first element is a number, ignore it (by stetting it to "")
            // otherwise assume it is part of the name and carry on
            try {
                Integer.parseInt(ug_split[0]);
                ug_split[0] = "";
            } catch (NumberFormatException e) {/* Carry on; nothing to see here */ }
        }

        this.prettyText = Arrays.stream(ug_split)
                          .reduce((acc, ele)-> acc + " " + ele)
                                  .get()
                                  .trim();
    }

    @Override
    public String toString() {
        return this.cxnType + "." +
                this.ioType + ":" +
                this.prettyText;
    }

    public String getFullText() {
        return fullText;
    }

    public ConnectionType getCxnType() {
        return cxnType;
    }

    public IOType getIoType() {
        return ioType;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public String getPrettyText() {
        return prettyText;
    }
}
