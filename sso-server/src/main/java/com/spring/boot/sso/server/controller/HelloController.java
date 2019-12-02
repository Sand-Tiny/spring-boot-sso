package com.spring.boot.sso.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("hello")
    public String helloword(String name) {
        String helloname = "hello " + name;
        System.out.println(helloname);
        return helloname;
    }

}
