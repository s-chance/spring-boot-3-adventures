/* // 导入 axios 
import axios from "axios";
// 定义公共前缀变量 baseURL
const baseURL = 'http://127.0.0.1:4523/m1/4126110-0-default';
const instance = axios.create({ baseURL }); */

import request from "@/util/request.js";

// 获取所有文章数据
export function articleListService() {
    // 同步等待服务器响应的结果，并返回给调用者
    return request.get('/articleList');
}

// 根据文章分类和发布状态搜索数据
export function articleSearchService(conditions) {
    return request.get('/search', { params: conditions });
}