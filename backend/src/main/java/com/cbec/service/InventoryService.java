package com.cbec.service;

import com.cbec.entity.Inventory;
import com.cbec.mapper.InventoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service  // 标记为业务逻辑层
public class InventoryService {

    @Autowired  // 自动注入Mapper，相当于把数据库操作工具拿过来用
    private InventoryMapper inventoryMapper;

    public Inventory getInventoryById(Integer id) {
        return inventoryMapper.findById(id);
    }
}