import { createRouter, createWebHistory } from 'vue-router';
import Login from '../views/Login.vue';
import Layout from '../views/Layout.vue';

const routes = [
    { path: '/login', component: Login, meta: { title: '登录' } },
    {
        path: '/',
        component: Layout,
        redirect: '/inventory/stock',
        children: [
            { path: 'inventory/stock', component: () => import('../views/inventory/Stock.vue') },
            { path: 'warehouse', component: () => import('../views/warehouse/index.vue') },
            { path: 'inventory/warehouse', component: () => import('../views/warehouse/index.vue') },
            { path: 'product', component: () => import('@/views/product/index.vue') },
            { path: 'inventory/stock', component: () => import('@/views/inventory/Stock.vue') },
            { path: 'inventory/inbound', component: () => import('@/views/inbound/index.vue') },
// 入库创建（后续实现）
            { path: 'inventory/inbound/create', component: () => import('@/views/inbound/create.vue') },
// 入库详情（后续实现）
            { path: 'inventory/inbound/detail/:id', component: () => import('@/views/inbound/detail.vue') },
            // 出库管理
            { path: 'inventory/outbound', component: () => import('@/views/outbound/index.vue') },
            { path: 'inventory/outbound/create', component: () => import('@/views/outbound/create.vue') },
            { path: 'inventory/outbound/detail/:id', component: () => import('@/views/outbound/detail.vue') },
        ]
    }
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

router.beforeEach((to, from, next) => {
    const token = localStorage.getItem('accessToken');
    if (to.path !== '/login' && !token) {
        next('/login');
    } else {
        next();
    }
});

export default router;