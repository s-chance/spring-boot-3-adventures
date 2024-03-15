// 导入 request.js 请求工具
import request from '@/utils/request.js'

// 注册接口
export const userRegisterService = (registerData) => {
    // 借助 urlSearchParams 对象将对象转换为字符串
    const params = new URLSearchParams()
    for (let key in registerData) {
        params.append(key, registerData[key]);
    }
    return request.post('/user/register', params)
    // 另一种写法
    // return request.post('/user/register', registerData, { headers: { 'Content-Type': 'application/x-www-form-urlencoded' }})
}

// 登录接口
export const userLoginService = (loginData) => {
    // 转换 x-www-form-urlencoded 格式
    const params = new URLSearchParams()
    for (let key in loginData) {
        params.append(key, loginData[key]);
    }
    return request.post('/user/login', params)
}

// 获取用户信息
export const userInfoService = () => {
    return request.get('/user/userInfo')
}

// 修改个人信息
export const userInfoUpdateService = (userInfoData) => {
    return request.put('/user/update', userInfoData)
}
// 修改头像
export const userAvatarUpdateService = (avatarUrl) => {
    const params = new URLSearchParams()
    params.append('avatarUrl', avatarUrl)
    return request.patch('/user/updateAvatar', params)
}

// 修改密码
export const userPasswordUpdateService = (passwordData) => {
    return request.patch('/user/updatePwd', passwordData)
}