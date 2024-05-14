<template>
    <form action="/">
        <van-search
            v-model="searchText"
            show-action
            placeholder="请输入要搜索的标签"
            @search="onSearch"
            @cancel="onCancel"
        />
    </form>
    <van-divider content-position="left">已选标签</van-divider>
    <van-row gutter="16" style="padding: 0 10px">
        <div v-if="activeIds.length === 0">暂无数据</div>
        <van-col v-for="tag in activeIds">
            <van-tag :show="show" closeable size="medium" type="primary" @close="doClose(tag)">
                {{tag}}
            </van-tag>
        </van-col>
    </van-row>

    <van-divider content-position="left">选择标签</van-divider>
    <van-tree-select
    v-model:active-id="activeIds"
    v-model:main-active-index="activeIndex"
    :items="tagList"/>
    <div style="padding: 12px">
        <van-button  block type="primary" @click="doSearchResult">搜索</van-button>
    </div>
</template>

<script setup>
import { showToast } from 'vant';
import 'vant/es/toast/style';
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import { listTag } from '../api/tag'
const router = useRouter();
//用户选择的标签
const activeIds = ref([]);
// 一级选择的初始值
const activeIndex = ref(0);
//搜索的内容
const searchText = ref('')
// 标签列表
const tagList = ref([]);

// 搜索内容
const onSearch = (val) => {
    tagList.value = originTagList.map(parentTag => {
        const tempChildren = [...parentTag.children];
        const tempParentTag = {...parentTag};

        tempParentTag.children = tempChildren.filter(item => item.text.includes(searchText.value));
        return tempParentTag;
    })
    // activeIds.value = tagList.flatMap(parentTag => parentTag.children)
    // .filter(item => item.text.includes(searchText.value))
};
// 取消搜索
const onCancel = () => {
    searchText.value = '';
    tagList.value = originTagList;
};
// 点击删除标签
const doClose = (tag) => {
    activeIds.value = activeIds.value.filter(item => {
        return item !== tag;
    })
}
// 搜索用户列表
const doSearchResult = () => {
    router.push({
        path:"/user/list",
        query: {
            tags: activeIds.value
        }
    })
}

onMounted(()=>{
    // 搜索标签数据，展示所有标签
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
})
</script>

<style>

</style>