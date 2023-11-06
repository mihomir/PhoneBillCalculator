package com.phonecompany.billing;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TelephoneBillCalculatorImplementationTest {

    //    private static final String oneline = "420774577453,13-01-2020 18:10:15,13-01-2020 18:12:57";
    private static final String oneLine = "420774577453,13-01-2020 18:10:15,13-01-2020 18:12:45";

    private static final String oneCheapCall = """
            420774577453,13-01-2020 18:10:15,13-01-2020 18:12:45
            420774577454,13-01-2020 18:10:15,13-01-2020 18:12:45
            420774577454,13-01-2020 18:10:15,13-01-2020 18:12:45
            """;

    private static final String exactlyTwoMinutes = """
            420774577453,13-01-2020 08:10:15,13-01-2020 08:12:15
            420774577454,13-01-2020 18:10:15,13-01-2020 18:12:45
            420774577454,13-01-2020 18:10:15,13-01-2020 18:12:45
            """;
    private static final String startedThirdMinute = """
            420774577453,13-01-2020 08:10:15,13-01-2020 08:12:16
            420774577454,13-01-2020 18:10:15,13-01-2020 18:12:45
            420774577454,13-01-2020 18:10:15,13-01-2020 18:12:45
            """;
    private static final String longCall = """
            420774577453,13-01-2020 18:10:15,13-01-2020 18:16:45
            420774577454,13-01-2020 18:10:15,13-01-2020 18:12:45
            420774577454,13-01-2020 18:10:15,13-01-2020 18:12:45
            """;

    private static final String expensiveCall = """
            420774577453,13-01-2020 08:10:15,13-01-2020 08:14:45
            420774577454,13-01-2020 18:10:15,13-01-2020 18:12:45
            420774577454,13-01-2020 18:10:15,13-01-2020 18:12:45
            """;

    private static final String exampleList = """
            420774577453,13-01-2020 18:10:15,13-01-2020 18:12:57
            420776562353,18-01-2020 08:59:20,18-01-2020 09:10:00
            """;

    private static final String freeCalls = """
            420774577453,13-01-2020 18:10:15,13-01-2020 18:16:45
            420774577453,13-01-2020 18:10:15,13-01-2020 18:16:45
            420774577453,13-01-2020 18:10:15,13-01-2020 18:16:45
            420774577453,13-01-2020 18:10:15,13-01-2020 18:16:45
            """;

    private static final String overlapExpensiveStart = """
            420123577453,13-01-2020 07:58:15,13-01-2020 08:13:15
            420774577453,13-01-2020 18:10:15,13-01-2020 18:16:45
            420774577453,13-01-2020 18:10:15,13-01-2020 18:16:45
            """;

    private static final String overlapExpensiveEnd = """
            420123577453,13-01-2020 15:58:15,13-01-2020 16:13:15
            420774577453,13-01-2020 18:10:15,13-01-2020 18:16:45
            420774577453,13-01-2020 18:10:15,13-01-2020 18:16:45
            """;

    private static final String oneCheapMinute = """
            420123577453,13-01-2020 07:59:15,13-01-2020 08:00:03
            420774577453,13-01-2020 18:10:15,13-01-2020 18:16:45
            420774577453,13-01-2020 18:10:15,13-01-2020 18:16:45
            """;


    @Test
    void testNonNull() {
        TelephoneBillCalculator telephoneBillCalculator = new TelephoneBillCalculatorImplementation();
        assertNotNull(telephoneBillCalculator.calculate(oneLine));
    }

    @Test
    void testOneCheapCall() {
        TelephoneBillCalculator telephoneBillCalculator = new TelephoneBillCalculatorImplementation();
        assertEquals(new BigDecimal("1.5"), telephoneBillCalculator.calculate(oneCheapCall));
    }

    @Test
    void testOneFreeCall() {
        TelephoneBillCalculator telephoneBillCalculator = new TelephoneBillCalculatorImplementation();
        assertEquals(new BigDecimal("0.0"), telephoneBillCalculator.calculate(oneLine));
    }

    @Test
    void testLongCall() {
        TelephoneBillCalculator telephoneBillCalculator = new TelephoneBillCalculatorImplementation();
        assertEquals(new BigDecimal("2.9"), telephoneBillCalculator.calculate(longCall));
    }

    @Test
    void testExpensiveCall() {
        TelephoneBillCalculator telephoneBillCalculator = new TelephoneBillCalculatorImplementation();
        assertEquals(new BigDecimal("5.0"), telephoneBillCalculator.calculate(expensiveCall));
    }

    @Test
    void testFreeCalls(){
        TelephoneBillCalculator telephoneBillCalculator = new TelephoneBillCalculatorImplementation();
        assertEquals(new BigDecimal("0.0"),telephoneBillCalculator.calculate(freeCalls));
    }

    @Test
    void testExample(){
        TelephoneBillCalculator telephoneBillCalculator = new TelephoneBillCalculatorImplementation();
        assertEquals(new BigDecimal("1.5"),telephoneBillCalculator.calculate(exampleList));
    }

    @Test
    void testExactlyTwoMinutes() {
        TelephoneBillCalculator telephoneBillCalculator = new TelephoneBillCalculatorImplementation();
        assertEquals(new BigDecimal("2.0"),telephoneBillCalculator.calculate(exactlyTwoMinutes));
    }

    @Test
    void testStartedThirdMinute() {
        TelephoneBillCalculator telephoneBillCalculator = new TelephoneBillCalculatorImplementation();
        assertEquals(new BigDecimal("3.0"),telephoneBillCalculator.calculate(startedThirdMinute));
    }

    @Test
    void testOverlapExpensiveStart() {
        TelephoneBillCalculator telephoneBillCalculator = new TelephoneBillCalculatorImplementation();
        assertEquals(new BigDecimal("6.0"),telephoneBillCalculator.calculate(overlapExpensiveStart));
    }

    @Test
    void testOverlapExpensiveEnd() {
        TelephoneBillCalculator telephoneBillCalculator = new TelephoneBillCalculatorImplementation();
        assertEquals(new BigDecimal("5.5"),telephoneBillCalculator.calculate(overlapExpensiveEnd));
    }

    @Test
    void testOneCheapMinute() {
        TelephoneBillCalculator telephoneBillCalculator = new TelephoneBillCalculatorImplementation();
        assertEquals(new BigDecimal("0.5"),telephoneBillCalculator.calculate(oneCheapMinute));
    }



}