<template>
  <div class="register">
    <Header title="注册" />
    <div class="img">同程</div>

    <!-- 添加 van-toast 组件 -->
    <van-toast v-model:show="showToast" :message="toastMessage" />

    <van-form @submit="onSubmit">
      <van-field v-model="username" name="用户名" label="用户名" placeholder="用户名"
        :rules="[{ required: true, message: '请填写用户名' }]" />
      <van-field v-model="password" type="password" name="密码" label="密码" placeholder="密码"
        :rules="[{ required: true, message: '请填写密码' }]" />
      <div style="margin: 16px">
        <van-button round block type="info" native-type="submit" color="#72c699">
          注册
        </van-button>
        <van-button round block type="info" color="#72c699" class="register" @click="toLogin">
          已有账号去登录
        </van-button>
      </div>
    </van-form>
  </div>
</template>

<script>
import { reactive, toRefs } from "vue";
import Header from "../../components/Header.vue";
import { useRouter } from 'vue-router';
import { registerUser } from "@/services/api";

// 0=本地localStorage，1=API后端
const USE_API = 0;

export default {
  components: {
    Header,
  },
  setup() {
    const router = useRouter();
    let data = reactive({
      username: "",
      password: "",
      showToast: false,
      toastMessage: "",
    });

    // 显示Toast的方法
    const showToastMessage = (message) => {
      data.toastMessage = message;
      data.showToast = true;
      setTimeout(() => data.showToast = false, 2000);
    };

    // 提交按钮
    const onSubmit = async (values) => {
      try {
        const params = {
          username: values["用户名"],
          password: values["密码"]
        };

        if (USE_API) {
          // 使用API注册
          await registerUser(params);
          showToastMessage("注册成功");
          setTimeout(() => router.push("/login"), 1500);
        } else {
          // 使用本地localStorage注册
          const existingUsers = JSON.parse(localStorage.getItem("userList") || "[]");
          const usernameExists = existingUsers.some(
            user => user.username === params.username
          );
          if (usernameExists) {
            showToastMessage("该账号已存在");
            return;
          }
          existingUsers.push({
            username: params.username,
            password: params.password
          });
          localStorage.setItem("userList", JSON.stringify(existingUsers));
          showToastMessage("注册成功");
          setTimeout(() => router.push("/login"), 1500);
        }
      } catch (error) {
        if (error.response?.status === 400) {
          showToastMessage("无效请求：请检查输入格式");
        } else if (error.response?.status === 409) {
          showToastMessage("用户名已被注册");
        } else {
          showToastMessage("注册失败，请重试");
        }
      }
    };

    // 已有账号去登录
    const toLogin = () => {
      router.push('./login');
    };

    return {
      ...toRefs(data),
      onSubmit,
      toLogin,
    };
  },
};
</script>
<style lang='less' scoped>
.register {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background: linear-gradient(110deg, #f5f7fa 0%, #b9efd7 100%);


  .img {
    width: 300px;
    height: 300px;
    background-color: #65eca4;
    font-size: 80px;
    line-height: 300px;
    text-align: center;
    border-radius: 60px;
    margin: 40px auto;
    box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
  }

  .register {
    margin-top: 20px;
  }

  .van-form {
    background-color: white;
    border-radius: 30px;
    padding: 20px;
    margin: 0 20px;
    box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
  }

  :deep(.van-cell) {
    font-size: 20px;
    margin-bottom: 15px;
    border-radius: 15px;
    padding: 15px;
    background-color: #f8f9fa;
  }

  :deep(.van-button) {
    font-size: 25px;

    border-radius: 15px;
    height: 50px;
    line-height: 50px;

  }
}
</style>