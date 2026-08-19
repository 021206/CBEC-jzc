package com.cbec.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Warehouse {
    private Long id;
    private String name;
    private String address;
    private String contact;
    private String phone;
    private Integer status; // 1启用 0禁用
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}