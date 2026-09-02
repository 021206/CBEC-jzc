package com.cbec.entity.order;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class InboundOrder {
    private Long id;
    private String orderNo;
    private Long warehouseId;
    private String supplier;
    private Integer inboundType;
    private Integer status;
    private String auditor;
    private LocalDateTime auditTime;
    private String remark;
    private String createBy;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // 非数据库字段：明细列表
    private List<InboundItem> items;
}