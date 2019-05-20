<template>
  <el-container class="page-dashboard">

    <el-main class="main-dashboard" :style="{height: globalDashboardMainHeight +'px'}">
      <el-row class="button-row">
        <el-col class="button-col" :span="24">
          <el-button type="primary" plain>客户元素<span class="count">&nbsp;{{cusEleCount}}&nbsp;</span>个</el-button>
          <el-button type="primary" plain>组织元素<span class="count">&nbsp;{{orgEleCount}}&nbsp;</span>个</el-button>
          <el-button type="primary" plain>个人化元素<span class="count">&nbsp;{{perEleCount}}&nbsp;</span>个</el-button>
          <el-button type="primary" plain>烫印元素<span class="count">&nbsp;{{tangyinEleCount}}&nbsp;</span>个</el-button>
          <el-button type="primary" plain>立金元素<span class="count">&nbsp;{{lijinEleCount}}&nbsp;</span>个</el-button>
        </el-col>
      </el-row>

      <el-row :gutter="20" class="charts-row">
        <el-col :span="12" class="template-col" :style="{height: globalDashboardMainHeight - 130 +'px'}">
          <div class="charts">
            <div class="template" :options="tempPiePolar" :style="{width: '100%', height: '100%'}" ref="line"></div>
          </div>
        </el-col>

        <el-col :span="12" class="order-col" :style="{height: globalDashboardMainHeight - 130 +'px'}">
          <div class="charts">
            <div class="order" :options="orderLinePolar" :style="{width: '100%', height: '100%'}" ref="line"></div>
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
  export default{
    components: {
      ElCol,
      ElRow
    },
    data() {
      return {
        cusEleCount: 0,
        orgEleCount: 0,
        perEleCount: 0,
        tangyinEleCount: 0,
        lijinEleCount: 0,

        globalDashboardMainHeight: this.getGlobalTypesetMainHeight(),

        // 图表数据源
        tempData: [],
        orderData: [],

        // --------------------模板饼图-----------------
        tempPiePolar: {
          title: {
            text: '稿样模板统计'
          },
          tooltip: {},
          grid: {
            left: '3%',
            bottom: '3%',
            containLabel: true
          },
          color: ["#409eff", "#67c23a"],
          legend: {},
          toolbox: {
            show: true,
            top: 20,
            right: 30,
            iconStyle:{
              emphasis:{
                textPosition:'top'
              }
            },
            feature: {
              dataView: {
                readOnly: true,
                lang:['稿样模板统计 - 数据视图', '关闭视图窗口'],
                buttonColor: '#409EFF',
                optionToContent: function (opt) {
                  let data = opt.dataset[0].source;
                  let tdHeads = '';
                  let tdBodys = '';
                  data.forEach(function (item) {
                    if (item[1] !== 'count') {
                      tdHeads += `<td style="padding: 0 10px">${item[0]}</td>`;
                    }
                  });
                  let table = `<table border="1" style=" width: 95%; margin-left:20px;border-collapse:collapse;font-size:14px;text-align:center"><tbody><tr>${tdHeads} </tr>`;
                  for (let i = 1, j = data.length; i < j; i++) {
                    tdBodys += `<td>${data[i][1]}</td>`;
                  }
                  table += `<tr>${tdBodys}</tr>`;
                  table += '</tbody></table>';
                  return table;
                }
              },
              magicType: {},
              restore: {},
              saveAsImage: {}
            },
          },
          series: [
            {
              name: '稿样模板',
              type: 'pie',
              radius: '55%',
              center: ['50%', '50%']
            }
          ]
        },


        // --------------------订单条柱图-----------------
        orderLinePolar: {
          title: {
            text: '稿样订单统计'
          },
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'shadow', // line、shadow
              label: {
                backgroundColor: '#6a7985'
              }
            }
          },
          grid: {
            top: '30%',
            right: '15%',
            left: '10%',
            bottom: '10%',
            containLabel: true
          },
          legend: {
            data: ['订单']
          },
          color: ["#409eff"],
          toolbox: {
            show: true,
            top: 20,
            right: 30,
            iconStyle:{
              emphasis:{
                textPosition:'top'
              }
            },
            feature: {
              dataZoom: {},
              dataView: {
                readOnly: true,
                lang:['稿样订单统计 - 数据视图', '关闭视图窗口'],
                buttonColor: '#409EFF',
                optionToContent: function (opt) {
                  let data = opt.dataset[0].source;
                  let tdHeads = '<td style="padding: 0 10px;">月份</td><td  style="padding: 0 10px">数量</td>';
                  let tdBodys = '';
                  let table = `<table border="1" style="width: 95%; margin-left:20px;border-collapse:collapse;font-size:14px;text-align:center"><tbody><tr>${tdHeads} </tr>`;
                  for (let i = 1, j = data.length; i < j; i++) {
                      tdBodys += `<tr><td>${ data[i][0]}</td><td>${ data[i][1]}</td></tr>`;
                  }
                  table += `<tr>${tdBodys}</tr>`;
                  tdBodys = '';
                  table += '</tbody></table>';
                  return table;
                }
              },
              magicType: {
                type: ['line', 'bar']
              },
              restore: {},
              saveAsImage: {}
            },
          },
//          dataZoom: [
//            {
//              type: 'slider',
//              show: true,
//              xAxisIndex: [0],
//              start: 50,
//              end: 100
//            },
//            {
//              type: 'inside',
//              xAxisIndex: [0],
//              start: 50,
//              end: 100
//            }
//          ],
          xAxis: [{
            name: '月份',
            type: 'category',
            axisLabel: {
              interval: 0
            }
          }],
          yAxis: [{
            name: '数量',
            type: 'value',
            axisLine: {
              show: false
            },
            nameLocation: 'end',
            nameGap: 20,
            nameRotate: 0,
            axisTick: {
              show: false
            },
            splitLine: {
              lineStyle: {
                color: ['bank']
              }
            }
          }],
          series: [
          {
            name: '订单',
            type: 'bar',
            barWidth: '35%'
          }
          ]
        }
      }
    },
    mounted: function () {

//      this.getElementData();

      let orderLine = this.$echarts.init(document.getElementsByClassName('order')[0], 'light')
      let tempPie = this.$echarts.init(document.getElementsByClassName('template')[0], 'light')

      orderLine.setOption(this.orderLinePolar);
      tempPie.setOption(this.tempPiePolar);

//      this.getOrderDataDrawLine(orderLine);
//      this.getTemplateDataDrawPie(tempPie);

      window.onresize = () => {
        this.reSetGlobalHeightParams();
        this.globalDashboardMainHeight = this.getGlobalTypesetMainHeight();
        setTimeout(() => {
          orderLine.resize();
          tempPie.resize();
        }, 20)
      }

    },
    methods: {

      getTemplateDataDrawPie (pie) {
        this.$ajax.get(
          '/template/pieChart',
          null,
          vo => {
            this.tempData = vo.data
            if (this.tempData.length > 0) {
//              this.$message.success('获取模板图表数据成功！');
              this.tempData.unshift(['product', 'count']);
              pie.setOption({
                dataset: {
                  source: this.tempData
                },
                series: [{
                  encode: {
                    itemName: 'product',
                    value: 'count'
                  }
                }]
              })
            }
          },
          vo => {
            this.isLoading = false;
            this.$message.error('查询失败，请重试！');
          }
        )
      },


      getOrderDataDrawLine (line) {
        this.$ajax.get(
          '/order/lineChart',
          null,
          vo => {
            if (vo.success === true) {
              this.orderData = vo.data
//              this.orderData = [
//                ['201801',1000],
//                ['201802',1500],
//                ['201803',2000],
//                ['201804',2500],
//                ['201805',1000]
//              ];
              if (this.orderData.length > 0) {
//                this.$message.success('获取订单图表数据成功！');
                this.orderData.unshift(['time', 'amount']);
                line.setOption({
//                  dataZoom: [
//                    {
//                      type: 'slider',
//                      show: true,
//                      xAxisIndex: [0],
//                      zoomLock: true,
//                      start:100 - ((5 / this.orderData.length) * 100) ,
//                      end: 100
//                    },
//                    {
//                      type: 'inside',
//                      xAxisIndex: [0],
//                      start: 100 - ((5 / this.orderData.length) * 100),
//                      end: 100
//                    }
//                  ],
                  dataset: {
                    source: this.orderData
                  },
                  series: [{
                    encode: {
                      x: 'time',
                      y: 'amount'
                    }
                  }]
                })
              }
            }
          },
          vo => {
            this.isLoading = false;
            this.$message.error('查询失败，请重试！');
          }
        )
      },

      getElementData () {
        this.$ajax.get(
          '/elementCount',
          null,
          vo => {
//            this.$message.success('获取要素数据成功！');
            let eleCount = vo.data
            this.cusEleCount = eleCount.CUSTOMER
            this.orgEleCount = eleCount.ORG
            this.perEleCount = eleCount.PERSONAL
            this.tangyinEleCount = eleCount.TANGYIN
            this.lijinEleCount = eleCount.LIJIN
          },
          vo => {
            this.isLoading = false;
            this.$message.error('查询失败，请重试！');
          }
        )
      }

    },
  }
</script>
<style lang="less">
  @import "../less/_dashboard.less";
</style>
