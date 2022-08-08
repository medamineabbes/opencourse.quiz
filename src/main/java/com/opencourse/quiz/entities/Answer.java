package com.opencourse.quiz.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String answer;
    private Boolean isCorrect;
    
    //question
    @ManyToOne
    private Question question;

    //ManyToMany with quizTakenByUser (selected answer)
    @ManyToMany(mappedBy="selectedAnswers")
    private List<QuizTakenByUser> quizTakenByUser;
}
