package com.seidlserver.security;

/*
    Created by: Jonas Seidl
    Date: 07.04.2021
    Time: 11:12
*/
public class JWTProperties {
    public static final String SECRET = "LMAO";
    public static final int EXPIRATION_TIME = 60*60*24;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String LOGIN_URL = "/auth/login";
    public static final String REGISTER_URL = "/auth/register";
}
