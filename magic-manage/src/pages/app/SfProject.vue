<template>
  <el-container class="page-sfProject">

    <el-header class="main-form">
      <el-form :inline="true"
               :model="sfProject"
               class="demo-form-inline">
        <el-form-item>
          <el-input v-model="sfProject.keyword" placeholder="项目名称/代码"
                    style="width:180px;"
                    clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search"
                     v-if="hasPerm('sfProject:list')"
                     @click="onSubmit">查询</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="success" icon="el-icon-circle-plus-outline"
                     v-if="hasPerm('sfProject:create')"
                     @click="showCreate">新建</el-button>
        </el-form-item>
      </el-form>
    </el-header>

    <el-main class="main-table">
      <el-table :data="this.sfProjectList"
                @selection-change="handleSelectionChange"
                :highlight-current-row="true"
                v-loading="isLoading"
                border
                stripe
                size="small"
                :height="globalTableHeight"
                :maxHeight="globalTableHeight">
        <el-table-column type="selection" width="50" align="center"></el-table-column>
        <el-table-column type="index" label="序号" fixed :index=this.gIndex+1 align="center"></el-table-column>
        <el-table-column label="项目名称" prop="projectName" width="150px" align="left" header-align="center"></el-table-column>
        <el-table-column label="项目代码" prop="projectCode" align="center" header-align="center"></el-table-column>
        <el-table-column label="负责人" prop="principalName" align="center" header-align="center"></el-table-column>
        <el-table-column label="访问代码" prop="accessCode" align="left" header-align="center"></el-table-column>
        <el-table-column label="校验码" prop="checkword" align="left" header-align="center"></el-table-column>
        <el-table-column label="月结账号" prop="custid" align="left" header-align="center"></el-table-column>
        <el-table-column label="支付方式" prop="payMethod" width="100px" align="center" header-align="center">
          <template slot-scope="scope">
            <el-tag v-for="method in payMethods" v-if="method.value === scope.row.payMethod" type="success">
              {{method.name}}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="快递类型" prop="expressType" width="100px" align="center" header-align="center">
          <template slot-scope="scope">
            <el-tag v-for="type in expressTypes" v-if="type.value === scope.row.expressType" type="success">
              {{type.name}}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="开始号段" prop="startNo" align="left" header-align="center"></el-table-column>
        <el-table-column label="结束号段" prop="endNo" align="left" header-align="center"></el-table-column>
        <el-table-column label="是否开启" prop="startSign" align="center" header-align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.startSign === true ? 'success' : 'danger' ">
              {{scope.row.startSign === true ? '是' : '否'}}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="备份周期（天）" prop="backupCycle" width="120px" align="canter" header-align="center"></el-table-column>
        <el-table-column label="备注" prop="remark"  width="200px" align="left" header-align="center"></el-table-column>
        <el-table-column label="操作" width="120px" align="center" fixed="right">
          <template slot-scope="scope">
            <el-tooltip content="编辑顺丰项目配置" placement="top">
              <el-button circle type="primary" size="mini" icon="el-icon-edit" v-if="hasPerm('sfProject:update')" @click="showUpdate(scope.row)"></el-button>
            </el-tooltip>
            <el-tooltip content="删除顺丰项目配置" placement="top">
              <el-button :disabled="scope.row.startSign" circle type="danger" size="mini" icon="el-icon-delete" v-if="hasPerm('sfProject:delete')" @click="deleteSfProject(scope.row)"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <div class="table-footer">
        <el-button type="danger" size="mini" icon="el-icon-delete"
                   style="margin: 0px;"
                   v-show="this.sfProjectList.length >= 0"
                   :disabled="this.selItems.length == 0"
                   v-if="hasPerm('sfProject:delete')"
                   @click="batchDeleteSfProject">批量删除</el-button>
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          background
          :current-page=this.currentPage
          :page-sizes="[25, 50, 100, 200]"
          :page-size="25"
          layout="total, sizes, prev, pager, next, jumper"
          :total="this.sfProjectListCount"
          v-show="this.sfProjectListCount >= 0">
        </el-pagination>
      </div>
    </el-main>

    <!-- 编辑窗口 -->
    <el-dialog :title="editDialogTitle" :visible.sync="isShowEditDialog" :before-close="handleClose" :close-on-click-modal="false">
      <el-scrollbar class="dialog-scroll" ref="sfProjectScroll">
        <el-form :model="sfProjectForm"
                 ref="sfProjectForm"
                 size="small"
                 label-position="right"
                 style="text-align: left"
                 label-width="130px">
          <el-form-item label="项目名称" prop="projectName" :rules="[{ required: true, message: '请输入项目名称'}]">
          <el-input v-model="sfProjectForm.projectName"></el-input>
          </el-form-item>
          <el-form-item label="项目代号" prop="projectCode" :rules="[{ required: true, message: '请输入项目代码'}]">
          <el-input :disabled="isUpdate" v-model="sfProjectForm.projectCode"></el-input>
          </el-form-item>
          <el-form-item label="负责人" prop="principal" :rules="[{required: true, message: '请选择负责人', trigger: 'change'}]">
            <el-select v-model="sfProjectForm.principal" style="width: 100%;" placeholder="请选择负责人">
              <el-option
                v-for="user in userList"
                :key="user.id"
                :label="user.userName"
                :value="user.id">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="接入编码" prop="accessCode" :rules="[{ required: true, message: '请输入接入编码'}]">
          <el-input v-model="sfProjectForm.accessCode"></el-input>
          </el-form-item>
          <el-form-item label="客户校验码" prop="checkword" :rules="[{ required: true, message: '请输入客户校验码'}]">
          <el-input v-model="sfProjectForm.checkword"></el-input>
          </el-form-item>
          <el-form-item label="月结账号" prop="custid" :rules="[{ required: true, message: '请输入月结账号'}]">
          <el-input v-model="sfProjectForm.custid"></el-input>
          </el-form-item>
          <el-form-item label="付费方式" prop="payMethod" :rules="[{required: true, message: '请选择付费方式', trigger: 'change'}]">
            <el-select v-model="sfProjectForm.payMethod" style="width: 100%;" placeholder="请选择付费方式">
              <el-option
                v-for="method in payMethods"
                :key="method.value"
                :label="method.name"
                :value="method.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="快递类型" prop="expressType" :rules="[{required: true, message: '请选择快递类型', trigger: 'change'}]">
            <el-select v-model="sfProjectForm.expressType" style="width: 100%;" placeholder="请选择快递类型">
              <el-option
                v-for="type in expressTypes"
                :key="type.value"
                :label="type.name"
                :value="type.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="起始号段" prop="startNo" :rules="[{ required: false, message: '请输入起始号段'}]">
          <el-input v-model="sfProjectForm.startNo"></el-input>
          </el-form-item>
          <el-form-item label="结束号段" prop="endNo" :rules="[{ required: false, message: '请输入结束号段'}]">
          <el-input v-model="sfProjectForm.endNo"></el-input>
          </el-form-item>
          <el-form-item label="是否开启" prop="startSign" :rules="[{ required: true, message: '请选择是否开启'}]">
            <el-switch v-model="sfProjectForm.startSign"></el-switch>
          </el-form-item>

          <el-form-item label="备份周期" prop="backupCycle" :rules="[{ required: true, message: '请输入备份周期'}]">
            <el-input v-model="sfProjectForm.backupCycle" placeholder="正整数" @keyup.native="checkBackupCycleInput">
              <template slot="append">天</template>
            </el-input>
          </el-form-item>
          <el-form-item label="备注" prop="remark" :rules="[{ required: false, message: '请输入remark'}]">
          <el-input v-model="sfProjectForm.remark"></el-input>
          </el-form-item>
        </el-form>
      </el-scrollbar>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeEditDialog()">取 消</el-button>
        <el-button type="primary" :loading="isSaving" @click="saveSfProject">确 定</el-button>
      </div>
    </el-dialog>

  </el-container>
</template>
<script>
  let _sfProjectForm = {}
  export default {
    name: 'sfProject',
    data() {
      return {
        globalTableHeight: this.getGlobalTableHeight(),
        currentUser:JSON.parse(this.getUser()),
        userList: [],
        sfProject: {},
        payMethods: [
          {name: '寄方付', value: '1'},{name: '收方付', value: '2'},{name: '第三方付', value: '3'}
        ],
        expressTypes: [
          {name: '顺丰次日', value: '1'},{name: '顺丰隔日', value: '2'},{name: '电商特惠', value: '3'},{name: '顺丰次晨', value: '5'},{name: '顺丰即日', value: '6'}
        ],
        sfProjectForm: {
		    	id: '',
		    	projectName: '',
		    	projectCode: '',
		    	principal: '',
		    	accessCode: '',
		    	checkword: '',
		    	custid: '',
		    	payMethod: '',
		    	expressType: '',
		    	startNo: '',
		    	endNo: '',
		    	startSign: '',
		    	backupCycle: '',
		    	remark: '',
        },

        selItems: [],
        editDialogTitle: '',
        isLoading: false,
        isShowEditDialog: false,
        isSaving: false,
        sfProjectList: [],
        isUpdate: false,

        sfProjectListCount: 0,
        gIndex: 0,
        currentPage: 1,
        pageSize: 25
      }
    },
    mounted: function () {
      this.querySfProject()
      this.initAllUserData()
      _sfProjectForm = Object.assign({}, this.sfProjectForm)
      window.onresize = () => {
        this.reSetGlobalHeightParams()
        this.globalTableHeight = this.getGlobalTableHeight()
        if (this.$refs.sfProjectScroll) {
          this.$refs.sfProjectScroll.update()
        }
      }
    },
    methods: {
      onSubmit() {
        this.querySfProject()
      },

      handleSelectionChange(rowVal) {
        this.selItems = rowVal
      },

      checkBackupCycleInput() {
        this.sfProjectForm.backupCycle = this.sfProjectForm.backupCycle.replace(/[^\.\d]/g,'')
        this.sfProjectForm.backupCycle =  this.sfProjectForm.backupCycle.replace('.','')
        if (this.sfProjectForm.backupCycle.substring(0, 1).indexOf('0') > -1) {
          this.sfProjectForm.backupCycle =  ''
        }
      },

      initAllUserData() {
        this.$ajax.get(
          '/allUsers',
          {},
          vo => {

            // 管理员可以查询所有负责人
            if (this.currentUser.account === 'admin') {
              this.userList = vo.dataList
            } else {
              // 本人只能选择自己的账号
              this.userList.push(this.currentUser)
            }

          },
          vo => {
            this.userList = []
          }
        )
      },

      querySfProject() {
        this.sfProject.start = this.gIndex
        this.sfProject.limit = this.pageSize
        this.sfProject.page = this.currentPage
        if (this.currentUser.account !== 'admin') {
          this.sfProject.principal = this.currentUser.id
        }
        this.isLoading = true
        this.$ajax.post(
          '/sfProjects',
          this.sfProject,
          vo => {
            this.isLoading = false
            this.sfProjectList = vo.dataList
            this.sfProjectListCount = vo.totalProperty
          },
          vo => {
            this.isLoading = false
            this.sfProjectList = []
            this.$message.error('查询失败，请重试！')
          }
        );
      },

      showCreate() {
        this.isUpdate = false
        this.editDialogTitle = '新建顺丰项目配置'
        // 创建窗口还原Form副本
        this.sfProjectForm = _sfProjectForm
        this.clearSfProjectForm()
        this.showEditDialog()
        if (this.$refs['sfProjectForm']) {
          this.$refs['sfProjectForm'].clearValidate()
        }
      },

      showUpdate(row) {
        this.isUpdate = true
        this.editDialogTitle = '更新顺丰项目配置'
//        for (var fld in this.sfProjectForm) {
//          this.sfProjectForm[fld] = row[fld]
//        }
        this.sfProjectForm = Object.assign({}, row)
        this.showEditDialog()
      },

      showEditDialog() {
        this.isShowEditDialog = true
        this.isSaving = false
      },

      handleClose(){
        this.closeEditDialog()
      },

      closeEditDialog() {
        this.clearSfProjectForm()
        if (this.$refs['sfProjectForm']) {
          this.$refs['sfProjectForm'].clearValidate()
        }
        this.isShowEditDialog = false
      },

      clearSfProjectForm() {
        for (var fld in this.sfProjectForm) {
          let ot = Object.prototype.toString.call(this.sfProjectForm[fld]).slice(8, -1)
          this.sfProjectForm[fld] = ot === 'String' ? '' : ot === 'Object' ? {} : ot === 'Array' ? [] : ''
        }
        this.sfProjectForm.startSign = true
      },

      saveSfProject() {
        this.$refs['sfProjectForm'].validate((valid) => {
          if (valid) {
            this.isSaving = true
            if (this.sfProjectForm.id === '') {
              this.createSfProject()
            } else {
              this.updateSfProject()
            }
          } else {
            return false
          }
        });
      },

      createSfProject() {
        this.sfProjectForm.projectCode = this.sfProjectForm.projectCode.toUpperCase()
        this.$ajax.post(
          '/sfProject',
          this.sfProjectForm,
          vo => {
            this.isSaving = false
            this.$message.success('保存成功!')
            this.closeEditDialog()
            this.querySfProject()
          },
          vo => {
            this.isLoading = false
            this.isSaving = false
            this.$message.error(vo.message)
          }
        )
      },

      updateSfProject() {
        this.$ajax.put(
          '/sfProject',
          this.sfProjectForm,
          vo => {
            this.isSaving = false
            this.$message.success('保存成功!')
            this.closeEditDialog()
            this.querySfProject()
          },
          vo => {
            this.isLoading = false
            this.isSaving = false
            this.$message.error(vo.message)
          }
        )
      },

      deleteSfProject(row) {
        this.$confirm('确定删除顺丰项目配置[' + row.projectName + ']?', '提示', {
          confirmButtonText: '确定',
          type: 'warning'
        }).then(() => {
          this.$ajax.delete(
            '/sfProject/' + row.projectCode,
            null,
            vo => {
              if (vo.success) {
                this.$message.success('删除成功！')
                this.querySfProject()
              }
            },
            vo => {
              this.$message.error('删除失败，请重试！')
            }
          )
        })
      },

      batchDeleteSfProject() {
        this.$confirm('确定批量删除选中的顺丰项目配置?', '提示', {
          confirmButtonText: '确定',
          type: 'warning'
        }).then(() => {
          let ids = []
          for(let i = 0, length = this.selItems.length; i < length; i ++){
            ids.push(this.selItems[i].id)
          }
          this.$ajax.post(
            '/sfProjects/batch',
            ids,
            vo => {
              if (vo.success) {
                this.$message.success('删除成功！')
                this.querySfProject()
              }
            },
            vo => {
              this.$message.error('删除失败，请重试！')
            }
          )
        })
      },

      handleSizeChange(pageSize) {
        this.pageSize = pageSize
        this.gIndex = this.currentPage > 1 ? ((this.currentPage - 1) * this.pageSize) : 0
        this.querySfProject()
      },

      handleCurrentChange(currentPage) {
        this.currentPage = currentPage
        this.gIndex = this.currentPage > 1 ? ((this.currentPage - 1) * this.pageSize) : 0
        this.querySfProject()
      }
    }
  }
</script>
<style lang="less">
  @import "../../less/app/_sfProject.less";
</style>
