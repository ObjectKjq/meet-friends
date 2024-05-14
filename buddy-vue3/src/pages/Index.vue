<template>
    <van-cell center title="心动模式">
        <template #right-icon>
            <van-switch v-model="isMatchMode" @update:model-value="onUpdateValue" :loading="loading" size="24" />
        </template>
    </van-cell>
    <user-card-list :user-list="userList" />
    <van-empty v-if="!userList ||userList.length === 0" image="search" description="网络异常" />
    <van-button :disabled="disabled" type="primary" @click="getUsersPage" v-if="!isMatchMode" block>{{disabled ? '没有更多内容' : '加载更多'}}</van-button>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import { recommendUsers, matchUsers } from "../api/tag"
import { showFailToast } from 'vant';

const disabled = ref(false);
// 用户列表
const userList = ref([]);
// 开关的状态
const loading = ref<boolean>(false);
// 是否开启心动模式
const isMatchMode = ref<boolean>(false);
// queryPage
const queryPage = ref({
    current: 1,
    pageSize: 5,
    total: 1,
})

onMounted(()=>{
    getUserPagelist();
})

// 根据分页查询更多数据
const getUsersPage = () => {
    getUserPagelist();
}

// 判断是否为心动模式
const onUpdateValue = (newValue) =>{
    loading.value = true;
    if(newValue){
        // 此时为心动模式，调用接口/user/match
        matchUsers().then( (res) => {
            userList.value = res.data;
            userList.value.forEach(user => {
                if(user.tags){
                    user.tags = JSON.parse(user.tags);
                }
            })
        })
    }else{
        // 此时为分页模式，调用接口/user/recommend
        userList.value = [];
        queryPage.value = {
            current: 1,
            pageSize: 5,
            total: 1,
        };
        getUserPagelist();
    }
    loading.value = false;
}
// 通用的分页方法
const getUserPagelist = () => {
    // 判断下一页有没有数据
    if((queryPage.value.current - 1) * queryPage.value.pageSize < queryPage.value.total){
        recommendUsers(queryPage.value).then((res)=>{
            const pageUsers = res.data.records;
            queryPage.value.total = res.data.total;
            pageUsers.forEach(user => {
                if(user.tags){
                    user.tags = JSON.parse(user.tags);
                }
            })
            userList.value = [...userList.value, ...pageUsers];
            queryPage.value.current++;
        })
    }else{
        disabled.value = true;
        showFailToast("已经是最后一页!");
    }
}
</script>

<style>

</style>