package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Hello world!
 *
 */
public class App 
{
    final static Logger Logger=LoggerFactory.getLogger(App.class);
    public static void main( String[] args )
    {
        CalcService calcService=new CalcService();
        Calculator calculator=new Calculator(calcService);
        Logger.info("Addition of 2 and 3 is {}",calculator.perform(2,3));
    }
}
