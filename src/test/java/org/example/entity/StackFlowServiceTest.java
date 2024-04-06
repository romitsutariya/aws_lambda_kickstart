package org.example.entity;

import org.example.QuestionStore;
import org.example.StackFlowService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.swing.*;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
@DisplayName("StackFlow Service Unit Test")
@Disabled
class StackFlowServiceTest {
    private  StackFlowService stackFlowService;
    private  QuestionStore questionStore;

    @BeforeEach
    void setUp() {
        questionStore = new QuestionStore();
        stackFlowService= new StackFlowService(questionStore);
    }

    @Test
    void askQuestion_A() {
        Question q= new Question();
        q.setQuestionId(UUID.randomUUID());
        q.setTitle("How to learn chat gpt?");
        q.setDescription("Hello how are you, this is simple demo of mapping description of question to dynamodb");
        q.setUpVote(220L);
        q.setCreatedDate(LocalDateTime.now());
        q.setUpdatedDate(LocalDateTime.now());

        boolean result = stackFlowService.AskQuestion(q);
        assertTrue(result);
        assertEquals(questionStore.countQuestion(),1);
    }


    @Test
    void askQuestion_B() {
        UUID questionId = UUID.randomUUID();
        User user= User.builder()
                .userId(questionId)
                .username("Romit")
                .email("Romit Sutriya")
                .role(Role.ADMIN)
                .build();
        Question q= Question.builder()
                .questionId(questionId)
                .title("How can i do the sfpt using java?")
                .description("""
                        I have to linux server and wanted to send the same file on 10 linux node using java
                        how can I achieve that!
                        """)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .upVote(10L)
                .user(user)
                .build();
        stackFlowService.AskQuestion(q);
        Question question = stackFlowService.getQuestion(questionId);
        assertEquals(question.getUser().getUsername(),"Romit");
        assertEquals(question.getUpVote(),10L);
        assertEquals(questionStore.countQuestion(),1);
    }

}