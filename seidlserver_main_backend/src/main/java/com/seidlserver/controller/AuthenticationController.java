package com.seidlserver.controller;

import com.seidlserver.db.DBAccess;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

/*
    Created by: Jonas Seidl
    Date: 17.03.2021
    Time: 18:13
*/
@RestController
@CrossOrigin
public class AuthenticationController {

    @PostMapping("/register")
    public String register(
            @RequestParam(name="first_name", defaultValue = "") String first_name,
            @RequestParam(name="last_name", defaultValue = "") String last_name,
            @RequestParam(name="email", defaultValue = "") String email,
            @RequestParam(name="password", defaultValue = "") String password
    ){
        if(!(first_name.equals("") || last_name.equals("") || email.equals("") || password.equals(""))){
            try{
                DBAccess dba = DBAccess.getInstance();
                dba.register(first_name, last_name, email, password);
                return "successfully registered";
            }catch (SQLException ex){
                return ex.toString();
            }
        }
        return "Missing arguments";
    }

    @GetMapping("/login")
    public String login(
            @RequestParam(name="email", defaultValue = "") String email,
            @RequestParam(name="password", defaultValue = "") String password)
    {
        if(!(email.equals("") || password.equals(""))){
            try{
                DBAccess dba = DBAccess.getInstance();
                return dba.login(email, password)+"";
            }catch (SQLException ex){
                return ex.toString();
            }
        }
        return false+"";
    }
}
