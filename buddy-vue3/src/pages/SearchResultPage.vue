<template>
     <user-card-list :user-list="userList" />
    <van-empty v-if="!userList ||userList.length === 0" image="search" description="没有内容" />
</template>

<script setup>
import { onMounted, ref } from "vue";
import { useRoute } from "vue-router";
import { searchUsersByTags } from "../api/tag"

const route = useRoute();
// 获取搜索参数
const { tags } = route.query;
// 用户列表
const userList = ref([]);

onMounted(()=>{
    searchUsersByTags(tags).then((res)=>{
        userList.value = res.data;
        userList.value.forEach(user => {
            if(user.tags){
                user.tags = JSON.parse(user.tags);
            }
        })
    })
})
</script>

<style>

</style>