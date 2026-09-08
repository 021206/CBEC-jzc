import request from '../utils/request';

// 获取树形结构（用于展示）
export const getCategoryTree = () => {
    return request({
        url: '/category/tree',
        method: 'get'
    });
};

// 获取所有分类（平铺列表，用于下拉框）
export const getAllCategories = () => {
    return request({
        url: '/category/list',
        method: 'get'
    });
};

// 新增分类
export const addCategory = (data) => {
    return request({
        url: '/category',
        method: 'post',
        data
    });
};

// 编辑分类
export const updateCategory = (data) => {
    return request({
        url: '/category',
        method: 'put',
        data
    });
};

// 删除分类
export const deleteCategory = (id) => {
    return request({
        url: `/category/${id}`,
        method: 'delete'
    });
};