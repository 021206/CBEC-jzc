package com.cbec.entity.auth;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SysMenu {
    private Long id;
    private Long parentId;
    private String menuName;
    private String path;
    private String component;
    private String perms;
    private Integer menuType; // 1目录 2菜单 3按钮
    private String icon;
    private Integer sortOrder;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private List<SysMenu> children;
}