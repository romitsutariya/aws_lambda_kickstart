package org.example;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class CalculatorTest {
    @Spy
    CalcService calcService;
    @RepeatedTest(1)
    void perform() {
        Mockito.when(calcService.add(2,3)).thenReturn((5));
        Calculator calculator=new Calculator(calcService);
        assertEquals(10,calculator.perform(2,3));
        verify(calcService).add(2,3);
    }
}