package com.opencourse.quiz.entities;

import java.util.List;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    private Long numberCorrectAnswers;

    //quiz
    @NotNull(message="quiz cannot be null")
    @ManyToOne
    private Quiz quiz;

    //answers
    @Size(min = 2,max = 5,message = "the number of answers must be between 2 and 5")
    @OneToMany(mappedBy="question")
    private List<Answer> answers;
}
