<template>
  <el-container class="page-user">

    <el-header class="main-form">
      <el-form :inline="true" class="demo-form-inline">
        <el-form-item>
          <el-input placeholder="用户姓名/账号/邮箱" style="width: 350px;" clearable v-model="keyword"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" v-if="hasPerm('user:list')" @click="getUserList">查询</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="success" icon="el-icon-refresh" v-if="hasPerm('user:sync')" @click="showSyncDialog">同步域用户</el-button>
        </el-form-item>
      </el-form>
    </el-header>

    <el-main class="main-table">
      <el-table
        :data="userList"
        @selection-change="handleSelectionChange"
        :highlight-current-row="true"
        v-loading="isLoading"
        size="small"
        border
        stripe
        :height="globalTableHeight"
        :maxHeight="globalTableHeight">
        <el-table-column type="selection" width="50" align="center"></el-table-column>
        <el-table-column type="index" label="序号" align="center"></el-table-column>
        <el-table-column label="用户姓名" prop="userName" align="left" header-align="center"></el-table-column>
        <el-table-column label="用户账号" prop="account" align="left" header-align="center"></el-table-column>
        <el-table-column label="邮箱" prop="email" width="250px" align="left" header-align="center"></el-table-column>
        <el-table-column label="部门" prop="remark" align="left" header-align="center"></el-table-column>
        <el-table-column label="操作" width="180px" align="center">
          <template slot-scope="scope">
            <el-tooltip content="编辑用户" placement="top">
              <el-button circle size="mini" type="primary" icon="el-icon-edit" v-if="hasPerm('user:update')" @click="showEditDialog(scope.row)"></el-button>
            </el-tooltip>
            <el-tooltip content="重置密码" placement="top">
              <el-button circle size="mini" type="warning" v-if="hasPerm('user:reset')" @click="resetPassword(scope.row)"><i class="fa fa-key" aria-hidden="true"></i></el-button>
            </el-tooltip>
            <el-tooltip content="删除用户" placement="top">
              <el-button circle size="mini" type="danger" icon="el-icon-delete" v-if="hasPerm('user:delete')" @click="deleteUser(scope.row)"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>
      <div class="table-footer">
        <el-button type="danger"
                   size="mini"
                   icon="el-icon-delete"
                   v-show="this.userList.length>=0"
                   :disabled="this.selItems.length==0"
                   v-if="hasPerm('user:delete')"
                   @click="deleteUserList">批量删除
        </el-button>
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          background
          :current-page="1"
          :page-sizes="[25, 50, 100, 200]"
          :page-size="25"
          layout="total, sizes, prev, pager, next, jumper"
          :total="this.userListCount"
          v-show="this.userListCount >= 0">
        </el-pagination>
      </div>
    </el-main>

    <!-- 域用户同步窗口 -->
    <el-dialog
      title="查询并同步域用户"
      width="60%"
      :visible.sync="isShowSyncDialog"
      :close-on-click-modal="false"
      class="sync-dialog">
      <el-form :inline="true" :model="ldapForm" class="demo-form-inline">
        <el-form-item>
          <el-input style="width: 300px" v-model="ldapForm.keytext" placeholder="请输入用户姓名/账号/邮箱" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" v-if="hasPerm('user:sync')" @click="getLdapUserList">查询</el-button>
        </el-form-item>
      </el-form>

      <el-table ref="multipleTable"
                :data="ldapUserList"
                tooltip-effect="dark"
                style="width: 100%;overflow-x: hidden; overflow-y: hidden;"
                @selection-change=""
                v-loading="isLdapLoading">
        <el-table-column label="用户姓名" prop="userName" width="100" align="left"></el-table-column>
        <el-table-column label="用户账号" prop="account" width="150" align="left"></el-table-column>
        <el-table-column label="邮箱" prop="email" width="250" align="left"></el-table-column>
        <el-table-column label="部门" prop="remark" width="150" align="left"></el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button type="success" size="mini" v-if="hasPerm('user:sync')" @click="syncLdapUser(scope.$index)">同步</el-button>
          </template>
        </el-table-column>
      </el-table>

      <span slot="footer" class="dialog-footer">
        <el-button @click="isShowSyncDialog = false">关 闭</el-button>
      </span>
    </el-dialog>

    <!-- 添加窗口 -->
    <el-dialog
      title="编辑用户"
      :visible.sync="isShowEditDialog"
      :close-on-click-modal="false"
      width="800px">
      <el-form :model="userForm"
               ref="userForm"
               :rules="rules"
               size="small"
               label-position="right"
               style="text-align: left"
               label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="内部账号" prop="account">
              <el-input v-model="userForm.account"></el-input>
            </el-form-item>
          </el-col>
          <!--<el-col :span="12">-->
            <!--<el-form-item label="所属机构" prop="unitId">-->
              <!--<el-select v-model="userForm.unitId" placeholder="请选择" style="width: 100%">-->
                <!--<el-option v-for="unit in unitList"-->
                           <!--:key="unit.id"-->
                           <!--:label="unit.unitName">-->
                <!--</el-option>-->
              <!--</el-select>-->
            <!--</el-form-item>-->
          <!--</el-col>-->
          <el-col :span="12">
            <el-form-item label="所属部门" prop="remark">
              <el-input v-model="userForm.remark"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="用户姓名" prop="userName">
              <el-input v-model="userForm.userName"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="用户性别">
              <el-radio-group v-model="userForm.gender" style="float: left;">
                <el-radio-button label="1">男</el-radio-button>
                <el-radio-button label="0">女</el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="启用状态">
              <el-switch v-model="userForm.state"></el-switch>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="用户生日">
              <el-date-picker
                v-model="userForm.birthday"
                type="date"
                value-format="yyyy-MM-dd"
                :editable="false"
                placeholder="选择用户的出生日期"
                style="float: left;width: 100%">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="员工职位" prop="position">
              <el-input v-model="userForm.position"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="员工工号" prop="workCode">
              <el-input v-model="userForm.workCode"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="办公电话" prop="officePhone">
              <el-input v-model="userForm.officePhone"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="电子邮箱" prop="email" >
              <el-input v-model="userForm.email" type="email"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item label="分配角色">
              <el-select v-model="userForm.roleIds" style="width: 100%;" multiple placeholder="请选择" @focus="getRoleList">
                <el-option v-for="role in roles" :key="role.id" :label="role.roleName" :value="role.id">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="closeEditDialog()">取 消</el-button>
        <el-button type="primary" :loading="isSaving" @click="updateUser">确 定</el-button>
      </div>
    </el-dialog>

  </el-container>
</template>
<script>
  import bus from '@/utils/eventBus'
  import ElRow from "element-ui/packages/row/src/row";
  import ElCol from "element-ui/packages/col/src/col";
  export default {
    name: 'user',
    components: {
      ElCol,
      ElRow
    },
    data() {
      return {
        globalTableHeight: this.getGlobalTableHeight(),
        sessionUserID: JSON.parse(this.getUser()).id,

        selItems: [],             // 选中行记录
        isLoading: false,         // 数据加载等待动画
        isLdapLoading: false,     // 同步框的域用户列表加载等待动画
        isShowSyncDialog: false,  // 是否显示同步框
        isShowEditDialog: false,  // 是否显示编辑框
        isSaving: false,          // 数据提交等待
        gIndex: 0,
        currentPage: 1,
        pageSize: 25,
        userListCount: 0,
        keyword: '',
        userForm: {
          id: '',
          unitId: '',
          account: '',
          password: '',
          userName: '',
          gender: '',
          state: '',
          birthday: '',
          position: '',
          workCode: '',
          officePhone: '',
          mobilePhone: '',
          homePhone: '',
          email: '',
          otherContact: '',
          remark: '',
          roleIds: []      // 用户的角色Ids
        },
        rules: {
          officePhone: [
            {required: true, message: '请输入办公电话', trigger: 'blur'}
          ],
        },
        ldapForm: {
            keytext: '',
        },
        userList: [],
        ldapUserList:[],
        roles:[],          // 系统的角色列表

        tableHeight: document.documentElement.clientHeight - 256,
        fullHeight: document.documentElement.clientHeight
      };
    },
    methods: {
      // 选中行记录
      handleSelectionChange(rowVal) {
        this.selItems = rowVal;
      },

      // 查询用户列表
      getUserList() {
        this.isLoading = true
        var params = {
          keyword: this.keyword,
          start: this.gIndex,
          limit: this.pageSize,
          page: this.currentPage
        }
        this.$ajax.post(
          '/users',
          params,
          vo => {
            this.isLoading = false
            this.userList = vo.dataList
            this.userListCount = vo.totalProperty
          },
          vo => {
            this.isLoading = false
            this.userList = []
          }
        );
      },

      // 显示同步窗口
      showSyncDialog() {
        this.ldapForm.keytext = ''
        this.isShowSyncDialog = true
      },

      // 查询域用户
      getLdapUserList() {
        this.isLdapLoading = true
        this.$ajax.get(
          '/ldapUsers',
          {
            name: this.ldapForm.keytext
          },
          vo => {
            this.isLdapLoading = false
            this.ldapUserList = vo.dataList
          },
          vo => {
            this.isLdapLoading = false;
          }
        )
      },

      // 同步域用户
      syncLdapUser(index) {
        let ldapUser = this.ldapUserList[index]
        this.$ajax.get(
          '/isUserExist',
          {
              account: ldapUser.account
          },
          vo => {
            if (vo.data) {
              this.$confirm('账户[' + ldapUser.account + ']已存在，是否继续同步?', '提示', {
                confirmButtonText: '确定',
                showCancelButton: false,
                type: 'warning'
              }).then(() => {
                this.realSync(ldapUser)
              }).catch(() => {
                this.$message.info({center: true, message: '取消操作'})
              });
            } else {
              this.realSync(ldapUser)
            }
          },
          vo => {
            this.$message.error('操作失败，请重试！')
          }
        )
      },

      // 同步
      realSync(ldapUser) {
        this.$ajax.post(
          '/syncLdapUser',
          ldapUser,
          vo => {
            this.$message.success('同步成功！');
            this.ldapUserList = []
            this.isShowSyncDialog = false
            this.getUserList()
          },
          vo => {
            this.$message.error('操作失败，请重试！')
          }
        )
      },

      // 显示编辑窗口
      showEditDialog(row) {
        this.userForm = row
//        for (var field in this.userForm) {
//          this.userForm[field] = row[field]
//        }
        this.isShowEditDialog = true
      },

      // 关闭编辑窗口
      closeEditDialog() {
        for (var field in this.userForm) {
          this.userForm[field] = ''
        }
        this.isShowEditDialog = false
      },

      // 更新用户
      updateUser() {
        this.$refs['userForm'].validate((valid) => {
          if (valid) {
            this.$ajax.put(
              '/user',
              this.userForm,
              vo => {
                this.$message.success('保存成功！');
                this.closeEditDialog()
                this.getUserList()
              },
              vo => {
                this.isLoading = false;
                this.$message.error('操作失败，请重试！');
              }
            )
          } else {
            return false;
          }
        });
      },

      // 查询用户角色
      getRoleList () {
        this.$ajax.get(
          '/roles',
          null,
          vo => {
            this.roles = vo.dataList
          },
          vo => {
            this.$message.error('查询失败，请重试！')
          }
        )
      },

      // 重置用户密码
      resetPassword (user) {
        this.$confirm('确定重置用户[ ' + user.userName + ' ]的密码?', '提示', {
          confirmButtonText: '确定',
          type: 'warning'
        }).then(() => {
          this.$ajax.put(
            '/resetPassword/' + user.id,
            null,
            vo => {
              if (this.sessionUserID === user.id) {
                this.$message.success('用户[ ' + user.userName + ' ]密码重置成功, 请重新登录！')
                setTimeout(() => {
                  bus.$emit('logOutRequest')
                }, 1000);
              } else {
                this.$message.success('用户[ ' + user.userName + ' ]密码重置成功！')
                this.getUserList()
              }
            },
            vo => {
              this.$message.error('操作失败，请重试！')
            }
          )
        }).catch(() => {
          this.$message.info({center: true, message: '取消操作'})
        })
      },

      // 删除用户
      deleteUser(user) {
        this.$confirm('确定删除用户[ ' + user.userName + ' ]?', '提示', {
          confirmButtonText: '确定',
          type: 'warning'
        }).then(() => {
          this.$ajax.delete(
            '/user/' + user.id,
            null,
            vo => {
              this.$message.success('删除成功！')
              this.getUserList()
            },
            vo => {
              this.$message.error('操作失败，请重试！')
            }
          )
        }).catch(() => {
          this.$message.info({center: true, message: '取消操作'})
        })
      },

      // 删除用户（批量）
      deleteUserList(){
        this.$confirm('确定批量删除选中的用户?', '提示', {
          confirmButtonText: '确定',
          type: 'warning'
        }).then(() => {
          let ids = []
          for (let i = 0, length = this.selItems.length; i < length; i++) {
            ids.push(this.selItems[i].id);
          }
          this.$ajax.post(
            '/users/batch',
            ids,
            vo => {
              if (vo.success) {
                this.$message.success('删除成功！')
                this.getUserList()
              }
            },
            vo => {
              this.$message.error('操作失败，请重试！')
            }
          )
        }).catch(() => {
          this.$message.info({center: true, message: '取消操作'})
        })
      },

      handleSizeChange(pageSize) {
        this.pageSize = pageSize
        this.gIndex = this.currentPage > 1 ? ((this.currentPage - 1) * this.pageSize) : 0
        this.getUserList();
      },

      handleCurrentChange(currentPage) {
        this.currentPage = currentPage;
        this.gIndex = this.currentPage > 1 ? ((this.currentPage - 1) * this.pageSize) : 0
        this.getUserList();
      }
    },
    mounted: function() {
      this.getUserList()
      this.getRoleList()
      window.onresize = () => {
        this.reSetGlobalHeightParams();
        this.globalTableHeight = this.getGlobalTableHeight()
      }
    }
  };
</script>
<style lang="less">
  @import "../../less/framework/_user.less";
</style>
