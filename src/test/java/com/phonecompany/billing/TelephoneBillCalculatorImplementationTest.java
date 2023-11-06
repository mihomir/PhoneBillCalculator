package com.phonecompany.billing;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TelephoneBillCalculatorImplementationTest {

//    private static final String oneline = "420774577453,13-01-2020 18:10:15,13-01-2020 18:12:57";
    private static final String oneline = "420774577453,13-01-2020 18:10:15,13-01-2020 18:12:45";
    private static final String longCall = "420774577453,13-01-2020 18:10:15,13-01-2020 18:16:45";
    private static final String expensiveCall = "420774577453,13-01-2020 08:10:15,13-01-2020 08:14:45";

    @org.junit.jupiter.api.Test
    void testNonNull(){
        TelephoneBillCalculator telephoneBillCalculator = new TelephoneBillCalculatorImplementation();
        assertNotNull(telephoneBillCalculator.calculate(oneline));
    }
    @Test
    void testOneCall() {
        TelephoneBillCalculator telephoneBillCalculator = new TelephoneBillCalculatorImplementation();
        assertEquals(new BigDecimal("1.5"), telephoneBillCalculator.calculate(oneline));
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
    void calculate() {
    }
}