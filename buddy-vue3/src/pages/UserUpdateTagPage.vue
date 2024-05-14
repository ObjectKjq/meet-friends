<template>
    <van-tree-select
    v-model:active-id="activeIds"
    v-model:main-active-index="activeIndex"
    :items="tagList"/>
    <van-button type="primary" @click="updateTag" block>提交</van-button>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import { listTag, getLoginUser, updateUserTag } from '../api/tag'
import { showSuccessToast } from 'vant';

//用户选择的标签
const activeIds = ref([]);
// 一级选择的初始值
const activeIndex = ref(0);
// 标签列表
const tagList = ref([]);
// 搜索标签数据，展示所有标签
const getTagList = () => {
    listTag().then((res)=>{
        let tagsList = [];
        res.data.forEach(tag => {
            let tagListChild = [];
            tag[1].forEach(tagChild=>{
                tagListChild.push({
                    text:tagChild.tagName,
                    id:tagChild.tagName
                })
            })
            tagsList.push({
                text:tag[0].tagName,
                children:tagListChild
            });
        });
        tagList.value = tagsList;
    })
}
// 修改用户的标签属性
const updateTag = async () => {
    const res = await updateUserTag({
        tags: JSON.stringify(activeIds.value)
    })
    if(res.code === 20000){
        showSuccessToast("修改成功")
    }
}

onMounted(()=>{
    getTagList();
    // 获取去当前用户的标签
    getLoginUser().then(res => {
        activeIds.value = JSON.parse(res.data.tags)
    })
})
</script>

<style>

</style>