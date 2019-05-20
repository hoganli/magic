<template>
  <el-container class="page-entityMonitor">

    <el-header class="main-form">
      <el-form :inline="true"
               :model="entity"
               class="demo-form-inline">
        <el-form-item>
          <el-select v-model="entity.projectCode" style="width: 100%;" placeholder="请选择项目">
            <el-option
              v-for="p in projects"
              :key="p.projectCode"
              :label="p.projectName"
              :value="p.projectCode">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-input v-model="entity.fileName" placeholder="请输入文件名称"
                    style="width:180px;"
                    clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-input v-model="entity.dtsBatch" placeholder="请输入批次日期"
                    style="width:180px;"
                    clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-select v-model="entity.orderStatus" style="width: 100%;" placeholder="请选择状态">
            <el-option key="0" label="已入库（待下单）" value="0"></el-option>
            <el-option key="1" label="下单成功" value="1"></el-option>
            <el-option key="-1" label="下单失败" value="-1"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search"
                     v-if="hasPerm('entity:list')"
                     @click="onSubmit">查询</el-button>
        </el-form-item>
      </el-form>
    </el-header>

    <el-main class="main-table">
      <el-table :data="this.entityList"
                :highlight-current-row="true"
                v-loading="isLoading"
                border
                stripe
                size="small"
                :height="globalTableHeight"
                :maxHeight="globalTableHeight">
        <el-table-column type="index" label="序号" fixed align="center"></el-table-column>
        <el-table-column label="文件名称" prop="fileName" align="left" header-align="center"></el-table-column>
        <el-table-column label="批次日期" prop="dtsBatch" align="left" header-align="center"></el-table-column>
        <el-table-column label="状态" prop="orderStatus" align="left" header-align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.orderStatus === 0 ? 'warning' : scope.row.orderStatus === 1 ? 'success' : 'danger' ">
              {{scope.row.orderStatus === 0 ? '已入库' : scope.row.orderStatus === 1 ? '下单成功' : '下单失败'}}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="总数" prop="amount" align="left" header-align="center"></el-table-column>
      </el-table>

      <div class="table-footer">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          background
          :current-page=this.currentPage
          :page-sizes="[25, 50, 100, 200]"
          :page-size="25"
          layout="total, sizes, prev, pager, next, jumper"
          :total="this.entityListCount"
          v-show="this.entityListCount >= 0">
        </el-pagination>
      </div>
    </el-main>

  </el-container>
</template>
<script>
  export default {
    name: 'sfProject',
    data() {
      return {
        globalTableHeight: this.getGlobalTableHeight(),
        currentUser:JSON.parse(this.getUser()),
        entityList: [],
        projects: [],
        entity: {},
        isLoading: false,
        entityListCount: 0,
        gIndex: 0,
        currentPage: 1,
        pageSize: 25
      }
    },
    mounted: function() {
      this.initProjects()
      this.queryEntityList()
    },
    methods: {
      onSubmit() {
        this.queryEntityList()
      },

      initProjects() {
        this.$ajax.post(
          '/findProjectOptions/' + this.currentUser.id,
          {},
          vo => {
            this.projects = vo.dataList
          },
          vo => {
            this.projects = []
          }
        )
      },

      queryEntityList() {
        if(!this.entity.projectCode) {
          this.$message('请选择项目进行查询！')
          return
        }
        this.isLoading = true
        this.entity.start = this.gIndex
        this.entity.limit = this.pageSize
        this.entity.page = this.currentPage
        this.$ajax.post(
          '/entities',
          this.entity,
          vo => {
            this.entityList = vo.dataList
            this.isLoading = false
            this.entityListCount = vo.totalProperty
          },
          vo => {
            this.entityList = []
            this.isLoading = false
          }
        )
      },

      handleSizeChange(pageSize) {
        this.pageSize = pageSize
        this.gIndex = this.currentPage > 1 ? ((this.currentPage - 1) * this.pageSize) : 0
        this.queryEntityList()
      },

      handleCurrentChange(currentPage) {
        this.currentPage = currentPage
        this.gIndex = this.currentPage > 1 ? ((this.currentPage - 1) * this.pageSize) : 0
        this.queryEntityList()
      }

    }
  }
</script>
<style lang="less">
  @import "../../less/app/_entityMonitor.less";
</style>
