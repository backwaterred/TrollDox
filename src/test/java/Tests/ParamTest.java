package Tests;

import TrollLang.AngryTrollException;
import TrollLang.ConnectionType;
import TrollLang.IOType;
import TrollLang.TrollParam;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class ParamTest {

    final String[] validParams = {
            "Application:Cust_System.ModBUS:Modbus_RTU_slave_dig.DO:_0500_B_WL_Sample_In_Progress.Value",
            "Application:Cust_System.CanOpen:CANopen_Transducers.DI:_0100_Air_Pressure_Normally_Open.Value",
            "Application:Cust_System.Console_Parameter:Console_Parameters.AI:_1470_Minimum_FSS2_Demin_Water_Pressure.Value",
            "Application:Cust_System.Console_Parameter:Console_Parameters.AO:_hidden_Digester_Cell_Temperature.Value",
            "Application:Project_Name.Stream:_0320_Specific_Stream_XYZ_B.enabled",
            "Application:Project_Name.Stream:_0180_1A-Encabulator_Intensity_Test.Sample:Sample.Analysis:Analysis.Diagnostic:Raw.LossOfIntensity:LossOfIntensity.RemainingIntensity",
            "Application:Project_Name.Stream:_0190_1A-Encabulator.Sample:Sample.Analysis:Analysis.Prediction:Prediction.Property:_0010_EA.PlsMethod:PLS.FRatio"
    };

    final String[] invalidParams = {
            "// This is a comment",
            "\"MoDo Cooling Water Pressure is low in BC Phase 1\"",
            "#Close_All_Valves",
            "SET Application:Cust_System.CanOpen:CANopen_Transducers.DO:_0320_Some_Water.Value 0",
            "Application:Cust_System.CanOpen:CANopen_Transducers.DO:_0320_Some_Water.Value 0"
    };

    TrollParam param;

    @BeforeAll
    static void beforeAll() {}
    @BeforeEach
    void beforeEach() {}
    @AfterEach
    void afterEach() {}
    @AfterAll
    static void afterAll() {}

    @Test
    public void test() {
        assertTrue(true);
    }

    @Test
    public void testValidParams() {
        for (String p : validParams) {
            assertTrue(TrollParam.isValidParam(p));
        }
    }

    @Test
    public void testInvalidParams() {
        for (String p : invalidParams) {
            assertFalse(TrollParam.isValidParam(p));
        }
    }

    // "Application:Cust_System.ModBUS:Modbus_RTU_slave_dig.DO:_0500_B_WL_Sample_In_Progress.Value"
    @Test
    public void testModBusParam() throws AngryTrollException {
        param = new TrollParam(validParams[0]);
        assertEquals("Application:Cust_System.ModBUS:Modbus_RTU_slave_dig.DO:_0500_B_WL_Sample_In_Progress.Value",
                param.getFullText());
        assertEquals(ConnectionType.MODBUS, param.getCxnType());
        assertEquals(IOType.DO, param.getIoType());
        assertFalse(param.isHidden());

        assertEquals("Modbus.DO:B WL Sample In Progress",
                param.toString());
    }

    // "Application:Cust_System.CanOpen:CANopen_Transducers.DI:_0100_Air_Pressure_Normally_Open.Value"
    @Test
    public void testCanOpenParam() throws AngryTrollException {
        param = new TrollParam(validParams[1]);
        assertEquals("Application:Cust_System.CanOpen:CANopen_Transducers.DI:_0100_Air_Pressure_Normally_Open.Value",
                param.getFullText());
        assertEquals(ConnectionType.CANOPEN, param.getCxnType());
        assertEquals(IOType.DI, param.getIoType());
        assertFalse(param.isHidden());

        assertEquals("CanOpen.DI:Air Pressure Normally Open",
                param.toString());
    }

    // "Application:Cust_System.Console_Parameter:Console_Parameters.AI:_1470_Minimum_FSS2_Demin_Water_Pressure.Value"
    @Test
    public void testConsoleParam() throws AngryTrollException {
        param = new TrollParam(validParams[2]);
        assertEquals("Application:Cust_System.Console_Parameter:Console_Parameters.AI:_1470_Minimum_FSS2_Demin_Water_Pressure.Value",
                param.getFullText());
        assertEquals(ConnectionType.CONSOLE_PARAM, param.getCxnType());
        assertEquals(IOType.AI, param.getIoType());
        assertFalse(param.isHidden());

        assertEquals("ConsoleParam.AI:Minimum FSS2 Demin Water Pressure",
                param.toString());
    }

    // "Application:Cust_System.Console_Parameter:Console_Parameters.AO:_hidden_Digester_Cell_Temperature.Value"
    @Test
    public void testHiddenParam() throws AngryTrollException {
        param = new TrollParam(validParams[3]);
        assertEquals("Application:Cust_System.Console_Parameter:Console_Parameters.AO:_hidden_Digester_Cell_Temperature.Value",
                param.getFullText());
        assertEquals(ConnectionType.CONSOLE_PARAM, param.getCxnType());
        assertEquals(IOType.AO, param.getIoType());
        assertTrue(param.isHidden());

        assertEquals("ConsoleParam.AO:hidden Digester Cell Temperature",
                param.toString());
    }

    @Test
    public void testIncorrectlyParsedParam() throws AngryTrollException {
        param = new TrollParam(validParams[4]);
        assertEquals(validParams[4],
                param.getFullText());
        assertEquals(ConnectionType.INTERNAL, param.getCxnType());
        assertEquals(IOType.Internal, param.getIoType());
        assertFalse(param.isHidden());

        assertEquals("Internal:" + "Specific Stream XYZ B",
                param.toString());
        assertEquals("Specific Stream XYZ B",
                TrollParam.makeParamsPretty(validParams[4]));
    }

    @Test
    public void testExtraLongParam() throws AngryTrollException {
        param = new TrollParam(validParams[5]);
        assertEquals(validParams[5],
                param.getFullText());
        assertEquals(ConnectionType.INTERNAL, param.getCxnType());
        assertEquals(IOType.Internal, param.getIoType());
        assertFalse(param.isHidden());

        assertEquals("Internal:" + "1A-Encabulator Intensity Test",
                param.toString());
        assertEquals("1A-Encabulator Intensity Test",
                TrollParam.makeParamsPretty(validParams[5]));
    }
}
