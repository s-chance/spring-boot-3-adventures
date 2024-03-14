// 定义 store
import { defineStore } from 'pinia'
import { ref } from 'vue'

/*
    第一个参数：store 的名字，唯一标识
    第二个参数：一个函数，返回一个对象，对象中包含了我们需要共享的数据和函数

    返回值：一个对象，包含了我们需要共享的数据和函数
*/
export const useTokenStore = defineStore('token', () => {
    // 定义状态的内容

    // 响应式变量
    const token = ref('')

    // 定义函数，用于修改状态
    const setToken = (newToken) => {
        token.value = newToken
    }

    // 定义函数，用于移除状态
    const removeToken = () => {
        token.value = ''
    }

    return {
        token, setToken, removeToken
    }
},{
    persist: true // 持久化存储
})