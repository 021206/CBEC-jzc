package com.cbec.service;

import com.cbec.entity.inventory.Inventory;
import com.cbec.mapper.InventoryMapper;
import com.cbec.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cbec.entity.inventory.InventoryLog;
import com.cbec.mapper.InventoryLogMapper;

@Service
public class InventoryService {

    @Autowired
    private InventoryMapper inventoryMapper;
    @Autowired
    private InventoryLogMapper inventoryLogMapper;

    /**
     * 查询库存（按商品+仓库）
     */
    public Inventory getByProductAndWarehouse(Long productId, Long warehouseId) {
        return inventoryMapper.findByProductAndWarehouse(productId, warehouseId);
    }

    /**
     * 增加库存（入库审核通过时调用）
     * 使用乐观锁，如果更新失败则抛出异常
     */
    @Transactional
    public void addStock(Long productId, Long warehouseId, Integer quantity, String orderNo, String operator) {
        if (quantity == null || quantity <= 0) {
            throw new BusinessException("入库数量必须大于0");
        }

        Inventory inventory = inventoryMapper.findByProductAndWarehouse(productId, warehouseId);
        int beforeQty = 0;
        Long inventoryId = null;

        if (inventory == null) {
            // 新增库存记录
            inventory = new Inventory();
            inventory.setProductId(productId);
            inventory.setWarehouseId(warehouseId);
            inventory.setQuantity(quantity);
            inventoryMapper.insert(inventory);
            beforeQty = 0;
            inventoryId = inventory.getId();
        } else {
            beforeQty = inventory.getQuantity();
            inventoryId = inventory.getId();
            int affected = inventoryMapper.increaseStock(inventoryId, quantity, inventory.getVersion());
            if (affected == 0) {
                throw new BusinessException("库存更新失败，请重试");
            }
        }

        // 查询更新后的库存
        Inventory updated = inventoryMapper.selectById(inventoryId);

        // 记录流水
        InventoryLog log = new InventoryLog();
        log.setProductId(productId);
        log.setWarehouseId(warehouseId);
        log.setOrderNo(orderNo);
        log.setChangeType(1); // 入库
        log.setChangeQty(quantity);
        log.setBeforeQty(beforeQty);
        log.setAfterQty(updated.getQuantity());
        log.setOperator(operator);
        log.setRemark("入库审核通过");
        inventoryLogMapper.insert(log);
    }

    /**
     * 扣减库存（出库审核通过时调用）
     * 使用乐观锁，检查库存是否充足
     */
    @Transactional
    public void deductStock(Long productId, Long warehouseId, Integer quantity, String orderNo, String operator) {
        if (quantity == null || quantity <= 0) {
            throw new BusinessException("出库数量必须大于0");
        }

        Inventory inventory = inventoryMapper.findByProductAndWarehouse(productId, warehouseId);
        if (inventory == null) {
            throw new BusinessException("库存记录不存在");
        }

        if (inventory.getQuantity() < quantity) {
            throw new BusinessException("库存不足，当前库存：" + inventory.getQuantity() + "，需要：" + quantity);
        }

        int beforeQty = inventory.getQuantity();
        int affected = inventoryMapper.decreaseStock(inventory.getId(), quantity, inventory.getVersion());
        if (affected == 0) {
            throw new BusinessException("库存扣减失败，请重试");
        }

        // 查询更新后的库存
        Inventory updated = inventoryMapper.selectById(inventory.getId());

        // 记录流水
        InventoryLog log = new InventoryLog();
        log.setProductId(productId);
        log.setWarehouseId(warehouseId);
        log.setOrderNo(orderNo);
        log.setChangeType(2); // 出库
        log.setChangeQty(quantity);
        log.setBeforeQty(beforeQty);
        log.setAfterQty(updated.getQuantity());
        log.setOperator(operator);
        log.setRemark("出库审核通过");
        inventoryLogMapper.insert(log);
    }
    /**
     * 查询库存数量（按商品+仓库）
     */
    public int getStock(Long productId, Long warehouseId) {
        Inventory inventory = inventoryMapper.findByProductAndWarehouse(productId, warehouseId);
        return inventory == null ? 0 : inventory.getQuantity();
    }
}