package org.task.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.task.domain.Task;

/** Handler for PUT /tasks/{task_id} - Update task. */
public class UpdateTaskHandler extends BaseHandler {

  @Override
  public APIGatewayProxyResponseEvent handle(
      APIGatewayProxyRequestEvent request, Context context) {
    try {
      APIGatewayProxyResponseEvent validationError = validateTaskId(request.getPathParameters());
      if (validationError != null) {
        return validationError;
      }

      String taskId = request.getPathParameters().get("task_id");
      Task task = mapper.readValue(request.getBody(), Task.class);
      Task updateTask = dynamoService.updateTask(taskId, task);
      return successResponse(mapper.writeValueAsString(updateTask));
    } catch (JsonProcessingException e) {
      context.getLogger().log("Exception occurred while parsing json: " + e.getMessage());
      return errorResponse(400, "Invalid JSON format");
    } catch (IllegalArgumentException e) {
      context.getLogger().log("Task not found: " + e.getMessage());
      return errorResponse(404, "Task not found");
    } catch (Exception e) {
      context.getLogger().log("Exception occurred: " + e.getMessage());
      return errorResponse(500, "Internal Server Error");
    }
  }
}
