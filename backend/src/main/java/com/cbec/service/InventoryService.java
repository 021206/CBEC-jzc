package com.cbec.service;

import com.cbec.common.exception.BusinessException;
import com.cbec.entity.inventory.Inventory;
import com.cbec.entity.inventory.InventoryLog;
import com.cbec.entity.product.Product;
import com.cbec.entity.warehouse.Warehouse;
import com.cbec.mapper.InventoryLogMapper;
import com.cbec.mapper.InventoryMapper;
import com.cbec.mapper.ProductMapper;
import com.cbec.mapper.WarehouseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InventoryService {

    @Autowired
    private InventoryMapper inventoryMapper;

    @Autowired
    private InventoryLogMapper inventoryLogMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private WarehouseMapper warehouseMapper;

    /**
     * 查询库存（按商品+仓库），返回完整对象
     */
    public Inventory getByProductAndWarehouse(Long productId, Long warehouseId) {
        return inventoryMapper.findByProductAndWarehouse(productId, warehouseId);
    }

    /**
     * 查询库存数量（按商品+仓库），只返回数量
     */
    public int getStock(Long productId, Long warehouseId) {
        Inventory inventory = getByProductAndWarehouse(productId, warehouseId);
        return inventory == null ? 0 : inventory.getQuantity();
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
        Long inventoryId;

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
     * 分页查询库存列表（带商品名称和仓库名称）
     */
    public Map<String, Object> list(Long productId, Long warehouseId, Integer pageNum, Integer pageSize) {
        if (pageNum == null || pageNum < 1) pageNum = 1;
        if (pageSize == null || pageSize < 1) pageSize = 10;

        int offset = (pageNum - 1) * pageSize;
        List<Inventory> list = inventoryMapper.selectPage(productId, warehouseId, offset, pageSize);
        int total = inventoryMapper.count(productId, warehouseId);

        // 填充商品名称和仓库名称
        for (Inventory inv : list) {
            if (inv.getProductId() != null) {
                Product product = productMapper.selectById(inv.getProductId());
                inv.setProductName(product != null ? product.getName() : null);
            }
            if (inv.getWarehouseId() != null) {
                Warehouse warehouse = warehouseMapper.selectById(inv.getWarehouseId());
                inv.setWarehouseName(warehouse != null ? warehouse.getName() : null);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);
        return result;
    }
}