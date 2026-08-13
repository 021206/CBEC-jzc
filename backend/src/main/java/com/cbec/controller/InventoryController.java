package com.cbec.controller;

import com.cbec.entity.Inventory;
import com.cbec.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController  // 标记为控制器，专门接收浏览器的请求
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/inventory/{id}")  // 当浏览器访问 /inventory/1 时，会触发这个方法
    public Inventory getInventory(@PathVariable Integer id) {
        return inventoryService.getInventoryById(id);
    }
}