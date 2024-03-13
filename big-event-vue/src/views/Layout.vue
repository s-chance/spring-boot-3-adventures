<script setup>
import {
    CaretBottom,
    SwitchButton,
    EditPen,
    Crop,
    User,
    UserFilled,
    Promotion,
    Management,
} from '@element-plus/icons-vue'
import avatar from '@/assets/avatar.webp'
</script>

<template>
    <!-- element-plus 容器 -->
    <el-container class="layout-container">
        <!-- 左侧菜单 -->
        <el-aside width="200px">
            <div class="el-aside__logo"></div>
            <!-- element-plus 菜单 -->
            <el-menu active-text-color="#ffd04b" background-color="#232323" class="el-menu-vertical-demo"
                text-color="#fff" router>
                <el-menu-item index="1">
                    <el-icon>
                        <management />
                    </el-icon>
                    <span>文章分类</span>
                </el-menu-item>
                <el-menu-item index="2">
                    <el-icon>
                        <promotion />
                    </el-icon>
                    <span>文章管理</span>
                </el-menu-item>
                <el-sub-menu index="3">
                    <template #title>
                        <el-icon>
                            <user-filled />
                        </el-icon>
                        <span>个人中心</span>
                    </template>
                    <el-menu-item index="3-1">
                        <el-icon>
                            <user />
                        </el-icon>
                        <span>基本资料</span>
                    </el-menu-item>
                    <el-menu-item index="3-2">
                        <el-icon>
                            <crop />
                        </el-icon>
                        <span>更换头像</span>
                    </el-menu-item>
                    <el-menu-item index="3-3">
                        <el-icon>
                            <edit-pen />
                        </el-icon>
                        <span>重置密码</span>
                    </el-menu-item>
                </el-sub-menu>
            </el-menu>
        </el-aside>

        <el-container>
            <!-- 顶部区域 -->
            <el-header>
                <div>当前用户：<strong>admin</strong></div>
                <!-- 下拉菜单 -->
                <el-dropdown placement="bottom-end">
                    <span class="el-dropdown__box">
                        <el-avatar :src="avatar" />
                        <el-icon>
                            <caret-bottom />
                        </el-icon>
                    </span>
                    <template #dropdown>
                        <el-dropdown-menu>
                            <el-dropdown-item command="profile" :icon="User">基本资料</el-dropdown-item>
                            <el-dropdown-item command="avatar" :icon="Crop">更换头像</el-dropdown-item>
                            <el-dropdown-item command="password" :icon="EditPen">重置密码</el-dropdown-item>
                            <el-dropdown-item command="logout" :icon="SwitchButton">退出登录</el-dropdown-item>
                        </el-dropdown-menu>
                    </template>
                </el-dropdown>
            </el-header>
            <!-- 中间区域 -->
            <el-main>
                <div style="width: 1290px; height: 570px; border: 1px solid red;">
                    content
                </div>
            </el-main>
            <!-- 底部区域 -->
            <el-footer>big-event @2023</el-footer>
        </el-container>
    </el-container>
</template>


<style lang="scss" scoped>
.layout-container {
    height: 100vh;

    .el-aside {
        background-color: #232323;

        &__logo {
            height: 120px;
            background: url('@/assets/logo.jpg') no-repeat center / 120px auto;
        }

        .el-menu {
            border-right: none;
        }

        .el-header {
            background-color: #fff;
            display: flex;
            align-items: center;
            justify-content: space-between;
        }

        .el-dropdown__box {
            display: flex;
            align-items: center;

            .el-icon {
                color: #999;
                margin-left: 10px;
            }

            &:active,
            &:focus {
                outline: none;
            }
        }
    }

    .el-footer {
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 14px;
        color: #666;
    }
}
</style>