package com.opencourse.quiz.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.opencourse.quiz.dtos.AnswerDto;
import com.opencourse.quiz.entities.Answer;
import com.opencourse.quiz.entities.Question;
import com.opencourse.quiz.exceptions.AnswerNotFoundException;
import com.opencourse.quiz.exceptions.QuestionNotFoundException;
import com.opencourse.quiz.repos.AnswerRepo;
import com.opencourse.quiz.repos.QuestionRepo;

import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class AnswerService {
    private final AnswerRepo aRepo;
    private final QuestionRepo qRepo;

    public List<AnswerDto> getByQuestionId(Long questionId){
        Question question=qRepo
        .findById(questionId)
        .orElseThrow(()->new QuestionNotFoundException(questionId));
        return question.getAnswers()
        .stream()
        .map((answer)->AnswerDto.toDto(answer))
        .collect(Collectors.toList());
    }
    
    public AnswerDto getAnswerById(Long id)throws AnswerNotFoundException{
        Answer a=aRepo.findById(id)
        .orElseThrow(()->
        new AnswerNotFoundException("answer with id : " + id + " not found"));
        return AnswerDto.toDto(a);
    }

    //only if he owns the course
    public Long addAnswer(AnswerDto answerDto)throws QuestionNotFoundException{
        //make sure question exists
        Question q=qRepo
        .findById(answerDto.getQuestionId())
        .orElseThrow(
            ()->
            new QuestionNotFoundException("question with id : " + answerDto.getQuestionId() + " not found")
            );

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

    //only if he owns the course
    public void updateAnswer(AnswerDto a)throws QuestionNotFoundException,AnswerNotFoundException{
        Answer answer=aRepo
        .findById(a.getId())
        .orElseThrow(()->
        new AnswerNotFoundException("answer with id : " + a.getId() + " not found")
        ); 
        
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

    //only if user owns the cousre
    public void deleteAnswerById(Long id){
	aRepo.findById(id)
	.orElseThrow(()->new AnswerNotFoundException(id));
        aRepo.deleteById(id);
    }
}
