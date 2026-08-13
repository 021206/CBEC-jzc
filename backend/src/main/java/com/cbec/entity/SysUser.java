package com.cbec.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SysUser {
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private Integer status;    // 1启用 0禁用
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}