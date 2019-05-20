<template>
  <el-container class="page-dashboard">

    <el-main class="main-dashboard" :style="{height: globalDashboardMainHeight +'px'}">

      <el-row :gutter="20" class="charts-row">
        <el-col :span="12" class="step-col" :style="{height: globalDashboardMainHeight - 40 +'px'}">
          <div class="charts">
            <h2>新项目上线步骤（必读）</h2>
            <el-table :data="this.stepList" style="width: 100%"
                      :height="globalDashboardMainHeight - 40 - 31 - 38 + 'px'"
                      :max-height="globalDashboa rdMainHeight - 40 - 31 - 38 + 'px'">
              <el-table-column align="left" prop="stepNumber" label="步骤序号" width="100"></el-table-column>
              <el-table-column align="left" prop="stepName" label="准备内容"></el-table-column>
            </el-table>
          </div>
        </el-col>

        <el-col :span="12" class="project-col" :style="{height: globalDashboardMainHeight - 40 +'px'}">
          <div class="charts">
            <h2>已上线项目清单</h2>
            <el-table :data="this.projectList" style="width: 100%"
                      :height="globalDashboardMainHeight - 40 - 31 - 38 + 'px'"
                      :max-height="globalDashboardMainHeight - 40 - 31 - 38 + 'px'">
              <el-table-column align="left" type="index" width="50"></el-table-column>
              <el-table-column align="left" prop="projectName" label="项目名称"></el-table-column>
              <el-table-column align="left" prop="projectCode" label="项目代号" width="100"></el-table-column>
              <el-table-column align="left" prop="principalName" label="负责人" width="100"></el-table-column>
              <el-table-column align="left" prop="startSign" label="启用状态" width="80">
                <template slot-scope="scope">
                  <el-tag :type="scope.row.startSign === true ? 'succes s' : 'danger' ">
                    {{scope.row.startSign === true ? '启用' : '停用'}}
                  </el-tag>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-col>
      </el-row>

    </el-main>

  </el-container>
</template>

<style>
</style>
<script>
  import ElRow from "element-ui/packages/row/src/row";
  import ElCol from "element-ui/packages/col/src/col";

  export default {
    components: {
      ElCol,
      ElRow
    },
    data() {
      return {
        globalDashboardMainHeight: this.getGlobalTypesetMainHeight(),
        sessionUserID: JSON.parse(this.getUser()).id,
        adminSign: JSON.parse(this.getUser()).adminSign,

        stepList: [
          {
            stepNumber: "第一步",
            stepName: "确定该项目的快递类型（1-顺丰次日、2-顺丰隔日、5-顺丰次晨、6-顺丰即日）"
          },
          {
            stepNumber: "第二步",
            stepName: "确定该项目的付费方式（1-寄方付、2-收方付、3-第三方付）"
          },
          {
            stepNumber: "第三步",
            stepName: "确定该项目的月结账号"
          },
          {
            stepNumber: "第四步",
            stepName: "向顺丰业务人员申请该项目的邮寄单号号码段（起始号段~结束号段）"
          },
          {
            stepNumber: "第五步",
            stepName: "向顺丰业务人员申请该项目的接入编码（access_code）和客户校验码（checkword）"
          },
          {
            stepNumber: "第六步",
            stepName: "准备好以上信息后，在本系统“项目配置”模块创建项目"
          },
          {
            stepNumber: "第七步",
            stepName: "准备好该项目的运单文件，并设置好运单文件传输路径，将运单文件上传至指定位置"
          },
          {
            stepNumber: "第八步",
            stepName: "启动该项目，并跟进该项目的试运行情况"
          }
        ],
        projectList: []
      }
    },
    mounted: function () {

      // 审计员权限处理
      if ('auditor' !== JSON.parse(this.getUser()).account) {
        this.getSfProjects();
      }

      window.onresize = () => {
        this.reSetGlobalHeightParams();
        this.globalDashboardMainHeight = this.getGlobalTypesetMainHeight();
      }

    },
    methods: {

      // 查询用户对应的项目列表
      getSfProjects() {
        this.$ajax.post(
          '/findProjectOptions/' + JSON.parse(this.getUser()).id,
          null,
          vo => {
            this.projectList = vo.dataList
          },
          vo => {
            this.projectList = []
            this.$message.error('查询失败，请重试！')
          }
        );
      }

    },
  }
</script>
<style lang="less">
  @import "../less/_dashboard_ep.less";
</style>
