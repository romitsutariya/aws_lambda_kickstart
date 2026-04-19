package org.task.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import org.task.service.DynamoService;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

/** Handler for GET /tasks - List all tasks with pagination. */
public class ListTasksHandler extends BaseHandler {

  @Override
  public APIGatewayProxyResponseEvent handle(
      APIGatewayProxyRequestEvent request, Context context) {
    try {
      Map<String, String> queryParams = request.getQueryStringParameters();
      int limit = DEFAULT_PAGE_SIZE;
      Map<String, AttributeValue> lastEvaluatedKey = null;

      if (queryParams != null) {
        if (queryParams.containsKey("limit")) {
          limit = Integer.parseInt(queryParams.get("limit"));
        }
        if (queryParams.containsKey("nextToken")) {
          lastEvaluatedKey = decodeNextToken(queryParams.get("nextToken"));
        }
      }

      DynamoService.TaskPage taskPage = dynamoService.getTasks(limit, lastEvaluatedKey);

      Map<String, Object> response = new HashMap<>();
      response.put("tasks", taskPage.tasks());
      response.put("hasMore", taskPage.hasMore());
      if (taskPage.hasMore()) {
        response.put("nextToken", encodeNextToken(taskPage.lastEvaluatedKey()));
      }

      return successResponse(mapper.writeValueAsString(response));
    } catch (Exception ex) {
      context.getLogger().log("Exception occurred: " + ex.getMessage());
      return errorResponse(500, "Failed to retrieve tasks");
    }
  }

  private String encodeNextToken(Map<String, AttributeValue> lastEvaluatedKey) {
    try {
      if (lastEvaluatedKey == null || lastEvaluatedKey.isEmpty()) {
        return null;
      }
      Map<String, String> simpleMap = new HashMap<>();
      lastEvaluatedKey.forEach(
          (key, value) -> {
            if (value.s() != null) {
              simpleMap.put(key, value.s());
            }
          });
      String json = mapper.writeValueAsString(simpleMap);
      return Base64.getUrlEncoder().encodeToString(json.getBytes());
    } catch (Exception e) {
      return null;
    }
  }

  private Map<String, AttributeValue> decodeNextToken(String token) {
    try {
      byte[] decoded = Base64.getUrlDecoder().decode(token);
      Map<String, String> simpleMap =
          mapper.readValue(
              decoded,
              mapper.getTypeFactory().constructMapType(HashMap.class, String.class, String.class));
      Map<String, AttributeValue> attributeMap = new HashMap<>();
      simpleMap.forEach(
          (key, value) -> attributeMap.put(key, AttributeValue.builder().s(value).build()));
      return attributeMap;
    } catch (Exception e) {
      return null;
    }
  }
}
