package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CalcService {
       private static final Logger logger = LoggerFactory.getLogger(CalcService.class);
    public int add(int i, int j) {
        logger.info("Adding {} and {}",i,j);
        return i + j;
    }
}
