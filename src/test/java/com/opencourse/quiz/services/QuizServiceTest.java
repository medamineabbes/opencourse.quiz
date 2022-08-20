package com.opencourse.quiz.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.opencourse.quiz.dtos.QuizDto;
import com.opencourse.quiz.dtos.TakeAnswerDto;
import com.opencourse.quiz.dtos.TakeQuestionDto;
import com.opencourse.quiz.dtos.TakeQuizDto;
import com.opencourse.quiz.entities.Quiz;
import com.opencourse.quiz.entities.Question;
import com.opencourse.quiz.entities.QuizTakenByUser;
import com.opencourse.quiz.entities.Answer;
import com.opencourse.quiz.exceptions.MissingAnswerException;
import com.opencourse.quiz.exceptions.QuizNotFoundException;
import com.opencourse.quiz.exceptions.QuizUntakableException;
import com.opencourse.quiz.exceptions.UnAnsweredQuestionException;
import com.opencourse.quiz.externalservices.CourseService;
import com.opencourse.quiz.repos.QuizRepo;
import com.opencourse.quiz.repos.QuizTakenRepo;


public class QuizServiceTest {
    Quiz quiz;
    Question question1,question2,question3;
    Answer a1,a2,a3,a4,a5,a6,a7,a8,a9;
    QuizTakenByUser qtbu,qtbu2;
    QuizRepo repo=mock(QuizRepo.class);
    QuizTakenRepo qRepo=mock(QuizTakenRepo.class);
    CourseService courseService=mock(CourseService.class);
    QuizService service;

    @BeforeEach
    public void init(){

        service=new QuizService(repo, qRepo,courseService);
        question1=new Question();question2=new Question();question3=new Question();
        a1=new Answer();a2=new Answer();a3=new Answer();a4=new Answer();a5=new Answer();a6=new Answer();a7=new Answer();a8=new Answer();a9=new Answer();
        quiz=new Quiz();
        qtbu=new QuizTakenByUser();

        //quiz
        quiz.setDescription("description");
        quiz.setSectionId(1L);
        quiz.setTitle("title");
        quiz.setId(1L);
        quiz.setQuestions(List.of(question1,question2,question3));
        quiz.setTakenByUser(List.of(qtbu));

        //questions
        question1.setId(1L);
        question2.setId(2L);
        question3.setId(3L);
        question1.setQuestion("question1");
        question2.setQuestion("question2");
        question3.setQuestion("question3");
        question1.setQuiz(quiz);
        question2.setQuiz(quiz);
        question3.setQuiz(quiz);
        question1.setNumberCorrectAnswers((byte) 2);
        question2.setNumberCorrectAnswers((byte) 1);
        question3.setNumberCorrectAnswers((byte) 1);
        question1.setAnswers(List.of(a1,a2,a3));
        question2.setAnswers(List.of(a4,a5,a6));
        question3.setAnswers(List.of(a7,a8,a9));
        
        //answers
        a1.setAnswer("answer1");
        a2.setAnswer("answer2");
        a3.setAnswer("answer3");
        a4.setAnswer("answer4");
        a5.setAnswer("answer5");
        a6.setAnswer("answer6");
        a7.setAnswer("answer7");
        a8.setAnswer("answer8");
        a9.setAnswer("answer9");
        a1.setId(1L);
        a2.setId(2L);
        a3.setId(3L);
        a4.setId(4L);
        a5.setId(5L);
        a6.setId(6L);
        a7.setId(7L);
        a8.setId(8L);
        a9.setId(9L);
        a1.setIsCorrect(true);
        a2.setIsCorrect(true);
        a3.setIsCorrect(false);
        a4.setIsCorrect(true);
        a5.setIsCorrect(false);
        a6.setIsCorrect(false);
        a7.setIsCorrect(false);
        a8.setIsCorrect(true);
        a9.setIsCorrect(false);
        a1.setQuestion(question1);
        a2.setQuestion(question1);
        a3.setQuestion(question1);
        a4.setQuestion(question2);
        a5.setQuestion(question2);
        a6.setQuestion(question2);
        a7.setQuestion(question3);
        a8.setQuestion(question3);
        a9.setQuestion(question3);
    }

    @Test
    @DisplayName("should return quiz")
    public void getQuizTest() throws Exception{
        when(repo.findById(1L)).thenReturn(Optional.of(quiz));
        when(courseService.userHasAccessToSection(quiz.getSectionId(), 15L)).thenReturn(true);
        QuizDto qdto=service.getQuiz(1L,15L);

        assertEquals(quiz.getDescription(),qdto.getDescription());
        assertEquals(quiz.getId(), qdto.getId());
        assertEquals(quiz.getSectionId(), qdto.getSectionId());
        assertEquals(quiz.getTitle(), qdto.getTitle());
    }

    @Test
    @DisplayName("should throws Quz nor found exception")
    public void getQuizErrorTest(){
        assertThrows(QuizNotFoundException.class,()->{
            service.getQuiz(1L,15L);
        });
    }

    @Test
    @DisplayName("should add quiz")
    public void addQuizTest(){
        when(courseService.userCreatedSection(1L, 15L)).thenReturn(true);
        QuizDto qdto=new QuizDto();
        qdto.setDescription("description");
        qdto.setPassed(true);
        qdto.setSectionId(1L);
        qdto.setTitle("title");
        
        service.addQuiz(qdto,15L);

        verify(repo).save(any(Quiz.class));
    }

    @Test
    @DisplayName("should update quiz")
    public void updateQuizTest() throws Exception{
        when(repo.findById(1L)).thenReturn(Optional.of(quiz));
        when(courseService.userCreatedSection(quiz.getSectionId(), 15L)).thenReturn(true);
        QuizDto qdto=new QuizDto();
        qdto.setDescription("description");
        qdto.setPassed(true);
        qdto.setSectionId(1L);
        qdto.setTitle("title");
        qdto.setId(1L);

        service.updateQuiz(qdto,15L);

        verify(repo).flush();
    }

    @Test
    @DisplayName("should throw Quiz not found exception")
    public void updateQuizErrorTest(){
        QuizDto qdto=new QuizDto();
        qdto.setDescription("description");
        qdto.setPassed(true);
        qdto.setSectionId(1L);
        qdto.setTitle("title");
        qdto.setId(1L);

        assertThrows(QuizNotFoundException.class, ()->{
            service.updateQuiz(qdto,15L);
        });
    }

    @Test
    @DisplayName("should delete quiz")
    public void deleteQuizTest(){
        when(repo.findById(1L)).thenReturn(Optional.of(quiz));
        when(courseService.userCreatedSection(quiz.getSectionId(), 15L)).thenReturn(true);

        service.deleteQuiz(1L,15L);

        verify(repo).deleteById(1L);
    }

    @Test
    @DisplayName("should throw Quiz not found Exception")
    public void deleteQuizErrorTest(){
        assertThrows(QuizNotFoundException.class, ()->{
            service.deleteQuiz(1L,15L);
        });
    }

    @Test
    @DisplayName("should return true(success) after take quiz")
    public void takeQuizTest() throws Exception{
        when(repo.findById(1L)).thenReturn(Optional.of(quiz));
        when(courseService.userHasAccessToSection(quiz.getSectionId(), 15L)).thenReturn(true);

        TakeAnswerDto ta1,ta2,ta3,ta4,ta5,ta6,ta7,ta8,ta9;
        ta1=new TakeAnswerDto(1L,true);
        ta2=new TakeAnswerDto(2L,true);
        ta3=new TakeAnswerDto(3L,false);
        ta4=new TakeAnswerDto(4L,true);
        ta5=new TakeAnswerDto(5L,false);
        ta6=new TakeAnswerDto(6L,false);
        ta7=new TakeAnswerDto(7L,false);
        ta8=new TakeAnswerDto(8L,true);
        ta9=new TakeAnswerDto(9L,false);
        
        TakeQuestionDto tq1,tq2,tq3;
        tq1=new TakeQuestionDto(1L, List.of(ta1,ta2,ta3));
        tq2=new TakeQuestionDto(2L, List.of(ta4,ta5,ta6));
        tq3=new TakeQuestionDto(3L, List.of(ta7,ta8,ta9));

        TakeQuizDto tqu;
        tqu=new TakeQuizDto(1L, List.of(tq1,tq2,tq3));

        boolean actual=service.takeQuiz(tqu,15L);
        assertTrue(actual);
    }

    @Test
    @DisplayName("should return false(failure) after take quiz")
    public void takeQuizFailureTest() throws Exception{
        when(repo.findById(1L)).thenReturn(Optional.of(quiz));
        when(courseService.userHasAccessToSection(quiz.getSectionId(), 15L)).thenReturn(true);
        TakeAnswerDto ta1,ta2,ta3,ta4,ta5,ta6,ta7,ta8,ta9;
        ta1=new TakeAnswerDto(1L,true);
        ta2=new TakeAnswerDto(2L,true);
        ta3=new TakeAnswerDto(3L,false);
        ta4=new TakeAnswerDto(4L,false);
        ta5=new TakeAnswerDto(5L,false);
        ta6=new TakeAnswerDto(6L,true);
        ta7=new TakeAnswerDto(7L,false);
        ta8=new TakeAnswerDto(8L,true);
        ta9=new TakeAnswerDto(9L,false);
        
        TakeQuestionDto tq1,tq2,tq3;
        tq1=new TakeQuestionDto(1L, List.of(ta1,ta2,ta3));
        tq2=new TakeQuestionDto(2L, List.of(ta4,ta5,ta6));
        tq3=new TakeQuestionDto(3L, List.of(ta7,ta8,ta9));

        TakeQuizDto tqu;
        tqu=new TakeQuizDto(1L, List.of(tq1,tq2,tq3));

        

        boolean actual=service.takeQuiz(tqu,15L);
        assertFalse(actual);
    }
    
    @Test
    @DisplayName("should return false after (failures) taking quiz")
    public void takeQuizFailureTest2() throws Exception{
        when(repo.findById(1L)).thenReturn(Optional.of(quiz));
        when(courseService.userHasAccessToSection(quiz.getSectionId(), 15L)).thenReturn(true);

        TakeAnswerDto ta1,ta2,ta3,ta4,ta5,ta6,ta7,ta8,ta9;
        ta1=new TakeAnswerDto(1L,true);
        ta2=new TakeAnswerDto(2L,false);
        ta3=new TakeAnswerDto(3L,true);
        ta4=new TakeAnswerDto(4L,true);
        ta5=new TakeAnswerDto(5L,false);
        ta6=new TakeAnswerDto(6L,false);
        ta7=new TakeAnswerDto(7L,false);
        ta8=new TakeAnswerDto(8L,true);
        ta9=new TakeAnswerDto(9L,true);
        
        TakeQuestionDto tq1,tq2,tq3;
        tq1=new TakeQuestionDto(1L, List.of(ta1,ta2,ta3));
        tq2=new TakeQuestionDto(2L, List.of(ta4,ta5,ta6));
        tq3=new TakeQuestionDto(3L, List.of(ta7,ta8,ta9));

        TakeQuizDto tqu;
        tqu=new TakeQuizDto(1L, List.of(tq1,tq2,tq3));

        boolean actual=service.takeQuiz(tqu,15L);
        assertFalse(actual);
    }
    
    @Test
    @DisplayName("should throw QuizUntakableException")
    public void takeQuizErrorTest1(){
        

        TakeAnswerDto ta1,ta2,ta3,ta4,ta5,ta6,ta7,ta8,ta9;
        ta1=new TakeAnswerDto(1L,true);
        ta2=new TakeAnswerDto(2L,true);
        ta3=new TakeAnswerDto(3L,false);
        ta4=new TakeAnswerDto(4L,true);
        ta5=new TakeAnswerDto(5L,false);
        ta6=new TakeAnswerDto(6L,false);
        ta7=new TakeAnswerDto(7L,false);
        ta8=new TakeAnswerDto(8L,true);
        ta9=new TakeAnswerDto(9L,false);
        
        TakeQuestionDto tq1,tq2,tq3;
        tq1=new TakeQuestionDto(1L, List.of(ta1,ta2,ta3));
        tq2=new TakeQuestionDto(2L, List.of(ta4,ta5,ta6));
        tq3=new TakeQuestionDto(3L, List.of(ta7,ta8,ta9));

        TakeQuizDto tqu;
        tqu=new TakeQuizDto(1L, List.of(tq1,tq2,tq3));

        qtbu=new QuizTakenByUser();
        qtbu.setId(1L);
        qtbu.setIsPassed(false);
        qtbu.setQuiz(quiz);
        qtbu.setSelectedAnswers(List.of(a1,a4,a7));
        qtbu.setTakenAt(LocalDateTime.now().minusHours(5));
        qtbu.setUserId(15L);

        when(repo.findById(1L)).thenReturn(Optional.of(quiz));
        when(qRepo.findByQuizIdAndUserId(1L, 15L)).thenReturn(List.of(qtbu));
        when(courseService.userHasAccessToSection(quiz.getSectionId(), 15L)).thenReturn(true);
        

        assertThrows(QuizUntakableException.class, ()->{
            service.takeQuiz(tqu,15L);
        });
        
    }

    @Test
    @DisplayName("should throw QuizUntakableException")
    public void takeQuizErrorTest5(){
        TakeAnswerDto ta1,ta2,ta3,ta4,ta5,ta6,ta7,ta8,ta9;
        ta1=new TakeAnswerDto(1L,true);
        ta2=new TakeAnswerDto(2L,true);
        ta3=new TakeAnswerDto(3L,false);
        ta4=new TakeAnswerDto(4L,true);
        ta5=new TakeAnswerDto(5L,false);
        ta6=new TakeAnswerDto(6L,false);
        ta7=new TakeAnswerDto(7L,false);
        ta8=new TakeAnswerDto(8L,true);
        ta9=new TakeAnswerDto(9L,false);
        
        TakeQuestionDto tq1,tq2,tq3;
        tq1=new TakeQuestionDto(1L, List.of(ta1,ta2,ta3));
        tq2=new TakeQuestionDto(2L, List.of(ta4,ta5,ta6));
        tq3=new TakeQuestionDto(3L, List.of(ta7,ta8,ta9));

        TakeQuizDto tqu;
        tqu=new TakeQuizDto(1L, List.of(tq1,tq2,tq3));

        qtbu=new QuizTakenByUser();
        qtbu.setId(1L);
        qtbu.setIsPassed(false);
        qtbu.setQuiz(quiz);
        qtbu.setSelectedAnswers(List.of(a1,a4,a7));
        qtbu.setTakenAt(LocalDateTime.now().minusDays(2));
        qtbu.setUserId(15L);

        qtbu2=new QuizTakenByUser();
        qtbu2.setId(2L);
        qtbu2.setIsPassed(false);
        qtbu2.setQuiz(quiz);
        qtbu2.setSelectedAnswers(List.of(a4,a7));
        qtbu2.setTakenAt(LocalDateTime.now().minusHours(3));
        qtbu2.setUserId(15L);

        when(repo.findById(1L)).thenReturn(Optional.of(quiz));
        when(qRepo.findByQuizIdAndUserId(1L, 15L)).thenReturn(List.of(qtbu,qtbu2));
        when(courseService.userHasAccessToSection(quiz.getSectionId(), 15L)).thenReturn(true);
        
        assertThrows(QuizUntakableException.class, ()->{
            service.takeQuiz(tqu,15L);
        });
    }
    
    @Test
    @DisplayName("should throw missing question exception")
    public void takeQuizTest2(){
        when(repo.findById(1L)).thenReturn(Optional.of(quiz));
        when(courseService.userHasAccessToSection(quiz.getSectionId(), 15L)).thenReturn(true);

        TakeAnswerDto ta1,ta2,ta3,ta4,ta5,ta6;
        ta1=new TakeAnswerDto(1L,true);
        ta2=new TakeAnswerDto(2L,true);
        ta3=new TakeAnswerDto(3L,false);
        ta4=new TakeAnswerDto(4L,true);
        ta5=new TakeAnswerDto(5L,false);
        ta6=new TakeAnswerDto(6L,false);;
        
        TakeQuestionDto tq1,tq2;
        tq1=new TakeQuestionDto(1L, List.of(ta1,ta2,ta3));
        tq2=new TakeQuestionDto(2L, List.of(ta4,ta5,ta6));

        TakeQuizDto tqu;
        tqu=new TakeQuizDto(1L, List.of(tq1,tq2));

        assertThrows(UnAnsweredQuestionException.class, () ->{
            service.takeQuiz(tqu,15L);
        });
    }

    @Test
    @DisplayName("sould throws missing answer Exception")
    public void takeQuizTest3(){
        when(repo.findById(1L)).thenReturn(Optional.of(quiz));
        when(courseService.userHasAccessToSection(quiz.getSectionId(), 15L)).thenReturn(true);

        TakeAnswerDto ta1,ta2,ta3,ta4,ta5,ta6,ta7,ta9;
        ta1=new TakeAnswerDto(1L,true);
        ta2=new TakeAnswerDto(2L,true);
        ta3=new TakeAnswerDto(3L,false);
        ta4=new TakeAnswerDto(4L,true);
        ta5=new TakeAnswerDto(5L,false);
        ta6=new TakeAnswerDto(6L,false);
        ta7=new TakeAnswerDto(7L,false);
        ta9=new TakeAnswerDto(9L,false);
        
        TakeQuestionDto tq1,tq2,tq3;
        tq1=new TakeQuestionDto(1L, List.of(ta1,ta2,ta3));
        tq2=new TakeQuestionDto(2L, List.of(ta4,ta5,ta6));
        tq3=new TakeQuestionDto(3L, List.of(ta7,ta9));

        TakeQuizDto tqu;
        tqu=new TakeQuizDto(1L, List.of(tq1,tq2,tq3));

        qtbu=new QuizTakenByUser();
        qtbu.setId(1L);
        qtbu.setIsPassed(false);
        qtbu.setQuiz(quiz);
        qtbu.setSelectedAnswers(List.of(a1,a4,a7));
        qtbu.setTakenAt(LocalDateTime.now().minusHours(5));
        qtbu.setUserId(15L);


        assertThrows(MissingAnswerException.class, ()->{
            service.takeQuiz(tqu,15L);
        });
    }

    @Test
    @DisplayName("should throw UnAnswered Question Exception")
    public void takeQuizError4(){
        when(repo.findById(1L)).thenReturn(Optional.of(quiz));
        when(courseService.userHasAccessToSection(quiz.getSectionId(),15L)).thenReturn(true);

        TakeAnswerDto ta4,ta5,ta6,ta7,ta8,ta9;
        ta4=new TakeAnswerDto(4L,true);
        ta5=new TakeAnswerDto(5L,false);
        ta6=new TakeAnswerDto(6L,false);
        ta7=new TakeAnswerDto(7L,false);
        ta8=new TakeAnswerDto(8L,true);
        ta9=new TakeAnswerDto(10L,false);
        
        TakeQuestionDto tq2,tq3;
        tq2=new TakeQuestionDto(2L, List.of(ta4,ta5,ta6));
        tq3=new TakeQuestionDto(3L, List.of(ta7,ta8,ta9));

        TakeQuizDto tqu;
        tqu=new TakeQuizDto(1L, List.of(tq2,tq3));

        qtbu=new QuizTakenByUser();
        qtbu.setId(1L);
        qtbu.setIsPassed(false);
        qtbu.setQuiz(quiz);
        qtbu.setSelectedAnswers(List.of(a1,a4,a7));
        qtbu.setTakenAt(LocalDateTime.now().minusHours(5));
        qtbu.setUserId(15L);

        

        assertThrows(UnAnsweredQuestionException.class, ()->{
            service.takeQuiz(tqu,15L);
        });
    }

    @Test
    @DisplayName("should return true")
    public void quizsAreFinishedTest(){
        Long userId=15L;
        Quiz q1,q2,q3,q4;
        q1=new Quiz();
        q2=new Quiz();
        q3=new Quiz();
        q4=new Quiz();
        q1.setId(1L);
        q2.setId(2L);
        q3.setId(3L);
        q4.setId(4L);

        QuizTakenByUser qtbu1,qtbu2,qtbu3,qtbu4,qtbu5;
        qtbu1=new QuizTakenByUser();
        qtbu2=new QuizTakenByUser();
        qtbu3=new QuizTakenByUser();
        qtbu4=new QuizTakenByUser();
        qtbu5=new QuizTakenByUser();
        
        qtbu1.setIsPassed(true);
        qtbu2.setIsPassed(true);
        qtbu3.setIsPassed(false);
        qtbu4.setIsPassed(true);
        qtbu5.setIsPassed(true);


        when(repo.findBySectionId(1L)).thenReturn(List.of(q1,q2));
        when(repo.findBySectionId(2L)).thenReturn(List.of(q3));
        when(repo.findBySectionId(3L)).thenReturn(List.of(q4));



        when(qRepo.findByQuizIdAndUserId(1L, userId)).thenReturn(List.of(qtbu1));
        when(qRepo.findByQuizIdAndUserId(2L, userId)).thenReturn(List.of(qtbu2));
        when(qRepo.findByQuizIdAndUserId(3L, userId)).thenReturn(List.of(qtbu3,qtbu4));
        when(qRepo.findByQuizIdAndUserId(4L, userId)).thenReturn(List.of(qtbu5));

        boolean actual =service.finishedSections(List.of(1L,2L,3L), userId);
        assertTrue(actual);
    }

    @Test
    @DisplayName("should return false")
    public void quizsAreFinishedTest2(){
        Long userId=15L;
        Quiz q1,q2,q3,q4;
        q1=new Quiz();
        q2=new Quiz();
        q3=new Quiz();
        q4=new Quiz();
        q1.setId(1L);
        q2.setId(2L);
        q3.setId(3L);
        q4.setId(4L);

        QuizTakenByUser qtbu1,qtbu2,qtbu3,qtbu4,qtbu5;
        qtbu1=new QuizTakenByUser();
        qtbu2=new QuizTakenByUser();
        qtbu3=new QuizTakenByUser();
        qtbu4=new QuizTakenByUser();
        qtbu5=new QuizTakenByUser();
        
        qtbu1.setIsPassed(true);
        qtbu2.setIsPassed(true);
        qtbu3.setIsPassed(false);
        qtbu4.setIsPassed(false);
        qtbu5.setIsPassed(true);


        when(repo.findBySectionId(1L)).thenReturn(List.of(q1,q2));
        when(repo.findBySectionId(2L)).thenReturn(List.of(q3));
        when(repo.findBySectionId(3L)).thenReturn(List.of(q4));



        when(qRepo.findByQuizIdAndUserId(1L, userId)).thenReturn(List.of(qtbu1));
        when(qRepo.findByQuizIdAndUserId(2L, userId)).thenReturn(List.of(qtbu2));
        when(qRepo.findByQuizIdAndUserId(3L, userId)).thenReturn(List.of(qtbu3,qtbu4));
        when(qRepo.findByQuizIdAndUserId(4L, userId)).thenReturn(List.of(qtbu5));

        boolean actual =service.finishedSections(List.of(1L,2L,3L), userId);
        assertFalse(actual);
    }

    @Test
    @DisplayName("should return true")
    public void validSectionsTest(){
        Quiz q1,q2,q3;
        Question qu1,qu2,qu3,qu4,qu5,qu6,qu7,qu8,qu9;
        Answer a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18;

        q1=new Quiz();
        q2=new Quiz();
        q3=new Quiz();

        qu1=new Question();
        qu2=new Question();
        qu3=new Question();
        qu4=new Question();
        qu5=new Question();
        qu6=new Question();
        qu7=new Question();
        qu8=new Question();
        qu9=new Question();
        a1=new Answer();a4=new Answer();a7=new Answer();a10=new Answer();a13=new Answer();a16=new Answer();
        a2=new Answer();a5=new Answer();a8=new Answer();a11=new Answer();a14=new Answer();a17=new Answer();
        a3=new Answer();a6=new Answer();a9=new Answer();a12=new Answer();a15=new Answer();a18=new Answer();

        q1.setId(1L);
        q2.setId(2L);
        q3.setId(3L);

        qu1.setId(1L);qu2.setId(2L);qu3.setId(3L);qu4.setId(4L);qu5.setId(5L);qu6.setId(6L);qu7.setId(7L);qu8.setId(8L);qu9.setId(9L);
        
        q1.setQuestions(List.of(qu1,qu2,qu3));
        q2.setQuestions(List.of(qu4,qu5,qu6));
        q3.setQuestions(List.of(qu7,qu8,qu9));

        qu1.setAnswers(List.of(a1,a2));
        qu2.setAnswers(List.of(a3,a4));
        qu3.setAnswers(List.of(a5,a6));
        qu4.setAnswers(List.of(a7,a8));
        qu5.setAnswers(List.of(a9,a10));
        qu6.setAnswers(List.of(a11,a12));
        qu7.setAnswers(List.of(a13,a14));
        qu8.setAnswers(List.of(a15,a16));
        qu9.setAnswers(List.of(a17,a18));

        when(repo.findBySectionId(1L)).thenReturn(List.of(q1,q2));
        when(repo.findBySectionId(2L)).thenReturn(List.of(q3));

        boolean valid=service.validSections(List.of(1L,2L));

        assertTrue(valid);
    }

    @Test
    @DisplayName("should return false")
    public void validSectionsTest2(){
        Quiz q1,q2,q3;
        Question qu1,qu2,qu3,qu4,qu5,qu6,qu7,qu8,qu9;
        Answer a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17;

        q1=new Quiz();
        q2=new Quiz();
        q3=new Quiz();

        qu1=new Question();
        qu2=new Question();
        qu3=new Question();
        qu4=new Question();
        qu5=new Question();
        qu6=new Question();
        qu7=new Question();
        qu8=new Question();
        qu9=new Question();
        a1=new Answer();a4=new Answer();a7=new Answer();a10=new Answer();a13=new Answer();a16=new Answer();
        a2=new Answer();a5=new Answer();a8=new Answer();a11=new Answer();a14=new Answer();a17=new Answer();
        a3=new Answer();a6=new Answer();a9=new Answer();a12=new Answer();a15=new Answer();

        q1.setId(1L);
        q2.setId(2L);
        q3.setId(3L);

        qu1.setId(1L);qu2.setId(2L);qu3.setId(3L);qu4.setId(4L);qu5.setId(5L);qu6.setId(6L);qu7.setId(7L);qu8.setId(8L);qu9.setId(9L);
        
        q1.setQuestions(List.of(qu1,qu2,qu3));
        q2.setQuestions(List.of(qu4,qu5,qu6));
        q3.setQuestions(List.of(qu7,qu8,qu9));

        qu1.setAnswers(List.of(a1,a2));
        qu2.setAnswers(List.of(a3,a4));
        qu3.setAnswers(List.of(a5,a6));
        qu4.setAnswers(List.of(a7,a8));
        qu5.setAnswers(List.of(a9,a10));
        qu6.setAnswers(List.of(a11,a12));
        qu7.setAnswers(List.of(a13,a14));
        qu8.setAnswers(List.of(a15,a16));
        qu9.setAnswers(List.of(a17));

        when(repo.findBySectionId(1L)).thenReturn(List.of(q1,q2));
        when(repo.findBySectionId(2L)).thenReturn(List.of(q3));
        boolean valid=service.validSections(List.of(1L,2L));

        assertFalse(valid);
    }

    @Test
    @DisplayName("should return false")
    public void validSectionsTest3(){
        Quiz q1,q2,q3;
        Question qu1,qu2,qu3,qu4,qu5,qu6,qu7;
        Answer a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14;

        q1=new Quiz();
        q2=new Quiz();
        q3=new Quiz();

        qu1=new Question();
        qu2=new Question();
        qu3=new Question();
        qu4=new Question();
        qu5=new Question();
        qu6=new Question();
        qu7=new Question();
        a1=new Answer();a4=new Answer();a7=new Answer();a10=new Answer();a13=new Answer();
        a2=new Answer();a5=new Answer();a8=new Answer();a11=new Answer();a14=new Answer();
        a3=new Answer();a6=new Answer();a9=new Answer();a12=new Answer();

        q1.setId(1L);
        q2.setId(2L);
        q3.setId(3L);

        qu1.setId(1L);qu2.setId(2L);qu3.setId(3L);qu4.setId(4L);qu5.setId(5L);qu6.setId(6L);qu7.setId(7L);
        
        q1.setQuestions(List.of(qu1,qu2,qu3));
        q2.setQuestions(List.of(qu4,qu5,qu6));
        q3.setQuestions(List.of(qu7));

        qu1.setAnswers(List.of(a1,a2));
        qu2.setAnswers(List.of(a3,a4));
        qu3.setAnswers(List.of(a5,a6));
        qu4.setAnswers(List.of(a7,a8));
        qu5.setAnswers(List.of(a9,a10));
        qu6.setAnswers(List.of(a11,a12));
        qu7.setAnswers(List.of(a13,a14));

        when(repo.findBySectionId(1L)).thenReturn(List.of(q1,q2));
        when(repo.findBySectionId(2L)).thenReturn(List.of(q3));

        boolean valid=service.validSections(List.of(1L,2L));

        assertFalse(valid);
    }
}