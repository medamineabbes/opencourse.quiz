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
import com.opencourse.quiz.entities.Question;

import com.opencourse.quiz.dtos.QuestionDto;
import com.opencourse.quiz.repos.*;

public class QuestionServiceTest {
    
    QuestionRepo repo=mock(QuestionRepo.class);
    QuizRepo qRepo=mock(QuizRepo.class);
    QuestionService service;

    Quiz quiz;
    Question question1;
    @BeforeEach
    public void init(){
        service=new QuestionService(repo, qRepo);
        question1=new Question();
        quiz=new Quiz();
        
        //quiz
        quiz.setDescription("description");
        quiz.setSectionId(1L);
        quiz.setTitle("title");
        quiz.setId(1L);
        quiz.setQuestions(List.of(question1));

        //questions
        question1.setId(1L);
        question1.setQuestion("question1");
        question1.setQuiz(quiz);
        question1.setNumberCorrectAnswers((byte) 2);

    }


    @Test
    @DisplayName("get question test")
    public void shouldReturnQuestionDto(){
        when(repo.findById(1L)).thenReturn(Optional.of(question1));
        QuestionDto qdto=service.getQuestionById(1L);

        assertEquals(question1.getQuestion(), qdto.getQuestion());
        assertEquals(question1.getId(), qdto.getId());
        assertEquals(question1.getNumberCorrectAnswers(), qdto.getNumberOfCorrectAnswers());
    }
    @Test
    @DisplayName("get question error test")
    public void shouldThrowsQuestionNotFoundException(){
        assertThrows(QuestionNotFoundException.class, ()->{
            service.getQuestionById(1L);
        });
    }
    @Test
    @DisplayName("add question Test")
    public void shouldAddQuestion(){
        when(qRepo.findById(1L)).thenReturn(Optional.of(quiz));
        QuestionDto qdto=new QuestionDto();
        qdto.setQuestion("question");
        qdto.setNumberOfCorrectAnswers((byte) 2);
        qdto.setQuizId(1L);

        service.addQuestion(qdto);

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
            service.addQuestion(qdto);
        });
    }

    @Test
    @DisplayName("should update question")
    public void updateQuestionTest(){
        QuestionDto qdto=new QuestionDto();
        qdto.setId(1L);
        qdto.setNumberOfCorrectAnswers((byte)2);
        qdto.setQuestion("question d");
        qdto.setQuizId(1L);
        when(repo.findById(1L)).thenReturn(Optional.of(question1));

        service.updateQuestion(qdto);
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
            service.updateQuestion(qdto);
        });
        
    }

    @Test
    @DisplayName("should delete question")
    public void deleteQuestiontest(){
        when(repo.findById(1L)).thenReturn(Optional.of(question1));
        service.deleteQuestionById(1L);
        verify(repo).deleteById(1L);
    }

    @Test
    @DisplayName("should throw Question not Found Exception")
    public void deleteQuestionErrorTest(){
        assertThrows(QuestionNotFoundException.class, ()->{
            service.deleteQuestionById(1L);
        });
    }

    @Test
    @DisplayName("should return QuestionDto ")
    public void getQuestionsByQuizId(){
        Quiz q=new Quiz();
        Question question1=new Question();
        Question question2=new Question();
        Question question3=new Question();
        Question question4=new Question();
        question1.setQuiz(q);
        question2.setQuiz(q);
        question3.setQuiz(q);
        question4.setQuiz(q);
        q.setQuestions(List.of(question1,question2,question3,question4));

        when(qRepo.findById(1L)).thenReturn(Optional.of(q));

        List<QuestionDto> list=service.getQuestionsByQuizId(1L);

        assertEquals(list.size(), 4);
    }

}
