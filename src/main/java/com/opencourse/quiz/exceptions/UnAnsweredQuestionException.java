package com.opencourse.quiz.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class UnAnsweredQuestionException extends RuntimeException{
    public UnAnsweredQuestionException(Long id){
        super("question with id : " + id + " is not answered");
    }
}
