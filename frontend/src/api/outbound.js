import request from '../utils/request';

// 创建出库单
export const createOutboundOrder = (data) => {
    return request({
        url: '/outbound/order',
        method: 'post',
        data
    });
};

// 提交审核
export const submitOutboundAudit = (orderId) => {
    return request({
        url: `/outbound/order/submit/${orderId}`,
        method: 'put'
    });
};

// 审核通过
export const approveOutboundOrder = (orderId, auditor) => {
    return request({
        url: `/outbound/order/approve/${orderId}`,
        method: 'put',
        params: { auditor }
    });
};

// 审核驳回
export const rejectOutboundOrder = (orderId, auditor) => {
    return request({
        url: `/outbound/order/reject/${orderId}`,
        method: 'put',
        params: { auditor }
    });
};

// 查询出库单列表
export const getOutboundOrderList = () => {
    return request({
        url: '/outbound/order/list',
        method: 'get'
    });
};

// 查询出库单详情
export const getOutboundOrderDetail = (orderId) => {
    return request({
        url: `/outbound/order/${orderId}`,
        method: 'get'
    });
};