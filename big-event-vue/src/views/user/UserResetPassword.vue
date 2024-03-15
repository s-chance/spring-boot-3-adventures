<script setup>
import { ref } from 'vue'

const passwordModel = ref({
    oldPassword: '',
    newPassword: '',
    confirmPassword: ''
})
// 校验新密码是否与旧密码相同
const checkNewPassward = (rule, value, callback) => {
    if (value === '') {
        callback(new Error('请输入新密码'))
    } else if (value === passwordModel.value.oldPassword) {
        callback(new Error('新密码不能与旧密码相同'))
    } else {
        callback()
    }
}

// 校验确认密码是否与新密码相同
const checkRePassward = (rule, value, callback) => {
    if (value === '') {
        callback(new Error('请再次确认密码'))
    } else if (value !== passwordModel.value.newPassword) {
        callback(new Error('两次输入密码不一致'))
    } else {
        callback()
    }
}

const rules = {
    oldPassword: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 5, max: 16, message: '长度在 5 到 16 个字符', trigger: 'blur' }
    ],
    newPassword: [
        { required: true, message: '请输入新密码', trigger: 'blur' },
        { min: 5, max: 16, message: '长度在 5 到 16 个字符', trigger: 'blur' },
        { validator: checkNewPassward, trigger: 'blur' }
    ],
    confirmPassword: [
        { required: true, message: '请再次确认密码', trigger: 'blur' },
        { min: 5, max: 16, message: '长度在 5 到 16 个字符', trigger: 'blur' },
        { validator: checkRePassward, trigger: 'blur' }
    ]
}

import { useTokenStore } from '@/stores/token.js'
import useUserInfoStore from '@/stores/userInfo.js'
import { useRouter } from 'vue-router'
import { userPasswordUpdateService } from '@/api/user.js';
import { ElMessage, ElMessageBox } from 'element-plus';
const tokenStore = useTokenStore()
const userInfoStore = useUserInfoStore()
const router = useRouter()
// 修改密码
const updatePwd = () => {
    ElMessageBox.confirm('确认修改密码吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
    }).then(async () => {
        // 确认修改

        // 为了适配接口，将属性名修改为接口需要的属性名
        passwordModel.value.old_pwd = passwordModel.value.oldPassword
        passwordModel.value.new_pwd = passwordModel.value.newPassword
        passwordModel.value.re_pwd = passwordModel.value.confirmPassword
        // 调用接口
        let result = await userPasswordUpdateService(passwordModel.value)

        // 清除 token
        tokenStore.removeToken()
        // 清除用户信息
        userInfoStore.removeInfo()
        // 跳转到登录页
        router.push('/login')

        ElMessage.success(result.message ? result.message : '密码修改成功,请重新登录')
    }).catch(() => {
        // 取消修改
        ElMessage.info(result.message? result.message:'取消修改')
    })
}
</script>

<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>重置密码</span>
            </div>
        </template>
        <el-row>
            <el-col :span="12">
                <el-form :model="passwordModel" :rules="rules" label-width="100px" size="large">
                    <el-form-item label="旧密码" prop="oldPassword">
                        <el-input v-model="passwordModel.oldPassword" type="password" placeholder="请输入旧密码" />
                    </el-form-item>
                    <el-form-item label="新密码" prop="newPassword">
                        <el-input v-model="passwordModel.newPassword" type="password" placeholder="请输入新密码" />
                    </el-form-item>
                    <el-form-item label="确认新密码" prop="confirmPassword">
                        <el-input v-model="passwordModel.confirmPassword" type="password" placeholder="请再次输入新密码" />
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="updatePwd">提交修改</el-button>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-row>
    </el-card>
</template>

<style scoped>
</style>