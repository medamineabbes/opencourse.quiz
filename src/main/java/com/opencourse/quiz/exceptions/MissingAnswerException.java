package com.opencourse.quiz.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class MissingAnswerException extends RuntimeException{
    public MissingAnswerException(){
        super("an answer is missing");
    }
    public MissingAnswerException(Long id){
        super("answer with id : " + id + " is missing");
    }
}
