package com.entropy.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.*;

import java.time.LocalDateTime;

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
