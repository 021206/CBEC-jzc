package com.cbec.service;

import com.cbec.entity.dto.StatisticsDTO;
import com.cbec.mapper.InventoryLogMapper;
import com.cbec.mapper.InventoryMapper;
import com.cbec.mapper.ProductMapper;
import com.cbec.mapper.WarehouseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        Integer inboundToday = inventoryLogMapper.sumTodayInbound(today);
        dto.setTodayInboundQty(inboundToday != null ? inboundToday : 0);

        // 4. 今日出库总数
        Integer outboundToday = inventoryLogMapper.sumTodayOutbound(today);
        dto.setTodayOutboundQty(outboundToday != null ? outboundToday : 0);

        // 5. 近7天出入库趋势
        dto.setTrendData(getTrendData());

        // 6. 库存预警商品
        dto.setWarningList(inventoryMapper.findWarningList());

        return dto;
    }

    private List<Map<String, Object>> getTrendData() {
        // 构造近7天的日期列表（格式 MM-dd）
        List<String> last7Days = new ArrayList<>();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MM-dd");
        for (int i = 6; i >= 0; i--) {
            last7Days.add(LocalDate.now().minusDays(i).format(fmt));
        }

        // 查询数据库返回的入库、出库趋势
        List<Map<String, Object>> inboundList = inventoryLogMapper.sumInboundTrend();
        List<Map<String, Object>> outboundList = inventoryLogMapper.sumOutboundTrend();

        // 转为 Map，方便按日期匹配
        Map<String, Integer> inboundMap = new HashMap<>();
        Map<String, Integer> outboundMap = new HashMap<>();

        for (Map<String, Object> row : inboundList) {
            String date = String.valueOf(row.get("date"));
            if (date.length() >= 10) {
                date = date.substring(5); // 截取 MM-dd
            }
            inboundMap.put(date, ((Number) row.get("total")).intValue());
        }

        for (Map<String, Object> row : outboundList) {
            String date = String.valueOf(row.get("date"));
            if (date.length() >= 10) {
                date = date.substring(5);
            }
            outboundMap.put(date, ((Number) row.get("total")).intValue());
        }

        // 组装返回
        List<Map<String, Object>> result = new ArrayList<>();
        for (String day : last7Days) {
            Map<String, Object> item = new HashMap<>();
            item.put("date", day);
            item.put("inbound", inboundMap.getOrDefault(day, 0));
            item.put("outbound", outboundMap.getOrDefault(day, 0));
            result.add(item);
        }
        return result;
    }
}