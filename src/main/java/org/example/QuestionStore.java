package org.example;

import lombok.NoArgsConstructor;
import org.example.entity.Question;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.*;

@NoArgsConstructor
public class QuestionStore {
    List<Question> questionList = new ArrayList<>();

    DynamoDbClient dynamoDbClient=DynamoDbClient.builder()
            .region(Region.AP_SOUTH_1)
            .credentialsProvider(ProfileCredentialsProvider.create("default"))
            .build();



    public boolean addQuestion(Question question){
        HashMap<String, AttributeValue> itemValues = new HashMap<>();
        itemValues.put("questionId", AttributeValue.builder().s(question.getQuestionId().toString()).build());
        itemValues.put("title", AttributeValue.builder().s(question.getTitle()).build());
        itemValues.put("description", AttributeValue.builder().s(question.getDescription()).build());
        itemValues.put("upVote", AttributeValue.builder().n(question.getUpVote().toString()).build());
        itemValues.put("createdDate", AttributeValue.builder().s(question.getCreatedDate().toString()).build());
        itemValues.put("updatedDate", AttributeValue.builder().s(question.getUpdatedDate().toString()).build());


        PutItemRequest request = PutItemRequest.builder()
                .tableName("question")
                .item(itemValues)
                .build();
        try {
            PutItemResponse response = dynamoDbClient.putItem(request);
            System.out.println("question was successfully updated. The request id is "+response.responseMetadata().requestId());

        } catch (ResourceNotFoundException e) {
            System.err.format("Error: The Amazon DynamoDB table \"%s\" can't be found.\n", "question");
            System.err.println("Be sure that it exists and that you've typed its name correctly!");
            System.exit(1);
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return this.questionList.add(question);

    }

    public Question getQuestion(UUID questionId){

        String s = "question";
        Map<String, String> expressionAttributesNames = new HashMap<>();
        expressionAttributesNames.put("#title", "title");
        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":titleValue", AttributeValue.builder().s("How to learn chat gpt?").build());

        QueryRequest request = QueryRequest.builder()
                .tableName(s)
                .indexName("title-index")
                .keyConditionExpression("#title = :titleValue")
                .expressionAttributeNames(expressionAttributesNames)
                .expressionAttributeValues(expressionAttributeValues)
                .build();
        QueryResponse qr=dynamoDbClient.query(request);
        qr.items().forEach(q->System.out.println(q.get("title").s()));
        return this.questionList.stream().filter(question -> question.getQuestionId().equals(questionId)).findFirst().orElse(null);
    }

    public long countQuestion(){
        return this.questionList.size();
    }
}
