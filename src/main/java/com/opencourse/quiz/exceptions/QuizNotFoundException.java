package com.opencourse.quiz.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class QuizNotFoundException extends RuntimeException{
    public QuizNotFoundException(String msg){
        super(msg);
    }

    public QuizNotFoundException(Long id){
        super("quiz with id : " + id + " not found");
    }
}
