package com.cbec.service;

import com.cbec.entity.dto.StatisticsDTO;
import com.cbec.mapper.ProductMapper;
import com.cbec.mapper.WarehouseMapper;
import com.cbec.mapper.InventoryMapper;
import com.cbec.mapper.InventoryLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@Service
public class StatisticsService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private WarehouseMapper warehouseMapper;

    @Autowired
    private InventoryMapper inventoryMapper;

    @Autowired
    private InventoryLogMapper inventoryLogMapper;

    public StatisticsDTO getOverview() {
        StatisticsDTO dto = new StatisticsDTO();

        // 1. 总商品数
        dto.setTotalProducts(productMapper.countAll());

        // 2. 总仓库数
        dto.setTotalWarehouses(warehouseMapper.countAll());

        // 3. 今日入库总数
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        dto.setTodayInboundQty(inventoryLogMapper.sumTodayInbound(today));

        // 4. 今日出库总数
        dto.setTodayOutboundQty(inventoryLogMapper.sumTodayOutbound(today));

        // 5. 近7天出入库趋势
        dto.setTrendData(getTrendData());

        // 6. 库存预警商品
        dto.setWarningList(inventoryMapper.findWarningList());

        return dto;
    }

    private List<Map<String, Object>> getTrendData() {
        List<Map<String, Object>> result = new ArrayList<>();
        // 实现近7天按日期统计入库/出库数量
        // 需要编写 SQL 按日期分组查询 inventory_log
        // 这里先返回空列表，后续完善
        return result;
    }
}