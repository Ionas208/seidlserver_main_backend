package com.seidlserver.controller;

import com.seidlserver.db.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/*
    Created by: Jonas Seidl
    Date: 08.04.2021
    Time: 10:59
*/
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private ApplicationContext context;

    @PostMapping("/register")
    public ResponseEntity register(
            @RequestParam String first_name,
            @RequestParam String last_name,
            @RequestParam String email,
            @RequestParam String password
    ){
        UserManager um = UserManager.getInstance();
        PasswordEncoder encoder = context.getBean("passwordEncoer", PasswordEncoder.class);
        um.addUser(first_name, last_name, email, encoder.encode(password));
        return ResponseEntity.ok().build();
    }
}
