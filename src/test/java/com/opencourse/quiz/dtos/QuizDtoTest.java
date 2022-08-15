package com.opencourse.quiz.dtos;
import com.opencourse.quiz.entities.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class QuizDtoTest {
    
    QuizDto qd;
    QuestionDto q1;
    QuestionDto q2;
    AnswerDto a1;
    AnswerDto a2;
    AnswerDto a3;
    AnswerDto a4;
    Quiz q;


    @Test
    public void fromDtoTest(){
        qd=new QuizDto();
        q1=new QuestionDto();
        q2=new QuestionDto();
        a1=new AnswerDto();
        a2=new AnswerDto();
        a3=new AnswerDto();
        a4=new AnswerDto();

        q1.setId(1L);
        q2.setId(2L);

        qd.setDescription("description");
        qd.setId(1L);
        qd.setSectionId(1L);
        qd.setTitle("title");
        q=QuizDto.toQuiz(qd);

        assertEquals(qd.getDescription(), q.getDescription());
        assertEquals(qd.getId(), q.getId());
        assertEquals(qd.getSectionId(), q.getSectionId());
        assertEquals(qd.getTitle(), q.getTitle());
    }

}
