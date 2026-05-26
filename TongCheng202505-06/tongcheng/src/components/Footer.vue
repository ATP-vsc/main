//底部导航栏

<template>
  <div class="footer">
    <div class="item">
      <router-link to="/home" class="nav_item" active-class="nav_color">
        <van-icon name="home-o" size="30" />
        首页
      </router-link>
    </div> 
    <div class="item">
      <router-link to="/travel" class="nav_item" active-class="nav_color">
        <van-icon name="guide-o" size="30" />
        去旅行
      </router-link>
    </div>
    <div class="item">
      <router-link to="/order" class="nav_item" active-class="nav_color">
        <van-icon name="notes-o" size="30" />
        行程
      </router-link>
    </div>
    <div class="item">
      <router-link to="/me" class="nav_item" active-class="nav_color">
        <van-icon name="user-circle-o" size="30" />
        我的
      </router-link>
    </div>
  </div>
</template>
<script>
export default {};
</script>

<style lang='less' scoped>
.footer {
  display: flex;
  font-size: 18px;
  background-color: #fff;
  border-top: 0.5px solid #eee;
  .item {
    flex: 1;
    display: flex;
    justify-content: center;
    .nav_color {
      color: #2dcb69;
    }
    .nav_item {
      display: flex;
      flex-flow: column;
      align-items: center;
    }
  }
}
</style>
