package com.cbec.service;

import com.cbec.entity.order.InboundItem;
import com.cbec.entity.order.InboundOrder;
import com.cbec.mapper.InboundItemMapper;
import com.cbec.mapper.InboundOrderMapper;
import com.cbec.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InboundService {

    @Autowired
    private InboundOrderMapper inboundOrderMapper;

    @Autowired
    private InboundItemMapper inboundItemMapper;

    @Autowired
    private InventoryService inventoryService;

    /**
     * 创建入库单（草稿状态）
     */
    @Transactional
    public InboundOrder createOrder(InboundOrder order, List<InboundItem> items) {
        // 生成入库单号
        order.setOrderNo("IN" + System.currentTimeMillis());
        order.setStatus(1); // 草稿
        inboundOrderMapper.insert(order);

        // 保存明细
        for (InboundItem item : items) {
            item.setInboundId(order.getId());
            item.setActualQty(item.getPlannedQty()); // 默认实际=计划
            inboundItemMapper.insert(item);
        }
        return order;
    }

    /**
     * 提交审核（草稿 → 待审核）
     */
    public void submitAudit(Long orderId) {
        InboundOrder order = inboundOrderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("入库单不存在");
        }
        if (order.getStatus() != 1) {
            throw new BusinessException("只有草稿状态的单据才能提交审核");
        }
        order.setStatus(2); // 待审核
        inboundOrderMapper.updateStatus(order);
    }

    /**
     * 审核通过（待审核 → 已完成，增加库存）
     */
    @Transactional
    public void approve(Long orderId, String auditor) {
        InboundOrder order = inboundOrderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("入库单不存在");
        }
        if (order.getStatus() != 2) {
            throw new BusinessException("只有待审核状态的单据才能审核");
        }

        // 获取明细
        List<InboundItem> items = inboundItemMapper.selectByInboundId(orderId);

        // 增加库存
        for (InboundItem item : items) {
            inventoryService.addStock(
                    item.getProductId(),
                    order.getWarehouseId(),
                    item.getActualQty(),
                    order.getOrderNo(),
                    auditor
            );
        }

        // 更新状态
        order.setStatus(3); // 已完成
        order.setAuditor(auditor);
        order.setAuditTime(LocalDateTime.now());
        inboundOrderMapper.updateStatus(order);
    }

    /**
     * 审核驳回（待审核 → 驳回）
     */
    public void reject(Long orderId, String auditor) {
        InboundOrder order = inboundOrderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("入库单不存在");
        }
        if (order.getStatus() != 2) {
            throw new BusinessException("只有待审核状态的单据才能驳回");
        }
        order.setStatus(4); // 驳回
        order.setAuditor(auditor);
        order.setAuditTime(LocalDateTime.now());
        inboundOrderMapper.updateStatus(order);
    }

    public InboundOrder getById(Long id) {
        InboundOrder order = inboundOrderMapper.selectById(id);
        if (order != null) {
            List<InboundItem> items = inboundItemMapper.selectByInboundId(id);
            order.setItems(items);
        }
        return order;
    }

    public List<InboundOrder> listAll() {
        return inboundOrderMapper.selectAll();
    }
}