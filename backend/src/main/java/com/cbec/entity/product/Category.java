package com.cbec.entity.product;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class Category {
    private Long id;
    private Long parentId;
    private String name;
    private Integer sortOrder;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // 非数据库字段：子分类列表（树形结构用）
    private List<Category> children;
}