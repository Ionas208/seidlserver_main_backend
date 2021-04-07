package com.seidlserver.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seidlserver.pojos.user.Login;
import com.seidlserver.pojos.user.User;
import lombok.SneakyThrows;
import org.springframework.core.log.LogMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.auth0.jwt.JWT;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

/*
    Created by: Jonas Seidl
    Date: 07.04.2021
    Time: 11:16
*/
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authManager;

    public JWTAuthenticationFilter(AuthenticationManager authManager){
        this.authManager = authManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try{
            Login creds = new ObjectMapper().readValue(request.getInputStream(), Login.class);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    creds.getUsername(),
                    creds.getPassword(),
                    new ArrayList<>()
            );
            Authentication auth = authManager.authenticate(authToken);
            return auth;
        }catch(IOException ex){
            ex.printStackTrace();
            return null;
        }
    }
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        User u = (User)authResult.getPrincipal();
        String token = JWT.create()
                .withSubject(u.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JWTProperties.EXPIRATION_TIME))
                .sign(HMAC512(JWTProperties.SECRET.getBytes()));

        response.addHeader(JWTProperties.HEADER_STRING, JWTProperties.TOKEN_PREFIX + token);
    }
}
