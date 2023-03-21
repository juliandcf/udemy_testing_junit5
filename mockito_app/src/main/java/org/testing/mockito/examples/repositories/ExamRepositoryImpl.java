package org.testing.mockito.examples.repositories;

import org.testing.mockito.examples.models.Exam;
import org.testing.mockito.examples.utils.Data;

import java.util.List;

public class ExamRepositoryImpl implements ExamRepository {
    @Override
    public List<Exam> findAll() {
        System.out.println("ExamRepositoryImpl.findAll");
        return Data.EXAMS;
    }

    @Override
    public Exam save(Exam exam) {
        System.out.println("ExamRepositoryImpl.save");
        return Data.EXAM_WITHOUT_QUESTIONS;
    }
}
