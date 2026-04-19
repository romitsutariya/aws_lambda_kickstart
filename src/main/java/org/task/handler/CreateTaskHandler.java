package org.task.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.task.domain.Task;

/** Handler for POST /tasks - Create new task. */
public class CreateTaskHandler extends BaseHandler {

  @Override
  public APIGatewayProxyResponseEvent handle(APIGatewayProxyRequestEvent request, Context context) {
    context.getLogger().log("CreateTaskHandler.handle() - Starting task creation");
    try {
      Task task = mapper.readValue(request.getBody(), Task.class);
      context.getLogger().log("CreateTaskHandler.handle() - Parsed task: " + task.getTitle());
      Task saveTask = dynamoService.saveTask(task);
      context
          .getLogger()
          .log("CreateTaskHandler.handle() - Task created with ID: " + saveTask.getId());
      return successResponse(mapper.writeValueAsString(saveTask));
    } catch (JsonProcessingException ex) {
      context
          .getLogger()
          .log("CreateTaskHandler.handle() - JSON parsing error: " + ex.getMessage());
      return errorResponse(400, "Please provide valid JSON body");
    }
  }
}
