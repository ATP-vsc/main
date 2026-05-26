<template>
    <div class="home">
        <Header title="泉州到北京"></Header>
        <div class="content">
            <!-- 头部 -->
            <img src="../../assets/feijiheader.jpg" alt="" style="height:150px">
            <!-- 主要内容 -->
            <div class="main">

                <!-- 下部tab页 -->
                <div class="tabs">
                    <img src="../../assets/one.jpg" alt="" @click="$router.push('/feijiStore')">
                    <img src="../../assets/two.jpg" alt="" @click="$router.push('/feijiStore')">
                    <img src="../../assets/three.jpg" alt="" @click="$router.push('/feijiStore')">
                    <img src="../../assets/five.jpg" alt="" @click="$router.push('/feijiStore')">
                    <img src="../../assets/six.jpg" alt="" @click="$router.push('/feijiStore')">
                    <img src="../../assets/seven.jpg" alt="" @click="$router.push('/feijiStore')">
                    <img src="../../assets/eight.jpg" alt="" @click="$router.push('/feijiStore')">

                </div>
            </div>
        </div>

    </div>
</template>

<script>
import Header from "@/components/Header.vue";
import { reactive, toRefs } from "vue";

export default {
    components: {
        Header

    },
    setup() {
        let data = reactive({
            data: [
                {
                    pic: require('@/assets/one.jpg'),
                },

                {
                    pic: require('@/assets/two.jpg'),
                },
                {
                    pic: require('@/assets/three.jpg'),
                },
                {
                    pic: require("@/assets/five.jpg"),
                },
                {
                    pic: require("@/assets/six.jpg"),
                },
                {
                    pic: require("@/assets/seven.jpg"),
                },
                {
                    pic: require("@/assets/eight.jpg"),
                },
            ],
        });
        return {
            ...toRefs(data),
        };
    },
};
</script>

<style lang='less' scoped>
img {
    width: 100%;
    height: 100%;
}

.home {
    display: flex;
    flex-flow: column;
    height: 100%;
    font-size: 12px;

    .content {
        flex: 1;
        overflow-y: auto;
        .main {
            margin-top: -30px;

            .main_bg {
            
                padding: 10px 20px 0 20px;
                position: relative;
                border-radius: 30px 30px 0 0;  
            }
        }
    }
}
</style>
