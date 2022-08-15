package com.opencourse.quiz.dtos;

import java.util.ArrayList;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.opencourse.quiz.entities.Answer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnswerDto {

    private Long id;

    @NotBlank(message = "answer is mandatory")
    private String answer;

    private Long questionId;

    @NotNull(message = "isCorrect must be spcified")
    private boolean correct;

    public static Answer toAnswer(AnswerDto answerDto){
        Answer answer=new Answer();
        answer.setAnswer(answerDto.getAnswer());
        answer.setId(answerDto.getId());
        answer.setIsCorrect(answerDto.isCorrect());
        answer.setQuizTakenByUser(new ArrayList<>());
        return answer;
    }
    
    public static AnswerDto toDto(Answer answer){
        AnswerDto answerDto =new AnswerDto();
        answerDto.setAnswer(answer.getAnswer());
        answerDto.setCorrect(false);
        answerDto.setId(answer.getId());
        answerDto.setQuestionId(answer.getQuestion().getId());
        return answerDto;
    }

}