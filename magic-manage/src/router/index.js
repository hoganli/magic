import Vue from 'vue'
import Router from 'vue-router'

import Login from '@/pages/framework/Login'
import Home from '@/pages/framework/Home'
import Dashboard from '@/pages/DashboardEp'

import User from '@/pages/framework/User'
import Role from '@/pages/framework/Role'
import Log from '@/pages/framework/Log'

import SfProject from '@/pages/app/SfProject'
import FileMonitor from '@/pages/app/FileMonitor'
import EntityMonitor from '@/pages/app/EntityMonitor'

Vue.use(Router)

// 公有路由
export const commonRouterMap = [
  {
    path: '/login',
    name: '登录',
    component: Login,
    hidden: true
  },
  {
    path: '/home',
    name: '系统主页',
    component: Home,
    iconCls: 'el-icon-menu',
    children: [
      {
        path: '/home/dashboard',
        iconCls: 'fa fa-dashboard fa-fw',
        name: '系统主页',
        component: Dashboard
      }
    ]
  }
]

// 动态加载的路由
export const asyncRouterMap = [
  {
    path: '/system',
    name: '系统管理',
    component: Home,
    iconCls: 'el-icon-setting',
    children: [
      // {
      //   path: '/unit',
      //   iconCls: 'fa fa-cubes',
      //   name: '机构管理',
      //   component: Unit,
      //   code: 'unit'
      // },
      {
        path: '/system/user',
        iconCls: 'fa fa-users fa-fw',
        name: '用户管理',
        component: User,
        code: 'user'
      },
      {
        path: '/system/role',
        iconCls: 'fa fa-user-plus fa-fw',
        name: '角色管理',
        component: Role,
        code: 'role'
      },
      {
        path: '/system/log',
        iconCls: 'fa fa-file-text fa-fw',
        name: '操作日志',
        component: Log,
        code: 'log'
      }
    ]
  },
  {
    path: '/project',
    name: '项目配置',
    component: Home,
    iconCls: 'el-icon-news',
    children: [
      {
        path: '/project/list',
        iconCls: 'fa fa-bank fa-fw',
        name: '项目配置',
        component: SfProject,
        code: 'project'
      }
    ]
  },
  {
    path: '/monitor',
    name: '项目监控',
    component: Home,
    iconCls: 'el-icon-message',
    children: [
      {
        path: '/file/list',
        iconCls: 'fa fa-envelope-open fa-fw',
        name: '文件监控',
        component: FileMonitor,
        code: 'file'
      },
      {
        path: '/entity/list',
        iconCls: 'fa fa-envelope-open fa-fw',
        name: '数据监控',
        component: EntityMonitor,
        code: 'entity'
      }
    ]
  }
]

export default new Router({
  routes: commonRouterMap
})
