package com.cbec.mapper;

import com.cbec.entity.inventory.Inventory;
import org.apache.ibatis.annotations.*;
import java.util.List;
import java.util.Map;

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

    // 库存预警功能暂不实现
    // @Select("SELECT ...")
    // List<Map<String, Object>> findWarningList();

    @Select("SELECT * FROM inventory WHERE id = #{id}")
    Inventory selectById(@Param("id") Long id);

    /**
     * 分页查询库存列表（支持按商品和仓库筛选）
     */
    @Select("SELECT * FROM inventory " +
            "WHERE (#{productId} IS NULL OR product_id = #{productId}) " +
            "AND (#{warehouseId} IS NULL OR warehouse_id = #{warehouseId}) " +
            "ORDER BY id DESC LIMIT #{offset}, #{limit}")
    List<Inventory> selectPage(@Param("productId") Long productId,
                               @Param("warehouseId") Long warehouseId,
                               @Param("offset") int offset,
                               @Param("limit") int limit);

    /**
     * 统计库存总数（用于分页）
     */
    @Select("SELECT COUNT(*) FROM inventory " +
            "WHERE (#{productId} IS NULL OR product_id = #{productId}) " +
            "AND (#{warehouseId} IS NULL OR warehouse_id = #{warehouseId})")
    int count(@Param("productId") Long productId,
              @Param("warehouseId") Long warehouseId);

    @Select("SELECT i.product_id, p.name as productName, i.warehouse_id, w.name as warehouseName, i.quantity, p.warning_threshold " +
            "FROM inventory i " +
            "LEFT JOIN product p ON i.product_id = p.id " +
            "LEFT JOIN warehouse w ON i.warehouse_id = w.id " +
            "WHERE i.quantity < p.warning_threshold")
    List<Map<String, Object>> findWarningList();
}