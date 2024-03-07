package com.entropy.dmybatisspringbootdemo.service.impl;

import com.entropy.dmybatisspringbootdemo.mapper.UserMapper;
import com.entropy.dmybatisspringbootdemo.pojo.User;
import com.entropy.dmybatisspringbootdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findById(Integer id) {
        return userMapper.findById(id);
    }
}
