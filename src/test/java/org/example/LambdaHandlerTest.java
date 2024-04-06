package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LambdaHandlerTest {
     private LambdaHandler lambdaHandler=new LambdaHandler();
    @Test
    void handleRequest() {
        Assertions.assertEquals("Hello, Romit!!!",lambdaHandler.handleRequest("Romit"));
    }
}