package com.entropy.controller;

import com.entropy.pojo.Article;
import com.entropy.pojo.PageBean;
import com.entropy.pojo.Result;
import com.entropy.service.ArticleService;
import com.entropy.utils.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/article")
public class ArticleController {

    /*@GetMapping("/list")
    public Result<String> list(*//*@RequestHeader(name = "Authorization") String token, HttpServletResponse response*//*) {
        // 验证 token
        *//*try {
            Map<String, Object> claims = JwtUtil.parseToken(token);
            return Result.success("所有文章数据....");
        } catch (Exception e) {
            // http 响应状态码为 401
            response.setStatus(401);
            return Result.error("未登录");
        }*//*
        return Result.success("所有文章数据....");
    }*/

    @Autowired
    private ArticleService articleService;

    @PostMapping
    public Result add(@RequestBody @Validated Article article) {
        articleService.add(article);
        return Result.success();
    }

    @GetMapping
    public Result<PageBean<Article>> list(Integer pageNum,
                                          Integer pageSize,
                                          @RequestParam(required = false) Integer categoryId,
                                          @RequestParam(required = false) String state) {
        PageBean<Article> pb = articleService.list(pageNum, pageSize, categoryId, state);
        return Result.success(pb);
    }

    @GetMapping("/detail")
    public Result<Article> detail(Integer id) {
        Article article = articleService.findById(id);
        return Result.success(article);
    }

    @PutMapping
    public Result update(@RequestBody @Validated Article article) {
        articleService.update(article);
        return Result.success();
    }

    @DeleteMapping
    public Result delete(@RequestBody Map<String, Integer> param) {
        Integer id = param.get("id");
        if (id == null) {
            return Result.error("缺少必要的参数");
        }
        articleService.deleteById(id);
        return Result.success();
    }
}
