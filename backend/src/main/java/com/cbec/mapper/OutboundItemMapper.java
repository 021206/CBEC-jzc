package com.cbec.mapper;

import com.cbec.entity.order.OutboundItem;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OutboundItemMapper {

    @Insert("INSERT INTO outbound_item(outbound_id, product_id, planned_qty, actual_qty) " +
            "VALUES(#{outboundId}, #{productId}, #{plannedQty}, #{actualQty})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(OutboundItem item);

    @Select("SELECT * FROM outbound_item WHERE outbound_id = #{outboundId}")
    List<OutboundItem> selectByOutboundId(@Param("outboundId") Long outboundId);

    @Delete("DELETE FROM outbound_item WHERE outbound_id = #{outboundId}")
    int deleteByOutboundId(@Param("outboundId") Long outboundId);
}