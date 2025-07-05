<template>
  <div class="mine">
    <!-- 添加 van-toast 组件 -->
    <van-toast v-model:show="showToast" :message="toastMessage" position="top" />

    <div class="content">
      <!-- 用户信息卡片 -->
      <div class="user-card">
        <!-- 头像区域 -->
        <div class="avatar-container">
          <img :src="user.avatar" alt="用户头像" class="avatar">

        </div>

        <!-- 用户信息 -->
        <div class="user-info">
          <div v-if="isLogin">
            <h2 class="username">{{ user.name }}</h2>
            <p class="level">VIP会员 | 等级: {{ user.level }}</p>
          </div>

        </div>
      </div>

      <!-- 功能入口 -->
      <div class="features">
        <div class="feature-item" @click="navigateTo('/order')">
          <van-icon name="orders-o" size="24" color="#72c699" />
          <span>我的订单</span>
          <van-icon name="arrow" size="16" />
        </div>
        <div class="feature-item" @click="navigateTo('/staff')">
          <van-icon name="star-o" size="24" color="#72c699" />
          <span>我的客服</span>
          <van-icon name="arrow" size="16" />
        </div>
        <div class="feature-item" @click="navigateTo('/coupon')">
          <van-icon name="coupon-o" size="24" color="#72c699" />
          <span>优惠券</span>
          <van-icon name="arrow" size="16" />
        </div>
      </div>

      <!-- 退出登录按钮 -->
      <div class="logout-container">
        <van-button v-if="isLogin" round block type="primary" color="#72c699" @click="logout" class="logout-btn">
          退出登录
        </van-button>
        <van-button v-else round block type="primary" color="#72c699" @click="$router.push('/login')"
          class="logout-btn">
          立即登录
        </van-button>
      </div>
    </div>
    <div class="worker">
      <pre> @前端暑假工</pre>
      <pre>Class:230901</pre>
      <pre>-李强—杨凯-朱子浩-陈伟涵-王耀坤-林飞杨-</pre>
    </div>
        
    <Footer></Footer>
  </div>
</template>

<script>
import Footer from '@/components/Footer.vue';
import { ref } from 'vue';
import { useRouter } from 'vue-router';

export default {
  components: {
    Footer
  },
  setup() {
    const router = useRouter();
    const showToast = ref(false);
    const toastMessage = ref('');
    const isLogin = ref(!!localStorage.getItem('isLogin'));
    // 获取当前用户信息
    let currentUser = null;
    try {
      currentUser = JSON.parse(localStorage.getItem('currentUser'));// 从localStorage获取当前用户信息
    } catch (e) {
      currentUser = null;
    }
    const user = ref({
      name: isLogin.value && currentUser && currentUser.username ? currentUser.username : '游客',
      avatar: require('@/assets/default-avatar.jpg'),
      level: '黄金'
    });


    // 显示Toast消息
    const showToastMessage = (message) => {
      toastMessage.value = message;
      showToast.value = true;
      setTimeout(() => showToast.value = false, 2000);
    };

    // 退出登录
    const logout = () => {
      localStorage.removeItem('isLogin');
      localStorage.removeItem('currentUser');
      showToastMessage('已退出登录');
      isLogin.value = false;
      user.value.name = '游客';
    };

    // 导航功能
    const navigateTo = (path) => {
      if (!isLogin.value) {
        showToastMessage('请先登录');
        router.push('/login');
      } else {
        router.push(path);
      }
    };



    return {
      isLogin,
      user,
      showToast,
      toastMessage,
      logout,
      navigateTo,
    
    };
  }
};
</script>

<style lang="less" scoped>

.mine {
  display: flex;
  flex-flow: column;
  height: 100%;
  background: linear-gradient(135deg, #f5f7fa 0%, #b9efd7 100%);

  .content {
    flex: 1;
    overflow-y: auto;
    padding: 20px;
    padding-top: 60px;
  }

  .user-card {
    display: flex;
    align-items: center;
    background: white;
    border-radius: 20px;
    padding: 25px;
    margin-bottom: 45px;
    box-shadow: 0 6px 15px rgba(114, 198, 153, 0.2);
  }

  .avatar-container {
    position: relative;
    width: 80px;
    height: 80px;
    margin-right: 20px;

    .avatar {
      width: 100%;
      height: 100%;
      border-radius: 50%;
      object-fit: cover;// 保持图片比例
      border: 3px solid #72c699;// 边框颜色
    }

    .avatar-overlay {
      position: absolute;
      bottom: 0;
      right: 0;
      background: rgba(114, 198, 153, 0.8);
      width: 30px;
      height: 30px;
      border-radius: 50%;// 圆形
      display: flex;
      align-items: center;
      justify-content: center;
    }
  }

  .user-info {
    flex: 1;// 占据剩余空间

    .username {
      font-size: 24px;
      color: #333;
      margin-bottom: 8px;
      font-weight: bold;
    }

    .level,
    .tip {
      font-size: 16px;
      color: #72c699;
    }

    .tip {
      color: #999;
    }
  }

  .features {
    background: white;
    border-radius: 20px;
    overflow: hidden;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
    margin-bottom: 55px;

    .feature-item {
      display: flex;
      align-items: center;
      padding: 20px;
      font-size: 18px;
      border-bottom: 1px solid #f0f0f0;
      transition: background 0.3s;

      &:last-child {
        border-bottom: none;
      }

      &:hover {
        background: #f9f9f9;
      }

      span {
        flex: 1;
        margin-left: 15px;
        color: #333;
      }
    }
  }

  .logout-container {
    padding: 0 15px;

    .logout-btn {
      font-size: 25px;
      height: 50px;
      font-weight: bold;
      letter-spacing: 2px;// 字符间距
      box-shadow: 0 4px 10px rgba(114, 198, 153, 0.3);
    }
  }
.worker {
   
    bottom: 0;
    left: 0;
    width: 100%;
    text-align: center;
    padding: 10px 0;
    font-size: 20px;

    pre {
      margin: 0;
      line-height: 1.5;
      font-family: 'Courier New', Courier, monospace;
      
    }
  }
}
</style>