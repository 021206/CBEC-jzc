import request from '../utils/request';

// 创建入库单
export const createInboundOrder = (data) => {
    return request({
        url: '/inbound/order',
        method: 'post',
        data
    });
};

// 提交审核
export const submitInboundAudit = (orderId) => {
    return request({
        url: `/inbound/order/submit/${orderId}`,
        method: 'put'
    });
};

// 审核通过
export const approveInboundOrder = (orderId, auditor) => {
    return request({
        url: `/inbound/order/approve/${orderId}`,
        method: 'put',
        params: { auditor }
    });
};

// 审核驳回
export const rejectInboundOrder = (orderId, auditor) => {
    return request({
        url: `/inbound/order/reject/${orderId}`,
        method: 'put',
        params: { auditor }
    });
};

// 查询入库单列表
export const getInboundOrderList = () => {
    return request({
        url: '/inbound/order/list',
        method: 'get'
    });
};

// 查询入库单详情
export const getInboundOrderDetail = (orderId) => {
    return request({
        url: `/inbound/order/${orderId}`,
        method: 'get'
    });
};