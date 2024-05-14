<template>
  <div id="teamAddPage">
    <van-form @submit="onSubmit">
      <van-cell-group inset>
      <van-field
            v-model="addTeamData.name"
            name="name"
            label="队伍名"
            placeholder="请输入队伍名"
            :rules="[{ required: true, message: '请输入队伍名' }]"
        />
        <van-field
            v-model="addTeamData.description"
            rows="4"
            autosize
            label="队伍描述"
            type="textarea"
            placeholder="请输入队伍描述"
        />
        <van-field
            is-link
            readonly
            name="datetimePicker"
            label="过期时间"
            :placeholder="addTeamData.expireTime ?? '点击选择过期时间'"
            @click="showPicker = true"
        />
        <van-popup v-model:show="showPicker" position="bottom">
          <van-date-picker
              v-model="addTeamData.expireTime"
              @confirm="showPicker = false"
              type="datetime"
              title="请选择过期时间"
              :min-date="minDate"
          />
        </van-popup>
        <van-field name="radio" label="队伍状态">
          <template #input>
            <van-radio-group v-model="addTeamData.status" direction="horizontal">
              <van-radio name="0">公开</van-radio>
              <van-radio name="1">私有</van-radio>
              <van-radio name="2">加密</van-radio>
            </van-radio-group>
          </template>
        </van-field>
        <van-field
            v-if="Number(addTeamData.status) === 2"
            v-model="addTeamData.password"
            type="password"
            name="password"
            label="密码"
            placeholder="请输入队伍密码"
            :rules="[{ required: true, message: '请填写密码' }]"
        />
      </van-cell-group>
      <div style="margin: 16px;">
        <van-button round block type="primary" native-type="submit">
          提交
        </van-button>
      </div>
    </van-form>
  </div>
</template>

<script setup lang="ts">

import {useRoute, useRouter} from "vue-router";
import {onMounted, ref} from "vue";
import { showSuccessToast, showFailToast } from 'vant';
import { getTeamById, updateTeam } from "../api/tag";
import {UTCTime} from "../utils/UTCTime"

const router = useRouter();
const route = useRoute();

// 展示日期选择器
const showPicker = ref(false);
const a = ref("0")
const minDate = new Date();

// 获取队伍的id
const id = route.query.id;

// 需要用户填写的表单数据
const addTeamData = ref({})

// 获取之前的队伍信息
onMounted(async () => {
  if (id <= 0) {
    showFailToast('加载队伍失败');
    return;
  }
  const res = await getTeamById(id);
  if (res.code === 20000) {
    addTeamData.value = res.data;
    // 转化成字符串
    addTeamData.value.status += '';
    // 转化时间格式
    const time = UTCTime(addTeamData.value.expireTime);
    addTeamData.value.expireTime = time.split("-")
  }
})

// 提交
const onSubmit = async () => {
  const postData = {
    ...addTeamData.value,
    status: Number(addTeamData.value.status),
    expireTime: new Date(addTeamData.value.expireTime)
  }
  // 前端参数校验
  const res = await updateTeam(postData);
  if (res.code === 20000){
    showSuccessToast('更新成功');
    router.push({
      path: '/team',
      replace: true,
    });
  }
}
</script>

<style scoped>
#teamPage {

}
</style>
