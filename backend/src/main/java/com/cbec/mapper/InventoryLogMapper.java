package com.cbec.mapper;

import com.cbec.entity.inventory.InventoryLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;


import java.util.List;
import java.util.Map;

@Mapper
public interface InventoryLogMapper {

    @Insert("INSERT INTO inventory_log(product_id, warehouse_id, order_no, change_type, change_qty, before_qty, after_qty, operator, remark) " +
            "VALUES(#{productId}, #{warehouseId}, #{orderNo}, #{changeType}, #{changeQty}, #{beforeQty}, #{afterQty}, #{operator}, #{remark})")
    int insert(InventoryLog log);

    // ========== 分页查询（使用纯字符串 + 条件拼接） ==========
    @Select("SELECT * FROM inventory_log " +
            "WHERE product_id = COALESCE(#{productId}, product_id) " +
            "AND warehouse_id = COALESCE(#{warehouseId}, warehouse_id) " +
            "AND change_type = COALESCE(#{changeType}, change_type) " +
            "AND (#{startTime} IS NULL OR create_time >= #{startTime}) " +
            "AND (#{endTime} IS NULL OR create_time <= #{endTime}) " +
            "ORDER BY id DESC LIMIT #{offset}, #{limit}")
    List<InventoryLog> selectPage(@Param("productId") Long productId,
                                  @Param("warehouseId") Long warehouseId,
                                  @Param("changeType") Integer changeType,
                                  @Param("startTime") String startTime,
                                  @Param("endTime") String endTime,
                                  @Param("offset") int offset,
                                  @Param("limit") int limit);

    @Select("SELECT COUNT(*) FROM inventory_log " +
            "WHERE product_id = COALESCE(#{productId}, product_id) " +
            "AND warehouse_id = COALESCE(#{warehouseId}, warehouse_id) " +
            "AND change_type = COALESCE(#{changeType}, change_type) " +
            "AND (#{startTime} IS NULL OR create_time >= #{startTime}) " +
            "AND (#{endTime} IS NULL OR create_time <= #{endTime})")
    int count(@Param("productId") Long productId,
              @Param("warehouseId") Long warehouseId,
              @Param("changeType") Integer changeType,
              @Param("startTime") String startTime,
              @Param("endTime") String endTime);

    @Select("SELECT IFNULL(SUM(change_qty), 0) FROM inventory_log WHERE change_type = 1 AND DATE(create_time) = #{today}")
    Integer sumTodayInbound(@Param("today") String today);

    @Select("SELECT IFNULL(SUM(change_qty), 0) FROM inventory_log WHERE change_type = 2 AND DATE(create_time) = #{today}")
    Integer sumTodayOutbound(@Param("today") String today);

    /**
     * 近7天入库趋势（按日期分组）
     */
    @Select("SELECT DATE(create_time) as date, IFNULL(SUM(change_qty), 0) as total " +
            "FROM inventory_log " +
            "WHERE change_type = 1 AND create_time >= DATE_SUB(CURDATE(), INTERVAL 6 DAY) " +
            "GROUP BY DATE(create_time) ORDER BY date ASC")
    List<Map<String, Object>> sumInboundTrend();

    /**
     * 近7天出库趋势（按日期分组）
     */
    @Select("SELECT DATE(create_time) as date, IFNULL(SUM(change_qty), 0) as total " +
            "FROM inventory_log " +
            "WHERE change_type = 2 AND create_time >= DATE_SUB(CURDATE(), INTERVAL 6 DAY) " +
            "GROUP BY DATE(create_time) ORDER BY date ASC")
    List<Map<String, Object>> sumOutboundTrend();
}