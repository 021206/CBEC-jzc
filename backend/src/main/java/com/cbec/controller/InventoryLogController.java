package com.cbec.controller;

import com.cbec.common.Result;
import com.cbec.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/inventory/log")
public class InventoryLogController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/page")
    public Result<Map<String, Object>> page(
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) Integer changeType,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(inventoryService.listLogs(productId, warehouseId, changeType, startTime, endTime, pageNum, pageSize));
    }
}