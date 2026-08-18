import axios from 'axios';
import { ElMessage } from 'element-plus';
// import router from '@/router';

const request = axios.create({
    baseURL: 'http://localhost:8080',
    timeout: 10000
});

request.interceptors.request.use(
    config => {
        const token = localStorage.getItem('accessToken');
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        return config;
    },
    error => Promise.reject(error)
);

request.interceptors.response.use(
    response => {
        const res = response.data;
        if (res.code === 401) {
            ElMessage.error(res.msg || '未登录，请重新登录');
            localStorage.removeItem('accessToken');
            // router.push('/login');
            window.location.href = '/login'; // 保持你现在的也行
            return Promise.reject(new Error('未登录'));
        }
        if (res.code !== 200) {
            ElMessage.error(res.msg || '请求失败');
            return Promise.reject(new Error(res.msg));
        }
        return res;
    },
    error => {
        console.error('请求发生网络错误:', error);
        ElMessage.error(error.message || '网络连接失败，请检查后端服务是否启动');
        return Promise.reject(error);
    }
);

export default request;