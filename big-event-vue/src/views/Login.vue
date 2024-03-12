<script lang="ts" setup>
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

// 校验密码是否一致
const checkRePassward = (rule, value, callback) => {
    if (value==='') {
        callback(new Error('请再次确认密码'))
    } else if (value !== registerData.password) {
        callback(new Error('两次输入密码不一致'))
    } else {
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

// 调用注册接口
import { userRegisterService } from '@/api/user'
const register = async () => {
    let result = await userRegisterService(registerData);
    if (result.code === 0) {
        // 成功
        console.log('注册成功')   
    } else {
        // 失败
        console.log('注册失败')
    }
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
                    <el-link type="info" :underline="false" @click="isRegister = false">
                        返回
                    </el-link>
                </el-form-item>
            </el-form>

            <!-- 登录表单 -->
            <el-form :model="registerData" label-width="auto" style="max-width: 600px" v-else >
                <el-form-item>
                    <h1>登录</h1>
                </el-form-item>
                <el-form-item>
                    <el-input :prefix-icon="User" v-model="registerData.username" placeholder="请输入用户名" />
                </el-form-item>
                <el-form-item>
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
                    <el-button class="button" type="primary" @click="onSubmit" auto-insert-space >登录</el-button>
                </el-form-item>
                <el-form-item class="flex">
                    <el-link type="info" :underline="false" @click="isRegister = true">
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