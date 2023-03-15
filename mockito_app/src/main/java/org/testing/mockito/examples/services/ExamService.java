package org.testing.mockito.examples.services;

import org.testing.mockito.examples.models.Exam;

import java.util.Optional;

public interface ExamService {

    Optional<Exam> findExamByName(String name);
}
