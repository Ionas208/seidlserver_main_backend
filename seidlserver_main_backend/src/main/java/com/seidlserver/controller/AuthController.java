package com.seidlserver.controller;

import com.seidlserver.db.UserManager;
import com.seidlserver.pojos.user.User;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/*
    Created by: Jonas Seidl
    Date: 08.04.2021
    Time: 10:59<
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
        try{
            um.addUser(user.getFirst_name(), user.getLast_name(), user.getEmail(), hash);
            return ResponseEntity.ok().build();
        } catch(HibernateException e){
            return new ResponseEntity(e.getMessage(), HttpStatus.CONFLICT);
        }
        catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/change/password")
    public ResponseEntity change_password(@RequestParam String oldPassword, @RequestParam String newPassword){
        UserManager um = UserManager.getInstance();
        PasswordEncoder encoder = context.getBean("passwordEncoder", PasswordEncoder.class);
        User u = getUser();
        String hash_new = encoder.encode(newPassword);
        try{
            if(BCrypt.checkpw(oldPassword, u.getPassword())){
                um.changePassword(u.getId(), hash_new);
            }else{
                return new ResponseEntity("Old password is wrong.", HttpStatus.BAD_REQUEST);
            }

            return ResponseEntity.ok().build();
        } catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/change/email")
    public ResponseEntity change_email(@RequestParam String password, @RequestParam String email){
        UserManager um = UserManager.getInstance();
        User u = getUser();
        try{
            if(BCrypt.checkpw(password, u.getPassword())){
                um.changeEmail(u.getId(), email);
            }else{
                return new ResponseEntity("Old password is wrong.", HttpStatus.BAD_REQUEST);
            }

            return ResponseEntity.ok().build();
        } catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private User getUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserManager um = UserManager.getInstance();
        return um.getUserByEmail((String)auth.getPrincipal());
    }
}
