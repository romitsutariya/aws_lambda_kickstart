package org.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class LambdaHandler implements RequestHandler<Object, String> {
  @Override
  public String handleRequest(Object name, Context context) {
    return "Hello, %s!!!".formatted(name);
  }
}
