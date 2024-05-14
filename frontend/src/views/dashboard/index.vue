<template>
  <div class="dashboard-container">
    <div class="dashboard-text">
      欢迎<span class="username-color">{{ name }}</span
      >，网站统计数据如下：
    </div>
    <div
      ref="charts"
      style="width: 800px; height: 400px; margin-top: 20px"
    ></div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import * as echarts from "echarts";
import { getNumber } from "@/api/user";

// 这里是引入 Vuex 的 mapGetters 方法
// 这里是引入 tubiao 图片

export default {
  name: "Dashboard",
  data() {
    return {
      userCount: [
        { value: 1048, name: "王五" },
        { value: 735, name: "李四" },
        { value: 580, name: "张三" },
        { value: 484, name: "李华" },
        { value: 300, name: "小明" },
      ],
    };
  },
  computed: {
    ...mapGetters(["name"]),
  },
  mounted() {
    // 发送请求获取数据
    this.getUserCount();
  },
  methods: {
    initChart() {
      let EChart = echarts.init(this.$refs.charts);
      // 配置参数
      let config = {
        title: {
          text: "昨天浏览次数最多的10名用户",
          left: "center",
        },
        tooltip: {
          trigger: "item",
        },
        legend: {
          orient: "vertical",
          left: "left",
        },
        series: [
          {
            name: "浏览次数",
            type: "pie",
            radius: "50%",
            data: this.userCount,
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: "rgba(0, 0, 0, 0.5)",
              },
            },
          },
        ],
      };
      // 设置参数
      EChart.setOption(config);
    },
    getUserCount() {
      getNumber().then((res) => {
        this.userCount = res.data;
        // 初始化图标
        this.initChart();
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
  }
  &-text {
    font-size: 20px;
    .username-color {
      color: #6b00c9;
    }
  }
}
</style>
