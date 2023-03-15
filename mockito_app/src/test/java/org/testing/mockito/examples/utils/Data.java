package org.testing.mockito.examples.utils;

import org.testing.mockito.examples.models.Exam;

import java.util.Arrays;
import java.util.List;

public class Data {

    public static final List<Exam> EXAMS = Arrays.asList(new Exam(5l, "matematica"), new Exam(6l, "Lengua"), new Exam(7l, "Historia"));
    public static final List<String> QUESTIONS = Arrays.asList("aritmetica", "integrales", "geometria", "trigonometria", "derivadas");
}
