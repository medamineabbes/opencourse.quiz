package com.opencourse.quiz.externalservices;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.opencourse.quiz.prop.ExternalServicesProp;

@Service
public class AuthenticationService {
    
    private RestTemplate restTemplate;
    private final ExternalServicesProp prop;

    public AuthenticationService(ExternalServicesProp prop){
        restTemplate=new RestTemplate();
        this.prop=prop;
    }

    public Boolean validateToken(String token){
        ResponseEntity<Boolean> response=restTemplate.postForEntity(prop.getAuthUrl(), token, Boolean.class);
        return response.getBody();
    }

}
