import { asyncRoutes, constantRoutes } from '@/router'
// 引入路由
import router from './router'
// 引入仓库
import store from './store'
// 引入一个提示组件
import { Message } from 'element-ui'
// 引入进度条组件
import NProgress from 'nprogress' 
import 'nprogress/nprogress.css'
// 获取文章标题
import getPageTitle from '@/utils/get-page-title'
// 开启进度条
NProgress.configure({ showSpinner: false }) 

// 定义白名单
const whiteList = ['/login', '/register']

// 路由前置守卫，
router.beforeEach(async(to, from, next) => {
  // 开启进度条
  NProgress.start()

  // 设置标题，每个路由中都有一个meta，meta有title
  document.title = getPageTitle(to.meta.title)

  // 获取用户名
  const hasGetUserInfo = store.getters.name
  // 用户已经登录
  if (hasGetUserInfo) {
    console.log(to.path);
    if (whiteList.indexOf(to.path) !== -1) {
      // 如果是登陆页面就跳转到首页
      next({ path: '/' })
      NProgress.done()
    }else{
      next()
      NProgress.done()
    }
  // 用户登录未知，需要判定
  }else{
    await store.dispatch('user/getInfo').catch((error)=>{})
    // 重新获取用户名
    const roles = store.getters.roles
    // 用户登录了，需要重新渲染路由
    if(roles.length > 0){
      // 根据角色，生成当前角色可以访问的路由
      const accessRoutes = await store.dispatch('permission/generateRoutes', roles)
      console.log(accessRoutes);
      // 把角色生的路由规则添加到路由系统中
      router.addRoutes(accessRoutes)
      if (whiteList.indexOf(to.path) !== -1) {
        // 如果是登陆页面就跳转到首页
        next({ path: '/' })
        NProgress.done()
      }else{
        // next()
        next({...to, replace: true})
        NProgress.done()
      }
    //用户没有登陆，判断当前页面 
    }else{
      if (whiteList.indexOf(to.path) !== -1) {
        next()
        NProgress.done()
      }else{
        // Message.error('你还没有登录！！')
        next(`/login?redirect=${to.path}`)
        NProgress.done()
      }
    }
  }

  // else{// 用户没有登录
  //   // const newHasGetUserInfo = '';
  //   // try {
  //   // 现在不确定用户有没有登陆，调用接口插入数据
  //   await store.dispatch('user/getInfo').catch((error)=>{
  //     Message.error('服务器异常')
  //   })
  //   // 重新获取用户名
  //   const newHasGetUserInfo = store.getters.name
  //   // 确实登录了
  //   if(newHasGetUserInfo){
      
  //     if (whiteList.indexOf(to.path) !== -1) {
  //       // 如果是登陆页面就跳转到首页
  //       next({ path: '/' })
  //       NProgress.done()
  //     }else{
  //       next()
  //       NProgress.done()
  //     }
  //   }else{
  //     // 没有登录
  //     if (whiteList.indexOf(to.path) !== -1) {
  //       next()
  //       NProgress.done()
  //     }else{
  //       Message.error('你还没有登录！！')
  //       next(`/login?redirect=${to.path}`)
  //       NProgress.done()
  //     }
  //   }
  // }
})

router.afterEach(() => {
  NProgress.done()
})
