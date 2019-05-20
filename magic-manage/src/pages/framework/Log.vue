<template>
  <el-container class="page-log">

    <el-header class="main-form">
      <el-form :inline="true"
               :model="log"
               class="demo-form-inline">
        <el-form-item>
          <el-input v-model="log.userName" placeholder="用户名称" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-date-picker
            v-model="log.startDate"
            type="datetime"
            :editable="false"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="开始日期时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-date-picker
            v-model="log.endDate"
            type="datetime"
            :editable="false"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="结束日期时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" v-if="hasPerm('log:list')" @click="onSubmit">查询</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="success" icon="el-icon-download" v-if="hasPerm('log:list') && hasPerm('log:export')" @click="exportCVS">导出CVS</el-button>
        </el-form-item>
      </el-form>
    </el-header>

    <el-main class="main-table">
      <el-table :data="this.logData"
                :highlight-current-row="true"
                v-loading="logDataLoading"
                border
                stripe
                size="small"
                class="log-grid"
                :height="globalTableHeight"
                :maxHeight="globalTableHeight">
        <el-table-column type="index" label="序号" fixed :index=this.gIndex+1 align="center"></el-table-column>
        <el-table-column prop="userAccount" label="用户账号" width="150" align="left" header-align="center"></el-table-column>
        <el-table-column prop="userName" label="用户名称" width="100" align="left" header-align="center"></el-table-column>
        <el-table-column prop="opType" label="操作类型" width="80" align="left" header-align="center"></el-table-column>
        <el-table-column prop="opMethod" label="操作方法" width="150" align="left" header-align="center"></el-table-column>
        <el-table-column prop="opArgs" label="操作参数" align="left" header-align="center" :show-overflow-tooltip=true></el-table-column>
        <el-table-column label="操作结果" width="80" align="left" header-align="center">
          <template slot-scope="scope">{{scope.row.opResult === true ? '成功' : '失败'}}</template>
        </el-table-column>
        <el-table-column prop="opIp" label="操作IP" width="120" align="left" header-align="center"></el-table-column>
        <el-table-column label="操作时间" width="180" align="left" header-align="center">
          <template slot-scope="scope">
            <i class="el-icon-time"></i>
            <span style="margin-left: 5px">{{scope.row.createDate}}</span>
          </template>
        </el-table-column>
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
          :total="this.logDataCount">
        </el-pagination>
      </div>
  </el-main>
  </el-container>
</template>
<script>
  export default {
    name: 'log',
    data() {
      return {
        globalTableHeight: this.getGlobalTableHeight(),

        centerDialogVisible: false,
        log: {},                    // form表单绑定元素
        logDataLoading: false,      // logTable数据加载遮罩状态
        logData: [],                // logTable数据集合
        logDataCount: 0,
        gIndex: 0,                  // logTable序号列下标
        currentPage: 1,
        pageSize: 25
      }
    },
    mounted: function () {
      this.queryLogLists();
      window.onresize = () => {
        this.reSetGlobalHeightParams();
        this.globalTableHeight = this.getGlobalTableHeight()
      }
    },
    methods: {
      onSubmit: function () {
        this.queryLogLists();
      },

      exportCVS: function () {
        var params = {
          userName: this.log.userName,
          startDate: this.log.startDate,
          endDate: this.log.endDate
        }
        this.logDataLoading = true
        this.$ajax.post(
          '/logs',
          params,
          vo => {
            this.logDataLoading = false
            this.logData = vo.dataList
            this.logDataCount = vo.totalProperty
            if (vo.totalProperty > 0) {
              params.userName = (params.userNama === '' || params.userNama === null || params.userNama === undefined)  ? '' : params.userName
              params.startDate = (params.startDate === '' || params.startDate === null || params.startDate === undefined) ? '' : params.startDate
              params.endDate = (params.endDate === '' || params.endDate === null || params.endDate === undefined) ? '' :  params.endDate
              window.location = '/gcm/api/logs/export.do?userName=' +  params.userName
                              + '&startDate=' + params.startDate
                              + '&endDate=' + params.endDate
              this.$message.success('导出成功！');
            } else {
              this.$message.error('没有数据可以导出，请重试！');
            }
          },
          vo => {
            this.logDataLoading = false
            this.$message.error('导出失败，请重试！');
          }
        );
      },

      queryLogLists: function () {
        var params = {
          userName: this.log.userName,
          startDate: this.log.startDate,
          endDate: this.log.endDate,
          start: this.gIndex,
          limit: this.pageSize,
          page: this.currentPage
        }
        this.logDataLoading = true
        this.$ajax.post(
          '/logs',
          params,
          vo => {
//            this.$message.success('查询成功！');
            this.logDataLoading = false
            this.logData = vo.dataList
            this.logDataCount = vo.totalProperty
          },
          vo => {
            this.logDataLoading = false
            this.$message.error('查询失败，请重试！');
          }
        );
      },

      handleSizeChange(pageSize) {
        this.pageSize = pageSize
        this.gIndex = this.currentPage > 1 ? ((this.currentPage - 1) * this.pageSize) : 0
        this.queryLogLists();
      },

      handleCurrentChange(currentPage) {
        this.currentPage = currentPage;
        this.gIndex = this.currentPage > 1 ? ((this.currentPage - 1) * this.pageSize) : 0
        this.queryLogLists();
      }
    }
  }
</script>
<style lang="less">
 @import "../../less/framework/_log.less";
</style>
