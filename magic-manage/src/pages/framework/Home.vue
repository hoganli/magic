<template>
  <el-container class="page-home" v-loading="loading">
    <!--header部分-->
    <el-header class="home-header">
      <div class="home-title" v-if="!isCollapse" style="width:200px">后台管理系统</div>
      <div class="home-title" v-else="isCollapse" style="width:44px">
        <el-popover
          placement="top-start"
          title="后台管理系统"
          width="200"
          trigger="hover">
          <div slot="" class="popover-content">
            <span>版本：1.0.0</span><br>
            <span>Powered By Hogan</span><br>
          </div>
          <span slot="reference"><i class="fa fa-th"></i></span>
        </el-popover>
      </div>
      <div class="home-tool"><i :class="isCollapse ? 'fa fa-indent' : 'fa fa-outdent'" @click="collapseMenu"></i></div>
      <div class="home-info">
        <el-dropdown @command="handleCommand">
          <span class="el-dropdown-link home-info-user">
            <i class="fa fa-user-circle fa-lg"></i>
            &nbsp;当前用户：{{userName}}
            <i class="el-icon-arrow-down el-icon--right home-info-user"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="sysMsg"><i class="fa fa-envelope-o"></i>&nbsp;&nbsp;系统消息</el-dropdown-item>
            <el-dropdown-item command="updPwd"><i class="fa fa-key" aria-hidden="true"></i>&nbsp;&nbsp;修改密码</el-dropdown-item>
            <el-dropdown-item command="logout" divided><i class="fa fa-power-off"></i>&nbsp;&nbsp;退出系统</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </el-header>

    <!--导航+面包屑-->
    <el-container>

      <!--导航-->
      <el-aside :width="isCollapse ? '64px' : '220px'">

        <el-menu
          :default-active="activeMenu"
          class="el-menu-vertical-demo"
          @select="selectMenu"
          background-color="#545c64"
          text-color="#fff"
          active-text-color="#ffd04b"
          :unique-opened=true
          :collapse=isCollapse>
          <!--router>-->

          <!--遍历一级-->
          <template v-for="(item,index) in this.$router.options.routes" v-if="!item.hidden">

            <!--一级菜单-->
            <el-menu-item :index=build1Index(index) :route="{path:item.path}" v-if="!item.children">
              <i :class="item.iconCls"></i>
              <span slot="title">{{index}}&nbsp;{{item.name}}</span>
            </el-menu-item>

            <!--多级菜单-->
            <el-submenu :index=build1Index(index) v-else>
              <!--一级标题-->
              <template slot="title">
                <i :class="item.iconCls"></i>
                <span>{{index}}&nbsp;{{item.name}}</span>
              </template>

              <!--遍历二级-->
              <template v-for="(child,childIndex) in item.children">
                <!--二级菜单-->
                <!--为了实现tab与菜单的对应，将index更改为菜单的path-->
                <!--<el-menu-item :index=build2Index(index,childIndex) :route="{path:child.path}">-->
                <el-menu-item :index="child.path" :route="{path:child.path}">
                  <i :class="child.iconCls"></i>
                  <span slot="title">{{index}}.{{childIndex + 1}}&nbsp;{{child.name}}</span>
                </el-menu-item>
              </template>
            </el-submenu>

          </template>
        </el-menu>
      </el-aside>

      <!--面包屑模式-->
     <!-- <el-main class="right-main">
        <el-breadcrumb separator-class="el-icon-arrow-right">
          <el-breadcrumb-item :to="{ path: '/home/dashboard' }">主页</el-breadcrumb-item>
          <el-breadcrumb-item v-for="(r,index) in this.$router.currentRoute.matched" v-if="r.name">{{r.name}}
          </el-breadcrumb-item>
        </el-breadcrumb>
        <keep-alive>
          <router-view v-if="this.$route.meta.keepAlive"></router-view>
        </keep-alive>
        <router-view v-if="!this.$route.meta.keepAlive"></router-view>
      </el-main>-->

      <!--tab模式-->
      <el-main class="right-main">
        <el-tabs v-model="activeTab" type="card" @tab-click="clickPageTab" @tab-remove="closePageTab">
          <el-tab-pane
            v-for="(item, index) in openedPageTabList"
            :key="item.menuIndex"
            :label="item.menuName"
            :name="item.menuIndex"
            :closable="index===0?false:true">
          </el-tab-pane>
          <keep-alive :include="cacheComponentList">
            <router-view></router-view>
          </keep-alive>
        </el-tabs>
      </el-main>

    </el-container>

    <upd-pwd-dialog ref="updpwddialog"
                    v-bind:is-login="isLogin"
                    v-bind:cur-account="account"
                    v-on:logOutRequest="logOutRequest">
    </upd-pwd-dialog>

  </el-container>

</template>
<script>
  import {getUser, removeUser} from '@/utils/auth'
  import bus from '@/utils/eventBus'
  import UpdPwdDialog from '@/components/UpdPwdDialog';
  export default{
    components: {
      "upd-pwd-dialog": UpdPwdDialog
    },
    data(){

      return {
        activeMenu: '/home/dashboard',
        isCollapse: false,
        userName: '',
        account: '',
        loading: false,
        activeTab: '/home/dashboard',
        cacheComponentList: [],           // 要缓存的组件列表
        allPageTabList: [],     // 所有可点击菜单Tab
        openedPageTabList: [    // 已经打开的菜单Tab
          {
            menuIndex: '/home/dashboard',
            menuName: '系统主页'
          }
        ],
        isLogin: true
      }
    },

    methods: {
      // 折叠/收起菜单
      collapseMenu() {
        this.isCollapse = !this.isCollapse;
      },

      // 选择菜单
      selectMenu(index, indexPath) {
        this.activeMenu = index;
        this.addPageTab(index);
      },

      // 创建一级菜单索引
      build1Index(index) {
        return index + '';
      },

      // 创建二级菜单索引
      build2Index(index, childIndex) {
        return index + '-' + (childIndex + 1);
      },

      // 创建三级菜单索引
      build3Index(index, childIndex, subChildIndex) {
        return index + '-' + childIndex + '-' + subChildIndex;
      },

      // 点击菜单打开tab
      addPageTab(menuIndex){
        // 如果菜单对应的tab已经打开，直接激活
        let openedTabs = this.openedPageTabList;
        let isOpened = false;
        openedTabs.forEach((tab,index) => {
          if(menuIndex === tab.menuIndex){
            isOpened = true;
            // 激活tab
            this.activeTab = tab.menuIndex;
            this.$router.push({ path: menuIndex });
          }
        })

        // 如果菜单对应的tab已经打开并激活，结束函数
        if(isOpened){
          return
        }

        if (openedTabs.length > 7) {
          this.$message({type: 'error', message: '已打开的菜单太多，请先关闭不用菜单！'});
          return;
        }

        // 如果菜单对应的tab未曾打开，则打开tab，并激活
        let allTabs = this.allPageTabList;
        allTabs.forEach((tab,index) => {
          if(menuIndex === tab.menuIndex){
            // 添加到tab列表
            this.openedPageTabList.push({
              menuIndex: tab.menuIndex,
              menuName: tab.menuName,
              componentName: tab.componentName
            });
            // 激活tab
            this.activeTab = tab.menuIndex;
            this.$router.push({ path: menuIndex });
            //将组件添加到缓存列表中
            this.cacheComponentList.push(tab.componentName)
          }
        });
      },

      // 点击菜单对应的tab
      clickPageTab(tab){
        // 激活tab
        this.activeTab = tab.name;
        // 跳转路由
        this.$router.push({path: tab.name});
        // 选中菜单
        this.activeMenu = tab.name;
      },

      // 关闭菜单对应的tab
      closePageTab(targetName) {
        let tabs = this.openedPageTabList;
        let activeTab = this.activeTab;
        tabs.forEach((tab, index) => {
          if (tab.menuIndex === targetName) {
            if (activeTab === targetName) {
              let nextTab = tabs[index + 1] || tabs[index - 1];
              if (nextTab) {
                // 激活替代tab
                activeTab = nextTab.menuIndex;
                // 跳转路由
                this.$router.push({path: activeTab});
              }
            }
            //将组件从缓存列表中删除
            this.delCacheComponent(tab.componentName)
          }
        });

        this.activeTab = activeTab;
        this.openedPageTabList = tabs.filter(tab => tab.menuIndex !== targetName);
      },

      //删除已缓存的组件
      delCacheComponent(componentName) {
        let that = this
        let cacheComponentList = that.cacheComponentList
        cacheComponentList.forEach(function (item, index) {
          if (item === componentName) {
            that.cacheComponentList.splice(index, 1)
          }
        })
      },

      // 构建所有的pagaTabList
      buildAllPageTabList(){
        var menus = this.$router.options.routes;
        for (let i in menus) {
          let item = menus[i];
          if (!item.hidden) {
            if (!item.children) {
              this.allPageTabList.push({
                menuIndex: item.path,
                menuName: item.name
              });
            } else {
              for (let j in item.children) {
                let child = item.children[j];
                if (!child.hidden) {
                  this.allPageTabList.push({
                    menuIndex: child.path,
                    menuName: child.name,
                    componentName: child.component.name
                  });
                }
              }
            }
          }
        }
      },

      // 用户操作
      handleCommand(command){
        switch (command) {
          case "logout":
            this.logOut()
            break;
          case "updPwd":
            this.updPwd()
            break;
        }
      },

      // 修改密码调用子组件修改密码弹窗方法
      updPwd() {
        this.$refs.updpwddialog.showUpdPwdDialog()
      },

      // logout登出方法
      logOut() {
        var _this = this;
        this.$confirm('确定注销并退出？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(function () {
          _this.loading = true
          _this.logOutRequest()
          _this.loading = false
        }).catch(() => {
          this.$message.info({center: true, message: '取消操作'})
        })
      },

      // 登出请求
      logOutRequest() {
        // 请求后台注销
        this.$ajax.get(
          '/logout',
          null,
          vo => {
            this.loading = false
            // 删除cookie中的user
            removeUser()
            // 跳转至登录页面，用go是为了能够刷新菜单和路由
            this.$router.go({path: '/login'});
          },
          vo => {
            this.loading = false
            this.$message({type: 'error', message: '注销失败，请重试'});
          }
        )
      },

      // 获取ElementyType枚举值
      getElementType() {
        this.$ajax.get(
          '/elementTypes',
          null,
          vo => {
            var filterEleType = []
            for (let i = vo.dataList.length -1; i >= 0; i--) {
              if (vo.dataList[i].value === 'FONT' || vo.dataList[i].value === 'QRCODE') {
                filterEleType.push(vo.dataList[i])
                vo.dataList.splice(i, 1);
              }
            }
            window.localStorage.clear();
            window.localStorage.setItem('filterEleType', JSON.stringify(filterEleType));
            window.localStorage.setItem('elementType', JSON.stringify(vo.dataList));
          },
          vo => {
            this.$message.error('查询元素类别失败，请重试！');
          }
        );
      }
    },

    mounted: function () {
      let that = this
      let loginUser = JSON.parse(getUser())
      this.userName = loginUser.userName
      this.account = loginUser.account
      this.buildAllPageTabList();
//      this.getElementType();
      this.$router.push({path: this.activeTab});

      bus.$on('logOutRequest', function () {
        that.logOutRequest();
      })
    }
  }
</script>
<style lang="less">
  @import "../../less/framework/_home.less";
</style>
