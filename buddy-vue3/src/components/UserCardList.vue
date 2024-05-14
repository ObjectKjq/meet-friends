<template>
    <van-card
    v-for="user in userList"
    :desc="user.profile"
    :title="user.username"
    :thumb="user.avatarUrl">
        <template #tags>
            <van-tag style="margin:4px 8px 0 0" plain type="primary" v-for="tag in user.tags">
                {{tag}}
            </van-tag>
        </template>
        <template #footer>
            <van-button @click="showUser(user.id)" size="mini">联系我</van-button>
        </template>
    </van-card>
    <van-popup v-model:show="show" closeable round :style="{ padding: '30px'}">
        <van-cell :title="'用户名：'+ (atUser.username == null ? '空': atUser.username)" />
        <van-cell :title="'手机号：'+ (atUser.phone == null ? '空': atUser.phone)" />
        <van-cell :title="'邮箱：'+ (atUser.email == null ? '空': atUser.email)" />
    </van-popup>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import {UserType} from '../models/user'
interface UserCardListProps{
    userList:UserType[];
}

const atUser = ref({});
const show = ref(false);
const props = defineProps<UserCardListProps>();
const showUser = (id) => {
    show.value = true
    atUser.value = props.userList.find( user => {
        return user.id === id;
    });
}

</script>

<style>

</style>