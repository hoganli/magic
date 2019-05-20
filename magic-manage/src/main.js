import Vue from 'vue'

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import echarts from 'echarts'                   // Echarts图表库
import 'font-awesome/css/font-awesome.min.css'  // font-awesome字体库

import router from './router'                   // 路由
import store from './store/store.js'            // store
import {getUser} from './utils/auth' // 验权

import ajax from './utils/ajax.js'              // ajax工具类
import date from './utils/date.js'              // date工具类
import cryptoJs from 'crypto-js'                // crypto加密工具类
import uuid from 'node-uuid'                    // uuid 工具类  .v1()-时间戳生成；.v4()-随机数生成

import {hasPermission} from "./utils/permission.js";
import {getFullHeight, getTableHeight, getTypesetMainHeight, reSetGlobalHeight} from "./utils/globalParams.js";

import App from './App'                         // 程序主入口文件

Vue.use(ElementUI)

// 将公共方法绑定到全局
Vue.prototype.$echarts = echarts;
Vue.prototype.$ajax = ajax
Vue.prototype.$date = date
Vue.prototype.$cryptoJs = cryptoJs
Vue.prototype.$uuid = uuid
Vue.prototype.getUser = getUser
Vue.prototype.hasPerm = hasPermission
Vue.prototype.getGlobalFullHeight = getFullHeight
Vue.prototype.getGlobalTableHeight = getTableHeight
Vue.prototype.getGlobalTypesetMainHeight = getTypesetMainHeight
Vue.prototype.reSetGlobalHeightParams = reSetGlobalHeight

// 圆角图片处理
CanvasRenderingContext2D.prototype.roundRect = function (x, y, w, h, r) {
  var min_size = Math.min(w, h);
  if (r > min_size / 2) r = min_size / 2;
  // 开始绘制
  this.beginPath();
  this.moveTo(x + r, y);
  this.arcTo(x + w, y, x + w, y + h, r);
  this.arcTo(x + w, y + h, x, y + h, r);
  this.arcTo(x, y + h, x, y, r);
  this.arcTo(x, y, x + w, y, r);
  this.closePath();
  return this;
}

Vue.config.productionTip = false
window.bus = new Vue();
new Vue({
  el: '#app',
  router,
  store,
  template: '<App/>',
  components: {App}
})

