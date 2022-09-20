package com.opencourse.quiz.security;

import java.util.Base64;
import java.util.Base64.Decoder;

import org.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.opencourse.quiz.externalservices.AuthenticationService;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class JwtProvider {
    
    private final AuthenticationService auth;

    public Boolean isValide(String token){
        return auth.validateToken(token);
    }

    public Authentication getAuth(String token){
        Decoder decoder=Base64.getDecoder();
        String[] chunks=token.split("\\.");
        String payload=new String(decoder.decode(chunks[1]));
        JSONObject body=new JSONObject(payload);
        Authentication authentication=new JwtAuthentication(body.getLong("id"),  
        body.getString("role"), 
        true);
        return authentication;
    }

}
