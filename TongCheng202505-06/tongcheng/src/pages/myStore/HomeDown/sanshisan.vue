<template>
    <Header title="叁拾叁民宿"></Header>
    <div class="main">
        
        <div class="jpg">
            <img src="../../../assets/home3018.jpg" alt="ad1" id="ad1">
        </div>
        <div class="jpg">
            <img src="../../../assets/home3019-1.jpg" alt="ad1" id="ad1">
        </div>
        <div class="jpg">
            <img src="../../../assets/home3019-click1.jpg" alt="ad1" id="ad1" @click="clickBuy1">
        </div>
        <div class="jpg">
            <img src="../../../assets/home3019-2.jpg" alt="ad1" id="ad1">
        </div>
        <div class="jpg">
            <img src="../../../assets/home3019-click2.jpg" alt="ad1" id="ad1" @click="clickBuy2">
        </div>
        <div class="jpg">
            <img src="../../../assets/home3019-3.jpg" alt="ad1" id="ad1">
        </div>



    </div>

</template>
<script>
import Header from '@/components/Header.vue'


import { useStore } from 'vuex';
import { useRouter } from 'vue-router';

export default {
    components: {
        Header
    },
    setup() {
        const store = useStore();
        const router = useRouter();




        const clickBuy1 = () => {
            // 假设当前商品信息为 concertItem（需根据实际数据调整）
            const concertItem = {
                id: Date.now(), // 唯一ID
                title: "叁拾叁投影大床房",
                price: 202, // 示例价格
                num: 1,
                router: "/sanshisan",
                pic: require("@/assets/home3.png")
            };

            // 添加到购物车
            store.commit('ADDCART', [...store.state.cartList, concertItem]);

            // 直接结算生成订单
            store.commit('PAY', [concertItem]);

            // 跳转到订单页
            router.push("./order");
        };

        const clickBuy2 = () => {
            // 假设当前商品信息为 concertItem（需根据实际数据调整）
            const concertItem = {
                id: Date.now(), // 唯一ID
                title: "叁拾叁投影双床房",
                price: 299, // 示例价格
                num: 1,
                router: "/sanshisan",
                pic: require("@/assets/home3.png")
            };

            // 添加到购物车
            store.commit('ADDCART', [...store.state.cartList, concertItem]);

            // 直接结算生成订单
            store.commit('PAY', [concertItem]);

            // 跳转到订单页
            router.push("./order");
        };

        return {


            clickBuy1,
            clickBuy2
        };



    },




}
</script>
<style lang='less' scoped>
:deep(.van-action-bar-icon) {
    width: 20%;
    font-size: 20px;

}

:deep(.van-button__content) {
    background-color: #2e9667;
    border-radius: 30px;
    font-size: 20px;
}

.main {
    display: flex;
    flex-flow: column;
    height: 100%;
    background-color: #fff;
}

.jpg {
    width: 100%;

    justify-content: center;
    align-items: center;
}

#ad1 {
    width: 100%;
    height: 100%;
}
</style>