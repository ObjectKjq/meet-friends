<template>
  <van-cell title="头像" is-link>
    <img :src="user.avatarUrl" style="height: 42px" alt="" srcset="">
  </van-cell>
  <van-cell title="昵称" is-link @click="toEdit('username', '昵称', user.username)" :value="user.username"/>
  <van-cell title="账号" is-link @click="toEdit('userAccount', '账号', user.userAccount)" :value="user.userAccount"/>
  <van-cell title="性别" is-link @click="toEdit('gender', '性别', user.gender)" :value="user.gender == 0 ? '男' : '女'"/>
  <van-cell title="电话" is-link @click="toEdit('phone', '电话', user.phone)" :value="user.phone"/>
  <van-cell title="邮箱" is-link @click="toEdit('email', '邮箱', user.email)" :value="user.email"/>
  <van-cell title="注册时间" is-link :value="user.createTime"/>
  <van-cell title="介绍" is-link @click="toEdit('profile', '介绍', user.profile)" :value="user.profile"/>
  <van-cell title="我的标签" is-link to="/user/update/tag"/>
</template>


<script setup lang="ts">
import {useRouter} from 'vue-router'
import { getLoginUser } from "../api/tag"
import {ref, onMounted} from 'vue'
import {UTCTime} from '../utils/UTCTime'

const router = useRouter();
const user = ref({})

const toEdit = (editKey, editName, currentValue) => {
  router.push({
    path: "/user/edit",
    query: {
      editKey,
      currentValue,
      editName
    }
  })
}

onMounted(()=>{
    getLoginUser().then((res)=>{
      user.value = res.data;
      const time = UTCTime(user.value.createTime)
      user.value.createTime = time;
    })
})
    

</script>

<style scoped>

</style>