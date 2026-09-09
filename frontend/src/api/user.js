import request from '../utils/request';

// 分页查询
export const getUserPage = (params) => {
    return request({
        url: '/sys/user/page',
        method: 'get',
        params
    });
};

// 新增
export const addUser = (data) => {
    return request({
        url: '/sys/user',
        method: 'post',
        data
    });
};

// 编辑
export const updateUser = (data) => {
    return request({
        url: '/sys/user',
        method: 'put',
        data
    });
};

// 切换状态
export const toggleUserStatus = (id) => {
    return request({
        url: `/sys/user/status/${id}`,
        method: 'put'
    });
};

// 重置密码
export const resetUserPassword = (id) => {
    return request({
        url: `/sys/user/resetPwd/${id}`,
        method: 'put'
    });
};

// 删除
export const deleteUser = (id) => {
    return request({
        url: `/sys/user/${id}`,
        method: 'delete'
    });
};

// 查询详情
export const getUserById = (id) => {
    return request({
        url: `/sys/user/${id}`,
        method: 'get'
    });
};