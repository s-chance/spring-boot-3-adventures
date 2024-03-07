create database if not exists mybatis character set utf8mb4 collate utf8mb4_unicode_ci;

use mybatis;

create table user(
    id int unsigned primary key auto_increment comment 'ID',
    nickname varchar(100) comment '姓名',
    age tinyint unsigned comment '年龄',
    gender varchar(10) comment '性别'
) comment '用户表';

insert into user(id, nickname, age, gender) values (1, '张三', 20, '男');
insert into user(id, nickname, age, gender) values (2, '彩阳', 22, '女');
insert into user(id, nickname, age, gender) values (3, '王五', 21, '男');
insert into user(id, nickname, age, gender) values (4, '马六', 44, '男');
insert into user(id, nickname, age, gender) values (5, '流霞', 12, '女');
