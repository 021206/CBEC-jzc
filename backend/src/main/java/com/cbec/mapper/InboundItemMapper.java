package com.cbec.mapper;

import com.cbec.entity.order.InboundItem;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface InboundItemMapper {

    @Insert("INSERT INTO inbound_item(inbound_id, product_id, planned_qty, actual_qty) " +
            "VALUES(#{inboundId}, #{productId}, #{plannedQty}, #{actualQty})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(InboundItem item);

    @Select("SELECT * FROM inbound_item WHERE inbound_id = #{inboundId}")
    List<InboundItem> selectByInboundId(@Param("inboundId") Long inboundId);

    @Delete("DELETE FROM inbound_item WHERE inbound_id = #{inboundId}")
    int deleteByInboundId(@Param("inboundId") Long inboundId);
}