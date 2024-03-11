<script setup>
import { articleListService,articleSearchService } from '@/api/article.js'
import { onMounted,ref } from "vue";
// 定义文章列表响应式数据
const articleList = ref([]);
// 获取文章列表数据
const getArticleList = async () => {
    let data = await articleListService();
    articleList.value = data;
}
// 需要手动调用函数获取数据
getArticleList();
// 定义搜索条件响应式数据
const searchConditions = ref({
    category: '',
    state: ''
})
// 定义搜索函数
const search = async () => {
    // 文章搜索
    let data = await articleSearchService({...searchConditions.value});
    articleList.value = data;
}
</script>

<template>
    文章分类：<input type="text" v-model="searchConditions.category">
    发布状态：<input type="text" v-model="searchConditions.state">
    <button @click="search">搜索</button>
    <table border="1 solid" colspa="0" cellspacing="0">
        <tr>
            <th>文章标题</th>
            <th>分类</th>
            <th>发布时间</th>
            <th>状态</th>
            <th>操作</th>
        </tr>

        <tr v-for="(article,index) in articleList">
            <td>{{ article.title }}</td>
            <td>{{ article.category }}</td>
            <td>{{ article.time }}</td>
            <td>{{ article.state }}</td>
            <td>
                <button>编辑</button>
                <button>删除</button>
            </td>
        </tr>

    </table>
</template>