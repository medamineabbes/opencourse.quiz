package com.opencourse.quiz.dtos;

import java.util.ArrayList;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.opencourse.quiz.entities.Quiz;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuizDto {
    
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

    private boolean isPassed;

    private boolean retakable;

    public static Quiz toQuiz(QuizDto qd){
        Quiz q=new Quiz();
        q.setDescription(qd.getDescription());
        q.setId(qd.getId());        
        q.setSectionId(qd.getSectionId());
        q.setTakenByUser(new ArrayList<>());
        q.setTitle(qd.getTitle());
        return q;
    }
    public static QuizDto toDto(Quiz q){
        QuizDto qd=new QuizDto();
        qd.setDescription(q.getDescription());
        qd.setId(q.getId());
        qd.setSectionId(q.getSectionId());
        qd.setTitle(q.getTitle());
        qd.setPassed(false);
        qd.setRetakable(true);
        return qd;
    }
}
