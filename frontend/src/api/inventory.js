import request from '../utils/request';

// 查询单条库存（已有）
export const getInventoryByProductAndWarehouse = (productId, warehouseId) => {
    return request({
        url: `/inventory/${productId}/${warehouseId}`,
        method: 'get'
    });
};

// 分页查询库存列表（新增）
export const getInventoryList = (params) => {
    return request({
        url: '/inventory/list',
        method: 'get',
        params
    });
};