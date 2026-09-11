package com.cbec.controller;

import com.cbec.common.Result;
import com.cbec.common.annotation.RequiresPermission;
import com.cbec.entity.inventory.Inventory;
import com.cbec.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @RequiresPermission("inventory:list")
    @GetMapping("/{productId}/{warehouseId}")
    public Result<Inventory> getByProductAndWarehouse(
            @PathVariable Long productId,
            @PathVariable Long warehouseId) {
        return Result.success(inventoryService.getByProductAndWarehouse(productId, warehouseId));
    }

    @RequiresPermission("inventory:list")
    @GetMapping("/list")
    public Result<Map<String, Object>> list(
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(inventoryService.list(productId, warehouseId, pageNum, pageSize));
    }
}