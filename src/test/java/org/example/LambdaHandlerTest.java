package org.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LambdaHandlerTest {
     private LambdaHandler lambdaHandler=new LambdaHandler();

     @Mock
     Context context;
     @Mock
     LambdaLogger logger;
     @BeforeEach
     public void init(){
         when(context.getLogger()).thenReturn(logger);
         doAnswer(call->{
             System.out.println((String)call.getArgument(0));
             return null;
         }).when(logger).log(anyString());
     }
     @Test
     void handleRequest() {
         when(context.getFunctionName()).thenReturn("Romit");
        String output = lambdaHandler.handleRequest("Hello world", context);
        Assertions.assertEquals("HELLO WORLD",output);

    }
}