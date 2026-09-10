package com.cbec.mapper;

import com.cbec.entity.order.InboundOrder;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface InboundOrderMapper {

    @Insert("INSERT INTO inbound_order(order_no, warehouse_id, supplier, inbound_type, status, remark, create_by) " +
            "VALUES(#{orderNo}, #{warehouseId}, #{supplier}, #{inboundType}, #{status}, #{remark}, #{createBy})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(InboundOrder order);

    @Update("UPDATE inbound_order SET status=#{status}, auditor=#{auditor}, audit_time=#{auditTime} WHERE id=#{id}")
    int updateStatus(InboundOrder order);

    @Select("SELECT i.*, w.name as warehouseName " +
            "FROM inbound_order i " +
            "LEFT JOIN warehouse w ON i.warehouse_id = w.id " +
            "ORDER BY i.id ASC")
    List<InboundOrder> selectAll();

    @Select("SELECT i.*, w.name as warehouseName " +
            "FROM inbound_order i " +
            "LEFT JOIN warehouse w ON i.warehouse_id = w.id " +
            "WHERE i.id = #{id}")
    InboundOrder selectById(@Param("id") Long id);
}