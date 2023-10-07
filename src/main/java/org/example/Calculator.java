package org.example;

public class Calculator {
    private final CalcService calcService;

    public Calculator(CalcService calcService) {
        this.calcService = calcService;
    }

    public int perform(int i, int j,String operation) {

        return switch (operation) {
            case "+" -> calcService.add(i, j);
            case "subtract" -> calcService.subtract(i, j);
            case "multiply" -> calcService.multiply(i, j);
            case "divide" -> calcService.divide(i, j);
            default -> throw new IllegalArgumentException("Invalid operation: " + operation);
        };
    }

}
