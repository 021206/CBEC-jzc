package com.cbec.service;

import com.cbec.entity.warehouse.Warehouse;
import com.cbec.mapper.WarehouseMapper;
import com.cbec.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WarehouseService {

    @Autowired
    private WarehouseMapper warehouseMapper;

    public Warehouse add(Warehouse warehouse) {
        if (warehouse.getName() == null || warehouse.getName().isEmpty()) {
            throw new BusinessException("仓库名称不能为空");
        }
        // 如果 status 为空，默认为 1（启用）
        if (warehouse.getStatus() == null) {
            warehouse.setStatus(1);
        }
        warehouseMapper.insert(warehouse);
        return warehouse;
    }

    public Warehouse update(Warehouse warehouse) {
        if (warehouse.getId() == null) {
            throw new BusinessException("仓库ID不能为空");
        }
        warehouseMapper.update(warehouse);
        return warehouse;
    }

    public void delete(Long id) {
        warehouseMapper.deleteById(id);
    }

    public Warehouse getById(Long id) {
        return warehouseMapper.selectById(id);
    }

    public Map<String, Object> page(String keyword, Integer pageNum, Integer pageSize) {
        if (pageNum == null || pageNum < 1) pageNum = 1;
        if (pageSize == null || pageSize < 1) pageSize = 10;
        if (keyword == null) keyword = "";

        int offset = (pageNum - 1) * pageSize;
        List<Warehouse> list = warehouseMapper.selectPage(keyword, offset, pageSize);
        int total = warehouseMapper.count(keyword);

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);
        return result;
    }

    public List<Warehouse> getAllEnabled() {
        return warehouseMapper.selectAllEnabled();
    }

    // 切换状态
    public void toggleStatus(Long id) {
        Warehouse warehouse = warehouseMapper.selectById(id);
        if (warehouse == null) {
            throw new BusinessException("仓库不存在");
        }
        Integer currentStatus = warehouse.getStatus();
        if (currentStatus == null) {
            // 如果状态为空，默认设为1（启用）
            currentStatus = 1;
        }
        warehouse.setStatus(currentStatus == 1 ? 0 : 1);
        warehouseMapper.update(warehouse);
    }
}