package org.task.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.task.domain.Task;

/** Handler for DELETE /tasks/{task_id} - Delete task. */
public class DeleteTaskHandler extends BaseHandler {

  @Override
  public APIGatewayProxyResponseEvent handle(APIGatewayProxyRequestEvent request, Context context) {
    context.getLogger().log("DeleteTaskHandler.handle() - Starting task deletion");
    try {
      APIGatewayProxyResponseEvent validationError = validateTaskId(request.getPathParameters());
      if (validationError != null) {
        context.getLogger().log("DeleteTaskHandler.handle() - Validation failed");
        return validationError;
      }

      String taskId = request.getPathParameters().get("task_id");
      context.getLogger().log("DeleteTaskHandler.handle() - Deleting task with ID: " + taskId);
      Task task = dynamoService.deleteTask(taskId);
      if (task == null) {
        context.getLogger().log("DeleteTaskHandler.handle() - Task not found");
        return errorResponse(404, "Task not found");
      }

      context.getLogger().log("DeleteTaskHandler.handle() - Task deleted successfully");
      return successResponse("{\"message\":\"Task deleted successfully\"}");
    } catch (Exception e) {
      context.getLogger().log("DeleteTaskHandler.handle() - Unexpected error: " + e.getMessage());
      return errorResponse(500, "Internal Server Error");
    }
  }
}
