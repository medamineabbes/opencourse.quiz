package com.opencourse.quiz.exceptions;

public class CustomAuthenticationException extends RuntimeException{
    public CustomAuthenticationException(){
        super("authentication error");
    }
}
