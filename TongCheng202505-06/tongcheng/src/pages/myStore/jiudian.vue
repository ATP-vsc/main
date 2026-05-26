<template>
  <div class="main">
    <Header title="泉州酒店"></Header>
    <div class="hotel-page">
      <!-- 顶部筛选栏 -->
      <div class="filter-bar">
        <input type="text" placeholder="搜索城市/酒店" class="search-input" />
        <div class="date-person"> 
        </div>
        <div class="sort-filters">
          <button class="sort-btn">智能排序</button>
          <button class="sort-btn">价格/星级</button>
          <button class="sort-btn">筛选</button>
          <button class="sort-btn active">西街</button>
        </div>
      </div>

      <!-- 酒店列表 -->
      <div class="hotel-list">
        <HotelItem v-for="hotel in hotels" :key="hotel.id" :hotel="hotel" />
      </div>
    </div>
  </div>

</template>
<script>
import HotelItem from '@/pages/myHome/hotelitem/HotelItem.vue'
import Header from '@/components/Header.vue'
export default {
  components: {
    HotelItem,
    Header
  },
  data() {
    return {
      hotels: [
        {
          id: 1,
          img: require("@/assets/qzjd.jpeg"),
          name: '泉州酒店',
          rating: 4.8,
          desc: '老城中心看闽南特色建筑',
          reviews: 8365,
          distance: '距泉州西街直线698米',
          facilities: ['免费水果', '免费停车', '洗衣服务', 'spa按摩'],
          originalPrice: 530,
          currentPrice: 514,
        },
        {
          id: 2,
          img: require("@/assets/llmlms.jpg"),
          name: '邻里沐里民宿(泉州西街开元寺店)',
          rating: 4.9,
          desc: '房间设计精美,服务热情周到',
          reviews: 220,
          distance: '距泉州西街直线470米',
          facilities: ['停车场', '洗衣房', '充电桩', '影音房'],
          originalPrice: 192,
          currentPrice: 108,
        },
        {
          id: 3,
          img: require("@/assets/xyqsbd.jpg"),
          name: '兮遇轻奢别院(泉州西街开元寺店)',
          rating: 4.8,
          desc: '私享200m2的无敌观景大...',
          reviews: 844,
          distance: '距泉州西街直线215米',
          facilities: ['免费停车', '洗衣服务', '充电桩', '影音房', '浴缸'],
          originalPrice: 339,
          currentPrice: 154,
        },
        {
          id: 4,
          img: require("@/assets/tfyms.jpg"),
          name: '听风驿民宿(泉州西街开元寺店)',
          rating: 4.9,
          desc: '房间整洁舒适,设施齐全温馨',
          reviews: 208,
          distance: '距泉州西街直线306米',
          facilities: ['洗衣房', '影音房', '浴缸'],
          originalPrice: 200,
          currentPrice: 143,
        },
        {
          id: 5,
          img: require("@/assets/xyms.jpg"),
          name: '星遇民宿',
          rating: 4.9,
          desc: '露台景色优美，适合泡茶休闲',
          reviews: 921,
          distance: '距泉州西街直线383米',
          facilities: ['洗衣房', '影音房'],
          originalPrice: 317,
          currentPrice: 174,
        },
        {
          id: 6,
          img: require("@/assets/tkzcjd.jpg"),
          name: '天空之城酒店(西街)',
          rating: 4.6,
          desc: '窗外绿树成荫,巨幕投影观...',
          reviews: 1656,
          distance: '距泉州西街直线754米',
          facilities: ['免费停车', '影音房', '影音房'],
          originalPrice: 286,
          currentPrice: 156,
        },
        {
          id: 7,
          img: require("@/assets/xfl.jpg"),
          name: '向风旅(西街开元寺店)',
          rating: 4.9,
          desc: '房间设计简约清新,温馨舒适',
          reviews: 154,
          distance: '距泉州西街直线410米',
          facilities: ['洗衣服务', '影音房'],
          originalPrice: 286,
          currentPrice: 165,
        },
        {
          id: 8,
          img: require("@/assets/xjms.jpg"),
          name: '喜见民宿(开元寺西街店)',
          rating: 4.9,
          desc: '热情管家服务,家庭影院体验',
          reviews: 343,
          distance: '距泉州西街直线306米',
          facilities: ['洗衣房', '影音房', '浴缸'],
          originalPrice: 169,
          currentPrice: 107,
        }
      ]
    };
  }
};

</script>
<style lang='less' scoped>
.hotel-page {
  font-family: Arial, sans-serif; // 酒店页面文本使用 Arial 或无衬线字体 
  padding: 20px;
}

.filter-bar {
  display: flex;
  flex-direction: column; // 垂直布局（搜索框在上，下方内容在下）
  gap: 10px; // 搜索框与下方的间距
  margin-bottom: 20px;
}

.search-input {
  width: 97%; // 占满宽度
  padding: 8px;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  flex: 1;
  margin-right: 10px;
  font-size: 30px;
}

.date-person {
  display: flex;
  gap: 10px;
}

.sort-filters {
  display: flex;
  font-size: 30px;
  gap: 10px;
}

.sort-btn {
  padding: 6px 12px;
  border: none;
  background-color: #f5f5f5;
  border-radius: 4px;
  cursor: pointer;
}

.sort-btn.active {
  background-color: #e0e0e0;
}

.hotel-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}
</style>