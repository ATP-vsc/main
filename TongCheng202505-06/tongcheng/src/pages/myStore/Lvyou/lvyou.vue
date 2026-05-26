
<template>
  <div class="main">
    <Header title="旅游"></Header>

    <!-- 内容 -->
    <div class="content">
      <!-- 广告 -->
      <div class="ad">
        <van-swipe :autoplay="3000" lazy-render>
          <van-swipe-item v-for="image in adimages" :key="image">
            <img :src="image" />
          </van-swipe-item>
        </van-swipe>
      </div>
      <!-- 目的地 -->
      <div class="down">
        <van-tabs class="my-van-tabs">
          <van-tab v-for="(i, index) in destination_list" :title="i.tab" :key="index">
            <DownTab :store_list="i.data"></DownTab>
          </van-tab>
        </van-tabs>
      </div>
    </div>
  </div>
</template>


<script>
import Header from "@/components/Header.vue";
import DownTab from "@/pages/myHome/components/DownTab.vue";
import { lvyouData } from '@/data/lvyouData';
import { ref, reactive, toRefs } from "vue"; // 引入Vue的ref函数
export default {
  components: {
    Header,
    DownTab,
  },
  setup() {
    let data = reactive({
      adimages: [],
      destination_list: [],
    });
    const value = ref("");
    return { value, ...toRefs(data), ...lvyouData };
  },
};

</script>


<style lang="less" scoped>
.main {
  :deep(.van-tabs__wrap) {
    border-radius: 30px 30px 0 0;
    width: 100%;
    margin: 0 auto;
    height: 55px;
    margin-top: 15px;
  }

  :deep(.van-tabs__content) {
    width: 100%;

    margin: 0 auto;
    font-size: 20px;
  }

  :deep(span) {
    padding-top: 5px;
    font-size: 25px;

  }



  .content {
    flex: 1;
    overflow-y: auto;

    .ad {
      position: relative;
      overflow: hidden;

      img {
        display: block;
        width: 100%;
        height: 100%;
        object-fit: cover; // 确保图片覆盖容器，避免变形
        border-radius: inherit; // 继承容器的圆角值
      }
    }

    .dowm {
      
      width: 100%;
    }
  }
}
</style>
