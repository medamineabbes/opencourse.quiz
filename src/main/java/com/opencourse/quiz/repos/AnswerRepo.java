package com.opencourse.quiz.repos;
import com.opencourse.quiz.entities.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface AnswerRepo extends JpaRepository<Answer,Long>{
    
}
