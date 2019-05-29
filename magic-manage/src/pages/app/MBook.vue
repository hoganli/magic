<template>
  <el-container class="page-mBook">

    <el-header class="main-form">
      <el-form :inline="true"
               :model="mBook"
               class="demo-form-inline">
        <el-form-item>
          <el-input v-model="mBook.id" placeholder="预约ID"
                    style="width:200px;"
                    clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search"
                     v-if="hasPerm('mBook:list')"
                     @click="onSubmit">查询</el-button>
        </el-form-item>
        <!--<el-form-item>-->
          <!--<el-button type="success" icon="el-icon-circle-plus-outline"-->
                     <!--v-if="hasPerm('mBook:create')"-->
                     <!--@click="showCreate">新建</el-button>-->
        <!--</el-form-item>-->
      </el-form>
    </el-header>

    <el-main class="main-table">
      <el-table :data="this.mBookList"
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
        <el-table-column label="姓名" prop="member.userName" align="center" header-align="center"></el-table-column>
        <el-table-column label="性别" prop="member.sexual" align="center" header-align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.member.sexual === 1 ? 'success' : 'danger' ">
              {{scope.row.member.sexual === 1 ? '先生' : '小姐'}}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="预约项目" prop="project.projectName" align="center" header-align="center"></el-table-column>
        <el-table-column label="预约日期" prop="bDate" align="center" header-align="center"></el-table-column>
        <el-table-column label="预约时间" prop="bTime" align="center" header-align="center"></el-table-column>
        <el-table-column label="是否确认" prop="confirm" align="center" header-align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.confirm === true ? 'success' : 'danger' ">
              {{scope.row.confirm === true ? '已确认' : '未确认'}}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120px" align="center" fixed="right">
          <template slot-scope="scope">
            <el-tooltip content="编辑预约" placement="top">
              <el-button circle type="primary" size="mini" icon="el-icon-edit" v-if="hasPerm('mBook:update')" @click="showUpdate(scope.row)"></el-button>
            </el-tooltip>
            <el-tooltip content="删除预约" placement="top">
              <el-button circle type="danger" size="mini" icon="el-icon-delete" v-if="hasPerm('mBook:delete')" @click="deleteMBook(scope.row)"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <div class="table-footer">
        <el-button type="danger" size="mini" icon="el-icon-delete"
                   style="margin: 0px;"
                   v-show="this.mBookList.length >= 0"
                   :disabled="this.selItems.length == 0"
                   v-if="hasPerm('mBook:delete')"
                   @click="batchDeleteMBook">批量删除</el-button>
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          background
          :current-page=this.currentPage
          :page-sizes="[25, 50, 100, 200]"
          :page-size="25"
          layout="total, sizes, prev, pager, next, jumper"
          :total="this.mBookListCount"
          v-show="this.mBookListCount >= 0">
        </el-pagination>
      </div>
    </el-main>

    <!-- 编辑窗口 -->
    <el-dialog :title="editDialogTitle" :visible.sync="isShowEditDialog" :before-close="handleClose" :close-on-click-modal="false">
      <el-scrollbar class="dialog-scroll" ref="mBookScroll">
        <el-form :model="mBookForm"
                 ref="mBookForm"
                 size="small"
                 label-position="right"
                 style="text-align: left"
                 label-width="130px">
          <el-form-item label="member_id" prop="memberId" :rules="[{ required: false, message: '请输入member_id'}]">
          <el-input v-model="mBookForm.memberId"></el-input>
          </el-form-item>
          <el-form-item label="project_id" prop="projectId" :rules="[{ required: false, message: '请输入project_id'}]">
          <el-input v-model="mBookForm.projectId"></el-input>
          </el-form-item>
          <el-form-item label="b_date" prop="bDate" :rules="[{ required: false, message: '请输入b_date'}]">
          <el-input v-model="mBookForm.bDate"></el-input>
          </el-form-item>
          <el-form-item label="b_time" prop="bTime" :rules="[{ required: false, message: '请输入b_time'}]">
          <el-input v-model="mBookForm.bTime"></el-input>
          </el-form-item>
          <el-form-item label="是否确认" prop="confirm">
            <el-switch v-model="mBookForm.confirm"></el-switch>
          </el-form-item>
        </el-form>
      </el-scrollbar>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeEditDialog()">取 消</el-button>
        <el-button type="primary" :loading="isSaving" @click="saveMBook">确 定</el-button>
      </div>
    </el-dialog>

  </el-container>
</template>
<script>
  let _mBookForm = {}
  export default {
    name: 'mBook',
    data() {
      return {
        globalTableHeight: this.getGlobalTableHeight(),

        mBook: {},
        mBookForm: {
		    	id: '',
		    	memberId: '',
		    	projectId: '',
		    	bDate: '',
		    	bTime: '',
        },

        selItems: [],
        editDialogTitle: '',
        isLoading: false,
        isShowEditDialog: false,
        isSaving: false,
        mBookList: [],

        mBookListCount: 0,
        gIndex: 0,
        currentPage: 1,
        pageSize: 25
      }
    },
    mounted: function () {
      this.queryMBook()
      _mBookForm = Object.assign({}, this.mBookForm)
      window.onresize = () => {
        this.reSetGlobalHeightParams()
        this.globalTableHeight = this.getGlobalTableHeight()
        if (this.$refs.mBookScroll) {
          this.$refs.mBookScroll.update()
        }
      }
    },
    methods: {
      onSubmit() {
        this.queryMBook()
      },

      handleSelectionChange(rowVal) {
        this.selItems = rowVal
      },

      queryMBook() {
        this.mBook.start = this.gIndex
        this.mBook.limit = this.pageSize
        this.mBook.page = this.currentPage
        this.isLoading = true
        this.$ajax.post(
          '/mBooks',
          this.mBook,
          vo => {
            this.isLoading = false
            this.mBookList = vo.dataList
            this.mBookListCount = vo.totalProperty
          },
          vo => {
            this.isLoading = false
            this.mBookList = []
            this.$message.error('查询失败，请重试！')
          }
        );
      },

      showCreate() {
        this.editDialogTitle = '新建预约'
        // 创建窗口还原Form副本
        this.mBookForm = _mBookForm
        this.clearMBookForm()
        this.showEditDialog()
        if (this.$refs['mBookForm']) {
          this.$refs['mBookForm'].clearValidate()
        }
      },

      showUpdate(row) {
        this.editDialogTitle = '更新预约'
//        for (var fld in this.mBookForm) {
//          this.mBookForm[fld] = row[fld]
//        }
        this.mBookForm = Object.assign({}, row)
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
        this.clearMBookForm()
        if (this.$refs['mBookForm']) {
          this.$refs['mBookForm'].clearValidate()
        }
        this.isShowEditDialog = false
      },

      clearMBookForm() {
        for (var fld in this.mBookForm) {
          let ot = Object.prototype.toString.call(this.mBookForm[fld]).slice(8, -1)
          this.mBookForm[fld] = ot === 'String' ? '' : ot === 'Object' ? {} : ot === 'Array' ? [] : ''
        }
      },

      saveMBook() {
        this.$refs['mBookForm'].validate((valid) => {
          if (valid) {
            this.isSaving = true
            if (this.mBookForm.id === '') {
              this.createMBook()
            } else {
              this.updateMBook()
            }
          } else {
            return false
          }
        });
      },

      createMBook() {
        this.$ajax.post(
          '/mBook',
          this.mBookForm,
          vo => {
            this.isSaving = false
            this.$message.success('保存成功!')
            this.closeEditDialog()
            this.queryMBook()
          },
          vo => {
            this.isLoading = false
            this.isSaving = false
            this.$message.error(vo.message)
          }
        )
      },

      updateMBook() {
        this.$ajax.put(
          '/mBook',
          this.mBookForm,
          vo => {
            this.isSaving = false
            this.$message.success('保存成功!')
            this.closeEditDialog()
            this.queryMBook()
          },
          vo => {
            this.isLoading = false
            this.isSaving = false
            this.$message.error(vo.message)
          }
        )
      },

      deleteMBook(row) {
        this.$confirm('确定删除预约[' + row.mBookName + ']?', '提示', {
          confirmButtonText: '确定',
          type: 'warning'
        }).then(() => {
          this.$ajax.delete(
            '/mBook/' + row.id,
            null,
            vo => {
              if (vo.success) {
                this.$message.success('删除成功！')
                this.queryMBook()
              }
            },
            vo => {
              this.$message.error('删除失败，请重试！')
            }
          )
        })
      },

      batchDeleteMBook() {
        this.$confirm('确定批量删除选中的预约?', '提示', {
          confirmButtonText: '确定',
          type: 'warning'
        }).then(() => {
          let ids = []
          for(let i = 0, length = this.selItems.length; i < length; i ++){
            ids.push(this.selItems[i].id)
          }
          this.$ajax.post(
            '/mBooks/batch',
            ids,
            vo => {
              if (vo.success) {
                this.$message.success('删除成功！')
                this.queryMBook()
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
        this.queryMBook()
      },

      handleCurrentChange(currentPage) {
        this.currentPage = currentPage
        this.gIndex = this.currentPage > 1 ? ((this.currentPage - 1) * this.pageSize) : 0
        this.queryMBook()
      }
    }
  }
</script>
<style lang="less">
  @import "../../less/app/_mBook.less";
</style>
