package org.task.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.Map;
import org.task.domain.Base36IdGenerator;
import org.task.service.DynamoService;

/** Base handler providing common utilities for all request handlers. */
public abstract class BaseHandler {

  protected static final Map<String, String> JSON_HEADERS =
      Map.of("Content-Type", "application/json");
  protected static final int DEFAULT_PAGE_SIZE = 20;

  protected static final ObjectMapper mapper;
  protected static final DynamoService dynamoService;

  static {
    mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    dynamoService = new DynamoService(new Base36IdGenerator());
  }

  /**
   * Handle the request.
   *
   * @param request the API Gateway request
   * @param context the Lambda context
   * @return the API Gateway response
   */
  public abstract APIGatewayProxyResponseEvent handle(
      APIGatewayProxyRequestEvent request, Context context);

  protected static APIGatewayProxyResponseEvent errorResponse(int statusCode, String message) {
    return new APIGatewayProxyResponseEvent()
        .withStatusCode(statusCode)
        .withHeaders(JSON_HEADERS)
        .withBody(String.format("{\"error\":\"%s\"}", message));
  }

  protected static APIGatewayProxyResponseEvent successResponse(String body) {
    return new APIGatewayProxyResponseEvent()
        .withStatusCode(200)
        .withHeaders(JSON_HEADERS)
        .withBody(body);
  }

  protected static APIGatewayProxyResponseEvent validateTaskId(Map<String, String> pathParams) {
    if (pathParams == null || !pathParams.containsKey("task_id")) {
      return errorResponse(400, "Task ID is required");
    }
    String taskId = pathParams.get("task_id");
    if (taskId == null || taskId.isEmpty()) {
      return errorResponse(400, "Task ID cannot be empty");
    }
    return null;
  }
}
