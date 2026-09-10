import request from '../utils/request';

// 查询所有角色
export const getRoleList = () => {
    return request({
        url: '/sys/role/list',
        method: 'get'
    });
};

// 新增角色
export const addRole = (data) => {
    return request({
        url: '/sys/role',
        method: 'post',
        data
    });
};

// 编辑角色
export const updateRole = (data) => {
    return request({
        url: '/sys/role',
        method: 'put',
        data
    });
};

// 删除角色
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

// 查询角色详情
export const getRoleById = (id) => {
    return request({
        url: `/sys/role/${id}`,
        method: 'get'
    });
};

// 查询角色已分配的菜单ID
export const getRoleMenus = (roleId) => {
    return request({
        url: `/sys/role/menus/${roleId}`,
        method: 'get'
    });
};

// 给角色分配菜单权限
export const assignMenus = (roleId, menuIds) => {
    return request({
        url: '/sys/role/assignMenus',
        method: 'post',
        data: { roleId, menuIds }
    });
};