package com.cbec.controller;

import com.cbec.common.Result;
import com.cbec.common.annotation.RequiresPermission;
import com.cbec.common.exception.BusinessException;
import com.cbec.entity.order.InboundItem;
import com.cbec.entity.order.InboundOrder;
import com.cbec.entity.dto.InboundRequest;
import com.cbec.service.InboundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inbound")
public class InboundController {

    @Autowired
    private InboundService inboundService;

    /**
     * 创建入库单（草稿）
     */
    @RequiresPermission("inbound:add")
    @PostMapping("/order")
    public Result<InboundOrder> createOrder(@RequestBody InboundRequest request) {
        InboundOrder order = request.getOrder();
        List<InboundItem> items = request.getItems();

        if (order.getWarehouseId() == null) {
            throw new BusinessException("仓库ID不能为空");
        }
        if (items == null || items.isEmpty()) {
            throw new BusinessException("入库明细不能为空");
        }
        return Result.success(inboundService.createOrder(order, items));
    }

    /**
     * 提交审核
     */
    @RequiresPermission("inbound:audit")
    @PutMapping("/order/submit/{orderId}")
    public Result<Void> submitAudit(@PathVariable Long orderId) {
        inboundService.submitAudit(orderId);
        return Result.success(null);
    }

    /**
     * 审核通过
     */
    @RequiresPermission("inbound:audit")
    @PutMapping("/order/approve/{orderId}")
    public Result<Void> approve(@PathVariable Long orderId,
                                @RequestParam String auditor) {
        inboundService.approve(orderId, auditor);
        return Result.success(null);
    }

    /**
     * 审核驳回
     */
    @RequiresPermission("inbound:audit")
    @PutMapping("/order/reject/{orderId}")
    public Result<Void> reject(@PathVariable Long orderId,
                               @RequestParam String auditor) {
        inboundService.reject(orderId, auditor);
        return Result.success(null);
    }

    /**
     * 查询入库单详情
     */
    @RequiresPermission("inbound:list")
    @GetMapping("/order/{orderId}")
    public Result<InboundOrder> getById(@PathVariable Long orderId) {
        return Result.success(inboundService.getById(orderId));
    }

    /**
     * 查询所有入库单
     */
    @RequiresPermission("inbound:list")
    @GetMapping("/order/list")
    public Result<List<InboundOrder>> listAll() {
        return Result.success(inboundService.listAll());
    }
}