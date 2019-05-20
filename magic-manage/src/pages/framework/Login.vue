<template>
  <el-container class="page-login">

    <el-main class="login-container" :style="{height:globalFullHeight-200 + 'px'}">
      <el-form :rules="rules"
               :model="loginUser"
               ref="loginUser"
               class="login-form"
               label-position="left"
               label-width="0px"
               v-loading="loading">
        <h3 class="login-title">后台管理系统</h3>
        <el-form-item prop="account">
          <el-input type="text" v-model="loginUser.account" auto-complete="off" placeholder="账号"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input type="password" v-model="loginUser.password" auto-complete="off" placeholder="密码"></el-input>
        </el-form-item>
        <!--<el-form-item>
          <el-radio-group v-model="loginType">
            <el-radio label="0">本地认证</el-radio>
            <el-radio label="1">域认证</el-radio>
          </el-radio-group>
        </el-form-item>-->
        <!--<el-checkbox class="login-remember" v-model="checked" label-position="left">记住密码</el-checkbox>-->
        <!--<span class="login-tip"><i class="fa fa-info-circle fa-lg"></i>&nbsp;&nbsp;提示：普通用户请使用（域用户名+本地密码）登录</span>-->
        <el-form-item>
          <el-button class="login-button" type="primary" :loading="logining" @click.native.prevent="login">立即登录</el-button>
        </el-form-item>
      </el-form>
    </el-main>

    <el-footer class="login-footer" height="80px">
      <p>All rights reserved copyright © 2018-{{year}}</p>
      <p>珠海市幻魅魔法美容会所</p>
    </el-footer>

    <upd-pwd-dialog ref="updpwddialog"
                    v-bind:is-login="isLogin"
                    v-bind:cur-account="loginUser.account"
                    v-on:loginRequest="loginRequest">
    </upd-pwd-dialog>

  </el-container>
</template>
<script>
  import {setUser} from '../../utils/auth'
  import UpdPwdDialog from '@/components/UpdPwdDialog';

  export default {
    components: {
      "upd-pwd-dialog": UpdPwdDialog
    },
    data() {
      return {
        globalFullHeight: this.getGlobalFullHeight(),
        year: '',
        rules: {
          account: [{required: true, message: '请输入账号', trigger: 'blur'}],
          password: [{required: true, message: '请输入密码', trigger: 'blur'}]
        },
        checked: true,
        loginUser: {
          account: '',
          password: ''
        },
        // loginType: '0',
        isLogin: false,
        loading: false,
        logining: false
      }
    },
    methods: {
      // 用户登录
      login: function () {
        this.logining = true

        // 密码处理（根据认证方式采用不同的加密策略）
        var account = this.loginUser.account;

        // 全部用户使用本地认证
        var password = this.$cryptoJs.SHA256(this.loginUser.password).toString();
        this.loginRequest(account, password)
        // 校验然后登录
//        this.loginCheck(account, password);

        /*if ('admin' === account || 'auditor' === account) {
          // 本地认证：sha256摘要
          password = this.$cryptoJs.SHA256(this.loginUser.password).toString();

          // 校验然后登录
          this.loginCheck(account, password)

        } else {
          // 域认证：AES对称加密
          var secret = this.$cryptoJs.SHA256(account).toString().substring(0, 16);
          var key = this.$cryptoJs.enc.Utf8.parse(secret);
          var encrypted = this.$cryptoJs.AES.encrypt(this.loginUser.password, key, {
            mode: this.$cryptoJs.mode.ECB,
            padding: this.$cryptoJs.pad.Pkcs7
          });
          password = (encrypted.ciphertext.toString(this.$cryptoJs.enc.Base64));

          // 域认证：DES对称加密
//          var secret = this.$cryptoJs.SHA256(account).toString().substring(0, 8);
//          var key = this.$cryptoJs.enc.Utf8.parse(secret);
//          var encrypted = this.$cryptoJs.DES.encrypt(this.loginUser.password, key, {
//            mode: this.$cryptoJs.mode.ECB,
//            padding: this.$cryptoJs.pad.Pkcs7
//          });
//          password = (encrypted.ciphertext.toString(this.$cryptoJs.enc.Base64));

          // 直接登录
          this.loginRequest(account, password)
        }*/

      },

//      loginCheck(account, password) {
//        this.$ajax.post(
//          '/loginCheck',
//          {
//            account: account,
//            password: password
//          },
//          vo => {
//            this.logining = false
//            if (vo.success) {
//              if (vo.data === true) {
//                this.$message.error(vo.message)
//                this.$refs.updpwddialog.showUpdPwdDialog()
//              } else {
//                this.loginRequest(account, password)
//              }
//            }
//          },
//          vo => {
//            this.logining = false
//            this.$message.error(vo.message)
//          }
//        );
//      },

      loginRequest(account, password) {
        this.$ajax.post(
          '/login',
          {
            account: account,
            password: password
          },
          vo => {
            this.logining = false
            // 登录成功，保存用户数据到cookie
            setUser(vo.data)
            this.$message.success('登录成功，欢迎您');
            this.$router.replace({path: '/home/dashboard'});
          },
          vo => {
            this.logining = false
            this.$message.error(vo.message);
          }
        );
      }
    },
    mounted: function () {
      // 版权时间
      this.year = new Date().getFullYear();

      // 浏览器改变尺寸
      window.onresize = () => {
        this.reSetGlobalHeightParams();
        this.globalFullHeight = this.getGlobalFullHeight();
      }
    }
  }
</script>
<style lang="less">
  @import '../../less/framework/_login.less';
</style>
