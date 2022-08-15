package com.opencourse.quiz.repos;

import com.opencourse.quiz.entities.Quiz;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepo extends JpaRepository<Quiz,Long>{
    List<Quiz> findBySectionId(Long sectionId);
}
