package org.example;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class CalcServiceTest {

    private static CalcService calcService;

    @BeforeAll
    static void beforeAll() {
         calcService= new CalcService();
    }
    @RepeatedTest(5)
    @Timeout(3)
    @Tag("Math")
    void add() {
        assertEquals(5,calcService.add(2,3));
    }

    @Test
    void subtract() {
        assertEquals(-1,calcService.subtract(2,3));
    }
    @Test
    void multiply() {
        assertEquals(6,calcService.multiply(2,3));
    }
}