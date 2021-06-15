package com.seidlserver.controller;

import com.seidlserver.db.UserManager;
import com.seidlserver.model.UserModel;
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

/***
 * Controller for authentication related requests
 * Reachable under /auth/
 */
@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private ApplicationContext context;

    /***
     * Entrypoint for registering a new user
     * @param user Json Model in the Request Body for the new user
     * @return ResponseEntity with Code
     *         200 OK: When register was successful
     *         400 BAD REQUEST: When there is missing data
     *         409 CONFLICT: When there is a duplicate email
     *         500 INTERNAL SERVER ERROR: When there is some other error
     *                                    Error message is included in the response body
     */
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody UserModel user){
        UserManager um = UserManager.getInstance();

        if(user.getEmail().equals("") || user.getPassword().equals("") || user.getFirst_name().equals("") || user.getLast_name().equals("")){
            return new ResponseEntity("All fields must not be empty.", HttpStatus.BAD_REQUEST);
        }

        PasswordEncoder encoder = context.getBean("passwordEncoder", PasswordEncoder.class);
        String hash = encoder.encode(user.getPassword());
        try{
            um.addUser(user.getFirst_name(), user.getLast_name(), user.getEmail().replace(" ",""), hash);
            return ResponseEntity.ok().build();
        } catch(HibernateException e){
            return new ResponseEntity(e.getMessage(), HttpStatus.CONFLICT);
        }
        catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /***
     * Entrypoint for changing a password
     * @param oldPassword The old password used for authenticating the change
     * @param newPassword The new password to be changed to
     * @return ResponseEntity with Code
     *         200 OK: When the change was successful
     *         400 BAD REQUEST: When the password authentication was unsuccessful
     *         500 INTERNAL SERVER ERROR: When there is some other error
     *                                    Error message is included in the response body
     */
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

    /***
     * Entrypoint for changing the email
     * @param password  The password used for authenticating the change
     * @param email The new email to be changed to
     * @return ResponseEntity with Code
     *         200 OK: When the change was successful
     *         400 BAD REQUEST: When the password authentication was unsuccessful
     *         500 INTERNAL SERVER ERROR: When there is some other error
     *                                    Error message is included in the response body
     */
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

    /***
     * This method fetches the user from the current Security Context
     * (i.e. from the JWT token sent with the request)
     * @return current User
     */
    private User getUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserManager um = UserManager.getInstance();
        return um.getUserByEmail((String)auth.getPrincipal());
    }
}
