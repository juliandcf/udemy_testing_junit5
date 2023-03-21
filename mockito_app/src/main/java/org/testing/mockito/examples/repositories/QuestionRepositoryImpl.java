package org.testing.mockito.examples.repositories;

import org.testing.mockito.examples.utils.Data;

import java.util.List;

public class QuestionRepositoryImpl implements QuestionRepository {
    @Override
    public List<String> findByQuestionId(Long id) {
        System.out.println("QuestionRepositoryImpl.findByQuestionId");
        return Data.QUESTIONS;
    }

    @Override
    public void saveQuestions(List<String> questions) {
        System.out.println("QuestionRepositoryImpl.saveQuestions");
    }
}
