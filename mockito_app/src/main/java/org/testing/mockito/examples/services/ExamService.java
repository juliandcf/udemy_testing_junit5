package org.testing.mockito.examples.services;

import org.testing.mockito.examples.models.Exam;

public interface ExamService {

    Exam findExamByName(String name);
}
