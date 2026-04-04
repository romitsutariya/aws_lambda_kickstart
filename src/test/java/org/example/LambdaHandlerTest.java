package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import com.amazonaws.services.lambda.runtime.Context;
import org.junit.jupiter.api.Test;

class LambdaHandlerTest {
  private final LambdaHandler lambdaHandler = new LambdaHandler();

  @Test
  void handleRequestReturnsGreetingForAnimalSpecies() {
    Context context = mock(Context.class);
    Animal animal = new Animal("Rabbit", true, false);

    String response = lambdaHandler.handleRequest(animal, context);

    assertEquals("Hello, Rabbit!!!", response);
  }
}
