<template>
  <div id="app" style="background-color: rgba(235, 235, 235, 0.08)">
    <router-view/>
  </div>
</template>

<script>
  export default {
    name: 'app'
  }
</script>

<style>
  #app {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
    margin-top: 60px;
  }

  #app > .el-aside, .el-main {
    overflow: hidden !important;
  }

  /* firefox需要 */
  #app > .el-header, .home-header, .el-container {
    overflow: hidden !important;
  }

  /* 附件模板配置抄送人校验提示框样式 */
   .user-defined-popover {
    border: 1px solid #5c5c5c;
    color: red;
  }

   .user-defined-popover > .popper__arrow{
    border-top-color: #5c5c5c !important;
  }

</style>
