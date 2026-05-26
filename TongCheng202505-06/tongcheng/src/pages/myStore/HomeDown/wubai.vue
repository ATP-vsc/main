<template>
    <div class="main">
        <Header title="伍佰演唱会"></Header>
        <div class="jpg">
            <img src="../../../assets/home3028.jpg" alt="ad1" id="ad1">
        </div>
        <div class="store">
            <van-action-bar>
                <van-action-bar-icon icon="chat-o" text="客服" @click="service" />
                <van-action-bar-icon icon="cart-o" text="购物车" @click="toCart" />

                <van-action-bar-button type="danger" text="立即购买" @click="clickBuy" />
            </van-action-bar>

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

        // 客服的点击
        const service = () => {
            router.push("./order")
        };
        // 跳转购物车
        const toCart = () => {
            router.push("./order");
        };

        const clickBuy = () => {
            // 假设当前商品信息为 concertItem（需根据实际数据调整）
            const concertItem = {
                id: Date.now(), // 唯一ID
                title: "伍佰巡回演唱会门票",
                price: 480, // 示例价格
                num: 1,
                router: "/wubai",
                pic: require("@/assets/home26.jpg")
            };

            // 添加到购物车
            store.commit('ADDCART', [...store.state.cartList, concertItem]);

            // 直接结算生成订单
            store.commit('PAY', [concertItem]);

            // 跳转到订单页
            router.push("./order");
        };

        return {

            service,
            toCart,
            clickBuy,
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