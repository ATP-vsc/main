<template>
  <div class="store_list">
    <div v-for="(i, index) in store_list" :key="index" class="store_list_item">
      <router-link :to="i.router">
        <DownTabItem :item="i" />
      </router-link>

    </div>
  </div>
</template>

<script>
import DownTabItem from "./DownTabItem.vue";

export default {
  props: ["store_list"],
  components: {
    DownTabItem,
  },
};
</script>

<style lang='less' scoped>
.store_list {
  display: flex;
  flex-wrap: wrap;
  display: grid; //网格分布
  grid-template-columns: repeat(2, 1fr);



  .store_list_item {
    margin-top: 0px;

  }
}
</style>
