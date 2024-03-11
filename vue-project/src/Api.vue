<script setup>
import { onMounted, ref } from 'vue'
const count = ref(0);
function increment() {
    count.value++;
}
onMounted(() => {
    console.log('mounted')
})
</script>

<template>
    <button @click="increment">count: {{ count }}</button>
</template>