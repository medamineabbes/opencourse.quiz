package com.opencourse.quiz.externalservices;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.opencourse.quiz.prop.ExternalServicesProp;

import lombok.Data;

@Service
public class CourseService {
    
    private RestTemplate restTemplate;
    private final ExternalServicesProp prop;

    public CourseService(ExternalServicesProp prop){
        this.prop=prop;
        restTemplate=new RestTemplate();
    }

    public boolean userHasAccessToSection(Long sectionId,Long userId){
        AccessDto dto=new AccessDto();
        dto.setSectionId(sectionId);
        dto.setUserId(userId);
        ResponseEntity<Boolean> response=restTemplate.postForEntity(prop.getSectionAccessUrl(),dto,Boolean.class);
        return response.getBody();
    }

    public boolean userCreatedSection(Long sectionId,Long userId){
        AccessDto dto=new AccessDto();
        dto.setSectionId(sectionId);
        dto.setUserId(userId);
        ResponseEntity<Boolean> response=restTemplate.postForEntity(prop.getSectionCreatorUrl(), dto, Boolean.class);
        return response.getBody();
    }
}

@Data
class AccessDto{
    private Long userId;
    private Long sectionId; 
}