package com.entropy.service;

import com.entropy.pojo.Category;

import java.util.List;

public interface CategoryService {
    // 新增文章分类
    void add(Category category);

    // 文章分类列表
    List<Category> list();

    // 根据 id 查询文章分类详情
    Category findById(Integer id);

    // 更新文章分类
    void update(Category category);

    // 根据 id 删除文章分类
    void deleteById(Integer id);
}
