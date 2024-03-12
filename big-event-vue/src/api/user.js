// 导入 request.js 请求工具
import request from '@/utils/request.js'

// 注册接口
export const userRegisterService = (registerData) => {
    // 借助 urlSearchParams 对象将对象转换为字符串
    const params = new URLSearchParams()
    for (let key in registerData) {
        params.append(key, registerData[key]);
    }
    request.post('/user/register', params)
}