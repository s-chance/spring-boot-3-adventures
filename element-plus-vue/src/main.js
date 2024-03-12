import { createApp } from 'vue' // 导入 vue
import ElementPlus from 'element-plus' // 导入 element-plus
import 'element-plus/dist/index.css' // 导入 element-plus 的样式
import App from './App.vue' // 导入 App.vue
import locale from 'element-plus/dist/locale/zh-cn.js'

const app = createApp(App) // 创建一个 vue 实例

app.use(ElementPlus,{locale}) // 使用 element-plus
app.mount('#app') // 挂载到 id 为 app 的元素上