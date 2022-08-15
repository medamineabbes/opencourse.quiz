package com.opencourse.quiz.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.persistence.GenerationType;

import lombok.Data;

@Data
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message="question attribute cannot be empty")
    private String question;
    private Byte numberCorrectAnswers;

    //quiz
    @NotNull(message="quiz cannot be null")
    @ManyToOne
    private Quiz quiz;

    //answers
    @OneToMany(mappedBy="question",cascade = CascadeType.REMOVE)
    private List<Answer> answers;
}
