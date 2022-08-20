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
import com.opencourse.quiz.exceptions.UnAuthorizedActionException;
import com.opencourse.quiz.externalservices.CourseService;


@Service
public class QuizService {
    private final QuizRepo repo;
    private final QuizTakenRepo qtRepo;
    private final CourseService courseService;
    public static float minForSuccess=(float) 80.0;
    public static int waitingDaysBeforeRetake=1;

    public QuizService(QuizRepo repo,QuizTakenRepo qtRepo,CourseService courseService){
        this.repo=repo;
        this.qtRepo=qtRepo;
        this.courseService=courseService;
    }


    public Long addQuiz(QuizDto qdto,Long userId){

        if(!courseService.userCreatedSection(qdto.getSectionId(), userId))
        throw new UnAuthorizedActionException();

        Quiz quiz=QuizDto.toQuiz(qdto);
        repo.save(quiz);
        return quiz.getId();
    }


    public QuizDto getQuiz(Long id,Long userId){
        //make sure quiz exists
        Quiz q=repo.findById(id)
        .orElseThrow(()->new QuizNotFoundException(id));

        //make sure user suscribed to course
        if(!courseService.userHasAccessToSection(q.getSectionId(), userId))
        throw new QuizNotFoundException(id);

        return QuizDto.toDto(q);
    }


    public void updateQuiz(QuizDto q,Long userId) {
        //make sure quiz exists
        Quiz quiz=repo.findById(q.getId())
        .orElseThrow(()->new QuizNotFoundException(q.getId()));

        //make sure user created course 
        if(!courseService.userCreatedSection(quiz.getSectionId(), userId))
        throw new UnAuthorizedActionException();

        quiz.setDescription(q.getDescription());
        quiz.setTitle(q.getTitle());
        repo.flush();
    }


    public void deleteQuiz(Long id,Long userId){
        //make sure quiz exists
        Quiz quiz=repo.findById(id)
        .orElseThrow(()->new QuizNotFoundException(id));
        //make sure user created the course
        if(!courseService.userCreatedSection(quiz.getSectionId(), userId))
        throw new UnAuthorizedActionException();
        repo.deleteById(id);
    }

    public boolean takeQuiz(TakeQuizDto q,Long userId){
        int wrongAnswersNumber=0;
        int numberOfAnswers=0;
        List<Answer> selectedAnswers=new ArrayList<Answer>();
        
        //make sure quiz exists
        Quiz quiz=repo
        .findById(q.getQuizId())
        .orElseThrow(
            ()->new QuizNotFoundException(q.getQuizId())
            );

        //make sure user subscribed to course
        if(!courseService.userHasAccessToSection(quiz.getSectionId(), userId))
        throw new QuizNotFoundException(quiz.getId());

        //make sure quize is takable
        //quiz can be retaken after 1 day of the last try

        //get last try
        Optional<QuizTakenByUser> isTakenBefore=qtRepo
        .findByQuizIdAndUserId(q.getQuizId(), userId)
        .stream()
        .sorted((one,two)->two.getTakenAt().compareTo(one.getTakenAt()))
        .findFirst();
    


        if(isTakenBefore.isPresent() && 
        LocalDateTime.now().isBefore(isTakenBefore.get()
        .getTakenAt()
        .plusDays(waitingDaysBeforeRetake)))
        throw new QuizUntakableException(q.getQuizId(),isTakenBefore.get().getTakenAt().plusDays(waitingDaysBeforeRetake));

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

                //increment the number of answers to get the total
                numberOfAnswers++;

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

        //all questions are correct 80%
        //replace 80 with propertie value
        boolean isPassed=(float)wrongAnswersNumber<=(float) (numberOfAnswers*(1-(minForSuccess/100.0)));
        quizTaken.setIsPassed(isPassed);

        quizTaken.setQuiz(quiz);
        quizTaken.setSelectedAnswers(selectedAnswers);
        quizTaken.setTakenAt(LocalDateTime.now());
        quizTaken.setUserId(userId);

        qtRepo.save(quizTaken);

        return  isPassed;
    }

    public List<QuizDto> getQuizBySectionId(Long id,Long userId){
        
        //make sure user can access section
        if(courseService.userHasAccessToSection(id, userId))
        throw new QuizNotFoundException("no quiz found");

        return repo.findBySectionId(id)
        .stream()
        .map(quiz->QuizDto.toDto(quiz))
        .collect(Collectors.toList());
    }


    public boolean finishedSections(List<Long> sectionIds,Long userId){
        
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


    public boolean validSections(List<Long> sectionIds){
        for(int i=0;i<sectionIds.size();i++){
            List<Quiz> quizs=repo.findBySectionId(sectionIds.get(i));
            for(int j=0;j<quizs.size();j++){
                Quiz quiz=quizs.get(j);

                //make sure at least 2 question per quiz
                if(quiz.getQuestions().size()<2)
                return false;
                
                for(int k=0;k<quiz.getQuestions().size();k++){
                    Question question=quiz.getQuestions().get(k);

                    //make sure at least 2 answers per question
                    if(question.getAnswers().size()<2)
                    return false;
                }
            }
        }
        return true;
    }

}
