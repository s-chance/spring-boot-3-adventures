package com.entropy.dmybatisspringbootdemo.service;

import com.entropy.dmybatisspringbootdemo.pojo.User;

public interface UserService {

    public User findById(Integer id);
}
