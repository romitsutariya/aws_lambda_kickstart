package org.task.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.task.domain.Task;

/** Handler for DELETE /tasks/{task_id} - Delete task. */
public class DeleteTaskHandler extends BaseHandler {

  @Override
  public APIGatewayProxyResponseEvent handle(
      APIGatewayProxyRequestEvent request, Context context) {
    try {
      APIGatewayProxyResponseEvent validationError = validateTaskId(request.getPathParameters());
      if (validationError != null) {
        return validationError;
      }

      String taskId = request.getPathParameters().get("task_id");
      Task task = dynamoService.deleteTask(taskId);
      if (task == null) {
        return errorResponse(404, "Task not found");
      }

      return successResponse("{\"message\":\"Task deleted successfully\"}");
    } catch (Exception e) {
      context.getLogger().log("Exception occurred: " + e.getMessage());
      return errorResponse(500, "Internal Server Error");
    }
  }
}
