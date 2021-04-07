package com.seidlserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/*
    Created by: Jonas Seidl
    Date: 07.04.2021
    Time: 20:03
*/
@RestController
public class TestController {
    @GetMapping("/test")
    public String hello(){
        return "Test successful";
    }
}
