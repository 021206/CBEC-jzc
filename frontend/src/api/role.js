import request from '../utils/request';

// 查询所有角色
export const getRoleList = () => {
    return request({
        url: '/sys/role/list',
        method: 'get'
    });
};

// 新增
export const addRole = (data) => {
    return request({
        url: '/sys/role',
        method: 'post',
        data
    });
};

// 编辑
export const updateRole = (data) => {
    return request({
        url: '/sys/role',
        method: 'put',
        data
    });
};

// 删除
export const deleteRole = (id) => {
    return request({
        url: `/sys/role/${id}`,
        method: 'delete'
    });
};

// 切换状态
export const toggleRoleStatus = (id) => {
    return request({
        url: `/sys/role/status/${id}`,
        method: 'put'
    });
};

// 查询详情
export const getRoleById = (id) => {
    return request({
        url: `/sys/role/${id}`,
        method: 'get'
    });
};