package com.cbec.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Product {
    private Long id;
    private String skuCode;
    private String name;
    private Long categoryId;
    private String spec;
    private String unit;
    private Integer warningThreshold;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}