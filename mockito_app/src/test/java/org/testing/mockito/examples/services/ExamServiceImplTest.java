package org.testing.mockito.examples.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testing.mockito.examples.models.Exam;
import org.testing.mockito.examples.repositories.ExamRepository;
import org.testing.mockito.examples.repositories.QuestionRepository;
import org.testing.mockito.examples.utils.Data;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ExamServiceImplTest {

    ExamRepository examRepository;

    QuestionRepository questionRepository;

    ExamService examService;

    @BeforeEach
    void setUp() {
        examRepository = mock(ExamRepository.class);
        questionRepository = mock(QuestionRepository.class);
        examService = new ExamServiceImpl(examRepository, questionRepository);
    }

    @Test
    void findExamByName() {

        when(examRepository.findAll()).thenReturn(Data.EXAMS);

        Optional<Exam> exam = examService.findExamByName("matematica");
        assertTrue(exam.isPresent());
        assertEquals(5l, exam.get().getId());
    }

    @Test
    void findExamByNameEmptyList() {
        when(examRepository.findAll()).thenReturn(Collections.EMPTY_LIST);

        Optional<Exam> exam = examService.findExamByName("matematica");
        assertTrue(exam.isEmpty());
    }

    @Test
    void findExamByNameWithQuestions() {
        when(examRepository.findAll()).thenReturn(Data.EXAMS);
        when(questionRepository.findByQuestionId(anyLong())).thenReturn(Data.QUESTIONS);

        Exam exam = examService.findExamByNameWithQuestions("matematica");
        assertNotNull(exam);
        assertEquals(5l, exam.getId());
        assertEquals(5, exam.getQuestions().size());
    }

    @Test
    void findExamByNameWithQuestionsVerify() {
        when(examRepository.findAll()).thenReturn(Data.EXAMS);
        when(questionRepository.findByQuestionId(anyLong())).thenReturn(Data.QUESTIONS);

        Exam exam = examService.findExamByNameWithQuestions("matematica");
        assertNotNull(exam);
        assertEquals(5l, exam.getId());
        assertEquals(5, exam.getQuestions().size());
        verify(examRepository).findAll();
        verify(questionRepository).findByQuestionId(anyLong());
    }
}