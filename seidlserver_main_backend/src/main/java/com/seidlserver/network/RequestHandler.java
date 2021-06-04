package com.seidlserver.network;

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
    public static String API = "http://seidlserver.ddns.net:8080/";

    public static String sendRequest(String entrypoint, String method, Boolean checkForError) throws Exception {
        URL url = new URL(API+entrypoint);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(method);
        String data = null;
        try{
            data = new BufferedReader(new InputStreamReader(con.getInputStream())).lines().collect(Collectors.joining());
        }catch(IOException ex){
            if(checkForError){
                ex.printStackTrace();
                int code = con.getResponseCode();
                if(code!=200){
                    throw new Exception("Error: Server might already be running");
                }
            }
        }
        return data;
    }
}
