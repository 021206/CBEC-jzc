package com.cbec.entity;

import lombok.Data;
import java.util.List;

@Data
public class SysMenu {
    private Long id;
    private Long parentId;
    private String menuName;
    private String path;
    private String component;
    private String perms;
    private Integer menuType;  // 1、目录  2、菜单  3、按钮
    private String icon;
    private Integer sortOrder;
    private Integer status;

    // 用于前端树形结构的子菜单列表（非数据库字段）
    private List<SysMenu> children;
}