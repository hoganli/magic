import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

// State：Vuex store 实例的根 state 对象
const state = {
  // 当前用户
  user: {
    id: '',
    account: '',
    userName: '',
    menus: ['permission', 'role', 'user', 'unit', 'log'],
    permissions: ['role:add', 'role:list', 'role:update', 'role:delete', 'permission:list', 'permission:update', 'permission:add']
  },
  globalTableHeight: 0
}

// Action：将状态提交给mutaions处理，不直接变更状态，且支持异步
const actions = {}

// Mutation：更改Vuex的store中的状态的唯一方法是提交mutation
const mutations = {
  saveUserId(state, id) {
    state.user.id = id
  },
  saveAccount(state, account) {
    state.user.account = account
  },
  saveUserName(state, userName) {
    state.user.userName = userName
  },
  setGlobalTableHeight(state, globalTableHeight) {
    state.globalTableHeight = globalTableHeight
  }
}

// 获取状态信息
const getter = {}

// 注册所有模块
export default new Vuex.Store({
  state, getter, mutations, actions
})
