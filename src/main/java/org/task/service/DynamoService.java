package org.task.service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.task.domain.IdGenerator;
import org.task.domain.Task;
import org.task.domain.TaskStatus;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

/** Service class for DynamoDB operations. */
public class DynamoService {

  private static final String TABLE_NAME = "tasks";
  private static final DynamoDbClient ddbClient = DynamoDbClient.create();
  private static final DynamoDbEnhancedClient enhancedClient =
      DynamoDbEnhancedClient.builder().dynamoDbClient(ddbClient).build();
  private static final TableSchema<Task> taskTableSchema = TableSchema.fromBean(Task.class);

  private final IdGenerator idGenerator;

  private DynamoDbTable<Task> getTable() {
    return enhancedClient.table(TABLE_NAME, taskTableSchema);
  }

  /** Constructor that initializes DynamoDB service. */
  public DynamoService(IdGenerator idGenerator) {
    this.idGenerator = idGenerator;
  }

  /**
   * Saves a task to DynamoDB.
   *
   * @param task the task to save
   * @return task after the save to DynamoDB table
   */
  public Task saveTask(Task task) {
    task.setId(idGenerator.getKey());
    task.setCreatedAt(Instant.now());
    task.setStatus(TaskStatus.PENDING);
    DynamoDbTable<Task> table = getTable();
    table.putItem(task);
    return task;
  }

  /**
   * Retrieves tasks from DynamoDB with pagination.
   *
   * @param limit maximum number of items to return per page
   * @param lastEvaluatedKey pagination token from previous request (null for first page)
   * @return paginated result containing tasks and next page token
   */
  public TaskPage getTasks(int limit, Map<String, AttributeValue> lastEvaluatedKey) {
    DynamoDbTable<Task> table = getTable();

    ScanEnhancedRequest.Builder requestBuilder = ScanEnhancedRequest.builder().limit(limit);

    if (lastEvaluatedKey != null && !lastEvaluatedKey.isEmpty()) {
      requestBuilder.exclusiveStartKey(lastEvaluatedKey);
    }

    Page<Task> page = table.scan(requestBuilder.build()).iterator().next();

    return new TaskPage(
        page.items(),
        page.lastEvaluatedKey(),
        page.lastEvaluatedKey() != null && !page.lastEvaluatedKey().isEmpty());
  }

  /** Paginated response containing tasks and pagination metadata. */
  public record TaskPage(
      List<Task> tasks, Map<String, AttributeValue> lastEvaluatedKey, boolean hasMore) {

    /**
     * Returns simplified page token (task id) for next page.
     *
     * @return next page token or null if no more pages
     */
    public String getNextPageToken() {
      if (lastEvaluatedKey == null || lastEvaluatedKey.isEmpty()) {
        return null;
      }
      return lastEvaluatedKey.get("id").s();
    }
  }

  public Task deleteTask(String id) {
    DynamoDbTable<Task> table = getTable();
    return table.deleteItem(Key.builder().partitionValue(id).build());
  }

  /**
   * Updates an existing task in DynamoDB.
   *
   * @param id the ID of the task to update
   * @param updates the task object containing fields to update
   * @return the updated task
   * @throws IllegalArgumentException if task is not found
   */
  public Task updateTask(String id, Task updates) {
    DynamoDbTable<Task> table = getTable();
    Task existing = table.getItem(Key.builder().partitionValue(id).build());

    if (existing == null) {
      throw new IllegalArgumentException("Task not found: " + id);
    }
    if (updates.getTitle() != null) {
      existing.setTitle(updates.getTitle());
    }
    if (updates.getDescription() != null) {
      existing.setDescription(updates.getDescription());
    }
    if (updates.getStatus() != null) {
      existing.setStatus(updates.getStatus());
    }
    existing.setUpdatedAt(Instant.now());

    table.updateItem(existing);
    return existing;
  }

  /**
   * Retrieves a task by its ID from DynamoDB.
   *
   * @param id the ID of the task to retrieve
   * @return the task if found
   * @throws IllegalArgumentException if task is not found
   */
  public Task getTaskById(String id) {
    DynamoDbTable<Task> table = getTable();
    Task existingTask = table.getItem(Key.builder().partitionValue(id).build());
    if (existingTask == null) {
      throw new IllegalArgumentException("Task not found: " + id);
    }
    return existingTask;
  }
}
