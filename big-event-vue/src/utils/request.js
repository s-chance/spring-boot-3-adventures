// 定制请求实例

// 导入 axios 
import axios from "axios";

import { ElMessage } from 'element-plus'
// 定义公共前缀变量 baseURL
// const baseURL = 'http://localhost:8080';
const baseURL = '/api';
const instance = axios.create({ baseURL });

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
        // alert('请求失败，请稍后重试');
        ElMessage.error(result.data.message?result.data.message:'请求失败，请稍后重试');
        return Promise.reject(err); // 将异步状态改为失败状态，方便被 catch 捕获
    }
)

// 暴露请求实例
export default instance;