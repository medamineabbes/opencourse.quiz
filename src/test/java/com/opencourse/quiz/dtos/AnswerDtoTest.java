package com.opencourse.quiz.dtos;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.opencourse.quiz.entities.Answer;
import com.opencourse.quiz.entities.Question;

public class AnswerDtoTest {
    private Answer a;
    private Question q;
    private AnswerDto ad;
    @Test
    public void toDtoTest(){
        q=new Question();
        q.setId(1L);
        a=new Answer();
        a.setAnswer("answer");
        a.setId(1L);
        a.setIsCorrect(true);
        a.setQuestion(q);
        a.setQuizTakenByUser(List.of());
        ad=AnswerDto.toDto(a);

        assertEquals(a.getAnswer(), ad.getAnswer());
        assertEquals(a.getId(),ad.getId());
        assertEquals(a.getQuestion().getId(), ad.getQuestionId());
    }

    @Test
    public void toAnswerTest(){
        ad=new AnswerDto();
        ad.setAnswer("answer");
        ad.setCorrect(true);
        ad.setId(1L);
        ad.setQuestionId(2L);
        a=AnswerDto.toAnswer(ad);

        assertEquals(ad.getAnswer(), a.getAnswer());
        assertEquals(ad.getId(), a.getId());
    }
}
