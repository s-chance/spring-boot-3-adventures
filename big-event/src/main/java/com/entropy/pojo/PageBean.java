package com.entropy.pojo;

import lombok.*;

import java.util.List;

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
