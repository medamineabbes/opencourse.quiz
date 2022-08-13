package com.opencourse.quiz.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
@Entity
public class Quiz {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message="title is mandatory")
    @Size(max=50,message="max number of characters in title is 50")
    private String title;

    @NotBlank(message="description is mandatory")
    @Size(max=300,message = "max number of characters in description is 300")
    private String description;

    //section
    @NotNull
    private Long sectionId;
    
    //questions
    @NotEmpty(message="questions are mandatory")
    @Size(min = 5,message="the min number of questions in a quiz is 5")
    @OneToMany(mappedBy="quiz")
    private List<Question> questions;

    // takenByUser
    @OneToMany(mappedBy="quiz")
    private List<QuizTakenByUser> takenByUser;
}
