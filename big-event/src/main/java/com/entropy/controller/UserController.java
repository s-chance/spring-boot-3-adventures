package com.entropy.controller;


import com.entropy.pojo.Result;
import com.entropy.pojo.User;
import com.entropy.service.UserService;
import com.entropy.utils.JwtUtil;
import com.entropy.utils.ThreadLocalUtil;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("/register")
    public Result register(@Pattern(regexp = "^\\S{5,16}$") String username, @Pattern(regexp = "^\\S{5,16}$") String password) {

        // 查询用户
        User user = userService.findByUsername(username);
        if (user == null) {
            // 未注册
            // 注册
            userService.register(username, password);
            return Result.success();
        } else {
            // 已注册
            return Result.error("用户名已注册");
        }
    }

    @PostMapping("/login")
    public Result<String> login(@Pattern(regexp = "^\\S{5,16}$") String username, @Pattern(regexp = "^\\S{5,16}$") String password) {
        // 根据用户名查询用户
        User loginUser = userService.findByUsername(username);
        // 判断用户是否存在
        if (loginUser == null) {
            return Result.error("用户名不存在");
        }

        // 判断密码是否正确
        // 密文解密
        if (password.equals(loginUser.getPassword())) {
            // 登录成功
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", loginUser.getId());
            claims.put("username", loginUser.getUsername());
            String token = JwtUtil.genToken(claims);
            // 将 token 存储到 redis 中
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            operations.set(token, token, 12, TimeUnit.HOURS); // 设置 12 小时过期时间
            return Result.success(token);
        }

        return Result.error("密码错误");
    }

    @GetMapping("/userInfo")
    public Result<User> userInfo(/*@RequestHeader(name = "Authorization") String token*/) {
        // 根据用户名查询用户
        /*Map<String, Object> claims = JwtUtil.parseToken(token);
        String username = claims.get("username").toString();*/
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = map.get("username").toString();
        User user = userService.findByUsername(username);
        return Result.success(user);
    }

    @PutMapping("/update")
    public Result update(@RequestBody @Validated User user) {
        userService.update(user);
        return Result.success();
    }

    @PatchMapping("/updateAvatar")
    public Result updateAvatar(@RequestParam @URL String avatarUrl) {
        userService.updateAvatar(avatarUrl);
        return Result.success();
    }

    @PatchMapping("/updatePwd")
    public Result updatePwd(@RequestBody Map<String, String> params, @RequestHeader("Authorization") String token) {
        // 1.校验参数
        String oldPwd = params.get("old_pwd");
        String newPwd = params.get("new_pwd");
        String rePwd = params.get("re_pwd");

        if (!StringUtils.hasLength(oldPwd) || !StringUtils.hasLength(newPwd) || !StringUtils.hasLength(rePwd)) {
            return Result.error("缺少必要的参数");
        }

        // 原密码是否正确
        // 调用 userService 根据用户名拿到原密码，再和 oldPwd 比对
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = map.get("username").toString();
        User loginUser = userService.findByUsername(username);
        if (!loginUser.getPassword().equals(oldPwd)) {
            return Result.error("原密码错误");
        }

        // newPwd 和 rePwd 是否一样
        if (!rePwd.equals(newPwd)) {
            return Result.error("两次填写的新密码不一致");
        }
        // 2.调用 service 完成密码更新
        userService.updatePwd(newPwd);
        // 删除 redis 中对应的 token
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.getOperations().delete(token);
        return Result.success();
    }
}
