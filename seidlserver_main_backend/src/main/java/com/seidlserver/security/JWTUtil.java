package com.seidlserver.security;

import org.springframework.stereotype.Component;

import java.io.Serializable;

/*
    Created by: Jonas Seidl
    Date: 26.03.2021
    Time: 10:46
*/
@Component
public class JWTUtil implements Serializable {
    private static final long serialVersionUID = -123;

    public static final long JWT_TOKEN_VALIDITY = 50*60*60;

    public String getEmailFromToken(String token){
        return null;
    }
}
