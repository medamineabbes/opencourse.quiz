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
import com.opencourse.quiz.entities.Question;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class QuestionService {
    private final QuestionRepo qRepo;
    private final QuizRepo quizRepo;

    public QuestionDto getQuestionById(Long id)throws QuestionNotFoundException{
        Question q=qRepo.findById(id)
        .orElseThrow(()->
            new QuestionNotFoundException("question with id : " + id + " not found")
        );

        return QuestionDto.toDto(q);
    }

    //only if user owns course
    public Long addQuestion(QuestionDto q)throws QuizNotFoundException{
        Quiz quiz=quizRepo
        .findById(q.getQuizId())
        .orElseThrow(()->
            new QuizNotFoundException(q.getId())
        );

        /*
         * test if course created by user  
         */

        Question question=QuestionDto.toQuestion(q);
        question.setQuiz(quiz);
        question.setNumberCorrectAnswers((byte)0);
        qRepo.save(question);

        return question.getId();
    }

    //only id user owns course
    public void updateQuestion(QuestionDto q)throws QuestionNotFoundException{
        Question question=qRepo
        .findById(q.getId())
        .orElseThrow(()->
            new QuestionNotFoundException(q.getId())
        );
        question.setQuestion(q.getQuestion());

        qRepo.flush();
    }

    //only if user own course
    public void deleteQuestionById(Long id){
        qRepo.findById(id).orElseThrow(()->new QuestionNotFoundException(id));
        qRepo.deleteById(id);
    }

    public List<QuestionDto> getQuestionsByQuizId(Long id){
        Quiz quiz=quizRepo
        .findById(id)
        .orElseThrow(()->new QuizNotFoundException(id));

        return quiz.getQuestions()
        .stream()
        .map(question->QuestionDto.toDto(question))
        .collect(Collectors.toList());
    }

}
