package com.opencourse.quiz.entities;

import java.util.List;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.GenerationType;

import lombok.Data;

@Data
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String question;
    private Long numberCorrectAnswers;

    //quiz
    @ManyToOne
    private Quiz quiz;

    //answers
    @OneToMany(mappedBy="question")
    private List<Answer> answers;
}
