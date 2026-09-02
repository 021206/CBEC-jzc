package com.cbec.mapper;

import com.cbec.entity.inventory.Inventory;
import org.apache.ibatis.annotations.*;

@Mapper
public interface InventoryMapper {

    @Select("SELECT * FROM inventory WHERE product_id = #{productId} AND warehouse_id = #{warehouseId}")
    Inventory findByProductAndWarehouse(@Param("productId") Long productId, @Param("warehouseId") Long warehouseId);

    @Insert("INSERT INTO inventory(product_id, warehouse_id, quantity, version) VALUES(#{productId}, #{warehouseId}, #{quantity}, 0)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Inventory inventory);

    @Update("UPDATE inventory SET quantity = quantity + #{delta}, version = version + 1 " +
            "WHERE id = #{id} AND version = #{version}")
    int increaseStock(@Param("id") Long id, @Param("delta") Integer delta, @Param("version") Integer version);

    @Update("UPDATE inventory SET quantity = quantity - #{delta}, version = version + 1 " +
            "WHERE id = #{id} AND version = #{version} AND quantity >= #{delta}")
    int decreaseStock(@Param("id") Long id, @Param("delta") Integer delta, @Param("version") Integer version);

    @Select("SELECT * FROM inventory WHERE product_id = #{productId}")
    Inventory findByProductId(@Param("productId") Long productId);

    // 库存预警功能暂不实现，后续补充
    // @Select("SELECT ...")
    // List<Map<String, Object>> findWarningList();

    @Select("SELECT * FROM inventory WHERE id = #{id}")
    Inventory selectById(@Param("id") Long id);
}