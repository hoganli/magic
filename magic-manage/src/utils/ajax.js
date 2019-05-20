// 配置API接口地址（已通过/config/index.js配置proxyTable实现）
var root = 'http://127.0.0.1/magic/api'

// 引用组件
import router from '../router'
import {Message} from 'element-ui';
var axios = require('axios')
var crypto = require('crypto-js')

// 自定义判断元素类型JS
function toType(obj) {
  return ({}).toString.call(obj).match(/\s([a-zA-Z]+)/)[1].toLowerCase()
}

// 参数过滤函数
function filterNull(o) {
  for (var key in o) {
    if (o[key] === null) {
      delete o[key]
    }
    if (toType(o[key]) === 'string') {
      o[key] = o[key].trim()
    } else if (toType(o[key]) === 'object') {
      o[key] = filterNull(o[key])
    } else if (toType(o[key]) === 'array') {
      o[key] = filterNull(o[key])
    }
  }
  return o
}

// 接口处理函数
function apiAxios(method, url, params, success, failure) {
  if (params) {
    params = filterNull(params)
  }

  // 参数摘要
  var paramString = ''
  if(params){
    paramString = JSON.stringify(params)
  }
  var salt = '12345'
  var digest = crypto.SHA256(paramString + salt)

  // 真实请求
  axios({
    method: method,
    baseURL: root,
    url: url,
    headers: {
      'Digest': salt + '!' + digest
    },
    data: method === 'POST' || method === 'PUT' ? params : null,
    params: method === 'GET' || method === 'DELETE' ? params : null,
    withCredentials: false
  })
  // 正常响应处理（成功or失败）
    .then(function (response) {
      if (response.data.success === true) {
        if (success) {  // 如果定义了success方法，将vo作为参数传入
          success(response.data)
        }
      } else {
        if (failure) {  // 如果定义了failure方法，将vo作为参数传入
          failure(response.data)
        }
      }
    })
    // 错误响应处理
    .catch(function (error) {
      console.log(error.response.status)
      if (error.response) {
        // 请求已发出，但服务器响应的状态码不在2xx范围内
        switch (error.response.status) {
          // 400：请求错误，主要指参数错误
          case 400:
            Message.error(error.response.status + "：" + error.response.data.message)
            break
          // 401：没有登录
          case 401:
            Message.error(error.response.status + "：" + error.response.data.message)
            router.replace('/login')
            break
          // 403：没有权限
          case 403:
            Message.error(error.response.status + "：" + error.response.data.message)
            break
        }
      } else {
        // 请求异常
        Message.error(error.message)
      }
    })
}

// 返回在vue模板中的调用接口
export default {
  get: function (url, params, success, failure) {
    return apiAxios('GET', url, params, success, failure)
  },
  post: function (url, params, success, failure) {
    return apiAxios('POST', url, params, success, failure)
  },
  put: function (url, params, success, failure) {
    return apiAxios('PUT', url, params, success, failure)
  },
  delete: function (url, params, success, failure) {
    return apiAxios('DELETE', url, params, success, failure)
  },
  root
}
