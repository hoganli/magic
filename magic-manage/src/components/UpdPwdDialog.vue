<template>
  <el-dialog
    :title="updPwdDialogTitle"
    :visible.sync="isShowUpdPwd"
    :close-on-click-modal="false"
    :before-close="handleClose"
    :show-close="isLogin"
    :close-on-press-escape="isLogin">

    <el-form
      :model="updPwdForm"
      :rules="pwdRules"
      ref="updPwdForm"
      size="small"
      status-icon
      label-width="100px">
      <el-form-item label="旧密码" prop="oldPwd">
        <el-input type="password" v-model="updPwdForm.oldPwd" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="新密码" prop="newPwd">
        <el-input type="password" v-model="updPwdForm.newPwd" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="确认新密码" prop="checkNewPwd">
        <el-input type="password" v-model="updPwdForm.checkNewPwd" autocomplete="off"></el-input>
      </el-form-item>
    </el-form>
    <el-tag size="small" type="danger">Tips:</el-tag>
    <el-tag size="small">密码必须包含字母、数字、特殊字符，并且长度不少于8位；</el-tag>
    <span slot="footer" class="dialog-footer">
        <el-button v-show="isLogin" @click="closeUpdPwdDialog">取 消</el-button>
        <el-button type="primary" :loading="updPwdLoading" @click="updPwd">确 定</el-button>
      </span>
  </el-dialog>
</template>

<script>
  export default {
    name: "UpdPwdDialog",
    props: [
      'curAccount',
      'isLogin'
    ],

    data() {
      const pwdReg = /^(?=.*\d)(?=.*[a-zA-Z])(?=.*[\W_]).{8,16}$/
      const validateCheckOldPwd = (rule, value, callback) => {
        if (!value) {
          return callback(new Error('旧密码不能为空'))
        }
        if (!pwdReg.test(value)) {
          return callback(new Error('密码规则校验不通过'))
        }
        // 检查旧密码是否正确
        setTimeout(() => {
          this.checkPwd(callback)
        }, 1000);
      }

      const validateNewPwd = (rule, value, callback) => {
        if (!value) {
          return callback(new Error('请输入新密码'))
        }
        if (!pwdReg.test(value)) {
          return callback(new Error('密码规则校验不通过'))
        }
        if (value === this.updPwdForm.oldPwd) {
          return callback(new Error('新密码跟旧密码不能相同'))
        }
        if (this.updPwdForm.newPwd !== '') {
          this.$refs['updPwdForm'].validateField('checkNewPwd')
        }
        callback();
      }

      const validateCheckNewPwd = (rule, value, callback) => {
        if (value === '') {
          return callback(new Error('请再次输入新密码'))
        }
        if (!pwdReg.test(value)) {
          return callback(new Error('密码规则校验不通过'))
        }
        if (value !== this.updPwdForm.newPwd) {
          return callback(new Error('两次输入的新密码不一致'))
        }
        callback()
      }

      return {
        updPwdForm: {
          oldPwd: '',
          newPwd: '',
          checkNewPwd: ''
        },
        updPwdDialogTitle: '修改密码',
        isShowUpdPwd: false,
        updPwdLoading: false,
        pwdRules: {
          newPwd: [
            { validator: validateNewPwd, trigger: 'blur' }
          ],
          checkNewPwd: [
            { validator: validateCheckNewPwd, trigger: 'blur' }
          ],
          oldPwd: [
            { validator: validateCheckOldPwd, trigger: 'blur'}
          ]
        }
      }
    },

    methods: {
      // updPwd修改密码方法
      updPwd() {
        this.$refs['updPwdForm'].validate((valid) => {
          if (valid) {
            this.updPwdLoading = true
            let account = this.curAccount
            let isLogin = this.isLogin
            let password = this.$cryptoJs.SHA256(this.updPwdForm.newPwd).toString()
            let oldPassword = this.$cryptoJs.SHA256(this.updPwdForm.oldPwd).toString()
            this.$ajax.post(
              '/updatePassword',
              {
                account: account,
                password: password,
                oldPassword: oldPassword
              },
              vo => {
                this.updPwdLoading = false
                this.closeUpdPwdDialog()
                // 回调父组件方法
                  if (isLogin) {
                    this.$message.success('修改成功,请重新登录!')
                    setTimeout(() => {
                      this.$emit('logOutRequest')
                    }, 1000);
                  } else {
                    this.$message.success('修改成功,正在登录...')
                    this.$emit('loginRequest', account, password)
                  }
              },
              vo => {
                this.updPwdLoading = false
                this.$message.error(vo.message)
              }
            );
          } else {
            return false
          }
        });
      },

      checkPwd(callback) {
        let account = this.curAccount
        let password = this.$cryptoJs.SHA256(this.updPwdForm.oldPwd).toString()
        this.$ajax.post(
          '/checkPassword',
          {
            account: account,
            password: password
          },
          vo => {
            if (vo.success) {
              callback();
            } else {
              return callback(new Error(vo.message))
            }
          },
          vo => {
            return callback(new Error(vo.message))
          }
        );
      },

      showUpdPwdDialog() {
        for (var field in this.updPwdForm) {
          this.updPwdForm[field] = ''
        }
        if (this.$refs['updPwdForm']) {
          this.$refs['updPwdForm'].clearValidate()
        }
        this.isShowUpdPwd = true
        this.updPwdLoading = false
      },

      handleClose() {
        this.closeUpdPwdDialog()
      },

      closeUpdPwdDialog() {
        for (var field in this.updPwdForm) {
          this.updPwdForm[field] = ''
        }
        if (this.$refs['updPwdForm']) {
          this.$refs['updPwdForm'].clearValidate()
        }
        this.isShowUpdPwd = false
        this.updPwdLoading = false
      }
    }
  };
</script>
