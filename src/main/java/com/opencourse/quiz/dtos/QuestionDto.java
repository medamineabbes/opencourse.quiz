package com.opencourse.quiz.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.opencourse.quiz.entities.Question;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDto {
    
    private Long id;

    @NotBlank(message="question attribute cannot be empty")
    private String question;
    private Byte numberOfCorrectAnswers;

    @NotNull(message="quiz cannot be null")
    private Long quizId;

    public static Question toQuestion(QuestionDto questionDto){
        Question q=new Question();

        q.setId(questionDto.getId());
        q.setNumberCorrectAnswers(questionDto.getNumberOfCorrectAnswers());
        q.setQuestion(questionDto.getQuestion());
        return q;
    }

    public static QuestionDto toDto(Question q){
        QuestionDto qd=new QuestionDto();

        qd.setId(q.getId());
        qd.setNumberOfCorrectAnswers(q.getNumberCorrectAnswers());
        qd.setQuestion(q.getQuestion());
        qd.setQuizId(q.getQuiz().getId());

        return qd;
    }
}
