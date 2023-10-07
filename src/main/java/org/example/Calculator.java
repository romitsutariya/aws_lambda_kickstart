package org.example;

public class Calculator {
    private CalcService calcService;

    public Calculator(CalcService calcService) {
        this.calcService = calcService;
    }

    public int perform(int i, int j) {
        return calcService.add(i, j) * i;
    }

}
