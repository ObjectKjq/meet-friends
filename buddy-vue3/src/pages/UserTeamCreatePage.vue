<template>
  <div id="teamPage">
    <van-search v-model="searchText" placeholder="搜索队伍" @search="onSearch" />
    <van-button type="primary" @click="doJoinTeam" block>创建队伍</van-button>
    <team-card-list :teamList="teamList" />
    <van-empty v-if="teamList?.length < 1" description="数据为空"/>
  </div>
</template>

<script setup lang="ts">

import {useRouter} from "vue-router";
import TeamCardList from "../components/TeamCardList.vue";
import {onMounted, ref} from "vue";
import { listMyCreateTeams } from '../api/tag'

const router = useRouter();
// 搜索的内容
const searchText = ref('');
// 创建队伍的列表
const teamList = ref([]);

// // 跳转到加入队伍页
const doJoinTeam = () => {
  router.push({
    path: "/team/add"
  })
}

/**
 * 搜索队伍
 */
const onSearch = (val) => {
  listTeam(val);
};

/**
 * 搜索队伍
 * @param val
 * @returns {Promise<void>}
 */
const listTeam = async (val = '') => {
    const res = await listMyCreateTeams({
        searchText: val,
        current: 1,
    })
    if (res?.code === 20000) {
        teamList.value = res.data;
    } else {
        teamList.value = [];
    }
}

// // 页面加载时只触发一次
onMounted( () => {
  listTeam();
})

</script>

<style scoped>
#teamPage {

}
</style>
