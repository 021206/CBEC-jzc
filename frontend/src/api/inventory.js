import request from '../utils/request';

// 查询库存（按商品+仓库）
export const getInventoryByProductAndWarehouse = (productId, warehouseId) => {
    return request({
        url: `/inventory/${productId}/${warehouseId}`,
        method: 'get'
    });
};

// 查询所有库存（后续补充分页接口）
export const getInventoryList = (params) => {
    return request({
        url: '/inventory/list',
        method: 'get',
        params
    });
};