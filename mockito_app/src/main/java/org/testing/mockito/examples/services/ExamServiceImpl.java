package org.testing.mockito.examples.services;

import org.testing.mockito.examples.models.Exam;
import org.testing.mockito.examples.repositories.ExamRepository;
import org.testing.mockito.examples.repositories.QuestionRepository;

import java.util.List;
import java.util.Optional;

public class ExamServiceImpl implements ExamService {

    private ExamRepository examRepository;

    private QuestionRepository questionRepository;

    public ExamServiceImpl(ExamRepository examRepository, QuestionRepository questionRepository) {
        this.examRepository = examRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    public Optional<Exam> findExamByName(String name) {
        return examRepository.findAll().stream().filter(e -> e.getName().contains(name)).findFirst();
    }

    @Override
    public Exam findExamByNameWithQuestions(String name) {
        Optional<Exam> exam = findExamByName(name);
        if(exam.isEmpty())
            return null;

        List<String> questions = questionRepository.findByQuestionId(exam.get().getId());
        exam.get().setQuestions(questions);
        return exam.get();
    }

    @Override
    public Exam save(Exam exam) {
        if (!exam.getQuestions().isEmpty())
            questionRepository.saveQuestions(exam.getQuestions());

        return examRepository.save(exam);
    }

}
