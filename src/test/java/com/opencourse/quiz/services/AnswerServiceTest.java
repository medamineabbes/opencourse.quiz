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

import com.opencourse.quiz.dtos.AnswerDto;
import com.opencourse.quiz.entities.Answer;
import com.opencourse.quiz.entities.Question;
import com.opencourse.quiz.exceptions.AnswerNotFoundException;
import com.opencourse.quiz.exceptions.QuestionNotFoundException;
import com.opencourse.quiz.repos.AnswerRepo;
import com.opencourse.quiz.repos.QuestionRepo;

public class AnswerServiceTest {
    AnswerRepo aRepo=mock(AnswerRepo.class);
    QuestionRepo qRepo=mock(QuestionRepo.class);

    AnswerService service;

    AnswerDto a1;
    AnswerDto a2;
    Question q;

    Answer a;

    @BeforeEach
    public void init(){
        a=new Answer();
        a1=new AnswerDto();
        a2=new AnswerDto();
        q=new Question();

        a.setAnswer("actual answer");
        a.setId(1L);
        a.setIsCorrect(false);
        a.setQuizTakenByUser(List.of());
        a.setQuestion(q);


        a1.setAnswer("answer 1 ");
        a1.setCorrect(true);
        a1.setQuestionId(1L);
        a1.setId(1L);

        a2.setAnswer("answer 2");
        a2.setCorrect(false);
        a2.setQuestionId(1L);
        a2.setId(1L);

        q.setId(1L);
        Integer nca=0;
        q.setNumberCorrectAnswers(nca.byteValue());
        q.setAnswers(List.of(a));

        service=new AnswerService(aRepo,qRepo);
    }
    
    @Test
    @DisplayName("add answer test")
    public void shouldAddAnswer() throws Exception{
        when(qRepo.findById(1L)).thenReturn(Optional.of(q));

        service.addAnswer(a1);
        Answer a=AnswerDto.toAnswer(a1);
        a.setQuestion(q);
        verify(aRepo).save(a);
        verify(qRepo).flush();
        Integer expectednum=1;
        assertEquals(expectednum.byteValue() ,q.getNumberCorrectAnswers());
    }

    @Test
    @DisplayName("add answer error test")
    public void shouldThrowsException(){
        assertThrows(QuestionNotFoundException.class, ()->{
            service.addAnswer(a1);
        });
    }

    @Test
    @DisplayName("get answer test")
    public void shouldReturnAnswerDto() throws Exception{
        when(aRepo.findById(1L)).thenReturn(Optional.of(a));

        
        AnswerDto answerDto=service.getAnswerById(1L);

        assertEquals(a.getAnswer(),answerDto.getAnswer());
        assertEquals(a.getId(), answerDto.getId());
        assertEquals(a.getIsCorrect(), answerDto.isCorrect());
        assertEquals(a.getQuestion().getId(), answerDto.getQuestionId());
    }

    @Test
    @DisplayName("get answer error test")
    public void shouldThrowException() throws Exception{
        assertThrows(AnswerNotFoundException.class,()->{
            service.getAnswerById(1L);
        });
    }

    @Test
    @DisplayName("update answer test")
    public void shouldUpdateAnswer(){
        when(aRepo.findById(1L)).thenReturn(Optional.of(a));
        when(qRepo.findById(1L)).thenReturn(Optional.of(q));
        Integer number=q.getNumberCorrectAnswers()+1;

        service.updateAnswer(a1);

        verify(aRepo).flush();
        verify(qRepo).flush();
        assertEquals(q.getNumberCorrectAnswers(),number.byteValue());
    }


    @Test
    @DisplayName("update answer error test")
    public void shouldThrowAnswerNotFoundException(){
        assertThrows(AnswerNotFoundException.class, ()->{
            service.updateAnswer(a1);
        });
    }

    @Test
    @DisplayName("update answer error test")
    public void shouldThrowQuestionNotFoundException(){
        when(aRepo.findById(1L)).thenReturn(Optional.of(a));
        assertThrows(QuestionNotFoundException.class, ()->{
            service.updateAnswer(a1);
        });
    }


    @Test
    @DisplayName("sould return answerDtos")
    public void getAnswerByQuestionId(){
        Question q=new Question();
        Answer a1,a2,a3;
        a1=new Answer();
        a2=new Answer();
        a3=new Answer();
        a1.setQuestion(q);
        a2.setQuestion(q);
        a3.setQuestion(q);
        q.setAnswers(List.of(a1,a2,a3));
        q.setId(1L);
        when(qRepo.findById(1L)).thenReturn(Optional.of(q));

        List<AnswerDto> list=service.getByQuestionId(1L);

        assertEquals(list.size(), 3);
    }
}
