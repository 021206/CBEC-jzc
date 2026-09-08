import request from '../utils/request';

// 查询单条库存（按商品+仓库）
export const getInventoryByProductAndWarehouse = (productId, warehouseId) => {
    return request({
        url: `/inventory/${productId}/${warehouseId}`,
        method: 'get'
    });
};

// 分页查询库存列表
export const getInventoryList = (params) => {
    return request({
        url: '/inventory/list',
        method: 'get',
        params
    });
};

// 分页查询库存流水
export const getInventoryLogPage = (params) => {
    return request({
        url: '/inventory/log/page',
        method: 'get',
        params
    });
};