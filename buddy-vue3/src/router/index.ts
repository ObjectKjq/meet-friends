export default [
    {
        path: '/',
        component: () => import('../pages/Index.vue')
    },
    {
        path: '/team',
        component: () => import('../pages/Team.vue')
    },
    {
        path: '/user',
        component: () => import('../pages/User.vue'),
    },
    {
        path: '/search',
        component: () => import('../pages/Search.vue')
    },
    {
        path: '/user/edit',
        component: () => import('../pages/EditUser.vue')
    }
]