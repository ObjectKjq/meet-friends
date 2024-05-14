import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/register',
    component: () => import('@/views/register/index'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    redirect: '/statistics',
    children: [{
      path: 'statistics',
      name: 'Statistics',
      component: () => import('@/views/dashboard/index'),
      meta: { title: '首页', icon: 'dashboard', roles: ['admin'] }
    }]
  },
  
]

// 动态路由规则
export const asyncRoutes = [
  {
    path: '/user',
    component: Layout,
    children: [
      {
        path: 'index',
        component: () => import('@/views/user/index'),
        name: 'User',
        meta: { title: '用户管理', icon: 'el-icon-user-solid', roles: ['admin'] }
      }
    ]
  },
  {
    path: '/tag',
    component: Layout,
    children: [
      {
        path: 'index',
        component: () => import('@/views/tag/index'),
        name: 'Tag',
        meta: { title: '标签管理', icon: 'el-icon-collection-tag', roles: ['admin'] }
      }
    ]
  },
  {
    path: '/team',
    component: Layout,
    children: [
      {
        path: 'index',
        component: () => import('@/views/team/index'),
        name: 'Team',
        meta: { title: '队伍管理', icon: 'el-icon-s-comment', roles: ['admin'] }
      }
    ]
  },
  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true, roles: ['admin'] },
]

const createRouter = () => new Router({
  mode: 'history', // require service support
  base: '/back/',
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
