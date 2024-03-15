package com.entropy.mapper;

import com.entropy.pojo.Category;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryMapper {

    // 新增
    @Insert("insert into category(category_name, category_alias, author_id, create_time, update_time) " +
            "values (#{categoryName},#{categoryAlias},#{authorId},#{createTime},#{updateTime})")
    void add(Category category);


    // 列表查询
    @Select("select * from category where author_id=#{authorId}")
    List<Category> list(Integer authorId);


    // 根据 id 查询文章分类详情
    @Select("select * from category where id=#{id}")
    Category findById(Integer id);

    // 更新文章分类
    @Update("update category set category_name=#{categoryName},category_alias=#{categoryAlias},update_time=#{updateTime} where id=#{id}")
    void update(Category category);

    // 根据 id 删除文章分类
    @Delete("delete from category where id=#{id}")
    void deleteById(Integer id);
}
