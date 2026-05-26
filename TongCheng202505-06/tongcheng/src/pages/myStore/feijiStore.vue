<template>
  <div class="feijiStore">
    <Header title="泉州--北京"></Header>
    <div class="container">
      <img src="../../assets/feijistore1001.jpg" alt="" >
      <img src="../../assets/feijistore1002click.jpg" alt="" @click="clickBuy(495)">
      <img src="../../assets/feijistore1003.jpg" alt="" >
      <img src="../../assets/feijistore1004click.jpg" alt="" @click="clickBuy(508)">
      <img src="../../assets/feijistore1005.jpg" alt="" @click="clickBuy(510)">
    </div>
  </div>
</template>

<script>
import Header from "@/components/Header.vue";
import { useStore } from 'vuex';
import { useRouter } from 'vue-router';

export default {
  components: {
    Header
  },
  setup() {
    const store = useStore();
    const router = useRouter();

    const clickBuy = (price) => {
      // 创建机票商品对象
      const flightItem = {
        id: Date.now(), // 唯一ID
        title: `泉州-北京机票`,
        price: price,
        num: 1,
        router: "/feijiStore",
        pic: require("@/assets/feijistorelogo.jpg") // 使用第一张图片作为商品图
      };

      // 添加到购物车
      store.commit('ADDCART', [...store.state.cartList, flightItem]);

      // 直接结算生成订单
      store.commit('PAY', [flightItem]);

      // 跳转到订单页
      router.push("/order");
    };

    return {
      clickBuy
    };
  }
};
</script>

<style>
.container {
  display: flex;
  flex-direction: column;
}

img {
  width: 100%;
  height: auto;
  display: block;
  cursor: pointer; /* 添加手型光标提示可点击 */
}
</style>