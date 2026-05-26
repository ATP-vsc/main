<template>
  <div class="item" > 
    <img :src="item.pic" alt="" />
    <div class="item_right">
      <div class="title">{{ item.title }}</div>
      <div class="sales">{{ item.sales }}</div>
      <div class="price">同程推荐@{{ item.price}} 浏览量</div>
      <div class="label">
        <div v-for="(i, index) in item.label" :key="index">{{ i }}</div>
      </div>
    </div>
  </div>
</template>

<script>

export default {
  props: ["item"],

};
</script>

<style lang='less' scoped>
.item:not(:first-child) {
  margin-top: 0px;
}

.item {
  
  background-color: #ffffff;
  width: 345px;
  padding-top: 30px;
  padding-left: 16px;
  padding-right: 15px;

  img {
    width: 310px;
    height: 400px;
    border-radius: 40px;
    padding-left: 15px;

  }

  .item_right {
    align-items: center;
    display: flex;
    flex-flow: column;
    justify-content: space-between;

    .title {
      font-size: 30px;
    }

    .label {

      display: flex;

      div {
        background-color: #ffc40047;
        padding: 2px 5px;
        border-radius: 5px;

      }

      div:not(:first-child) {
        margin-left: 5px;
      }
    }
  }
}
</style>
