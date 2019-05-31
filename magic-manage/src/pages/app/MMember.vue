<template>
  <el-container class="page-mMember">

    <el-header class="main-form">
      <el-form :inline="true"
               :model="mMember"
               class="demo-form-inline">
        <el-form-item>
          <el-input v-model="mMember.id" placeholder="memberID"
                    style="width:200px;"
                    clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search"
                     v-if="hasPerm('mMember:list')"
                     @click="onSubmit">查询</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="success" icon="el-icon-circle-plus-outline"
                     v-if="hasPerm('mMember:create')"
                     @click="showCreate">新建</el-button>
        </el-form-item>
      </el-form>
    </el-header>

    <el-main class="main-table">
      <el-table :data="this.mMemberList"
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
        <el-table-column label="微信名" prop="nickName" align="center" header-align="center"></el-table-column>
        <el-table-column label="姓名" prop="userName" align="center" header-align="center"></el-table-column>
        <el-table-column label="性别" prop="sexual" align="center" header-align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.sexual === 1 ? 'success' : 'danger' ">
              {{scope.row.sexual === 1 ? '先生' : '小姐'}}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="手机号" prop="mobilePhone" align="center" header-align="center"></el-table-column>
        <el-table-column label="会员等级" prop="level" align="center" header-align="center"></el-table-column>
        <el-table-column label="积分" prop="integral" align="center" header-align="center"></el-table-column>
        <el-table-column label="操作" width="120px" align="center" fixed="right">
          <template slot-scope="scope">
            <el-tooltip content="编辑" placement="top">
              <el-button circle type="primary" size="mini" icon="el-icon-edit" v-if="hasPerm('mMember:update')" @click="showUpdate(scope.row)"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button circle type="danger" size="mini" icon="el-icon-delete" v-if="hasPerm('mMember:delete')" @click="deleteMMember(scope.row)"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <div class="table-footer">
        <el-button type="danger" size="mini" icon="el-icon-delete"
                   style="margin: 0px;"
                   v-show="this.mMemberList.length >= 0"
                   :disabled="this.selItems.length == 0"
                   v-if="hasPerm('mMember:delete')"
                   @click="batchDeleteMMember">批量删除</el-button>
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          background
          :current-page=this.currentPage
          :page-sizes="[25, 50, 100, 200]"
          :page-size="25"
          layout="total, sizes, prev, pager, next, jumper"
          :total="this.mMemberListCount"
          v-show="this.mMemberListCount >= 0">
        </el-pagination>
      </div>
    </el-main>

    <!-- 编辑窗口 -->
    <el-dialog :title="editDialogTitle" :visible.sync="isShowEditDialog" :before-close="handleClose" :close-on-click-modal="false">
      <el-scrollbar class="dialog-scroll" ref="mMemberScroll">
        <el-form :model="mMemberForm"
                 ref="mMemberForm"
                 size="small"
                 label-position="right"
                 style="text-align: left"
                 label-width="130px">
          <el-form-item label="姓名手机号" prop="nickName" :rules="[{ required: false, message: '请输入nick_name'}]">
          <el-input v-model="mMemberForm.nickName"></el-input>
          </el-form-item>
          <el-form-item label="手机号码" prop="mobilePhone" :rules="[{ required: false, message: '请输入mobile_phone'}]">
          <el-input v-model="mMemberForm.mobilePhone"></el-input>
          </el-form-item>
          <el-form-item label="性别" prop="sexual" :rules="[{ required: false, message: '请输入sexual'}]">
            <el-switch v-model="mMemberForm.sexual"></el-switch>
          </el-form-item>
          <el-form-item label="会员等级" prop="level" :rules="[{ required: false, message: '请输入level'}]">
          <el-input v-model="mMemberForm.level"></el-input>
          </el-form-item>
          <el-form-item label="积分" prop="integral" :rules="[{ required: false, message: '请输入integral'}]">
          <el-input v-model="mMemberForm.integral"></el-input>
          </el-form-item>
        </el-form>
      </el-scrollbar>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeEditDialog()">取 消</el-button>
        <el-button type="primary" :loading="isSaving" @click="saveMMember">确 定</el-button>
      </div>
    </el-dialog>

  </el-container>
</template>
<script>
  let _mMemberForm = {}
  export default {
    name: 'mMember',
    data() {
      return {
        globalTableHeight: this.getGlobalTableHeight(),

        mMember: {},
        mMemberForm: {
		    	id: '',
		    	nickName: '',
		    	mobilePhone: '',
		    	sexual: '',
		    	level: '',
		    	integral: '',
        },

        selItems: [],
        editDialogTitle: '',
        isLoading: false,
        isShowEditDialog: false,
        isSaving: false,
        mMemberList: [],

        mMemberListCount: 0,
        gIndex: 0,
        currentPage: 1,
        pageSize: 25
      }
    },
    mounted: function () {
      this.queryMMember()
      _mMemberForm = Object.assign({}, this.mMemberForm)
      window.onresize = () => {
        this.reSetGlobalHeightParams()
        this.globalTableHeight = this.getGlobalTableHeight()
        if (this.$refs.mMemberScroll) {
          this.$refs.mMemberScroll.update()
        }
      }
    },
    methods: {
      onSubmit() {
        this.queryMMember()
      },

      handleSelectionChange(rowVal) {
        this.selItems = rowVal
      },

      queryMMember() {
        this.mMember.start = this.gIndex
        this.mMember.limit = this.pageSize
        this.mMember.page = this.currentPage
        this.isLoading = true
        this.$ajax.post(
          '/mMembers',
          this.mMember,
          vo => {
            this.isLoading = false
            this.mMemberList = vo.dataList
            this.mMemberListCount = vo.totalProperty
          },
          vo => {
            this.isLoading = false
            this.mMemberList = []
            this.$message.error('查询失败，请重试！')
          }
        );
      },

      showCreate() {
        this.editDialogTitle = '新建member'
        // 创建窗口还原Form副本
        this.mMemberForm = _mMemberForm
        this.clearMMemberForm()
        this.showEditDialog()
        if (this.$refs['mMemberForm']) {
          this.$refs['mMemberForm'].clearValidate()
        }
      },

      showUpdate(row) {
        this.editDialogTitle = '更新member'
//        for (var fld in this.mMemberForm) {
//          this.mMemberForm[fld] = row[fld]
//        }
        this.mMemberForm = Object.assign({}, row)
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
        this.clearMMemberForm()
        if (this.$refs['mMemberForm']) {
          this.$refs['mMemberForm'].clearValidate()
        }
        this.isShowEditDialog = false
      },

      clearMMemberForm() {
        for (var fld in this.mMemberForm) {
          let ot = Object.prototype.toString.call(this.mMemberForm[fld]).slice(8, -1)
          this.mMemberForm[fld] = ot === 'String' ? '' : ot === 'Object' ? {} : ot === 'Array' ? [] : ''
        }
      },

      saveMMember() {
        this.$refs['mMemberForm'].validate((valid) => {
          if (valid) {
            this.isSaving = true
            if (this.mMemberForm.id === '') {
              this.createMMember()
            } else {
              this.updateMMember()
            }
          } else {
            return false
          }
        });
      },

      createMMember() {
        this.$ajax.post(
          '/mMember',
          this.mMemberForm,
          vo => {
            this.isSaving = false
            this.$message.success('保存成功!')
            this.closeEditDialog()
            this.queryMMember()
          },
          vo => {
            this.isLoading = false
            this.isSaving = false
            this.$message.error(vo.message)
          }
        )
      },

      updateMMember() {
        this.$ajax.put(
          '/mMember',
          this.mMemberForm,
          vo => {
            this.isSaving = false
            this.$message.success('保存成功!')
            this.closeEditDialog()
            this.queryMMember()
          },
          vo => {
            this.isLoading = false
            this.isSaving = false
            this.$message.error(vo.message)
          }
        )
      },

      deleteMMember(row) {
        this.$confirm('确定删除member[' + row.mMemberName + ']?', '提示', {
          confirmButtonText: '确定',
          type: 'warning'
        }).then(() => {
          this.$ajax.delete(
            '/mMember/' + row.id,
            null,
            vo => {
              if (vo.success) {
                this.$message.success('删除成功！')
                this.queryMMember()
              }
            },
            vo => {
              this.$message.error('删除失败，请重试！')
            }
          )
        })
      },

      batchDeleteMMember() {
        this.$confirm('确定批量删除选中的member?', '提示', {
          confirmButtonText: '确定',
          type: 'warning'
        }).then(() => {
          let ids = []
          for(let i = 0, length = this.selItems.length; i < length; i ++){
            ids.push(this.selItems[i].id)
          }
          this.$ajax.post(
            '/mMembers/batch',
            ids,
            vo => {
              if (vo.success) {
                this.$message.success('删除成功！')
                this.queryMMember()
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
        this.queryMMember()
      },

      handleCurrentChange(currentPage) {
        this.currentPage = currentPage
        this.gIndex = this.currentPage > 1 ? ((this.currentPage - 1) * this.pageSize) : 0
        this.queryMMember()
      }
    }
  }
</script>
<style lang="less">
  @import "../../less/app/_mMember.less";
</style>
