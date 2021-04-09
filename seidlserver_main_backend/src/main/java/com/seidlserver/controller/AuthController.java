package com.seidlserver.controller;

import com.seidlserver.db.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/*
    Created by: Jonas Seidl
    Date: 08.04.2021
    Time: 10:59
*/
@RestController
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
        PasswordEncoder encoder = context.getBean("passwordEncoder", PasswordEncoder.class);
        String hash = encoder.encode(password);
        um.addUser(first_name, last_name, email, hash);
        return ResponseEntity.ok().build();
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "$2a$10$5oqkX1y4HgE4Op8H8iotWOEjiHZ.4hZXsuLb7rvIUkBl51qkUZL32";
        String hash = encoder.encode(password);
        System.out.println(hash);
        System.out.println(encoder.matches(password, hash));
    }
}
