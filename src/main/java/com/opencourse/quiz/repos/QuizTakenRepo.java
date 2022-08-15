package com.opencourse.quiz.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.opencourse.quiz.entities.QuizTakenByUser;

@Repository
public interface QuizTakenRepo extends JpaRepository<QuizTakenByUser,Long>{
    List<QuizTakenByUser> findByQuizIdAndUserId(Long id,Long userId);
}
