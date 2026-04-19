package org.task.domain;

import java.time.Instant;
import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

/**
 * Represents a task in the task manager system.
 *
 * <p>This model contains both user-provided fields (title, description) and system-generated fields
 * (id, status, createdAt) that are populated by the Lambda handler.
 */
@DynamoDbBean
@Data
public class Task {
  private String id;
  private String title;
  private String description;
  private TaskStatus status;
  private Instant createdAt;
  private Instant updatedAt;

  public Task() {}

  public Task(String title, String description) {
    this.title = title;
    this.description = description;
  }

  @DynamoDbPartitionKey
  public String getId() {
    return id;
  }

  @Override
  public String toString() {
    return "Task{"
        + "id='"
        + id
        + '\''
        + ", title='"
        + title
        + '\''
        + ", description='"
        + description
        + '\''
        + ", status="
        + status
        + ", createdAt="
        + createdAt
        + '}';
  }
}
