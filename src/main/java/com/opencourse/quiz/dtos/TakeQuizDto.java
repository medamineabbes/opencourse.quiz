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
public class TakeQuizDto {
    @NotNull
    private Long quizId;
    @NotEmpty
    private List<TakeQuestionDto> questions;
}
