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
    },
    {
        path: '/user/list',
        component: () => import('../pages/SearchResultPage.vue')
    },
    { 
        path: '/team/add',
        component: () => import('../pages/TeamAddPage.vue')
    },
    { 
        path: '/team/update',
        component: () => import('../pages/TeamUpdatePage.vue')
    },
    { 
        path: '/user/update',
        component: () => import('../pages/UserUpdatePage.vue')
    },
    { 
        path: '/user/team/create',
        component: () => import('../pages/UserTeamCreatePage.vue')
    },
    { 
        path: '/user/team/join',
        component: () => import('../pages/UserTeamJoinPage.vue')
    },
    {
        path: '/user/update/tag',
        component: () => import('../pages/UserUpdateTagPage.vue')
    },
    {
        path: '/login',
        component: () => import('../pages/Login.vue')
    },
]