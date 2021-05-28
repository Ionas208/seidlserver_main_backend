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

        URL url = new URL(API+entrypoint);
        int code = con.getResponseCode();
        }
        return data;
    }
}
