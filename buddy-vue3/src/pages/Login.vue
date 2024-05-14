<template>
    <van-form @submit="onSubmit">
        <van-field
            v-model="userAccount"
            name="用户名"
            label="用户名"
            placeholder="用户名"
            :rules="[{ required: true, message: '请填写用户名' }]"
        />
        <van-field
            v-model="userPassword"
            type="password"
            name="密码"
            label="密码"
            placeholder="密码"
            :rules="[{ required: true, message: '请填写密码' }]"
        />
        <div style="margin: 16px;">
            <van-button round block type="primary" native-type="submit">
            登录
            </van-button>
        </div>
    </van-form>
</template>

<script setup lang="ts">
import { login } from '../api/tag'
import { showFailToast, showSuccessToast } from 'vant';
import { ref } from "vue";
import { useRouter } from "vue-router";

const router = useRouter();
const userAccount = ref('');
const userPassword = ref('');

const onSubmit = async () => {
    login({
        userAccount:userAccount.value,
        userPassword:userPassword.value
    }).then(res => {
        if(res.code === 20000){
            showSuccessToast('登录成功');
            router.replace("/")
        }else{
            showFailToast('用户名或密码错误');
        }
    })
    
}
</script>

<style>

</style>