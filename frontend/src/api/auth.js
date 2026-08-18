import request from '../utils/request';

export const login = (username, password) => {
    return request({
        url: '/auth/login',
        method: 'post',
        // 1. 使用 URLSearchParams 将数据转成表单格式 (username=admin&password=xxx)
        data: new URLSearchParams({ username, password }),
        // 2. 强制指定请求头为 application/x-www-form-urlencoded
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
    });
};

export const getMenus = () => {
    return request({
        url: '/auth/menus',
        method: 'get'
    });
};