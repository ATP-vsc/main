<!-- src/components/ProductList.vue -->
<script setup>
import { ref, onMounted } from 'vue';
import { getProducts } from '@/services/api';

const products = ref([]);
const error = ref(null);

onMounted(async () => {
  try {
    const response = await getProducts();
    products.value = response.data;
  } catch (err) {
    error.value = '数据加载失败';
    console.error('API错误:', err);
  }
});
</script>

<template>
  <div v-if="error" class="error">{{ error }}</div>
  
  <ul v-else>
    <li v-for="product in products" :key="product.id">
      {{ product.name }} - ¥{{ product.price }}
    </li>
  </ul>
</template>