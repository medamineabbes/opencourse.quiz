package com.opencourse.quiz.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.opencourse.quiz.dtos.AnswerDto;
import com.opencourse.quiz.entities.Answer;
import com.opencourse.quiz.entities.Question;
import com.opencourse.quiz.exceptions.AnswerNotFoundException;
import com.opencourse.quiz.exceptions.QuestionNotFoundException;
import com.opencourse.quiz.exceptions.UnAuthorizedActionException;
import com.opencourse.quiz.externalservices.CourseService;
import com.opencourse.quiz.repos.AnswerRepo;
import com.opencourse.quiz.repos.QuestionRepo;

import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class AnswerService {
    private final AnswerRepo aRepo;
    private final QuestionRepo qRepo;
    private final CourseService courseService;
    public List<AnswerDto> getByQuestionId(Long questionId,Long userId){
        //make sure answer exists
        Question question=qRepo
        .findById(questionId)
        .orElseThrow(()->new QuestionNotFoundException(questionId));

        //make sure user has access to answer
        if(!courseService.userHasAccessToSection(question.getQuiz().getSectionId(), userId))
        throw new QuestionNotFoundException(questionId);


        return question.getAnswers()
        .stream()
        .map((answer)->AnswerDto.toDto(answer))
        .collect(Collectors.toList());
    }
    
    public AnswerDto getAnswerById(Long id,Long userId){
        //make sure answer exists
        Answer a=aRepo.findById(id)
        .orElseThrow(()->
        new AnswerNotFoundException("answer with id : " + id + " not found"));

        //make sure user has access
        if(!courseService.userHasAccessToSection(a.getQuestion().getQuiz().getSectionId(), userId))
        throw new AnswerNotFoundException(id);

        return AnswerDto.toDto(a);
    }

    //only teachers
    public Long addAnswer(AnswerDto answerDto,Long userId){
        //make sure question exists
        Question q=qRepo
        .findById(answerDto.getQuestionId())
        .orElseThrow(
            ()->
            new QuestionNotFoundException("question with id : " + answerDto.getQuestionId() + " not found")
            );
        
            //make sure user created the course
        if(!courseService.userCreatedSection(q.getQuiz().getSectionId(), userId))
        throw new UnAuthorizedActionException();

        //add answer
        Answer a=AnswerDto.toAnswer(answerDto);
        a.setQuestion(q);
        aRepo.save(a);
        
        // increment number of correct answers
        if(answerDto.isCorrect()){
            q.setNumberCorrectAnswers((byte) (q.getNumberCorrectAnswers()+1));
            qRepo.flush();
        }
        return a.getId();
    }

    //only teachers
    public void updateAnswer(AnswerDto a,Long userId){
        //make sure answer exists
        Answer answer=aRepo
        .findById(a.getId())
        .orElseThrow(()->
        new AnswerNotFoundException("answer with id : " + a.getId() + " not found")
        ); 
        
        //make sure user created the course
        if(!courseService.userCreatedSection(answer.getQuestion().getQuiz().getSectionId(), userId))
        throw new UnAuthorizedActionException();

        Question q=qRepo
        .findById(a.getQuestionId())
        .orElseThrow(()->
        new QuestionNotFoundException("question with id : " + a.getQuestionId() + " not found")
        );

        //update number of correct answers
        if(!answer.getIsCorrect().equals(a.isCorrect())){
            Integer numberOfCorrectAnswers=q.getNumberCorrectAnswers().intValue();
            
            if(a.isCorrect())
            numberOfCorrectAnswers++;
            else
            numberOfCorrectAnswers--;
            
            q.setNumberCorrectAnswers(numberOfCorrectAnswers.byteValue());
            qRepo.flush();
        }

        answer.setAnswer(a.getAnswer());
        answer.setIsCorrect(a.isCorrect());
        answer.setQuestion(q);

        aRepo.flush();        
    }

    //only teachers
    public void deleteAnswerById(Long id,Long userId){
        //make sure answer exists
	    Answer a=aRepo.findById(id)
	    .orElseThrow(()->new AnswerNotFoundException(id));

        //make sure user created the course
        if(!courseService.userCreatedSection(a.getQuestion().getQuiz().getSectionId(), userId))
        throw new UnAuthorizedActionException();

        aRepo.deleteById(id);
    }
}
