package org.task;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import java.util.Map;
import org.task.handler.CreateTaskHandler;
import org.task.handler.DeleteTaskHandler;
import org.task.handler.GetTaskHandler;
import org.task.handler.ListTasksHandler;
import org.task.handler.UpdateTaskHandler;

/**
 * AWS Lambda handler that routes requests to appropriate task handlers.
 *
 * <p>This is the main entry point for the Lambda function. It routes incoming API Gateway requests
 * to specialized handlers based on HTTP method and path parameters.
 */
public class LambdaHandler
    implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  private static final Map<String, String> JSON_HEADERS =
      Map.of("Content-Type", "application/json");

  private final CreateTaskHandler createTaskHandler = new CreateTaskHandler();
  private final GetTaskHandler getTaskHandler = new GetTaskHandler();
  private final ListTasksHandler listTasksHandler = new ListTasksHandler();
  private final UpdateTaskHandler updateTaskHandler = new UpdateTaskHandler();
  private final DeleteTaskHandler deleteTaskHandler = new DeleteTaskHandler();

  /**
   * Routes incoming API Gateway requests to appropriate handlers.
   *
   * @param request the API Gateway proxy request event
   * @param context the Lambda execution context providing runtime information
   * @return API Gateway proxy response event
   */
  @Override
  public APIGatewayProxyResponseEvent handleRequest(
      APIGatewayProxyRequestEvent request, Context context) {
    try {
      context
          .getLogger()
          .log("Request received: " + request.getHttpMethod() + " " + request.getPath());

      String httpMethod = request.getHttpMethod();
      Map<String, String> pathParams = request.getPathParameters();

      if ("POST".equals(httpMethod)) {
        return createTaskHandler.handle(request, context);
      } else if ("GET".equals(httpMethod)) {
        if (pathParams == null || !pathParams.containsKey("task_id")) {
          return listTasksHandler.handle(request, context);
        } else {
          return getTaskHandler.handle(request, context);
        }
      } else if ("PUT".equals(httpMethod)) {
        return updateTaskHandler.handle(request, context);
      } else if ("DELETE".equals(httpMethod)) {
        return deleteTaskHandler.handle(request, context);
      }

      return errorResponse(405, "Method Not Allowed");
    } catch (Exception e) {
      context.getLogger().log("Unexpected error: " + e.getMessage());
      return errorResponse(500, "Internal Server Error");
    }
  }

  private APIGatewayProxyResponseEvent errorResponse(int statusCode, String message) {
    return new APIGatewayProxyResponseEvent()
        .withStatusCode(statusCode)
        .withHeaders(JSON_HEADERS)
        .withBody(String.format("{\"error\":\"%s\"}", message));
  }
}
