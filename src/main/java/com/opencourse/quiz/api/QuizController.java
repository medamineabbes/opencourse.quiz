package com.opencourse.quiz.api;

import java.util.List;

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
    
    //only authentic users
    @GetMapping("/{id}")
    public ResponseEntity<QuizDto> getQuizById(@PathVariable(required = true) Long id) {
        Long userId=15L;
        return ResponseEntity.ok(service.getQuiz(id,userId));
    }

    //only teachers
    @PostMapping
    public ResponseEntity<Long> addQuiz( @Valid @RequestBody(required = true) QuizDto quizDto) {    
        Long userId=15L;
        return ResponseEntity.ok(service.addQuiz(quizDto,userId));
    }
    
    //only teacher
    @PutMapping
    public void updateQuiz(@Valid @RequestBody(required = true) QuizDto quizDto){
        Long userId=15L;
        service.updateQuiz(quizDto,userId);
    }

    //only teacher
    @DeleteMapping("/{id}")
    public void deleteQuizById(@PathVariable(required = true) Long id){
        Long userId=15L;
        service.deleteQuiz(id,userId);
    }

    //only authentic users
    @PostMapping("/take")
    public ResponseEntity<Boolean> takeQuiz(@Valid @RequestBody(required = true) TakeQuizDto takeQuizDto){
        Long userId=15L;
        return ResponseEntity.ok(service.takeQuiz(takeQuizDto,userId));
    }

    //only from courseService
    @PostMapping("/passed")
    public ResponseEntity<Boolean> areQuizsPassed(@Valid @RequestBody(required = true) verifyQuizDto verifyQuizDto){
        return ResponseEntity
        .ok(service.finishedSections(verifyQuizDto.getSectionIds(), verifyQuizDto.getUserId()));
    }

    //only from CourseService
    @PostMapping("/valid")
    public ResponseEntity<Boolean> validSection(@RequestBody(required = true) List<Long> sectionIds){
        return ResponseEntity
        .ok(service.validSections(sectionIds));
    }

}
