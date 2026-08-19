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
            { path: 'inventory/warehouse', component: () => import('../views/warehouse/index.vue') }
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