package com.entropy.service.impl;

import com.entropy.mapper.ArticleMapper;
import com.entropy.pojo.Article;
import com.entropy.pojo.PageBean;
import com.entropy.service.ArticleService;
import com.entropy.utils.ThreadLocalUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public void add(Article article) {
        // 补充属性值
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer authorId = (Integer) map.get("id");
        article.setAuthorId(authorId);
        articleMapper.add(article);
    }

    @Override
    public PageBean<Article> list(Integer pageNum, Integer pageSize, Integer categoryId, String state) {
        // 1.创建 PageBean 对象
        PageBean<Article> pb = new PageBean<>();

        // 2.开启分页查询 PageHelper
        PageHelper.startPage(pageNum, pageSize);

        // 3.调用 mapper 查询
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer authorId = (Integer) map.get("id");
        List<Article> as = articleMapper.list(authorId, categoryId, state);
        // Page 中提供了方法，可以获取 PageHelper 分页查询结果的总记录条数和当前页数据集合
        Page<Article> p = (Page<Article>) as;

        // 把数据填充到 PageBean 对象中
        pb.setTotal(p.getTotal());
        pb.setItems(p.getResult());
        return pb;
    }

    @Override
    public Article findById(Integer id) {
        return articleMapper.findById(id);
    }

    @Override
    public void update(Article article) {
        article.setUpdateTime(LocalDateTime.now());
        articleMapper.update(article);
    }

    @Override
    public void deleteById(Integer id) {
        articleMapper.deleteById(id);
    }
}
