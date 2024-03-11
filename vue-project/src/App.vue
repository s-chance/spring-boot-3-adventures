<!-- <script>
export default{
  data() {
    return {
      msg: 'Hello Vue'
    }
  }
}
</script> -->
<script setup>
import { ref } from "vue";
// 调用 ref 函数，定义响应式数据
const msg = ref('Hello Vue');

// 导入 Api.vue 文件
import Api from './Api.vue';
// 导入 Article.vue 文件
import Article from './Article.vue';
</script>

<template>
  <!-- <h1>{{ msg }}</h1>
  <br>
  <Api/> -->
  <Article/>
</template>

<style scoped>
h1 {
  color: green;
}
</style>
