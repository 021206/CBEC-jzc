import request from '../utils/request';

// 分页查询
export const getWarehousePage = (params) => {
    return request({
        url: '/warehouse/page',
        method: 'get',
        params
    });
};

// 新增
export const addWarehouse = (data) => {
    return request({
        url: '/warehouse',
        method: 'post',
        data
    });
};

// 编辑
export const updateWarehouse = (data) => {
    return request({
        url: '/warehouse',
        method: 'put',
        data
    });
};

// 删除
export const deleteWarehouse = (id) => {
    return request({
        url: `/warehouse/${id}`,
        method: 'delete'
    });
};

// 切换状态
export const toggleWarehouseStatus = (id) => {
    return request({
        url: `/warehouse/status/${id}`,
        method: 'put'
    });
};

// 获取所有启用的仓库（下拉框用）
export const getAllWarehouses = () => {
    return request({
        url: '/warehouse/list',
        method: 'get'
    });
};