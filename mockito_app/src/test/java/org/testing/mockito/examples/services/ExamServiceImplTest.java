package org.testing.mockito.examples.services;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testing.mockito.examples.models.Exam;
import org.testing.mockito.examples.repositories.ExamRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExamServiceImplTest {

    @Test
    void findExamByName() {
        ExamRepository examRepository = mock(ExamRepository.class);
        ExamService examService = new ExamServiceImpl(examRepository);
        List<Exam> exams = Arrays.asList(new Exam(5l, "matematica"), new Exam(6l, "Lengua"), new Exam(7l, "Historia"));
        when(examRepository.findAll()).thenReturn(exams);

        Optional<Exam>  exam = examService.findExamByName("matematica");
        assertTrue(exam.isPresent());
        assertEquals(5l, exam.get().getId());
    }

    @Test
    void findExamByNameEmptyList() {
        ExamRepository examRepository = mock(ExamRepository.class);
        ExamService examService = new ExamServiceImpl(examRepository);
        List<Exam> exams = Collections.EMPTY_LIST;
        when(examRepository.findAll()).thenReturn(exams);

        Optional<Exam>  exam = examService.findExamByName("matematica");
        assertTrue(exam.isEmpty());
    }
}