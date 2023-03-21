package org.testing.mockito.examples.services;

import org.apache.commons.lang.SerializationUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.testing.mockito.examples.models.Exam;
import org.testing.mockito.examples.repositories.ExamRepository;
import org.testing.mockito.examples.repositories.ExamRepositoryImpl;
import org.testing.mockito.examples.repositories.QuestionRepository;
import org.testing.mockito.examples.repositories.QuestionRepositoryImpl;
import org.testing.mockito.examples.utils.Data;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExamServiceImplTestSpy {


    @Spy
    ExamRepositoryImpl examRepository;

    @Spy
    QuestionRepositoryImpl questionRepository;

    @InjectMocks
    ExamServiceImpl examService;

    @Test
    void testWithSpy() {

        //Al trabajar con Spy es mejor utiliar el do en lugar del when, porque sino se llama al metodo en la definicion del when
        //when(questionRepository.findByQuestionId(anyLong())).thenReturn(Data.QUESTIONS);
        doReturn(Data.QUESTIONS).when(questionRepository).findByQuestionId(anyLong());

        Exam exam = examService.findExamByNameWithQuestions("matematica");

        assertEquals(5l, exam.getId());
        assertEquals("matematica", exam.getName());
        assertEquals(5, exam.getQuestions().size());

        verify(examRepository).findAll();
        verify(questionRepository).findByQuestionId(anyLong());
    }
}