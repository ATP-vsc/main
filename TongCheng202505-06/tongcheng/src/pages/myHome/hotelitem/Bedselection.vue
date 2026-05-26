<template>
    <div class="main">
        <!-- 动态显示酒店名称 -->
        <Header :title="hotelName"></Header>
        
        <!-- 根据酒店ID显示不同内容 -->
        <template v-if="hotelId === 1">
            <div class="jpg">
                <img src="../../../assets/qzjd1.jpg" alt="泉州酒店">
            </div>
            <!-- 第二张图作为购买二 -->
            <div class="jpg clickable" @click="clickBuy(1, 1)">
                <img src="../../../assets/qzjd2.jpg" alt="购买一">
            </div>
            <!-- 第三张图作为购买一 -->
            <div class="jpg clickable" @click="clickBuy(1, 2)">
                <img src="../../../assets/qzjd3.jpg" alt="购买二">
            </div>
            <!-- 更多内容 -->
        </template>

        <template v-else-if="hotelId === 2">
            <div class="jpg">
                <img src="@/assets/llmlms1.jpg" alt="邻里沐里民宿">
            </div>
            <!-- 第二张图作为购买一 -->
             <div class="jpg clickable" @click="clickBuy(2, 1)">
                <img src="@/assets/llmlms2.jpg" alt="购买一">
            </div>
            <!-- 第三张图作为购买二 -->
           <div class="jpg clickable" @click="clickBuy(2, 2)">
                <img src="@/assets/llmlms3.jpg" alt="购买二">
            </div>
            <!-- 更多内容 -->
        </template>

           <template v-else-if="hotelId === 3">
            <div class="jpg">
                <img src="@/assets/xyqsby1.jpg" alt="兮遇轻奢别院">
            </div>
            <!-- 第二张图作为购买一 -->
             <div class="jpg clickable" @click="clickBuy(3, 1)">
                <img src="@/assets/xyqsby2.jpg" alt="购买一">
            </div>
            <!-- 第三张图作为购买二 -->
           <div class="jpg clickable" @click="clickBuy(3, 2)">
                <img src="@/assets/xyqsby3.jpg" alt="购买二">
            </div>
            <!-- 更多内容 -->
        </template>
   <template v-else-if="hotelId === 4">
            <div class="jpg">
                <img src="@/assets/tfyms1.jpg" alt="听风驿民宿">
            </div>
            <!-- 第二张图作为购买一 -->
             <div class="jpg clickable" @click="clickBuy(4, 1)">
                <img src="@/assets/tfyms2.jpg" alt="购买一">
            </div>
            <!-- 第三张图作为购买二 -->
           <div class="jpg clickable" @click="clickBuy(4, 2)">
                <img src="@/assets/tfyms3.jpg" alt="购买二">
            </div>
            <!-- 更多内容 -->
        </template>
        
      <template v-else-if="hotelId === 5">
            <div class="jpg">
                <img src="@/assets/xingyu1.jpg" alt="星遇民宿">
            </div>
            <!-- 第二张图作为购买一 -->
             <div class="jpg clickable" @click="clickBuy(5, 1)">
                <img src="@/assets/xingyu2.jpg" alt="购买一 ">
            </div>
            <!-- 第三张图作为购买二 -->
           <div class="jpg clickable" @click="clickBuy(5, 2)">
                <img src="@/assets/xingyu3.jpg" alt="购买二">
            </div>
            <!-- 更多内容 -->
        </template>
   <template v-else-if="hotelId === 6">
            <div class="jpg">
                <img src="@/assets/tkzcjd1.jpg" alt="天空之城酒店">
            </div>
            <!-- 第二张图作为购买一 -->
             <div class="jpg clickable" @click="clickBuy(6, 1)">
                <img src="@/assets/tkzcjd2.jpg" alt="购买一">
            </div>
            <!-- 第三张图作为购买二 -->
           <div class="jpg clickable" @click="clickBuy(6, 2)">
                <img src="@/assets/tkzcjd3.jpg"  alt="购买二">
            </div>
            <!-- 更多内容 -->
        </template>
   <template v-else-if="hotelId === 7">
            <div class="jpg">
                <img src="@/assets/xfl1.jpg" alt="向风旅">
            </div>
            <!-- 第二张图作为购买一 -->
             <div class="jpg clickable" @click="clickBuy(2, 1)">
                <img src="@/assets/xfl2.jpg" alt="购买一 ">
            </div>
            <!-- 第三张图作为购买二 -->
           <div class="jpg clickable" @click="clickBuy(2, 2)">
                <img src="@/assets/xfl3.jpg" alt="购买二">
            </div>
            <!-- 更多内容 -->
        </template>
   <template v-else-if="hotelId === 8">
            <div class="jpg">
                <img src="@/assets/xjms1.jpg" alt="喜见民宿">
            </div>
            <!-- 第二张图作为购买一 -->
             <div class="jpg clickable" @click="clickBuy(8, 1)">
                <img src="@/assets/xjms2.jpg" alt="购买一">
            </div>
            <!-- 第三张图作为购买二 -->
           <div class="jpg clickable" @click="clickBuy(8, 2)">
                <img src="@/assets/xjms3.jpg" alt="购买二">
            </div>
            <!-- 更多内容 -->
        </template>

        
    </div>
</template>

<script>
import Header from '@/components/Header.vue'
import { useStore } from 'vuex';
import { useRouter } from 'vue-router';
import { ref, onMounted } from 'vue';

export default {
    components: { Header },
    setup() {
        const store = useStore();
        const router = useRouter();
        const hotelId = ref(null);
        const hotelName = ref('民宿详情');
        
        // 酒店名称映射
        const hotelNames = {
            1: '泉州酒店',
            2: '邻里沐里民宿',
            3: '兮遇轻奢别院',
            4: '听风驿民宿',
            5: '星遇民宿',
            6: '天空之城酒店',
            7: '向风旅',
            8: '喜见民宿'
        };

        onMounted(() => {
            // 从路由参数获取酒店ID
            hotelId.value = parseInt(router.currentRoute.value.query.hotelId);
            hotelName.value = hotelNames[hotelId.value] || '民宿详情';
        });

        const clickBuy = (hotelId, roomType) => {
            // 根据酒店ID和房型生成商品信息
            const roomData = generateRoomData(hotelId, roomType);
            
            // 直接结算
            store.commit('PAY', [roomData]);
            
            // 跳转到订单页
            router.push("/order");
        };

        // 生成商品数据
        const generateRoomData = (hotelId, roomType) => {
            // 酒店房型数据映射
            const rooms = {
                1: {
                    1: { title: "泉州酒店-东辉楼标准大床房", price: 486 },
                    2: { title: "泉州酒店-高级商务双人房", price: 569 }
                  },
                2: {
                    1: { title: "邻里沐里-夏屿春", price: 116},
                    2: { title: "邻里沐里-拾光&榻榻米", price: 125}
                },
                3: {
                    1: { title: "兮遇-风雅轩学子房", price: 219 },
                    2: { title: "兮遇-亲子房", price: 242 }
                },
                4: {
                    1: { title: "听风驿-阳台投影大床房", price: 159 },
                    2: { title: "听风驿-投影大床房", price: 157 }
                }, 
                5: {
                    1: { title: "星遇-遇-投影双床房", price: 256 },
                    2: { title: "星遇-温馨家庭房", price: 251 }
                }, 
                6: {
                    1: { title: "天空之城-典雅中式双床房", price: 171 },
                    2: { title: "天空之城-中式投影大床房", price: 171 }
                },
                7: {
                    1: { title: "向风旅-暮烟里·捌", price: 240 },
                    2: { title: "向风旅-西岭雪·贰", price: 165 }
                }, 
                8: {
                    1: { title: "喜见-云素大床房", price: 83 },
                    2: { title: "喜见-影视云谊房", price: 109 }
                },
            };

            return {
                id: Date.now(),
                title: rooms[hotelId][roomType].title,
                price: rooms[hotelId][roomType].price,
                num: 1,
                router: `/bedselection`,
                pic: require(`@/assets/hotel-${hotelId}-${roomType}.jpg`) // 添加房型后缀
            };
        };

        return { clickBuy, hotelId, hotelName };
    }
}
</script>

<style lang="less" scoped>
.main {
  background: linear-gradient(to bottom, #8cdeb9, #ffffff);// 渐变背景
  min-height: 100vh;
 
  
  .jpg {
    margin: 0 auto 20px;
    max-width: 1200px;
  
    border-radius: 15px;
    overflow: hidden;
  

    // 房型图片（购买二）
    &:nth-child(2) {
      margin-bottom: 0;
    }
    
    // 购买按钮图片（购买一）
    &:last-child {
      margin-top: -20px;

    }
    
    img {
      display: block;
      width: 100%;
      height: auto;
      transition: transform 0.3s;
    }
  }
}
</style>