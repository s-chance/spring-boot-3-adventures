<script setup>
import { Delete, Edit } from '@element-plus/icons-vue';
import { ref } from 'vue';

// 文章分类数据模型
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

// 搜索时使用的 categoryId
const categoryId = ref('')

// 搜索时使用的 state
const state = ref('')

// 文章列表数据模型
const articles = ref([
    // { id: 1, title: 'Vue3.0新特性', content: '', cover: '', state: '已发布', categoryId: 1, createTime: '2021-08-08' },
    // { id: 2, title: 'React新特性', content: '', cover: '', state: '已发布', categoryId: 1, createTime: '2021-08-08', updateTime: '021-08-08' },
    // { id: 3, title: 'Node.js实战', content: '', cover: '', state: '已发布', categoryId: 2, createTime: '2021-08-08', updateTime: '021-08-08' },
    // { id: 4, title: 'Mysql优化', content: '', cover: '', state: '已发布', categoryId: 3, createTime: '2021-08-08', updateTime: '021-08-08' },
    // { id: 5, title: 'Nginx配置', content: '', cover: '', state: '已发布', categoryId: 4, createTime: '2021-08-08', updateTime: '021-08-08' },
    // { id: 6, title: 'Jenkins实战', content: '', cover: '', state: '已发布', categoryId: 4, createTime: '2021-08-08', updateTime: '021-08-08' },
    // { id: 7, title: 'Jest测试框架', content: '', cover: '', state: '已发布', categoryId: 5, createTime: '2021-08-08', updateTime: '021-08-08' },
    // { id: 8, title: 'Web安全', content: '', cover: '', state: '已发布', categoryId: 6, createTime: '2021-08-08', updateTime: '021-08-08' },
    // { id: 9, title: '人工智能入门', content: '', cover: '', state: '已发布', categoryId: 7, createTime: '2021-08-08', updateTime: '021-08-08' },
    // { id: 10, title: '大数据实战', content: '', cover: '', state: '已发布', categoryId: 8, createTime: '2021-08-08', updateTime: '021-08-08' },
    // { id: 11, title: '云计算实战', content: '', cover: '', state: '已发布', categoryId: 9, createTime: '2021-08-08', updateTime: '021-08-08' },
    // { id: 12, title: '区块链入门', content: '', cover: '', state: '已发布', categoryId: 10, createTime: '2021-08-08', updateTime: '021-08-08' },
    // { id: 13, title: '物联网实战', content: '', cover: '', state: '已发布', categoryId: 11, createTime: '2021-08-08', updateTime: '021-08-08' }
])

// 分页条数据模型
const pageNum = ref(1) // 当前页码
const total = ref(13) // 总条数
const pageSize = ref(5) // 每页显示条数

// 分页条大小改变时触发
const onSizeChange = (size) => {
    pageSize.value = size
    articleList()
}
// 分页条页码改变时触发
const onCurrentChange = (num) => {
    pageNum.value = num
    articleList()
}

// 回显文章分类列表
import { articleCategoryListService, articleListService, articleAddService } from '@/api/article.js'
const articleCategoryList = async () => {
    let result = await articleCategoryListService()
    categorys.value = result.data
}
articleCategoryList()

// 获取文章列表
const articleList = async () => {
    let params = {
        pageNum: pageNum.value,
        pageSize: pageSize.value,
        categoryId: categoryId.value ? categoryId.value : null,
        state: state.value ? state.value : null
    }
    let result = await articleListService(params)

    // 渲染视图
    total.value = result.data.total
    articles.value = result.data.items

    // 扩展：处理分类名称
    for (let i=0;i<articles.value.length;i++) {
        let article = articles.value[i]
        for(let j=0;j<categorys.value.length;j++) {
            if (article.categoryId === categorys.value[j].id) {
                article.categoryName = categorys.value[j].categoryName
            }
        }
    }
}
articleList()
import { Plus } from '@element-plus/icons-vue'
import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css';
// 控制抽屉的显示与隐藏
const drawerVisible = ref(false)
// 添加表单数据模型
const articleModel = ref({
    title: '',
    content: '',
    cover: '',
    state: '',
    categoryId: ''
})

// 导入 token
import { useTokenStore } from '@/stores/token.js';
import { ElMessage } from 'element-plus';
const tokenStore = useTokenStore()

// 上传成功的回调函数
const uploadSuccess = (result) => {
    articleModel.value.cover = 'http://example.com'
    console.log(result.data)
}

// 添加文章
const addArticle = async (state) => {
    // 把发布状态赋值给数据模型
    articleModel.value.state = state

    // 调用接口
    let result = await articleAddService(articleModel.value)

    ElMessage.success(result.message?result.message:'添加成功')

    // 关闭抽屉
    drawerVisible.value = false

    // 刷新当前列表
    articleList()
}

// 清空模型的数据
const clearData = () => {
    articleModel.value = {
        title: '',
        content: '',
        cover: '',
        state: '',
        categoryId: ''
    }
}
</script>

<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>文章管理</span>
                <div class="extra">
                    <el-button type="primary" @click="drawerVisible = true;clearData()">添加文章</el-button>
                </div>
            </div>
        </template>
        <!-- 搜索菜单 -->
        <el-form inline>
            <el-form-item label="文章分类：">
                <el-select placeholder="请选择" v-model="categoryId" style="width: 200px">
                    <el-option v-for="c in categorys" :key="c.id" :label="c.categoryName" :value="c.id" />
                </el-select>
            </el-form-item>

            <el-form-item label="发布状态：">
                <el-select placeholder="请选择" v-model="state" style="width: 200px">
                    <el-option label="已发布" value="已发布" />
                    <el-option label="草稿" value="草稿" />
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="articleList">搜索</el-button>
                <el-button @click="categoryId='';state=''">重置</el-button>
            </el-form-item>
        </el-form>
        <!-- 文章列表 -->
        <el-table :data="articles" style="width: 100%">
            <el-table-column label="文章标题" width="400" prop="title" />
            <el-table-column label="分类" prop="categoryName" />
            <el-table-column label="发布时间" prop="createTime" />
            <el-table-column label="状态" prop="state" />
            <el-table-column label="操作" width="100">
                <template #default="{ row }">
                    <el-button :icon="Edit" circle plain type="primary" />
                    <el-button :icon="Delete" circle plain type="danger" />
                </template>
            </el-table-column>
        </el-table>
        <!-- 分页条 -->
        <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :page-sizes="[3,5,10,15]"
        layout="jumper, total, sizes, prev, pager, next" background :total="total" @size-change="onSizeChange"
        @current-change="onCurrentChange" style="margin-top: 20px; justify-content: flex-end" />

        <!-- 抽屉 -->
        <el-drawer v-model="drawerVisible" title="添加文章" direction="rtl" size="50%">
            <!-- 添加文章表单 -->
            <el-form :model="articleModel" label-width="100px">
                <el-form-item label="文章标题">
                    <el-input v-model="articleModel.title" placeholder="请输入文章标题" />
                </el-form-item>
                <el-form-item label="文章分类">
                    <el-select v-model="articleModel.categoryId" placeholder="请选择">
                        <el-option v-for="c in categorys" :key="c.id" :label="c.categoryName" :value="c.id" />
                    </el-select>
                </el-form-item>
                <el-form-item label="文章封面">
                    <!-- 
                        auto-upload: 设置是否自动上传
                        action: 设置服务器接口路径
                        name: 设置上传的文件字段名
                        on-success: 设置上传成功的回调函数
                     -->
                    <el-upload class="avatar-uploader" :auto-upload="true" :show-file-list="false"
                    action="/api/upload" name="file" :headers="{'Authorization': tokenStore.token }"
                    :on-success="uploadSuccess">
                        <img v-if="articleModel.cover" :src="articleModel.cover" class="avatar" />
                        <el-icon v-else class="avatar-uploader-icon">
                            <plus />
                        </el-icon>
                    </el-upload>
                </el-form-item>
                <el-form-item label="文章内容">
                    <div class="editor">
                        <quill-editor theme="snow" v-model:content="articleModel.content" contentType="html" />
                    </div>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="addArticle('已发布')">发布</el-button>
                    <el-button type="info" @click="addArticle('草稿')">草稿</el-button>
                </el-form-item>
            </el-form>
        </el-drawer>
    </el-card>
</template>

<style lang="scss" scoped>
.page-container {
    min-height: 100%;
    box-sizing: border-box;

    .header {
        display: flex;
        align-items: center;
        justify-content: space-between;
    }
}
/* 抽屉样式 */
.avatar-uploader {
    :deep() {
        .avatar {
            width: 178px;
            height: 178px;
            display: block;
        }

        .el-upload {
            border: 1px dashed var(--el-border-color);
            border-radius: 6px;
            cursor: pointer;
            position: relative;
            overflow: hidden;
            transition: var(--el-transition-duration-fast);
        }

        .el-upload:hover {
            border-color: var(--el-color-primary);
        }

        .el-icon.avatar-uploader-icon {
            font-size: 28px;
            color: #8c939d;
            width: 178px;
            height: 178px;
            text-align: center;
        }
    }
}
.editor {
    width: 100%;
    :deep(.ql-editor) {
        min-height: 200px;
    }
}
</style>