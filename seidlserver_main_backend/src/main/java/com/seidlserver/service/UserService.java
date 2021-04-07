package com.seidlserver.service;

import com.seidlserver.db.UserManager;
import com.seidlserver.pojos.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/*
    Created by: Jonas Seidl
    Date: 26.03.2021
    Time: 10:41
*/
@Service
public class UserService implements UserDetailsService {

    @Autowired
    private ApplicationContext context;

    public UserService(){
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserManager um = UserManager.getInstance();
        return um.getUserByEmail(username);
    }
}
