package org.example;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;
import software.amazon.awssdk.services.cloudwatchlogs.CloudWatchLogsClient;
import software.amazon.awssdk.services.cloudwatchlogs.model.InputLogEvent;
import software.amazon.awssdk.services.cloudwatchlogs.model.PutLogEventsRequest;
import software.amazon.awssdk.services.cloudwatchlogs.model.PutLogEventsResponse;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class App 
{
    final static Logger Logger=LoggerFactory.getLogger(App.class);
    public static void main(String[] args) {
        AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create("","");
        String logGroupName = "YOUR_LOG_GROUP_NAME";
        String logStreamName = "YOUR_LOG_STREAM_NAME";

        // Create CloudWatchLogs client
        CloudWatchLogsClient cloudWatchLogsClient = CloudWatchLogsClient.builder()
                .region(Region.US_EAST_1) // Replace with your desired AWS region
                .credentialsProvider(() -> awsBasicCredentials)
                .build();


        Map<String, String> attributes = new HashMap<>();
        attributes.put("Key1", "Value1");
        attributes.put("Key2", "Value2");
        // Create a log entry
        String logMessage = "This is a sample log message.";

        InputLogEvent logEvent = InputLogEvent.builder()
                .timestamp(Instant.now().toEpochMilli())
                .message(attributes.toString())
                .build();

        // Create request to put log events
        PutLogEventsRequest putLogEventsRequest = PutLogEventsRequest.builder()
                .logGroupName(logGroupName)
                .logStreamName(logStreamName)
                .logEvents(Collections.singletonList(logEvent))
                .build();

        // Put log events to CloudWatch Logs
//        PutLogEventsResponse response = cloudWatchLogsClient.putLogEvents(putLogEventsRequest);
//        IntStream.rangeClosed(1,1000).forEach(i->cloudWatchLogsClient.putLogEvents(putLogEventsRequest));
//        // Check if the request was successful
//        if (response.sdkHttpResponse().isSuccessful()) {
//            System.out.println("Log entry successfully pushed to CloudWatch.");
//        } else {
//            System.out.println("Failed to push log entry to CloudWatch. Error message: " + response.sdkHttpResponse().statusText().get());
//        }

        // Close the CloudWatchLogs client
        cloudWatchLogsClient.close();

    }

}

