package com.opencourse.quiz.dtos;

import java.util.List;

import lombok.Data;

@Data
public class verifyQuizDto {
    private List<Long> sectionIds;
    private Long userId;
}
