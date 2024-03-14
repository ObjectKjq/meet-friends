<template>
  <van-cell title="头像" is-link>
    <img :src="user.avatarUrl" style="height: 42px" alt="" srcset="">
  </van-cell>
  <van-cell title="昵称" is-link :value="user.username"/>
  <van-cell title="账号" is-link :value="user.userAccount"/>
  <van-cell title="性别" is-link @click="toEdit('gender', "性别", user.gender)" :value="user.gender"/>
  <van-cell title="电话" is-link :value="user.phone"/>
  <van-cell title="邮箱" is-link :value="user.email"/>
  <van-cell title="注册时间" is-link :value="时间"/>
</template>

<script setup lang="ts">
import {useRouter} from 'vue-router'
const router = useRouter();

const user = {
  id: 1,
  username: "kjq",
  userAccount: "qwe",
  avatarUrl: "https://qiniu-web-assets.dcloud.net.cn/unidoc/zh/unicloudlogo.png",
  gender: 1,
  phone: "qeqweqwewqe",
  email: "1221@qq.com",
  userStatus: 1,
  userRole: 1,
  tags: ['sdfs'],
  // createTime: new Date()
}

const toEdit = (editKey: string, editName: string, currentValue: number) => {
  router.push({
    path: "/user/edit",
    query: {
      editKey,
      currentValue,
      editName
    }
  })
}

</script>

<style>

</style>