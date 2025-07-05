<template>
    <Header title="泉州→南昌"></Header>
    <div class="main">
        
        <div class="jpg">
            <img src="../../assets/huoche1001-1.jpg" alt="ad1" id="ad1">
        </div>
        <div class="jpg">
            <img src="../../assets/huoche1001-2.jpg" alt="ad1" id="ad1"  @click="clickBuy1">
        </div>
        <div class="jpg">
            <img src="../../assets/huoche1001-3.jpg" alt="ad1" id="ad1" @click="clickBuy2">
        </div>
        <div class="jpg">
            <img src="../../assets/huoche1001-4.jpg" alt="ad1" id="ad1"  @click="clickBuy3">
        </div>
        <div class="jpg">
            <img src="../../assets/huoche1002-1.jpg" alt="ad1" id="ad1">
        </div>
        <div class="jpg">
            <img src="../../assets/huoche1002-2.jpg" alt="ad1" id="ad1" @click="clickBuy4">
        </div>
        <div class="jpg">
            <img src="../../assets/huoche1002-3.jpg" alt="ad1" id="ad1"  @click="clickBuy5">
        </div>
        <div class="jpg">
            <img src="../../assets/huoche1002-4.jpg" alt="ad1" id="ad1"  @click="clickBuy6">
        </div>
        <div class="jpg">
            <img src="../../assets/huoche1003.jpg" alt="ad1" id="ad1" >
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
                title: "泉州→南昌 G2046",
                price: 359, // 示例价格
                num: 1,
                router: "/huoche",
                pic: require("../../assets/huoche1004.jpg")
            };

            // 添加到购物车
            store.commit('ADDCART', [...store.state.cartList, concertItem]);

            // 直接结算生成订单
            store.commit('PAY', [concertItem]);

            // 跳转到订单页
            router.push("./order");
        }
        const clickBuy2 = () => {
            // 假设当前商品信息为 concertItem（需根据实际数据调整）
            const concertItem = {
                id: Date.now(), // 唯一ID
                title: "泉州→南昌 D2232",
                price: 195, // 示例价格
                num: 1,
                router: "/huoche",
                pic: require("../../assets/huoche1004.jpg")
            };

            // 添加到购物车
            store.commit('ADDCART', [...store.state.cartList, concertItem]);

            // 直接结算生成订单
            store.commit('PAY', [concertItem]);

            // 跳转到订单页
            router.push("./order");
        }  
        const clickBuy3 = () => {
            // 假设当前商品信息为 concertItem（需根据实际数据调整）
            const concertItem = {
                id: Date.now(), // 唯一ID
                title: "泉州→南昌 T8010",
                price: 105, // 示例价格
                num: 1,
                router: "/huoche",
                pic: require("../../assets/huoche1004.jpg")
            };

            // 添加到购物车
            store.commit('ADDCART', [...store.state.cartList, concertItem]);

            // 直接结算生成订单
            store.commit('PAY', [concertItem]);

            // 跳转到订单页
            router.push("./order");
        }
        const clickBuy4 = () => {
            // 假设当前商品信息为 concertItem（需根据实际数据调整）
            const concertItem = {
                id: Date.now(), // 唯一ID
                title: "泉州→南昌 D2376",
                price: 195, // 示例价格
                num: 1,
                router: "/huoche",
                pic: require("../../assets/huoche1004.jpg")
            };

            // 添加到购物车
            store.commit('ADDCART', [...store.state.cartList, concertItem]);

            // 直接结算生成订单
            store.commit('PAY', [concertItem]);

            // 跳转到订单页
            router.push("./order");
        }
        const clickBuy5 = () => {
            // 假设当前商品信息为 concertItem（需根据实际数据调整）
            const concertItem = {
                id: Date.now(), // 唯一ID
                title: "泉州→南昌 D6522",
                price: 194, // 示例价格
                num: 1,
                router: "/huoche",
                pic: require("../../assets/huoche1004.jpg")
            };

            // 添加到购物车
            store.commit('ADDCART', [...store.state.cartList, concertItem]);

            // 直接结算生成订单
            store.commit('PAY', [concertItem]);

            // 跳转到订单页
            router.push("./order");
        }
        const clickBuy6 = () => {
            // 假设当前商品信息为 concertItem（需根据实际数据调整）
            const concertItem = {
                id: Date.now(), // 唯一ID
                title: "泉州→南昌 G648",
                price: 261, // 示例价格
                num: 1,
                router: "/huoche",
                pic: require("../../assets/huoche1004.jpg")
            };

            // 添加到购物车
            store.commit('ADDCART', [...store.state.cartList, concertItem]);

            // 直接结算生成订单
            store.commit('PAY', [concertItem]);

            // 跳转到订单页
            router.push("./order");
        }
        return {

            clickBuy1,
            clickBuy2,
            clickBuy3,
            clickBuy4,
            clickBuy5,
            clickBuy6
        };
    }



    
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