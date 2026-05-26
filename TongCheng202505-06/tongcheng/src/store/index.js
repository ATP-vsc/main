//vuex全局数据仓库

import { createStore } from 'vuex';

export default createStore({
    state: {
        cartList: [], // 购物车列表
        orderList: [] // 订单列表
    },
    mutations: {
        // 添加商品到购物车
        ADDCART(state, items) {
            state.cartList = items;
        },
        // 结算生成订单
        PAY(state, items) {
            state.orderList = [...state.orderList, ...items];
            // 清空已结算的购物车商品（根据实际需求调整）
            state.cartList = state.cartList.filter(item => !items.includes(item));
        }
    }
});