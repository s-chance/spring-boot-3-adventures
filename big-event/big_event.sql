# 数据库
create database big_event;
# 使用
use big_event;

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
