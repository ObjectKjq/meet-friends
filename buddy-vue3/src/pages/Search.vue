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
</template>

<script setup>
import { showToast } from 'vant';
import 'vant/es/toast/style';
import { ref } from 'vue';
//用户选择的标签
const activeIds = ref([]);
// 一级选择的初始值
const activeIndex = ref(0);
//搜索的内容
const searchText = ref('')

const originTagList = [
    {
        text: '浙江',
        children: [
        { text: '杭州', id: '杭州' },
        { text: '温州', id: '温州' },
        { text: '宁波', id: '宁波' },
        ],
    },
    {
        text: '江苏',
        children: [
        { text: '南京', id: '南京' },
        { text: '无锡', id: '无锡' },
        { text: '徐州', id: '徐州' },
        ],
    }
]

const tagList = ref(originTagList);

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
</script>

<style>

</style>