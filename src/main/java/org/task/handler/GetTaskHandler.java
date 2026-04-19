package org.task.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.task.domain.Task;

/** Handler for GET /tasks/{task_id} - Get task by ID. */
public class GetTaskHandler extends BaseHandler {

  @Override
  public APIGatewayProxyResponseEvent handle(
      APIGatewayProxyRequestEvent request, Context context) {
    try {
      APIGatewayProxyResponseEvent validationError = validateTaskId(request.getPathParameters());
      if (validationError != null) {
        return validationError;
      }

      String taskId = request.getPathParameters().get("task_id");
      Task task = dynamoService.getTaskById(taskId);
      return successResponse(mapper.writeValueAsString(task));
    } catch (IllegalArgumentException e) {
      context.getLogger().log("Task not found: " + e.getMessage());
      return errorResponse(404, "Task not found");
    } catch (JsonProcessingException e) {
      context.getLogger().log("Exception occurred while serializing task: " + e.getMessage());
      return errorResponse(500, "Internal Server Error");
    } catch (Exception e) {
      context.getLogger().log("Exception occurred: " + e.getMessage());
      return errorResponse(500, "Internal Server Error");
    }
  }
}
