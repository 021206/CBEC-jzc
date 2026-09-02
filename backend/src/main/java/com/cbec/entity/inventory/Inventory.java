package com.cbec.entity.inventory;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Inventory {
    private Long id;
    private Long productId;
    private Long warehouseId;
    private Integer quantity;
    private Integer version;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}