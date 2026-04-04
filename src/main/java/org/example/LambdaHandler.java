package org.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

/**
 * AWS Lambda handler that processes incoming requests and returns a greeting message.
 *
 * <p>This handler implements the RequestHandler interface to handle Lambda function invocations. It
 * accepts any object as input and formats it into a greeting string.
 */
public class LambdaHandler implements RequestHandler<Animal, String> {
  /**
   * Handles the Lambda function request by creating a personalized greeting.
   *
   * @param animal the input object to be included in the greeting message
   * @param context the Lambda execution context providing runtime information
   * @return a formatted greeting string with the provided name
   */
  @Override
  public String handleRequest(Animal animal, Context context) {
    return "Hello, %s!!!".formatted(animal.species());
  }
}
