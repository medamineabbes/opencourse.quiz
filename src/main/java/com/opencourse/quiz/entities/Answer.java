package com.opencourse.quiz.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "answer is mandatory")
    private String answer;

    @NotNull(message = "isCorrect attribute must be specified")
    private Boolean isCorrect;
    
    //question
    @ManyToOne
    private Question question;

    //ManyToMany with quizTakenByUser (selected answer)
    @ManyToMany(mappedBy="selectedAnswers")
    private List<QuizTakenByUser> quizTakenByUser;
}
