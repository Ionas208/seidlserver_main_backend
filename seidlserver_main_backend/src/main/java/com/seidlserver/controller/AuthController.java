package com.seidlserver.controller;

import com.seidlserver.db.UserManager;
import com.seidlserver.pojos.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity register(@RequestBody User user){
        UserManager um = UserManager.getInstance();
        PasswordEncoder encoder = context.getBean("passwordEncoder", PasswordEncoder.class);
        String hash = encoder.encode(user.getPassword());
        um.addUser(user.getFirst_name(), user.getLast_name(), user.getEmail(), hash);
        return ResponseEntity.ok().build();
    }
}
