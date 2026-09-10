import request from '../utils/request';

export const getUserPage = (params) => {
    return request({
        url: '/sys/user/page',
        method: 'get',
        params
    });
};

export const addUser = (data) => {
    return request({
        url: '/sys/user',
        method: 'post',
        data
    });
};

export const updateUser = (data) => {
    return request({
        url: '/sys/user',
        method: 'put',
        data
    });
};

export const toggleUserStatus = (id) => {
    return request({
        url: `/sys/user/status/${id}`,
        method: 'put'
    });
};

export const resetUserPassword = (id) => {
    return request({
        url: `/sys/user/resetPwd/${id}`,
        method: 'put'
    });
};

export const deleteUser = (id) => {
    return request({
        url: `/sys/user/${id}`,
        method: 'delete'
    });
};

export const getUserById = (id) => {
    return request({
        url: `/sys/user/${id}`,
        method: 'get'
    });
};