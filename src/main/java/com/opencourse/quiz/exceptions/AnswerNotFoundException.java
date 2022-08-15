package com.opencourse.quiz.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AnswerNotFoundException extends RuntimeException{
    public AnswerNotFoundException(String msg){
        super(msg);
    }
    public AnswerNotFoundException(Long id){
        super("answer with id : " + id + " not found");
    }
}
