<template>
  <el-container class="role-page">

    <el-header class="main-form">
      <el-form :inline="true" :model="searchForm" class="demo-form-inline">
        <el-form-item>
          <el-input v-model="searchForm.roleName" placeholder="角色名称" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="getRoleList">查询</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="success" icon="el-icon-circle-plus-outline" v-if="hasPerm('role:add')" @click="showAdd">新建</el-button>
        </el-form-item>
      </el-form>
    </el-header>

    <el-main class="main-table">
      <el-table
        :data="roles"
        v-loading="loading"
        @selection-change="handleSelectionChange"
        :highlight-current-row="true"
        size="small"
        border
        stripe
        :height="globalTableHeight"
        :maxHeight="globalTableHeight">
        <el-table-column type="selection" width="50" align="center"></el-table-column>
        <el-table-column type="index" label="序号" align="center"></el-table-column>
        <el-table-column prop="roleName" label="角色名称" align="left" header-align="center"></el-table-column>
        <el-table-column prop="actGroup" label="工作流组" align="left" header-align="center"></el-table-column>
        <el-table-column prop="remark" label="备注" align="left" header-align="center"></el-table-column>
        <el-table-column label="操作" align="center" width="180">
          <template slot-scope="scope">
            <el-tooltip content="编辑角色" placement="top">
            <el-button circle size="mini" icon="el-icon-edit" type="primary" v-if="hasPerm('role:update')" @click="showUpdate(scope.row)"></el-button>
            </el-tooltip>
            <el-tooltip content="授权权限" placement="top">
            <el-button circle size="mini" icon="el-icon-share" type="info" v-if="hasPerm('role:update')" @click="showGrant(scope.row)"></el-button>
            </el-tooltip>
            <el-tooltip content="删除角色" placement="top">
            <el-button circle size="mini" icon="el-icon-delete" type="danger" v-if="hasPerm('role:delete')" @click="deleteRole(scope.row)"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>
      <div class="table-footer">
        <el-button type="danger"
                   size="mini"
                   icon="el-icon-delete"
                   v-show="this.roles.length >= 0"
                   :disabled="this.selItems.length == 0"
                   v-if="hasPerm('role:delete')"
                   @click="deleteRoleList">批量删除
        </el-button>
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          background
          :current-page=this.currentPage
          :page-sizes="[25, 50, 100, 200]"
          :page-size="25"
          layout="total, sizes, prev, pager, next, jumper"
          :total="this.roles.length"
          v-show="this.roles.length >= 0">
        </el-pagination>
      </div>
    </el-main>

    <!-- 编辑窗口 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="isShowEditDialog"
      :close-on-click-modal="false"
      :before-close="handleClose">
      <el-form
        :model="roleForm"
        :rules="rules"
        ref="roleForm"
        size="small"
        label-width="100px">
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="roleForm.roleName"></el-input>
        </el-form-item>
        <el-form-item label="工作流组">
          <el-input v-model="roleForm.actGroup"></el-input>
        </el-form-item>
        <el-form-item label="备注">
          <el-input type="textarea" v-model="roleForm.remark"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeEditDialog">取 消</el-button>
        <el-button type="primary" :loading="adding" @click="onSave">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 授权窗口 -->
    <el-dialog
      class="grant-dialog"
      :title="dialogTitle"
      width="750px"
      :visible.sync="showGrantDialog"
      :close-on-click-modal="false">
      <el-scrollbar class="dialog-scroll" ref="roleScroll">
      <div class="tips">说明：红色权限为对应菜单内的必选权限</div>
        <el-form :model="permissionForm" :rules="rules" ref="permissionForm">
          <el-form-item>
            <div v-for="(menu, index) in menus" class="checkbox-group-div">
              <span class="inline-block" style="width:120px">
                <el-button @click="checkAll(index)" :type="isNoneChecked(index)?'':(isAllChecked(index)?'success':'primary')" size="mini" style="width:110px;">{{menu.menuName}}</el-button>
              </span>
              <div class="inline-block">
                <el-checkbox-group v-model="permissionForm.permissionIds">
                  <el-checkbox v-for="(p, pIndex) in menu.permissions" :label="p.id" @change="checkPermission(p, index, $event)" :key="p.id">
                    <span :style="p.isNecessary?'color:red':''">{{p.permissionName}}</span>
                  </el-checkbox>
                </el-checkbox-group>
              </div>
            </div>
          </el-form-item>
        </el-form>
      </el-scrollbar>
      <span slot="footer" class="dialog-footer">
        <el-button @click="showGrantDialog = false">取 消</el-button>
        <el-button type="primary" :loading="adding" @click="grant">确 定</el-button>
      </span>
    </el-dialog>

  </el-container>
</template>
<script>
  export default {
    name: 'role',
    data() {
      return {
        globalTableHeight: this.getGlobalTableHeight(),

        selItems: [],             // 选中行记录
        loading: false,
        isShowEditDialog: false,
        showGrantDialog: false,
        dialogTitle: '',
        adding: false,
        searchForm: {
          roleName: ''
        },
        roleForm: {
          id: '',
          roleName: '',
          actGroup: '',
          remark: '',
          createDate: '',
          createBy: '',
          permissionIds: []
        },
        permissionForm: {
          id: '',
          permissionIds: []
        },
        roles: [],
        menus: [],
        rules: {
          roleName: [
            {required: true, message: '请输入角色名称', trigger: 'blur'},
            {min: 2, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur'}
          ]
        },

        roleListCount: 0,
        gIndex: 0,
        currentPage: 1,
        pageSize: 25
      }
    },
    methods: {
      handleSizeChange(pageSize) {
        this.pageSize = pageSize
        this.gIndex = this.currentPage > 1 ? ((this.currentPage - 1) * this.pageSize) : 0
        this.getRoleList()
      },

      handleCurrentChange(currentPage) {
        this.currentPage = currentPage
        this.gIndex = this.currentPage > 1 ? ((this.currentPage - 1) * this.pageSize) : 0
        this.getRoleList()
      },

      // 选中行记录
      handleSelectionChange(rowVal) {
        this.selItems = rowVal;
      },

      // 查询角色列表
      getRoleList () {
        this.searchForm.start = this.gIndex
        this.searchForm.limit = this.pageSize
        this.searchForm.page = this.currentPage
        this.loading = true
        this.$ajax.post(
          '/roles/queries',
          this.searchForm,
          vo => {
            this.loading = false
            this.roles = vo.dataList
            this.roleListCount = vo.totalProperty
          },
          vo => {
            this.loading = false
            this.$message.error('操作失败，请重试！')
          }
        )
      },

      // 显示新增窗口
      showAdd () {
        this.dialogTitle = '新增角色'
        this.clearRoleForm()
        this.showEditDialog()
        if (this.$refs['roleForm']) {
          this.$refs['roleForm'].clearValidate()
        }
      },

      // 显示编辑窗口
      showUpdate (row) {
        this.dialogTitle = '编辑角色'
        for (var field in this.roleForm) {
          this.roleForm[field] = row[field]
        }
        this.showEditDialog()
      },

      showEditDialog() {
        this.isShowEditDialog = true
        this.adding = false
      },

      clearRoleForm() {
        for (var fld in this.roleForm) {
          let ot = Object.prototype.toString.call(this.roleForm[fld]).slice(8,-1)
          this.roleForm[fld] = ot === 'String' ? '' : ot === 'Object' ? {} : ot === 'Array' ? [] : ''
        }
      },

      // 关闭新增编辑窗口
      closeEditDialog () {
        this.clearRoleForm()
        if (this.$refs['roleForm']) {
          this.$refs['roleForm'].clearValidate()
        }
        this.isShowEditDialog = false
      },

      handleClose() {
        this.closeEditDialog()
      },

      // 显示授权窗口
      showGrant (row) {
        // 先清空已选权限列表
        this.permissionForm.permissionIds = []

        // 显示窗口
        this.showGrantDialog = true
        this.dialogTitle = '角色授权'
        this.permissionForm.id=row.id

        // 查询已有权限
        this.$ajax.get(
          '/role/' + row.id + '/permissions',
          null,
          vo => {
            this.permissionForm.permissionIds = vo.dataList
          },
          vo => {
            this.$message.error('操作失败，请重试！')
          }
        )
      },

      // 保存角色
      onSave() {
        this.$refs['roleForm'].validate((valid) => {
          if (valid) {
            this.adding = true
            if (this.roleForm.id === '') {
              this.createRole()
            } else {
              this.updateRole()
            }
            this.adding = false
          } else {
            return false
          }
        });
      },

      // 新增角色
      createRole() {
        this.$ajax.post(
          '/role',
          this.roleForm,
          vo => {
            this.$message.success('保存成功！');
            this.closeEditDialog()
            this.getRoleList();
          },
          vo => {
            this.$message.error('操作失败，请重试！');
          }
        )
      },

      // 更新角色
      updateRole() {
        this.$ajax.put(
          '/role',
          this.roleForm,
          vo => {
            this.$message.success('保存成功!');
            this.closeEditDialog()
            this.getRoleList();
          },
          vo => {
            this.$message.error('操作失败，请重试！');
          }
        )
      },

      // 删除角色
      deleteRole(row) {
        this.$confirm('确定删除此记录?', '提示', {
          confirmButtonText: '确定',
          type: 'warning'
        }).then(() => {
          let id = row.id
          this.$ajax.delete(
            '/role',
            {id: id},
            vo => {
              this.$message.success('删除成功!');
              this.getRoleList()
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
      deleteRoleList(){
        this.$confirm('确定批量删除选中的用户?', '提示', {
          confirmButtonText: '确定',
          type: 'warning'
        }).then(() => {
          let ids = []
          for (let i = 0, length = this.selItems.length; i < length; i++) {
            ids.push(this.selItems[i].id);
          }
          this.$ajax.post(
            '/roles/batch',
            ids,
            vo => {
              if (vo.success) {
                this.$message.success('删除成功！')
                this.getRoleList()
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

      // 保存权限
      grant() {
        this.$ajax.put(
          '/rolePermission',
          this.permissionForm,
          vo => {
            this.$message.success('保存成功!');
            this.showGrantDialog = false;
            this.getRoleList();
          },
          vo => {
            this.adding = false;
            this.$message.error('操作失败，请重试!');
          }
        )
      },


      // 查询菜单及菜单包含的权限
      getMenuList () {
        this.$ajax.get(
          '/menus',
          null,
          vo => {
            this.menus = vo.dataList
          },
          vo => {
            this.$message.error('操作失败，请重试！')
          }
        )
      },

      // 选择权限
      checkPermission(p, index, event) {
        let permId = p.id
        this.checkNecessary(index)

        if (!event && p.isNecessary) {
          this.checkNonePermissions(index)
        }
      },

      // 选中必选权限
      checkNecessary(index) {
        let permissions = this.menus[index].permissions
        for (let j = 0; j < permissions.length; j++) {
          let permission = permissions[j]
          if (permission.isNecessary) {
            this.addUnique(permission.id, this.permissionForm.permissionIds)
          }
        }
      },

      // 将选中权限添加到列表（去重）
      addUnique(value, array) {
        //数组内防重复地添加元素
        let index = array.indexOf(value)
        if (index < 0) {
          array.push(value)
        }
      },

      // 判断菜单内权限是否一个都没选
      isNoneChecked(index) {
        let permissions = this.menus[index].permissions
        let result = true
        for (let j = 0; j < permissions.length; j++) {
          if (this.permissionForm.permissionIds.indexOf(permissions[j].id) > -1) {
            result = false
            break
          }
        }
        return result
      },

      // 判断菜单内的权限是否全选
      isAllChecked(index) {
        let permissions = this.menus[index].permissions
        let result = true
        for (let j = 0; j < permissions.length; j++) {
          if (this.permissionForm.permissionIds.indexOf(permissions[j].id) < 0) {
            result = false
            break
          }
        }
        return result
      },

      // 点击菜单（全选/全取消）
      checkAll(index) {
        let v = this
        if (v.isAllChecked(index)) {
          // 如果已经全选了,则全部取消
          v.checkNonePermissions(index)
        } else {
          // 如果尚未全选,则全选
          v.checkAllPermissions(index)
        }
      },

      // 选中菜单包含的所有权限
      checkAllPermissions(index) {
        let permissions = this.menus[index].permissions
        for (let i = 0; i < permissions.length; i++) {
          this.addUnique(permissions[i].id, this.permissionForm.permissionIds)
        }
      },

      // 取消选中菜单包含的所有权限
      checkNonePermissions(index) {
        let permissions = this.menus[index].permissions
        for (let j = 0; j < permissions.length; j++) {
          let idIndex = this.permissionForm.permissionIds.indexOf(permissions[j].id)
          if (idIndex > -1) {
            this.permissionForm.permissionIds.splice(idIndex, 1)
          }
        }
      },
    },
    mounted () {
      this.getRoleList()
      this.getMenuList()
      window.onresize = () => {
        this.reSetGlobalHeightParams();
        this.globalTableHeight = this.getGlobalTableHeight();
        if (this.$refs.roleScroll) {
          this.$refs.roleScroll.update();
        }
      }
    }
  }
</script>
<style lang="less">
  @import '../../less/framework/_role.less';
</style>
