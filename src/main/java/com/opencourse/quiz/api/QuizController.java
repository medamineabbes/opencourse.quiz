package com.opencourse.quiz.api;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.opencourse.quiz.dtos.QuizDto;
import com.opencourse.quiz.dtos.TakeQuizDto;
import com.opencourse.quiz.dtos.verifyQuizDto;
import com.opencourse.quiz.services.QuizService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/quiz")
@AllArgsConstructor
public class QuizController {
    private QuizService service;
    
    @GetMapping("/{id}")
    public ResponseEntity<QuizDto> getQuizById(@PathVariable(required = true) Long id) {
        return ResponseEntity.ok(service.getQuiz(id));
    }

    @PostMapping
    public ResponseEntity<Long> addQuiz( @Valid @RequestBody(required = true) QuizDto quizDto) {    
        return ResponseEntity.ok(service.addQuiz(quizDto));
    }
    
    @PutMapping
    public void updateQuiz(@Valid @RequestBody(required = true) QuizDto quizDto){
        service.updateQuiz(quizDto);
    }

    @DeleteMapping("/{id}")
    public void deleteQuizById(@PathVariable(required = true) Long id){
        service.deleteQuiz(id);
    }

    @PostMapping("/take")
    public boolean takeQuiz(@Valid @RequestBody(required = true) TakeQuizDto takeQuizDto){
        return service.takeQuiz(takeQuizDto);
    }

    @PostMapping("/verif")
    public boolean areQuizsPassed(@Valid @RequestBody(required = true) verifyQuizDto verifyQuizDto){
        return service.quizArePassed(verifyQuizDto.getSectionIds(), verifyQuizDto.getUserId());
    }

}
