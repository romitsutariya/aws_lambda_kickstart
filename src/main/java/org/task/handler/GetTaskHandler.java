package org.task.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.task.domain.Task;

/** Handler for GET /tasks/{task_id} - Get task by ID. */
public class GetTaskHandler extends BaseHandler {

  @Override
  public APIGatewayProxyResponseEvent handle(APIGatewayProxyRequestEvent request, Context context) {
    context.getLogger().log("GetTaskHandler.handle() - Starting task retrieval");
    try {
      APIGatewayProxyResponseEvent validationError = validateTaskId(request.getPathParameters());
      if (validationError != null) {
        context.getLogger().log("GetTaskHandler.handle() - Validation failed");
        return validationError;
      }

      String taskId = request.getPathParameters().get("task_id");
      context.getLogger().log("GetTaskHandler.handle() - Fetching task with ID: " + taskId);
      Task task = dynamoService.getTaskById(taskId);
      context.getLogger().log("GetTaskHandler.handle() - Task retrieved successfully");
      return successResponse(mapper.writeValueAsString(task));
    } catch (IllegalArgumentException e) {
      context.getLogger().log("GetTaskHandler.handle() - Task not found: " + e.getMessage());
      return errorResponse(404, "Task not found");
    } catch (JsonProcessingException e) {
      context
          .getLogger()
          .log("GetTaskHandler.handle() - JSON serialization error: " + e.getMessage());
      return errorResponse(500, "Internal Server Error");
    } catch (Exception e) {
      context.getLogger().log("GetTaskHandler.handle() - Unexpected error: " + e.getMessage());
      return errorResponse(500, "Internal Server Error");
    }
  }
}
