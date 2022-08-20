package com.opencourse.quiz.apis;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencourse.quiz.api.AnswerController;
import com.opencourse.quiz.dtos.AnswerDto;
import com.opencourse.quiz.exceptions.AnswerNotFoundException;
import com.opencourse.quiz.services.AnswerService;


@WebMvcTest(AnswerController.class)
public class AnswerControllerTest {
    private String basePath="/api/v1/answer";
    private ObjectMapper mapper;
    @MockBean
    private AnswerService service;

    @Autowired
    MockMvc mvc;

    @BeforeEach
    public void init(){
        mapper=new ObjectMapper();;
    }

    @Test
    @DisplayName("shoud return Answer")
    public void getAnswerTest() throws Exception{
        AnswerDto a=new AnswerDto();
        a.setAnswer("answer");
        a.setId(1L);
        a.setQuestionId(2L);
        when(service.getAnswerById(1L,15L)).thenReturn(a);

        mvc.perform(
            get(basePath + "/1")
        ).andDo(print())
        .andExpect(status().isOk());
    }

    @Test
    @DisplayName("should return 404")
    public void getAnswerErrorTest() throws Exception{
        when(service.getAnswerById(1L,15L)).thenThrow(new AnswerNotFoundException(1L));
        mvc.perform(
            get(basePath+"/1")
        ).andDo(print())
        .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("should add answer")
    public void addAnswerTest() throws JsonProcessingException, Exception{
        AnswerDto a=new AnswerDto();
        a.setAnswer("katha katha");
        a.setCorrect(true);
        a.setQuestionId(1L);
        Map<String,Object> body=new HashMap<>();
        body.put("answer","katha katha");
        body.put("questionId",1L);
        body.put("correct",true);

        mvc.perform(
            post(basePath)
            .content(mapper.writeValueAsString(body))
            .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isOk());

    }

    @Test
    @DisplayName("should update answer")
    public void updateAnswerTest() throws JsonProcessingException, Exception{
        AnswerDto a=new AnswerDto();
        a.setAnswer("answer");
        a.setCorrect(true);
        a.setId(1L);
        a.setQuestionId(2L);

        Map<String,Object> body=new HashMap<>();
        body.put("answer", "answer");
        body.put("correct",true);
        body.put("id", 1L);
        body.put("questionId", 2L);

        mvc.perform(
            put(basePath)
            .content(mapper.writeValueAsString(body))
            .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print())
        .andExpect(status().isOk());

        verify(service).updateAnswer(a,15L);
    }


}
