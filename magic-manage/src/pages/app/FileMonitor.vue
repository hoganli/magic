<template>
  <el-container class="page-fileMonitor">

    <el-header class="main-form">
      <el-form :inline="true"
               :model="file"
               class="demo-form-inline">
        <el-form-item>
          <el-select v-model="file.projectCode" style="width: 100%;" placeholder="请选择项目">
            <el-option
              v-for="p in projects"
              :key="p.projectCode"
              :label="p.projectName"
              :value="p.projectCode">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-select v-model="file.status" style="width: 100%;" placeholder="请选择处理状态">
            <el-option key="false" label="未处理" value="false"></el-option>
            <el-option key="true" label="已处理" value="true"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search"
                     v-if="hasPerm('file:list')"
                     @click="onSubmit">查询</el-button>
        </el-form-item>
      </el-form>
    </el-header>

    <el-main class="main-table">
      <el-table :data="this.fileList"
                :highlight-current-row="true"
                v-loading="isLoading"
                border
                stripe
                size="small"
                :height="globalTableHeight"
                :maxHeight="globalTableHeight">
        <el-table-column type="index" label="序号" fixed align="center"></el-table-column>
        <el-table-column label="文件名称" prop="fileName" align="left" header-align="center"></el-table-column>
        <el-table-column label="项目名称" prop="projectName" align="left" header-align="center"></el-table-column>
        <el-table-column label="项目代码" prop="projectCode" align="left" header-align="center"></el-table-column>
        <el-table-column label="上传时间" prop="uploadTime" align="left" header-align="center"></el-table-column>
        <el-table-column label="处理状态" prop="status" align="left" header-align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === true ? 'success' : 'danger' ">
              {{scope.row.status === true ? '已处理' : '未处理'}}
            </el-tag>
          </template>
        </el-table-column>

      </el-table>

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
        fileList: [],
        projects: [],
        file: {},
        isLoading: false
      }
    },
    mounted: function() {
      this.initProjects()
      this.queryFileList()
    },
    methods: {
      onSubmit() {
        this.queryFileList()
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

      queryFileList() {
        this.isLoading = true
        this.file.principal = this.currentUser.id
        this.$ajax.post(
          '/files',
          this.file,
          vo => {
            this.fileList = vo.dataList
            this.isLoading = false
          },
          vo => {
            this.fileList = []
            this.isLoading = false
          }
        )
      }

    }
  }
</script>
<style lang="less">
  @import "../../less/app/_fileMonitor.less";
</style>
