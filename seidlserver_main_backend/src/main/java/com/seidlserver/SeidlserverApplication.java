package com.seidlserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/*
    Created by: Jonas Seidl
    Date: 17.03.2021
    Time: 18:21
*/
@SpringBootApplication
public class SeidlserverApplication {
    public static void main(String[] args) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(5000);
                    while(true){
                        //Cpu
                        URL url = new URL("http://localhost:8080/stats/cpu");
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setRequestMethod("GET");
                        con.getInputStream();

                        //Mem
                        url = new URL("http://localhost:8080/stats/mem");
                        con = (HttpURLConnection) url.openConnection();
                        con.setRequestMethod("GET");
                        con.getInputStream();

                        Thread.sleep(60_000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
        SpringApplication.run(SeidlserverApplication.class, args);
    }

}