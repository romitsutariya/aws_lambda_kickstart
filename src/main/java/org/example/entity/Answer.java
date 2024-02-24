package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Answer {
    private Long answerId;
    private String answer;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private User user;
    private List<Comment> commentList;
}
