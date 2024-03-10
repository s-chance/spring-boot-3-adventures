## 大事记系统后端

### 环境搭建

工具：docker compose + mysql

#### 1.数据库容器

docker-compose.yaml 如下

```yaml
version: "3.1"

services:
  db:
    image: mysql
    environment:
      MYSQL_ROOT_PASSWORD: ${MYSQL_ROOT_PASSWORD}
      MYSQL_DATABASE: ${MYSQL_DATABASE}
      MYSQL_USER: ${MYSQL_USER}
      MYSQL_PASSWORD: ${MYSQL_PASSWORD}
    ports:
      - 3306:3306
```

环境变量模板`.env.template`文件如下，根据实际需求设置参数后重命名为`.env`

```env
MYSQL_ROOT_PASSWORD=
MYSQL_DATABASE=
MYSQL_USER=
MYSQL_PASSWORD=
```

使用`docker-compose up -d`启动(每次重启需要手动启动)

> 不进行持久化挂载，删除容器后需要重新执行 sql

##### 2.数据准备

sql 文件如下

```mysql
# 用户表
create table user
(
    id          int unsigned primary key auto_increment comment 'ID',
    username    varchar(20) not null unique comment '用户名',
    password    varchar(32) comment '密码',
    nickname    varchar(10)  default '' comment '昵称',
    email       varchar(128) default '' comment '邮箱',
    avatar      varchar(128) default '' comment '头像',
    create_time datetime    not null comment '创建时间',
    update_time datetime    not null comment '修改时间'
) comment '用户表';

# 分类表
create table category
(
    id             int unsigned primary key auto_increment comment 'ID',
    category_name  varchar(32)  not null comment '分类名称',
    category_alias varchar(32)  not null comment '分类别名',
    author_id      int unsigned not null comment '作者ID',
    create_time    datetime     not null comment '创建时间',
    update_time    datetime     not null comment '修改时间',
    constraint fk_category_user foreign key (author_id) references user (id) -- 外键
) comment '文章分类';

# 文章表
create table article
(
    id          int unsigned primary key auto_increment comment 'ID',
    title       varchar(30)  not null comment '文章标题',
    content     text         not null comment '文章内容',
    cover       varchar(128) not null comment '文章封面',
    state       varchar(3) default '草稿' comment '文章状态：已发布/草稿',
    category_id int unsigned comment '文章分类ID',
    author_id   int unsigned not null comment '作者ID',
    create_time datetime     not null comment '创建时间',
    update_time datetime     not null comment '修改时间',
    constraint fk_article_category foreign key (category_id) references category (id),
    constraint fk_article_user foreign key (author_id) references user (id) -- 外键
) comment '文章表';
```

使用数据库连接工具连接后执行即可

#### 3.Java 实体类

根据数据表字段编写即可，可以考虑使用 lombok 简化代码

### 接口文档

用户：6 个基本接口

> - 注册
> - 登录
> - 获取用户详细信息
> - 更新用户基本信息
> - 更新用户头像
> - 更新用户密码
>
> 明确需求、阅读接口文档、思路分析、开发、测试

### 注册

明确需求：注册需要用户名、密码，密码需要二次确认

接口文档查看[此处](interface.md)

根据接口文档，编写一个统一响应结果的实体类

```java
// 统一响应结果
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    // 快速返回操作成功响应结果(带响应数据)
    public static <E> Result<E> success(E data) {
        return new Result<>(0, "操作成功", data);
    }

    // 快速返回操作成功响应结果
    public static Result success() {
        return new Result(0, "操作成功", null);
    }

    public static Result error(String message) {
        return new Result(1, message, null);
    }

}
```

开发：采用三层架构，依次从 controller、service、mapper 分析

controller
```java
@PostMapping("/register")
public 返回值类型 register(String username, String password) {
    // 用户名是否已注册
    // 注册
}
```

service
```java
// 根据用户名查询用户
public User findByUsername(String username) {
}

// 注册
public void register(String username, String password) {
}
```

mapper
```sql
-- 查询
select * from user where username = ?;

-- 新增
insert into user(username, password,create_time,update_time) values (?,?,?,?);
```

关于`create_time`和`update_time`的处理，有两种方案：

- 使用 Java 提供的`LocalDateTime.now()`设置，这可以避免 8 小时时差问题，但是可能增加代码复杂性，需要额外传递时间参数或者是携带时间属性的复合类型参数。
- 使用 mysql 提供的`now()`设置，这可能会产生与当前系统相差 8 小时的问题，可以通过设置 mysql 的时区解决。

测试：目前还没涉及前端，可以接口测试工具测试接口

#### 参数校验

controller 修改如下，但是这种方法比较繁琐
```java
@PostMapping("/register")
public Result register(String username, String password) {

    if (username != null && username.length() >= 5 && username.length() <= 16
        && password != null && password.length() >= 5 && password.length() <= 16) {
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
    } else {
        return Result.error("参数不合法");
    }
}
```

#### 使用 Spring Validation

Spring 提供的一个参数校验框架，使用预定义的注解完成参数校 验

1. 引入 Spring Validation 起步依赖

   ```xml
   <dependencies>
       <!--validation-->
       <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-validation</artifactId>
       </dependency>
   </dependencies>
   ```

2. 在参数前面添加 @Pattern 注解

   ```java
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
   ```

3. 在 UserController 类上添加 @Validated 注解

   ```java
   @RestController
   @RequestMapping("/user")
   @Validated
   public class UserController {
       //...
   }
   ```

   不过这样参数校验失败的时候会返回不符合接口文档的异常信息，需要对参数校验失败的异常情况进行自定义处理。

4. 在全局异常处理器中处理参数校验失败的异常

   ```java
   package com.entropy.exception;
   
   import com.entropy.pojo.Result;
   import org.springframework.util.StringUtils;
   import org.springframework.web.bind.annotation.ExceptionHandler;
   import org.springframework.web.bind.annotation.RestControllerAdvice;
   
   @RestControllerAdvice
   public class GlobalExceptionHandler {
   
       @ExceptionHandler(Exception.class)
       public Result handleException(Exception e) {
           e.printStackTrace();
           return Result.error(StringUtils.hasLength(e.getMessage()) ? e.getMessage() : "操作失败");
       }
   }
   ```



### 登录

根据接口文档

controller

```java
@PostMapping("/login")
public Result login(@Pattern(regexp="^\\S{5,16}$") String username, @Pattern(regexp="^\\S{5,16}$") String password) {
    // 根据用户名查询用户
    // 判断用户是否存在
    // 判断密码是否正确
}
```

service

```java
// 根据用户名查询用户
public User findByUsername(String username) {
}
```

mapper

```sql
-- 查询
select * from user where username=?;
```

#### 登录认证

在未登录情况下，可以访问到其他资源，这存在安全问题——使用 JWT 技术。

令牌就是一段字符串

- 承载业务数据，减少后续请求查询数据库的次数
- 防篡改，保证信息的合法性和有效性

#### JWT 令牌

简介

- 全称：JSON Web Token (https://jwt.io/)
- 定义了一种简洁的、自包含的格式，用于通信双方以 json 数据格式安全地传输信息。
- 组成：
  - 第一部分：Header(头)，记录令牌类型、签名算法等。例如：{"alg":"HS256","type":"JWT"}
  - 第二部分：Payload(有效载荷)，携带一些自定义信息、默认信息等。例如，{"id":"1","username":"Tom"}
  - 第三部分：Signature(签名)，防止 Token 被篡改，确保安全性。将 header、payload 加上指定密钥，通过指定签名算法计算出来。

JWT 生成与验证

1. 引入 jwt 起步依赖和单元测试依赖

   ```xml
   <dependencies>
       <!--jwt-->
       <dependency>
           <groupId>com.auth0</groupId>
           <artifactId>java-jwt</artifactId>
           <version>4.4.0</version>
       </dependency>
   
       <!--test-->
       <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-test</artifactId>
       </dependency>
   </dependencies>
   ```

2. 编写测试代码

   ```java
   public class JwtTest {
   
       @Test
       public void testGen() {
           Map<String, Object> claims = new HashMap<>();
           claims.put("id", 1);
           claims.put("username", "black");
           // 生成 jwt
           String token = JWT.create()
                   .withClaim("user", claims) // 添加载荷
                   .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 12)) // 添加过期时间
                   .sign(Algorithm.HMAC256("secret"));// 指定算法，配置密钥
   
           System.out.println(token);
       }
   }
   ```

   注意：密钥是不能泄露的，实际开发也不会将密钥直接硬编码到代码中，这里只是方便演示。

3. 编写验证测试代码

   ```java
   @Test
   public void testParse() {
       // 定义字符串，模拟用户传递的 token
       String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9" +
           ".eyJ1c2VyIjp7ImlkIjoxLCJ1c2VybmFtZSI6ImJsYWNrIn0sImV4cCI6MTcwOTkzMDY1Nn0" +
           ".9ztgvOSUI2nmI0gdJ2-rL5pt9-q7GhmW4nVi1ShCpfw";
   
       // jwt 验证器，需要指定正确的算法和密钥
       JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("secret")).build();
   
       // 验证 token，返回解析后的结果
       DecodedJWT decodedJWT = jwtVerifier.verify(token);
   
       Map<String, Claim> claims = decodedJWT.getClaims();
       System.out.println(claims.get("user"));
   }
   ```

   头部、载荷、密钥，任何一个部分被修改后都会导致认证失败，超过了有效时间，也会因为 token 过期而认证失败。

   注意事项：

   - JWT 校验时使用的签名密钥，必须和生成 JWT 令牌时使用的密钥是配套的。
   - 如果 JWT 令牌解析校验时报错，则说明 JWT 令牌被篡改或超时失效了，令牌不合法。

#### JWT 实现登录认证

1. 编写 JWT 工具类

   ```java
   public class JwtUtil {
       private static final String KEY = "secret";
   
       // 接收业务数据，生成 token 并返回
       public static String genToken(Map<String, Object> claims) {
           return JWT.create()
               .withClaim("claims", claims)
               .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 12))
               .sign(Algorithm.HMAC256(KEY));
       }
   
       // 接收 token，验证 token，并返回业务数据
       public static Map<String, Object> parseToken(String token) {
           return JWT.require(Algorithm.HMAC256(KEY))
               .build()
               .verify(token)
               .getClaim("claims")
               .asMap();
       }
   }
   ```

2. 登录接口生成 JWT 令牌并返回

   ```java
   // 登录成功
   Map<String, Object> claims = new HashMap<>();
   claims.put("id", loginUser.getId());
   claims.put("username", loginUser.getUsername());
   String token = JwtUtil.genToken(claims);
   return Result.success(token);
   ```

3. 其它认证接口解析 JWT 令牌

   ```java
   @RestController
   @RequestMapping("/article")
   public class ArticleController {
   
       @GetMapping("/list")
       public Result<String> list(@RequestHeader(name = "Authorization") String token, HttpServletResponse response) {
           // 验证 token
           try {
               Map<String, Object> claims = JwtUtil.parseToken(token);
               return Result.success("所有文章数据....");
           } catch (Exception e) {
               // http 响应状态码为 401
               response.setStatus(401);
               return Result.error("未登录");
           }
       }
   }
   ```

   但是这种方法有局限性：未来将会存在大量的 controller，而每个 controller 会存在大量的接口，每个接口都需要编写 JWT 相关的代码。

   这个问题可以通过拦截器来解决，所有的请求都会经过拦截器，在拦截器里统一完成验证。

4. 编写拦截器

   ```java
   @Component
   public class LoginInterceptor implements HandlerInterceptor {
   
       @Override
       public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
           // 令牌验证
           String token = request.getHeader("Authorization");
           // 验证 token
           try {
               Map<String, Object> claims = JwtUtil.parseToken(token);
               // 放行
               return true;
           } catch (Exception e) {
               // http 响应状态码为 401
               response.setStatus(401);
               // 不放行
               return false;
           }
       }
   }
   ```

5. 在配置类中注册拦截器

   ```java
   @Configuration
   public class WebConfig implements WebMvcConfigurer {
   
       @Autowired
       private LoginInterceptor loginInterceptor;
   
       @Override
       public void addInterceptors(InterceptorRegistry registry) {
           // 登录接口和注册接口不拦截
           registry.addInterceptor(loginInterceptor).excludePathPatterns("/user/login", "/user/register");
       }
   }
   ```

   注册完成后，可以删除原来一些接口的 JWT 验证相关代码，JWT 生成的代码仍保留在登录接口。

至此，JWT 的登录认证实现完成。需要注意在全局拦截器统一实现 JWT 认证，同时放行登录和注册这类不需要 JWT 认证的接口。



### 获取用户详细信息

根据接口文档

controller

```java
@GetMapping("/userInfo")
public Result<User> userInfo(@RequestHeader(name="Authorization") String token) {
    // 根据用户名查询用户
}
```

> 这里的用户名从 token 中获取

service

```java
// 根据用户名查询用户
public User findByUsername(String username) {
}
```

mapper

```sql
-- 查询
select * from user where username=?;
```



#### 响应数据问题

**响应数据中包含了密码**

按照上面的方法，响应数据的时候会一并把 password 也返回，这不安全，可以在实体类中添加注解`@Json`忽略该数据。

```java
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class User {
    private Integer id;
    private String username;
    @JsonIgnore // 转换成 json 字符串时忽略 password 属性
    private String password;
    private String nickname;
    private String email;
    private String avatar;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
```



**时间属性返回的是 null**

`createTime`和`updateTime`的返回值是`null`，这是由于数据库采取蛇形命名，Java 实体类采取驼峰命名，导致数据无法直接映射。

可以在 application.yaml 文件中配置映射支持

```yaml
mybatis:
  configuration:
    map-underscore-to-camel-case: true # 开启驼峰命名和蛇形命名自动转换
```



#### 代码优化

观察前面 controller 实现的这一段代码

```java
@GetMapping("/userInfo")
public Result<User> userInfo(@RequestHeader(name = "Authorization") String token) {
    // 根据用户名查询用户
    Map<String, Object> claims = JwtUtil.parseToken(token);
    // ...
}
```

可以看到这段代码还是使用到了 JWT 的相关代码，这不符合前面“将 JWT 统一由拦截器处理”的工程需求。

这就需要考虑让接口获取拦截器中的 token 解析数据，可以通过`ThreadLocal`实现。



#### ThreadLocal

提供线程局部变量

- 用来存取数据：set()/get()
- 使用 ThreadLocal 存储的数据，线程安全
- 使用完之后需要调用 remove() 方法释放

可以通过以下测试代码验证 ThreadLocal 的线程安全性

```java
@Test
public void testThreadLocalSetAndGet() {
    // 创建 ThreadLocal 对象
    ThreadLocal tl = new ThreadLocal();

    // 开启两个线程
    new Thread(() -> {
        tl.set("blue");
        System.out.println(Thread.currentThread().getName() + ": " + tl.get());
        System.out.println(Thread.currentThread().getName() + ": " + tl.get());
        System.out.println(Thread.currentThread().getName() + ": " + tl.get());
    }, "blue").start();

    new Thread(() -> {
        tl.set("red");
        System.out.println(Thread.currentThread().getName() + ": " + tl.get());
        System.out.println(Thread.currentThread().getName() + ": " + tl.get());
        System.out.println(Thread.currentThread().getName() + ": " + tl.get());
    }, "red").start();
}
```

分析输出结果，发现同一个 ThreadLocal 对象可以实现对多线程的线程隔离。

ThreadLocal 提供线程局部变量可以有效保证多用户同时请求时的线程安全。



#### 使用 ThreadLocal 优化代码

可以将 controller 中有关 JWT 的代码都删除，在 interceptor 中使用 ThreadLocal 存储解析后的数据。

1. 编写 ThreadLocal 工具类

   ```java
   public class ThreadLocalUtil {
       // 提供 ThreadLocal 对象
       private static final ThreadLocal THREAD_LOCAL = new ThreadLocal();
   
       // 根据键获取值
       public static <T> T get() {
           return (T) THREAD_LOCAL.get();
       }
   
       // 存储键值对
       public static void set(Object value) {
           THREAD_LOCAL.set(value);
       }
   
       // 清除 ThreadLocal 防止内存泄漏
       public static void remove() {
           THREAD_LOCAL.remove();
       }
   }
   ```

2. 在拦截器中调用 ThreadLocalUtil 存储数据

   ```java
   public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
       // 令牌验证
       String token = request.getHeader("Authorization");
       // 验证 token
       try {
           Map<String, Object> claims = JwtUtil.parseToken(token);
           // 把业务数据存储到 ThreadLocal 中
           ThreadLocalUtil.set(claims);
           // 放行
           return true;
       } catch (Exception e) {
           // http 响应状态码为 401
           response.setStatus(401);
           // 不放行
           return false;
       }
   }
   ```

3. 在 controller 中调用 ThreadLocalUtil 获取数据

   ```java
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
   ```

4. 关于内存泄漏，在拦截器重写`afterCompletion`方法清除`ThreadLocal`的数据

   ```java
   @Override
   public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
       // 清空 ThreadLocal 中的数据
       ThreadLocalUtil.remove();
   }
   ```



### 更新用户基本信息

根据接口文档

controller

```java
@PutMapping("/update")
public Result update(@RequestBody User user) {
    
}
```

service

```java
public void update(User user) {
}
```

mapper

```sql
-- 更新
update user set nickname=?,email=?update_time=? where id=?;
```



#### 代码优化

对请求数据进行参数校验，使用 validation 实现针对 User 实体类的校验。

validation 对复合类型提供了以下注解：

- @NotNull：值不能为 null
- @NotEmpty：值不能为 null，且字符串内容不能为空
- @Email：要求满足邮箱格式

使用步骤：

- 在实体类的成员变量上添加注解

- 接口方法的实体参数上添加`@Validated`注解



1. 在 User 实体类添加注解

   ```java
   @Data
   @AllArgsConstructor
   @NoArgsConstructor
   @EqualsAndHashCode
   @ToString
   public class User {
       @NotNull
       private Integer id;
       private String username;
       @JsonIgnore
       private String password;
   
       @NotEmpty
       @Pattern(regexp = "^\\S{1,10}$")
       private String nickname;
   
       @NotEmpty
       @Email
       private String email;
       private String avatar;
       private LocalDateTime createTime;
       private LocalDateTime updateTime;
   }
   ```

2. 在 controller 添加相应的注解

   ```java
   @PutMapping("/update")
   public Result update(@RequestBody @Validated User user) {
       userService.update(user);
       return Result.success();
   }
   ```



### 更新用户头像

根据接口文档

controller

```java
@PatchMapping("/updateAvatar")
public Result updateAvatar(@RequestParam String avatarUrl) {
    
}
```

service

```java
public void updateAvatar(String url) {
}
```

mapper

```sql
-- 更新头像
update user set avatar=?,update_time=? where id=?;
```

这里的`id`可以通过前面介绍的`ThreadLocal`对象来获取。



#### 参数校验

按上面的代码还需要对头像地址进行校验，确保它是一个 url 地址。而 validation 正好提供了一个`@URL`注解用于校验 url 地址。该注解需要添加到 controller 中。

```java
@PatchMapping("/updateAvatar")
public Result updateAvatar(@RequestParam @URL String avatarUrl) {
    userService.updateAvatar(avatarUrl);
    return Result.success();
}
```



### 更新用户密码

根据接口文档

controller

```java
@PatchMapping("/updatePwd")
public Result updatePwd(@RequestBody Map<String, String> params) {
    
}
```

service

```java
public void updatePwd(String newPwd) {
}
```

mapper

```sql
-- 更新密码
update user set password=?,update_time=? where id=?;
```



### 新增文章分类

> 使用新的 controller、service、mapper，分模块开发。

根据接口文档

controller

```java
@PostMapping
public Result add(@RequestBody Category category) {
    
}
```

> `/category`的路径直接添加到类上

service

```java
public void add(Category category) {
    
}
```

mapper

```sql
-- 添加
insert into category(category_name,category_alias,create_time,update_time) values (?,?,?,?);
```

#### 参数校验

使用 validation 在实体类和 controller 上添加相应的注解。

实体类

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Category {
    private Integer id;
    @NotEmpty
    private String categoryName;
    @NotEmpty
    private String categoryAlias;
    private Integer authorId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
```

controller

```java
@PostMapping
public Result add(@RequestBody @Validated Category category) {
    categoryService.add(category);
    return Result.success();
}
```



### 文章分类列表

根据接口文档

controller

```java
@GetMapping
public Result<List<Category>> list() {
    
} 
```

service

```java
public List<Category> list() {
    
}
```

mapper

```sql
-- 查询所有文章分类
select * from category where author_id=?;
```



#### 关于日期时间格式问题

在前面其实已经出现过很多次日期时间格式问题了，解决这个问题可以在实体类中使用`@JsonFormat`注解指定日期时间格式

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Category {
    private Integer id;
    @NotEmpty
    private String categoryName;
    @NotEmpty
    private String categoryAlias;
    @JsonIgnore
    private Integer authorId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
```

类似地，把其它实体类的日期时间格式问题修复。



### 获取文章分类详情

根据接口文档

controller

```java
@GetMapping("/detail")
public Result<Category> detail(Integer id) {
    
}
```

service

```java
public Category findById(Integer id) {
    
}
```

mapper

```sql
-- 查询文章分类详情
select * from category where id=?;
```



### 更新文章分类

根据接口文档

controller

```java
@PutMapping
public Result update(@RequestBody @Validated Category category) {
    
}
```

service

```java
public void update(Category category) {
    
}
```

mapper

```sql
-- 更新文章分类
update category set category_name=?,category_alias=?,update_time=? where id=?;
```



#### 校验问题

“更新文章分类”要求必须提供主键 ID，而“新增文章分类”不需要提供主键 ID(自动递增生成)，这就需要使用分组校验功能，为新增和更新添加不同的校验规则。

validation 提供了分组校验的功能。

分组校验：把校验项进行归类分组，在完成不同的功能的时候，校验指定组中的校验项。

使用步骤

1. 定义分组

   在实体类内部定义分组接口

   ```java
   public interface Add {
   
   }
   
   public interface Update {
   
   }
   ```

2. 定义校验项时指定归属的分组

   在注解上通过`groups`属性指定分组

   ```java
   @NotNull(groups = Update.class)
   private Integer id;
   @NotEmpty(groups = {Add.class, Update.class})
   private String categoryName;
   @NotEmpty(groups = {Add.class, Update.class})
   private String categoryAlias;
   ```

3. 校验时指定要校验的分组

   在 controller 相关接口的`@Validated`注解上通过`value`属性指定分组

   ```java
   @PostMapping
   public Result add(@RequestBody @Validated(Category.Add.class) Category category) {
       categoryService.add(category);
       return Result.success();
   }
   
   @PutMapping
   public Result update(@RequestBody @Validated(Category.Update.class) Category category) {
       categoryService.update(category);
       return Result.success();
   }
   ```

4. 补充默认分组，方便管理那些没有指定分组的校验项注解

   最终优化如下

   ```java
   @Data
   @AllArgsConstructor
   @NoArgsConstructor
   @EqualsAndHashCode
   @ToString
   public class Category {
       @NotNull(groups = Update.class)
       private Integer id;
       @NotEmpty
       private String categoryName;
       @NotEmpty
       private String categoryAlias;
       @JsonIgnore
       private Integer authorId;
       @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
       private LocalDateTime createTime;
       @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
       private LocalDateTime updateTime;
   
       // 没有指定分组，默认属于 Default 分组
       // 分组之间可以继承
       public interface Add extends Default {}
       public interface Update extends Default {}
   }
   ```

   

### 删除文章分类

根据接口文档

controller

```java
@DeleteMapping
public Result delete(@RequestBody Map<String, Integer> param) {
    
}
```

service

```java
public void delete(Integer id) {
    
}
```

mapper

```sql
-- 删除文章分类
delete from category where id=?;
```



### 新增文章

根据接口文档

controller

```java
@PostMapping
public Result add(@RequestBody Article article) {
    
}
```

service

```java
public void add(Article article) {
    
}
```

mapper

```sql
-- 新增文章
insert into article(title,content,cover,state,category_id,author_id,create_time,update_time) values (?,?,?,?,?,?,?,?);
```



#### 参数校验

实体类

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Article {
    private Integer id;
    @NotEmpty
    @Pattern(regexp = "^\\S{1,10}$")
    private String title;
    @NotEmpty
    private String content;
    @NotEmpty
    @URL
    private String cover;
    private String state;
    @NotNull
    private Integer categoryId;
    @JsonIgnore
    private Integer authorId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
```

controller

```java
@PostMapping
public Result add(@RequestBody @Validated Article article) {
    articleService.add(article);
    return Result.success();
}
```

对于`state`字段无法直接使用 validation 的提供的校验，需要自定义校验。



#### 自定义校验

已有的注解不能满足所有的校验需求，特殊的情况需要自定义校验(自定义校验注解)

实现步骤：

1. 自定义注解 State

   创建注解类如下

   ```java
   // 元注解，帮助文档
   @Documented
   // 指定校验规则提供类
   @Constraint(
       validatedBy = {}
   )
   // 元注解，标注可以使用的地方，这里只需要在实体类属性上使用
   @Target({ElementType.FIELD})
   // 元注解，标注生命周期
   @Retention(RetentionPolicy.RUNTIME)
   public @interface State {
   
       // 提供校验失败后的提示信息
       String message() default "state 的值只能为已发布或者草稿";
       // 指定分组
       Class<?>[] groups() default {};
       // 负载，获取到 State 注解的附加信息
       Class<? extends Payload>[] payload() default {};
   }
   ```

   

2. 自定义校验数据的类 StateValidation，实现 ConstraintValidator 接口

   编写提供校验规则的类

   ```java
   // 第一个泛型指定需要提供校验规则的注解类，第二个泛型指定需要校验的数据类型
   public class StateValidation implements ConstraintValidator<State, String> {
       /**
        *
        * @param value 将来要校验的数据
        * @param context
        * @return 返回 false，校验不通过，返回 true，校验通过
        */
       @Override
       public boolean isValid(String value, ConstraintValidatorContext context) {
           // 提供校验规则
           if (value == null) {
               return false;
           }
           if (value.equals("已发布") || value.equals("草稿")) {
               return true;
           }
           return false;
       }
   }
   ```

   在注解类的`@Constraint`中指定提供校验规则的类

   ```java
   @Constraint(validatedBy = { StateValidation.class }) // 指定提供校验规则的类
   ```

3. 在需要校验的地方使用自定义注解

   在实体类的`state`属性上添加自定义注解`@State`

   ```java
   @State
   private String state;
   ```

   

### 文章列表(条件分页)

根据接口文档

controller

```java
@GetMapping
public Result<PageBean<Article>> list(Integer pageNum,
                                      Integer pageSize,
                                      @RequestParam(required = false) Integer categoryId,
                                      @RequestParam(required = flase) String state) {
    
}
```

其中`PageBean`实体类如下

```java
// 分页返回结果对象
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class PageBean<T> {
    private Long total; // 总条数
    private List<T> items; // 当前页数据集合
}
```

service

```java
public PageBean<Article> list(Integer pageNum, Integer pageSize, String categoryId, String state) {
    // 构建 PageBean 对象
    // 开启分页查询，使用 PageHelper
    // 调用 mapper 完成查询
}
```

`PageHelper`在`pom.xml`中引入

```xml
<dependencies>
    <dependency>
        <groupId>com.github.pagehelper</groupId>
        <artifactId>pagehelper-spring-boot-starter</artifactId>
        <version>1.4.6</version>
    </dependency>
</dependencies>
```

mapper

方法原型

```java
List<Article> list(Integer authorId, String categoryId, String state);
```

这里需要使用`xml`编写动态 sql 完成复杂查询

```xml
<select id="list" resultType="com.entropy.pojo.Article">
    select * from article
    <where>
        ....
    </where>
</select>
```





### 获取文章详情

根据接口文档

controller

```java
@GetMapping("/detail")
public Result<Article> detail(Integer id) {
    
}
```

service

```java
public Article findById(Integer id) {
    
}
```

mapper

```sql
-- 查询文章详情
select * from article where id=?;
```



### 更新文章

根据接口文档

controller

```java
@PutMapping
public Result update(@RequestBody @Validated Article article) {
    
}
```

service

```java
public void update(Article article) {
    
}
```

mapper

```sql
-- 更新文章
update article set title=?,content=?,cover=?,state=?,category_id=?,update_time=? where id=?;
```



### 删除文章

根据接口文档

controller

```java
@DeleteMapping
public Result delete(@RequestBody Map<String, Integer> param) {
    
}
```

service

```java
public void delete(Integer id) {
    
}
```

mapper

```sql
-- 删除文章
delete from article where id=?;
```



### 文件上传

文件上传参考的前端代码如下(这里暂时不编写前端)

```html
<form action="/upload" method="post" enctype="multipart/form-data">
    avatar: <input type="file" name="image"><br>
    <input type="submit" value="上传">
</form>
```

后端 MultipartFile 类提供了以下方法

```java
String getOriginalFilename(); // 获取原始文件名
void transferTo(File dest); // 将接收的文件转存到磁盘文件中
long getSize(); // 获取文件的大小，单位：字节
byte[] getBytes(); // 获取文件内容的字节数组
InputStream getInputStream(); // 获取接收到的文件内容的输入流
```



根据接口文档

controller

```java
@PostMapping("/upload")
public Result<String> upload(MultipartFile file) throws IOException {
    // 获取文件内容的输入流，写入到本地磁盘文件
}
```

mysql 数据库一般不会直接存储图片、音视频这类数据，有专门的第三方服务或者自建服务用于存储这类数据。



### 登录优化 Redis

在前面使用 JWT 生成的令牌存在一个问题：旧的令牌在生成新的令牌之后依然有效，旧的令牌可以访问操作资源，这是不安全的。正常情况下，生成新令牌之后，旧令牌应该失效。这就需要引入令牌主动失效的机制，借助 Redis 实现。

#### 令牌主动失效机制

实现思路：当用户登录生成令牌返回时，同时在 Redis 中存储一份。当浏览器携带令牌访问其它服务资源时，在全局拦截器不仅要对令牌进行合法性校验，还需要从 Redis 中获取同样的令牌，如果 Redis 中没有令牌或者是令牌不一致则说明浏览器的令牌已经失效，如果令牌是合法的而且能从 Redis 获取到同样的令牌，则说明令牌有效，可以正常提供服务。当新的令牌生成时就清除 Redis 中的旧令牌，以此实现令牌主动失效机制。

实现步骤：

1. 登录成功后，给浏览器响应令牌的同时，把该令牌存储到 redis 中
2. LoginInterceptor 拦截器中，需要验证浏览器携带的令牌，并同时获取 redis 中的令牌进行比较，判断是否一致
3. 当用户修改密码成功后，删除 redis 中存储的旧令牌



#### SpringBoot 集成 Redis

- 导入 spring-boot-starter-data-redis 起步依赖

  ```xml
  <dependencies>
      <!--redis-->
      <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-data-redis</artifactId>
      </dependency>
  </dependencies>
  ```

- 在 yaml 配置文件中，配置 redis 连接信息

  ```yaml
  spring:
    datasource:
      driver-class-name: com.mysql.cj.jdbc.Driver
      url: jdbc:mysql://localhost:3306/big_event
      username: admin
      password: 12345
    data:
      redis:
        host: localhost
        port: 6379
  ```

  docker-compose.yaml

  ```yaml
  version: "3.1"
  
  services:
    db:
      image: mysql
      environment:
        MYSQL_ROOT_PASSWORD: ${MYSQL_ROOT_PASSWORD}
        MYSQL_DATABASE: ${MYSQL_DATABASE}
        MYSQL_USER: ${MYSQL_USER}
        MYSQL_PASSWORD: ${MYSQL_PASSWORD}
      ports:
        - 3306:3306
  
    redis:
      image: redis
      ports:
        - 6379:6379
  ```

- 调用事件 API (StringRedisTemplate) 完成字符串的存取操作

  测试代码

  ```java
  @SpringBootTest // 该注解会先初始化 Spring 容器，方便直接测试与 Spring 容器相关的方法
  public class RedisTest {
  
      @Autowired
      private StringRedisTemplate stringRedisTemplate;
  
      @Test
      public void testSet() {
          // 往 redis 中存储一个键值对 StringRedisTemplate
          ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
          operations.set("username", "black");
          operations.set("id", "1", 15, TimeUnit.SECONDS);
      }
  
      @Test
      public void testGet() {
          // 从 redis 中获取一个键值对
          ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
          System.out.println(operations.get("username"));
      }
  }
  ```

  

#### 机制实现

1. 在 controller 生成 token 的代码中添加 redis 存储 token 以及设置过期时间(过期时间与 token 过期时间一致)

   ```java
   // 登录成功
   Map<String, Object> claims = new HashMap<>();
   claims.put("id", loginUser.getId());
   claims.put("username", loginUser.getUsername());
   String token = JwtUtil.genToken(claims);
   // 将 token 存储到 redis 中
   ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
   operations.set(token, token, 12, TimeUnit.HOURS); // 设置 12 小时过期时间
   ```

2. 在全局拦截器中编写 redis token 验证代码

   ```java
   @Autowired
   private StringRedisTemplate stringRedisTemplate;
   
   @Override
   public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
       // 令牌验证
       String token = request.getHeader("Authorization");
       // 验证 token
       try {
           // 从 redis 中获取 token，判断是否和当前 token 一致
           ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
           String redisToken = operations.get(token);
           if (redisToken == null) {
               // token 已经失效
               throw new RuntimeException();
           }
           Map<String, Object> claims = JwtUtil.parseToken(token);
           // 把业务数据存储到 ThreadLocal 中
           ThreadLocalUtil.set(claims);
           // 放行
           return true;
       } catch (Exception e) {
           // http 响应状态码为 401
           response.setStatus(401);
           // 不放行
           return false;
       }
   }
   ```

3. 用户密码修改后删除 redis 中的 token，在修改密码的接口上接收请求头中的 token，在修改完密码后清空 redis 中对应的 token

   ```java 
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
   ```



### SpringBoot 项目部署

SpringBoot 项目的打包产物是 jar 包。

在 pom.xml 配置 maven 打包插件

```xml 
<build>
    <plugins>
        <!--打包插件-->
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <version>3.2.3</version>
        </plugin>
    </plugins>
</build>
```

通过 maven 的 package 命令会在`target`目录下生成 jar 包。

生成的 jar 包通过`java -jar`运行，需要注意端口占用问题和 jre 环境问题，以及相关的服务 mysql、redis 是否在运行。



#### 属性配置方式  

原始的项目配置方式是写在配置文件中，这样导致的一个问题是生产环境中，运维人员是无法直接修改 jar 包的，这就不能改变项目的一些属性，例如端口号。

SpringBoot 提供了多种属性配置方式

- 配置文件方式：properties 或 yaml 配置文件。通常用于开发环境，配置会一并打包到 jar 包中，无法直接修改。
- 命令行参数方式：`java -jar big-event-1.0-SNAPSHOT.jar --server.port=8081`这里的`--server.port=8081`会通过启动类的`args`参数传递给项目。
- 环境变量方式：在系统环境变量中设置变量名`server.port`，值为`8081`。该环境变量会被 SpringBoot 项目自动读取。
- 外部配置文件方式：相对最方便的方式，可以在 jar 包同目录下创建一个`application.yaml`文件，该配置文件优先于项目本身的配置文件。

配置优先级(从上往下递增)：

- 项目中 resources 目录下的 application.yaml
- jar 包所在目录下的 application.yaml
- 操作系统环境变量
- 命令行参数



### 多环境开发 Profiles

项目的运行环境有多种：开发环境、测试环境、生产环境。在不同的环境下，程序的配置信息会有所不同，这就需要频繁地修改配置文件。

#### 单文件配置方式

SpringBoot 提供了多环境开发技术 profiles，可以用来隔离应用程序配置的各个部分，并在特定环境下指定部分配置生效。

- 隔离不同环境的配置

  使用`---`分隔符

- 指定配置归属的环境

  ```yaml
  spring:
    config:
      active:
        on-profile: 环境名称
  ```

- 指定某个配置生效

  ```yaml
  spring:
    profiles:
      active: 环境名称
  ```

配置示例

```yaml
# 通用配置，指定生效环境
# 多环境下通用的配置
# 如果通用配置和特定环境配置冲突了，则以特定环境配置为准
spring:
  profiles:
    active: dev
server:
  servlet:
    context-path: /api
---
# 开发环境
spring:
  config:
    active:
      on-profile: dev
server:
  port: 8081
server:
  servlet:
    context-path: /endpoint
---
# 测试环境
spring:
  config:
    active:
      on-profile: test
server:
  port: 8082
---
# 生产环境
spring:
  config:
    active:
      on-profile: pro
server:
  port: 8083 
```

这里有个问题是当配置项非常多时，整个文件会变得难以维护。

实际上这里演示的只是多环境开发中的单文件配置方式，SpringBoot 提供了多环境多文件配置的方式，更加符合实际需求。

#### 多文件配置方式

- 开发环境：application-dev.yaml
- 测试环境：application-test.yaml
- 生产环境：application-pro.yaml
- 通用环境(同时用于激活指定环境)：application.yaml

简单来说，就是将单文件使用分隔符区分多个环境改成了使用多文件区分多个环境。

但是这样还是存在一定的维护问题，例如服务器相关的配置、数据源相关的配置和自定义的配置混杂在一起，不利于维护。而 profiles 还提供了对配置文件分组的功能。

#### Profiles 分组

分组功能可以对环境配置根据功能的不同再次进行拆分，写到不同的配置文件。例如：

- application-devServer.yaml：只写服务器相关的配置
- application-devDB.yaml：只写数据源相关的配置
- application-devSelf.yaml：只写自定义的配置

然后可以在 application.yaml 文件中使用以下配置定义分组使以上配置全部生效

```yaml
spring:
  profiles:
    active: dev
    group:
      "dev": devServer,devDB,devSelf
      "test": testServer,testDB,testSelf
```



使用小结：

- 按照配置的类别，把配置信息配置到不同的配置文件中

  application-分类名.yaml

- 在 application.yaml 中定义分组

  spring.profiles.group

- 在 application.yaml 中激活分组

  spring.profiles.active
