<template>
  <van-form @submit="onSubmit">
    <van-cell-group inset>
      <van-field
        v-model="editUser.currentValue"
        :name="editUser.editKey"
        :label="editUser.editName"
        placeholder="用户名"
        :rules="[{ required: true, message: '请填写用户名' }]"
      />
    </van-cell-group>
    <div style="margin: 16px;">
      <van-button round block type="primary" native-type="submit">
        提交
      </van-button>
    </div>
  </van-form>
</template>

<script setup>
import {useRoute, useRouter} from 'vue-router'
import {ref} from 'vue'
import { getLoginUser, updateUser } from "../api/tag"
import { showFailToast } from 'vant';

const route = useRoute();
const reuter = useRouter();
const editUser = ref({
  editKey: route.query.editKey,
  currentValue: route.query.currentValue,
  editName: route.query.editName,
})

const username = ref('');
const onSubmit = async (values) => {
  const res = await getLoginUser();
  if(!res || res == undefined){
    showFailToast("没有登陆!")
  }
  // 发送请求修改信息
  updateUser({
    id: res.data.id,
    [editUser.value.editKey]: editUser.value.currentValue,
  })
  reuter.back();
};

</script>

<style>

</style>