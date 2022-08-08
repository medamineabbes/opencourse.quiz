package com.opencourse.quiz.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;

@Data
@Entity
public class Quiz {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    //section
    private Long sectionId;
    
    //questions
    @OneToMany(mappedBy="quiz")
    private List<Question> questions;

    // takenByUser
    @OneToMany(mappedBy="quiz")
    private List<QuizTakenByUser> takenByUser;
}
