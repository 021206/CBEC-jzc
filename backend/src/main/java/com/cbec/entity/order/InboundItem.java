package com.cbec.entity.order;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class InboundItem {
    private Long id;
    private Long inboundId;
    private Long productId;
    private Integer plannedQty;
    private Integer actualQty;
    private LocalDateTime createTime;
}