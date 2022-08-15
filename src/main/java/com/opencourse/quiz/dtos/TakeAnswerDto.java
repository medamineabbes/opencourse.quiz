package com.opencourse.quiz.dtos;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TakeAnswerDto {
    @NotNull
    private Long answerId;
    @NotNull
    private boolean checked;
}
