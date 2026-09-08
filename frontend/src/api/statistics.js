import request from '../utils/request';

// 获取数据概览
export const getStatisticsOverview = () => {
    return request({
        url: '/statistics/overview',
        method: 'get'
    });
};