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