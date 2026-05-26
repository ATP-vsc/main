// toast组件化，解决toast组件无法在setup中使用的问题

<template>
  <div class="login">
    <Header title="登陆" />
    <div class="img">同程</div>

    <!-- 添加 van-toast 组件 -->
    <van-toast v-model:show="showToast" :message="toastMessage" />
    <!-- onsubmit提交 -->
    <van-form @submit="onSubmit">
      <van-field v-model="username" name="用户名" label="用户名" placeholder="用户名"
        :rules="[{ required: true, message: '请填写用户名' }]" />
        <!--  -->
      <van-field v-model="password" type="password" name="密码" label="密码" placeholder="密码"
        :rules="[{ required: true, message: '请填写密码' }]" />

      <div style="margin: 16px">
        <van-button round block type="info" native-type="submit" color="#72c699">
          登录
        </van-button>
        <van-button round block type="info" color="#72c699" class="register" @click='toRegister'>
          注册
        </van-button>
      </div>
    </van-form>
  </div>
</template>

<script>
import Header from "../../components/Header.vue";
import { reactive, toRefs } from "vue";
import { useRouter } from 'vue-router';
import { loginUser } from "@/services/api";

// 0=本地localStorage，1=API后端
const USE_API = 0;

export default {
  components: { Header },
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
        const credentials = {
          username: values["用户名"],
          password: values["密码"]
        };

        if (USE_API) {
          // 使用后端API校验
          const response = await loginUser(credentials);// 调用API登录
          // 登录成功，保存状态
          let userInfo = response.data.user;
          if (!userInfo || !userInfo.username) {
            userInfo = { username: credentials.username };// 如果API没有返回用户信息，则使用输入的用户名
          }
          localStorage.setItem("isLogin", "1");
          localStorage.setItem("currentUser", JSON.stringify(userInfo));// 保存当前用户信息到localStorage
          showToastMessage("登录成功");
          setTimeout(() => router.push("./home"), 1500);// 跳转到首页timeout
        } else {
          // 使用本地localStorage校验
          const userList = JSON.parse(localStorage.getItem("userList") || "[]");// 从localStorage获取用户列表
          const matchedUser = userList.find(
            user => user.username === credentials.username && user.password === credentials.password
          );// 查找匹配的用户
          if (!matchedUser) {
            showToastMessage("用户名或密码错误");
            return;
          }
          localStorage.setItem("isLogin", "1");
          localStorage.setItem("currentUser", JSON.stringify(matchedUser));
          showToastMessage("登录成功");
          setTimeout(() => router.push("./home"), 1500);
        }
      } catch (error) {
        if (error.response?.status === 401) {
          showToastMessage("用户名或密码错误");
        } else if (error.response?.status === 404) {
          showToastMessage("账号不存在");
        } else {
          showToastMessage("登录失败，请重试");
        }
      }
    };

    // 转到注册页面
    const toRegister = () => {
      router.push("./register");
    };

    return {
      ...toRefs(data),
      onSubmit,
      toRegister
    };
  },
};
</script>

<style lang='less' scoped>
.login {
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