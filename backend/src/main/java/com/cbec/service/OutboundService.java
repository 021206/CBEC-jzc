package com.cbec.service;

import com.cbec.entity.order.OutboundItem;
import com.cbec.entity.order.OutboundOrder;
import com.cbec.mapper.OutboundItemMapper;
import com.cbec.mapper.OutboundOrderMapper;
import com.cbec.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OutboundService {

    @Autowired
    private OutboundOrderMapper outboundOrderMapper;

    @Autowired
    private OutboundItemMapper outboundItemMapper;

    @Autowired
    private InventoryService inventoryService;

    @Transactional
    public OutboundOrder createOrder(OutboundOrder order, List<OutboundItem> items) {
        order.setOrderNo("OUT" + System.currentTimeMillis());
        order.setStatus(1); // 草稿
        outboundOrderMapper.insert(order);

        for (OutboundItem item : items) {
            item.setOutboundId(order.getId());
            item.setActualQty(item.getPlannedQty());
            outboundItemMapper.insert(item);
        }
        return order;
    }

    public void submitAudit(Long orderId) {
        OutboundOrder order = outboundOrderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("出库单不存在");
        }
        if (order.getStatus() != 1) {
            throw new BusinessException("只有草稿状态的单据才能提交审核");
        }
        // 提交审核前检查库存是否充足
        List<OutboundItem> items = outboundItemMapper.selectByOutboundId(orderId);
        for (OutboundItem item : items) {
            int stock = inventoryService.getStock(item.getProductId(), order.getWarehouseId());
            if (stock < item.getPlannedQty()) {
                throw new BusinessException("商品ID " + item.getProductId() + " 库存不足");
            }
        }
        order.setStatus(2);
        outboundOrderMapper.updateStatus(order);
    }

    @Transactional
    public void approve(Long orderId, String auditor) {
        OutboundOrder order = outboundOrderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("出库单不存在");
        }
        if (order.getStatus() != 2) {
            throw new BusinessException("只有待审核状态的单据才能审核");
        }

        List<OutboundItem> items = outboundItemMapper.selectByOutboundId(orderId);
        for (OutboundItem item : items) {
            inventoryService.deductStock(
                    item.getProductId(),
                    order.getWarehouseId(),
                    item.getActualQty(),
                    order.getOrderNo(),
                    auditor
            );
        }

        order.setStatus(3);
        order.setAuditor(auditor);
        order.setAuditTime(LocalDateTime.now());
        outboundOrderMapper.updateStatus(order);
    }

    public void reject(Long orderId, String auditor) {
        OutboundOrder order = outboundOrderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("出库单不存在");
        }
        if (order.getStatus() != 2) {
            throw new BusinessException("只有待审核状态的单据才能驳回");
        }
        order.setStatus(4);
        order.setAuditor(auditor);
        order.setAuditTime(LocalDateTime.now());
        outboundOrderMapper.updateStatus(order);
    }

    public OutboundOrder getById(Long id) {
        return outboundOrderMapper.selectById(id);
    }

    public List<OutboundOrder> listAll() {
        return outboundOrderMapper.selectAll();
    }
}