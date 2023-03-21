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

    @Captor
    ArgumentCaptor<Long> captorVariable;

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

    @Test
    void testExamWithoutQuestionsSaveAutoincrementMock() {
        //Data.EXAM_WITHOUT_QUESTIONS
        when(examRepository.save(any())).then(new Answer<Exam>() {

            Long secuenceId = 1l;

            @Override
            public Exam answer(InvocationOnMock invocationOnMock) throws Throwable {
                Exam exam = invocationOnMock.getArgument(0);
                exam.setId(secuenceId++);
                return exam;
            }
        });
        // clone objet to test increment id with answers
        Exam exam1 = examService.save((Exam) SerializationUtils.clone(Data.EXAM_WITHOUT_QUESTIONS));
        Exam exam2 = examService.save((Exam) SerializationUtils.clone(Data.EXAM_WITHOUT_QUESTIONS));

        assertNotNull(exam1.getId());
        assertNotNull(exam2.getId());
        assertEquals(1l, exam1.getId());
        assertEquals(2l, exam2.getId());
    }

    @Test
    void testIdExamNullHandlerException() {
        when(examRepository.findAll()).thenReturn(Data.EXAMS_ID_NULL);
        when(questionRepository.findByQuestionId(isNull())).thenThrow(IllegalArgumentException.class);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            examService.findExamByNameWithQuestions("matematica");
        });
        assertEquals(IllegalArgumentException.class, exception.getClass());
        verify(examRepository).findAll();
        verify(questionRepository).findByQuestionId(null);
    }

    @Test
    void testArgumentMatchers() {
        when(examRepository.findAll()).thenReturn(Data.EXAMS);
        when(questionRepository.findByQuestionId(anyLong())).thenReturn(Data.QUESTIONS);
        examService.findExamByNameWithQuestions("matematica");

        verify(examRepository).findAll();
        verify(questionRepository).findByQuestionId(argThat(arg -> arg != null && arg.equals(5l)));
    }

    @Test
    void testArgumentMatchersCustom() {
        //Descomentar para ver el error personalizado
        // when(examRepository.findAll()).thenReturn(Data.EXAMS_ID_NEGATIVE);
        when(examRepository.findAll()).thenReturn(Data.EXAMS);
        when(questionRepository.findByQuestionId(anyLong())).thenReturn(Data.QUESTIONS);
        examService.findExamByNameWithQuestions("matematica");

        verify(examRepository).findAll();
        verify(questionRepository).findByQuestionId(argThat(new MyArgsMatchers()));
    }

    public static class MyArgsMatchers implements ArgumentMatcher<Long> {

        Long argument;

        @Override
        public boolean matches(Long argument) {
            this.argument = argument;
            return argument != null && argument > 0;
        }

        @Override
        public String toString() {
            return "El id debe ser distinto de null y un valor positivo. El valor fue " + argument;
        }
    }

    @Test
    void testArgumentCaptor() {
        when(examRepository.findAll()).thenReturn(Data.EXAMS);
        examService.findExamByNameWithQuestions("matematica");

        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(questionRepository).findByQuestionId(captor.capture());

        assertEquals(5l, captor.getValue());
    }

    @Test
    void testArgumentCaptorWithAnnotation() {
        when(examRepository.findAll()).thenReturn(Data.EXAMS);
        examService.findExamByNameWithQuestions("matematica");

        verify(questionRepository).findByQuestionId(captorVariable.capture());

        assertEquals(5l, captorVariable.getValue());
    }

    @Test
    void testDoThrow() {
        doThrow((IllegalArgumentException.class)).when(questionRepository).saveQuestions(anyList());

        assertThrows(IllegalArgumentException.class, () -> {
            examService.save(Data.EXAM_WITH_QUESTIONS);
        });
    }

    @Test
    void testDoAnswer() {
        when(examRepository.findAll()).thenReturn(Data.EXAMS);
        //  when(questionRepository.findByQuestionId(anyLong())).thenReturn(Data.QUESTIONS);
        doAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            return id == 5l ? Data.QUESTIONS : Collections.EMPTY_LIST;
        }).when(questionRepository).findByQuestionId(anyLong());

        Exam exam = examService.findExamByNameWithQuestions("matematica");
        assertEquals(5, exam.getQuestions().size());
        assertEquals(5l, exam.getId());
        assertEquals("matematica", exam.getName());

        verify(questionRepository).findByQuestionId(anyLong());

    }
}