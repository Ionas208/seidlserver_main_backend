package com.seidlserver.network;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

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

/***
 * Class for sending requests to the API
 */
//@PropertySource("/application.properties")
public class RequestHandler {
    //@Value("${api.url}")
    //api.url=http://seidlserver.ddns.net:8080
    public static String API = "http://seidlserver.ddns.net:8080";

    /***
     * Sends a request to the API
     * @param entrypoint The entrypoint on the API (i.e. start)
     * @param method The request method (GET, POST, ...)
     * @param checkForError Whether to check for errors
     * @return The Response from the API
     * @throws Exception
     */
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
                con.getResponseCode();
                throw ex;
            }
        }
        return data;
    }
}
