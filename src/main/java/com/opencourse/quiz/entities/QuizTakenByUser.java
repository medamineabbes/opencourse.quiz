package com.opencourse.quiz.entities;

import java.util.List;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
public class QuizTakenByUser {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "isPassed must be specified")
    private Boolean isPassed;
    @NotNull(message = "takenAt must be specified")
    private LocalDateTime takenAt;
    //quiz
    @ManyToOne
    private Quiz quiz;

    //user
    @NotNull(message = "userId cannot be null")
    private Long userId;


    // ManyToMany with answer (selected answer)
    @ManyToMany
    @JoinTable(
        name="selected_answers",
        joinColumns=@JoinColumn(name="quiztakenbyuser_id"),
        inverseJoinColumns = @JoinColumn(name="answer_id")
    )
    private List<Answer> selectedAnswers;
}
