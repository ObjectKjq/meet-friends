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
              :min-date="minDate"
              @confirm="showPicker = false"
              @cancel="showPicker = false"
              type="datetime"
              :formatter="formatter"
              title="请选择过期时间"
          />
        </van-popup>
        <van-field name="stepper" label="最大人数">
          <template #input>
            <van-stepper v-model="addTeamData.maxNum" max="10" min="3"/>
          </template>
        </van-field>
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
import { ref } from "vue";
import {addTeam} from '../api/tag'
import { showSuccessToast } from 'vant';


const initFormData = {
  "name": "",
  "description": "",
  "expireTime": [],
  "maxNum": 3,
  "password": "",
  "status": 0,
}
// 需要用户填写的表单数据
const addTeamData = ref({...initFormData})
// 是否展示日期选择器
const showPicker = ref(false);
const minDate = new Date();
// 提交
const onSubmit = async () => {
  const postData = {
    ...addTeamData.value,
    status: Number(addTeamData.value.status),
    expireTime: new Date(addTeamData.value.expireTime)
  }
  // 这里可以做数据的校验
  const res = await addTeam(postData)
  if(res.code === 20000){
    showSuccessToast("添加成功")
  }
}

const formatter = (type, option) => {

  return option;
};
</script>

<style scoped>
#teamPage {

}
</style>
