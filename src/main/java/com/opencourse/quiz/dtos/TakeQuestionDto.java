package com.opencourse.quiz.dtos;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TakeQuestionDto {
    @NotNull
    private Long questionId;
    @NotEmpty
    private List<TakeAnswerDto> answers;
}
