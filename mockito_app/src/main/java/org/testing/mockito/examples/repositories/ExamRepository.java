package org.testing.mockito.examples.repositories;

import org.testing.mockito.examples.models.Exam;

import java.util.List;

public interface ExamRepository {
    List<Exam> findAll();

    Exam save(Exam exam);
}
