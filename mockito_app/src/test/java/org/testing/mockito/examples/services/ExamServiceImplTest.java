package org.testing.mockito.examples.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testing.mockito.examples.models.Exam;
import org.testing.mockito.examples.repositories.ExamRepository;
import org.testing.mockito.examples.repositories.QuestionRepository;
import org.testing.mockito.examples.utils.Data;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExamServiceImplTest {

    @Mock
    ExamRepository examRepository;

    @Mock
    QuestionRepository questionRepository;

    @InjectMocks
    ExamServiceImpl examService;

    @Test
    void testFindExamByName() {

        when(examRepository.findAll()).thenReturn(Data.EXAMS);

        Optional<Exam> exam = examService.findExamByName("matematica");
        assertTrue(exam.isPresent());
        assertEquals(5l, exam.get().getId());
    }

    @Test
    void testFindExamByNameEmptyList() {
        when(examRepository.findAll()).thenReturn(Collections.EMPTY_LIST);

        Optional<Exam> exam = examService.findExamByName("matematica");
        assertTrue(exam.isEmpty());
    }

    @Test
    void testFindExamByNameWithQuestions() {
        when(examRepository.findAll()).thenReturn(Data.EXAMS);
        when(questionRepository.findByQuestionId(anyLong())).thenReturn(Data.QUESTIONS);

        Exam exam = examService.findExamByNameWithQuestions("matematica");
        assertNotNull(exam);
        assertEquals(5l, exam.getId());
        assertEquals(5, exam.getQuestions().size());
    }

    @Test
    void testFindExamByNameWithQuestionsVerify() {
        when(examRepository.findAll()).thenReturn(Data.EXAMS);
        when(questionRepository.findByQuestionId(anyLong())).thenReturn(Data.QUESTIONS);

        Exam exam = examService.findExamByNameWithQuestions("matematica");
        assertNotNull(exam);
        assertEquals(5l, exam.getId());
        assertEquals(5, exam.getQuestions().size());
        verify(examRepository).findAll();
        verify(questionRepository).findByQuestionId(anyLong());
    }

    @Test
    void testExamWithQuestionsSave() {
        when(examRepository.save(any())).thenReturn(Data.EXAM_WITHOUT_QUESTIONS);
        Exam exam = examService.save(Data.EXAM_WITH_QUESTIONS);
        assertNotNull(exam.getId());
        assertEquals(8l, exam.getId());
        assertEquals("fisica", exam.getName());
        verify(examRepository).save(any(Exam.class));
        verify(questionRepository).saveQuestions(anyList());
    }

    @Test
    void testExamWithoutQuestionsSave() {
        when(examRepository.save(any())).thenReturn(Data.EXAM_WITHOUT_QUESTIONS);
        Exam exam = examService.save(Data.EXAM_WITHOUT_QUESTIONS);
        assertNotNull(exam.getId());
        assertEquals(8l, exam.getId());
        assertEquals("fisica", exam.getName());
        verify(examRepository).save(any(Exam.class));
        verify(questionRepository, never()).saveQuestions(anyList());
    }
}