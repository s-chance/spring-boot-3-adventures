import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server: {
    proxy: {
      '/api': { // 获取路径中包含了 /api 的请求
        target: 'http://localhost:8080', //  转发的目标地址
        changeOrigin: true, // 支持跨域
        rewrite: (path) => path.replace(/^\/api/, '') // 重写路径，将其中的 /api 去掉
      }
    }
  }
})
