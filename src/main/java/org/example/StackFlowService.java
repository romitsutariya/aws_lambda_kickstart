package org.example;

import org.example.QuestionStore;
import org.example.entity.Question;

import java.util.UUID;

public class StackFlowService {

    private final QuestionStore questionStore;

    public StackFlowService(QuestionStore questionStore){
        this.questionStore=questionStore;
    }
    public Boolean AskQuestion(Question question){
        return questionStore.addQuestion(question);
    }
    public Question getQuestion(UUID questionId) {return questionStore.getQuestion(questionId);}
}
