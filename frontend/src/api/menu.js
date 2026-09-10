import request from '../utils/request';

// 获取菜单树
export const getMenuTree = () => {
    return request({
        url: '/sys/menu/tree',
        method: 'get'
    });
};

// 新增菜单
export const addMenu = (data) => {
    return request({
        url: '/sys/menu',
        method: 'post',
        data
    });
};

// 编辑菜单
export const updateMenu = (data) => {
    return request({
        url: '/sys/menu',
        method: 'put',
        data
    });
};

// 删除菜单
export const deleteMenu = (id) => {
    return request({
        url: `/sys/menu/${id}`,
        method: 'delete'
    });
};

// 查询菜单详情
export const getMenuById = (id) => {
    return request({
        url: `/sys/menu/${id}`,
        method: 'get'
    });
};