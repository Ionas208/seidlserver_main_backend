package com.seidlserver.pojos.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
    Created by: Jonas Seidl
    Date: 07.04.2021
    Time: 11:15
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Login {
    public String username;
    private String password;
}
