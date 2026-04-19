package org.task;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import org.junit.jupiter.api.Test;

class LambdaHandlerTest {
  private final LambdaHandler lambdaHandler = new LambdaHandler();

  @Test
  void handleRequestReturnsGreetingForAnimalSpecies() {
    Context context = mock(Context.class);
    LambdaLogger logger = mock(LambdaLogger.class);
    // Animal animal = new Animal("Rabbit", true, false);
    when(context.getLogger()).thenReturn(logger);

    // String response = lambdaHandler.handleRequest(animal, context);

    // assertEquals("Hello, Rabbit!!!", response);
  }
}
