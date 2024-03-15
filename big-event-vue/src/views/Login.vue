<script lang="ts" setup>
import { ElMessage } from 'element-plus'
import { User,Lock } from '@element-plus/icons-vue'
import { ref, reactive } from 'vue'
// 控制注册与登录表单的显示，默认显示登录表单
const isRegister = ref(false)
// 数据模型
const registerData = reactive({
    username: '',
    password: '',
    rePassword: ''
})

// 获取表单验证状态
const validate = ref(false)

// 校验密码是否一致
const checkRePassward = (rule, value, callback) => {
    if (value==='') {
        validate.value = false
        callback(new Error('请再次确认密码'))
    } else if (value !== registerData.password) {
        validate.value = false
        callback(new Error('两次输入密码不一致'))
    } else {
        validate.value = true
        callback()
    }
}

// 表单校验规则
const rules = {
    username: [
        { required: true, message: '请输入用户名', trigger: 'blur' },
        { min: 5, max: 16, message: '长度在 5 到 16 个字符', trigger: 'blur' }
    ],
    password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 5, max: 16, message: '长度在 5 到 16 个字符', trigger: 'blur' }
    ],
    rePassword: [
        { validator: checkRePassward, trigger: 'blur' }
    ]
}

import { userRegisterService, userLoginService } from '@/api/user'
// 调用注册接口
const register = async () => {
    /* if (result.code === 0) {
        // 成功
        console.log('注册成功')   
    } else {
        // 失败
        console.log('注册失败')
    } */
    // alert(result.message?result.message:'注册成功')
    if (validate.value === true) {
        let result = await userRegisterService(registerData);
        ElMessage.success(result.message ? result.message : '注册成功')
        // 切换到登录表单
        isRegister.value = false
    }
}

// 登录数据模型，复用注册数据模型
// 表单校验规则，复用注册表单校验规则
// 调用登录接口
import { useTokenStore } from '@/stores/token.js'
import { useRouter } from 'vue-router'
const router = useRouter()
const tokenStore = useTokenStore()
const login = async () => {
    let result = await userLoginService(registerData)
    /* if (result.code === 0) {
        console.log('登录成功')
        alert(result.message?result.message:'登录成功')
    } else {
        console.log('登录失败')
        alert(result.message?result.message:'登录失败')
    } */
    // alert(result.message?result.message:'登录成功')
    ElMessage.success(result.message?result.message:'登录成功')
    // 把 token 保存到 pinia 中
    tokenStore.setToken(result.data)
    // 路由跳转到首页
    router.push('/')
}
// 出于安全考虑，切换登录和注册时需要手动清空数据，也可以考虑使用独立的登录数据模型
const clearRegisterData = () => {
    registerData.username = ''
    registerData.password = ''
    registerData.rePassword = ''
}

</script>

<template>
    <el-row class="login-page">
        <el-col :span="12" class="bg"></el-col>
        <el-col :span="6" :offset="3" class="form">
            <!-- 注册表单 -->
            <el-form :model="registerData" label-width="auto" style="max-width: 600px" v-if="isRegister" :rules="rules">
                <el-form-item>
                    <h1>注册</h1>
                </el-form-item>
                <el-form-item prop="username">
                    <el-input :prefix-icon="User" v-model="registerData.username" placeholder="请输入用户名" />
                </el-form-item>
                <el-form-item prop="password">
                    <el-input :prefix-icon="Lock" v-model="registerData.password" type="password" placeholder="请输入密码" />
                </el-form-item>
                <el-form-item prop="rePassword">
                    <el-input :prefix-icon="Lock" v-model="registerData.rePassword" type="password" placeholder="请再次输入密码" />
                </el-form-item>
                <!-- 注册按钮 -->
                <el-form-item>
                    <el-button class="button" type="primary" @click="register" auto-insert-space>
                        注册
                    </el-button>
                </el-form-item>
                <el-form-item class="flex">
                    <el-link type="info" :underline="false" @click="isRegister = false; clearRegisterData()">
                        返回
                    </el-link>
                </el-form-item>
            </el-form>

            <!-- 登录表单 -->
            <el-form :model="registerData" label-width="auto" style="max-width: 600px" v-else :rules="rules" >
                <el-form-item>
                    <h1>登录</h1>
                </el-form-item>
                <el-form-item prop="username">
                    <el-input :prefix-icon="User" v-model="registerData.username" placeholder="请输入用户名" />
                </el-form-item>
                <el-form-item prop="password">
                    <el-input :prefix-icon="Lock" v-model="registerData.password" type="password" placeholder="请输入密码" />
                </el-form-item>
                <el-form-item class="flex">
                    <div class="flex">
                        <el-checkbox  label="Option 1" size="large" >记住我</el-checkbox>
                        <el-link type="primary" :underline="false">忘记密码？</el-link>
                    </div>
                </el-form-item>
                <!-- 登录按钮 -->
                <el-form-item>
                    <el-button class="button" type="primary" @click="login" auto-insert-space >登录</el-button>
                </el-form-item>
                <el-form-item class="flex">
                    <el-link type="info" :underline="false" @click="isRegister = true; clearRegisterData()">
                        注册
                    </el-link>
                </el-form-item>
            </el-form>
        </el-col>
    </el-row>
</template>

<style lang="scss" scoped>
/* 样式 */
.login-page {
    height: 100vh;
    background-color: #fff;

    .bg {
        background: url('@/assets/login-bg.jpg') no-repeat center / cover;
        border-radius: 0 20px 20px 0;
    }

    .form {
        display: flex;
        flex-direction: column;
        justify-content: center;
        user-select: none;

        .title {
            margin: 0 auto;
        }

        .button {
            width: 100%;
        }

        .flex {
            width: 100%;
            display: flex;
            justify-content: space-between;
        }
    }
}
</style>