<template>
  <van-empty v-if="user.username==null" description="你没有登录快去登录吧!">
    <van-button round type="primary" @click="toLogin" class="bottom-button">去登录</van-button>
  </van-empty>
  <div v-else>
    <van-cell title="当前用户" :value="user?.username" />
    <van-cell title="修改信息" is-link to="/user/update" />
    <van-cell title="我创建的队伍" is-link to="/user/team/create" />
    <van-cell title="我加入的队伍" is-link to="/user/team/join" />
    <van-cell @click="logout" title="退出登录" is-link/>
  </div>
</template>


<script setup lang="ts">
import {useRouter} from 'vue-router'
import { getLoginUser, logOut } from "../api/tag"
import {ref, onMounted} from 'vue'
import {UTCTime} from '../utils/UTCTime'
import { showSuccessToast } from 'vant';

const router = useRouter();
const user = ref({})

onMounted(()=>{
    getLoginUser().then((res)=>{
      user.value = res.data;
      const time = UTCTime(user.value.createTime)
      user.value.createTime = time;
    })
})

const toLogin = () => {
  router.replace("/login")
}

const logout = () => {
  logOut().then(()=>{
    showSuccessToast('退出成功');
    router.replace("/login")
  })
}
    

</script>

<style scoped>
.bottom-button {
    width: 160px;
    height: 40px;
  }
</style>