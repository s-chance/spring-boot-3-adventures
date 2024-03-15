package com.entropy.service;

import com.entropy.pojo.Article;
import com.entropy.pojo.PageBean;

public interface ArticleService {

    // 新增文章
    void add(Article article);

    // 条件分页列表查询
    PageBean<Article> list(Integer pageNum, Integer pageSize, Integer categoryId, String state);

    // 根据 id 查询文章详情
    Article findById(Integer id);

    // 更新文章
    void update(Article article);

    // 根据 id 删除文章
    void deleteById(Integer id);
}
