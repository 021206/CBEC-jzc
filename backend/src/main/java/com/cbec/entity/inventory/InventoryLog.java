package com.cbec.entity.inventory;

import java.time.LocalDateTime;

public class InventoryLog {
    private Long id;
    private Long productId;
    private Long warehouseId;
    private String orderNo;
    private Integer changeType; // 1入库 2出库
    private Integer changeQty;
    private Integer beforeQty;
    private Integer afterQty;
    private String operator;
    private String remark;
    private LocalDateTime createTime;

    // ========== 以下是 getter 和 setter，必须全部存在 ==========
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public Long getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }

    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }

    public Integer getChangeType() { return changeType; }
    public void setChangeType(Integer changeType) { this.changeType = changeType; }

    public Integer getChangeQty() { return changeQty; }
    public void setChangeQty(Integer changeQty) { this.changeQty = changeQty; }

    public Integer getBeforeQty() { return beforeQty; }
    public void setBeforeQty(Integer beforeQty) { this.beforeQty = beforeQty; }

    public Integer getAfterQty() { return afterQty; }
    public void setAfterQty(Integer afterQty) { this.afterQty = afterQty; }

    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}