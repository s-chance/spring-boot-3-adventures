import request from '@/utils/request.js'
import { useTokenStore } from '@/stores/token.js'
// 文章分类列表查询
export const articleCategoryListService = () => {
    // const tokenStore = useTokenStore()
    // 在 pinia 中定义的响应式数据不需要通过 .value 获取值
    // return request.get('/category', { headers: { 'Authorization': tokenStore.token } })
    return request.get('/category')
}
// 添加文章分类
export const articleCategoryAddService = (categoryData) => {
    return request.post('/category', categoryData)
}
// 修改文章分类
export const articleCategoryUpdateService = (categoryData) => {
    return request.put('/category', categoryData)
}
// 删除文章分类
export const articleCategoryDeleteService = (id) => {
    const data = { id: id }
    return request.delete('/category', { data })
}
// 文章列表查询
export const articleListService = (params) => {
    return request.get('/article', { params: params })
}
// 文章添加
export const articleAddService = (articleData) => {
    return request.post('/article', articleData)
}
// 文章修改
export const articleUpdateService = (articleData) => {
    return request.put('/article', articleData)
}