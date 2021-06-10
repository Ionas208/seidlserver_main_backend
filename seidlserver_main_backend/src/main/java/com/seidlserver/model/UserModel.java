package com.seidlserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
    Created by: Jonas Seidl
    Date: 10.06.2021
    Time: 19:32
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
    private String first_name;
    private String last_name;
    private String email;
    private String password;
}
