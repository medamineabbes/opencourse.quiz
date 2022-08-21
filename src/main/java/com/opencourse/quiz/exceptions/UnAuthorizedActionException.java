package com.opencourse.quiz.exceptions;

public class UnAuthorizedActionException extends RuntimeException{
    public UnAuthorizedActionException(){
        super("unauthorised action");
    }
}
