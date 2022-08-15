package com.opencourse.quiz.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
public class QuizUntakableException extends RuntimeException{
    public QuizUntakableException(Long id,LocalDateTime availabilityDate){
        super("quiz with id : " + id + " cannot be taken before " + availabilityDate);
    }
}
