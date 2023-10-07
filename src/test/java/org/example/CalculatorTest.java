package org.example;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CalculatorTest {
    @Spy
    CalcService calcService;
    @RepeatedTest(10)
    void perform_addition() {
        Mockito.when(calcService.add(2,3)).thenReturn((5));
        Calculator calculator=new Calculator(calcService);
        assertEquals(5,calculator.perform(2,3,"+"));
        verify(calcService).add(2,3);
    }

    @RepeatedTest(10)
    void perform_subtraction() {
        Mockito.when(calcService.subtract(2,3)).thenReturn((-1));
        Calculator calculator=new Calculator(calcService);
        assertEquals(-1,calculator.perform(2,3,"subtract"));
        verify(calcService).subtract(2,3);
    }

    @RepeatedTest(10)
    void perform_multiplication() {
        Mockito.when(calcService.multiply(2,3)).thenReturn((6));
        Calculator calculator=new Calculator(calcService);
        assertEquals(6,calculator.perform(2,3,"multiply"));
        verify(calcService).multiply(2,3);
    }

    @RepeatedTest(10)
    void perform_division() {
        Mockito.when(calcService.divide(6,3)).thenReturn((2));
        Calculator calculator=new Calculator(calcService);
        assertEquals(2,calculator.perform(6,3,"divide"));
        verify(calcService).divide(6,3);
    }

}