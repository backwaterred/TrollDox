package Tests;

import FlowGraph.FlowGraph;
import FlowGraph.AbstractFlowGraphElement;
import TrollLang.TrollParam;
import TrollLang.TrollParser.TrollParser;
import TrollLang.TrollSpeak;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractTestRunner {

    protected TrollParser parser;
    protected FlowGraph g;
    protected static final String TEST_PARAM = TrollParam.makeParamsPretty("Application:Cust_System.CanOpen:CANopen_Transducers.DI:_0100_Air_Pressure_Normally_Open.Value");


}
