package com.entropy.dmybatisspringbootdemo.controller;

import com.entropy.dmybatisspringbootdemo.pojo.User;
import com.entropy.dmybatisspringbootdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("findById")
    public User findById(Integer id) {
        return userService.findById(id);
    }
}
