package com.seidlserver.network;

import org.springframework.http.HttpMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Collectors;

/*
    Created by: Jonas Seidl
    Date: 28.04.2021
    Time: 11:32
*/
public class RequestHandler {
    public static String API = "http://10.0.0.1:8080/";
    public static String sendRequest(String entrypoint, String method) throws MalformedURLException {
        URL url = new URL(API+entrypoint);
        try {
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(method);
            String data = new BufferedReader(new InputStreamReader(con.getInputStream())).lines().collect(Collectors.joining());
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
