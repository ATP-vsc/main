// 首页

<template>
  <!-- //主页全覆盖大类 -->
  <div class="home">
    <!-- 搜索框 -->
    <div class="search">
      <van-search v-model="value" background="#8cdeb9" shape="round" placeholder="请输入搜索关键词" @search="handleSearch" />
    </div>
    <!-- 图标加广告 -->
    <div class="content">

      <!-- 图标类别 -->
      <div class="classify">
        <!-- 大图标，需router -->
        <div class="big_classify">
          <div v-for="(i, index) in big_classify" :key="index">
            <li>
              <router-link :to="i.router">
                <svg class="icon" aria-hidden="true">
                  <use :xlink:href="`#${i.icon}`"></use>
                </svg>
              </router-link>
            </li>
            {{ i.name }}
          </div>
        </div>
        <!-- 小图标，无router -->
        <div class="small_classify">
          <div v-for="(i, index) in small_classify" :key="index">
            <svg class="icon" aria-hidden="true">
              <use :xlink:href="`#${i.icon}`"></use>
            </svg>
            {{ i.name }}
          </div>
        </div>
      </div>
      <!-- AD -->
      <div class="ad">
        <img src="../../assets/HomeJpg1.jpg" alt="ad1" id="ad1">
      </div>
      <!-- 下拉内容大类 -->
      <div class="dowm">
        <van-tabs class="my-van-tabs">
          <van-tab v-for="(i, index) in centent_nav_list" :title="i.tab" :key="index">
            <!-- DownTab -->
            <DownTab :store_list="i.data"></DownTab>
          </van-tab>
        </van-tabs>

      </div>
    </div>

    <Footer></Footer>
  </div>
</template>


<script>
import { ref, reactive, toRefs } from 'vue'; // 引入Vue的ref函数
import DownTab from './components/DownTab.vue';
import Footer from '@/components/Footer.vue';
import { useRouter } from 'vue-router';
import { homeData } from '@/data/homeData';

export default {
  components: {
    Footer,
    DownTab, // 注册底部导航组件
  },
  setup() {
    let data = reactive({
      big_classify: [],
      small_classify: [],
      centent_nav_list: [],
      hotels: [],
    });

    const value = ref('');
    const router = useRouter();

    const handleSearch = () => {
      // ，判断非空
      if (value.value.trim()) {
        router.push({
          path: '/search-results',
          query: { q: value.value }//向results页面传递搜索关键词
        });
      }
    };
    return { value, ...toRefs(data), handleSearch, ...homeData };
  },
};
</script>

<style lang="less" scoped>
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

.home {
  //fiex垂直
  display: flex;
  flex-flow: column;
  height: 100%;
  font-size: 12px;
  background-color: #8cdeb9;

  .van-search {
    --van-search-input-height: 50px;
    /* 修改输入框高度 */
    --van-search-input-height: 50px;

    /* 新增 placeholder 文字大小设置 */
    :deep(.van-field__control)::placeholder {
      font-size: 20px;
      /* 设置你想要的字体大小 */
      color: #666;
      /* 可选：设置 placeholder 颜色 */
    }
  }


  .content {
    flex: 1;
    overflow-y: auto;



    .classify {
      font-size: 16px;
      background-image: linear-gradient(#fff, #f8f8f8);
      padding: 10px 20px 0px 20px;
      padding-top: 30px;
      position: relative; //相对
      border-radius: 30px 30px 0 0;
      padding-bottom: 40px;


      .big_classify {
        display: flex;
        flex-wrap: wrap;


        div {
          flex: 1; //铺满页面
          display: flex;
          justify-content: center;
          flex-flow: column;
          align-items: center;


          .icon {
            width: 65px;
            height: 65px;
            margin-bottom: 5px;
            background-color: #b8efd6;
            border-radius: 10px;
          }
        }
      }

      .small_classify {
        display: flex;
        flex-wrap: wrap; //换行
        margin-top: 10px;

        div {
          display: flex;
          justify-content: center;
          flex-flow: column;
          align-items: center;
          width: 20%;

          .icon {
            width: 55px;
            height: 55px;
            margin: 20px 10px 10px 10px;
          }
        }
      }

    }

    .ad {
      overflow: hidden; //隐藏溢出
      border-radius: 0px 0px 20px 20px;

      img {
        width: 100%;
        height: 100%;
        border-radius: 0px 0px 30px 30px;
      }
    }

    .dowm {
      width: 100%;
    }


  }

}
</style>