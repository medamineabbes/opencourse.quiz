package com.opencourse.quiz.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.opencourse.quiz.dtos.QuestionDto;
import com.opencourse.quiz.repos.QuestionRepo;
import com.opencourse.quiz.repos.QuizRepo;
import com.opencourse.quiz.entities.Quiz;
import com.opencourse.quiz.exceptions.QuestionNotFoundException;
import com.opencourse.quiz.exceptions.QuizNotFoundException;
import com.opencourse.quiz.exceptions.UnAuthorizedActionException;
import com.opencourse.quiz.externalservices.CourseService;
import com.opencourse.quiz.entities.Question;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class QuestionService {
    private final QuestionRepo qRepo;
    private final QuizRepo quizRepo;
    private final CourseService courseService;
    //only authentic users
    public QuestionDto getQuestionById(Long id,Long userId){
        //make sure question exists
        Question q=qRepo.findById(id)
        .orElseThrow(()->
            new QuestionNotFoundException("question with id : " + id + " not found")
        );

        //make sure user has access to section
        if(!courseService.userHasAccessToSection(q.getQuiz().getSectionId(), userId))
        throw new QuestionNotFoundException("question with id : " + id + " not found");

        return QuestionDto.toDto(q);
    }

    //only if user owns course
    public Long addQuestion(QuestionDto q,Long userId){
        Quiz quiz=quizRepo
        .findById(q.getQuizId())
        .orElseThrow(()->
            new QuizNotFoundException(q.getId())
        );


        //make sure user created the course
        if(!courseService.userCreatedSection(quiz.getSectionId(), userId))
        throw new UnAuthorizedActionException();

        Question question=QuestionDto.toQuestion(q);
        question.setQuiz(quiz);
        question.setNumberCorrectAnswers((byte)0);
        qRepo.save(question);

        return question.getId();
    }

    //only id user owns course
    public void updateQuestion(QuestionDto q,Long userId){
        //make sure question exists
        Question question=qRepo
        .findById(q.getId())
        .orElseThrow(()->
            new QuestionNotFoundException(q.getId())
        );

        //make sure user created course
        if(!courseService.userCreatedSection(question.getQuiz().getSectionId(), userId))
        throw new UnAuthorizedActionException();

        question.setQuestion(q.getQuestion());

        qRepo.flush();
    }

    //only if user own course
    public void deleteQuestionById(Long questionId,Long userId){
        //make sure question exists
        Question q=qRepo.findById(questionId).orElseThrow(()->new QuestionNotFoundException(questionId));
        
        //make sure user created the course
        if(!courseService.userCreatedSection(q.getQuiz().getSectionId(), userId))
        throw new UnAuthorizedActionException();

        qRepo.deleteById(questionId);
    }

    public List<QuestionDto> getQuestionsByQuizId(Long id,Long userId){
        //make sure quis exists
        Quiz quiz=quizRepo
        .findById(id)
        .orElseThrow(()->new QuizNotFoundException(id));

        //make sure user has access
        if(!courseService.userHasAccessToSection(quiz.getSectionId(), userId))
        throw new QuizNotFoundException(id);

        return quiz.getQuestions()
        .stream()
        .map(question->QuestionDto.toDto(question))
        .collect(Collectors.toList());
    }

}
