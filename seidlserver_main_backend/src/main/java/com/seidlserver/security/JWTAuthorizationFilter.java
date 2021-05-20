package com.seidlserver.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.seidlserver.db.UserManager;
import com.seidlserver.pojos.user.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/*
    Created by: Jonas Seidl
    Date: 07.04.2021
    Time: 11:36
*/
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(JWTProperties.HEADER_STRING);

        if (header == null || !header.startsWith(JWTProperties.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        Authentication auth = getUsernamePasswordAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(auth);

        chain.doFilter(request, response);
    }

    private Authentication getUsernamePasswordAuthentication(HttpServletRequest request){
        String token = request.getHeader(JWTProperties.HEADER_STRING);
        if(token != null){
            String username = JWT.require(Algorithm.HMAC512(JWTProperties.SECRET.getBytes()))
                    .build()
                    .verify(token.replace(JWTProperties.TOKEN_PREFIX, ""))
                    .getSubject();

            if(username != null){
                UserManager um = UserManager.getInstance();
                User user = um.getUserByEmail(username);
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(user.getUsername(), null, user.getAuthorities());
                return auth;
            }
            return null;
        }
        return null;
    }
}
