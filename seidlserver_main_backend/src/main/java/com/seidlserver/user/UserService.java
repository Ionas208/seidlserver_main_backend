package com.seidlserver.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
    Created by: Jonas Seidl
    Date: 26.03.2021
    Time: 10:41
*/
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

}
