// 获取所有分类（平铺列表，用于下拉框）
export const getAllCategories = () => {
    return request({
        url: '/category/list',
        method: 'get'
    });
};