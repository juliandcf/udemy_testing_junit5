package org.testing.mockito.examples.repositories;

import java.util.List;

public interface QuestionRepository {

    List<String> findByQuestionId(Long id);

    void saveQuestions(List<String> questions);
}
