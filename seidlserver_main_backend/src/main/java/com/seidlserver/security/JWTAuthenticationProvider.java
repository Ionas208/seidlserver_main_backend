package com.seidlserver.security;

import com.seidlserver.pojos.user.User;
import com.seidlserver.service.UserService;
import com.seidlserver.util.ApplicationContextProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/*
    Created by: Jonas Seidl
    Date: 09.04.2021
    Time: 17:49
*/
@Component
public class JWTAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        User user = (User) userService.loadUserByUsername(name);
        String hash = user.getPassword();

        ApplicationContext context = ApplicationContextProvider.getContext();
        PasswordEncoder encoder = context.getBean("passwordEncoder", PasswordEncoder.class);

        /*System.out.println("-------------------------");
        System.out.println("User from DB:" + user);
        System.out.println("Name from Req: " + name);
        System.out.println("Password from Req: " + password);
        System.out.println("Hash from DB: " + hash);
        System.out.println("Matches: " + encoder.matches(password, hash));
        System.out.println("-------------------------");*/

        UsernamePasswordAuthenticationToken token;

        if(encoder.matches(password, hash)){
            token = new UsernamePasswordAuthenticationToken(name, password, new ArrayList<>());
        }
        else{
            token = null;
            //token = new UsernamePasswordAuthenticationToken(name, password);
        }
        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
