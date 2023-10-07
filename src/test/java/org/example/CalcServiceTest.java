package org.example;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;

class CalcServiceTest {

    @RepeatedTest(5)
    @Timeout(3)
    @Tag("Math")
    void add() {
        CalcService calcService=new CalcService();
        assertEquals(5,calcService.add(2,3));
    }
}