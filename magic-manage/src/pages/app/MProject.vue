<template>
  <el-container class="page-mProject">

    <el-header class="main-form">
      <el-form :inline="true"
               :model="mProject"
               class="demo-form-inline">
        <el-form-item>
          <el-input v-model="mProject.id" placeholder="projectID"
                    style="width:200px;"
                    clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search"
                     v-if="hasPerm('mProject:list')"
                     @click="onSubmit">查询</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="success" icon="el-icon-circle-plus-outline"
                     v-if="hasPerm('mProject:create')"
                     @click="showCreate">新建</el-button>
        </el-form-item>
      </el-form>
    </el-header>

    <el-main class="main-table">
      <el-table :data="this.mProjectList"
                @selection-change="handleSelectionChange"
                :highlight-current-row="true"
                v-loading="isLoading"
                border
                stripe
                size="small"
                :height="globalTableHeight"
                :maxHeight="globalTableHeight">
        <!--<el-table-column type="selection" width="50" align="center"></el-table-column>-->
        <el-table-column type="index" label="序号" fixed :index=this.gIndex+1 align="center"></el-table-column>
        <el-table-column label="项目名称" prop="projectName" align="center" header-align="center"></el-table-column>
        <el-table-column label="类型" prop="type" align="center" header-align="center"></el-table-column>
        <el-table-column label="服务时长（分）" prop="period" align="center" header-align="center"></el-table-column>
        <el-table-column label="价格" prop="price" align="center" header-align="center"></el-table-column>
        <el-table-column label="优惠价" prop="disPrice" align="center" header-align="center"></el-table-column>
        <el-table-column label="是否启用" prop="used" align="center" header-align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.used === true ? 'success' : 'danger' ">
              {{scope.row.used === true ? '是' : '否'}}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120px" align="center" fixed="right">
          <template slot-scope="scope">
            <el-tooltip content="编辑" placement="top">
              <el-button circle type="primary" size="mini" icon="el-icon-edit" v-if="hasPerm('mProject:update')" @click="showUpdate(scope.row)"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button circle type="danger" size="mini" icon="el-icon-delete" v-if="hasPerm('mProject:delete')" @click="deleteMProject(scope.row)"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <div class="table-footer">
        <el-button type="danger" size="mini" icon="el-icon-delete"
                   style="margin: 0px;"
                   v-show="this.mProjectList.length >= 0"
                   :disabled="this.selItems.length == 0"
                   v-if="hasPerm('mProject:delete')"
                   @click="batchDeleteMProject">批量删除</el-button>
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          background
          :current-page=this.currentPage
          :page-sizes="[25, 50, 100, 200]"
          :page-size="25"
          layout="total, sizes, prev, pager, next, jumper"
          :total="this.mProjectListCount"
          v-show="this.mProjectListCount >= 0">
        </el-pagination>
      </div>
    </el-main>

    <!-- 编辑窗口 -->
    <el-dialog :title="editDialogTitle" :visible.sync="isShowEditDialog" :before-close="handleClose" :close-on-click-modal="false">
      <el-scrollbar class="dialog-scroll" ref="mProjectScroll">
        <el-form :model="mProjectForm"
                 ref="mProjectForm"
                 size="small"
                 label-position="right"
                 style="text-align: left"
                 label-width="130px">
          <el-form-item label="项目名称" prop="projectName" :rules="[{ required: true, message: '请输入项目名称'}]">
          <el-input v-model="mProjectForm.projectName"></el-input>
          </el-form-item>
          <el-form-item label="类型" prop="type" :rules="[{ required: true, message: '请选择类型'}]">
            <el-select v-model="mProjectForm.type" placeholder="请选择">
              <el-option label="面部" value="面部"></el-option>
              <el-option label="身体" value="身体"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="服务时长（分）" prop="period" :rules="[{ required: true, message: '请输入服务时长'}]">
            <el-input v-model="mProjectForm.period"></el-input>
          </el-form-item>
          <el-form-item label="价格" prop="price" :rules="[{ required: true, message: '请输入价格'}]">
          <el-input v-model="mProjectForm.price"></el-input>
          </el-form-item>
          <el-form-item label="优惠价" prop="disPrice">
          <el-input v-model="mProjectForm.disPrice"></el-input>
          </el-form-item>
          <el-form-item label="是否启用" prop="used">
            <el-switch v-model="mProjectForm.used"></el-switch>
          </el-form-item>
        </el-form>
      </el-scrollbar>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeEditDialog()">取 消</el-button>
        <el-button type="primary" :loading="isSaving" @click="saveMProject">确 定</el-button>
      </div>
    </el-dialog>

  </el-container>
</template>
<script>
  let _mProjectForm = {}
  export default {
    name: 'mProject',
    data() {
      return {
        globalTableHeight: this.getGlobalTableHeight(),

        mProject: {},
        mProjectForm: {
		    	id: '',
		    	projectName: '',
		    	type: '',
		    	period: '',
		    	price: '',
		    	disPrice: '',
		    	used: '',
        },

        selItems: [],
        editDialogTitle: '',
        isLoading: false,
        isShowEditDialog: false,
        isSaving: false,
        mProjectList: [],

        mProjectListCount: 0,
        gIndex: 0,
        currentPage: 1,
        pageSize: 25
      }
    },
    mounted: function () {
      this.queryMProject()
      _mProjectForm = Object.assign({}, this.mProjectForm)
      window.onresize = () => {
        this.reSetGlobalHeightParams()
        this.globalTableHeight = this.getGlobalTableHeight()
        if (this.$refs.mProjectScroll) {
          this.$refs.mProjectScroll.update()
        }
      }
    },
    methods: {
      onSubmit() {
        this.queryMProject()
      },

      handleSelectionChange(rowVal) {
        this.selItems = rowVal
      },

      queryMProject() {
        this.mProject.start = this.gIndex
        this.mProject.limit = this.pageSize
        this.mProject.page = this.currentPage
        this.isLoading = true
        this.$ajax.post(
          '/mProjects',
          this.mProject,
          vo => {
            this.isLoading = false
            this.mProjectList = vo.dataList
            this.mProjectListCount = vo.totalProperty
          },
          vo => {
            this.isLoading = false
            this.mProjectList = []
            this.$message.error('查询失败，请重试！')
          }
        );
      },

      showCreate() {
        this.editDialogTitle = '新建项目'
        // 创建窗口还原Form副本
        this.mProjectForm = _mProjectForm
        this.clearMProjectForm()
        this.showEditDialog()
        if (this.$refs['mProjectForm']) {
          this.$refs['mProjectForm'].clearValidate()
        }
        this.mProjectForm.used = true
      },

      showUpdate(row) {
        this.editDialogTitle = '更新项目'
//        for (var fld in this.mProjectForm) {
//          this.mProjectForm[fld] = row[fld]
//        }
        this.mProjectForm = Object.assign({}, row)
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
        this.clearMProjectForm()
        if (this.$refs['mProjectForm']) {
          this.$refs['mProjectForm'].clearValidate()
        }
        this.isShowEditDialog = false
      },

      clearMProjectForm() {
        for (var fld in this.mProjectForm) {
          let ot = Object.prototype.toString.call(this.mProjectForm[fld]).slice(8, -1)
          this.mProjectForm[fld] = ot === 'String' ? '' : ot === 'Object' ? {} : ot === 'Array' ? [] : ''
        }
      },

      saveMProject() {
        this.$refs['mProjectForm'].validate((valid) => {
          if (valid) {
            this.isSaving = true
            if (this.mProjectForm.id === '') {
              this.createMProject()
            } else {
              this.updateMProject()
            }
          } else {
            return false
          }
        });
      },

      createMProject() {
        this.$ajax.post(
          '/mProject',
          this.mProjectForm,
          vo => {
            this.isSaving = false
            this.$message.success('保存成功!')
            this.closeEditDialog()
            this.queryMProject()
          },
          vo => {
            this.isLoading = false
            this.isSaving = false
            this.$message.error(vo.message)
          }
        )
      },

      updateMProject() {
        this.$ajax.put(
          '/mProject',
          this.mProjectForm,
          vo => {
            this.isSaving = false
            this.$message.success('保存成功!')
            this.closeEditDialog()
            this.queryMProject()
          },
          vo => {
            this.isLoading = false
            this.isSaving = false
            this.$message.error(vo.message)
          }
        )
      },

      deleteMProject(row) {
        this.$confirm('确定删除project[' + row.mProjectName + ']?', '提示', {
          confirmButtonText: '确定',
          type: 'warning'
        }).then(() => {
          this.$ajax.delete(
            '/mProject/' + row.id,
            null,
            vo => {
              if (vo.success) {
                this.$message.success('删除成功！')
                this.queryMProject()
              }
            },
            vo => {
              this.$message.error('删除失败，请重试！')
            }
          )
        })
      },

      batchDeleteMProject() {
        this.$confirm('确定批量删除选中的project?', '提示', {
          confirmButtonText: '确定',
          type: 'warning'
        }).then(() => {
          let ids = []
          for(let i = 0, length = this.selItems.length; i < length; i ++){
            ids.push(this.selItems[i].id)
          }
          this.$ajax.post(
            '/mProjects/batch',
            ids,
            vo => {
              if (vo.success) {
                this.$message.success('删除成功！')
                this.queryMProject()
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
        this.queryMProject()
      },

      handleCurrentChange(currentPage) {
        this.currentPage = currentPage
        this.gIndex = this.currentPage > 1 ? ((this.currentPage - 1) * this.pageSize) : 0
        this.queryMProject()
      }
    }
  }
</script>
<style lang="less">
  @import "../../less/app/_mProject.less";
</style>
