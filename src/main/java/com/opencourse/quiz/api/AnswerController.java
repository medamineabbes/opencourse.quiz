package com.opencourse.quiz.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.opencourse.quiz.dtos.AnswerDto;
import com.opencourse.quiz.services.AnswerService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/answer")
@AllArgsConstructor
public class AnswerController {
    private final AnswerService service;
    
    //only teachers
    @PostMapping
    public Long addAnswer(@Valid @RequestBody(required = true) AnswerDto answerDto){
        Long userId=Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        return service.addAnswer(answerDto,userId);
    }
    
    //only authentic users 
    @GetMapping("/{id}")
    public ResponseEntity<AnswerDto> getAnswerById(@PathVariable(required = true) Long id){
        Long userId=Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity.ok(service.getAnswerById(id,userId));
    }

    //only teacher
    @PutMapping
    public void updateAnswer(@Valid @RequestBody(required = true) AnswerDto answer){
        Long userId=Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        service.updateAnswer(answer,userId);
    }

    //only teacher
    @DeleteMapping("/{id}")
    public void deleteAnswerById(@PathVariable(required = true) Long id){
        Long userId=Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        service.deleteAnswerById(id,userId);
    }
    
    //authentic users
    @GetMapping("/question/{id}")
    public ResponseEntity<List<AnswerDto>> getAnswersByQuestionId(@PathVariable(required=true)Long id){
        Long userId=Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity.ok(service.getByQuestionId(id,userId));
    }

    
}
