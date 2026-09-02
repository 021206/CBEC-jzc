package com.cbec.mapper;

import com.cbec.entity.inventory.InventoryLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface InventoryLogMapper {

    @Insert("INSERT INTO inventory_log(product_id, warehouse_id, order_no, change_type, change_qty, before_qty, after_qty, operator, remark) " +
            "VALUES(#{productId}, #{warehouseId}, #{orderNo}, #{changeType}, #{changeQty}, #{beforeQty}, #{afterQty}, #{operator}, #{remark})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(InventoryLog log);
}