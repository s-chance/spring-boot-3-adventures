<script setup>
import { Edit, Delete } from '@element-plus/icons-vue'
import { ref } from 'vue'
const categorys = ref([
    // { id: 1, categoryName: '前端', categoryAlias: 'web', createTime: '2021-08-08', updateTime: '2021-08-08' },
    // { id: 2, categoryName: '后端', categoryAlias: 'server', createTime: '2021-08-08', updateTime: '2021-08-08' },
    // { id: 3, categoryName: '数据库', categoryAlias: 'db', createTime: '2021-08-08', updateTime: '2021-08-08' },
    // { id: 4, categoryName: '运维', categoryAlias: 'ops', createTime: '2021-08-08', updateTime: '2021-08-08' },
    // { id: 5, categoryName: '测试', categoryAlias: 'test', createTime: '2021-08-08', updateTime: '2021-08-08' },
    // { id: 6, categoryName: '安全', categoryAlias: 'security', createTime: '2021-08-08', updateTime: '2021-08-08' },
    // { id: 7, categoryName: '人工智能', categoryAlias: 'ai', createTime: '2021-08-08', updateTime: '2021-08-08' },
    // { id: 8, categoryName: '大数据', categoryAlias: 'bigdata', createTime: '2021-08-08', updateTime: '2021-08-08' },
    // { id: 9, categoryName: '云计算', categoryAlias: 'cloud', createTime: '2021-08-08', updateTime: '2021-08-08' },
    // { id: 10, categoryName: '区块链', categoryAlias: 'blockchain', createTime: '2021-08-08', updateTime: '2021-08-08' },
    // { id: 11, categoryName: '物联网', categoryAlias: 'iot', createTime: '2021-08-08', updateTime: '2021-08-08' }
])
// 声明异步函数调用文章列表查询函数
import { articleCategoryListService, articleCategoryAddService } from '@/api/article.js'
const articleCategoryList = async() => {
    let result = await articleCategoryListService()
    console.log(result.data)
    categorys.value = result.data
}
articleCategoryList();
// 控制添加分类弹窗可见性
const dialogVisible = ref(false)

// 添加分类数据模型
const categoryModel = ref({
    categoryName: '',
    categoryAlias: ''
})
// 添加分类表单校验规则
const rules = {
    categoryName: [
        { required: true, message: '请输入分类名称', trigger: 'blur' },
    ],
    categoryAlias: [
        { required: true, message: '请输入分类别名', trigger: 'blur' },
    ]
}
import { ElMessage } from 'element-plus'; 
// 调用添加分类接口
const addCategory = async () => {
    // 调用接口
    let result = await articleCategoryAddService(categoryModel.value)
    ElMessage.success(result.message?result.message:'添加成功')

    // 调用获取所有文章分类的函数
    articleCategoryList()
    // 关闭弹窗
    dialogVisible.value = false
}
</script>

<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>文章分类</span>
                <div class="extra">
                    <el-button type="primary" @click="dialogVisible = true">添加分类</el-button>
                </div>
            </div>
        </template>
        <el-table :data="categorys" style="width: 100%">
            <el-table-column label="序号" width="100" type="index" />
            <el-table-column label="分类名称" prop="categoryName" />
            <el-table-column label="分类别名" prop="categoryAlias" />
            <el-table-column label="操作" width="100">
                <template #default="{ row }">
                    <el-button :icon="Edit" circle plain type="primary" />
                    <el-button :icon="Delete" circle plain type="danger" />
                </template>
            </el-table-column>
            <template #empty>
                <el-empty description="暂无数据" />
            </template>
        </el-table>
        <!-- 添加分类弹窗 -->
        <el-dialog v-model="dialogVisible" title="添加文章分类" width="30%">
            <el-form :model="categoryModel" :rules="rules" label-width="100px" style="padding-right: 30px">
                <el-form-item label="分类名称" prop="categoryName">
                    <el-input v-model="categoryModel.categoryName" minlength="1" maxlength="10" />
                </el-form-item>
                <el-form-item label="分类别名" prop="categoryAlias">
                    <el-input v-model="categoryModel.categoryAlias" minlength="1" maxlength="15" />
                </el-form-item>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="dialogVisible = false">取消</el-button>
                    <el-button type="primary" @click="addCategory">确认</el-button>
                </span>
            </template>
        </el-dialog>
    </el-card>
</template>

<style scoped>
.page-container {
    min-height: 100%;
    box-sizing: border-box;

    .header {
        display: flex;
        align-items: center;
        justify-content: space-between;
    }
}
</style>