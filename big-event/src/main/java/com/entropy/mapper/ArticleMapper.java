package com.entropy.mapper;


import com.entropy.pojo.Article;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleMapper {

    // 新增文章
    @Insert("insert into article (title, content, cover, state, category_id, author_id, create_time, update_time) " +
            "values (#{title},#{content},#{cover},#{state},#{categoryId},#{authorId},#{createTime},#{updateTime})")
    void add(Article article);


    List<Article> list(Integer authorId, Integer categoryId, String state);


    // 根据 id 查询文章分类详情
    @Select("select * from article where id=#{id}")
    Article findById(Integer id);

    // 更新文章
    @Update("update article set title=#{title},content=#{content},cover=#{cover},state=#{state}," +
            "category_id=#{categoryId},update_time=#{updateTime} where id=#{id}")
    void update(Article article);

    // 根据 id 删除文章
    @Delete("delete from article where id=#{id}")
    void deleteById(Integer id);
}
