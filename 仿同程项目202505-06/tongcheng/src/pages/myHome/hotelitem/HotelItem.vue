<template>
  <div class="hotel-item" @click="goToBedSelection">
    <img :src="hotel.img" alt="酒店图片" class="hotel-img" />
    <div class="hotel-details">
      <h2 class="hotel-name">{{ hotel.name }}</h2>
      <div class="rating">
        <span class="star">{{ hotel.rating }}</span>
        <span class="desc">"{{ hotel.desc }}"</span>
        <span class="reviews">{{ hotel.reviews }}点评</span>
      </div>
      <div class="distance">{{ hotel.distance }}</div>
      <div class="facilities">
        <span v-for="(facility, idx) in hotel.facilities" :key="idx">{{ facility }} </span>
      </div>
      <div class="price">
        <div class="price-left">
          <span class="original">¥{{ hotel.originalPrice }}</span>
          <span class="current">¥{{ hotel.currentPrice }}起</span>
        </div>
        <span class="discount">已优惠{{ hotel.originalPrice - hotel.currentPrice }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
const props = defineProps({ // 添加 const props = 
  hotel: {
    type: Object,
    required: true,
    validator: (h) => h.id !== undefined
  },
});
const router = useRouter();

const goToBedSelection = () => {
  router.push({
    name: 'Bedselection',
    query: { hotelId: props.hotel.id }, // 通过 props.hotel 传递酒店ID
  });
};
</script>

<style scoped>
.hotel-item {
  display: flex;
  gap: 20px;

  margin-bottom: 25px;
  border-radius: 15px;
  background: linear-gradient(to right, #ffffff, #f0faf5);
  box-shadow: 0 5px 15px rgba(140, 222, 185, 0.3);
  transition: all 0.3s ease;
  border: 1px solid #d1f0e0;
}

.hotel-item:hover {
  transform: translateY(-5px);
}

.hotel-img {
  width: 280px;
  height: 320px;
  object-fit: cover;
  border-radius: 12px;
  border: 2px solid #d1f0e0;
}

.hotel-details {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.hotel-name {
  margin: 0;
  font-size: 38px;
  color: #0a5f44;
  position: relative;
  padding-bottom: 10px;
}

.hotel-name::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 80px;
  height: 3px;
  background: #8cdeb9;
  border-radius: 2px;
}

.rating {
  display: flex;
  align-items: center;
  gap: 8px;
  margin: 12px 0;
  font-size: 30px;
}

.star {
  background-color: #ffd700;
  font-weight: bold;
  padding: 2px 8px;
  border-radius: 5px;
}

.desc {
  color: #2c7a5a;
  font-size: 28px;
  font-weight: bold;
}

.reviews {
  color: #666;
  font-size: 22px;
  margin-left: 10px;
}

.distance {
  color: #2c7a5a;
  margin: 10px 0;
  font-size: 26px;
  display: flex;
  align-items: center;
  gap: 5px;
}

.facilities {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin: 12px 0;
  font-size: 22px;
}

.facilities span {
  background: #e8f8f0;
  padding: 5px 12px;
  border-radius: 20px;
  color: #0a5f44;
  border: 1px solid #b8e8d3;
}

.price {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 15px;
}

.price-left {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
}

.original {
  text-decoration: line-through;
  color: #999;
  font-size: 24px;
}

.current {
  color: #e74c3c;
  font-weight: bold;
  font-size: 32px;
  margin-bottom: 5px;
}

.discount {
  background: #ffebee;
  color: #e74c3c;
  font-size: 22px;
  padding: 3px 10px;
  border-radius: 15px;
  font-weight: bold;
}
</style>