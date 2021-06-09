package com.seidlserver.security;

/*
    Created by: Jonas Seidl
    Date: 07.04.2021
    Time: 11:12
*/

/***
 * Class that holds all properties for our JWT Tokens
 */
public class JWTProperties {
    public static final String SECRET = "LMAO";
    public static final long EXPIRATION_TIME = 60*60*24*365*1000;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String LOGIN_URL = "/auth/login";
    public static final String REGISTER_URL = "/auth/register";
}
