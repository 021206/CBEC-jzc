package com.cbec.entity.order;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OutboundItem {
    private Long id;
    private Long outboundId;
    private Long productId;
    private Integer plannedQty;
    private Integer actualQty;
    private LocalDateTime createTime;
}