package TrollLang;

import java.util.Arrays;

public class TrollParam {

    public static boolean isValidParam(String possibleParam) {
        if (possibleParam.split("\\s++").length > 1)
            return false;
        else {
            String[] partsColonDelim = possibleParam.split(":");
            String[] partsPeriodDelim = possibleParam.split("\\.");

            return possibleParam.startsWith("Application:") &&
                    ((partsColonDelim.length == 3 && partsPeriodDelim.length == 3) ||
                     (partsColonDelim.length == 4 && partsPeriodDelim.length == 4) ||
                     (partsColonDelim.length == 7 && partsPeriodDelim.length == 7) ||
                     (partsColonDelim.length == 8 && partsPeriodDelim.length == 8));
        }
    }

    // Takes mixed input. Finds the TrollParams and maps them to 'informative' params using the TrollParam toString method
    public static String makeParamsInformative(String paramful) {
        // Map each troll param to it's pretty string equivalent
        return Arrays.stream(paramful.split(" "))
                .map(word -> {
                    if (TrollParam.isValidParam(word)) {
                        try {
                            return (new TrollParam(word).toString());
                        } catch (AngryTrollException e) {
                            System.out.println("Error parsing troll param which appeared valid: " + word);
                            return word;
                        }
                    } else
                        return word;
                })
                .reduce((acc, ele) -> acc + " " + ele)
                .get();
    }

    // Takes mixed input. Finds the TrollParams and maps them to 'pretty' params using the TrollParam getPrettyText method
    public static String makeParamsPretty(String paramful) {
        // Map each troll param to it's pretty string equivalent
        return Arrays.stream(paramful.split(" "))
                .map(word -> {
                    if (TrollParam.isValidParam(word)) {
                        try {
                            return (new TrollParam(word).getPrettyText());
                        } catch (AngryTrollException e) {
                            System.out.println("Error parsing troll param which appeared valid: " + word);
                            return word;
                        }
                    } else
                        return word;
                })
                .reduce((acc, ele) -> acc + " " + ele)
                .get();
    }

    private String fullText;
    private ConnectionType cxnType;
    private IOType ioType;
    private boolean isHidden;
    private String prettyText;

    // Example Input:
    // Application:Comp_Name.ModBUS:Modbus_RTU_slave.DO:_0540_Process_Flag_B.Value
    // Application:Project_Name.Stream:_0320_Specific_Stream_XYZ_B.enabled
    public TrollParam(String paramText) throws AngryTrollException {
        this.fullText = paramText;
        String[] param_split = paramText.split(":");
        if (param_split.length == 4) {
            setConnectionType(param_split[2]);
            setIOType(param_split[2]);
            setPrettyText(param_split[3]);
        } else {
            this.cxnType = ConnectionType.INTERNAL;
            this.ioType = IOType.Internal;
            setPrettyText(param_split[2]);
        }
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
    private void setIOType(String uglyText) throws AngryTrollException {
        String type;
        try {
            type = uglyText.split("\\.")[1];
        } catch (Exception e) {
            throw new AngryTrollException("TrollParam::setIOType - Couldn't parse IOType from: " + uglyText);
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
    // _0540_Process_Flag_B.Value
    // _hidden_Process_Flag
    private void setPrettyText(String uglyText) {
        uglyText = uglyText.substring(0, uglyText.lastIndexOf('.'));
        String[] ug_split = uglyText.split("_");
        ug_split = Arrays.stream(ug_split)
                    .filter((ele)-> !ele.isEmpty())
                    .toArray(String[]::new);

        this.isHidden = ug_split[0].equalsIgnoreCase("hidden");

        if (!isHidden) {
            // This is a little hack-y (and not in a good way)
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
        return (this.cxnType.toString().isEmpty() ? "" : this.cxnType.toString() + ".") +
               (this.ioType.toString().isEmpty()  ? "" : this.ioType.toString()  + ":") +
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
