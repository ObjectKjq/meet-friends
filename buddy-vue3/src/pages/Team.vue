<template>
  <div id="teamPage">
    <van-search v-model="searchText" placeholder="搜索队伍" @search="onSearch" />
    <van-tabs v-model:active="active" @change="onTabChange">
      <van-tab title="公开" name="public" />
      <van-tab title="加密" name="private" />
    </van-tabs>
    <div style="margin-bottom: 16px" />
    <van-button class="add-button" type="primary" icon="plus" @click="toAddTeam" />
    <!-- 队伍的列表 -->
    <team-card-list :teamList="teamList" />
    <!-- 当没有搜到队伍时显示 -->
    <van-empty v-if="teamList?.length < 1" description="数据为空"/>
  </div>
</template>

<script setup lang="ts">
import {useRouter} from "vue-router";
import TeamCardList from "../components/TeamCardList.vue";
import {onMounted, ref} from "vue";
import { listTeams } from "../api/tag";
import {Toast} from "vant";

// 用来判断是加密还是公开
const active = ref('public')
const router = useRouter();
// 搜索的内容
const searchText = ref('');
// 队伍列表
const teamList = ref([]);

const onTabChange = (name) => {
  if (name === 'public') {
    // 查询公开的列表
    listTeam(searchText.value, 0);
  } else {
    // 查寻加密的列表
    listTeam(searchText.value, 2);
  }
}
// 搜索队伍
const listTeam = async (val = '', status = 0) => {
  listTeams({
    searchText: val,
    current: 1,
    status,
  }).then((res)=>{
    teamList.value = res.data;
  }, error=>{
    teamList.value = [];
  })
}

// 页面加载时只触发一次
onMounted( () => {
  listTeam();
})

const onSearch = (val) => {
  listTeam(val);
};

// 跳转到创建队伍页
const toAddTeam = () => {
  router.push({
    path: "/team/add"
  })
}

</script>

<style scoped>
.add-button {
    position: fixed;
    bottom: 60px;
    width: 50px;
    right: 12px;
    height: 50px;
    border-radius: 50%;
    z-index: 1;
}
</style>