import request from '../utils/request';

export const getProductPage = (params) => {
    return request({
        url: '/product/page',
        method: 'get',
        params
    });
};

export const addProduct = (data) => {
    return request({
        url: '/product',
        method: 'post',
        data
    });
};

export const updateProduct = (data) => {
    return request({
        url: '/product',
        method: 'put',
        data
    });
};

export const deleteProduct = (id) => {
    return request({
        url: `/product/${id}`,
        method: 'delete'
    });
};

export const toggleProductStatus = (id) => {
    return request({
        url: `/product/status/${id}`,
        method: 'put'
    });
};

// 获取所有启用的商品（下拉框用）
export const getAllProducts = () => {
    return request({
        url: '/product/list',
        method: 'get'
    });
};