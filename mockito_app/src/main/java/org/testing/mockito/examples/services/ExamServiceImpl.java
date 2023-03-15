package org.testing.mockito.examples.services;

import org.testing.mockito.examples.models.Exam;
import org.testing.mockito.examples.repositories.ExamRepository;

import java.util.Optional;

public class ExamServiceImpl implements ExamService {

    private ExamRepository examRepository;

    public ExamServiceImpl(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    @Override
    public Optional<Exam> findExamByName(String name) {
        return examRepository.findAll().stream().filter(e -> e.getName().contains(name)).findFirst();
    }
}
