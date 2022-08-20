package com.opencourse.quiz.handlers;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.opencourse.quiz.exceptions.AnswerNotFoundException;
import com.opencourse.quiz.exceptions.MissingAnswerException;
import com.opencourse.quiz.exceptions.QuestionNotFoundException;
import com.opencourse.quiz.exceptions.QuizNotFoundException;
import com.opencourse.quiz.exceptions.QuizUntakableException;
import com.opencourse.quiz.exceptions.UnAnsweredQuestionException;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
class ApiError{
    private HttpStatus status;
    private String msg;
    private List<String> errors;
}

@RestControllerAdvice
public class CustomResponseEntityExceptionHandler {
    
    @ExceptionHandler({MethodArgumentNotValidException.class})
    protected ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex,WebRequest request){
        List<String> errors=new ArrayList<String>();
        for(FieldError error:ex.getBindingResult().getFieldErrors()){
            errors.add(error.getDefaultMessage());
        }
        ApiError apiError=new ApiError();
        apiError.setErrors(errors);
        apiError.setMsg(ex.getLocalizedMessage());
        apiError.setStatus(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        List<String> errors = new ArrayList<String>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass().getName() + " " + 
            violation.getPropertyPath() + ": " + violation.getMessage());
        }
        ApiError apiError = new ApiError();
        apiError.setErrors(errors);
        apiError.setMsg(ex.getLocalizedMessage());
        apiError.setStatus(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }
    
    @ExceptionHandler({AnswerNotFoundException.class})
    protected ResponseEntity<Object> handleAnswerNotFoundException(AnswerNotFoundException ex,WebRequest request){
        List<String> errors=new ArrayList<String>();
        ApiError apiError=new ApiError();
        apiError.setErrors(errors);
        apiError.setMsg(ex.getLocalizedMessage());
        apiError.setStatus(HttpStatus.NOT_FOUND);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({QuestionNotFoundException.class})
    protected ResponseEntity<Object> handleQuestionNotFoundException(QuestionNotFoundException ex,WebRequest request){
        List<String> errors=new ArrayList<String>();
        ApiError apiError=new ApiError();
        apiError.setErrors(errors);
        apiError.setMsg(ex.getLocalizedMessage());
        apiError.setStatus(HttpStatus.NOT_FOUND);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({QuizNotFoundException.class})
    protected ResponseEntity<Object> handleQuizNotFoundException(QuizNotFoundException ex,WebRequest request){
        List<String> errors=new ArrayList<String>();
        ApiError apiError=new ApiError();
        apiError.setErrors(errors);
        apiError.setMsg(ex.getLocalizedMessage());
        apiError.setStatus(HttpStatus.NOT_FOUND);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({MissingAnswerException.class})
    protected ResponseEntity<Object> handleMissingAnswerException(MissingAnswerException ex,WebRequest request){
        List<String> errors=new ArrayList<String>();
        ApiError apiError=new ApiError();
        apiError.setErrors(errors);
        apiError.setMsg(ex.getLocalizedMessage());
        apiError.setStatus(HttpStatus.NOT_ACCEPTABLE);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({QuizUntakableException.class})
    protected ResponseEntity<Object> handleQuizUntakableException(QuizUntakableException ex,WebRequest request){
        List<String> errors=new ArrayList<String>();
        ApiError apiError=new ApiError();
        apiError.setErrors(errors);
        apiError.setMsg(ex.getLocalizedMessage());
        apiError.setStatus(HttpStatus.METHOD_NOT_ALLOWED);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({UnAnsweredQuestionException.class})
    protected ResponseEntity<Object> handleUnAnsweredQuestionException(UnAnsweredQuestionException ex,WebRequest request){
        List<String> errors=new ArrayList<String>();
        ApiError apiError=new ApiError();
        apiError.setErrors(errors);
        apiError.setMsg(ex.getLocalizedMessage());
        apiError.setStatus(HttpStatus.NOT_ACCEPTABLE);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }


}
