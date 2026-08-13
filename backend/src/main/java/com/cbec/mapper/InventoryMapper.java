package com.cbec.mapper;

import com.cbec.entity.Inventory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper  // 告诉Spring，这是一个操作数据库的接口
public interface InventoryMapper {
    @Select("SELECT id, product_name AS productName, quantity, version FROM inventory WHERE id = #{id}")
    Inventory findById(Integer id);
}