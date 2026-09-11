package com.cbec.controller;

import com.cbec.common.Result;
import com.cbec.common.annotation.RequiresPermission;
import com.cbec.common.exception.BusinessException;
import com.cbec.entity.warehouse.Warehouse;
import com.cbec.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/warehouse")
public class WarehouseController {

    @Autowired
    private WarehouseService warehouseService;

    @RequiresPermission("warehouse:add")
    @PostMapping
    public Result<Warehouse> add(@RequestBody Warehouse warehouse) {
        if (warehouse.getName() == null || warehouse.getName().isEmpty()) {
            throw new BusinessException("仓库名称不能为空");
        }
        return Result.success(warehouseService.add(warehouse));
    }

    @RequiresPermission("warehouse:edit")
    @PutMapping
    public Result<Warehouse> update(@RequestBody Warehouse warehouse) {
        if (warehouse.getId() == null) {
            throw new BusinessException("ID不能为空");
        }
        return Result.success(warehouseService.update(warehouse));
    }

    @RequiresPermission("warehouse:delete")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        warehouseService.delete(id);
        return Result.success(null);
    }

    @RequiresPermission("warehouse:list")
    @GetMapping("/{id}")
    public Result<Warehouse> getById(@PathVariable Long id) {
        return Result.success(warehouseService.getById(id));
    }

    @RequiresPermission("warehouse:list")
    @GetMapping("/page")
    public Result<Map<String, Object>> page(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(warehouseService.page(keyword, pageNum, pageSize));
    }

    @RequiresPermission("warehouse:list")
    @GetMapping("/list")
    public Result<List<Warehouse>> listEnabled() {
        return Result.success(warehouseService.getAllEnabled());
    }

    @RequiresPermission("warehouse:edit")
    @PutMapping("/status/{id}")
    public Result<Void> toggleStatus(@PathVariable Long id) {
        warehouseService.toggleStatus(id);
        return Result.success(null);
    }
}