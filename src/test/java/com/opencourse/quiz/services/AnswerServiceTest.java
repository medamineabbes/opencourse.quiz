package com.opencourse.quiz.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.opencourse.quiz.dtos.AnswerDto;
import com.opencourse.quiz.entities.Answer;
import com.opencourse.quiz.entities.Question;
import com.opencourse.quiz.entities.Quiz;
import com.opencourse.quiz.exceptions.AnswerNotFoundException;
import com.opencourse.quiz.exceptions.QuestionNotFoundException;
import com.opencourse.quiz.externalservices.CourseService;
import com.opencourse.quiz.repos.AnswerRepo;
import com.opencourse.quiz.repos.QuestionRepo;

public class AnswerServiceTest {
    AnswerRepo answerRepo=mock(AnswerRepo.class);
    QuestionRepo questionRepo=mock(QuestionRepo.class);
    CourseService courseService=mock(CourseService.class);

    AnswerService service;

    Quiz q1,q2;
    Question qu1,qu2,qu3,qu4,qu5;
    Answer a1,a2,a3,a4,a5,a6,a7,a8,a9;

    @BeforeEach
    public void init(){
        service=new AnswerService(answerRepo,questionRepo,courseService);

        q1=new Quiz();
        q1.setId(1L);
        q1.setSectionId(1L);
        q2=new Quiz();
        q2.setId(2L);
        q2.setSectionId(2L);

        qu1=new Question();
        qu1.setId(1L);
        qu1.setNumberCorrectAnswers((byte) 1);
        qu2=new Question();
        qu2.setId(2L);
        qu2.setNumberCorrectAnswers((byte) 2);
        qu3=new Question();
        qu3.setId(3L);
        qu3.setNumberCorrectAnswers((byte) 1);
        qu4=new Question();
        qu4.setId(4L);
        qu5=new Question();
        qu5.setId(5L);


        a1=new Answer();
        a1.setId(1L);
        a1.setIsCorrect(true);
        a2=new Answer();
        a2.setId(2L);
        a2.setIsCorrect(false);
        a3=new Answer();
        a3.setId(3L);
        a3.setIsCorrect(true);
        a4=new Answer();
        a4.setId(4L);
        a4.setIsCorrect(true);
        a5=new Answer();
        a5.setId(5L);
        a5.setIsCorrect(false);
        a6=new Answer();
        a6.setId(6L);
        a6.setIsCorrect(true);
        a7=new Answer();
        a7.setId(7L);
        a8=new Answer();
        a8.setId(8L);
        a9=new Answer();
        a9.setId(9L);

        qu1.setAnswers(List.of(a1,a2));
        a1.setQuestion(qu1);
        a2.setQuestion(qu1);

        qu2.setAnswers(List.of(a3,a4));
        a3.setQuestion(qu2);
        a4.setQuestion(qu2);

        qu3.setAnswers(List.of(a5,a6));
        a5.setQuestion(qu3);
        a6.setQuestion(qu3);

        q1.setQuestions(List.of(qu1,qu2,qu3));
        qu1.setQuiz(q1);
        qu2.setQuiz(q1);
        qu3.setQuiz(q1);

        qu4.setAnswers(List.of(a7,a8));
        a7.setQuestion(qu4);
        a8.setQuestion(qu4);

        qu5.setAnswers(List.of(a9));
        a9.setQuestion(qu5);

        q2.setQuestions(List.of(qu4,qu5));
        qu4.setQuiz(q2);
        qu5.setQuiz(q2);

    }
    
    @Test
    @DisplayName("add answer test")
    public void shouldAddAnswer() throws Exception{
        when(questionRepo.findById(1L)).thenReturn(Optional.of(qu1));
        when(courseService.userCreatedSection(qu1.getQuiz().getSectionId(), 15L)).thenReturn(true);

        AnswerDto dto=new AnswerDto();
        dto.setQuestionId(1L);

        service.addAnswer(dto,15L);

        verify(answerRepo).save(any(Answer.class));
    }

    @Test
    @DisplayName("add answer error test")
    public void shouldThrowsException(){
        AnswerDto dto=new AnswerDto();
        dto.setQuestionId(1L);
        assertThrows(QuestionNotFoundException.class, ()->{
            service.addAnswer(dto,15L);
        });
    }

    @Test
    @DisplayName("get answer test")
    public void shouldReturnAnswerDto() throws Exception{
        when(answerRepo.findById(1L)).thenReturn(Optional.of(a1));
        when(courseService.userHasAccessToSection(a1.getQuestion().getQuiz().getSectionId(), 15L)).thenReturn(true);
        
        AnswerDto answerDto=service.getAnswerById(1L,15L);

        assertEquals(a1.getAnswer(),answerDto.getAnswer());
        assertEquals(a1.getId(), answerDto.getId());
        assertEquals(false, answerDto.isCorrect());
        assertEquals(a1.getQuestion().getId(), answerDto.getQuestionId());
    }

    @Test
    @DisplayName("get answer error test")
    public void shouldThrowException() throws Exception{
        assertThrows(AnswerNotFoundException.class,()->{
            service.getAnswerById(1L,15L);
        });
    }

    @Test
    @DisplayName("update answer test")
    public void shouldUpdateAnswer(){
        when(answerRepo.findById(1L)).thenReturn(Optional.of(a1));
        when(questionRepo.findById(1L)).thenReturn(Optional.of(qu1));
        when(courseService.userCreatedSection(1L, 15L)).thenReturn(true);
        AnswerDto dto=new AnswerDto();
        dto.setId(1L);
        dto.setQuestionId(1L);
        dto.setCorrect(false);
        Integer number=qu1.getNumberCorrectAnswers()-1;
        service.updateAnswer(dto,15L);

        verify(answerRepo).flush();
        verify(questionRepo).flush();
        assertEquals(qu1.getNumberCorrectAnswers(),number.byteValue());
    }


    @Test
    @DisplayName("update answer error test")
    public void shouldThrowAnswerNotFoundException(){
        AnswerDto dto=new AnswerDto();
        dto.setId(1L);
        assertThrows(AnswerNotFoundException.class, ()->{
            service.updateAnswer(dto,15L);
        });
    }

    @Test
    @DisplayName("update answer error test")
    public void shouldThrowQuestionNotFoundException(){
        when(answerRepo.findById(1L)).thenReturn(Optional.of(a1));
        when(courseService.userCreatedSection(a1.getQuestion().getQuiz().getSectionId(), 15L)).thenReturn(true);
        AnswerDto dto=new AnswerDto();
        dto.setId(1L);
        dto.setQuestionId(15L);
        assertThrows(QuestionNotFoundException.class, ()->{
            service.updateAnswer(dto,15L);
        });
    }


    @Test
    @DisplayName("sould return answerDtos")
    public void getAnswerByQuestionId(){
        when(questionRepo.findById(1L)).thenReturn(Optional.of(qu1));
        when(courseService.userHasAccessToSection(qu1.getQuiz().getSectionId(), 15L)).thenReturn(true);

        List<AnswerDto> list=service.getByQuestionId(1L,15L);

        assertEquals(list.size(), 2);
    }

    
}
