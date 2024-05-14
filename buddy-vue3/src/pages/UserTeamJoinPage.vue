<template>
  <div id="teamPage">
    <van-search v-model="searchText" placeholder="搜索队伍" @search="onSearch" />
    <team-card-list :teamList="teamList" />
    <van-empty v-if="teamList?.length < 1" description="数据为空"/>
  </div>
</template>

<script setup lang="ts">

import {useRouter} from "vue-router";
import TeamCardList from "../components/TeamCardList.vue";
import {onMounted, ref} from "vue";
import { listMyJoinTeams } from '../api/tag'

const router = useRouter();
// 搜索的内容
const searchText = ref('');
// 创建list数据
const teamList = ref([]);

/**
 * 搜索队伍
 * @param val
 * @returns {Promise<void>}
 */
const listTeam = async (val = '') => {
    const res = await listMyJoinTeams({
      searchText: val,
      pageNum: 1,
    })
    if (res?.code === 20000) {
        teamList.value = res.data;
    } else {
        teamList.value = res.data;
    }
}


// // 页面加载时只触发一次
onMounted( () => {
  listTeam();
})

const onSearch = (val) => {
  listTeam(val);
};

</script>

<style scoped>
#teamPage {

}
</style>
