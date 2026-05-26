// 行程订单

<template>
  <div class="order">
    <div class="content">
      <div class="head">
        <img src="../../assets/order1.jpg" alt="" class="head1">
        <div class="slider">
          <h1 style="padding-left:10px"> 精选路线</h1>
          <div class="slider-content">
            <div v-for="(image, index) in images" :key="index" class="slider-card">
              <img :src="image.src" :alt="image.alt" class="slider-image">
              <div class="slider-sub">{{ image.alt }}</div>
            </div>
          </div>
        </div>
      </div>
      <div class="main">
        <h1>我的订单</h1>
        <div class="main-content">
          <!-- 固定订单 + 动态订单 -->
          <div v-for="(order, index) in orderList" :key="index">
            <router-link :to="order.router" class="order-item">
              <img :src="order.pic" class="order-image">
              <div class="order-info">
                <h3>{{ order.title }}</h3>
                <p>数量：{{ order.num }}</p>
                <p>订单ID：{{order.id}}</p>
                <p>总价：￥{{ (order.price * order.num).toFixed(2) }}</p>
              </div>
            </router-link>

          </div>
        </div>
      </div>

    </div>
    <Footer></Footer>
  </div>
</template>

<script>
import Footer from '@/components/Footer.vue'; // 引入底部导航组件

import { computed } from 'vue';
import { useStore } from 'vuex';

export default {
  data() {
    return {
      images: [
        { src: require("@/assets/order2.jpg"), alt: '洱海大理风花雪月' },
        { src: require("@/assets/order3.jpg"), alt: '广州经典三日游' },
        { src: require("@/assets/order4.jpg"), alt: '北京天坛' },
        { src: require("@/assets/order5.jpg"), alt: '魔都上海' },
        { src: require("@/assets/order6.jpg"), alt: '厦门烂漫黄昏' },
        { src: require("@/assets/order7.jpg"), alt: '内蒙呼伦贝尔' },
      ]
    }
  },
  setup() {
    const store = useStore();
    // 定义固定订单
    const fixedOrder = {
      id: 'fixed-001', // 唯一标识符
      title: '旅行团巡回演唱会门票',
      price: 299,
      num: 1,
      router: '/lvxingtuan',
      pic: require("@/assets/home21.jpg") // 确保图片路径正确
    };
    //computed属性合并固定订单和动态订单
    const mergedOrderList = computed(() => {
      return [fixedOrder, ...store.state.orderList];
    });


    return { orderList: mergedOrderList };
  },
  components: {
    Footer, // 注册底部导航组件
  },

};
</script>

<style lang='less' scoped>
.order {
  display: flex;
  flex-flow: column;
  height: 100%;
  // background-color: #b9efd7;
  background: linear-gradient(135deg, #f5f7fa 0%, #b9efd7 100%); // 更换柔和渐变背景
  min-height: 100vh;

  .content {
    flex: 1;
    overflow-y: auto;
    font-size: 12px;


    .head {

      .head1 {
        width: 100%;
        border-radius: 0 0 30px 30px;
      }

      .slider {
        background-color: #fff;
        padding: 10px;
        margin-top: 30px;
        border-radius: 30px;
        box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);

        h1 {
          color: #333;
          margin-bottom: 15px;
          border-bottom: 1px solid #eee; // 增加分割线
        }

      }

      .slider-content {
        width: 100%;
        overflow-x: scroll;// 横向滚动
        white-space: nowrap;// 强制内容在一行内显示
        -webkit-overflow-scrolling: touch;// 支持iOS平滑滚动
        display: inline-block;
        background-color: #fff;

        /* 自定义滚动条样式 */
        &::-webkit-scrollbar {
          height: 8px;
        }

        &::-webkit-scrollbar-track {
          background: #f1f1f1;
          border-radius: 4px;
        }

        &::-webkit-scrollbar-thumb {
          background: #888;
          border-radius: 4px;
        }

        &::-webkit-scrollbar-thumb:hover {
          background: #555;
        }

        /* 新增卡片样式 */
        .slider-card {
          display: inline-block;
          margin-right: 20px;
          width: 300px;
          /* 与图片宽度保持一致 */
          text-align: center;
          /* 整体居中对齐 */
        }

        .slider-sub {

          display: block;
          width: 300px;
          padding: 10px 0;
          font-size: 14px;
          color: #666;
          transition: color 0.3s ease;
          background-color: rgba(255, 255, 255, 0.8);
          /* 半透明背景 */
          border-radius: 0 0 8px 8px;
          /* 与图片底部圆角一致 */
        }

      }

      .slider-image {

        display: inline-block;
        width: 300px;
        height: 200px;
        object-fit: cover;
        margin-right: 20px;
        border-radius: 8px;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1); // 更流畅的动画

        &:hover {
          transform: translateY(-5px) scale(1.02); // 立体悬浮效果
          box-shadow: 0 12px 24px rgba(0, 0, 0, 0.1);
        }

      }

    }

    .main {
      background-color: #fff;

      height: 60%;
      border-radius: 30px;

      h1 {
        color: #333;
        margin: 25px;
        padding-top: 10px;

      }


      .order-item {
        display: flex;
        padding: 15px;
        margin: 10px 0;
        background-color: #fff;
        border-radius: 8px;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        border: 1px solid rgba(0,0,0,0.05); // 增加边框层次

        transition: all 0.3s ease;
        /* 平滑过渡效果 */

        &:hover {
          transform: translateY(-2px);
          /* 轻微上移 */
           box-shadow: 0 6px 20px rgba(46,203,105,0.1); // 绿色系阴影
          /* 加深阴影 */
          background-color: #f8f9fa;
          /* 悬停背景色 */
        }

        .order-image {
          width: 100px;
          height: 100px;
          object-fit: cover;
          border-radius: 6px;
          margin-right: 15px;
        }

        .order-info {
          flex: 1;

          h3 {
            margin: 0 0 10px 0; // 增加下边距
            color: #333; // 深灰色标题
            font-size: 18px; // 标题放大
            font-weight: 600; // 加粗
            line-height: 1.3; // 行高优化
          }

          p {
            margin: 8px 0; // 调整边距
            color: #666; // 正文灰色
            font-size: 18px; // 正文放大
            line-height: 1.2; // 行高优化
          }

          // 总价突出显示
          p:last-child {
            color: #2ecc71; // 红色强调
            font-weight: 520; // 加粗
            font-size: 20px; // 总价更大
          }
        }
      }


    }
  }
}
</style>
