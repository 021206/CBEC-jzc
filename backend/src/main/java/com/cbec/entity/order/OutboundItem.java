package com.cbec.entity.order;

import java.time.LocalDateTime;

public class OutboundItem {
    private Long id;
    private Long outboundId;
    private Long productId;
    private Integer plannedQty;
    private Integer actualQty;
    private LocalDateTime createTime;

    // getter/setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getOutboundId() { return outboundId; }
    public void setOutboundId(Long outboundId) { this.outboundId = outboundId; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public Integer getPlannedQty() { return plannedQty; }
    public void setPlannedQty(Integer plannedQty) { this.plannedQty = plannedQty; }
    public Integer getActualQty() { return actualQty; }
    public void setActualQty(Integer actualQty) { this.actualQty = actualQty; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}