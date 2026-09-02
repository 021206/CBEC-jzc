package com.cbec.controller;

import com.cbec.common.Result;
import com.cbec.common.exception.BusinessException;
import com.cbec.entity.order.OutboundItem;
import com.cbec.entity.order.OutboundOrder;
import com.cbec.service.OutboundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.cbec.entity.dto.OutboundRequest;

import java.util.List;

@RestController
@RequestMapping("/outbound")
public class OutboundController {

    @Autowired
    private OutboundService outboundService;

    @PostMapping("/order")
    public Result<OutboundOrder> createOrder(@RequestBody OutboundRequest request) {
        OutboundOrder order = request.getOrder();
        List<OutboundItem> items = request.getItems();

        if (order.getWarehouseId() == null) {
            throw new BusinessException("仓库ID不能为空");
        }
        if (items == null || items.isEmpty()) {
            throw new BusinessException("出库明细不能为空");
        }
        return Result.success(outboundService.createOrder(order, items));
    }

    @PutMapping("/order/submit/{orderId}")
    public Result<Void> submitAudit(@PathVariable Long orderId) {
        outboundService.submitAudit(orderId);
        return Result.success(null);
    }

    @PutMapping("/order/approve/{orderId}")
    public Result<Void> approve(@PathVariable Long orderId,
                                @RequestParam String auditor) {
        outboundService.approve(orderId, auditor);
        return Result.success(null);
    }

    @PutMapping("/order/reject/{orderId}")
    public Result<Void> reject(@PathVariable Long orderId,
                               @RequestParam String auditor) {
        outboundService.reject(orderId, auditor);
        return Result.success(null);
    }

    @GetMapping("/order/{orderId}")
    public Result<OutboundOrder> getById(@PathVariable Long orderId) {
        return Result.success(outboundService.getById(orderId));
    }

    @GetMapping("/order/list")
    public Result<List<OutboundOrder>> listAll() {
        return Result.success(outboundService.listAll());
    }
}