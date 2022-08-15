package com.opencourse.quiz.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.opencourse.quiz.repos.*;
import com.opencourse.quiz.dtos.*;
import com.opencourse.quiz.entities.*;
import com.opencourse.quiz.exceptions.MissingAnswerException;
import com.opencourse.quiz.exceptions.QuizNotFoundException;
import com.opencourse.quiz.exceptions.QuizUntakableException;
import com.opencourse.quiz.exceptions.UnAnsweredQuestionException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class QuizService {
    private final QuizRepo repo;
    private final QuizTakenRepo qtRepo;
    // only if user ows course
    public Long addQuiz(QuizDto qdto){
        /*
         * 
         * make sure user created the course
         */
        Quiz quiz=QuizDto.toQuiz(qdto);
        repo.save(quiz);
        return quiz.getId();
    }

    //get userId from principal 
    public QuizDto getQuiz(Long id){
        Quiz q=repo.findById(id)
        .orElseThrow(()->new QuizNotFoundException(id));

        return QuizDto.toDto(q);
    }

    //only if user onws course 
    public void updateQuiz(QuizDto q){
        Quiz quiz=repo.findById(q.getId())
        .orElseThrow(()->new QuizNotFoundException(q.getId()));

        quiz.setDescription(q.getDescription());
        quiz.setTitle(q.getTitle());
        repo.flush();
    }

    public void deleteQuiz(Long id){
        repo.findById(id)
        .orElseThrow(()->new QuizNotFoundException(id));
        repo.deleteById(id);
    }

    //authentic user 
    //get userId from principal
    //user can retake quiz after 1 day
    public boolean takeQuiz(TakeQuizDto q){
        Long userId=15L;//take from spring security
        int wrongAnswersNumber=0;
        List<Answer> selectedAnswers=new ArrayList<Answer>();
        
        //make sure quiz exists
        Quiz quiz=repo
        .findById(q.getQuizId())
        .orElseThrow(
            ()->new QuizNotFoundException(q.getQuizId())
            );

        //make sure quize is takable
        //quiz can be retaken after 1 day of the last try

        //get last try
        Optional<QuizTakenByUser> isTakenBefore=qtRepo
        .findByQuizIdAndUserId(q.getQuizId(), userId)
        .stream()
        .sorted((one,two)->two.getTakenAt().compareTo(one.getTakenAt()))
        .findFirst();
    


        if(isTakenBefore.isPresent() && 
        LocalDateTime.now().isBefore(isTakenBefore.get().getTakenAt().plusDays(1)))
        throw new QuizUntakableException(q.getQuizId(),isTakenBefore.get().getTakenAt().plusDays(1));

        //evaluate quiz
        for(int i=0;i<quiz.getQuestions().size();i++){

            //get question from database 
            Question question=quiz.getQuestions().get(i);

            //get corresponding question from user input
            TakeQuestionDto takeQuestionDto=q.getQuestions().stream()
            .filter(qs->qs.getQuestionId()==question.getId())
            .findFirst()
            .orElseThrow(()->new UnAnsweredQuestionException(question.getId()));

            for(int j=0;j<question.getAnswers().size();j++){
                //get answers one by one from database 
                Answer expected=question.getAnswers().get(j);

                //get correcponding answers from user input
                TakeAnswerDto actual=takeQuestionDto.getAnswers()
                .stream().filter(a->a.getAnswerId()==expected.getId()).findFirst()
                .orElseThrow(()->new MissingAnswerException(expected.getId()));
                
                //if wrong increment wrongAnswersNumber
                if(actual.isChecked() != expected.getIsCorrect())
                wrongAnswersNumber++;

                //populate selected answers
                if(actual.isChecked())
                selectedAnswers.add(expected);
            }
        }


        //add to database
        QuizTakenByUser quizTaken=new QuizTakenByUser();

        //all questions are correct 100%
        quizTaken.setIsPassed(wrongAnswersNumber==0);

        quizTaken.setQuiz(quiz);
        quizTaken.setSelectedAnswers(selectedAnswers);
        quizTaken.setTakenAt(LocalDateTime.now());
        quizTaken.setUserId(userId);

        qtRepo.save(quizTaken);

        return  wrongAnswersNumber==0;
    }

    public List<QuizDto> getQuizBySectionId(Long id){
        return repo.findBySectionId(id)
        .stream()
        .map(quiz->QuizDto.toDto(quiz))
        .collect(Collectors.toList());
    }

    public boolean quizArePassed(List<Long> sectionIds,Long userId){
        
        for(int i=0;i<sectionIds.size();i++){
            List<Quiz> quizs=repo.findBySectionId(sectionIds.get(i));
            for(int j=0;j<quizs.size();j++){
                Optional<QuizTakenByUser> quizTaken=qtRepo
                .findByQuizIdAndUserId(quizs.get(j).getId(), userId)
                .stream().filter((qtbu)->qtbu.getIsPassed()).findAny();
                if(quizTaken.isEmpty())
                return false;
            }
        }
        return true;
    }
}
