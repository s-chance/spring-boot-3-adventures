// 定制请求实例

// 导入 axios 
import axios from "axios";

import { ElMessage } from 'element-plus'
// 定义公共前缀变量 baseURL
// const baseURL = 'http://localhost:8080';
const baseURL = '/api';
const instance = axios.create({ baseURL });

import { useTokenStore } from '@/stores/token.js'
// 添加请求拦截器
instance.interceptors.request.use(
    (config) => {
        // 请求前的回调
        // 添加 token
        const tokenStore = useTokenStore()
        // 判断有没有 token
        if (tokenStore.token) {
            config.headers.Authorization = tokenStore.token;
        }
        return config
    },
    (err) => {
        // 请求错误的回调
        return Promise.reject(err);
    }
)

// 由于模块加载机制，这里不能直接使用 useRouter
// import { useRouter } from 'vue-router'
// const router =  useRouter()
// 可以直接导入 router 实例
import router from '@/router'

// 添加响应拦截器
instance.interceptors.response.use(
    result => {
        // 判断业务状态码
        if (result.data.code === 0) {
            // 业务成功
            return result.data;
        }

        // 业务失败
        // alert(result.data.message?result.data.message:'请求失败，请稍后重试');
        ElMessage.error(result.data.message?result.data.message:'请求失败，请稍后重试');
        // 将异步状态改为失败状态，方便被 catch 捕获
        return Promise.reject(result.data);
        
    },
    err => {
        // 判断响应状态码，如果为 401 则证明未登录，提示请登录并跳转到登录页面
        if (err.response.status === 401) {
            // 未登录
            ElMessage.error('请先登录')
            router.push('/login')
        } else {
            ElMessage.error(result.data.message?result.data.message:'请求失败，请稍后重试');
        }
        return Promise.reject(err); // 将异步状态改为失败状态，方便被 catch 捕获
    }
)

// 暴露请求实例
export default instance;