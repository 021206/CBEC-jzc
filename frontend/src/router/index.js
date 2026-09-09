import { createRouter, createWebHistory } from 'vue-router';
import Login from '../views/Login.vue';
import Layout from '../views/Layout.vue';

const routes = [
    { path: '/login', component: Login, meta: { title: '登录' } },
    {
        path: '/',
        component: Layout,
        redirect: '/dashboard',
        children: [
            //数据看板
            { path: 'dashboard', component: () => import('@/views/Dashboard.vue') },

            //库存管理
            { path: 'inventory/stock', component: () => import('@/views/inventory/Stock.vue') },
            { path: 'inventory/log', component: () => import('@/views/inventory/Log.vue') },

            //入库管理
            { path: 'inventory/inbound', component: () => import('@/views/inbound/index.vue') },
            { path: 'inventory/inbound/create', component: () => import('@/views/inbound/create.vue') },
            { path: 'inventory/inbound/detail/:id', component: () => import('@/views/inbound/detail.vue') },

            //出库管理
            { path: 'inventory/outbound', component: () => import('@/views/outbound/index.vue') },
            { path: 'inventory/outbound/create', component: () => import('@/views/outbound/create.vue') },
            { path: 'inventory/outbound/detail/:id', component: () => import('@/views/outbound/detail.vue') },

            //基础数据
            { path: 'warehouse', component: () => import('@/views/warehouse/index.vue') },
            { path: 'product', component: () => import('@/views/product/index.vue') },
            { path: 'category', component: () => import('@/views/category/index.vue') },
            //系统管理
            { path: 'system/user', component: () => import('@/views/system/user/index.vue') },
            { path: 'system/role', component: () => import('@/views/system/role/index.vue') },
        ]
    }
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

// 路由守卫
router.beforeEach((to, from, next) => {
    const token = localStorage.getItem('accessToken');
    if (to.path !== '/login' && !token) {
        next('/login');
    } else {
        next();
    }
});

export default router;