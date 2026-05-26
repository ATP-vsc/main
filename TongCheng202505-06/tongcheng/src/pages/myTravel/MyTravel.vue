<template>
  <div class="myTravel">
    <!-- 顶部导航 -->
    <div class="header">
      <div class="title">广场</div>
      <div class="location">泉州 ● 消息</div>
    </div>

    <!-- 主要内容区域 -->
    <div class="content">
      <!-- 看看别人怎么玩   大头标题 要加线 -->
      <div class="section">
        <div class="section-header">
          <div class="section-title">看看别人怎么玩</div>
          <div class="more">查看更多</div>
        </div>

        <!-- 小街巷卡片 -->
        <div class="feature-card">
          <div class="card-content">
            <!-- 灰tag -->
            <div class="card-tag">抓住好时机</div>
            <div class="card-title">【「五一来泉州南安小街巷」】巷弄烟火漫游指南</div>
            <!-- 地点 -->
            <div class="card-subtitle">泉州市 · 泉州开元寺</div>
          </div>
          <img src="../../assets/travel101.jpeg" alt="泉州南安小街巷" class="feature-image">
        </div>

        <!-- 用户分享列表 -->
        <div class="share-list">
          <!-- 评论循环 -->
          <div class="share-card" v-for="(card, index) in shareCards" :key="index">
            <!-- 用户 -->
            <div class="user-info">
              <img :src="card.avatar" class="avatar">
              <div class="user-name">{{ card.userName }}</div>
            </div>
            <!-- 内容 -->
            <div class="share-content">
              <div class="share-title">{{ card.title }}</div>
              <img :src="card.image" class="share-image">
            </div>
            <!-- 爱心动画，最重要！！！！用模板 -->
            <div class="interaction">
              <div class="likes" @click="toggleLike('share', index)">
                <van-icon :name="card.liked ? 'like' : 'like-o'" :class="card.liked ? 'liked-icon' : ''" size="16" />
                <!-- 点赞数 -->
                <span>{{ card.likes }}</span>
              </div>
              <div class="tags">
                <van-tag type="primary" v-for="(tag, tagIndex) in card.tags" :key="tagIndex">
                  {{ tag }}
                </van-tag>
              </div>
            </div>


          </div>
        </div>
      </div>

      <!-- 大头标题来泉州 -->
      <div class="section">
        <div class="section-header">
          <div class="section-title">五一来泉州·细品岁月静好</div>
          <div class="more">更多推荐</div>
        </div>

        <!-- 目的地卡片 -->
        <div class="destination-cards">
          <!-- 卡片循环 -->
          <div class="destination-card" v-for="(card, index) in destinationCards" :key="index">
            <!-- 卡片头加标签 -->
            <div class="card-header">
              <div class="card-title">{{ card.location }}</div>
              <div class="card-tag">{{ card.tag }}</div>
            </div>

            <img :src="card.image" :alt="card.title" class="destination-image">

            <div class="card-content">
              <div class="destination-title">{{ card.title }}</div>
              <!-- 用户 -->
              <div class="user-info">
                <img :src="card.userAvatar" alt="用户头像" class="small-avatar">
                <span>{{ card.userName }}</span>
                <!-- 爱心！！ -->
                <div class="likes" @click="toggleLike('destination', index)">
                  <van-icon :name="card.liked ? 'like' : 'like-o'" :class="card.liked ? 'liked-icon' : ''" size="14" />
                  <span>{{ card.likes }}</span>
                </div>

              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 底部导航 -->
    <Footer></Footer>
  </div>
</template>

<script>
import { ref, reactive, toRefs } from 'vue'; // 引入Vue的ref函数
import Footer from '@/components/Footer.vue';
import { Icon, Tag } from 'vant';

export default {
  components: {
    Footer,
    [Icon.name]: Icon,
    [Tag.name]: Tag,
  },
  setup() {
    // 数据
    let data = reactive({
      shareCards: [
        {
          avatar: require('../../assets/travel201.jpg'),
          userName: '同程会员_62F301D0...',
          title: '【五一穷游攻略】洛阳偃师：在烟火古韵里邂逅青春浪漫',
          image: require('../../assets/travel202.jpg'),
          likes: 14,
          tags: ['文化古迹', '美食探店'],
          liked: false // 点赞状态0和1
        },
        {
          avatar: require('../../assets/travel301.jpg'),
          userName: '同程会员_2DD267AE1...',
          title: '【五一来泉州南安小街巷】巷弄烟火漫',
          image: require('../../assets/travel302.jpg'),
          likes: 9,
          tags: ['摄影打卡', '街巷漫游'],
          liked: false
        }
      ],
      destinationCards: [
        {
          location: '泉州市 · 黄金海岸',
          tag: '自然风光',
          image: require('../../assets/travel1001.jpeg'),
          title: '五一来黄金海岸，细品岁月静好',
          userAvatar: require('../../assets/travel1002.jpg'),
          userName: '同程会员_5CD8B64...',
          likes: 12,
          liked: false
        },
        {
          location: '泉州市 · 石狮龟湖公园',
          tag: '历史文化',
          image: require('../../assets/travel1003.jpg'),
          title: '千年海丝梦未醒，火在石狮',
          userAvatar: require('../../assets/travel1004.jpg'),
          userName: '同程会员_1890A72D...',
          likes: 7,
          liked: false
        },
        {
          location: '泉州市 · 清源山风景区',
          tag: '自然风光',
          image: require('../../assets/travel1005.jpg'),
          title: '清源山云海仙境，五一打卡好去处',
          userAvatar: require('../../assets/travel1002.jpg'),
          userName: '同程会员_8A9B3C...',
          likes: 15,
          liked: false
        },
        {
          location: '泉州市 · 开元寺',
          tag: '文化古迹',
          image: require('../../assets/travel1007.jpg'),
          title: '千年古刹开元寺，五一祈福之旅',
          userAvatar: require('../../assets/travel201.jpg'),
          userName: '同程会员_4D5E6F...',
          likes: 18,
          liked: false
        },
        {
          location: '泉州市 · 崇武古城',
          tag: '历史文化',
          image: require('../../assets/travel1009.jpg'),
          title: '崇武古城：海防历史的活化石',
          userAvatar: require('../../assets/travel201.jpg'),
          userName: '同程会员_7G8H9I...',
          likes: 11,
          liked: false
        },
        {
          location: '泉州市 · 安平桥',
          tag: '世界遗产',
          image: require('../../assets/travel1011.jpg'),
          title: '中国现存最长古石桥：安平桥',
          userAvatar: require('../../assets/travel1004.jpg'),
          userName: '同程会员_1J2K3L...',
          likes: 9,
          liked: false // 新增点赞状态
        }
      ]
    });
    const value = ref('');
    return {
      value, ...toRefs(data)
    }

  },
  //  爱心动画
  methods: {
    //tybe:share or destination index:data
    toggleLike(type, index) {
      if (type === 'share') {
        const card = this.shareCards[index];//卡片对象
        card.liked = !card.liked;//切换liked 1or0
        card.likes += card.liked ? 1 : -1;//数字true:1
      } else if (type === 'destination') {
        const card = this.destinationCards[index];
        card.liked = !card.liked;
        card.likes += card.liked ? 1 : -1;
      }
    }
  }
};
</script>

<style lang="less" scoped>
// 点赞动画样式
.likes {

  transition: all 0.3s;

  &:active {
    transform: scale(0.95);
  }
}

.liked-icon {
  color: #ee0a24 !important;
  animation: likeScale 0.5s ease; //动画likescale
}

@keyframes likeScale {
  0% {
    transform: scale(1); //缩放倍数
  }

  30% {
    transform: scale(1.3);
  }

  50% {
    transform: scale(0.9);
  }

  70% {
    transform: scale(1.2);
  }

  100% {
    transform: scale(1);
  }
}




.myTravel {
  display: flex;
  flex-direction: column; //排列
  height: 100vh;
  background-color: #f8f8f8;
  overflow: hidden; //防溢出
  font-family: 'PingFang SC', 'Helvetica Neue', Arial, sans-serif;
}

//  <!-- 顶部导航 -->
.header {
  background-color: #099b59;
  color: white; //文字
  padding: 15px;
  text-align: center;
  border-radius: 0 0 30px 30px;
  position: relative;
  z-index: 10;

  .title {
    font-size: 22px;
    font-weight: bold;
    margin-bottom: 5px;
  }

  .location {
    font-size: 16px;
    opacity: 0.9;//透明度
  }
}

.content {
  flex: 1;
  overflow-y: auto;
  padding: 15px;
  padding-bottom: 70px;
}

// 大头标题那条线
.section {
  background-color: white;
  border-radius: 16px;
  padding: 16px;
  margin-bottom: 20px;


  .section-header {
    display: flex;
    justify-content: space-between; //主轴上均匀分布
    align-items: center;
    margin-bottom: 16px;

    .section-title {
      font-size: 20px;
      font-weight: bold;
      color: #333;
      position: relative; //相对定位
      padding-left: 12px;


    }

    .more {
      font-size: 18px;
      color: #8cdeb9;
    }
  }
}

.feature-card {
  position: relative; //定位
  border-radius: 12px;

  margin-bottom: 20px;
  height: 200px;

  .card-content {
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    padding: 16px;
    // 加一个黑色渐变
    background: linear-gradient(transparent, rgba(0, 0, 0, 0.7));
    color: white;
    z-index: 2; //图层控制

    //灰tag
    .card-tag {
      background-color: rgba(255, 255, 255, 0.2);

      display: inline-block;
      padding: 4px 10px;
      border-radius: 20px;
      font-size: 16px;
      margin-bottom: 8px;
    }

    .card-title {
      font-size: 18px;
      font-weight: bold; //加粗
      margin-bottom: 5px;
      line-height: 1.3;
    }

    // 地点开元寺
    .card-subtitle {
      font-size: 18px;
      opacity: 0.9; //透明度
    }
  }

  //开元寺图片
  .feature-image {
    width: 100%;
    height: 100%;
    object-fit: cover; //图片美观
    position: absolute;
    top: 0;
    left: 0;
  }
}

//分享
.share-list {
  .share-card {
    border-top: 1px solid #d1d1d1;
    padding: 14px 0;

    //删除第一天线
    &:first-child {
      border-top: none;
      padding-top: 0;
    }

    .user-info {
      display: flex;
      align-items: center;
      margin-bottom: 12px;

      .avatar {
        width: 40px;
        height: 40px;
        border-radius: 50%;
        margin-right: 10px;
        object-fit: cover;
      }

      .user-name {
        font-size: 16px;
        color: #666;
      }
    }

    .share-content {
      display: flex;

      .share-title {
        flex: 1;
        font-size: 22px;
        font-weight: 500;
        line-height: 1.4;
        margin-right: 12px;
      }

      .share-image {
        width: 100px;
        height: 100px;
        border-radius: 8px;
        object-fit: cover;
      }
    }

    .interaction {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-top: 12px;

      .likes {
        display: flex;
        align-items: center;
        color: #666;
        font-size: 14px;

        .van-icon {
          margin-right: 4px;
          transition: color 0.3s;
        }
      }

      .tags {
        .van-tag {
          margin-left: 8px;
          background-color: #e6f7ff;
          height: 30px;
          width: 70px;
          font-size: 16px;
          color: #1890ff;
          border: 1px solid #91d5ff;
        }
      }
    }
  }
}
//目的地
.destination-cards {
  display: grid;//网格分布
  grid-template-columns: repeat(2, 1fr);
  gap: 15px;//间隙

  .destination-card {
    background-color: #fff;
    border-radius: 12px;
    overflow: hidden;
    //来给阴影
    box-shadow: 0 3px 10px rgba(0, 0, 0, 0.08);

    .card-header {
      display: flex;
      justify-content: space-between;//主轴上均匀分布
      padding: 12px;

      .card-title {
        font-size: 18px;
        color: #666;
      }

      .card-tag {
        background-color: #f0f9ff;
        color: #8cdeb9;
        font-size: 12px;
        padding: 2px 8px;
        border-radius: 10px;
        border: 1px solid #d0f0e0;
      }
    }

    .destination-image {
      width: 100%;
      height: 150px;
      object-fit: cover;
      border-radius: 20px;
    }

    .card-content {
      padding: 12px;

      .destination-title {
        font-size: 18px;
        font-weight: bold;
        margin-bottom: 12px;
        line-height: 1.3;
      }

      .user-info {
        display: flex;
        align-items: center;
        font-size: 12px;
        color: #888;

        .small-avatar {
          width: 24px;
          height: 24px;
          border-radius: 50%;
          margin-right: 6px;
          object-fit: cover;//图片美观
        }

        .likes {
          margin-left: auto;
          display: flex;
          align-items: center;
          color: #666;

          .van-icon {
            margin-right: 4px;
            transition: color 0.3s;
          }
        }
      }
    }
  }
}
</style>