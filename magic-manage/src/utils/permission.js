/**
 * Created by hogan.li on 2018/4/17.
 */
import router from '../router'

import {getUser} from '@/utils/auth' // 验权

import {asyncRouterMap, commonRouterMap} from '@/router/index'

const whiteList = ['checkPassword', 'updatePassword', 'loginCheck', '/login', '/404'] //白名单,不需要登录的路由

/**
 * 判断用户是否拥有此菜单
 */
function hasMenu(menus, route) {
  if (route.code) {
    // 如果这个路由有menu属性,就需要判断用户是否拥有此menu权限
    return menus.indexOf(route.code) > -1
  } else {
    return true
  }
}

/**
 * 判断是否显示权限
 */
export function hasPermission(permission) {
  let loginUser = JSON.parse(getUser())
  let permissions = loginUser.permissions
  return permissions.indexOf(permission) > -1
}

/**
 * 递归过滤异步路由表，返回符合用户菜单权限的路由表
 * @param asyncRouterMap
 * @param menus
 */
function filterAsyncRouter(asyncRouterMap, menus) {
  const accessedRouters = asyncRouterMap.filter(route => {
    //filter,js语法里数组的过滤筛选方法
    if (hasMenu(menus, route)) {
      if (route.children && route.children.length) {
        //如果这个路由下面还有下一级的话,就递归调用
        route.children = filterAsyncRouter(route.children, menus)
        //如果过滤一圈后,没有子元素了,这个父级菜单就也不显示了
        return (route.children && route.children.length)
      }
      return true
    }
    return false
  })
  return accessedRouters
}

// 导航守卫：主要用来通过跳转或取消的方式守卫导航
var add = false // 创建路由标识
router.beforeEach((to, from, next) => {

  // 已登录
  if (getUser()) {
    // 已登录访问根路径跳转到首页
    if(to.path === '/') {
      next('/home')
    }
    // 执行动态添加路由操作
    if(!add){
      let loginUser = JSON.parse(getUser())
      // 筛选出当前用户的菜单
      const accessedRouters = filterAsyncRouter(asyncRouterMap, loginUser.menus)
      // 创建路由
      router.options.routes = router.options.routes.concat(accessedRouters)
      router.addRoutes(accessedRouters)
      add = true // 标记已经创建，否则会反复创建路由
      next({...to})
    }else {
      next()
    }
  } else if (whiteList.indexOf(to.path) !== -1) {
    // 白名单直接放行
    next()
  } else {
    // 未登录又不是白名单，跳转登录页面
    next('/login')
  }

})
