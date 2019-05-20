<template>
  <el-container class="page-unit">

    <el-header class="header-form">
      <el-form :inline="true" :model="formInline" class="demo-form-inline">
        <el-form-item>
          <el-input placeholder="机构名称" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="onSubmit">查询</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="success" icon="el-icon-circle-plus-outline" @click="showAddDialog">新建</el-button>
        </el-form-item>
      </el-form>
    </el-header>

    <el-main class="main-table">
      <el-table
        ref="multipleTable"
        :data="userList"
        tooltip-effect="dark"
        style="width: 100%;overflow-x: hidden; overflow-y: hidden;"
        @selection-change="handleSelectionChange"
        v-loading="isLoading"
        :height="globalTableHeight"
        :maxHeight="globalTableHeight">
        <el-table-column type="selection" width="50" align="left"></el-table-column>
        <el-table-column label="用户名称" prop="userName" width="120" align="left"></el-table-column>
        <el-table-column label="用户账号" prop="account" width="120" align="left"></el-table-column>
        <el-table-column label="邮箱" prop="email" width="250" align="left"></el-table-column>
        <el-table-column label="手机" prop="mobilePhone" width="120" align="left"></el-table-column>
        <!--<el-table-column label="操作" align="left" v-if="showEdit || showDelete">-->
        <el-table-column label="操作" align="left">
          <template slot-scope="scope">
            <el-button
              size="mini"
              @click="handleEdit(scope.$index, scope.row)">编辑
            </el-button>
            <el-button
              size="mini"
              type="danger"
              @click="handleDelete(scope.$index, scope.row)">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="table-footer">
        <el-button type="danger" size="mini" style="margin: 0px;"
                   v-show="this.userList.length>0"
                   :disabled="this.selItems.length==0"
                   @click="deleteMany">批量删除
        </el-button>
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          background
          :current-page="1"
          :page-sizes="[25, 50, 100, 200]"
          :page-size="25"
          layout="total, sizes, prev, pager, next, jumper"
          :total="this.userList.length"
          v-show="this.userList.length>0">
        </el-pagination>
      </div>
    </el-main>

    <!-- 添加窗口 -->
    <el-dialog title="添加用户" :visible.sync="isShowAddDialog">
      <el-form :model="userForm" :rules="rules" ref="userForm">
        <el-form-item label="登录账号" :label-width="formLabelWidth" prop="account">
          <el-input v-model="userForm.account" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="所属机构" :label-width="formLabelWidth" prop="unitId">
          <el-select v-model="userForm.unitId" placeholder="请选择" style="width: 100%">
              <el-option v-for="unit in unitList"
                :key="unit.id"
                :label="unit.unitName">
              </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="用户名称" :label-width="formLabelWidth" prop="userName">
          <el-input v-model="userForm.userName" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="性别" :label-width="formLabelWidth">
          <el-radio-group v-model="userForm.gender" style="float: left;">
            <el-radio-button label="1">男</el-radio-button>
            <el-radio-button label="0">女</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="状态" :label-width="formLabelWidth" prop="state">
          <el-input v-model="userForm.state" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="生日" :label-width="formLabelWidth" prop="birthday">
          <el-input v-model="userForm.birthday" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="职位" :label-width="formLabelWidth" prop="position">
          <el-input v-model="userForm.position" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="员工号" :label-width="formLabelWidth" prop="workCode">
          <el-input v-model="userForm.workCode" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="办公电话" :label-width="formLabelWidth" prop="officePhone">
          <el-input v-model="userForm.officePhone" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="手机号码" :label-width="formLabelWidth" prop="mobilePhone">
          <el-input v-model="userForm.mobilePhone" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="家庭电话" :label-width="formLabelWidth" prop="homePhone">
          <el-input v-model="userForm.homePhone" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="Email" :label-width="formLabelWidth" prop="email">
          <el-input v-model="userForm.email" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="其它联系方式" :label-width="formLabelWidth" prop="otherContact">
          <el-input v-model="userForm.otherContact" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="备注" :label-width="formLabelWidth" prop="remark">
          <el-input v-model="userForm.remark" auto-complete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="isShowAddDialog = false">取 消</el-button>
        <el-button type="primary" :loading="isSaving" @click="saveUser">确 定</el-button>
      </div>
    </el-dialog>

  </el-container>
</template>
<script>
  export default {
    data() {
      return {
        globalTableHeight: this.getGlobalTableHeight(),

        activeName: 'post',
        isAdmin: false,
        selItems: [],
        isLoading: false, // 数据加载等待动画
        isShowAddDialog: false, // 是否显示创建框
        isSaving: false, // 数据提交等待
        currentPage: 1,
        totalCount: -1,
        pageSize: 6,
        userForm: {
          id: '',
          unitId: '',
          account: '',
          userName: '',
          gender: '',
          state: '',
          birthday: '',
          position: '',
          workCode: '',
          officePhonr: '',
          mobilePhone: '',
          homePhone: '',
          email: '',
          otherContact: '',
          remark: ''
        },
        userList: []
      };
    },
    mounted: function () {
      window.onresize = () => {
        this.reSetGlobalHeightParams();
        this.globalTableHeight = this.getGlobalTableHeight()
      }
    },
    methods: {
      handleClick(tab, event) {
//        console.log(tab, event);
      },
      onSubmit() {
        this.queryUser()
      },
      queryUser() {
        this.isLoading = true
        postRequest(
          '/users',
          {
            userName: null
          },
        ).then(
          resp => {
            this.isLoading = false
            if (resp.status == 200) {
              //成功
              var json = resp.data;
              if (json.success == true) {
                this.userList = json.dataList
              } else {
                // _this.$alert('登录失败!', '失败!');
              }
            } else {
              // 失败
              // _this.$alert('登录失败!', '失败!');
            }
          },
          resp => {
            this.isLoading = false;
            // _this.$alert('找不到服务器⊙﹏⊙∥!', '失败!');
          }
        );
      }, //End query user
      showAddDialog() {
        for (var fld in this.userForm) {
            // console.log(fld)
            this.userForm[fld] = ''
        }
        this.isShowAddDialog = true
      },
      saveUser() {
          postRequest(
              '/user',
              this.userForm
          ).then(
              resp => {
                if (resp.status == 200) {
                  this.$alert('提示', '保存成功!');
                } else {
                  this.$alert('提示', '操作不成功!');
                }
              },
              resp => {
                this.isLoading = false;
                // _this.$alert('找不到服务器⊙﹏⊙∥!', '失败!');
              }
          )
      }, // End save user
      deleteUser(userId, userName) {
          deleteRequest(

          ).then()
      } // End delete user

    },
    mounted: function() {
      this.queryUser()
    }
  };
</script>
<style lang="less">
  @import "../../less/framework/_unit.less";
</style>
