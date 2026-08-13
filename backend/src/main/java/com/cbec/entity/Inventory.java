package com.cbec.entity;

import lombok.Data;

@Data
public class Inventory {
    private Integer id;//对应数据库Inventory字段
    private String productName;
    private Integer quantity;
    private Integer version;
}
