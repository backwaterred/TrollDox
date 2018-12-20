package TrollElementTests;

import GML.GMLNode;
import TrollLang.TrollParam;
import TrollLang.TrollParser.ParserInput;
import TrollLang.TrollParser.TrollParser;

import java.util.Arrays;

public abstract class AbstractTrollElementTestClass {

    GMLNode element;
    TrollParser parser;
    ParserInput input;

    String[] validParams = {
            "Application:Alpac_FTNIR.ModBUS:Modbus_RTU_slave_dig.DO:_0500_B_WL_Sample_In_Progress.Value",
            "Application:Alpac_FTNIR.CanOpen:CANopen_Transducers.DI:_0100_Air_Pressure_Normally_Open.Value",
            "Application:Alpac_FTNIR.Console_Parameter:Console_Parameters.AI:_1470_Minimum_FSS2_Demin_Water_Pressure.Value",
            "Application:Alpac_FTNIR.Console_Parameter:Console_Parameters.AO:_hidden_Digester_Cell_Temperature.Value"
    };

    String[] validParamsPretty = Arrays.stream(validParams)
            .map((param) -> (new TrollParam(param)).toString())
            .toArray(String[]::new);
}
