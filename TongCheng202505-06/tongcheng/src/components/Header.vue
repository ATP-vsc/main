// 头部返回栏

<template>
    <div class="header">
        <van-icon name="arrow-left" class="icon" size="20" @click="toBack"/>
        <div>{{ title }}</div>
    </div>
</template>
  
<script>
import { useRouter } from "vue-router";
export default {
    props: ["title"],
    setup(){
        const router=useRouter();
        const toBack=()=>{
            router.back();
        };
        return{
            toBack,
        }
    }
};
</script>  
  
<style lang='less' scoped>
.header {
    background-color: #fff;
    height: 70px;
    font-size: 30px;
    display: flex;    
    justify-content: center;
    align-items: center;
    border-bottom: 1px solid #d7d7d7;
    .icon {
        position: absolute;
        left: 10px;       
    }
}
</style>
