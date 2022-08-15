package com.opencourse.quiz.dtos;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;


import com.opencourse.quiz.entities.*;
public class QuestionDtoTest {
    Quiz quiz;
    Question q;
    Answer a1;
    Answer a2;

    AnswerDto ad1;
    AnswerDto ad2;

    QuestionDto qd;
    @Test
    public void toDtoTest(){
        Integer correctanswers=1;
        quiz=new Quiz();
        q=new Question();

        quiz.setId(2L);

        a1=new Answer();
        a1.setId(1L);
        a1.setAnswer("answer 1 ");
        a1.setIsCorrect(true);
        a1.setQuestion(q);
        
        a2=new Answer();
        a2.setId(2L);
        a2.setAnswer("answer 2");
        a2.setIsCorrect(false);
        a2.setQuestion(q);

        q.setId(1L);
        q.setNumberCorrectAnswers(correctanswers.byteValue());
        q.setQuestion("question");
        q.setQuiz(quiz);
        q.setAnswers(List.of(a1,a2));

        qd=QuestionDto.toDto(q);

        assertEquals(q.getId(),qd.getId());
        assertEquals(q.getNumberCorrectAnswers(), qd.getNumberOfCorrectAnswers());
        assertEquals(q.getQuestion(), qd.getQuestion());
    }

    @Test
    public void toQuestionTest(){
        Integer numberofcorrectanswer=1;
        qd=new QuestionDto();

        ad1=new AnswerDto();
        ad1.setAnswer("answer 1");
        ad1.setCorrect(true);
        ad1.setId(1L);
        ad1.setQuestionId(2L);

        ad2=new AnswerDto();
        ad2.setAnswer("answer 2");
        ad2.setCorrect(false);
        ad2.setId(2L);
        ad2.setQuestionId(2L);

        qd.setId(2L);
        qd.setNumberOfCorrectAnswers(numberofcorrectanswer.byteValue());
        qd.setQuestion("question");
        qd.setQuizId(1L);

        q=QuestionDto.toQuestion(qd);

        assertEquals(qd.getId(),q.getId());
        assertEquals(qd.getNumberOfCorrectAnswers(), q.getNumberCorrectAnswers());
        assertEquals(qd.getQuestion(), q.getQuestion());
    }

}
