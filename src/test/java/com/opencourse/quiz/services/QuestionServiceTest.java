package com.opencourse.quiz.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.opencourse.quiz.entities.Quiz;
import com.opencourse.quiz.exceptions.QuestionNotFoundException;
import com.opencourse.quiz.exceptions.QuizNotFoundException;
import com.opencourse.quiz.externalservices.CourseService;
import com.opencourse.quiz.entities.Question;
import com.opencourse.quiz.entities.Answer;

import com.opencourse.quiz.dtos.QuestionDto;
import com.opencourse.quiz.repos.*;

public class QuestionServiceTest {
    
    QuestionRepo repo=mock(QuestionRepo.class);
    QuizRepo qRepo=mock(QuizRepo.class);
    CourseService courseService=mock(CourseService.class);
    QuestionService service;

    Quiz q1,q2;
    Question qu1,qu2,qu3,qu4,qu5;
    Answer a1,a2,a3,a4,a5,a6,a7,a8,a9;
    @BeforeEach
    public void init(){
        service=new QuestionService(repo, qRepo,courseService);
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
    @DisplayName("get question test")
    public void shouldReturnQuestionDto(){
        when(repo.findById(1L)).thenReturn(Optional.of(qu1));
        when(courseService.userHasAccessToSection(qu1.getQuiz().getSectionId(), 15L)).thenReturn(true);
        QuestionDto qdto=service.getQuestionById(1L,15L);

        assertEquals(qu1.getQuestion(), qdto.getQuestion());
        assertEquals(qu1.getId(), qdto.getId());
        assertEquals(qu1.getNumberCorrectAnswers(), qdto.getNumberOfCorrectAnswers());
    }

    @Test
    @DisplayName("get question error test")
    public void shouldThrowsQuestionNotFoundException(){
        assertThrows(QuestionNotFoundException.class, ()->{
            service.getQuestionById(1L,15L);
        });
    }
    
    @Test
    @DisplayName("add question Test")
    public void shouldAddQuestion(){
        when(qRepo.findById(1L)).thenReturn(Optional.of(q1));
        when(courseService.userCreatedSection(q1.getSectionId(), 15L)).thenReturn(true);

        QuestionDto qdto=new QuestionDto();
        qdto.setQuestion("question");
        qdto.setNumberOfCorrectAnswers((byte) 2);
        qdto.setQuizId(1L);

        service.addQuestion(qdto,15L);

        Question q=QuestionDto.toQuestion(qdto);
        q.setAnswers(List.of());

        verify(repo).save(Mockito.any(Question.class));
    }

    @Test
    @DisplayName("should Throw Quiz Not Found Exception ")
    public void addQuestionErrorTest(){
        QuestionDto qdto=new QuestionDto();
        qdto.setNumberOfCorrectAnswers((byte)2);
        qdto.setQuestion("question");
        qdto.setQuizId(1L);
        assertThrows(QuizNotFoundException.class,()->{
            service.addQuestion(qdto,15L);
        });
    }

    @Test
    @DisplayName("should update question")
    public void updateQuestionTest(){
        when(repo.findById(1L)).thenReturn(Optional.of(qu1));
        when(courseService.userCreatedSection(qu1.getQuiz().getSectionId(), 15L)).thenReturn(true);

        QuestionDto qdto=new QuestionDto();
        qdto.setId(1L);
        qdto.setNumberOfCorrectAnswers((byte)2);
        qdto.setQuestion("question d");
        qdto.setQuizId(1L);
        
        service.updateQuestion(qdto,15L);
        verify(repo).flush();

    }

    @Test
    @DisplayName("should Throw Question Not Found Exception")
    public void updateQuestionErrorTest(){
        QuestionDto qdto=new QuestionDto();
        qdto.setId(1L);
        qdto.setNumberOfCorrectAnswers((byte)2);
        qdto.setQuestion("question d");
        qdto.setQuizId(1L);
        assertThrows(QuestionNotFoundException.class, ()->{
            service.updateQuestion(qdto,15L);
        });
        
    }

    @Test
    @DisplayName("should delete question")
    public void deleteQuestiontest(){
        when(repo.findById(1L)).thenReturn(Optional.of(qu1));
        when(courseService.userCreatedSection(qu1.getQuiz().getSectionId(), 15L)).thenReturn(true);
        service.deleteQuestionById(1L,15L);
        verify(repo).deleteById(1L);
    }

    @Test
    @DisplayName("should throw Question not Found Exception")
    public void deleteQuestionErrorTest(){
        assertThrows(QuestionNotFoundException.class, ()->{
            service.deleteQuestionById(1L,15L);
        });
    }

    @Test
    @DisplayName("should return QuestionDto ")
    public void getQuestionsByQuizId(){
        when(qRepo.findById(1L)).thenReturn(Optional.of(q1));
        when(courseService.userHasAccessToSection(q1.getSectionId(), 15L)).thenReturn(true);

        List<QuestionDto> list=service.getQuestionsByQuizId(1L,15L);

        assertEquals(list.size(), 3);
    }

}
