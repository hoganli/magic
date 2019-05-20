<template>
  <el-container class="page-dashboard">

    <el-main class="main-dashboard" :style="{height: globalDashboardMainHeight +'px'}">
      欢迎你！

    </el-main>

  </el-container>
</template>

<style>
</style>
<script>

  export default{
    data() {
      return {
        globalDashboardMainHeight: this.getGlobalTypesetMainHeight()
      }
    },
    mounted: function () {
      window.onresize = () => {
        this.reSetGlobalHeightParams();
        this.globalDashboardMainHeight = this.getGlobalTypesetMainHeight();
      }

    },
    methods: {}
  }
</script>
<style lang="less">
  @import "../less/_dashboard.less";
</style>
