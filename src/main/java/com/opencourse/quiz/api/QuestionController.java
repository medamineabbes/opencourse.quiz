package com.opencourse.quiz.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.opencourse.quiz.dtos.QuestionDto;
import com.opencourse.quiz.services.QuestionService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/question")
@AllArgsConstructor
public class QuestionController {
    private final QuestionService service;
    
    @GetMapping("/{id}")
    public ResponseEntity<QuestionDto> getQuestionById(@PathVariable(required = true) Long id){
        return ResponseEntity.ok(service.getQuestionById(id));
    }

    @PostMapping
    public Long addQuestion(@Valid @RequestBody(required = true) QuestionDto questionDto){
        return service.addQuestion(questionDto);
    }

    @PutMapping
    public void updateQuestion(@Valid @RequestBody(required = true) QuestionDto questionDto){
        service.updateQuestion(questionDto);
    }

    @DeleteMapping("/{id}")
    public void deleteQuestionById(@PathVariable(required = true) Long id){
        service.deleteQuestionById(id);
    }

    @GetMapping("/quiz/{id}")
    public ResponseEntity<List<QuestionDto>> getQuestionsByQuizId(@PathVariable(required = true) Long id){
        return ResponseEntity.ok(service.getQuestionsByQuizId(id));
    }
}
