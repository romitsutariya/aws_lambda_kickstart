package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Question {
    private UUID questionId;
    private String title;
    private String description;
    private Long upVote;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private User user;
    private List<Answer> answersList;
    private List<Comment> commentList;
}
