package com.opencourse.quiz.externalservices;

import org.springframework.stereotype.Service;

@Service
public class CourseService {
    
    public boolean userHasAccessToSection(Long sectionId,Long userId){

        //call courseService
        
        return true;
    }
    public boolean userCreatedSection(Long sectionId,Long userId){
        //call courseService
        return true;
    }
}
