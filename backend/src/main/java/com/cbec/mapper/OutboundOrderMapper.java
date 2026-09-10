package com.cbec.mapper;

import com.cbec.entity.order.OutboundOrder;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OutboundOrderMapper {

    @Insert("INSERT INTO outbound_order(order_no, warehouse_id, outbound_type, status, remark, create_by) " +
            "VALUES(#{orderNo}, #{warehouseId}, #{outboundType}, #{status}, #{remark}, #{createBy})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(OutboundOrder order);

    @Update("UPDATE outbound_order SET status=#{status}, auditor=#{auditor}, audit_time=#{auditTime} WHERE id=#{id}")
    int updateStatus(OutboundOrder order);

    @Select("SELECT o.*, w.name as warehouseName " +
            "FROM outbound_order o " +
            "LEFT JOIN warehouse w ON o.warehouse_id = w.id " +
            "WHERE o.id = #{id}")
    OutboundOrder selectById(@Param("id") Long id);

    @Select("SELECT o.*, w.name as warehouseName " +
            "FROM outbound_order o " +
            "LEFT JOIN warehouse w ON o.warehouse_id = w.id " +
            "ORDER BY o.id ASC")
    List<OutboundOrder> selectAll();
}