package com.cbec.entity.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class StatisticsDTO {
    private Long totalProducts;
    private Long totalWarehouses;
    private Integer todayInboundQty;
    private Integer todayOutboundQty;
    private List<Map<String, Object>> trendData;  // 近7天趋势
    private List<Map<String, Object>> warningList; // 预警商品
}