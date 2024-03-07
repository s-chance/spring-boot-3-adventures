package com.entropy.dmybatisspringbootdemo.mapper;

import com.entropy.dmybatisspringbootdemo.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("select * from user where id = #{id}")
    public User findById(Integer id);
}
