package com.cbec.controller;

import com.cbec.common.Result;
import com.cbec.entity.inventory.Inventory;
import com.cbec.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/{productId}/{warehouseId}")
    public Result<Inventory> getByProductAndWarehouse(
            @PathVariable Long productId,
            @PathVariable Long warehouseId) {
        return Result.success(inventoryService.getByProductAndWarehouse(productId, warehouseId));
    }
}