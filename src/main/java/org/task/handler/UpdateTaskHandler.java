package org.task.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.task.domain.Task;

/** Handler for PUT /tasks/{task_id} - Update task. */
public class UpdateTaskHandler extends BaseHandler {

  @Override
  public APIGatewayProxyResponseEvent handle(APIGatewayProxyRequestEvent request, Context context) {
    context.getLogger().log("UpdateTaskHandler.handle() - Starting task update");
    try {
      APIGatewayProxyResponseEvent validationError = validateTaskId(request.getPathParameters());
      if (validationError != null) {
        context.getLogger().log("UpdateTaskHandler.handle() - Validation failed");
        return validationError;
      }

      String taskId = request.getPathParameters().get("task_id");
      context.getLogger().log("UpdateTaskHandler.handle() - Updating task with ID: " + taskId);
      Task task = mapper.readValue(request.getBody(), Task.class);
      Task updateTask = dynamoService.updateTask(taskId, task);
      context.getLogger().log("UpdateTaskHandler.handle() - Task updated successfully");
      return successResponse(mapper.writeValueAsString(updateTask));
    } catch (JsonProcessingException e) {
      context.getLogger().log("UpdateTaskHandler.handle() - JSON parsing error: " + e.getMessage());
      return errorResponse(400, "Invalid JSON format");
    } catch (IllegalArgumentException e) {
      context.getLogger().log("UpdateTaskHandler.handle() - Task not found: " + e.getMessage());
      return errorResponse(404, "Task not found");
    } catch (Exception e) {
      context.getLogger().log("UpdateTaskHandler.handle() - Unexpected error: " + e.getMessage());
      return errorResponse(500, "Internal Server Error");
    }
  }
}
