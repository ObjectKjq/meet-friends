<template>
    <van-nav-bar 
    title="标题" 
    left-arrow
    @click-left="onclickLeft"
    @click-right="onclickRight">
        <template #right>
            <van-icon name="search" size="18" />
        </template>
    </van-nav-bar>
    <div class="content">
        <router-view/>
    </div>
    <van-tabbar route @change="onChange">
        <van-tabbar-item to="/" icon="home-o" name="index">主页</van-tabbar-item>
        <van-tabbar-item to="/team" icon="chat-o" name="team">队伍</van-tabbar-item>
        <van-tabbar-item to="/user" icon="friends-o" name="user">个人</van-tabbar-item>
    </van-tabbar>
</template>

<script setup>
import { showToast } from 'vant';
import 'vant/es/toast/style';
import {useRouter} from 'vue-router'

const router = useRouter();

const onclickLeft = () => {
    router.back();
}
const onclickRight = () =>{
    router.push("/search")
}

const onChange = (index) => {
    // showToast(`标签 ${index}`)
};

</script>

<style>
.content{
  padding-bottom: 50px;
}
</style>